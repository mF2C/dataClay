package CIMI;

import java.util.Map;

public class Callback extends CIMIResource {
	
	//An attribute for each field in the CIMI resource spec, with the same name 
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	private String action; 
    private String state;
    private Map<String, Object> targetResource;
    private Map<String, Object> data;
    private String expires;
    
	//Constructor, with a parameter for each attribute in this class and in CIMIResource
	public Callback(String action, String state, Map<String, Object> targetResource, Map<String, Object> data, String expires,
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
			
		super(resourceID, resourceName, resourceDescription, resourceURI);
		this.action = action;
        this.state = state;
        this.targetResource = targetResource;
        this.data = data;
        this.expires = expires;
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_action(String action) {
		this.action = action;
	}

	public void set_state(String state) {
		this.state = state;
    }
    
    public void set_targetResource(Map<String, Object> targetResource) {
		this.targetResource = targetResource;
    }
    
    public void set_data(Map<String, Object> data) {
		this.data = data;
    }
    
    public void set_expires(String expires) {
		this.expires = expires;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> get_Callback_info() {
		Map<String, Object> info = getCIMIResourceInfo();
		info.put("action", this.action);
        info.put("state", this.state);
        info.put("targetResource", this.targetResource);
        info.put("data", this.data);
        info.put("expires", this.expires);
		return info;
	}
	
}
