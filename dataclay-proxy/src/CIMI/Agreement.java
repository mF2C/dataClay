package CIMI;

import java.util.Map;

@SuppressWarnings({ "unchecked", "serial" })
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
	//	creation: String
	//	expiration: String
	//	guarantees: List<Map<String, String>> - another nested field: name, constraint
	private Map<String, Object> assessment; //opt
	//assessment is a nested field
	//	first_execution: String
	//	last_execution: String
	
	
	public Agreement(Map<String, Object> objectData) {
		super(objectData);
		this.state = (String) objectData.get("state");
		this.details = (Map<String, Object>) objectData.get("details");
		this.assessment = (Map<String, Object>) objectData.get("assessment");
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_state(String state) {
		this.state = state;
	}
	
	public void set_details(Map<String, Object> details) {
		this.details = details;
	}
	
	public void set_assessment(Map<String, Object> assessment) {
		this.assessment = assessment;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "getCIMIResourceData"
	public Map<String, Object> getCIMIResourceData() {
		Map<String, Object> info = super.getCIMIResourceData();
		if (this.state != null) info.put("state", this.state);
		if (this.details != null) info.put("details", this.details);
		if (this.assessment != null)info.put("assessment", this.assessment);
		return info;
	}
	
	public void updateAllData(Map<String, Object> data) {
		setCIMIResourceData(data);
		if (data.get("state") != null) set_state((String) data.get("state"));
		if (data.get("details") != null) set_details((Map<String, Object>) data.get("details"));
		if (data.get("assessment") != null) set_assessment((Map<String, Object>) data.get("assessment"));
	}

}
