package CIMI;

import java.util.Map;


public class User extends CIMIResource {
	
	//An attribute for each field in the CIMI resource spec, with the same name 
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
    private String username;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String organization;
    private String method;
    private String href;
    private String password;
    private String roles;
    private Boolean isSuperUser;
    private String state;
    private Boolean deleted;
    private String githublogin;
    private String creation;
    private String lastOnline;
    private String lastExecute;
    private String activeSince;
	
	//Constructor
    public User(Map<String, Object> objectData) {
		super(objectData);
        this.username = (String) objectData.get("username");
        this.emailAddress = (String) objectData.get("emailAddress");
        this.firstName = (String) objectData.get("firstName");
        this.lastName = (String) objectData.get("lastName");
        this.organization = (String) objectData.get("organization");
        this.method = (String) objectData.get("method");
        this.href = (String) objectData.get("href");
        this.password = (String) objectData.get("password");
        this.roles = (String) objectData.get("roles");
        this.state = (String) objectData.get("state");
        this.githublogin = (String) objectData.get("githublogin");
        this.creation = (String) objectData.get("creation");
        this.lastOnline = (String) objectData.get("lastOnline");
        this.lastExecute = (String) objectData.get("lastExecute");
        this.activeSince = (String) objectData.get("activeSince");
        this.deleted = (Boolean) objectData.get("deleted");
        this.isSuperUser = (Boolean) objectData.get("isSuperUser");
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_username(String username) {
		this.username = username;
    }
    
    public void set_emailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
    }
    
    public void set_firstName(String firstName) {
		this.firstName = firstName;
    }
    
    public void set_lastName(String lastName) {
		this.lastName = lastName;
    }
    
    public void set_organization(String organization) {
		this.organization = organization;
    }
    
    public void set_method(String method) {
		this.method = method;
    }
    
    public void set_href(String href) {
		this.href = href;
    }
    
    public void set_password(String password) {
		this.password = password;
    }
    
    public void set_roles(String roles) {
		this.roles = roles;
    }
    
    public void set_state(String state) {
		this.state = state;
    }
    
    public void set_githublogin(String githublogin) {
		this.githublogin = githublogin;
    }
    
    public void set_creation(String creation) {
		this.creation = creation;
    }
    
    public void set_lastOnline(String lastOnline) {
		this.lastOnline = lastOnline;
    }
    
    public void set_lastExecute(String lastExecute) {
		this.lastExecute = lastExecute;
    }
    
    public void set_activeSince(String activeSince) {
		this.activeSince = activeSince;
    }
    
    public void set_deleted(Boolean deleted) {
		this.deleted = deleted;
    }
    
    public void set_isSuperUser(Boolean isSuperUser) {
		this.isSuperUser = isSuperUser;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> getCIMIResourceData() {
		Map<String, Object> info = super.getCIMIResourceData();
		if (this.isSuperUser != null) info.put("isSuperUser", this.isSuperUser);
		if (this.deleted != null) info.put("deleted", this.deleted);
		if (this.activeSince != null) info.put("activeSince", this.activeSince);
		if (this.lastExecute != null) info.put("lastExecute", this.lastExecute);
		if (this.lastOnline != null) info.put("lastOnline", this.lastOnline);
		if (this.creation != null) info.put("creation", this.creation);
		if (this.githublogin != null) info.put("githublogin", this.githublogin);
		if (this.state != null) info.put("state", this.state);
		if (this.roles != null) info.put("roles", this.roles);
		if (this.password != null) info.put("password", this.password);
		if (this.href != null) info.put("href", this.href);
		if (this.method != null) info.put("method", this.method);
		if (this.lastName != null) info.put("lastName", this.lastName);
		if (this.firstName != null) info.put("firstName", this.firstName);
		if (this.organization != null) info.put("organization", this.organization);
		if (this.emailAddress != null) info.put("emailAddress", this.emailAddress);
		if (this.username != null) info.put("username", this.username);
		return info;
	}
	
	public void updateAllData(Map<String, Object> data) {
		setCIMIResourceData(data);
		if (data.get("isSuperUser") != null) set_isSuperUser((boolean) data.get("isSuperUser"));
		if (data.get("deleted") != null) set_deleted((boolean) data.get("deleted"));
		if (data.get("activeSince") != null) set_activeSince((String) data.get("activeSince"));
		if (data.get("lastExecute") != null) set_lastExecute((String) data.get("lastExecute"));
		if (data.get("lastOnline") != null) set_lastOnline((String) data.get("lastOnline"));
		if (data.get("creation") != null) set_creation((String) data.get("creation"));
		if (data.get("githublogin") != null) set_githublogin((String) data.get("githublogin"));
		if (data.get("state") != null) set_state((String) data.get("state"));
		if (data.get("roles") != null) set_roles((String) data.get("roles"));
		if (data.get("password") != null) set_password((String) data.get("password"));
		if (data.get("href") != null) set_href((String) data.get("href"));
		if (data.get("method") != null) set_method((String) data.get("method"));
		if (data.get("lastName") != null) set_lastName((String) data.get("lastName"));
		if (data.get("firstName") != null) set_firstName((String) data.get("firstName"));
		if (data.get("organization") != null) set_organization((String) data.get("organization"));
		if (data.get("emailAddress") != null) set_emailAddress((String) data.get("emailAddress"));
		if (data.get("username") != null) set_username((String) data.get("username"));


	}
}