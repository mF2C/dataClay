package CIMI;

import java.math.BigDecimal;
import java.util.Map;

public class ServiceOperationReport extends CIMIResource {

	private ServiceInstance serviceInstance;
	private String operation;
	private Float execution_time;

	// Constructor
	public ServiceOperationReport(Map<String, Object> objectData) {
		super(objectData);
		this.serviceInstance = (ServiceInstance) objectData.get("serviceInstance");

		this.operation = (String) objectData.get("operation");
		Double d = (Double) objectData.get("execution_time");
		if (d != null) {
			this.execution_time = BigDecimal.valueOf(d).floatValue();
		}
	}

	// Setters (a setter for each property called "set_propertyname")
	public void set_serviceInstance(ServiceInstance serviceInstance) {
		this.serviceInstance = serviceInstance;
	}

	public void set_operation(String operation) {
		this.operation = operation;
	}

	public void set_execution_time(Float execution_time) {
		this.execution_time = execution_time;
	}

	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "get_classname_info"
	public Map<String, Object> getCIMIResourceData() {
		Map<String, Object> info = super.getCIMIResourceData();
		if (this.serviceInstance != null)
			info.put("serviceInstance", this.serviceInstance);
		if (this.operation != null)
			info.put("operation", this.operation);
		if (this.execution_time != null)
			info.put("execution_time", this.execution_time);
		return info;
	}

	public void updateAllData(Map<String, Object> data) {
		setCIMIResourceData(data);
		if (data.get("serviceInstance") != null)
			set_serviceInstance((ServiceInstance) data.get("serviceInstance"));
		if (data.get("operation") != null)
			set_operation((String) data.get("operation"));
		if (data.get("execution_time") != null)
			set_execution_time((Float) data.get("execution_time"));
	}

}
