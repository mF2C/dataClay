package CIMI;

import java.util.Map;

@SuppressWarnings("serial")
public class SharingModel extends CIMIResource {
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
	String user_id;
    String device_id;
	Boolean gps_allowed;
	Integer max_cpu_usage;
	Integer max_memory_usage;
	Integer max_storage_usage;
	Integer max_bandwidth_usage;
	Integer battery_limit;

	// Constructor, with a parameter for each attribute in this class and in
	// CIMIResource
	public SharingModel(final Map<String, Object> objectData) {
		super(objectData);
	}
}
