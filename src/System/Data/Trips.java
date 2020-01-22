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


import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Melissa Lin
 * @version 1.0
 * @created 10-Oct-2019 1:41:24 PM
 */
public class Trips {

	public HashMap<String, Trip> tripsMap;
	public ArrayList tripsList;
	public ArrayList<String> headerList;
	public final ArrayList<String> sampleHeaderList = new ArrayList<>(Arrays.asList("route_id",
			"service_id", "trip_id", "trip_headsign", "trip_short_name", "direction_id",
			"block_id", "shape_id", "wheelchair_accessible", "bikes_allowed"));

	/**
	 * Default constructor for Trips class
	 * Initializes trips HashMap and ArrayList
	 */
	public Trips() {
		tripsMap = new HashMap<String, Trip>();
		tripsList = new ArrayList();
	}

	public HashMap<String, Trip> getTripsMap() {
		return tripsMap;
	}

	/**
	 * This method takes in the path to the trips.txt file for the GTFS
	 * data structure and returns true if the import was a success. Otherwise
	 * it will return false
	 * @param path to the trips.txt
	 * @author Jack Haek, Melissa Lin, Milan Kablar
	 */
	public String importFile(Path path) throws IOException {
		Scanner tripScanner = new Scanner(path.toFile());
		tripsMap = new HashMap<String, Trip>();
		String headerLine = tripScanner.nextLine();

		headerList = validateHeader(headerLine);
		if(headerList.size() == 0) {
			return "trips.txt header line is invalid.";
		}

		while (tripScanner.hasNextLine()) {
			String currentLine = tripScanner.nextLine();

			ArrayList<String> tripList = validateDataLine(currentLine,headerList);
			if(tripList.size() == 0) {
				tripsMap = new HashMap<String, Trip>();
				return "One or more trips.txt data lines is invalid.";
			} else {
				Trip trip = new Trip(tripList.get(0), tripList.get(1),
						tripList.get(2), tripList.get(3),
						tripList.get(4), tripList.get(5),
						tripList.get(6), tripList.get(7),
						tripList.get(8), tripList.get(9));
				tripsMap.put(tripList.get(2), trip);
			}
		}
		return "error-free";
	}

	/**
	 * Method which exports trips data as file by calling tripsToString()
	 * @param path output file path
	 * @return true or false if file was successfully exported
	 * @author Hayden Klein
	 */
	public boolean exportFile(Path path){
		try {
			PrintWriter printWriter = new PrintWriter(path.toFile().getName());
			printWriter.write(tripsToString());
			printWriter.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Method which searches through trips map with trip id
	 * @param tripId trip id the user will input
	 * @return returns the trip with the given ID
	 * @author Jack Haek
	 */
	public Trip searchByTrip(String tripId){
		return tripsMap.get(tripId);
	}

	/**
	 * Searches for all trips contained in a route
	 * @param routeID the given ID
	 * @return a list of trips contained in a route
	 * @author Jack Haek
	 */
	public List<Trip> searchByRouteID(String routeID){
		Iterator<Map.Entry<String, Trip>> iterator = tripsMap.entrySet().iterator();
		List<Trip> tripList = new ArrayList<>();
		while(iterator.hasNext()){
			Trip currentEntry = iterator.next().getValue();
			if(currentEntry.getRouteId().equals(routeID)){
				tripList.add(currentEntry);
			}
		}
		return tripList;
	}

	/**
	 * Helper method that takes in header line as a string and returns a parsed array if valid
	 * If invalid, returns empty array
	 * @param headerLine first line of trips.txt
	 * @return array of attribute values parsed at comma
	 * @author Milan Kablar
	 */
	public ArrayList<String> validateHeader(String headerLine) {
		boolean flag = false;
		headerList = new ArrayList<>(Arrays.asList(headerLine.split(",")));
		for(String headerItem : headerList) {
			for(String sampleItem : sampleHeaderList) {
				if(headerItem.equals(sampleItem)) {
					flag = true;
				}
			}
			if(!flag) {
				flag = false;
				return new ArrayList<>();
			}
		}
		return headerList;
	}

	/**
	 * Helper method that takes in data line as a string and returns a parsed array if valid
	 * If invalid, returns empty array
	 * @param dataLine String line
	 * @param headerList ArrayList of parsed headerLine
	 * @return array of attribute values parsed at comma
	 * @author Milan Kablar
	 */
	public ArrayList<String> validateDataLine(String dataLine,ArrayList<String> headerList) {
		try {
			String[] splitArray = dataLine.split(",");

			ArrayList<String> temp = new ArrayList<>();
			temp.addAll(Arrays.asList(splitArray));

			ArrayList<String> checkedArray = checkForQuotations(temp);

			ArrayList<String> sortedArray = sortArray(checkedArray, headerList);

			if (sortedArray.get(0).equals(" ") || sortedArray.get(2).equals(" ")) {
				return new ArrayList<>();
			}

			return sortedArray;
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	/**
	 * Helper method that takes in the split array and adjusts it if there commas between quotations.
	 * @param array comma split array
	 * @return adjusted array
	 * @author Milan Kablar
	 */
	private ArrayList<String> checkForQuotations(ArrayList<String> array) {
		ArrayList<String> newArray = new ArrayList<>();
		int i = 0;
		while(i < array.size())
			if(!(array.get(i).startsWith("\"")) || (array.get(i).startsWith("\"") && array.get(i).endsWith("\""))) {
				newArray.add(array.get(i));
				i++;
			} else {
				boolean breakFlag = false;
				String str = array.get(i);
				i++;
				while(!breakFlag && i < array.size()) {
					if(array.get(i).endsWith("\"")) {
						str += "," + array.get(i);
						newArray.add(str);
						breakFlag = true;
					} else {
						str += "," + array.get(i);
					}
					i++;
				}
			}
		return newArray;
	}

	/**
	 * Helper method that sorts the attributes passed in and puts them in order to conform to the constructor
	 * If no attribute is found from the file, the index should be empty/null
	 * @param unsortedArray array of attributes unsorted
	 * @return array list with the sorted attributes
	 * @author Hayden Klein, Melissa Lin
	 */
	public ArrayList<String> sortArray(ArrayList<String> unsortedArray, ArrayList<String> headerList){
		ArrayList<String> sortedArray = new ArrayList<>(10);
		for (int k = 0; k < 10; k++){
			sortedArray.add(" ");
		}
		for (int i =0; i<unsortedArray.size();i++){
			if (headerList.get(i).equals("route_id")){
				sortedArray.set(0,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("service_id")){
				sortedArray.set(1,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("trip_id")){
				sortedArray.set(2,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("trip_headsign")){
				sortedArray.set(3,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("trip_short_name")){
				sortedArray.set(4,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("direction_id")){
				sortedArray.set(5,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("block_id")){
				sortedArray.set(6,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("shape_id")){
				sortedArray.set(7,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("wheelchair_accessible")){
				sortedArray.set(8,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("bikes_allowed")){
				sortedArray.set(9,unsortedArray.get(i));
			}
		}

		for (int i = 0; i < 10; i++){
			if(sortedArray.get(i).equals("")) {
				sortedArray.set(i, " ");
			}
		}

		return sortedArray;
	}

	/**
	 * toString method which loops through tripsMap and calls toString for each stop
	 * @return String of all trip attributes
	 * @author Hayden Klein
	 */
	String tripsToString(){
		StringBuilder returningString = new StringBuilder("route_id,service_id,trip_id,trip_headsign,stop_sequence,trip_short_name," +
				"direction_id,block_id,shape_id,wheel_chair_access,bikes_allowed"+"\n");
		for (Object entry : tripsMap.entrySet()){
			String[] temparray = entry.toString().split("=");

			returningString.append(temparray[1]).append("\n");
		}
		return returningString.toString();
	}
}