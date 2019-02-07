package CIMI;

import java.util.HashMap;
import java.util.Map;

public class CloudEntryPoint extends CIMIResource {

	// An attribute for each field in the CIMI resource spec, with the same name and
	// type.
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	private String baseURI;
	// We will have an entry for each resource collection 
	private Map<String, Object> resourceCollections;

	// Constructor
	public CloudEntryPoint(final Map<String, Object> objectData) {
		super(objectData);
		this.baseURI = (String) objectData.get("baseURI");
		this.resourceCollections = new HashMap<>();
		// mF2C resources
		Map<String, String> link = new HashMap<>();
		link.put("href", "agreement");
		resourceCollections.put("agreements", link);
		link = new HashMap<>();
		link.put("href", "device");
		resourceCollections.put("devices", link);
		link = new HashMap<>();
		link.put("href", "device-dynamic");
		resourceCollections.put("device-dynamics", link);
		link = new HashMap<>();
		link.put("href", "fog-area");
		resourceCollections.put("fog-areas", link);
		link = new HashMap<>();
		link.put("href", "service");
		resourceCollections.put("services", link);
		link = new HashMap<>();
		link.put("href", "service-instance");
		resourceCollections.put("service-instances", link);
		link = new HashMap<>();
		link.put("href", "sharing-model");
		resourceCollections.put("sharing-models", link);
		link = new HashMap<>();
		link.put("href", "sla-violation");
		resourceCollections.put("sla-violations", link);
		link = new HashMap<>();
		link.put("href", "user-profile");
		resourceCollections.put("user-profiles", link);
		link = new HashMap<>();
		link.put("href", "service-operation-report");
		resourceCollections.put("service-operation-report", link);
		//CIMI server resources
		link = new HashMap<>();
		link.put("href", "email");
		resourceCollections.put("emails", link);
		link = new HashMap<>();
		link.put("href", "user");
		resourceCollections.put("users", link);
		link = new HashMap<>();
		link.put("href", "user-template");
		resourceCollections.put("user-templates", link);
		link = new HashMap<>();
		link.put("href", "credential");
		resourceCollections.put("credentials", link);
		link = new HashMap<>();
		link.put("href", "credential-template");
		resourceCollections.put("credential-templates", link);
		link = new HashMap<>();
		link.put("href", "configuration");
		resourceCollections.put("configurations", link);
		link = new HashMap<>();
		link.put("href", "configuration-template");
		resourceCollections.put("configuration-templates", link);
		link = new HashMap<>();
		link.put("href", "session");
		resourceCollections.put("sessions", link);
		link = new HashMap<>();
		link.put("href", "session-template");
		resourceCollections.put("session-templates", link);
		link = new HashMap<>();
		link.put("href", "user-param");
		resourceCollections.put("user-params", link);
		link = new HashMap<>();
		link.put("href", "user-param-template");
		resourceCollections.put("user-param-templates", link);
		link = new HashMap<>();
		link.put("href", "callback");
		resourceCollections.put("callbacks", link);
		link = new HashMap<>();
		link.put("href", "example-resource");
		resourceCollections.put("example-resources", link);
	}

	/*
	 * private ResourceCollection getResourceCollection(String name) {
	 * ResourceCollection result = new ResourceCollection(); try { result =
	 * (ResourceCollection) ResourceCollection.getByAlias(name); } catch (Exception
	 * e) { // ignore } return result; }
	 */

	// Setters (a setter for each property called "set_propertyname")
	public void set_baseURI(final String baseURI) {
		this.baseURI = baseURI;
	}

	public void set_resourceCollections(final Map<String, Object> resourceCollections) {
		this.resourceCollections = resourceCollections;
	}


	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "get_classname_info"
	@Override
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = super.getCIMIResourceData();
		if (this.baseURI != null)
			info.put("baseURI", this.baseURI);
		if (this.resourceCollections != null) {
			for (final Map.Entry<String, Object> item : this.resourceCollections.entrySet()) {
				info.put(item.getKey(), item.getValue());
			}
		}
		return info;
	}
	
	public void updateAllData(Map<String, Object> data) {
		setCIMIResourceData(data);
		if (data.get("baseURI") != null) set_baseURI((String) data.get("baseURI"));
		if (data.get("resourceCollections") != null) 
			set_resourceCollections((Map<String, Object>) data.get("resourceCollections"));
	}


}
