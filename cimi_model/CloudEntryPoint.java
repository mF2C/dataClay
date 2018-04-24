package CIMI;

import java.util.Map;

public class CloudEntryPoint extends CIMIResource {

	//An attribute for each field in the CIMI resource spec, with the same name and type.
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
    private String baseURI; 
    // every nested field below corresponds to an existing resource. 
	private Map<String, Object> credentials;
    private Map<String, Object> userTemplates; 
    private Map<String, Object> configurations;
    private Map<String, Object> configurationTemplates;
    private Map<String, Object> sessionTemplates;
    private Map<String, Object> callbacks;
    private Map<String, Object> userParam;
    private Map<String, Object> slaViolations;
    private Map<String, Object> agreements;
    private Map<String, Object> userParamTemplates;
    private Map<String, Object> devices;
    private Map<String, Object> credentialTemplates;
    private Map<String, Object> deviceDynamics;
    private Map<String, Object> sessions;
    private Map<String, Object> serviceInstances;
    private Map<String, Object> exampleResources;
    private Map<String, Object> emails;
    private Map<String, Object> fogAreas;
    private Map<String, Object> sharingModels;
    private Map<String, Object> users;
    private Map<String, Object> services;
	private Map<String, Object> userProfiles;
	
	//Constructor, with a parameter for each attribute in this class and in CIMIResource
    public CloudEntryPoint(String baseURI, Map<String, Object> credentials, Map<String, Object> userTemplates,
            Map<String, Object> configurations, Map<String, Object> configurationTemplates, Map<String, Object> sessionTemplates, 
            Map<String, Object> callbacks, Map<String, Object> userParam, Map<String, Object> slaViolations, 
            Map<String, Object> agreements, Map<String, Object> userParamTemplates, Map<String, Object> devices, 
            Map<String, Object> credentialTemplates, Map<String, Object> deviceDynamics, Map<String, Object> sessions, 
            Map<String, Object> serviceInstances, Map<String, Object> exampleResources, Map<String, Object> emails, 
            Map<String, Object> fogAreas, Map<String, Object> sharingModels, Map<String, Object> users, 
            Map<String, Object> services, Map<String, Object> userProfiles,
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
        super(resourceID, resourceName, resourceDescription, resourceURI);
        this.baseURI = baseURI;
		this.credentials = credentials;
        this.userTemplates = userTemplates;
        this.configurations = configurations;
        this.configurationTemplates = configurationTemplates;
        this.sessionTemplates = sessionTemplates;
        this.callbacks = callbacks;
        this.userParam = userParam;
        this.slaViolations = slaViolations;
        this.agreements = agreements;
        this.userParamTemplates = userParamTemplates;
        this.devices = devices;
        this.credentialTemplates = credentialTemplates;
        this.deviceDynamics = deviceDynamics;
        this.sessions = sessions;
        this.serviceInstances = serviceInstances;
        this.exampleResources = exampleResources;
        this.emails = emails;
        this.fogAreas = fogAreas;
        this.sharingModels = sharingModels;
        this.users = users;
        this.services = services;
        this.userProfiles = userProfiles;
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_baseURI(String baseURI) {
		this.baseURI = baseURI;
    }
    
    public void set_credentials(Map<String, Object> credentials) {
		this.credentials = credentials;
    }
    
    public void set_configurations(Map<String, Object> configurations) {
		this.configurations = configurations;
    }

    public void set_userTemplates(Map<String, Object> userTemplates) {
		this.userTemplates = userTemplates;
    }
    
    public void set_configurationTemplates(Map<String, Object> configurationTemplates) {
		this.configurationTemplates = configurationTemplates;
    }

    public void set_sessionTemplates(Map<String, Object> sessionTemplates) {
		this.sessionTemplates = sessionTemplates;
    }
    
    public void set_callbacks(Map<String, Object> callbacks) {
		this.callbacks = callbacks;
    }

    public void set_userParam(Map<String, Object> userParam) {
		this.userParam = userParam;
    }
    
    public void set_slaViolations(Map<String, Object> slaViolations) {
		this.slaViolations = slaViolations;
    }

    public void set_agreements(Map<String, Object> agreements) {
		this.agreements = agreements;
    }
    
    public void set_userParamTemplates(Map<String, Object> userParamTemplates) {
		this.userParamTemplates = userParamTemplates;
    }

    public void set_devices(Map<String, Object> devices) {
		this.devices = devices;
    }

    public void set_credentialTemplates(Map<String, Object> credentialTemplates) {
		this.credentialTemplates = credentialTemplates;
    }

    public void set_deviceDynamics(Map<String, Object> deviceDynamics) {
		this.deviceDynamics = deviceDynamics;
    }

    public void set_sessions(Map<String, Object> sessions) {
		this.sessions = sessions;
    }

    public void set_serviceInstances(Map<String, Object> serviceInstances) {
		this.serviceInstances = serviceInstances;
    }

    public void set_exampleResources(Map<String, Object> exampleResources) {
		this.exampleResources = exampleResources;
    }

    public void set_emails(Map<String, Object> emails) {
		this.emails = emails;
    }

    public void set_fogAreas(Map<String, Object> fogAreas) {
		this.fogAreas = fogAreas;
    }

    public void set_sharingModels(Map<String, Object> sharingModels) {
		this.sharingModels = sharingModels;
    }

    public void set_services(Map<String, Object> services) {
		this.services = services;
    }

    public void set_users(Map<String, Object> users) {
		this.users = users;
    }

    public void set_userProfiles(Map<String, Object> userProfiles) {
		this.userProfiles = userProfiles;
    }
    
   
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> get_CloudEntryPoint_info() {
		Map<String, Object> info = getCIMIResourceInfo();
		info.put("baseURI", this.baseURI);
		info.put("credentials", this.credentials);
        info.put("userTemplates", this.userTemplates);
        info.put("configurations", this.configurations);
		info.put("configurationTemplates", this.configurationTemplates);
		info.put("sessionTemplates", this.sessionTemplates);
		info.put("callbacks", this.callbacks);
		info.put("userParam", this.userParam);
		info.put("slaViolations", this.slaViolations);
		info.put("agreements", this.agreements);
		info.put("userParamTemplates", this.userParamTemplates);
		info.put("devices", this.devices);
		info.put("credentialTemplates", this.credentialTemplates);
		info.put("deviceDynamics", this.deviceDynamics);
		info.put("sessions", this.sessions);
		info.put("serviceInstances", this.serviceInstances);
		info.put("exampleResources", this.exampleResources);
		info.put("emails", this.emails);
		info.put("fogAreas", this.fogAreas);
		info.put("sharingModels", this.sharingModels);
		info.put("users", this.users);
		info.put("services", this.services);
		info.put("userProfiles", this.userProfiles);        
		return info;
	}

}
