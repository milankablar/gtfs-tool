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

import System.Data.Stops;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class StopsTest {

    Stops stops;

    /**
     * Set up so the tests can be ran
     * @author Melissa Lin
     */
    @BeforeEach
    void setUp() {
        stops = new Stops(); // initialize stop object
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
        headersArray.add("stop_id");
        headersArray.add("stop_name");
        headersArray.add("stop_lat");
        ArrayList<String> unsortedArray = new ArrayList<>();
        unsortedArray.add("6712");
        unsortedArray.add("STATE");
        unsortedArray.add("43.0444475");
        // sample sorted array to compare to
        ArrayList<String> sampleSortedArray = new ArrayList<>();
        for (int j = 0; j < 14; j++) {
            sampleSortedArray.add(" ");
        }
        sampleSortedArray.set(0, "6712");
        sampleSortedArray.set(2,"STATE");
        sampleSortedArray.set(4, "43.0444475");
        // compares the expected array from the sample data with the output from the call to sortArray
        assertEquals(sampleSortedArray, stops.sortArray(unsortedArray, headersArray));

        // test sort #2
        // resets the data added to the arrays
        headersArray = new ArrayList<>();
        headersArray.add("stop_id");
        headersArray.add("stop_desc");
        headersArray.add("stop_lon");
        unsortedArray = new ArrayList<>();
        unsortedArray.add("7661");
        unsortedArray.add("LONG");
        unsortedArray.add("-87.9152766");
        sampleSortedArray = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            sampleSortedArray.add(" ");
        }
        sampleSortedArray.set(0, "7661");
        sampleSortedArray.set(3, "LONG");
        sampleSortedArray.set(5, "-87.9152766");
        // compares the expected array from the sample dat with the output from the call to sortArray
        assertEquals(sampleSortedArray, stops.sortArray(unsortedArray, headersArray));
    }

    /**
     * test the validation of data line for importFile
     * @author Melissa Lin
     */
    @Test
    void testValidateDataLine() {
        String header = "stop_id,stop_code,stop_name,stop_desc,stop_lat,stop_lon," +
                "zone_id,stop_url,location_type,parent_station,stop_timezone," +
                "wheelchair_boarding,level_id,platform_code";
        // since this method is only called after header is validated
        ArrayList<String> headersList = stops.validateHeader(header);
        // test #1
        String badDataLine = ",,REDSTOP,,LONGSTOP,,12,23,142556,,,,,,3,9083";
        // no stop_id
        ArrayList<String> badExpectedHeaderList = new ArrayList<>(Arrays.asList(("1231241,stop_code," +
                "stop_name,stop_desc,stop_lat,stop_lon,zone_id,stop_url," +
                "location_type,parent_station,stop_timezone,wheelchair_boarding," +
                "level_id,platform_code").split(",")));
        String badExpectedDataLine = "[]";
        assertEquals(badExpectedDataLine, stops.validateDataLine(badDataLine,badExpectedHeaderList).toString());
        // test #2
        String dataLine = "4892,,REDSTOP,,LONGSTOP,,12,23,142556,,,,3,9083";
        ArrayList<String> expectedHeaderList = new ArrayList<>(Arrays.asList(("stop_id,stop_code," +
                "stop_name,stop_desc,stop_lat,stop_lon,zone_id,stop_url," +
                "location_type,parent_station,stop_timezone,wheelchair_boarding," +
                "level_id,platform_code").split(",")));
        String expectedDataLine = "[4892,  , REDSTOP,  , LONGSTOP,  , 12, 23, 142556,  ,  ,  , 3, 9083]";
        assertEquals(expectedDataLine, stops.validateDataLine(dataLine, expectedHeaderList).toString());
    }

    /**
     * test the validation of header for importFile
     * @author Melissa Lin
     */
    @Test
    void testValidateHeader() {
        // test #1 where a bad header is passed in, it should retrun an empty array
        ArrayList<String> badExpectedHeaderList = new ArrayList<>();
        String badHeaderLine = "1231241,stop_code,stop_name,stop_desc,stop_lat," +
                "stop_lon,zone_id,stop_url,location_type,parent_station," +
                "stop_timezone,wheelchair_boarding,level_id,platform_code";
        assertEquals(badExpectedHeaderList, stops.validateHeader(badHeaderLine));
        // test #2 passes in an array of headers with all of the attributes
        ArrayList<String> expectedHeaderList = new ArrayList<>(Arrays.asList(("stop_id,stop_code," +
                "stop_name,stop_desc,stop_lat,stop_lon,zone_id,stop_url," +
                "location_type,parent_station,stop_timezone,wheelchair_boarding," +
                "level_id,platform_code").split(",")));
        String headerLine = "stop_id,stop_code," +
                "stop_name,stop_desc,stop_lat,stop_lon,zone_id,stop_url," +
                "location_type,parent_station,stop_timezone,wheelchair_boarding," +
                "level_id,platform_code";
        assertEquals(expectedHeaderList, stops.validateHeader(headerLine));
    }
}
