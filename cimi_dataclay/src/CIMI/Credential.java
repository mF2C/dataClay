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
	
	//Constructor
	public Credential(Map<String, Object> objectData) {
		super(objectData);
		this.method = (String) objectData.get("method");
		this.type = (String) objectData.get("type");
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_method(String method) {
		this.method = method;
	}

	public void set_type(String type) {
		this.type = type;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> getCIMIResourceData() {
		Map<String, Object> info = super.getCIMIResourceData();
		if (this.type != null) info.put("type", this.type);
		if (this.method != null) info.put("method", this.method);
		return info;
	}
	
	public void updateAllData(Map<String, Object> data) {
		setCIMIResourceData(data);
		if (data.get("type") != null) set_type((String) data.get("type"));
		if (data.get("method") != null) set_method((String) data.get("method"));
	}
}