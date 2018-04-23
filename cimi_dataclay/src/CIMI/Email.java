package CIMI;

import java.util.Map;

public class Email extends CIMIResource {
	
	//An attribute for each field in the CIMI resource spec, with the same name 
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
    private String address;
    private Boolean validated;

	//Constructor
	public Email(Map<String, Object> objectData) {
		super(objectData);
        this.validated = (Boolean) objectData.get("validated");
        this.address = (String) objectData.get("address");
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_address(String address) {
		this.address = address;
    }
    
    public void set_validated(Boolean validated) {
		this.validated = validated;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> getCIMIResourceData() {
		Map<String, Object> info = super.getCIMIResourceData();
		if (this.address != null) info.put("address", this.address);
		if (this.validated != null) info.put("validated", this.validated);
		return info;
	}
	
	public void updateAllData(Map<String, Object> data) {
		setCIMIResourceData(data);
		if (data.get("address") != null) set_address((String) data.get("address"));
		if (data.get("validated") != null) set_validated((Boolean) data.get("validated"));
	}
	
}