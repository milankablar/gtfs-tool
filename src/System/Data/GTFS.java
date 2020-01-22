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
package System.Data;


import System.GUI.Observer;
import System.GUI.Subject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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


    /**
     * Default constructor for GTFS class
     * Initializes routes, trips, stops and stopTimes HashMap and ArrayList of Observers
     */
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
	 * Search for a stop by stop_id and display all route_id(s) that contain the stop
	 * @param stopId stop id
	 * @return list of routes that contain the stop associated with the stop id
	 * @author Melissa Lin
	 */
	public List<Route> searchForStopDisplayRoutes (String stopId) {
		List<Route> routesList = new ArrayList<>();
		// handles invalid stop id by displaying error message to user
		if (stops.getStopsMap().containsKey(stopId)) {
			// gets all the stop times associated with stop id user has entered
			List<StopTime> stopTimesOnStop = stopTimes.searchByStop(stopId);
			for (StopTime stopTime : stopTimesOnStop) {
				if (stopTime.getStopId().equals(stopId)) {
					Trip trip = trips.getTripsMap().get(stopTime.getTripId());
					// eliminates duplicate routes
					if (!routesList.contains(routes.getRoutesMap().get(trip.getRouteId()))) {
						routesList.add(routes.getRoutesMap().get(trip.getRouteId()));
					}
				}
			}
		} else {
			throw new NullPointerException("The Stop ID you have entered could not be found or is invalid.");
		}
		return routesList;
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


	/*
	 * This method takes in a stop ID and uses it to find all the Trips
	 * after the given time when given a stop ID.
	 * @author: Jack Haek
	 * @param stopID the stop ID you wish to search for
	 */
	public List<String> findTripsAfterCurrentTime(String routeId){
		List<Trip> tripsList = trips.searchByRouteID(routeId);
		List<String> tripIDs = new LinkedList<>();
		for(Trip trip : tripsList){
			List<StopTime> stopsList = stopTimes.searchByTrip(trip.getTripId());
			int maxTime = Integer.MAX_VALUE;
			String fullTime = "00:00:00";
			for (StopTime stopTime: stopsList) {
				String[] time = stopTime.getArrivalTime().split(":");
				if(Integer.parseInt(time[0]) < maxTime){
					maxTime = Integer.parseInt(time[0]);
					fullTime = stopTime.getArrivalTime();
				}
			}
			if(!existsIn(tripIDs, trip.getTripId()) && timeCheck(fullTime)){
				//add to the list
				tripIDs.add(trip.getTripId());
			}
		}
		return tripIDs;
	}

	/*
	 * Helper method that checks if the given time is past the current time.
	 * The time needs to be given in the format HH:mm:ss otherwise it will
	 * not work
	 * @author: Jack Haek
	 * @param time the time that needs to be checked
	 * @return boolean returns false if the time is before the current time and true if it
	 * is after
	 */
	private boolean timeCheck(String time){
		//return true if after the specified time and false if before
		String[] currentTime = currentTime().split(":");
		String[] testTime = time.split(":");
		if(Integer.parseInt(currentTime[0]) > Integer.parseInt(testTime[0])){
			//the current times has already passed the testTime
			return false;
		} else if(Integer.parseInt(currentTime[0]) == Integer.parseInt(testTime[0])){
			//not sure if before or after must go deeper
			if(Integer.parseInt(currentTime[1]) > Integer.parseInt(testTime[1])){
				return false;
			} else if(Integer.parseInt(currentTime[1]) == Integer.parseInt(testTime[1])){
				//must look @ seconds
				if(Integer.parseInt(currentTime[2]) >= Integer.parseInt(testTime[2])){
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		} else {
			//must mean that the testTime is greater than current time and thus hasn't passed yet
			return true;
		}

	}

	/*
	 * Helper method designed to check if there is a string contained in a list already
	 * @author: Jack Haek
	 * @param String string to be checked
	 * @param List<String> list that will be checked
	 * @return boolean based on if it is found in the list. True if found in list. False
	 * otherwise
	 */
	private boolean existsIn(List<String> list, String string){
		for (String listString: list) {
			if(string.equals(listString)){
				return true;
			}
		}
		return false;
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
	 * Method which returns list of String stop ids on route
	 * @param routeId String route id
	 * @return String List of stop ids
     * @author Milan Kablar
	 */
	public List<String> getStopIdsOnRoute(String routeId) {
		List<String> stopIds = new ArrayList<>();
		for (Map.Entry<String, LinkedList<StopTime>> entry : stopTimes.stopTimesMap.entrySet()) {
			String tripId = entry.getKey();
			if (trips.getTripsMap().get(tripId).getRouteId().equals(routeId)) {
				for (StopTime stopTime : entry.getValue()) {
					if (!stopIds.contains(stopTime.getStopId())) {
						stopIds.add(stopTime.getStopId());
					}
				}
			}
		}
		return stopIds;
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

	/**
	 * Method for displaying the number of trips on stops
	 * @param stopID the stop ID to display the number of trips for
	 * @return the size of the tripsId list or the number of trips on the stop
	 * @author James Lang
	 */
	public String numberOfTripsOnStops(String stopId) {
		List<StopTime> stopTimesUsingStop = stopTimes.searchByStop(stopId);
		List<String> tripIds = new ArrayList<String>();
		for(StopTime stopTime : stopTimesUsingStop) {
			String tripId = stopTime.getTripId();
			if(!tripIds.contains(tripId)) {
				tripIds.add(tripId);
			}
		}
		return String.valueOf(tripIds.size());
	}
}