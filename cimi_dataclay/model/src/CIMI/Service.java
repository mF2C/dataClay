package CIMI;

import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings({ "unchecked", "serial" })
public class Service extends CIMIResource {

	// An attribute for each field in the CIMI resource spec, with the same name
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	private String exec;
	private String exec_type; //docker, compss, ... (enum checked by CIMI)
	private ArrayList<Integer> exec_ports;
	private String agent_type; //cloud, normal or micro (enum checked by CIMI)
	private Integer num_agents;
	private String cpu_arch; //arm or x86-64 (enum checked by CIMI)
	private String os; //linux, mac, windows, ios, android (enum checked by CIMI)
	private Integer memory_min;
	private Integer storage_min;
	private Integer disk;
	private ArrayList<String> req_resource; 
	private ArrayList<String> opt_resource; 
	private Integer category;
	private ArrayList<Map<String, Object>> sla_templates; 
	
	// Constructor
	public Service(final Map<String, Object> objectData) {
		super(objectData);
		this.exec = (String) objectData.get("exec");
		this.exec_type = (String) objectData.get("exec_type");
		this.exec_ports = (ArrayList<Integer>) objectData.get("exec_ports");
		this.agent_type = (String) objectData.get("agent_type");
		this.num_agents = (Integer) objectData.get("num_agents");
		this.cpu_arch = (String) objectData.get("cpu_arch");
		this.os = (String) objectData.get("os");
		this.memory_min = (Integer) objectData.get("memory_min");
		this.storage_min = (Integer) objectData.get("storage_min");
		this.disk = (Integer) objectData.get("disk");
		this.req_resource = (ArrayList<String>) objectData.get("req_resource");
		this.opt_resource = (ArrayList<String>) objectData.get("opt_resource");
		this.category = (Integer) objectData.get("category");
		this.sla_templates = (ArrayList<Map<String, Object>>) objectData.get("sla_templates");
	}

	// Setters (a setter for each property called "set_propertyname")
	public void set_exec(final String exec) {
		this.exec = exec;
	}

	public void set_exec_type(final String exec_type) {
		this.exec_type = exec_type;
	}

	public void set_exec_ports(final ArrayList<Integer> exec_ports) {
		this.exec_ports = exec_ports;
	}
	
	public void set_agent_type(final String agent_type) {
		this.agent_type = agent_type;
	}
	
	public void set_num_agents(final Integer num_agents) {
		this.num_agents = num_agents;
	}
	
	public void set_cpu_arch(final String cpu_arch) {
		this.cpu_arch = cpu_arch;
	}
	
	public void set_os(final String os) {
		this.os = os;
	}
	
	public void set_memory_min(final Integer memory_min) {
		this.memory_min = memory_min;
	}
	
	public void set_storage_min(final Integer storage_min) {
		this.storage_min = storage_min;
	}
	
	public void set_disk(final Integer disk) {
		this.disk = disk;
	}
	
	public void set_req_resource(final ArrayList<String> req_resource) {
		this.req_resource = req_resource;
	}
	
	public void set_opt_resource(final ArrayList<String> opt_resource) {
		this.opt_resource = opt_resource;
	}

	public void set_category(final Integer category) {
		this.category = category;
	}
	
	public void set_sla_templates(final ArrayList<Map<String, Object>> sla_templates) {
		this.sla_templates = sla_templates;
	}

	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "getCIMIResourceData"
	@Override
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = super.getCIMIResourceData();
		if (this.exec != null)
			info.put("exec", this.exec);
		if (this.exec_type != null)
			info.put("exec_type", this.exec_type);
		if (this.exec_ports != null)
			info.put("exec_ports", this.exec_ports);
		if (this.agent_type != null)
			info.put("agent_type", this.agent_type);
		if (this.num_agents != null)
			info.put("num_agents", this.num_agents);
		if (this.cpu_arch != null)
			info.put("cpu_arch", this.cpu_arch);
		if (this.os != null)
			info.put("os", this.os);
		if (this.memory_min != null)
			info.put("memory_min", this.memory_min);
		if (this.storage_min != null)
			info.put("storage_min", this.storage_min);
		if (this.disk != null)
			info.put("disk", this.disk);
		if (this.req_resource != null)
			info.put("req_resource", this.req_resource);
		if (this.opt_resource != null)
			info.put("opt_resource", this.opt_resource);
		if (this.category != null)
			info.put("category", this.category);
		if (this.sla_templates != null)
			info.put("sla_templates", this.sla_templates);
		return info;
	}

	public void updateAllData(final Map<String, Object> data) {
		super.setCIMIResourceData(data);
		if (data.get("exec") != null)
			set_exec((String) data.get("exec"));
		if (data.get("exec_type") != null)
			set_exec_type((String) data.get("exec_type"));
		if (data.get("exec_ports") != null)
			set_exec_ports((ArrayList<Integer>) data.get("exec_ports"));
		if (data.get("agent_type") != null)
			set_agent_type((String) data.get("agent_type"));
		if (data.get("num_agents") != null)
			set_num_agents((Integer) data.get("num_agents"));
		if (data.get("cpu_arch") != null)
			set_cpu_arch((String) data.get("cpu_arch"));
		if (data.get("os") != null)
			set_os((String) data.get("os"));
		if (data.get("memory_min") != null)
			set_memory_min((Integer) data.get("memory_min"));
		if (data.get("storage_min") != null)
			set_storage_min((Integer) data.get("storage_min"));
		if (data.get("disk") != null)
			set_disk((Integer) data.get("disk"));
		if (data.get("req_resource") != null)
			set_req_resource((ArrayList<String>) data.get("req_resource"));
		if (data.get("opt_resource") != null)
			set_opt_resource((ArrayList<String>) data.get("opt_resource"));
		if (data.get("category") != null)
			set_category((Integer) data.get("category"));
		if (data.get("sla_templates") != null)
			set_sla_templates((ArrayList<Map<String, Object>>) data.get("sla_templates"));
	}

}
