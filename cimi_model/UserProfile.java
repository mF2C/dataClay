package CIMI;

import java.util.Map;

public class UserProfile extends CIMIResource {

	//An attribute for each field in the CIMI resource spec, with the same name
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	private Boolean service_consumer;
	private Boolean resource_contributor;
	
	//Constructor
	public UserProfile(Map<String, Object> objectData) {
		super(objectData);
		this.service_consumer = (Boolean) objectData.get("service_consumer");
		this.resource_contributor = (Boolean) objectData.get("resource_contributor");
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_service_consumer(boolean service_consumer) {
		this.service_consumer = service_consumer;
	}
	
	public void set_resource_contributor(boolean resource_contributor) {
		this.resource_contributor = resource_contributor;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "getCIMIResourceData"	//For all classes, I will return a Map with all the info, called get_classname_info.
	public Map<String, Object> getCIMIResourceData() {
		Map<String, Object> info = getCIMIResourceData();
		if (this.service_consumer != null) info.put("service_consumer", this.service_consumer);
		if (this.resource_contributor != null) info.put("resource_contributor", this.resource_contributor);
		return info;
	}
	
	public void updateAllData(Map<String, Object> data) {
		super.setCIMIResourceData(data);
		if (data.get("service_consumer") != null) set_service_consumer((Boolean) data.get("service_consumer"));
		if (data.get("resource_contributor") != null) set_resource_contributor((Boolean) data.get("resource_contributor"));
	}

}