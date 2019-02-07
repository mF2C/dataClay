package CIMI;

import java.util.Map;

@SuppressWarnings("serial")
public class UserProfile extends CIMIResource {

	// An attribute for each field in the CIMI resource spec, with the same name
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	private String user_id;
    private String device_id;
	private Boolean service_consumer;
	private Boolean resource_contributor;
	private Integer max_apps;

	// Constructor, with a parameter for each attribute in this class and in
	// CIMIResource
	public UserProfile(final Map<String, Object> objectData) {
		super(objectData);
		this.user_id = (String) objectData.get("user_id");
		this.device_id = (String) objectData.get("device_id");
		this.service_consumer = (Boolean) objectData.get("service_consumer");
		this.resource_contributor = (Boolean) objectData.get("resource_contributor");
		this.max_apps = (Integer) objectData.get("max_apps");
	}

	// Setters (a setter for each property called "set_propertyname")
	public void set_user_id(final String user_id) {
		this.user_id = user_id;
	}
	
	public void set_device_id(final String device_id) {
		this.device_id = device_id;
	}
	
	public void set_service_consumer(final Boolean service_consumer) {
		this.service_consumer = service_consumer;
	}

	public void set_resource_contributor(final Boolean resource_contributor) {
		this.resource_contributor = resource_contributor;
	}
	
	public void set_max_apps(final Integer max_apps) {
		this.max_apps = max_apps;
	}

	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "getCIMIResourceData" //For all classes, I will return a
	// Map with all the info, called get_classname_info.
	@Override
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = super.getCIMIResourceData();
		if (this.user_id != null)
			info.put("user_id", this.user_id);
		if (this.device_id != null)
			info.put("device_id", this.device_id);
		if (this.service_consumer != null)
			info.put("service_consumer", this.service_consumer);
		if (this.resource_contributor != null)
			info.put("resource_contributor", this.resource_contributor);
		if (this.max_apps != null)
			info.put("max_apps", this.max_apps);
		return info;
	}

	public void updateAllData(final Map<String, Object> data) {
		super.setCIMIResourceData(data);
		if (data.get("user_id") != null)
			set_user_id((String) data.get("user_id"));
		if (data.get("device_id") != null)
			set_device_id((String) data.get("device_id"));
		if (data.get("service_consumer") != null)
			set_service_consumer((Boolean) data.get("service_consumer"));
		if (data.get("resource_contributor") != null)
			set_resource_contributor((Boolean) data.get("resource_contributor"));
		if (data.get("max_apps") != null)
			set_max_apps((Integer) data.get("max_apps"));
	}

}
