package CIMI;

import java.util.Map;

import com.sun.java_cup.internal.runtime.virtual_parse_stack;

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
	
	//Constructor, with a parameter for each attribute in this class and in CIMIResource
    public User(String username, String emailAddress, String firstName, String lastName, String organization,
            String method, String href, String password, String roles, String state,
            String githublogin, String creation, String lastOnline, String lastExecute, String activeSince,
            Boolean deleted, Boolean isSuperUser,
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
			
		super(resourceID, resourceName, resourceDescription, resourceURI);
        this.username = username;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organization = organization;
        this.method = method;
        this.href = href;
        this.password = password;
        this.roles = roles;
        this.state = state;
        this.githublogin = githublogin;
        this.creation = creation;
        this.lastOnline = lastOnline;
        this.lastExecute = lastExecute;
        this.activeSince = activeSince;
        this.deleted = deleted;
        this.isSuperUser = isSuperUser;
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
	public Map<String, Object> get_User_info() {
		Map<String, Object> info = getCIMIResourceInfo();
        info.put("isSuperUser", this.isSuperUser);
        info.put("deleted", this.deleted);
        info.put("activeSince", this.activeSince);
        info.put("lastExecute", this.lastExecute);
        info.put("lastOnline", this.lastOnline);
        info.put("creation", this.creation);
        info.put("githublogin", this.githublogin);
        info.put("state", this.state);
        info.put("roles", this.roles);
        info.put("password", this.password);
        info.put("href", this.href);
        info.put("method", this.method);
        info.put("lastName", this.lastName);
        info.put("firstName", this.firstName);
        info.put("organization", this.organization);
        info.put("emailAddress", this.emailAddress);
        info.put("username", this.username);
		return info;
	}
	
}
