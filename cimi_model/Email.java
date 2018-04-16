package CIMI;

import java.util.Map;

public class Email extends CIMIResource {
	
	//An attribute for each field in the CIMI resource spec, with the same name 
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
    private String address;
    private Boolean validated;

	//Constructor, with a parameter for each attribute in this class and in CIMIResource
	public Email(String address, Boolean validated,
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
			
		super(resourceID, resourceName, resourceDescription, resourceURI);
        this.validated = validated;
        this.address = address;
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_address(String address) {
		this.address = address;
    }
    
    public void set_validated(Boolean validated) {
		this.validated = validated;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> get_Email_info() {
		Map<String, Object> info = getCIMIResourceInfo();
        info.put("address", this.address);
        info.put("validated", this.validated);
		return info;
	}
	
}
