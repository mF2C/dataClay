package CIMI;

import java.util.Map;

public class Session extends CIMIResource {
	
	//An attribute for each field in the CIMI resource spec, with the same name 
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	
	private String method;
	private Map<String, Object> sessionTemplate;
	private String expiry;
	private String username;
	private String roles;
	private String server;
	private String clientIP;
	private String redirectURI;
	
	//Constructor, with a parameter for each attribute in this class and in CIMIResource
	public Session(Map<String, Object> sessionTemplate, String method, String expiry, String username,
			String roles, String server, String clientIP, String redirectURI,  
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
			
		super(resourceID, resourceName, resourceDescription, resourceURI);
		this.method = method;
		this.sessionTemplate = sessionTemplate;
		this.expiry = expiry;
		this.roles = roles;
		this.server = server;
		this.clientIP = clientIP;
		this.redirectURI = redirectURI;
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_sessionTemplate(Map<String, Object> sessionTemplate) {
		this.sessionTemplate = sessionTemplate;
	}

	public void set_method(String method) {
		this.method = method;
	}

	public void set_expiry(String expiry) {
		this.expiry = expiry;
	}

	public void set_roles(String roles) {
		this.roles = roles;
	}

	public void set_server(String server) {
		this.server = server;
	}

	public void set_clientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public void set_redirectURI(String redirectURI) {
		this.redirectURI = redirectURI;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> get_Session_info() {
		Map<String, Object> info = getCIMIResourceInfo();
		info.put("redirectURI", this.redirectURI);
		info.put("clientIP", this.clientIP);
		info.put("server", this.server);
		info.put("roles", this.roles);
		info.put("expiry", this.expiry);
		info.put("method", this.method);
		info.put("sessionTemplate", this.sessionTemplate);		
		return info;
	}
	
}
