package CIMI;

import java.util.Map;

public class Agent extends CIMIResource {
	// An attribute for each field in the CIMI resource spec, with the same name and
	// type.
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	private String deviceID;
	private String leaderID;
	private String leaderIP;
	private Boolean authenticated;
	private Boolean connected;
	
	public Agent(final Map<String, Object> objectData) {
		super(objectData);
		this.deviceID = (String) objectData.get("deviceID");
		this.leaderID = (String) objectData.get("leaderID");
		this.leaderIP = (String) objectData.get("leaderIP");
		this.authenticated = (Boolean) objectData.get("authenticated");
		this.connected = (Boolean) objectData.get("connected");
	}
	
	// Setters (a setter for each property called "set_propertyname")
	public void set_deviceID(final String deviceID) {
		this.deviceID = deviceID;
	}
	
	public void set_leaderID(final String leaderID) {
		this.leaderID = leaderID;
	}
	
	public void set_leaderIP(final String leaderIP) {
		this.leaderIP = leaderIP;
	}
	
	public void set_authenticated(final Boolean authenticated) {
		this.authenticated = authenticated;
	}
	
	public void set_connected(final Boolean connected) {
		this.connected = connected;
	}
	
	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "getCIMIResourceData"
	@Override
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = super.getCIMIResourceData();
		if (this.deviceID != null)
			info.put("deviceID", this.deviceID);
		if (this.leaderID != null)
			info.put("leaderID", this.leaderID);
		if (this.leaderIP != null)
			info.put("leaderAddress", this.leaderIP);
		if (this.authenticated != null)
			info.put("authenticated", this.authenticated);
		if (this.connected != null)
			info.put("connected", this.connected);
		return info;
	}
	
	public void updateAllData(final Map<String, Object> data) {
		super.setCIMIResourceData(data);
		if (data.get("deviceID") != null)
			set_deviceID((String) data.get("deviceID"));
		if (data.get("leaderID") != null)
			set_leaderID((String) data.get("leaderID"));
		if (data.get("leaderIP") != null)
			set_leaderIP((String) data.get("leaderIP"));
		if (data.get("authenticated") != null)
			set_authenticated((Boolean) data.get("authenticated"));
		if (data.get("connected") != null)
			set_connected((Boolean) data.get("connected"));
	}	
	
}
