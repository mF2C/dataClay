package CIMI;

import java.util.Map;

public class Service extends CIMIResource {
	
	//An attribute for each field in the CIMI resource spec, with the same name 
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	private Map<String, Object> category;
	//category contains "nested" info:
	//	cpu: String - high, medium, low
	//	memory: String - high, medium, low
	//	storage: String - high, medium, low
	//	inclinometer: boolean
	//	temperature: boolean
	//	jammer: boolean
	//	location: boolean
	
	//Constructor, with a parameter for each attribute in this class and in CIMIResource
	public Service(Map<String, Object> category,  
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
			
		super(resourceID, resourceName, resourceDescription, resourceURI);
		this.category = category;
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_category(Map<String, Object> newCategory) {
		this.category = newCategory;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> get_Service_info() {
		Map<String, Object> info = getCIMIResourceInfo();
		info.put("category", this.category);
		return info;
	}
	
}
