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


/**
 * @author Milan Kablar
 * @version 1.0
 * @created 10-Oct-2019 1:41:02 PM
 */
public class Stop {

	private String stopId;
	private String stopCode;
	private String stopName;
	private String stopDesc;
	private String stopLat;
	private String stopLon;
	private String zoneId;
	private String stopURL;
	private String locationType;
	private String parentStation;
	private String stopTimezone;
	private String wheelchairBoarding;
	private String levelId;
	private String platformCode;

	/**
	 * Constructor for Stop class will take attributes from stops.txt
	 * @param stopCode String stop code
	 * @param stopName String stop name
	 * @param stopDesc String stop description
	 * @param stopLat String stop latitude
	 * @param stopLon String stop longitude
	 * @param zoneId String zone id
	 * @param stopURL String stop URL
	 * @param locationType String location type for stop
	 * @param parentStation String parent station for stop
	 * @param stopTimezone String stop timezone
	 * @param wheelchairBoarding String wheelchair boarding
	 * @param levelId String level id
	 * @param platformCode String platform code
	 */
	public Stop(String stopId, String stopCode, String stopName, String stopDesc, String stopLat, String stopLon, String zoneId,
				String stopURL, String locationType, String parentStation, String stopTimezone,
				String wheelchairBoarding, String levelId, String platformCode) {
		this.stopId = stopId;
		this.stopCode = stopCode;
		this.stopName = stopName;
		this.stopDesc = stopDesc;
		this.stopLat = stopLat;
		this.stopLon = stopLon;
		this.zoneId = zoneId;
		this.stopURL = stopURL;
		this.locationType = locationType;
		this.parentStation = parentStation;
		this.stopTimezone = stopTimezone;
		this.wheelchairBoarding = wheelchairBoarding;
		this.levelId = levelId;
		this.platformCode = platformCode;
	}

	public String getStopId() {
		return stopId;
	}

	public String getStopCode() {
		return stopCode;
	}

	public String getStopName() {
		return stopName;
	}

	public String getStopDesc() {
		return stopDesc;
	}

	public String getStopLat() {
		return stopLat;
	}

	public String getStopLon() {
		return stopLon;
	}

	public String getZoneId() {
		return zoneId;
	}

	public String getStopURL() {
		return stopURL;
	}

	public String getLocationType() {
		return locationType;
	}

	public String getParentStation() {
		return parentStation;
	}

	public String getStopTimezone() {
		return stopTimezone;
	}

	public String getWheelchairBoarding() {
		return wheelchairBoarding;
	}

	public String getLevelId() {
		return levelId;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setStopId(String stopId) {
		this.stopId = stopId;
	}

	public void setStopCode(String stopCode) {
		this.stopCode = stopCode;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	public void setStopDesc(String stopDesc) {
		this.stopDesc = stopDesc;
	}

	public void setStopLat(String stopLat) {
		this.stopLat = stopLat;
	}

	public void setStopLon(String stopLon) {
		this.stopLon = stopLon;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public void setStopURL(String stopURL) {
		this.stopURL = stopURL;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public void setParentStation(String parentStation) {
		this.parentStation = parentStation;
	}

	public void setStopTimezone(String stopTimezone) {
		this.stopTimezone = stopTimezone;
	}

	public void setWheelchairBoarding(String wheelchairBoarding) {
		this.wheelchairBoarding = wheelchairBoarding;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	/**
	 * toString method will return String of all attributes separated by a comma
	 * @return String
	 * @author Milan Kablar
	 */
	@Override
	public String toString() {
		return stopId + "," + stopCode + "," + stopName + "," + stopDesc + "," + stopLat + "," + stopLon + "," + zoneId + "," + stopURL
				+ "," + locationType + "," + parentStation + "," + stopTimezone + "," + wheelchairBoarding + "," + levelId + "," + platformCode;
	}
}