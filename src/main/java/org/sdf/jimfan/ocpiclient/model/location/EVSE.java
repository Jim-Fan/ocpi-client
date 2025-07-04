package org.sdf.jimfan.ocpiclient.model.location;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;

import org.sdf.jimfan.ocpiclient.model.datatype.Capability;
import org.sdf.jimfan.ocpiclient.model.datatype.DisplayText;
import org.sdf.jimfan.ocpiclient.model.datatype.GeoLocation;
import org.sdf.jimfan.ocpiclient.model.datatype.Image;
import org.sdf.jimfan.ocpiclient.model.datatype.ParkingRestriction;
import org.sdf.jimfan.ocpiclient.model.datatype.Status;
import org.sdf.jimfan.ocpiclient.model.datatype.StatusSchedule;

public class EVSE {

	/**
	 * OCPI 2.2.1:
	 * 
	 * Uniquely identifies the EVSE within the CPOs platform (and suboperator platforms). This
	 * field can never be changed, modified or renamed. This is the 'technical' identification
	 * of the EVSE, not to be used as 'human readable' identification, use the field evse_id for
	 * that.
	 */
	@JsonProperty("uid")
	private String uid;
	
	/**
	 * In OCPI 2.2.1, EVSE ID is optional. But in my context, it is mandatory.
	 */
	@JsonProperty("evse_id")
	private String evseId;

	@JsonProperty("status")
	private Status status;
	
	@JsonProperty("status_schedule")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<StatusSchedule> statusSchedule;

	@JsonProperty("capabilities")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Capability> capabilities;
	
	@JsonProperty("connectors")
	private List<Connector> connectors;
	
	@JsonProperty("floor_level")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String floorLevel;
	
	@JsonProperty("coordinates")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private GeoLocation coordinates;
	
	/**
	 * In OCPI 2.2.1, physical reference is optional. But in my context, it is mandatory.
	 */
	@JsonProperty("physical_reference")
	private String physicalReference;
	
	@JsonProperty("directions")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<DisplayText> directions;
	
	@JsonProperty("parking_restrictions")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<ParkingRestriction> parkingRestrictions;
	
	@JsonProperty("images")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Image> images;
	
	@JsonProperty("last_updated")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private Date lastUpdated;

	
	public EVSE(String uid, String evseId, Status status, List<Connector> connectors, String physicalReference,
			Date lastUpdated) {
		this.uid = uid;
		this.evseId = evseId;
		this.status = status;
		this.connectors = connectors;
		this.physicalReference = physicalReference;
		this.lastUpdated = lastUpdated;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getEvseId() {
		return evseId;
	}

	public void setEvseId(String evseId) {
		this.evseId = evseId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<StatusSchedule> getStatusSchedule() {
		return statusSchedule;
	}

	public void setStatusSchedule(List<StatusSchedule> statusSchedule) {
		this.statusSchedule = statusSchedule;
	}

	public List<Capability> getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(List<Capability> capabilities) {
		this.capabilities = capabilities;
	}

	public List<Connector> getConnectors() {
		return connectors;
	}

	public void setConnectors(List<Connector> connectors) {
		this.connectors = connectors;
	}

	public String getFloorLevel() {
		return floorLevel;
	}

	public void setFloorLevel(String floorLevel) {
		this.floorLevel = floorLevel;
	}

	public GeoLocation getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(GeoLocation coordinates) {
		this.coordinates = coordinates;
	}

	public String getPhysicalReference() {
		return physicalReference;
	}

	public void setPhysicalReference(String physicalReference) {
		this.physicalReference = physicalReference;
	}

	public List<DisplayText> getDirections() {
		return directions;
	}

	public void setDirections(List<DisplayText> directions) {
		this.directions = directions;
	}

	public List<ParkingRestriction> getParkingRestrictions() {
		return parkingRestrictions;
	}

	public void setParkingRestrictions(List<ParkingRestriction> parkingRestrictions) {
		this.parkingRestrictions = parkingRestrictions;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
