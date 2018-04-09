package CIMI;

import java.util.Date;
import java.util.Map;

public class SLAViolation extends CIMIResource {
	//An attribute for each field in the CIMI resource spec, with the same name and type.
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	private String agreement_id;
	private String guarantee;
	private Date datetime;
	
	//Constructor, with a parameter for each attribute in this class and in CIMIResource
	public SLAViolation(String agreement_id, String guarantee, Date datetime,
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
		super(resourceID, resourceName, resourceDescription, resourceURI);
		this.agreement_id = agreement_id;
		this.guarantee = guarantee;
		this.datetime = datetime;
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_agreement_id(String agreement_id) {
		this.agreement_id = agreement_id;
	}
	
	public void set_guarantee(String guarantee) {
		this.guarantee = guarantee;
	}
	
	public void set_datetime(Date datetime) {
		this.datetime = datetime;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> get_SLAViolation_info() {
		Map<String, Object> info = getCIMIResourceInfo();
		info.put("agreement_id", this.agreement_id);
		info.put("guarantee", this.guarantee);
		info.put("datetime", this.datetime);
		return info;
	}

}
