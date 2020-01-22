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

import System.Data.StopTimes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class StopTimesTest {

    StopTimes stopTimes;

    /**
     * Set up so the tests can be ran
     * @author Melissa Lin
     */
    @BeforeEach
    void setUp() {
        stopTimes = new StopTimes(); // initialize routes object
    }

    /**
     * tests the helper method sortArray for importFile
     * assumes the array has passed the header test; regardless of data it will still sort the array
     * @author Melissa Lin
     */
    @Test
    void testSortArray() {
        // test sort #1
        ArrayList<String> headersArray = new ArrayList<>();
        // attributes for test header
        headersArray.add("trip_id");
        headersArray.add("arrival_time");
        headersArray.add("stop_id");
        ArrayList<String> unsortedArray = new ArrayList<>();
        unsortedArray.add("21726564");
        unsortedArray.add("08:51:00");
        unsortedArray.add("9113");
        // sample sorted array to compare to
        ArrayList<String> sampleSortedArray = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            sampleSortedArray.add(" ");
        }
        sampleSortedArray.set(0, "21726564");
        sampleSortedArray.set(1,"08:51:00");
        sampleSortedArray.set(3, "9113");
        // compares the expected array from the sample data with the output from the call to sortArray
        assertEquals(sampleSortedArray, stopTimes.sortArray(unsortedArray, headersArray));

        // test sort #2
        // resets the data added to the arrays
        headersArray = new ArrayList<>();
        headersArray.add("trip_id");
        headersArray.add("stop_id");
        headersArray.add("dropoff_type");
        unsortedArray = new ArrayList<>();
        unsortedArray.add("21736564");
        unsortedArray.add("4664");
        sampleSortedArray = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sampleSortedArray.add(" ");
        }
        sampleSortedArray.set(0, "21736564");
        sampleSortedArray.set(3, "4664");
        // compares the expected array from the sample dat with the output from the call to sortArray
        assertEquals(sampleSortedArray, stopTimes.sortArray(unsortedArray, headersArray));
    }

    /**
     * test the validation of data line for importFile
     * @author Melissa Lin
     */
    @Test
    void testValidateDataLine() {
        String header = "trip_id,arrival_time,departure_time,stop_id,stop_sequence," +
                "stop_headsign,pickup_type,dropoff_type,shape_dist_traveled,timepoint";
        ArrayList<String> headersList = stopTimes.validateHeader(header);
        // test #1
        String badDataLine = "08:53:00,08:52:00,1";
        ArrayList<String> badExpectedHeaderList = new ArrayList<>(Arrays.asList(("123123123,arrival_time," +
                "departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,dropoff_type," +
                "shape_dist_traveled,timepoint").split(",")));
        String badExpectedDataLine = "[]";
        assertEquals(badExpectedDataLine, stopTimes.validateDataLine(badDataLine,badExpectedHeaderList).toString());
        // test #2
        String dataLine = "123456,08:52:00,08:53:00,1234,1";
        ArrayList<String> expectedHeaderList = new ArrayList<>(Arrays.asList(("trip_id,arrival_time," +
                "departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,dropoff_type," +
                "shape_dist_traveled,timepoint").split(",")));
        String expectedDataLine = "[123456, 08:52:00, 08:53:00, 1234, 1,  ,  ,  ,  ,  ]";
        assertEquals(expectedDataLine, stopTimes.validateDataLine(dataLine, headersList).toString());
    }

    /**
     * test the validation of header for importFile
     * @author Melissa Lin
     */
    @Test
    void testValidateHeader() {
        // test #1 a bad header list so it should return an empty array
        ArrayList<String> badExpectedHeaderList = new ArrayList<>();
        String badHeaderLine = "123124141241241,arrival_time,departure_time," +
                "stop_id,stop_sequence,stop_headsign,pickup_type," +
                "drop_off_type,shape_distance_traveled,timepoint";
        assertEquals(badExpectedHeaderList, stopTimes.validateHeader(badHeaderLine));
        // test #2 header list with all attributes, should return a header with all of the attributes
        ArrayList<String> expectedHeaderList = new ArrayList<>(Arrays.asList(("trip_id,arrival_time," +
                "departure_time,stop_id,stop_sequence,stop_headsign,pickup_type," +
                "drop_off_type,shape_distance_traveled,timepoint").split(",")));
        String headerLine = "trip_id,arrival_time,departure_time,stop_id,stop_sequence," +
                "stop_headsign,pickup_type,drop_off_type,shape_distance_traveled,timepoint";
        assertEquals(expectedHeaderList, stopTimes.validateHeader(headerLine));
    }
}
