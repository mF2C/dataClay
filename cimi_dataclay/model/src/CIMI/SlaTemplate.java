package CIMI;

import java.util.Map;

import dataclay.util.replication.Replication;

@SuppressWarnings({ "serial" })
public class SlaTemplate extends CIMIResource {

	/** 
	 * ==== READ THIS! ====
	 * An attribute for each field in the CIMI resource spec, with the same name and type.
	 * 
	 * 1) If it contains nested info or its a href, it is implemented as a Map<String, Object>
	 *  where String is the field name, and Object is the value
	 *  
	 * 2) if you want to synchronize your field with agent leaders: add following annotations to your field: 
	 *		@Replication.InMaster and 
	 * 		@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	 * 
	 * 3) WARNING: All fields must be public or no modifier defined.
	 * 
	 * 4) All types defined must be Object types (Integer, Float, List...) and not primitive types (int, float,...) 
	 * 
	 * 5) Once finished, please do a Pull request and after testing and verifications dataClay will publish 
	 * a new docker image with your changes/new resource model. 
	 **/
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	String state; // "started", "stopped" or "terminated"
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	Map<String, Object> details;
	// details is a nested field
	// id: String
	// type: String - Agreement or Template
	// name: String
	// provider: Map<String, String> - another nested field: id, name
	// client: Map<String, String> - another nested field: id, name
	// creation: String
	// expiration: String
	// guarantees: List<Map<String, String>> - another nested field: name,
	// constraint

	public SlaTemplate(final Map<String, Object> objectData) {
		super(objectData);
	}

	// Setters (a setter for each property called "set_propertyname")
	public void set_state(final String state) {
		this.state = state;
	}

	public void set_details(final Map<String, Object> details) {
		this.details = details;
	}

}
