package CIMI;

import java.util.Map;

public class UserProfile extends CIMIResource {

	//An attribute for each field in the CIMI resource spec, with the same name
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	private boolean service_consumer;
	private boolean resource_contributor;
	
	//Constructor, with a parameter for each attribute in this class and in CIMIResource
	public UserProfile(boolean service_consumer, boolean resource_contributor,
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
		super(resourceID, resourceName, resourceDescription, resourceURI);
		this.service_consumer = service_consumer;
		this.resource_contributor = resource_contributor;
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_service_consumer(boolean service_consumer) {
		this.service_consumer = service_consumer;
	}
	
	public void set_resource_contributor(boolean resource_contributor) {
		this.resource_contributor = resource_contributor;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"	//For all classes, I will return a Map with all the info, called get_classname_info.
	public Map<String, Object> get_UserProfile_info() {
		Map<String, Object> info = getCIMIResourceInfo();
		info.put("service_consumer", this.service_consumer);
		info.put("resource_contributor", this.resource_contributor);
		return info;
	}
}
