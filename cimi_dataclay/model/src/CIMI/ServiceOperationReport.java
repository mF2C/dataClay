package CIMI;

import java.util.Map;

public class ServiceOperationReport extends CIMIResource {

	private String compute_node_id;
	private Map<String, Object> requesting_application_id;
	private String operation_name;
	private String operation_id;
	private String start_time;
	private String expected_end_time;
	private String result;
	private Float execution_length;

	// Constructor
	public ServiceOperationReport(final Map<String, Object> objectData) {
		super(objectData);

		this.compute_node_id = (String) objectData.get("compute_node_id");
		this.requesting_application_id = (Map<String, Object>) objectData.get("requesting_application_id");

		this.operation_name = (String) objectData.get("operation_name");
		this.operation_id = (String) objectData.get("operation_id");
		this.start_time = (String) objectData.get("start_time");
		this.expected_end_time = (String) objectData.get("expected_end_time");
		this.result = (String) objectData.get("result");

		this.execution_length = getFloat(objectData.get("execution_length"));

	}

	// Setters (a setter for each property called "set_propertyname")
	public void set_compute_node_id(final String compute_node_id) {
		this.compute_node_id = compute_node_id;
	}
	
	public void set_requesting_application_id(final Map<String, Object> requesting_application_id) {
		this.requesting_application_id = requesting_application_id;
	}
	
	public void set_operation_name(final String operation_name) {
		this.operation_name = operation_name;
	}

	public void set_operation_id(final String operation_id) {
		this.operation_id = operation_id;
	}

	public void set_start_time(final String start_time) {
		this.start_time = start_time;
	}
	
	public void set_result(final String result) {
		this.result = result;
	}

	public void set_expected_end_time(final String expected_end_time) {
		this.expected_end_time = expected_end_time;
	}
	
	public void set_execution_length(final Float execution_length) {
		this.execution_length = execution_length;
	}

	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "get_classname_info"
	@Override
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = super.getCIMIResourceData();
		if (this.compute_node_id != null)
			info.put("compute_node_id", this.compute_node_id);
		if (this.requesting_application_id != null)
			info.put("requesting_application_id", this.requesting_application_id);
		if (this.operation_name != null)
			info.put("operation_name", this.operation_name);
		if (this.operation_id != null)
			info.put("operation_id", this.operation_id);
		if (this.start_time != null)
			info.put("start_time", this.start_time);
		if (this.result != null)
			info.put("result", this.result);
		if (this.expected_end_time != null)
			info.put("expected_end_time", this.expected_end_time);
		if (this.execution_length != null)
			info.put("execution_length", this.execution_length);
		return info;
	}

	public void updateAllData(final Map<String, Object> data) {
		setCIMIResourceData(data);
		if (data.get("compute_node_id") != null)
			set_compute_node_id((String)data.get("compute_node_id"));
		if (data.get("requesting_application_id") != null)
			set_requesting_application_id((Map<String, Object>)data.get("requesting_application_id"));
		if (data.get("operation_name") != null)
			set_operation_name((String) data.get("operation_name"));
		if (data.get("operation_id") != null)
			set_operation_id((String) data.get("operation_id"));
		if (data.get("start_time") != null)
			set_start_time((String) data.get("start_time"));
		if (data.get("result") != null)
			set_result((String) data.get("result"));
		if (data.get("expected_end_time") != null)
			set_expected_end_time((String) data.get("expected_end_time"));
		if (data.get("execution_length") != null)
			set_execution_length(getFloat(data.get("execution_length")));

	}

}
