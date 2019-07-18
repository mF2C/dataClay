package CIMI;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class CloudEntryPoint extends CIMIResource {
	/** 
	 * ==== READ THIS! ====
	 * An attribute for each field in the CIMI resource spec, with the same name and type.
	 * 
	 * 1) If it contains nested info or its a href, it is implemented as a Map<String, Object>
	 *  where String is the field name, and Object is the value
	 *  
	 * 2) If you want your class to be shared/visible in the Leader, please annotate the class 
	 * 		with @ReplicateInLeader 
	 *  
	 * 3) if you want to synchronize your field with agent leaders: add following annotations to your field: 
	 *		@Replication.InMaster and 
	 * 		@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	 * 
	 * 4) WARNING: All fields must be public or no modifier defined.
	 * 
	 * 5) All types defined must be Object types (Integer,, List...) and not primitive types (int,...) 
	 *    WARNING!!: Use Double for floating point numbers due to JSON restrictions
	 * 
	 * 6) Once finished, please do a Pull request and after testing and verifications dataClay will publish 
	 * a new docker image with your changes/new resource model. 
	 **/
	String baseURI;
	// We will have an entry for each resource collection 
	Map<String, Object> resourceCollections;

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

}
