/*
 * Course: SE2030 - 031
 * Fall 2019
 * Lab 8 - Issue Tracking
 * Name: Group H (Hayden Klein, Milan Kablar, Jack Haek, Melissa Lin, James Lang)
 * Created: 10/13/2019
 */
package System.DataStructures;


import jdk.jshell.spi.ExecutionControl;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Melissa Lin
 * @version 1.0
 * @created 10-Oct-2019 1:41:12 PM
 */
public class StopTimes {

    private HashMap<String, LinkedList<StopTime>> stopTimesMap;
    public ArrayList stopTimesList;
    private ArrayList<String> headerList;
    private final ArrayList<String> sampleHeaderList = new ArrayList<>(Arrays.asList("trip_id",
            "arrival_time", "departure_time", "stop_id", "stop_sequence", "stop_headsign",
            "pickup_type", "drop_off_type", "shape_dist_traveled", "timepoint"));


    /**
     * Default constructor for StopTimes class
     * Initializes stopTimes HashMap and ArrayList
     */
	public StopTimes(){
		stopTimesMap = new HashMap<String, LinkedList<StopTime>>();
		stopTimesList = new ArrayList();
	}

    public HashMap getStopTimesMap() {
        return stopTimesMap;
    }

    /**
     * This method takes in the path to the stop_times.txt file for the GTFS
     * data structure and returns true if the import was a success. Otherwise
     * it will return false
     * @param path to the stop_times.txt
     * @author Jack Haek, Melissa Lin, Milan Kablar
     */
    public String importFile(Path path) throws IOException {
        Scanner stopTimesScanner = new Scanner(path.toFile());
        stopTimesMap = new HashMap<String, LinkedList<StopTime>>();

        String headerLine = stopTimesScanner.nextLine();

        headerList = validateHeader(headerLine);
        if(headerList.size() == 0) {
            return "stop_times.txt header line is invalid.";
        }

        while (stopTimesScanner.hasNextLine()) {
            String currentLine = stopTimesScanner.nextLine();

            ArrayList<String> stopTimeList = validateDataLine(currentLine,headerList);
            if(stopTimeList.size() == 0) {
                stopTimesMap = new HashMap<String, LinkedList<StopTime>>();
                return "One or more stop_times.txt data lines is invalid.";

            } else {
                // create stopTime object and stores data passed in
                StopTime stopTime = new StopTime(stopTimeList.get(0), stopTimeList.get(1),
                        stopTimeList.get(2), stopTimeList.get(3), stopTimeList.get(4),
                        stopTimeList.get(5), stopTimeList.get(6), stopTimeList.get(7),
                        stopTimeList.get(8), stopTimeList.get(9));
                // stop id, stop time object
                if(!stopTimesMap.containsKey(stopTimeList.get(3))){
                    stopTimesMap.put(stopTimeList.get(3), new LinkedList<StopTime>());
                }
                stopTimesMap.get(stopTimeList.get(3)).add(stopTime);
            }
        }
        return "error-free";
    }

    /**
     * Method which exports stopTimes data as file by calling stopTimesExportString()
     * @param path output file path
     * @return true or false if file was successfully exported
     * @author Hayden Klein
     */
	public boolean exportFile(Path path){
		try {
			PrintWriter printWriter = new PrintWriter(path.toFile().getName());
			printWriter.write(stopTimesExportString());
			printWriter.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Method which searches through stopsTimes map with the stop id
     * @param stopId stop id the user will input
     * @author
     */
	public List<StopTime> searchByStop(String stopId){
        Iterator<Map.Entry<String, LinkedList<StopTime>>> iterator = stopTimesMap.entrySet().iterator();
        List<StopTime> stopList = new ArrayList<StopTime>();
        while(iterator.hasNext()){
            Map.Entry<String, LinkedList<StopTime>> currentEntry = iterator.next();
            //TODO: could be a source for Error
            StopTime temp = hasID(currentEntry.getValue(), stopId, "stopId");
            if(temp != null){
                stopList.add(temp);
            }
        }
        return stopList;
	}

	/**
	 * Method which searches through stopsTimes map with the trip id
	 * @param tripId trip id the user will input
     * @author
	 */
	public List<StopTime> searchByTrip(String tripId){
		Iterator<Map.Entry<String, LinkedList<StopTime>>> iterator = stopTimesMap.entrySet().iterator();
		List<StopTime> stopTimeList = new ArrayList<StopTime>();
        while(iterator.hasNext()){
            Map.Entry<String, LinkedList<StopTime>> currentEntry = iterator.next();
            //TODO: possible source for errors
            StopTime temp = hasID(currentEntry.getValue(), tripId, "tripId");
            if(temp != null){
                stopTimeList.add(temp);
            }
        }
        return stopTimeList;
	}


    /**
     * Helper method that takes in header line as a string and returns a parsed array if valid
     * If invalid, returns empty array
     * @param headerLine first line of stop_times.txt
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

            if (sortedArray.get(0).equals(" ") || sortedArray.get(1).equals(" ") || sortedArray.get(2).equals(" ") ||
                    sortedArray.get(3).equals(" ") || sortedArray.get(4).equals(" ")) {
                return new ArrayList<>();
            }

            if(Integer.parseInt(sortedArray.get(4)) < 0 && !sortedArray.get(4).equals(" ")) {
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
     * @param headerList first line of stop_times.txt
     * @return array list with the sorted attributes
     * @author Hayden Klein, Melissa Lin
     */
    public ArrayList<String> sortArray(ArrayList<String> unsortedArray,ArrayList<String> headerList) {

        ArrayList<String> sortedArray = new ArrayList<>(10);
        for (int k = 0; k < 10;k++){
            sortedArray.add(" ");
        }

        // iterates for the amount of attributes in the header passed in
        for (int i = 0; i < unsortedArray.size(); i++) {
            // if attribute is found in array, store at it's corresponding index
            if (headerList.get(i).equals("trip_id")) {
                sortedArray.set(0, unsortedArray.get(i));
            }
            if (headerList.get(i).equals("arrival_time")) {
                sortedArray.set(1, unsortedArray.get(i));
            }
            if (headerList.get(i).equals("departure_time")) {
                sortedArray.set(2, unsortedArray.get(i));
            }
            if (headerList.get(i).equals("stop_id")) {
                sortedArray.set(3, unsortedArray.get(i));
            }
            if (headerList.get(i).equals("stop_sequence")) {
                sortedArray.set(4, unsortedArray.get(i));
            }
            if (headerList.get(i).equals("stop_headsign")) {
                sortedArray.set(5, unsortedArray.get(i));
            }
            if (headerList.get(i).equals("pickup_type")) {
                sortedArray.set(6, unsortedArray.get(i));
            }
            if (headerList.get(i).equals("drop_off_type")) {
                sortedArray.set(7, unsortedArray.get(i));
            }
            if (headerList.get(i).equals("shape_dist_traveled")) {
                sortedArray.set(8, unsortedArray.get(i));
            }
            if (headerList.get(i).equals("timepoint")) {
                sortedArray.set(9, unsortedArray.get(i));
            }
        }

        for (int i = 0; i < 10; i++){
            if(sortedArray.get(i).equals("")) {
                sortedArray.set(i, " ");
            }
        }

        // sorted array in the right order
        return sortedArray;
    }

    /**
     * Method is meant to limit the amount of time spent displaying the data, because there
     * is already a limit as to how much can be displayed. The export method should be used when
     * all the data is needed.
     * @return A string that only contains the first stopTime with a stop ID
     * @author Jack Haek
     */
    public String stopTimesDisplayString(){
        StringBuilder returningString = new StringBuilder("trip_id,arrival_time,departure_time,stop_id,stop_sequence," +
                "stop_headsign,pickup_type,dropoff_type,shape_dist_traveled,timepoint\n");
        for(Map.Entry<String, LinkedList<StopTime>> entry : stopTimesMap.entrySet()){
            returningString.append(entry.getValue().get(0).toString()).append("\n");
        }
        return returningString.toString();
    }

    /**
     * Method which calls toString of all stopTimes in stopTimesMap and appends them to a return string
     * @return String of all stopTimes
     * @author Hayden Klein
     */
    public String stopTimesExportString(){
        StringBuilder returningString = new StringBuilder("trip_id,arrival_time,departure_time,stop_id,stop_sequence," +
                "stop_headsign,pickup_type,dropoff_type,shape_dist_traveled,timepoint\n");
        for (Map.Entry<String, LinkedList<StopTime>> entry : stopTimesMap.entrySet()){
            for(StopTime stopTime: entry.getValue()){
                returningString.append(stopTime.toString()).append("\n");
            }
        }
        return returningString.toString();
    }

    /**
     * Method will loop through a list and determine if the contained in the linked list
     * It will return the stopTime if it has the correct stop ID
     * @param list the list to search through
     * @param id the id to look for in the list
     * @return returns a singular StopTime that has the stop ID
     * @author Jack Haek
     */
    private StopTime hasID(List<StopTime> list, String id, String type){
        if(type.equals("stopId")) {
            for (StopTime element : list) {
                if (element.getStopId().equals(id)) {
                    return element;
                }
            }
        } else if(type.equals("tripId")){
            for (StopTime element : list) {
                if (element.getTripId().equals(id)) {
                    return element;
                }
            }
        }
        return null;
    }

}