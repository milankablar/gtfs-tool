/*
 * Course: SE2030 - 031
 * Fall 2019
 * Lab 8 - Issue Tracking
 * Name: Group H (Hayden Klein, Milan Kablar, Jack Haek, Melissa Lin, James Lang)
 * Created: 10/13/2019
 */
package System.DataStructures;


import System.GUI.Observer;
import System.GUI.Subject;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author James lang
 * @version 1.0
 * @created 10-Oct-2019 1:40:39 PM
 */
public class GTFS implements Subject {


	private Routes routes;
	private Trips trips;
	private StopTimes stopTimes;
	private Stops stops;
	private List<Observer> observers;


	public GTFS(){
			routes = new Routes();
			stops = new Stops();
			trips = new Trips();
			stopTimes = new StopTimes();
			this.observers = new ArrayList<Observer>();


	}

	/**
	 * Method that takes in file objects for each data structure and calls individual import methods
	 * @param routeFile the file containing route information
	 * @param tripsFile the file containing trip information
	 * @param stopTimesFile the fine containing StopTime information
	 * @param stopsFile the file containing Stop information
	 * @return a string that indicated if files had issues, and if so which ones
	 * @author James Lang
	 */

	public String importData(File routeFile, File tripsFile, File stopTimesFile, File stopsFile) throws IOException {
		String b1 = "error-free";
		String b2 = "error-free";
		String b3 = "error-free";
		String b4 = "error-free";
		String returnString;

		if (routeFile!=null) {
			b1 = routes.importFile(routeFile.toPath());
		}if (tripsFile!=null) {
			b2 = trips.importFile(tripsFile.toPath());
		}if (stopsFile!=null) {
			b3 = stops.importFile(stopsFile.toPath());
		}if (stopTimesFile!=null) {
			b4 = stopTimes.importFile(stopTimesFile.toPath());
		}

		if(!b1.equals("error-free")) {
			returnString = b1;
		} else if (!b2.equals("error-free")) {
			returnString = b2;
		} else if (!b3.equals("error-free")) {
			returnString = b3;
		} else if (!b4.equals("error-free")) {
			returnString = b4;
		} else if(routeFile == null && tripsFile == null && stopTimesFile == null && stopsFile == null){
			returnString = "No files uploaded";

		}else {
			returnString = "Data upload successful!";
		}

		notifyObservers();
		return returnString;
	}





	/**
	 * Method calculates distance between two stops given stopIds
	 * @param stopId1 a stop id to find the distance from
	 * @param stopId2 a stop id to find the distance to
	 * @return distance between stop 1 and 2
	 * @author https://www.geodatasource.com/developers/java
	 */
	public double calculateDistanceBetweenStops(String stopId1, String stopId2){
		Stop stop1 = stops.searchByStop(stopId1);
		Stop stop2 = stops.searchByStop(stopId2);
		double lat1 = Double.parseDouble(stop1.getStopLat());
		double lat2 = Double.parseDouble(stop2.getStopLat());
		double lon1 = Double.parseDouble(stop1.getStopLon());
		double lon2 = Double.parseDouble(stop2.getStopLon());

		if ((lat1 == lat2) && (lon1 == lon2)) {
			return 0;
		}
		else {
			double theta = lon1 - lon2;
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			return (dist);
		}
	}


	/**
	 * Method calculates distance of a trip given the tripId
	 * Calls calculateDistanceBetweenStops()
	 * @param tripId String trip id
	 * @return total distance of trip
	 * @author Milan Kablar
	 */
	public double calculateTripDistance(String tripId) throws IllegalArgumentException{
		List<StopTime> stopTimesInTrip = stopTimes.searchByTrip(tripId);
		if(stopTimesInTrip.size() < 2) {
			throw new IllegalArgumentException();
		}
		List<StopTime> sortedStopTimes = new ArrayList<StopTime>();
		StopTime addToList;
		while(stopTimesInTrip.size() > 0) {
			addToList = stopTimesInTrip.get(0);
			for (StopTime stopTime : stopTimesInTrip) {
				if (timeConvert(stopTime.getArrivalTime()) < timeConvert(addToList.getArrivalTime())) {
					addToList = stopTime;
				}
			}
			stopTimesInTrip.remove(addToList);
			sortedStopTimes.add(addToList);
		}
		double distanceTotal = 0;
		StopTime prevStopTime = null;
		StopTime stopTime = null;
		Iterator iterator = sortedStopTimes.iterator();
		while(iterator.hasNext()) {
			if(prevStopTime == null) {
				prevStopTime = (StopTime)iterator.next();
				stopTime = (StopTime)iterator.next();
				distanceTotal += calculateDistanceBetweenStops(prevStopTime.getStopId(), stopTime.getStopId());
			} else {
				prevStopTime = stopTime;
				stopTime = (StopTime)iterator.next();
				distanceTotal += calculateDistanceBetweenStops(prevStopTime.getStopId(), stopTime.getStopId());
			}

		}
			return distanceTotal;
	}


	/**
	 * Method returns the current time as HH:MM:SS
	 * @return formatted current time
	 * @author James Lang
	 **/
	public String currentTime(){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}




	/**
	 * Method calls exportFile() in the Routes class given a path
	 * @param path export path for routes.txt
	 * @author Hayden Klein
	 */
	public boolean exportRoutes(Path path){

		return routes.exportFile(path);
	}

	/**
	 * Method calls exportFile() in the Stops class given a path
	 * @param path export path for stops.txt
	 * @author Hayden Klein
	 */
	public boolean exportStops(Path path){

		return stops.exportFile(path);
	}

	/**
	 * Method calls exportFile() in the StopTimes class given a path
	 * @param path export path for stop_times.txt
	 * @author Hayden Klein
	 */
	public boolean exportStopTimes(Path path){

		return stopTimes.exportFile(path);
	}

	/**
	 * Method calls exportFile() in the Trips class given a path
	 * @param path export path for trips.txt
	 * @author Hayden Klein
	 */
	public boolean exportTrips(Path path){

		return trips.exportFile(path);
	}

	/**
	 * toString Method for all stops
	 * @return String of all stops
	 * @author Hayden Klein
	 */
	public String stopsToString(){
		return stops.stopsToString();
	}

	/**
	 * toString Method for all trips
	 * @return String of all trips
	 * @author Hayden Klein
	 */
	public String tripsToString(){
		return trips.tripsToString();
	}

	/**
	 * toString Method for all routes
	 * @return String of all routes
	 * @author Hayden Klein
	 */
	public String routesToString(){
		return routes.routesToString();
	}

	/**
	 * toString Method for all stopTimes when exporting
	 * @return String of stopTimes for exporting
	 * @author Hayden Klein
	 */
	public String stopTimesToStringExport(){
		return stopTimes.stopTimesExportString();
	}

	/**
	 * toString Method for all stopTimes when displaying
	 * @return String of stopTimes for displaying
	 * @author Hayden Klein
	 */
	public String stopTimesToStringDisplay(){
		return stopTimes.stopTimesDisplayString();
	}

	/**
	 * Method calculate and return average speed
	 * Divides the total distance of the trip by the total time of the trip.
	 * @param tripId This is the trip that the average speed will be calculated over
	 * @return the average speed over the given trip. It will return -1 if there is not a valid tripID
	 * @author Jack Haek
	 */
	public double calculateAverageSpeed(String tripId) {
		if(!trips.getTripsMap().containsKey(tripId)) {
			throw new NullPointerException();
		}

		List<StopTime> stops = stopTimes.searchByTrip(tripId);
		//get distance
		double distance = calculateTripDistance(tripId);
		//get time
		if(stops.size() != 0) {
			int startTime; //holds start time in seconds
			int endTime; //holds end time in seconds
			List<StopTime> sortedStopTimes = new ArrayList<StopTime>();
			StopTime addToList;
			while (stops.size() > 0) {
				addToList = stops.get(0);
				for (StopTime stopTime : stops) {
					if (timeConvert(stopTime.getArrivalTime()) < timeConvert(addToList.getArrivalTime())) {
						addToList = stopTime;
					}
				}
				stops.remove(addToList);
				sortedStopTimes.add(addToList);
			}
			startTime = timeConvert(sortedStopTimes.get(0).getArrivalTime());
			endTime = timeConvert(sortedStopTimes.get(sortedStopTimes.size() - 1).getArrivalTime());
			double timeElapsed = endTime - startTime;
			timeElapsed = timeElapsed / 3600.0; //gives time elapsed in hours
			return distance / timeElapsed;
		} else {
			return -1;
		}
	}


	/**
	 * Accessor method for routes
	 * @return Routes object
	 * @author Milan Kablar
	 */
	public Routes getRoutes(){
		return routes;
	}


	/**
	 * Accessor method for stops
	 * @return Stops object
	 * @author Milan Kablar
	 */
	public Stops getStops(){
		return stops;
	}


	/**
	 * Accessor method for stopTimes
	 * @return stopTimes object
	 * @author Milan Kablar
	 */
	public StopTimes getStopTimes(){
		return stopTimes;
	}



	/**
	 * Accessor method for trips
	 * @return Trips object
	 * @author Milan Kablar
	 */
	public Trips getTrips(){
		return trips;
	}

	/**
	 * Method to calculate and get the next trips going to a stop
	 * @param stopId the stop that the user wants to look for the next trip arriving at
	 * @return the next trip arriving at a stop
	 * @author James Lang
	 */
	public List<StopTime> getNextTripsGoingToStopId(String stopId) {
		if(!stops.getStopsMap().containsKey(stopId)) {
			throw new NullPointerException("The Stop ID you have entered could not be found or is invalid.");
		}

		List<StopTime> stopTimesOnStop = stopTimes.searchByStop(stopId);
		if (stopTimesOnStop.isEmpty()) {
			throw new NullPointerException("The	Stop ID for the stop you have entered does" +
					"not have any trips happening in the future.");
		}
		int timeInt = timeConvert(currentTime());
		List<StopTime> currentClosest = new ArrayList<StopTime>();
		currentClosest.add(stopTimesOnStop.get(0));
		for(StopTime stopTime : stopTimesOnStop) {
			if((timeConvert(stopTime.getArrivalTime()) - timeInt) < (timeConvert(currentClosest.get(0).getArrivalTime()) - timeInt)) {
				if (timeConvert(stopTime.getArrivalTime()) > timeInt) {
					currentClosest.clear();
					currentClosest.add(stopTime);
				}
			} else if ((timeConvert(stopTime.getArrivalTime()) - timeInt) == (timeConvert(currentClosest.get(0).getArrivalTime()) - timeInt)) {
				if(currentClosest.get(0) != stopTime) {
					currentClosest.add(stopTime);
				}
			}
		}
		return currentClosest;
	}

	/**
	 * Helper method for converting given formatted time string to seconds integer
	 * @param timeStr formatted time string
	 * @return seconds as integer
	 * @author James Lang
	 */
	private int timeConvert(String timeStr) {
		String[] temp = timeStr.split(":");
		int hr = Integer.parseInt(temp[0]);
		int min = Integer.parseInt(temp[1]);
		int sec = Integer.parseInt(temp[2]);
		return (hr * 3600) + (min * 60) + sec;
	}

	/**
	 * Override method for adding observer objects to list of observers
	 * @author Hayden Klein
	 */
	@Override
	public void register(Observer obj) {
		if (obj!= null){
			if (!observers.contains(obj)){
				observers.add(obj);
			}

		}

	}

	/**
	 * Override method for deleting observer objects to list of observers
	 * @author Hayden Klein
	 */
	@Override
	public void unregister(Observer obj) {
		observers.remove(obj);
	}

	/**
	 * Override method for notifying observers
	 * @author Hayden Klein
	 */
	@Override
	public void notifyObservers() {
		for (Observer observer : observers){
			observer.update(this);
		}
	}
}