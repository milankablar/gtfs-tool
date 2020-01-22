/*
 * Course: SE2030 - 031
 * Fall 2019
 * Lab 8 - Issue Tracking
 * Name: Group H (Hayden Klein, Milan Kablar, Jack Haek, Melissa Lin, James Lang)
 * Created: 10/13/2019
 */
package System.DataStructures;


import java.util.List;

/**
 * @author Milan Kablar
 * @version 1.0
 * @created 10-Oct-2019 1:41:22 PM
 */
public class Trip {

	private String routeId;
	private String serviceId;
	private String tripId;
	private String tripHeadsign;
	private String tripShortName;
	private String directionId;
	private String blockId;
	private String shapeId;
	private String wheelchairAccessible;
	private String bikesAllowed;

	/**
	 * Constructor for Trip class wil take attributes from trips.txt
	 * @param routeId String route id
	 * @param serviceId String service id
	 * @param tripId String trip id
	 * @param tripHeadsign String trip headsign
	 * @param tripShortName String trip short name
	 * @param directionId String direction id
	 * @param blockId String block id
	 * @param shapeId String shape id
	 * @param wheelchairAccessible String wheelchair accessible
	 * @param bikesAllowed String bikes allowed
	 */
	public Trip(String routeId, String serviceId, String tripId, String tripHeadsign, String tripShortName,
				String directionId, String blockId, String shapeId, String wheelchairAccessible, String bikesAllowed) {
		this.routeId = routeId;
		this.serviceId = serviceId;
		this.tripId = tripId;
		this.tripHeadsign = tripHeadsign;
		this.tripShortName = tripShortName;
		this.directionId = directionId;
		this.blockId = blockId;
		this.shapeId = shapeId;
		this.wheelchairAccessible = wheelchairAccessible;
		this.bikesAllowed = bikesAllowed;
	}

	public String getRouteId() {
		return routeId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public String getTripId() {
		return tripId;
	}

	public String getTripHeadsign() {
		return tripHeadsign;
	}

	public String getTripShortName() {
		return tripShortName;
	}

	public String getDirectionId() {
		return directionId;
	}

	public String getBlockId() {
		return blockId;
	}

	public String getShapeId() {
		return shapeId;
	}

	public String getWheelchairAccessible() {
		return wheelchairAccessible;
	}

	public String getBikesAllowed() {
		return bikesAllowed;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public void setTripHeadsign(String tripHeadsign) {
		this.tripHeadsign = tripHeadsign;
	}

	public void setTripShortName(String tripShortName) {
		this.tripShortName = tripShortName;
	}

	public void setDirectionId(String directionId) {
		this.directionId = directionId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public void setShapeId(String shapeId) {
		this.shapeId = shapeId;
	}

	public void setWheelchairAccessible(String wheelchairAccessible) {
		this.wheelchairAccessible = wheelchairAccessible;
	}

	public void setBikesAllowed(String bikesAllowed) {
		this.bikesAllowed = bikesAllowed;
	}

	/**
	 * toString method will return String of all attributes separated by a comma
	 * @return String
	 * @author Milan Kablar
	 */
	@Override
	public String toString() {
		return routeId + "," + serviceId + "," + tripId + "," + tripHeadsign + ","
				+ tripShortName + "," + directionId + "," + blockId + "," + shapeId
				+ "," + wheelchairAccessible + "," + bikesAllowed;
	}
}