package CIMI;

import java.util.List;
import java.util.Map;

import dataclay.util.replication.Replication;

@SuppressWarnings({"serial" })
public class ServiceInstance extends CIMIResource {
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
	String user;
	String device_id; 
	String device_ip; 
	String parent_device_id;
	String parent_device_ip;
	String service;
	String service_type;
	String agreement;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	String status;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	List<Map<String, Object>> agents;
	// agents is a collection of "nested" info:
	// agent: String (resource link)
	// app_type: string
	// url: String
	// ports: List<Integer>
	// agent_param: String
	// container_id: String
	// status: String
	// num_cpus: int
	// allow: bool
	// master_compss: bool

	// Constructor
	public ServiceInstance(final Map<String, Object> objectData) {
		super(objectData);
	}

}
