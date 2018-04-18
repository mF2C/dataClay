package CIMI;

import java.util.Map;

public class SharingModel extends CIMIResource {

	//An attribute for each field in the CIMI resource spec, with the same name
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	private Integer max_apps;
	private Boolean gps_allowed;
	private Integer max_cpu_usage;
	private Integer max_memory_usage;
	private Integer max_storage_usage;
	private Integer max_bandwidth_usage;
	private Integer battery_limit;
	
	//Constructor
	public SharingModel(Map<String, Object> objectData) {
		super(objectData);
		this.max_apps = (int) objectData.get("max_apps");
		this.gps_allowed = (boolean) objectData.get("gps_allowed");
		this.max_cpu_usage = (int) objectData.get("max_cpu_usage");
		this.max_memory_usage = (int) objectData.get("max_memory_usage");
		this.max_storage_usage = (int) objectData.get("max_storage_usage");
		this.max_bandwidth_usage = (int) objectData.get("max_bandwidth_usage");
		this.battery_limit = (int) objectData.get("battery_limit");
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_max_apps(int max_apps) {
		this.max_apps = max_apps;
	}
	
	public void set_gps_allowed(boolean gps_allowed) {
		this.gps_allowed = gps_allowed;
	}
	
	public void set_max_cpu_usage(int max_cpu_usage) {
		this.max_cpu_usage = max_cpu_usage;
	}
	
	public void set_max_memory_usage(int max_memory_usage) {
		this.max_memory_usage = max_memory_usage;
	}
	
	public void set_max_storage_usage(int max_storage_usage) {
		this.max_storage_usage = max_storage_usage;
	}
	
	public void set_max_bandwidth_usage(int max_bandwidth_usage) {
		this.max_bandwidth_usage = max_bandwidth_usage;
	}
	
	public void set_battery_limit(int battery_limit) {
		this.battery_limit = battery_limit;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "getCIMIResourceData"
	public Map<String, Object> getCIMIResourceData() {
		Map<String, Object> info = getCIMIResourceData();
		if (this.max_apps != null) info.put("max_apps", this.max_apps);
		if (this.gps_allowed != null) info.put("gps_allowed", this.gps_allowed);
		if (this.max_cpu_usage != null) info.put("max_cpu_usage", this.max_cpu_usage);
		if (this.max_memory_usage != null) info.put("max_memory_usage", this.max_memory_usage);
		if (this.max_storage_usage != null) info.put("max_storage_usage", this.max_storage_usage);
		if (this.max_bandwidth_usage != null) info.put("max_bandwidth_usage", this.max_bandwidth_usage);
		if (this.battery_limit != null) info.put("battery_limit", this.battery_limit);
		return info;
	}
	
	public void updateAllData(Map<String, Object> data) {
		super.setCIMIResourceData(data);
		if (data.get("max_apps") != null) set_max_apps((int) data.get("max_apps"));
		if (data.get("gps_allowed") != null) set_gps_allowed((boolean) data.get("gps_allowed"));
		if (data.get("max_cpu_usage") != null) set_max_cpu_usage((int) data.get("max_cpu_usage"));
		if (data.get("max_memory_usage") != null) set_max_memory_usage((int) data.get("max_memory_usage"));
		if (data.get("max_storage_usage") != null) set_max_storage_usage((int) data.get("max_storage_usage"));
		if (data.get("max_bandwidth_usage") != null) set_max_bandwidth_usage((int) data.get("max_bandwidth_usage"));
		if (data.get("battery_limit") != null) set_battery_limit((int) data.get("battery_limit"));
	}
}
