package CIMI;

import java.util.Map;

public class Agreement extends CIMIResource {

	//An attribute for each field in the CIMI resource spec, with the same name and type.
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	private String state; //"started", "stopped" or "terminated"
	private Map<String, Object> details;
	//details is a nested field
	//	id: String
	//	type: String - Agreement or Template
	//	name: String
	//	provider: Map<String, String> - another nested field: id, name
	//	client: Map<String, String> - another nested field: id, name
	//	creation: DateTime
	//	expiration: DateTime
	//	guarantees: List<Map<String, String>> - another nested field: name, constraint
	private Map<String, Object> assessment; //opt
	//assessment is a nested field
	//	first_execution: DateTime
	//	last_execution: DateTime
	
	//Constructor, with a parameter for each attribute in this class and in CIMIResource
	public Agreement(String state, Map<String, Object> details, Map<String, Object> assessment,
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
		super(resourceID, resourceName, resourceDescription, resourceURI);
		this.details = details;
		this.assessment = assessment;
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_state(String state) {
		this.state = state;
	}
	
	public void details(Map<String, Object> details) {
		this.details = details;
	}
	
	public void assessment(Map<String, Object> assessment) {
		this.assessment = assessment;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> get_Agreement_info() {
		Map<String, Object> info = getCIMIResourceInfo();
		info.put("state", this.state);
		info.put("details", this.details);
		info.put("assessment", this.assessment);
		return info;
	}

}
