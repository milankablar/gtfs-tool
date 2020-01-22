/*
 * Copyright 2019 Milan Kablar, Hayden Klein, Melissa Lin, James Lang, Jack Haek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Course: SE2030 - 031
 * Fall 2019
 * Lab 9 - Implementation lab
 * Name: Group H (Hayden Klein, Milan Kablar, Jack Haek, Melissa Lin, James Lang)
 * Created: 10/13/2019
 */
package System.GUI;

import System.Data.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author James Lang
 * @version 1.0
 * @created 10-Oct-2019 1:40:49 PM
 */
public class MainController implements Observer {


	public Menu displayMenu;
	public Menu displayExportMenu;
	public Menu updateMenu;
	private GTFS gtfs = new GTFS();

	@FXML
	private TextArea stopsTextArea = new TextArea();
	@FXML
	private TextArea stopTimesArea = new TextArea();
	@FXML
	private TextArea tripsArea = new TextArea();
	@FXML
	private TextArea routesArea = new TextArea();
	@FXML
	private TextArea miscArea = new TextArea();


	/**
	 * Method which opens FileChooser and allows user to select all of the GTFS files
	 * Adds MainController as obserser
	 * Calls notifyObserver()
	 * @param event action event from JavaFX
	 * @author James Lang with updates by Jack Haek
	 * @JackHaek notes - used https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm for reference
	 * for the directory chooser code.
	 */
	public void importData(ActionEvent event) {

		gtfs.register(this);
		try {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directoryChooser.setTitle("Choose directory of GTFS files");
			File directory = directoryChooser.showDialog(null);
			String directoryPath = null;
			if (!directory.exists()) {
			    throw new NullPointerException("You have not selected the files to be imported.");
            }
			if (directory != null) {
				directoryPath = directory.getAbsolutePath();
			}
			if (directoryPath != null) {
				File routesFile = Paths.get(directoryPath + "\\routes.txt").toFile();
				File tripsFile = Paths.get(directoryPath + "\\trips.txt").toFile();
				File stopTimesFile = Paths.get(directoryPath + "\\stop_times.txt").toFile();
				File stopsFile = Paths.get(directoryPath + "\\stops.txt").toFile();
				String response = gtfs.importData(routesFile, tripsFile, stopTimesFile, stopsFile);
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("File Import Status");
				alert.setContentText(response);
				alert.showAndWait();
				displayMenu.setVisible(true);
				displayExportMenu.setVisible(true);
				updateMenu.setVisible(true);
			}
			gtfs.notifyObservers();
		} catch (NullPointerException e) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Import Error");
            a.setContentText("You have not selected the files to be imported.");
            a.showAndWait();
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Import Error");
            a.setContentText("The file that was chosen cannot be imported. Please try again.");
            a.showAndWait();
        }
	}

	/**
	 * Method which opens inputDialog to enter filename
	 * and calls exportStops() in GTFS
	 * @param event action event from JavaFX
	 * @throws IOException exception pass through
	 * @author Hayden Klein
	 */
	public void exportStops(ActionEvent event) throws IOException {

			Path file = Paths.get("stops.txt");
			boolean conform = gtfs.exportStops(file);
			if (conform){
				stopsTextArea.setText("File Exported Correctly");
			}else stopsTextArea.setText("File Exported Incorrectly");
	}



	/**
	 * Method which opens inputDialog to enter filename
	 * and calls exportStopTimes() in GTFS
	 * @param event action event from JavaFX
	 * @throws IOException exception pass through
	 * @author Hayden Klein
	 */
	public void exportStopTimes(ActionEvent event){

			Path file = Paths.get("stop_times.txt");
			boolean conform = gtfs.exportStopTimes(file);
			if (conform){
				stopTimesArea.setText("File Exported Correctly");
			}else stopTimesArea.setText("File Exported Incorrectly");
	}

	/**
	 * Method which opens inputDialog to enter filename
	 * and calls exportTrips() in GTFS
	 * @param event action event from JavaFX
	 * @throws IOException exception pass through
	 * @author Hayden Klein
	 */
	public void exportTrips(ActionEvent event){

			Path file = Paths.get("trips.txt");
			boolean conform = gtfs.exportTrips(file);
			if (conform){
				tripsArea.setText("File Exported Correctly");
			}else tripsArea.setText("File Exported Incorrectly");
		}

	/**
	 * Method which opens inputDialog to enter filename
	 * and calls exportRoutes() in GTFS
	 * @param event action event from JavaFX
	 * @throws IOException exception pass through
	 * @author Hayden Klein
	 */
	public void exportRoutes(ActionEvent event){

			Path file = Paths.get("routes.txt");
			boolean conform = gtfs.exportRoutes(file);
			if (conform){
				routesArea.setText("File Exported Correctly");
			}else routesArea.setText("File Exported Incorrectly");
	}
	/**
	 * Method which displays average speed in mph of a trip for a given trip id
	 * @author James Lang
	 */
	public void displayAverageSpeed(){
		try {
			miscArea.clear();
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("Average Speed of a Trip");
			dialog.setHeaderText("Trip Id");
			dialog.setContentText("Please enter the trip id that you would like to find the average speed of");
			Optional<String> result = dialog.showAndWait();
			if (result.equals("-1 miles")) {
				miscArea.clear();
			} else if (result.isPresent()) {
				String newResult = result.get();
				miscArea.setText(String.valueOf(gtfs.calculateAverageSpeed(newResult)) + " miles per hour");
			}
		}catch (NullPointerException e){
			miscArea.setText("-1");

			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setHeaderText("Invalid Trip ID");
			a.setContentText("The Trip ID you have entered could not be found or is invalid.");
			a.showAndWait();
		}
	}

	/**
	 * Method which displays the next trips go to a stop for a given stop id
	 * @author James Lang
	 */
	public void displayNextTripsGoingToStopId(){
		try {
			miscArea.clear();
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("Get Next Trips Going To Stop");
			dialog.setHeaderText("Stop ID");
			dialog.setContentText("Please enter the Stop ID for which you want to find the next trip(s) for");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				String newResult = result.get();
				List<StopTime> listToDisplay = gtfs.getNextTripsGoingToStopId(newResult);
				StringBuilder display = new StringBuilder("Next Trip(s) Arriving:");
				for (StopTime stopTime : listToDisplay) {
					display.append("\n").append(stopTime.getTripId());
				}
				miscArea.setText(display.toString());
			}
		} catch (NullPointerException e) {
			miscArea.setText("-1");
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setHeaderText("Display Next Arrival at Stop Error");
			a.setContentText(e.getMessage());
			a.showAndWait();
		}
	}

    /**
     * displays routes based off of the stop id the user enters
     * should properly handle errors of incorrect stop id or non-existent
     * @author Melissa Lin
     */
    public void displayRoutesBasedOffStopId() {
        try {
            miscArea.clear();
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Routes Associated With Stop ID");
            dialog.setHeaderText("Stop ID");
            dialog.setContentText("Please enter a Stop ID.");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String newResult = result.get();
                List<Route> routesList = (gtfs.searchForStopDisplayRoutes(newResult));
                StringBuilder display = new StringBuilder("Route(s) that contain the Stop ID you've entered: ");
                for (int i = 0; i < routesList.size(); i++) {
                    // get route id
                    display.append("\n").append(routesList.get(i).getRouteId());
                }
                miscArea.setText(display.toString());
            } else {
                throw new NullPointerException("You did not enter a Stop ID!");
            }
        } catch (NullPointerException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Error!");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }

	/**
	 * Method which displays distance of a trip in miles for a given trip id
	 * @author James Lang
	 */
	public void displayTripDistance() {
		try {
			miscArea.clear();
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("Distance Of Trip");
			dialog.setHeaderText("Distance");
			dialog.setContentText("Please enter a trip ID.");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				String newResult = result.get();
				double distance = (gtfs.calculateTripDistance(newResult));
				if (distance > 0.0) {
					miscArea.setText(distance + " miles");
				} else {
					miscArea.setText("0.0");
					Alert a = new Alert(Alert.AlertType.ERROR);
					a.setHeaderText("Invalid Trip ID");
					a.setContentText("The Trip ID you have entered could not be found or is invalid.");
					a.showAndWait();
				}
			}
		} catch (IllegalArgumentException e) {
			Alert a = new Alert(Alert.AlertType.WARNING);
			a.setHeaderText("Warning!");
			a.setContentText("The distance could not be calculated because the Trip ID " +
					"you have entered has one or fewer stops from the list of trips.");
			a.showAndWait();
		}
	}

    /**
     * Method which displays all the stops on a route in the miscArea
     * @param actionEvent ActionEvent object
     * @author Hayden Klein and Milan Kablar
     */
	public void displayStopIdsOnRoute(ActionEvent actionEvent){
	    miscArea.clear();
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Route Stops");
        dialog.setHeaderText("Stops on a Route");
        dialog.setContentText("Please enter a route ID.");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            if(gtfs.getRoutes().getRoutesMap().containsKey(result.get())) {
                StringBuilder displayString = new StringBuilder();
                List<String> stops = gtfs.getStopIdsOnRoute(result.get());
                for (String stopId : stops) {
                    displayString.append(stopId).append("\n");
                }
                miscArea.setText("Stops on the Route\n"+displayString.toString()); }
            else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Invalid Route ID");
                a.setContentText("The Route ID you have entered could not be found or is invalid.");
                a.showAndWait();
            }
        }
    }

    /**
     * Method which displays the number of trips on all stops to the miscArea
     * @param actionEvent ActionEvent object
     * @author James Lang
     */
    public void displayNumberOfTripsOnStops(ActionEvent actionEvent) {
        miscArea.clear();
        StringBuilder displayStr = new StringBuilder();
        List<Stop> stops = gtfs.getStops().stopsList;
        for(Stop stop : stops) {
            String stopId = stop.getStopId();
            displayStr.append(stopId + ":" + gtfs.numberOfTripsOnStops(stopId) + "\n");
        }
        miscArea.setText(displayStr.toString());
    }

    /*
     * This method takes in a stop ID and uses it to find all the Trips
     * after the given time when given a stop ID.
     * @author: Jack Haek
     * @param stopID the stop ID you wish to search for
     */
    public List<String> findTripsAfterCurrentTime(String routeId){
        return gtfs.findTripsAfterCurrentTime(routeId);
    }

    /**
     * Displays the results of findsTripsAfterCurrentTime
     * @author Jack Haek
     * @param event
     */
    public void displayTripsAfterCurrentTime(ActionEvent event){
        miscArea.clear();
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Trips on a Route");
        dialog.setHeaderText("Trips after the Current Time on a Route");
        dialog.setContentText("Please enter a Route ID.");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            if (gtfs.getRoutes().getRoutesMap().containsKey(result.get())){
                List list = findTripsAfterCurrentTime(result.get());
                StringBuilder display = new StringBuilder();
                for (Object o : list) {
                    display.append("\n").append(o);
                }
                miscArea.setText("Trips after the Current Time\n"+display.toString());
            }
        }else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Invalid Route ID");
            a.setContentText("The Route ID you have entered could not be found or is invalid.");
            a.showAndWait();
        }
    }
    /**
     * Method updates and of the attributes of a route
     * @author Hayden Klein and Milan Kablar
     */
    public void updateRouteAttribute() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Route that is being changed");
        dialog.setHeaderText("Enter the ID of the Route you'd like to change");
        dialog.setContentText("Route ID");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            Map<String, Route> routesMap = gtfs.getRoutes().routesMap;
            if (routesMap.containsKey(result.get())) {
                Route route = routesMap.get(result.get());
                List<String> headerList = gtfs.getRoutes().sampleHeaderList;
                ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("route_id", headerList);
                choiceDialog.setTitle("Attribute Choice");
                choiceDialog.setHeaderText("Choose and attribute to change");
                choiceDialog.setContentText("Attribute");
                Optional<String> choiceResult = choiceDialog.showAndWait();
                if (choiceResult.isPresent()) {
                    String choice = choiceResult.get();
                    if (choice.equals("route_id")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Route ID");
                        if (changeResult.isPresent()) {
                            String convert = changeResult.get();
                            route.setRouteId(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("agency_id")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Agency ID");
                        if (changeResult.isPresent()) {
                            String convert = changeResult.get();
                            route.setAgencyId(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("route_short_name")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Route Short Name");
                        if (changeResult.isPresent()) {
                            String convert = changeResult.get();
                            route.setRouteShortName(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("route_long_name")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Route Long Name");
                        if (changeResult.isPresent()) {
                            String convert = changeResult.get();
                            route.setRouteLongName(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("route_desc")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Route Description");
                        if (changeResult.isPresent()) {
                            String convert = changeResult.get();
                            route.setRouteDesc(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("route_type")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Route Type");
                        if (changeResult.isPresent()) {
                            if (Integer.parseInt(changeResult.get()) ==0 || Integer.parseInt(changeResult.get()) ==1 || Integer.parseInt(changeResult.get()) ==2 ||Integer.parseInt(changeResult.get()) ==3 ||
                                    Integer.parseInt(changeResult.get()) ==4 ||Integer.parseInt(changeResult.get()) ==5 ||Integer.parseInt(changeResult.get()) ==6 ||Integer.parseInt(changeResult.get()) ==7) {
                                route.setRouteType(changeResult.get());
                                clearAndNotify();
                            } else {
                                invalidChangeAlert();
                            }
                        }
                    }
                    if (choice.equals("route_url")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Route URL");
                        if (changeResult.isPresent()) {
                            String convert = changeResult.get();
                            route.setRouteUrl(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("route_color")) {
                        Optional<String> changeResult = attributeChangeTextDialog(
                                "Enter your change for Route Color");
                        if (changeResult.isPresent()) {
                            String convert = changeResult.get();
                            Pattern pattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
                            String convertWithHash = "#"+convert;
                            if (pattern.matcher(convertWithHash).matches()) {
                                route.setRouteColor(convert);
                                clearAndNotify();
                            } else {
                                invalidChangeAlert();
                            }
                        }
                    }
                    if (choice.equals("route_text_color")) {
                        Optional<String> changeResult = attributeChangeTextDialog(
                                "Enter your change for Route Text Color");
                        if (changeResult.isPresent()) {
                            String convert = changeResult.get();
                            Pattern pattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
                            String convertWithHash = "#"+convert;
                            if (pattern.matcher(convertWithHash).matches()) {
                                route.setRouteTextColor(convert);
                                clearAndNotify();
                            } else {
                                invalidChangeAlert();
                            }
                        }
                    }
                    if (choice.equals("route_sort_order")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Route Sort Order");
                        if (changeResult.isPresent()) {
                            int convert = Integer.parseInt(changeResult.get());
                            if (convert > 0) {
                                route.setRouteSortOrder(Integer.toString(convert));
                                clearAndNotify();
                            } else {
                                invalidChangeAlert();
                            }
                        }
                    }

                }
            } else {
                doesNotExistError();
            }
        }
    }

    /**
     * Method updates any of the attributes of a trip
     * @author Hayden Klein and Milan Kablar
     */
    public void updateTripAttribute() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Trip that is being changed");
        dialog.setHeaderText("Enter the ID of the Trip you'd like to change");
        dialog.setContentText("Trip ID");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            Map<String, Trip> tripMap = gtfs.getTrips().tripsMap;
            if (tripMap.containsKey(result.get())) {
                Trip trip = tripMap.get(result.get());
                List<String> headerList = gtfs.getTrips().sampleHeaderList;
                ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("route_id", headerList);
                choiceDialog.setTitle("Attribute Choice");
                choiceDialog.setHeaderText("Choose and attribute to change");
                choiceDialog.setContentText("Attribute");
                Optional<String> choiceResult = choiceDialog.showAndWait();
                if (choiceResult.isPresent()) {
                    String choice = choiceResult.get();
                    if (choice.equals("route_id")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Route ID");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            if (gtfs.getRoutes().routesMap.containsKey(convert)){
                                trip.setRouteId(convert);
                                clearAndNotify();
                            }
                        }
                    }
                    if (choice.equals("service_id")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Service ID");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            trip.setServiceId(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("trip_id")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Trip ID");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            trip.setTripId(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("trip_headsign")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Trip Headsign");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            trip.setTripHeadsign(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("trip_short_name")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Trip Short Name");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            trip.setTripShortName(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("direction_id")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Direction ID");
                        if (changeResult.isPresent()){
                            int convert = Integer.parseInt(changeResult.get());
                            if (convert == 0 || convert == 1){
                                trip.setDirectionId(String.valueOf(convert));
                                clearAndNotify();
                            }else {
                                invalidChangeAlert();
                            }
                        }
                    }
                    if (choice.equals("block_id")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Block ID");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            trip.setBlockId(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("shape_id")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Shape ID");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            trip.setShapeId(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("wheelchair_accessible")) {
                        Optional<String> changeResult = attributeChangeTextDialog(
                                "Enter your change for Wheelchair Accessibility");
                        if (changeResult.isPresent()){
                            int convert = Integer.parseInt(changeResult.get());
                            if (convert == 0 || convert == 1 || convert == 2){
                                trip.setWheelchairAccessible(String.valueOf(convert));
                                clearAndNotify();
                            }else {
                                invalidChangeAlert();
                            }
                        }
                    }
                    if (choice.equals("bikes_allowed")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Bike Allowably");
                        if (changeResult.isPresent()){
                            int convert = Integer.parseInt(changeResult.get());
                            if (convert == 0 || convert == 1 || convert == 2){
                                trip.setWheelchairAccessible(String.valueOf(convert));
                                clearAndNotify();
                            }else {
                                invalidChangeAlert();
                            }
                        }

                    }
                }
            } else {
                doesNotExistError();
            }
        }
    }

    /**
     * Method updates any of the attributes of a StopTime
     * @author Hayden Klein and Milan Kablar
     */
    public void updateStopTimeAttribute() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Stop Time that is being changed");
        dialog.setHeaderText("Enter the Trip ID and the Stop ID of the Stop Time " + "\n" +
                "you'd like to change, separated by a comma");
        dialog.setContentText("Trip ID, Stop ID");
        StopTime stopTime = null;
        boolean confirmation = false;
        Optional<String> result = dialog.showAndWait();
        try {

            if (result.isPresent()) {
                Map<String, LinkedList<StopTime>> stopTimeMap = gtfs.getStopTimes().stopTimesMap;
                String[] arrays = result.get().split(",");
                if (stopTimeMap.containsKey(arrays[0])) {
                    for (Map.Entry<String, LinkedList<StopTime>> entry : stopTimeMap.entrySet()) {
                        for (StopTime StopTime : entry.getValue()) {
                            if (StopTime.getStopId().equals(arrays[1])) {
                                stopTime = StopTime;
                                confirmation = true;
                            }
                        }
                    }
                    if (!confirmation){
                        throw new NullPointerException();
                    }
                    List<String> headerList = gtfs.getStopTimes().sampleHeaderList;
                    ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("trip_id", headerList);
                    choiceDialog.setTitle("Attribute Choice");
                    choiceDialog.setHeaderText("Choose and attribute to change");
                    choiceDialog.setContentText("Attribute");
                    Optional<String> choiceResult = choiceDialog.showAndWait();
                    if (choiceResult.isPresent()) {
                        String choice = choiceResult.get();
                        if (choice.equals("trip_id")) {
                            Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Trip ID");
                            if (changeResult.isPresent()) {
                                String convert = changeResult.get();
                                stopTime.setTripId(convert);
                                clearAndNotify();
                            }
                        }
                        if (choice.equals("arrival_time")) {
                            Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Arrival Time");
                            if (changeResult.isPresent()) {
                                String convert = changeResult.get();
                                String departureTime = stopTime.getDepartureTime();
                                String[] convertArray = convert.split(":");
                                String[] departureArray = departureTime.split(":");
                                if (convertArray.length == 3) {
                                    if (Integer.parseInt(convertArray[0])<=23 && Integer.parseInt(convertArray[1]) <=59 && Integer.parseInt(convertArray[2]) <=59 && Integer.parseInt(convertArray[0])>=0 &&
                                    Integer.parseInt(convertArray[1])>=0 && Integer.parseInt(convertArray[2])>=0) {
                                        int convertSeconds = Integer.parseInt(convertArray[0]) * 3600 + Integer.parseInt(convertArray[1]) * 60 + Integer.parseInt(convertArray[2]);
                                        int departureSeconds = Integer.parseInt(departureArray[0]) * 3600 + Integer.parseInt(departureArray[1]) * 60 + Integer.parseInt(departureArray[2]);
                                            if (convertSeconds<departureSeconds){
                                                stopTime.setArrivalTime(convert);
                                                clearAndNotify();
                                            }else {
                                                invalidChangeAlert();
                                            }
                                    }else {
                                        invalidChangeAlert();
                                    }
                                } else {
                                    invalidChangeAlert();
                                }
                            }
                        }
                        if (choice.equals("departure_time")) {
                            Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Departure Time");
                            if (changeResult.isPresent()) {
                                String convert = changeResult.get();
                                String arrivalTime = stopTime.getArrivalTime();
                                String[] convertArray = convert.split(":");
                                String[] arrivalArray = arrivalTime.split(":");
                                if (convertArray.length == 3) {
                                    if (Integer.parseInt(convertArray[0])<=23 && Integer.parseInt(convertArray[1]) <=59 && Integer.parseInt(convertArray[2]) <=59 && Integer.parseInt(convertArray[0])>=0 &&
                                            Integer.parseInt(convertArray[1])>=0 && Integer.parseInt(convertArray[2])>=0) {
                                        int convertSeconds = Integer.parseInt(convertArray[0]) * 3600 + Integer.parseInt(convertArray[1]) * 60 + Integer.parseInt(convertArray[2]);
                                        int arrivalSeconds = Integer.parseInt(arrivalArray[0]) * 3600 + Integer.parseInt(arrivalArray[1]) * 60 + Integer.parseInt(arrivalArray[2]);
                                        if (convertSeconds>arrivalSeconds){
                                            stopTime.setArrivalTime(convert);
                                            clearAndNotify();
                                        }else {
                                            invalidChangeAlert();
                                        }
                                    }else {
                                        invalidChangeAlert();
                                    }
                                } else {
                                    invalidChangeAlert();
                                }
                            }
                        }
                        if (choice.equals("stop_id")) {
                            Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Stop ID");
                            if (changeResult.isPresent()) {
                                String convert = changeResult.get();
                                if (!gtfs.getStops().stopsMap.containsKey(convert)){
                                    stopTime.setStopId(convert);
                                    clearAndNotify();
                                }
                            }
                        }
                        if (choice.equals("stop_sequence")) {
                            Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Stop Sequence");
                            if (changeResult.isPresent()) {
                                int convert = Integer.parseInt(changeResult.get());
                                if (convert >= 0) {
                                    stopTime.setStopSequence(String.valueOf(convert));
                                    clearAndNotify();
                                }

                            }
                        }
                        if (choice.equals("stop_headsign")) {
                            Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Stop Headsign");
                            if (changeResult.isPresent()) {
                                String convert = changeResult.get();
                                stopTime.setStopHeadsign(convert);
                                clearAndNotify();
                            }
                        }
                        if (choice.equals("pickup_type")) {
                            Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Pickup Type");
                            if (changeResult.isPresent()) {
                                int convert = Integer.parseInt(changeResult.get());
                                if (convert == 0 || convert == 1 || convert == 2 || convert == 3) {
                                    stopTime.setPickupType(String.valueOf(convert));
                                    clearAndNotify();
                                }
                            }
                        }
                        if (choice.equals("drop_off_type")) {
                            Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Drop Off Type");
                            if (changeResult.isPresent()) {
                                int convert = Integer.parseInt(changeResult.get());
                                if (convert == 0 || convert == 1 || convert == 2 || convert == 3) {
                                    stopTime.setDropOffType(String.valueOf(convert));
                                    clearAndNotify();
                                }
                            }
                        }
                        if (choice.equals("shape_dist_traveled")) {
                            Optional<String> changeResult = attributeChangeTextDialog(
                                    "Enter your change for Shape Distance Traveled");
                            if (changeResult.isPresent()) {
                                float convert = Float.parseFloat(changeResult.get());
                                if (convert>=0){
                                    stopTime.setShapeDistTraveled(String.valueOf(convert));
                                    clearAndNotify();
                                }
                            }
                        }
                        if (choice.equals("timepoint")) {
                            Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Time Point");
                            if (changeResult.isPresent()) {
                                int convert = Integer.parseInt(changeResult.get());
                                if (convert == 0 || convert == 1){
                                    stopTime.setTimepoint(String.valueOf(convert));
                                    clearAndNotify();
                                }
                            }
                        }
                    }
                } else {
                    doesNotExistError();
                }
            }
        }catch (NullPointerException e){
            doesNotExistError();
            }
        }

    /**
     * Method updates and of the attributes of a Stop
     * @author Hayden Klein and Milan Kablar
     */
    public void updateStopAttribute() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Stop that is being changed");
        dialog.setHeaderText("Enter the ID of the Stop you'd like to change");
        dialog.setContentText("Stop ID");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            Map<String, Stop> stopMap = gtfs.getStops().stopsMap;
            if (stopMap.containsKey(result.get())) {
                Stop stop = stopMap.get(result.get());
                List<String> headerList = gtfs.getStops().sampleHeaderList;
                ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("stop_id", headerList);
                choiceDialog.setTitle("Attribute Choice");
                choiceDialog.setHeaderText("Choose and attribute to change");
                choiceDialog.setContentText("Attribute");
                Optional<String> choiceResult = choiceDialog.showAndWait();
                if (choiceResult.isPresent()) {
                    String choice = choiceResult.get();
                    if (choice.equals("stop_id")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Stop ID");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            if (!gtfs.getStops().stopsMap.containsKey(convert)){
                                stop.setStopId(convert);
                                clearAndNotify();
                            }
                        }
                    }
                    if (choice.equals("stop_code")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Stop Code");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            stop.setStopCode(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("stop_name")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Stop Name");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            stop.setStopName(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("stop_desc")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Stop Description");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            stop.setStopDesc(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("stop_lat")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Stop Latitude");
                        if (changeResult.isPresent()){
                            double convert = Double.parseDouble(changeResult.get());
                            if (convert<=90.0 && convert>=-90.0){
                                stop.setStopLat(String.valueOf(convert));
                                clearAndNotify();
                            }

                        }
                    }
                    if (choice.equals("stop_lon")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Stop Longitude");
                        if (changeResult.isPresent()){
                            double convert = Double.parseDouble(changeResult.get());
                            if (convert<=180.0 && convert>=-180.0){
                                stop.setStopLon(String.valueOf(convert));
                                clearAndNotify();
                            }
                        }
                    }
                    if (choice.equals("zone_id")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Zone ID");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            stop.setZoneId(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("stop_url")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Stop URL");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            stop.setStopURL(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("location_type")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Location Type");
                        if (changeResult.isPresent()){
                            int convert = Integer.parseInt(changeResult.get());
                            if (convert == 0 || convert == 1 || convert == 2 || convert == 3 || convert == 4){
                                stop.setLocationType(String.valueOf(convert));
                            }
                        }
                    }
                    if (choice.equals("parent_station")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Parent Station");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            if (gtfs.getStops().stopsMap.containsKey(convert) && !(convert.equals(stop.getStopId()))){
                                stop.setParentStation(convert);
                                clearAndNotify();
                            }
                        }
                    }
                    if (choice.equals("stop_timezone")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Stop Timezone");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            stop.setStopTimezone(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("wheelchair_boarding")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Wheelchair Boarding");
                        if (changeResult.isPresent()){
                            int convert = Integer.parseInt(changeResult.get());
                            if (convert == 0 || convert == 1 || convert == 2){
                                stop.setWheelchairBoarding(String.valueOf(convert));
                                clearAndNotify();
                            }
                        }
                    }
                    if (choice.equals("level_id")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Level ID");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            stop.setLevelId(convert);
                            clearAndNotify();
                        }
                    }
                    if (choice.equals("platform_code")) {
                        Optional<String> changeResult = attributeChangeTextDialog("Enter your change for Platform Code");
                        if (changeResult.isPresent()){
                            String convert = changeResult.get();
                            stop.setPlatformCode(convert);
                            clearAndNotify();
                        }
                    }
                }
            } else {
                doesNotExistError();
            }
        }
    }

	/**
	 * Override method that updates TextArea depending on what class it was called from
	 * @param obj Subject object
	 */
	@Override
	public void update(Object obj) {
		if(obj instanceof GTFS) {
			routesArea.clear();
			routesArea.setText(gtfs.routesToString());
			tripsArea.clear();
			tripsArea.setText(gtfs.tripsToString());
			stopsTextArea.clear();
			stopsTextArea.setText(gtfs.stopsToString());
			stopTimesArea.clear();
			stopTimesArea.setText(gtfs.stopTimesToStringDisplay());
		}
		if(obj instanceof Routes) {
			routesArea.clear();
			routesArea.setText(gtfs.routesToString());
		}
		if(obj instanceof Trips) {
			tripsArea.clear();
			tripsArea.setText(gtfs.tripsToString());
		}
		if(obj instanceof Stops) {
			stopsTextArea.clear();
			stopsTextArea.setText(gtfs.stopsToString());
		}
		if(obj instanceof StopTimes) {
			stopTimesArea.clear();
			stopTimesArea.setText(gtfs.stopTimesToStringDisplay());
		}
	}



    /**
     * Helper method which takes in header text,
     * applies it to the template attribute change TextDialog and displays TextDialog
     * @param headerText specific headerText for TextDialog
     * @return Optional object to get input text from
     * @author Milan Kablar
     */
    public Optional<String> attributeChangeTextDialog(String headerText) {
        TextInputDialog attributeDialog = new TextInputDialog();
        attributeDialog.setTitle("Change Attribute");
        attributeDialog.setHeaderText(headerText);
        attributeDialog.setContentText("Attribute Change");
        Optional<String> changeResult = attributeDialog.showAndWait();
        return changeResult;
    }

    /**
     * Helper method which displays Alert when there is an invalid change
     * @author Milan Kablar
     */
    public void invalidChangeAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Change invalid");
        alert.setContentText("Your proposed change is invalid, please try again");
        alert.showAndWait();
    }

    /**
     * Helper method which displays Alert when user inputs invalid ID
     * @author Milan Kablar
     */
    public void doesNotExistError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Does not exist");
        alert.setContentText("The queried ID does not exist");
        alert.showAndWait();
    }

    /**
     * Helper method which clears all TextAreas and notifies Observers when attribute is successfully updated
     * @author Milan Kablar
     */
    public void clearAndNotify(){
        routesArea.clear();
        tripsArea.clear();
        stopTimesArea.clear();
        stopsTextArea.clear();
        gtfs.notifyObservers();
    }



}