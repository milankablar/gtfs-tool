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


import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Melissa Lin
 * @version 1.0
 * @created 10-Oct-2019 1:40:59 PM
 */
public class Routes {



	public HashMap<String, Route> routesMap;
	public ArrayList<String> routesList;
	public ArrayList<String> headerList;
	public final ArrayList<String> sampleHeaderList = new ArrayList<>(Arrays.asList("route_id",
            "agency_id", "route_short_name", "route_long_name", "route_desc", "route_type", "route_url",
            "route_color", "route_text_color", "route_sort_order"));

    /**
     * Default constructor for Routes class
	 * Initializes routes HashMap and ArrayList
     */
	public Routes() {
        routesMap = new HashMap<String, Route>();
        routesList = new ArrayList();
	}

	public HashMap<String, Route> getRoutesMap() {
		return routesMap;
	}

	/**
	 * Method takes in the path to the routes.txt file for the GTFS
	 * data structure and returns true if the import was a success. Otherwise
	 * it will return false
	 * @author Jack Haek, Melissa Lin, Milan Kablar
	 * @param path to the routes.txt
	 */
	public String importFile(Path path) throws IOException {
		Scanner routesScanner = new Scanner(path.toFile());
		routesMap = new HashMap<String, Route>();

		String headerLine = routesScanner.nextLine();

		headerList = validateHeader(headerLine);
		if(headerList.size() == 0) {
			return "routes.txt header line is invalid.";
		}

		while (routesScanner.hasNextLine()) {
			String currentLine = routesScanner.nextLine();

			ArrayList<String> routeList = validateDataLine(currentLine, headerList);
			if(routeList.size() == 0) {
				routesMap = new HashMap<String, Route>();
				return "One or more routes.txt data lines is invalid.";
			} else {
				Route route = new Route(routeList.get(0), routeList.get(1),
						routeList.get(2), routeList.get(3),
						routeList.get(4), routeList.get(5), routeList.get(6),
						routeList.get(7), routeList.get(8), routeList.get(9));

				routesMap.put(routeList.get(0), route);
			}
		}
		return "error-free";
	}

	/**
	 * Method which exports routes data as file by calling routesToString()
	 * @param path output file path
	 * @return true or false if file was successfully exported
	 * @author Hayden Klein
	 */
	public boolean exportFile(Path path){
		try {
			PrintWriter printWriter = new PrintWriter(path.toFile().getName());
			printWriter.write(routesToString());
			printWriter.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}



	/**
	 * Helper method that takes in header line as a string and returns a parsed array if validate
	 * If invalid, returns empty array
	 * @param headerLine first line of routes.txt
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

			if (sortedArray.get(0).equals(" ") || sortedArray.get(5).equals(" ")) {
				return new ArrayList<>();
			}

			if(!sortedArray.get(7).trim().equals("")){
				Color.web("#" + sortedArray.get(7));
			}
			if(!sortedArray.get(8).trim().equals("")){
				Color.web("#" + sortedArray.get(8));
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
		for (int i = 0; i < unsortedArray.size(); i++){
			if (headerList.get(i).equals("route_id")){
				sortedArray.set(0,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("agency_id")){
				sortedArray.set(1,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("route_short_name")){
				sortedArray.set(2,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("route_long_name")){
				sortedArray.set(3,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("route_desc")){
				sortedArray.set(4,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("route_type")){
				sortedArray.set(5,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("route_url")){
				sortedArray.set(6,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("route_color")){
				sortedArray.set(7,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("route_text_color")){
				sortedArray.set(8,unsortedArray.get(i));
			}
			if (headerList.get(i).equals("route_sort_order")){
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
	 * toString method which loops through routesMap and calls toString for each route
	 * @return String of all route attributes
	 * @author Hayden Klein
	 */
	public String routesToString(){
		StringBuilder returningString = new StringBuilder("route_id,agency_id,route_short_name,route_long_name,route_desc," +
				"route_type,route_url,route_color,route_text_color,route_sort_order"+"\n");
		for (Object entry : routesMap.entrySet()){
			String[] tempArray = entry.toString().split("=");

			returningString.append(tempArray[1]).append("\n");
		}
		return returningString.toString();
	}
}