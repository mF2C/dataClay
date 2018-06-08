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
	
	//Constructor
	public Session(Map<String, Object> objectData) {
		super(objectData);
		this.method = (String) objectData.get("method");
		this.sessionTemplate = (Map<String, Object>) objectData.get("sessionTemplate");
		this.expiry = (String) objectData.get("expiry");
		this.roles = (String) objectData.get("roles");
		this.server = (String) objectData.get("server");
		this.clientIP = (String) objectData.get("clientIP");
		this.redirectURI = (String) objectData.get("redirectURI");
		this.username = (String) objectData.get("username");
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

	public void set_username(String username) {
		this.username = username;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> getCIMIResourceData() {
		Map<String, Object> info = super.getCIMIResourceData();
		if (this.redirectURI != null) info.put("redirectURI", this.redirectURI);
		if (this.clientIP != null) info.put("clientIP", this.clientIP);
		if (this.server != null) info.put("server", this.server);
		if (this.roles != null) info.put("roles", this.roles);
		if (this.expiry != null) info.put("expiry", this.expiry);
		if (this.method != null) info.put("method", this.method);
		if (this.sessionTemplate != null) info.put("sessionTemplate", this.sessionTemplate);		
		if (this.username != null) info.put("username", this.username);		
		return info;
	}
	
	public void updateAllData(Map<String, Object> data) {
		setCIMIResourceData(data);
		if (data.get("redirectURI") != null) set_redirectURI((String) data.get("redirectURI"));
		if (data.get("clientIP") != null) set_clientIP((String) data.get("clientIP"));
		if (data.get("server") != null) set_server((String) data.get("server"));
		if (data.get("roles") != null) set_roles((String) data.get("roles"));
		if (data.get("expiry") != null) set_expiry((String) data.get("expiry"));
		if (data.get("method") != null) set_method((String) data.get("method"));
		if (data.get("username") != null) set_username((String) data.get("username"));
		if (data.get("sessionTemplate") != null) set_sessionTemplate((Map<String, Object>) data.get("sessionTemplate"));
	}

	
}
