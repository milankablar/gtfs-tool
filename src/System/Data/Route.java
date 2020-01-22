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
 * @created 10-Oct-2019 1:40:54 PM
 */
public class Route {

	private String routeId;
	private String agencyId;
	private String routeShortName;
	private String routeLongName;
	private String routeDesc;
	private String routeType;
	private String routeUrl;
	private String routeColor;
	private String routeTextColor;
	private String routeSortOrder;

	/**
	 * Constructor for Route class will take attributes from routes.txt
	 * @param routeId String route id
	 * @param agencyId String agency id
	 * @param routeShortName String route short name
	 * @param routeLongName String route long name
	 * @param routeDesc String route description
	 * @param routeType String route type
	 * @param routeUrl String route URL
	 * @param routeColor String route color as hex value
	 * @param routeTextColor String route text color as hex value
	 * @param routeSortOrder String route sort order
	 */
	public Route(String routeId, String agencyId, String routeShortName, String routeLongName,
				 String routeDesc, String routeType, String routeUrl, String routeColor, String routeTextColor,
				 String routeSortOrder) {
		this.routeId = routeId;
		this.agencyId = agencyId;
		this.routeShortName = routeShortName;
		this.routeLongName = routeLongName;
		this.routeDesc = routeDesc;
		this.routeType = routeType;
		this.routeUrl = routeUrl;
		this.routeColor = routeColor;
		this.routeTextColor = routeTextColor;
		this.routeSortOrder = routeSortOrder;
	}

	public String getRouteId() {
		return routeId;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public String getRouteShortName() {
		return routeShortName;
	}

	public String getRouteLongName() {
		return routeLongName;
	}

	public String getRouteDesc() {
		return routeDesc;
	}

	public String getRouteType() {
		return routeType;
	}

	public String getRouteUrl() {
		return routeUrl;
	}

	public String getRouteColor() {
		return routeColor;
	}

	public String getRouteTextColor() {
		return routeTextColor;
	}

	public String getRouteSortOrder() {
		return routeSortOrder;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public void setRouteShortName(String routeShortName) {
		this.routeShortName = routeShortName;
	}

	public void setRouteLongName(String routeLongName) {
		this.routeLongName = routeLongName;
	}

	public void setRouteDesc(String routeDesc) {
		this.routeDesc = routeDesc;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}

	public void setRouteUrl(String routeUrl) {
		this.routeUrl = routeUrl;
	}

	public void setRouteColor(String routeColor) {
		this.routeColor = routeColor;
	}

	public void setRouteTextColor(String routeTextColor) {
		this.routeTextColor = routeTextColor;
	}

	public void setRouteSortOrder(String routeSortOrder) {
		this.routeSortOrder = routeSortOrder;
	}

	/**
	 * toString method will return String of all attributes separated by a comma
	 * @return String
	 * @author Milan Kablar
	 */
	@Override
	public String toString() {
		return routeId + "," + agencyId + "," + routeShortName + "," + routeLongName + "," + routeDesc + ","
				+ routeType + "," + routeUrl + "," + routeColor + "," + routeTextColor + "," + routeSortOrder;
	}
}