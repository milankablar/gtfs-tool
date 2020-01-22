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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import System.Data.Routes;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class RoutesTest {

    Routes routes;

    /**
     * Set up so the tests can be ran
     * @author Melissa Lin
     */
    @BeforeEach
    void setUp() {
        routes = new Routes(); // initialize routes object
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
        headersArray.add("route_id");
        headersArray.add("route_desc");
        headersArray.add("route_color");
        ArrayList<String> unsortedArray = new ArrayList<>();
        unsortedArray.add("1");
        unsortedArray.add("north");
        unsortedArray.add("red");
        // sample sorted array to compare to
        ArrayList<String> sampleSortedArray = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            sampleSortedArray.add(" ");
        }
        sampleSortedArray.set(0, "1");
        sampleSortedArray.set(4,"north");
        sampleSortedArray.set(7, "red");
        // compares the expected array from the sample data with the output from the call to sortArray
        assertEquals(sampleSortedArray, routes.sortArray(unsortedArray, headersArray));

        // test sort #2
        // resets the data added to the arrays
        headersArray = new ArrayList<>();
        headersArray.add("route_color");
        headersArray.add("route_id");
        headersArray.add("route_text_color");
        unsortedArray = new ArrayList<>();
        unsortedArray.add("pink");
        unsortedArray.add("1JEK");
        unsortedArray.add("blue");
        sampleSortedArray = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sampleSortedArray.add(" ");
        }
        sampleSortedArray.set(7, "pink");
        sampleSortedArray.set(0, "1JEK");
        sampleSortedArray.set(8, "blue");
        // compares the expected array from the sample data with the output from the call to sortArray
        assertEquals(sampleSortedArray, routes.sortArray(unsortedArray, headersArray));

        // test sort #3 based off of GTFS_LAX
        String attributes = "route_id,agency_id,route_short_name,route_long_name," +
                "route_desc,route_type,route_url,route_color,route_text_color";
        String dataLine = "Route1,MTU,1,South Ave,\"The Shelby Mall bus operates " +
                "between downtown and Shelby Mall with service to Gundersen Lutheran " +
                "Medical Center.  The route operates M-F 5:10 a.m. - 10:40 p.m., Sat. " +
                "7:40 a.m. - 7:40 p.m., Sun 7:40 a.m. - 6:40 p.m.  \",3,,83C0E8,";

        headersArray = new ArrayList<>();
        headersArray.add("route_id");
        headersArray.add("agency_id");
        headersArray.add("route_short_name");
        headersArray.add("route_long_name");
        headersArray.add("route_desc");
        headersArray.add("route_type");
        headersArray.add("route_url");
        headersArray.add("route_color");
        headersArray.add("route_text_color");

        unsortedArray = new ArrayList<>();
        unsortedArray.add("Route1");
        unsortedArray.add("MTU");
        unsortedArray.add("1");
        unsortedArray.add("South Ave");
        unsortedArray.add("\"The Shelby Mall bus operates between downtown and Shelby Mall with service " +
                "to Gundersen Lutheran Medical Center.  The route operates M-F 5:10 a.m. - 10:40 p.m., " +
                "Sat. 7:40 a.m. - 7:40 p.m., Sun 7:40 a.m. - 6:40 p.m.  \"");
        unsortedArray.add("3");
        unsortedArray.add("");
        unsortedArray.add("83C0E8");
        unsortedArray.add("");


        ArrayList<String> sortedArray = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sortedArray.add("");
        }
        sortedArray.set(0, "Route1");
        sortedArray.set(1, "MTU");
        sortedArray.set(2, "1");
        sortedArray.set(3, "South Ave");
        sortedArray.set(4, "\"The Shelby Mall bus operates between downtown and Shelby " +
                "Mall with service to Gundersen Lutheran Medical Center.  The route " +
                "operates M-F 5:10 a.m. - 10:40 p.m., Sat. 7:40 a.m. - 7:40 p.m., " +
                "Sun 7:40 a.m. - 6:40 p.m.  \"");
        sortedArray.set(5, "3");
        sortedArray.set(6, " ");
        sortedArray.set(7, "83C0E8");
        sortedArray.set(8, " ");
        sortedArray.set(9, " ");
        assertEquals(sortedArray, routes.sortArray(unsortedArray, headersArray));

    }

    /**
     * test the validation of data line for importFile
     * @author Melissa Lin
     */
    @Test
    void testValidateDataLine() {
        String header = "route_id,agency_id,route_short_name,route_long_name," +
                "route_desc,route_type,route_url,route_color,route_text_color";
        ArrayList<String> headersList = routes.validateHeader(header);
        // test #1
        String badDataLine = "  ,12,Teutonia-Hampton,,3, ,008345,";
        ArrayList<String> badExpectedHeaderList = new ArrayList<>(Arrays.asList((" ," +
                "agency_id,route_short_name,route_long_name,route_desc,route_type,route_url," +
                "route_color,route_text_color").split(",")));
        String badExpectedDataLine = "[]";
        assertEquals(badExpectedDataLine, routes.validateDataLine(badDataLine,badExpectedHeaderList).toString());
        // test #2
        String dataLine = "12,MCTS,12,Teutonia-Hampton,,3,,008345,";
        ArrayList<String> expectedHeaderList = new ArrayList<>(Arrays.asList(("route_id,agency_id," +
                "route_short_name,route_long_name,route_desc,route_type,route_url,route_color," +
                "route_text_color").split(",")));
        String expectedDataLine = "[12, MCTS, 12, Teutonia-Hampton,  , 3,  , 008345,  ,  ]";
        assertEquals(expectedDataLine, routes.validateDataLine(dataLine, headersList).toString());
    }

    /**
     * test the validation of header for importFile
     * @author Melissa Lin
     */
    @Test
    void testValidateHeader() {
        // test #1
        ArrayList<String> badExpectedHeaderList = new ArrayList<>(Arrays.asList(("agency_id," +
                "route_short_name,route_long_name,route_desc,route_type,route_url," +
                "route_color,route_text_color").split(",")));
        String badHeaderLine = "agency_id,route_short_name,route_long_name," +
                "route_desc,route_type,route_url,route_color,route_text_color";
        assertEquals(badExpectedHeaderList, routes.validateHeader(badHeaderLine));
        // test #2
        ArrayList<String> expectedHeaderList = new ArrayList<>(Arrays.asList(("route_id,agency_id," +
                "route_short_name,route_long_name,route_desc,route_type,route_url,route_color," +
                "route_text_color").split(",")));
        String headerLine = "route_id,agency_id,route_short_name,route_long_name," +
                "route_desc,route_type,route_url,route_color,route_text_color";
        assertEquals(expectedHeaderList, routes.validateHeader(headerLine));
    }
}
