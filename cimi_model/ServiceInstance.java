package CIMI;

import java.util.List;
import java.util.Map;

public class ServiceInstance extends CIMIResource {
	
	//An attribute for each field in the CIMI resource spec, with the same name
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	private String service_id;
	private String agreement_id;
	private String status;
	private List<Map<String, Object>> agents;
		//agents is a collection of "nested" info:
		//	agent: Agent -> TODO: the Agent resource does not exist!!!!
		//	url: String
		//	port: int
		//	container_id: String
		//	status: String
		//	num_cpus: int
	
	//Constructor, with a parameter for each attribute in this class and in CIMIResource
	public ServiceInstance(String service_id, String agreement_id, String status, 
			List<Map<String, Object>> agents,
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
		
		super(resourceID, resourceName, resourceDescription, resourceURI);
		this.service_id = service_id;
		this.agreement_id = agreement_id;
		this.status = status;
		this.agents = agents;
	}
		
	//Setters (a setter for each property called "set_propertyname")
	public void set_service_id(String service_id) {
		this.service_id = service_id;
	}
	
	public void agreement_id(String agreement_id) {
		this.agreement_id = agreement_id;
	}
	
	public void set_status(String status) {
		this.status = status;
	}
	
	public void set_agents(List<Map<String, Object>> agents) {
		this.agents = agents;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> get_ServiceInstance_info() {
		Map<String, Object> info = getCIMIResourceInfo();
		info.put("service_id", this.service_id);
		info.put("agreement_id", this.agreement_id);
		info.put("status", this.status);
		info.put("agents", this.agents);
		return info;
	}
		

}
