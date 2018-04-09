package CIMI;

import java.util.Map;

public class SharingModel extends CIMIResource {

	//An attribute for each field in the CIMI resource spec, with the same name
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	private int max_apps;
	private boolean gps_allowed;
	private int max_cpu_usage;
	private int max_memory_usage;
	private int max_storage_usage;
	private int max_bandwidth_usage;
	private int battery_limit;
	
	//Constructor, with a parameter for each attribute in this class and in CIMIResource
	public SharingModel(int max_apps, boolean gps_allowed, int max_cpu_usage, int max_memory_usage,
			int max_storage_usage, int max_bandwidth_usage, int battery_limit,
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
		super(resourceID, resourceName, resourceDescription, resourceURI);
		this.max_apps = max_apps;
		this.gps_allowed = gps_allowed;
		this.max_cpu_usage = max_cpu_usage;
		this.max_memory_usage = max_memory_usage;
		this.max_storage_usage = max_storage_usage;
		this.max_bandwidth_usage = max_bandwidth_usage;
		this.battery_limit = battery_limit;
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
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> get_SharingModel_info() {
		Map<String, Object> info = getCIMIResourceInfo();
		info.put("max_apps", this.max_apps);
		info.put("gps_allowed", this.gps_allowed);
		info.put("max_cpu_usage", this.max_cpu_usage);
		info.put("max_memory_usage", this.max_memory_usage);
		info.put("max_storage_usage", this.max_storage_usage);
		info.put("max_bandwidth_usage", this.max_bandwidth_usage);
		info.put("battery_limit", this.battery_limit);
		return info;
	}
}
