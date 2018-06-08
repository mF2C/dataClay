package CIMI;

import java.util.Map;

public class SessionTemplateInternal extends CIMIResource {
	
	//An attribute for each field in the CIMI resource spec, with the same name 
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	
	private String instance;
	private String group;
	private String method;
	private String username;
	private String password;

	
	//Constructor
	public SessionTemplateInternal(Map<String, Object> objectData) {
		super(objectData);
		this.method = (String) objectData.get("method");
		this.instance = (String) objectData.get("instance");
		this.group = (String) objectData.get("group");
		this.password = (String) objectData.get("password");
		// this.redirectURI = (String) objectData.get("redirectURI");
		this.username = (String) objectData.get("username");
	}
	
	//Setters (a setter for each property called "set_propertyname")

	public void set_method(String method) {
		this.method = method;
	}

	public void set_instance(String instance) {
		this.instance = instance;
	}

	public void set_group(String group) {
		this.group = group;
	}

	public void set_password(String password) {
		this.password = password;
	}

	// public void set_redirectURI(String redirectURI) {
	// 	this.redirectURI = redirectURI;
	// }

	public void set_username(String username) {
		this.username = username;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> getCIMIResourceData() {
		Map<String, Object> info = super.getCIMIResourceData();
		// if (this.redirectURI != null) info.put("redirectURI", this.redirectURI);
		if (this.group != null) info.put("group", this.group);
		if (this.instance != null) info.put("instance", this.instance);
		if (this.password != null) info.put("password", this.password);
		if (this.method != null) info.put("method", this.method);
		if (this.username != null) info.put("username", this.username);				
		return info;
	}
	
	public void updateAllData(Map<String, Object> data) {
		setCIMIResourceData(data);
		// if (data.get("redirectURI") != null) set_redirectURI((String) data.get("redirectURI"));
		if (data.get("group") != null) set_group((String) data.get("group"));
		if (data.get("instance") != null) set_instance((String) data.get("instance"));
		if (data.get("password") != null) set_password((String) data.get("password"));
		if (data.get("method") != null) set_method((String) data.get("method"));
		if (data.get("username") != null) set_username((String) data.get("username"));		
	}

	
}
