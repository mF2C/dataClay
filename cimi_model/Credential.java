package CIMI;

import java.util.Map;

public class Credential extends CIMIResource {
	
	//An attribute for each field in the CIMI resource spec, with the same name 
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value

	// credential-template/type
	private String type; 

	private String method; 
	//credential-template/method
	
	//Constructor, with a parameter for each attribute in this class and in CIMIResource
	public Credential(String type, String method,  
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
			
		super(resourceID, resourceName, resourceDescription, resourceURI);
		this.method = method;
		this.type = type;
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_method(String method) {
		this.method = method;
	}

	public void set_type(String type) {
		this.type = type;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> get_Credential_info() {
		Map<String, Object> info = getCIMIResourceInfo();
		info.put("type", this.type);
		info.put("method", this.method);
		return info;
	}
	
}
