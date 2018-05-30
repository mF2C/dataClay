package CIMI;

import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings({ "unchecked", "serial" })
public class Service extends CIMIResource {

	// An attribute for each field in the CIMI resource spec, with the same name
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	private String exec;
	private String exec_type;
	private ArrayList<Integer> exec_ports;
	private Map<String, Object> category;
	// category contains "nested" info:
	// cpu: String - high, medium, low
	// memory: String - high, medium, low
	// storage: String - high, medium, low
	// inclinometer: boolean
	// temperature: boolean
	// jammer: boolean
	// location: boolean

	// Constructor
	public Service(final Map<String, Object> objectData) {
		super(objectData);
		this.exec = (String) objectData.get("exec");
		this.exec_type = (String) objectData.get("exec_type");
		this.exec_ports = (ArrayList<Integer>) objectData.get("exec_ports");
		this.category = (Map<String, Object>) objectData.get("category");
	}

	// Setters (a setter for each property called "set_propertyname")
	public String get_exec() {
		return exec;
	}

	public void set_exec(final String exec) {
		this.exec = exec;
	}

	public String get_exec_type() {
		return exec_type;
	}

	public void set_exec_type(final String exec_type) {
		this.exec_type = exec_type;
	}

	public ArrayList<Integer> get_exec_ports() {
		return exec_ports;
	}

	public void set_exec_ports(final ArrayList<Integer> exec_ports) {
		this.exec_ports = exec_ports;
	}

	public void set_category(final Map<String, Object> newCategory) {
		this.category = newCategory;
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
		if (this.category != null)
			info.put("category", this.category);
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
		if (data.get("category") != null)
			set_category((Map<String, Object>) data.get("category"));
	}

}
