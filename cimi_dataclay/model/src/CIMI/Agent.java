package CIMI;

import java.util.ArrayList;
import java.util.Map;

public class Agent extends CIMIResource {
	// An attribute for each field in the CIMI resource spec, with the same name and
	// type.
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	private String deviceID;
	private String deviceIP;
	private String leaderID;
	private String leaderIP;
	private String backupIP;
	private Boolean authenticated;
	private Boolean connected;
	private Boolean isLeader;
	private ArrayList<String> childrenIPs;
	
	public Agent(final Map<String, Object> objectData) {
		super(objectData);
		this.deviceID = (String) objectData.get("deviceID");
		this.deviceIP = (String) objectData.get("deviceIP");
		this.leaderID = (String) objectData.get("leaderID");
		this.leaderIP = (String) objectData.get("leaderIP");
		this.backupIP = (String) objectData.get("backupIP");
		this.authenticated = (Boolean) objectData.get("authenticated");
		this.connected = (Boolean) objectData.get("connected");
		this.isLeader = (Boolean) objectData.get("isLeader");
		this.childrenIPs = (ArrayList<String>) objectData.get("childrenIPs");
	}
	
	// Setters (a setter for each property called "set_propertyname")
	public void set_deviceID(final String deviceID) {
		this.deviceID = deviceID;
	}
	
	public void set_deviceIP(final String deviceIP) {
		this.deviceIP = deviceIP;
	}
	
	public void set_leaderID(final String leaderID) {
		this.leaderID = leaderID;
	}
	
	public void set_leaderIP(final String leaderIP) {
		this.leaderIP = leaderIP;
	}
	
	
	public String get_leaderIP() {
		return this.leaderIP;
	}
	
	public void set_backupIP(final String backupIP) {
		this.backupIP = backupIP;
	}
	
	
	public String get_backupIP() {
		return this.backupIP;
	}
	
	
	public void set_authenticated(final Boolean authenticated) {
		this.authenticated = authenticated;
	}
	
	public void set_connected(final Boolean connected) {
		this.connected = connected;
	}
	
	
	public void set_isLeader(final Boolean isLeader) {
		this.isLeader = isLeader;
	}
	
	public void set_childrenIPs(final ArrayList<String> childrenIPs) {
		this.childrenIPs = childrenIPs;
	}

	
	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "getCIMIResourceData"
	@Override
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = super.getCIMIResourceData();
		if (this.deviceID != null)
			info.put("deviceID", this.deviceID);
		if (this.deviceIP != null)
			info.put("deviceIP", this.deviceIP);
		if (this.leaderID != null)
			info.put("leaderID", this.leaderID);
		if (this.leaderIP != null)
			info.put("leaderAddress", this.leaderIP);
		if (this.authenticated != null)
			info.put("authenticated", this.authenticated);
		if (this.connected != null)
			info.put("connected", this.connected);
		if (this.isLeader != null)
			info.put("isLeader", this.isLeader);
		if (this.childrenIPs != null)
			info.put("childrenIPs", this.childrenIPs);
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
		if (data.get("isLeader") != null)
			set_isLeader((Boolean) data.get("isLeader"));
		if (data.get("deviceIP") != null)
			set_deviceIP((String) data.get("deviceIP"));
		if (data.get("childrenIPs") != null)
			set_childrenIPs((ArrayList<String>) data.get("childrenIPs"));
	}	
	
}
