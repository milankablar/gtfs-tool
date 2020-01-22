/*
 * Course: SE2030 - 031
 * Fall 2019
 * Lab 8 - Issue Tracking
 * Name: Group H (Hayden Klein, Milan Kablar, Jack Haek, Melissa Lin, James Lang)
 * Created: 10/13/2019
 */
package System.DataStructures;

/**
 * @author Milan Kablar
 * @version 1.0
 * @created 10-Oct-2019 1:41:09 PM
 */
public class StopTime {

	private String tripId;
	private String arrivalTime;
	private String departureTime;
	private String stopId;
	private String stopSequence;
	private String stopHeadsign;
	private String pickupType;
	private String dropOffType;
	private String shapeDistTraveled;
	private String timepoint;

	/**
	 * Constructor for StopTime class will take attribute from stop_times.txt
	 * @param tripId String trip id
	 * @param arrivalTime String arrival time formatted as HH:MM:SS
	 * @param departureTime String departure time formatted as HH:MM:SS
	 * @param stopId String stop id
	 * @param stopSequence String stop sequence
	 * @param stopHeadsign String stop headsign
	 * @param pickupType String pickup type
	 * @param dropOffType String drop off type
	 * @param shapeDistTraveled String shape distance traveled
	 * @param timepoint String time point
	 */
	public StopTime(String tripId, String arrivalTime, String departureTime, String stopId,
					String stopSequence, String stopHeadsign, String pickupType, String dropOffType,
					String shapeDistTraveled, String timepoint) {
		this.tripId = tripId;
		this.arrivalTime = arrivalTime;
		this.departureTime = departureTime;
		this.stopId = stopId;
		this.stopSequence = stopSequence;
		this.stopHeadsign = stopHeadsign;
		this.pickupType = pickupType;
		this.dropOffType = dropOffType;
		this.shapeDistTraveled = shapeDistTraveled;
		this.timepoint = timepoint;
	}

	public String getTripId() {
		return tripId;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public String getStopId() {
		return stopId;
	}

	public String getStopSequence() {
		return stopSequence;
	}

	public String getStopHeadsign() {
		return stopHeadsign;
	}

	public String getPickupType() {
		return pickupType;
	}

	public String getDropOffType() {
		return dropOffType;
	}

	public String getShapeDistTraveled() {
		return shapeDistTraveled;
	}

	public String getTimepoint() {
		return timepoint;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public void setStopId(String stopId) {
		this.stopId = stopId;
	}

	public void setStopSequence(String stopSequence) {
		this.stopSequence = stopSequence;
	}

	public void setStopHeadsign(String stopHeadsign) {
		this.stopHeadsign = stopHeadsign;
	}

	public void setPickupType(String pickupType) {
		this.pickupType = pickupType;
	}

	public void setDropOffType(String dropOffType) {
		this.dropOffType = dropOffType;
	}

	public void setShapeDistTraveled(String shapeDistTraveled) {
		this.shapeDistTraveled = shapeDistTraveled;
	}

	public void setTimepoint(String timepoint) {
		this.timepoint = timepoint;
	}

	/**
	 * toString method will return String of all attributes separated by a comma
	 * @return String
	 * @author Milan Kablar
	 */
	@Override
	public String toString() {
		return tripId + "," + arrivalTime + "," + departureTime + "," + stopId + "," + stopSequence
				+ "," + stopHeadsign + "," + pickupType + "," + dropOffType + "," + shapeDistTraveled + "," + timepoint;
	}
}