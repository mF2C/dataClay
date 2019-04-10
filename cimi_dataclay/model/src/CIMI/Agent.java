package CIMI;

import java.util.ArrayList;
import java.util.Map;

/**
 * Agent resource type *
 */
@SuppressWarnings({ "serial" })
public class Agent extends CIMIResource {
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
	String device_id;
	String device_ip;
	String leader_id;
	String leader_ip;
	String backup_ip;
	Boolean authenticated;
	Boolean connected;
	Boolean isLeader;
	ArrayList<String> childrenIPs;
	public Agent(final Map<String, Object> objectData) {
		super(objectData);
	}	
	
}
