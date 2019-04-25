package CIMI;

import java.util.List;
import java.util.Map;

import dataclay.util.replication.Replication;

@SuppressWarnings({"serial" })
@ReplicateInLeader
public class DeviceDynamic extends CIMIResource {
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
	Map<String, Object> device;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	Double ramFree;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	Double ramFreePercent;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	Double storageFree;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	Double storageFreePercent;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	Double cpuFreePercent;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	String powerRemainingStatus;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	String powerRemainingStatusSeconds;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	String ethernetAddress; // TODO: This is here and in Device. I guess it should only be in one place
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	String wifiAddress; // TODO: This is here and in Device. I guess it should only be in one place
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	List<String> ethernetThroughputInfo;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	List<String> wifiThroughputInfo;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	String actuatorInfo;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	Boolean powerPlugged;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	List<Map<String, Object>> sensors; 
	
	public DeviceDynamic(final Map<String, Object> objectData) {
		super(objectData);
	}
}
