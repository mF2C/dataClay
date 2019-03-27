package CIMI;

import java.util.ArrayList;
import java.util.Map;

public class Agent extends CIMIResource {
	// An attribute for each field in the CIMI resource spec, with the same name and
	// type.
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	private String device_id;
	private String device_ip;
	private String leader_id;
	private String leader_ip;
	private String backup_ip;
	private Boolean authenticated;
	private Boolean connected;
	private Boolean isLeader;
	private ArrayList<String> childrenIPs;
	
	public Agent(final Map<String, Object> objectData) {
		super(objectData);
		this.device_id = (String) objectData.get("device_id");
		this.device_ip = (String) objectData.get("device_ip");
		this.leader_id = (String) objectData.get("leader_id");
		this.leader_ip = (String) objectData.get("leader_ip");
		this.backup_ip = (String) objectData.get("backup_ip");
		this.authenticated = (Boolean) objectData.get("authenticated");
		this.connected = (Boolean) objectData.get("connected");
		this.isLeader = (Boolean) objectData.get("isLeader");
		this.childrenIPs = (ArrayList<String>) objectData.get("childrenIPs");
	}
	
	// Setters (a setter for each property called "set_propertyname")
	public void set_device_id(final String device_id) {
		this.device_id = device_id;
	}
	
	public void set_device_ip(final String device_ip) {
		this.device_ip = device_ip;
	}
	
	public void set_leader_id(final String leader_id) {
		this.leader_id = leader_id;
	}
	
	public void set_leader_ip(final String leader_ip) {
		this.leader_ip = leader_ip;
	}
	
	
	public String get_leader_ip() {
		return this.leader_ip;
	}
	
	public void set_backup_ip(final String backup_ip) {
		this.backup_ip = backup_ip;
	}
	
	
	public String get_backup_ip() {
		return this.backup_ip;
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
		if (this.device_id != null)
			info.put("device_id", this.device_id);
		if (this.device_ip != null)
			info.put("device_ip", this.device_ip);
		if (this.leader_id != null)
			info.put("leader_id", this.leader_id);
		if (this.leader_ip != null)
			info.put("leaderAddress", this.leader_ip);
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
		if (data.get("device_id") != null)
			set_device_id((String) data.get("device_id"));
		if (data.get("leader_id") != null)
			set_leader_id((String) data.get("leader_id"));
		if (data.get("leader_ip") != null)
			set_leader_ip((String) data.get("leader_ip"));
		if (data.get("authenticated") != null)
			set_authenticated((Boolean) data.get("authenticated"));
		if (data.get("connected") != null)
			set_connected((Boolean) data.get("connected"));
		if (data.get("isLeader") != null)
			set_isLeader((Boolean) data.get("isLeader"));
		if (data.get("device_ip") != null)
			set_device_ip((String) data.get("device_ip"));
		if (data.get("childrenIPs") != null)
			set_childrenIPs((ArrayList<String>) data.get("childrenIPs"));
	}	
	
}
