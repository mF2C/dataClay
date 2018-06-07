package CIMI;

import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked", "serial" })
public class ServiceInstance extends CIMIResource {

	// An attribute for each field in the CIMI resource spec, with the same name
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	private String user;
	private String service;
	private String agreement;
	private String status;
	private List<Map<String, Object>> agents;
	// agents is a collection of "nested" info:
	// agent: String
	// url: String
	// port: int
	// container_id: String
	// status: String
	// num_cpus: int

	// Constructor
	public ServiceInstance(final Map<String, Object> objectData) {
		super(objectData);
		this.user = (String) objectData.get("user");
		this.service = (String) objectData.get("service");
		this.agreement = (String) objectData.get("agreement");
		this.status = (String) objectData.get("status");
		this.agents = (List<Map<String, Object>>) objectData.get("agents");
	}

	// Setters (a setter for each property called "set_propertyname")
	public void set_user(final String user) {
		this.user = user;
	}

	public void set_service(final String service) {
		this.service = service;
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
		if (this.service != null)
			info.put("service", this.service);
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
		if (data.get("service") != null)
			set_service((String) data.get("service"));
		if (data.get("agreement") != null)
			set_agreement((String) data.get("agreement"));
		if (data.get("status") != null)
			set_status((String) data.get("status"));
		if (data.get("agents") != null)
			set_agents((List<Map<String, Object>>) data.get("agents"));
	}

}
