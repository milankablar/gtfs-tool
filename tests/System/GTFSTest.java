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

import System.Data.GTFS;
import System.Data.Stops;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GTFSTest {

    private Stops stops;
    private GTFS gtfs;

    /**
     * Set up so the tests can be ran
     * @author Melissa Lin
     */
    @BeforeEach
    void setUp() {
        stops = new Stops(); // initialize stops object
        gtfs = new GTFS(); // initialize gtfs object

    }

        /**
     * tests calculate distance method of GTFS
     * @author Melissa Lin
     */
    @Test
    void testCalculateTripDistance() throws IOException {
        // test #1 valid trip id from GTFS_MCTS
        String tripId = "21736564_2535";
        // expected distance
        String expectedDistance = "11.164691280271464 miles";
        // import data
        File routeFile = new File("./GTFS_MCTS/routes.txt");
        File tripsFile = new File("./GTFS_MCTS/trips.txt");
        File stopTimesFile = new File("./GTFS_MCTS/stop_times.txt");
        File stopsFile = new File("./GTFS_MCTS/stops.txt");
        gtfs.importData(routeFile, tripsFile, stopTimesFile, stopsFile);
        assertEquals(expectedDistance, gtfs.calculateTripDistance(tripId) + " miles");

        // test #3 valid trip id from GTFS_LAX
        // calculate trip distance crashes the application
        String tripId3 = "Rt9Trp2";
        String expectedDistance3= "13.904101557932274 miles";
        routeFile = new File("./GTFS_LAX/routes.txt");
        tripsFile = new File("./GTFS_LAX/trips.txt");
        stopTimesFile = new File("./GTFS_LAX/stop_times.txt");
        stopsFile = new File("./GTFS_LAX/stops.txt");
        gtfs.importData(routeFile, tripsFile, stopTimesFile, stopsFile);
        assertEquals(expectedDistance3, gtfs.calculateTripDistance(tripId3) + " miles");

        // test #4 valid trip id from GTFS-EauClaire
        String tripId4 = "37ABE0740A_R01_SAT";
        String expectedDistance4 = "16.85817550064958 miles";
        routeFile = new File("./GTFS_EauClaire/routes.txt");
        tripsFile = new File("./GTFS_EauClaire/trips.txt");
        stopTimesFile = new File("./GTFS_EauClaire/stop_times.txt");
        stopsFile = new File("./GTFS_EauClaire/stops.txt");
        gtfs.importData(routeFile, tripsFile, stopTimesFile, stopsFile);
        assertEquals(expectedDistance4, gtfs.calculateTripDistance(tripId4) + " miles");

        // test #5 valid trip id from GTFS_Chicago
        String tripId5 = "483119466199";
        String expectedDistance5 = "9.49295799055641 miles";
        routeFile = new File("./GTFS_Chicago/routes.txt");
        tripsFile = new File("./GTFS_Chicago/trips.txt");
        stopTimesFile = new File("./GTFS_Chicago/stop_times.txt");
        stopsFile = new File("./GTFS_Chicago/stops.txt");
        gtfs.importData(routeFile, tripsFile, stopTimesFile, stopsFile);
        assertEquals(expectedDistance5, gtfs.calculateTripDistance(tripId5) + " miles");
    }

    /**
     * tests get average speed method of GTFS
     * @author Melissa Lin
     */
    @Test
    void testGetAverageSpeed() throws IOException {
        // test #1 valid trip id from GTFS_MCTS
        String tripId = "21736564_2535";
        // expected distance
        String expectedSpeed = "20.933796150508996 miles per hour";
        // import data
        File routeFile = new File("./GTFS_MCTS/routes.txt");
        File tripsFile = new File("./GTFS_MCTS/trips.txt");
        File stopTimesFile = new File("./GTFS_MCTS/stop_times.txt");
        File stopsFile = new File("./GTFS_MCTS/stops.txt");
        gtfs.importData(routeFile, tripsFile, stopTimesFile, stopsFile);
        assertEquals(expectedSpeed, gtfs.calculateAverageSpeed(tripId) + " miles per hour");

        // test #2 valid trip id from GTFS_LAX
        String tripId2 = "Rt9Trp2";
        String expectedSpeed2= "14.765417583644892 miles per hour";
        routeFile = new File("./GTFS_LAX/routes.txt");
        tripsFile = new File("./GTFS_LAX/trips.txt");
        stopTimesFile = new File("./GTFS_LAX/stop_times.txt");
        stopsFile = new File("./GTFS_LAX/stops.txt");
        gtfs.importData(routeFile, tripsFile, stopTimesFile, stopsFile);
        assertEquals(expectedSpeed2, gtfs.calculateAverageSpeed(tripId2) + " miles per hour");

        // test #3 valid trip id from GTFS_EauClaire
        String tripId3 = "37ABE0740A_R01_SAT";
        String expectedSpeed3 = "21.52107510721223 miles per hour";
        routeFile = new File("./GTFS_EauClaire/routes.txt");
        tripsFile = new File("./GTFS_EauClaire/trips.txt");
        stopTimesFile = new File("./GTFS_EauClaire/stop_times.txt");
        stopsFile = new File("./GTFS_EauClaire/stops.txt");
        gtfs.importData(routeFile, tripsFile, stopTimesFile, stopsFile);
        assertEquals(expectedSpeed3, gtfs.calculateAverageSpeed(tripId3) + " miles per hour");

        // test #4 valid trip id from GTFS_Chicago
        String tripId4 = "483119466199";
        String expectedSpeed4 = "11.743865555327519 miles per hour";
        routeFile = new File("./GTFS_Chicago/routes.txt");
        tripsFile = new File("./GTFS_Chicago/trips.txt");
        stopTimesFile = new File("./GTFS_Chicago/stop_times.txt");
        stopsFile = new File("./GTFS_Chicago/stops.txt");
        gtfs.importData(routeFile, tripsFile, stopTimesFile, stopsFile);
        assertEquals(expectedSpeed4, gtfs.calculateAverageSpeed(tripId4) + " miles per hour");
    }

    /**
     * tests get next trips going to stop id method of GTFS
     * @author Melissa Lin
     */
    @Test
    void testGetNextTripsGoingToStopId() throws IOException {
        // test #1 valid stop id from GTFS_MCTS
        String stopId = "4970";
        String expectedNextTrip = "Next Trip(s) Arriving:\n" + "21847587_4450";
        File routeFile = new File("./GTFS_MCTS/routes.txt");
        File tripsFile = new File("./GTFS_MCTS/trips.txt");
        File stopTimesFile = new File("./GTFS_MCTS/stop_times.txt");
        File stopsFile = new File("./GTFS_MCTS/stops.txt");
        gtfs.importData(routeFile, tripsFile, stopTimesFile, stopsFile);
        assertEquals(expectedNextTrip, "Next Trip(s) Arriving:\n" +
                gtfs.getNextTripsGoingToStopId(stopId).get(0).getTripId());

        // test #2 valid stop id from GTFS_LAX
        String stopId2 = "0";
        String expectedNextTrip2 = "Next Trip(s) Arriving:\n" + "Rt10AMTrp1";
        routeFile = new File("./GTFS_LAX/routes.txt");
        tripsFile = new File("./GTFS_LAX/trips.txt");
        stopTimesFile = new File("./GTFS_LAX/stop_times.txt");
        stopsFile = new File("./GTFS_LAX/stops.txt");
        gtfs.importData(routeFile, tripsFile, stopTimesFile, stopsFile);
        assertEquals(expectedNextTrip2, "Next Trip(s) Arriving:\n" +
                gtfs.getNextTripsGoingToStopId(stopId2).get(0).getTripId());

        // test #3 valid stop id from GTFS_EauClaire
        String stopId3 = "FBBC723125";
        String expectedNextTrip3 = "Next Trip(s) Arriving:\n" +
                "37ABE0740A_R01_SAT";
        routeFile = new File("./GTFS_EauClaire/routes.txt");
        tripsFile = new File("./GTFS_EauClaire/trips.txt");
        stopTimesFile = new File("./GTFS_EauClaire/stop_times.txt");
        stopsFile = new File("./GTFS_EauClaire/stops.txt");
        gtfs.importData(routeFile, tripsFile, stopTimesFile, stopsFile);
        assertEquals(expectedNextTrip3, "Next Trip(s) Arriving:\n" +
                gtfs.getNextTripsGoingToStopId(stopId3).get(0).getTripId());

        // test #4 valid stop id from GTFS_Chicago
        String stopId4 = "17288";
        String expectedNextTrip4 = "Next Trip(s) Arriving:\n" + "483119466199";
        routeFile = new File("./GTFS_Chicago/routes.txt");
        tripsFile = new File("./GTFS_Chicago/trips.txt");
        stopTimesFile = new File("./GTFS_Chicago/stop_times.txt");
        stopsFile = new File("./GTFS_Chicago/stops.txt");
        gtfs.importData(routeFile, tripsFile, stopTimesFile, stopsFile);
        assertEquals(expectedNextTrip4, "Next Trip(s) Arriving:\n" +
                gtfs.getNextTripsGoingToStopId(stopId4).get(0).getTripId());
    }
}