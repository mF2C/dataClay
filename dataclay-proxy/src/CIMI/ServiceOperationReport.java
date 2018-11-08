package CIMI;

import java.util.Map;

public class ServiceOperationReport extends CIMIResource {

	private ServiceInstance serviceInstance;
	private String operation;
	private Float execution_time;

	// Constructor
	public ServiceOperationReport(final Map<String, Object> objectData) {
		super(objectData);
		this.serviceInstance = (ServiceInstance) objectData.get("serviceInstance");

		this.operation = (String) objectData.get("operation");
		this.execution_time = getFloat(objectData.get("execution_time"));

	}

	// Setters (a setter for each property called "set_propertyname")
	public void set_serviceInstance(final ServiceInstance serviceInstance) {
		this.serviceInstance = serviceInstance;
	}

	public void set_operation(final String operation) {
		this.operation = operation;
	}

	public void set_execution_time(final Float execution_time) {
		this.execution_time = execution_time;
	}

	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "get_classname_info"
	@Override
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = super.getCIMIResourceData();
		if (this.serviceInstance != null)
			info.put("serviceInstance", this.serviceInstance);
		if (this.operation != null)
			info.put("operation", this.operation);
		if (this.execution_time != null)
			info.put("execution_time", this.execution_time);
		return info;
	}

	public void updateAllData(final Map<String, Object> data) {
		setCIMIResourceData(data);
		if (data.get("serviceInstance") != null)
			set_serviceInstance((ServiceInstance) data.get("serviceInstance"));
		if (data.get("operation") != null)
			set_operation((String) data.get("operation"));
		if (data.get("execution_time") != null)
			set_execution_time(getFloat(data.get("execution_time")));

	}

}
