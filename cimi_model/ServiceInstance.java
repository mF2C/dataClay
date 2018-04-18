package CIMI;

import java.util.List;
import java.util.Map;

public class ServiceInstance extends CIMIResource {
	
	//An attribute for each field in the CIMI resource spec, with the same name
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	private String user_id;
	private String service_id;
	private String agreement_id;
	private String status;
	private List<Map<String, Object>> agents;
		//agents is a collection of "nested" info:
		//	agent: String
		//	url: String
		//	port: int
		//	container_id: String
		//	status: String
		//	num_cpus: int
	
	//Constructor
	public ServiceInstance(Map<String, Object> objectData) {
		super(objectData);
		this.user_id = (String) objectData.get("user_id");
		this.service_id = (String) objectData.get("service_id");
		this.agreement_id = (String) objectData.get("agreement_id");
		this.status = (String) objectData.get("status");
		this.agents = (List<Map<String, Object>>) objectData.get("agents");
	}
		
	//Setters (a setter for each property called "set_propertyname")
	public void set_user_id(String user_id) {
		this.user_id = user_id;
	}
	
	public void set_service_id(String service_id) {
		this.service_id = service_id;
	}
	
	public void set_agreement_id(String agreement_id) {
		this.agreement_id = agreement_id;
	}
	
	public void set_status(String status) {
		this.status = status;
	}
	
	public void set_agents(List<Map<String, Object>> agents) {
		this.agents = agents;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "getCIMIResourceData"
	public Map<String, Object> getCIMIResourceData() {
		Map<String, Object> info = super.getCIMIResourceData();
		if (this.user_id != null) info.put("user_id", this.user_id);
		if (this.service_id != null) info.put("service_id", this.service_id);
		if (this.agreement_id != null) info.put("agreement_id", this.agreement_id);
		if (this.status != null) info.put("status", this.status);
		if (this.agents != null) info.put("agents", this.agents);
		return info;
	}
	
	public void updateAllData(Map<String, Object> data) {
		super.setCIMIResourceData(data);
		if (data.get("user_id") != null) set_user_id((String) data.get("user_id"));
		if (data.get("service_id") != null) set_service_id((String) data.get("service_id"));
		if (data.get("agreement_id") != null) set_agreement_id((String) data.get("agreement_id"));
		if (data.get("status") != null) set_status((String) data.get("status"));
		if (data.get("agents") != null) set_agents((List<Map<String, Object>>) data.get("agents"));
	}
		

}