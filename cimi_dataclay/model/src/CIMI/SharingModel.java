package CIMI;

import java.util.Map;

@SuppressWarnings("serial")
public class SharingModel extends CIMIResource {

	// An attribute for each field in the CIMI resource spec, with the same name
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	private String user_id;
    private String device_id;
	private Boolean gps_allowed;
	private Integer max_cpu_usage;
	private Integer max_memory_usage;
	private Integer max_storage_usage;
	private Integer max_bandwidth_usage;
	private Integer battery_limit;

	// Constructor, with a parameter for each attribute in this class and in
	// CIMIResource
	public SharingModel(final Map<String, Object> objectData) {
		super(objectData);
		this.user_id = (String) objectData.get("user_id");
		this.device_id = (String) objectData.get("device_id");
		this.gps_allowed = (Boolean) objectData.get("gps_allowed");
		this.max_cpu_usage = (Integer) objectData.get("max_cpu_usage");
		this.max_memory_usage = (Integer) objectData.get("max_memory_usage");
		this.max_storage_usage = (Integer) objectData.get("max_storage_usage");
		this.max_bandwidth_usage = (Integer) objectData.get("max_bandwidth_usage");
		this.battery_limit = (Integer) objectData.get("battery_limit");
	}

	// Setters (a setter for each property called "set_propertyname")
	public void set_user_id(final String user_id) {
		this.user_id = user_id;
	}
	
	public void set_device_id(final String device_id) {
		this.device_id = device_id;
	}

	public void set_gps_allowed(final Boolean gps_allowed) {
		this.gps_allowed = gps_allowed;
	}

	public void set_max_cpu_usage(final Integer max_cpu_usage) {
		this.max_cpu_usage = max_cpu_usage;
	}

	public void set_max_memory_usage(final Integer max_memory_usage) {
		this.max_memory_usage = max_memory_usage;
	}

	public void set_max_storage_usage(final Integer max_storage_usage) {
		this.max_storage_usage = max_storage_usage;
	}

	public void set_max_bandwidth_usage(final Integer max_bandwidth_usage) {
		this.max_bandwidth_usage = max_bandwidth_usage;
	}

	public void set_battery_limit(final Integer battery_limit) {
		this.battery_limit = battery_limit;
	}

	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "getCIMIResourceData"
	@Override
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = super.getCIMIResourceData();
		if (this.user_id != null)
			info.put("user_id", this.user_id);
		if (this.device_id != null)
			info.put("device_id", this.device_id);
		if (this.gps_allowed != null)
			info.put("gps_allowed", this.gps_allowed);
		if (this.max_cpu_usage != null)
			info.put("max_cpu_usage", this.max_cpu_usage);
		if (this.max_memory_usage != null)
			info.put("max_memory_usage", this.max_memory_usage);
		if (this.max_storage_usage != null)
			info.put("max_storage_usage", this.max_storage_usage);
		if (this.max_bandwidth_usage != null)
			info.put("max_bandwidth_usage", this.max_bandwidth_usage);
		if (this.battery_limit != null)
			info.put("battery_limit", this.battery_limit);
		return info;
	}

	public void updateAllData(final Map<String, Object> data) {
		super.setCIMIResourceData(data);
		if (data.get("user_id") != null)
			set_user_id((String) data.get("user_id"));
		if (data.get("device_id") != null)
			set_device_id((String) data.get("device_id"));
		if (data.get("gps_allowed") != null)
			set_gps_allowed((Boolean) data.get("gps_allowed"));
		if (data.get("max_cpu_usage") != null)
			set_max_cpu_usage((Integer) data.get("max_cpu_usage"));
		if (data.get("max_memory_usage") != null)
			set_max_memory_usage((Integer) data.get("max_memory_usage"));
		if (data.get("max_storage_usage") != null)
			set_max_storage_usage((Integer) data.get("max_storage_usage"));
		if (data.get("max_bandwidth_usage") != null)
			set_max_bandwidth_usage((Integer) data.get("max_bandwidth_usage"));
		if (data.get("battery_limit") != null)
			set_battery_limit((int) data.get("battery_limit"));
	}
}
