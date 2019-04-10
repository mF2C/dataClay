package CIMI;

import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings({"serial" })
public class Service extends CIMIResource {

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
	String exec;
	String exec_type; //docker, compss, ... (enum checked by CIMI)
	ArrayList<Integer> exec_ports;
	String agent_type; //cloud, normal or micro (enum checked by CIMI)
	Integer num_agents;
	String cpu_arch; //arm or x86-64 (enum checked by CIMI)
	String os; //linux, mac, windows, ios, android (enum checked by CIMI)
	Integer memory_min;
	Integer storage_min;
	Integer disk;
	ArrayList<String> req_resource; 
	ArrayList<String> opt_resource; 
	Integer category;
	
	// Constructor
	public Service(final Map<String, Object> objectData) {
		super(objectData);
	}

}
