/*
 * Course: SE2030 - 031
 * Fall 2019
 * Lab 8 - Issue Tracking
 * Name: Group H (Hayden Klein, Milan Kablar, Jack Haek, Melissa Lin, James Lang)
 * Created: 10/13/2019
 */
package System.DataStructures;


import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Melissa Lin
 * @version 1.0
 * @created 10-Oct-2019 1:41:04 PM
 */
public class Stops {

	private HashMap<String, Stop> stopsMap;
	public ArrayList stopsList;
	private ArrayList<String> headerList;
	private final ArrayList<String> sampleHeaderList = new ArrayList<>(Arrays.asList("stop_id",
			"stop_code", "stop_name", "stop_desc", "stop_lat", "stop_lon",
			"zone_id", "stop_url", "location_type", "parent_station", "stop_timezone",
			"wheelchair_boarding", "level_id", "platform_code"));

	/**
	 * Default constructor for Stops class
	 * Initializes stops HashMap and ArrayList
	 */
	public Stops() {
		stopsMap = new HashMap<String, Stop>();
		stopsList = new ArrayList();
	}

	public HashMap getStopsMap() {
		return stopsMap;
	}

	/**
	 * This method takes in the path to the stops.txt file for the GTFS
	 * data structure and returns true if the import was a success. Otherwise
	 * it will return false
	 * @author Jack Haek, Melissa Lin, Milan Kablar
	 * @param path to the stops.txt
	 */
	public String importFile(Path path) throws IOException {
		Scanner stopsScanner = new Scanner(path.toFile());
		stopsMap = new HashMap<String, Stop>();

		String headerLine = stopsScanner.nextLine();

		headerList = validateHeader(headerLine);
		if(headerList.size() == 0) {
			return "stops.txt header line is invalid";
		}

		// loops through each line of the text and create a stop object
		while (stopsScanner.hasNextLine()) {
			String currentLine = stopsScanner.nextLine();

			ArrayList<String> stopList = validateDataLine(currentLine, headerList);
			if(stopList.size() == 0) {
				stopsMap = new HashMap<String, Stop>();
				return "One or more stops.txt data lines is invalid.";
			} else {
				Stop stop = new Stop(stopList.get(0), stopList.get(1),
						stopList.get(2), stopList.get(3),
						stopList.get(4), stopList.get(5),
						stopList.get(6), stopList.get(7), stopList.get(8),
						stopList.get(9), stopList.get(10), stopList.get(11),
						stopList.get(12), stopList.get(13));
				// stop id, stop object
				stopsMap.put(stopList.get(0), stop);
			}
		}
		// later when validation is implemented it should deal with the true or false returned
		return "error-free";
	}

	/**
	 * Method which exports stops data as file by calling stopsToString()
	 * @param path output file path
	 * @return true or false if file was successfully exported
	 * @author Hayden Klein
	 */
	public boolean exportFile(Path path){
		try {
			PrintWriter printWriter = new PrintWriter(path.toFile().getName());
			printWriter.write(stopsToString());
			printWriter.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Method which searches through stops map by using the stop id passed in
	 * @param stopId stop id the user will input
	 * @author Jack Haek
	 */
	public Stop searchByStop(String stopId){
		return stopsMap.get(stopId);
	}

	/**
	 * Helper method that takes in header line as a string and returns a parsed array if valid
	 * If invalid, returns empty array
	 * @param headerLine first line of stop.txt
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
	public ArrayList<String> validateDataLine(String dataLine, ArrayList<String> headerList) {
		try {
			String[] splitArray = dataLine.split(",");

			ArrayList<String> temp = new ArrayList<>();
			temp.addAll(Arrays.asList(splitArray));

			ArrayList<String> checkedArray = checkForQuotations(temp);

			ArrayList<String> sortedArray = sortArray(checkedArray, headerList);

			if (sortedArray.get(0).equals(" ")) {
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
	 * @param headerList first line of stops.txt
	 * @return array list with the sorted attributes
	 * @author Melissa Lin , Hayden Klein
	 */
	public ArrayList<String> sortArray(ArrayList<String> unsortedArray,ArrayList<String> headerList) {

		ArrayList<String> sortedArray = new ArrayList<>(14);
		for (int k = 0; k < 14; k++) {
			sortedArray.add(" ");
		}
		// iterates for the amount of attributes in the header passed in
		for (int i = 0; i < headerList.size(); i++) {
			// if attribute is found in array, store at it's corresponding index
			if (headerList.get(i).equals("stop_id")) {
				sortedArray.set(0, unsortedArray.get(i));
			}
			if (headerList.get(i).equals("stop_code")) {
				sortedArray.set(1, unsortedArray.get(i));
			}
			if (headerList.get(i).equals("stop_name")) {
				sortedArray.set(2, unsortedArray.get(i));
			}
			if (headerList.get(i).equals("stop_desc")) {
				sortedArray.set(3, unsortedArray.get(i));
			}
			if (headerList.get(i).equals("stop_lat")) {
				sortedArray.set(4, unsortedArray.get(i));
			}
			if (headerList.get(i).equals("stop_lon")) {
				sortedArray.set(5, unsortedArray.get(i));
			}
			if (headerList.get(i).equals("zone_id")) {
				sortedArray.set(6, unsortedArray.get(i));
			}
			if (headerList.get(i).equals("stop_url")) {
				sortedArray.set(7, unsortedArray.get(i));
			}
			if (headerList.get(i).equals("location_type")) {
				sortedArray.set(8, unsortedArray.get(i));
			}
			if (headerList.get(i).equals("parent_station")) {
				sortedArray.set(9, unsortedArray.get(i));
			}
			if (headerList.get(i).equals("stop_timezone")) {
				sortedArray.set(10, unsortedArray.get(i));
			}
			if (headerList.get(i).equals("wheelchair_boarding")) {
				sortedArray.set(11, unsortedArray.get(i));
			}
			if (headerList.get(i).equals("level_id")) {
				sortedArray.set(12, unsortedArray.get(i));
			}
			if (headerList.get(i).equals("platform_code")) {
				sortedArray.set(13, unsortedArray.get(i));
			}
		}

		for (int i = 0; i < 14; i++){
			if(sortedArray.get(i).equals("")) {
				sortedArray.set(i, " ");
			}
		}

		return sortedArray;
	}

	/**
	 * toString method which loops through stopsMap and calls toString for each stop
	 * @return String of all stop attributes
	 * @author Hayden Klein
	 */
	public String stopsToString(){
		StringBuilder returningString = new StringBuilder("stop_id,stop_code,stop_name,stop_desc,stop_lat,stop_lon,zone_id,stop_url," +
				"location_type,parent_station,stop_timezone,wheelchair_boarding,level_id,platform_code" + "\n");
		for (Object entry : stopsMap.entrySet()){
			String[] temparray = entry.toString().split("=");

			returningString.append(temparray[1]).append("\n");
		}
		return returningString.toString();
	}


}