package CIMI;

import java.util.List;
import java.util.Map;

import dataclay.util.replication.Replication;

@SuppressWarnings({ "unchecked", "serial" })
public class ServiceInstance extends CIMIResource {

	// An attribute for each field in the CIMI resource spec, with the same name
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	private String user;
	private String device_id; 
	private String device_ip; 
	private String parent_device_id;
	private String parent_device_ip;
	private String service;
	private String service_type;
	private String agreement;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private String status;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private List<Map<String, Object>> agents;
	// agents is a collection of "nested" info:
	// agent: String (resource link)
	// app_type: string
	// url: String
	// ports: List<Integer>
	// agent_param: String
	// container_id: String
	// status: String
	// num_cpus: int
	// allow: bool
	// master_compss: bool

	// Constructor
	public ServiceInstance(final Map<String, Object> objectData) {
		super(objectData);
		this.user = (String) objectData.get("user");
		this.device_id = (String) objectData.get("device_id");
		this.device_ip = (String) objectData.get("device_ip");
		this.parent_device_id = (String) objectData.get("parent_device_id");
		this.parent_device_ip = (String) objectData.get("parent_device_ip");
		this.service = (String) objectData.get("service");
		this.service_type = (String) objectData.get("service_type");
		this.agreement = (String) objectData.get("agreement");
		this.status = (String) objectData.get("status");
		this.agents = (List<Map<String, Object>>) objectData.get("agents");
	}

	// Setters (a setter for each property called "set_propertyname")
	public void set_user(final String user) {
		this.user = user;
	}
	
	public void set_device_id(final String device_id) {
		this.device_id = device_id;
	}
	
	public void set_device_ip(final String device_ip) {
		this.device_ip = device_ip;
	}
	
	public void set_parent_device_id(final String parent_device_id) {
		this.parent_device_id = parent_device_id;
	}
	
	public void set_parent_device_ip(final String parent_device_ip) {
		this.parent_device_ip = parent_device_ip;
	}
	
	public void set_service(final String service) {
		this.service = service;
	}
	
	public void set_service_type(final String service_type) {
		this.service_type = service_type;
	}

	public void set_agreement(final String agreement) {
		this.agreement = agreement;
	}

	public void set_status(final String status) {
		this.status = status;
	}

	public void set_agents(final List<Map<String, Object>> agents) {
		this.agents = agents;
	}

	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "getCIMIResourceData"
	@Override
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = super.getCIMIResourceData();
		if (this.user != null)
			info.put("user", this.user);
		if (this.device_id != null)
			info.put("device_id", this.device_id);
		if (this.device_ip != null)
			info.put("device_ip", this.device_ip);
		if (this.parent_device_id != null)
			info.put("parent_device_id", this.parent_device_id);
		if (this.parent_device_ip != null)
			info.put("parent_device_ip", this.parent_device_ip);
		if (this.service != null)
			info.put("service", this.service);
		if (this.service_type != null)
			info.put("service_type", this.service_type);
		if (this.agreement != null)
			info.put("agreement", this.agreement);
		if (this.status != null)
			info.put("status", this.status);
		if (this.agents != null)
			info.put("agents", this.agents);
		return info;
	}

	public void updateAllData(final Map<String, Object> data) {
		super.setCIMIResourceData(data);
		if (data.get("user") != null)
			set_user((String) data.get("user"));
		if (data.get("device_id") != null)
			set_device_id((String) data.get("device_id"));
		if (data.get("device_ip") != null)
			set_device_ip((String) data.get("device_ip"));
		if (data.get("parent_device_id") != null)
			set_parent_device_id((String) data.get("parent_device_id"));
		if (data.get("parent_device_ip") != null)
			set_parent_device_ip((String) data.get("parent_device_ip"));
		if (data.get("service") != null)
			set_service((String) data.get("service"));
		if (data.get("service_type") != null)
			set_service_type((String) data.get("service_type"));
		if (data.get("agreement") != null)
			set_agreement((String) data.get("agreement"));
		if (data.get("status") != null)
			set_status((String) data.get("status"));
		if (data.get("agents") != null)
			set_agents((List<Map<String, Object>>) data.get("agents"));
	}

}
