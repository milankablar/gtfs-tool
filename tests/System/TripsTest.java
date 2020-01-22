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
package System;

import System.Data.Trips;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TripsTest {

    Trips trips;

    /**
     * Set up so the tests can be ran
     * @author Melissa Lin
     */
    @BeforeEach
    void setUp() {
        trips = new Trips(); // initialize routes object
    }

    @Test
    void testExportFile() {
        // TODO
    }

    @Test
    void importFile() {
        // TODO
    }

    /**
     * tests the helper method sortArray for importFile
     * assumes the array has passed the header test; regardless of data it will still sort the array
     */
    @Test
    void testSortArray() {
        // test sort #1
        ArrayList<String> headersArray = new ArrayList<>();
        // attributes for test header
        headersArray.add("route_id");
        headersArray.add("trip_headsign");
        headersArray.add("shape_id");
        ArrayList<String> unsortedArray = new ArrayList<>();
        unsortedArray.add("64");
        unsortedArray.add("0");
        unsortedArray.add("17-SEP_64_0_23");
        // sample sorted array to compare to
        ArrayList<String> sampleSortedArray = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            sampleSortedArray.add(" ");
        }
        sampleSortedArray.set(0, "64");
        sampleSortedArray.set(3,"0");
        sampleSortedArray.set(7, "17-SEP_64_0_23");
        // compares the expected array from the sample data with the output from the call to sortArray
        assertEquals(sampleSortedArray, trips.sortArray(unsortedArray, headersArray));

        // test sort #2
        // resets the data added to the arrays
        headersArray = new ArrayList<>();
        headersArray.add("route_id");
        headersArray.add("trip_id");
        headersArray.add("direction_id");
        unsortedArray = new ArrayList<>();
        unsortedArray.add("64");
        unsortedArray.add("21736567_2541");
        unsortedArray.add("64102");
        sampleSortedArray = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sampleSortedArray.add(" ");
        }
        sampleSortedArray.set(0, "64");
        sampleSortedArray.set(2, "21736567_2541");
        sampleSortedArray.set(5, "64102");
        // compares the expected array from the sample dat with the output from the call to sortArray
        assertEquals(sampleSortedArray, trips.sortArray(unsortedArray, headersArray));
    }

    /**
     * test the validation of data line for importFile
     */
    @Test
    void testValidateDataLine() {
        String header = "route_id,service_id,trip_id,trip_headsign,stop_sequence,trip_short_name," +
                "direction_id,block_id,shape_id,wheel_chair_access,bikes_allowed";
        ArrayList<String> headersList = trips.validateHeader(header);
        // test #1
        String badDataLine = "MORGAN & S24 #4970,,42.9812627,-87.9445082";
        ArrayList<String> badExpectedHeaderList = new ArrayList<>(Arrays.asList(("34739417,service_id," +
                "trip_id,trip_headsign,stop_sequence,trip_short_name,direction_id,block_id,shape_id," +
                "wheel_chair_access,bikes_allowed").split(",")));
        String badExpectedDataLine = "[]";
        assertEquals(badExpectedDataLine, trips.validateDataLine(badDataLine,badExpectedHeaderList).toString());
        // test #2
        String dataLine = "64,17-SEP_SUN,21736564_2535,60TH-VLIET,0,64102,17-SEP_64_0_23";
        ArrayList<String> expectedHeaderList = new ArrayList<>(Arrays.asList(("route_id,service_id,trip_id," +
                "trip_headsign,stop_sequence,trip_short_name,direction_id,block_id,shape_id,wheel_chair_access," +
                "bikes_allowed").split(",")));
        String expectedDataLine = "[64, 17-SEP_SUN, 21736564_2535, 60TH-VLIET, 64102, 17-SEP_64_0_23,  ,  ,  ,  ]";
        assertEquals(expectedDataLine, trips.validateDataLine(dataLine, headersList).toString());
    }

    /**
     * test the validation of header for importFile
     * @author Melissa Lin
     */
    @Test
    void testValidateHeader() {
        // test #1 header with bad attributes, so it should return an empty array
        ArrayList<String> badExpectedHeaderList = new ArrayList<>();
        String badHeaderLine = "qwer1_id,service_id,trip_id,trip_headsign,stop_sequence,trip_short_name," +
                "direction_id,block_id,shape_id,wheel_chair_access,bikes_allowed";
        assertEquals(badExpectedHeaderList, trips.validateHeader(badHeaderLine));
        // test #2 array of headers with all of the attributes of the class
        ArrayList<String> expectedHeaderList = new ArrayList<>(Arrays.asList(("route_id,service_id,trip_id," +
                "trip_headsign,stop_sequence,trip_short_name,direction_id,block_id,shape_id,wheel_chair_access," +
                "bikes_allowed").split(",")));
        String headerLine = "route_id,service_id,trip_id,trip_headsign,stop_sequence,trip_short_name," +
                "direction_id,block_id,shape_id,wheel_chair_access,bikes_allowed";
        assertEquals(expectedHeaderList, trips.validateHeader(headerLine));
    }
}
