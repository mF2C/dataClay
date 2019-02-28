package CIMI;

import java.util.List;
import java.util.Map;

import dataclay.util.replication.Replication;

@SuppressWarnings({ "unchecked", "serial" })
public class DeviceDynamic extends CIMIResource {

	// An attribute for each field in the CIMI resource spec, with the same name and
	// type.
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	private Map<String, Object> device;
	// private boolean isLeader;
	// private String ramUnits;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private Float ramFree;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private Float ramFreePercent;
	// private String storageUnits;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private Float storageFree;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private Float storageFreePercent;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private Float cpuFreePercent;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private String powerRemainingStatus;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private String powerRemainingStatusSeconds;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private String ethernetAddress; // TODO: This is here and in Device. I guess it should only be in one place
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private String wifiAddress; // TODO: This is here and in Device. I guess it should only be in one place
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private List<String> ethernetThroughputInfo;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private List<String> wifiThroughputInfo;
	private Map<String, Object> myLeaderID;

	public DeviceDynamic(final Map<String, Object> objectData) {
		super(objectData);

		this.device = (Map<String, Object>) objectData.get("device");
		// this.isLeader = (boolean) objectData.get("isLeader");
		// this.ramUnits = (String) objectData.get("ramUnits");
		this.ramFree = getFloat(objectData.get("ramFree"));
		this.ramFreePercent = getFloat(objectData.get("ramFreePercent"));
		this.storageFree = getFloat(objectData.get("storageFree"));
		this.storageFreePercent = getFloat(objectData.get("storageFreePercent"));
		this.cpuFreePercent = getFloat(objectData.get("cpuFreePercent"));
		// this.storageUnits = (String) objectData.get("storageUnits");
		this.powerRemainingStatus = (String) objectData.get("powerRemainingStatus");
		this.powerRemainingStatusSeconds = (String) objectData.get("powerRemainingStatusSeconds");
		this.ethernetAddress = (String) objectData.get("ethernetAddress");
		this.wifiAddress = (String) objectData.get("wifiAddress");
		this.ethernetThroughputInfo = (List<String>) objectData.get("ethernetThroughputInfo");
		this.wifiThroughputInfo = (List<String>) objectData.get("wifiThroughputInfo");
		this.myLeaderID = (Map<String, Object>) objectData.get("myLeaderID");
	}

	// Setters (a setter for each property called "set_propertyname")
	public void set_device(final Map<String, Object> device) {
		this.device = device;
	}

	// public void set_isLeader(boolean isLeader) {
	// this.isLeader = isLeader;
	// }

	// public void set_ramUnits(String ramUnits) {
	// this.ramUnits = ramUnits;
	// }

	public void set_ramFree(final float ramFree) {
		this.ramFree = ramFree;
	}

	public void set_ramFreePercent(final float ramFreePercent) {
		this.ramFreePercent = ramFreePercent;
	}

	// public void set_storageUnits(String storageUnits) {
	// this.storageUnits = storageUnits;
	// }

	public void set_storageFree(final float storageFree) {
		this.storageFree = storageFree;
	}

	public void set_storageFreePercent(final float storageFreePercent) {
		this.storageFreePercent = storageFreePercent;
	}

	public void set_cpuFreePercent(final float cpuFreePercent) {
		this.cpuFreePercent = cpuFreePercent;
	}

	public void set_powerRemainingStatus(final String powerRemainingStatus) {
		this.powerRemainingStatus = powerRemainingStatus;
	}

	public void set_powerRemainingStatusSeconds(final String powerRemainingStatusSeconds) {
		this.powerRemainingStatusSeconds = powerRemainingStatusSeconds;
	}

	public void set_ethernetAddress(final String ethernetAddress) {
		this.ethernetAddress = ethernetAddress;
	}

	public void set_wifiAddress(final String wifiAddress) {
		this.wifiAddress = wifiAddress;
	}

	public void set_ethernetThroughputInfo(final List<String> ethernetThroughputInfo) {
		this.ethernetThroughputInfo = ethernetThroughputInfo;
	}

	public void set_wifiThroughputInfo(final List<String> wifiThroughputInfo) {
		this.wifiThroughputInfo = wifiThroughputInfo;
	}

	public void set_myLeaderID(final Map<String, Object> myLeaderID) {
		this.myLeaderID = myLeaderID;
	}

	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "getCIMIResourceData"
	@Override
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = super.getCIMIResourceData();
		if (this.device != null)
			info.put("device", this.device);
		// info.put("isLeader", this.isLeader);
		// info.put("ramUnits", this.ramUnits);
		if (this.ramFree != null)
			info.put("ramFree", this.ramFree);
		if (this.ramFreePercent != null)
			info.put("ramFreePercent", this.ramFreePercent);
		// info.put("storageUnits", this.storageUnits);
		if (this.storageFree != null)
			info.put("storageFree", this.storageFree);
		if (this.storageFreePercent != null)
			info.put("storageFreePercent", this.storageFreePercent);
		if (this.cpuFreePercent != null)
			info.put("cpuFreePercent", this.cpuFreePercent);
		if (this.powerRemainingStatus != null)
			info.put("powerRemainingStatus", this.powerRemainingStatus);
		if (this.powerRemainingStatusSeconds != null)
			info.put("powerRemainingStatusSeconds", this.powerRemainingStatusSeconds);
		if (this.ethernetAddress != null)
			info.put("ethernetAddress", this.ethernetAddress);
		if (this.wifiAddress != null)
			info.put("wifiAddress", this.wifiAddress);
		if (this.ethernetThroughputInfo != null)
			info.put("ethernetThroughputInfo", this.ethernetThroughputInfo);
		if (this.wifiThroughputInfo != null)
			info.put("wifiThroughputInfo", this.wifiThroughputInfo);
		if (this.myLeaderID != null)
			info.put("myLeaderID", this.myLeaderID);
		return info;
	}

	public void updateAllData(final Map<String, Object> data) {
		super.setCIMIResourceData(data);
		if (data.get("device") != null)
			set_device((Map<String, Object>) data.get("device"));
		// if (data.get("isLeader") != null) set_isLeader((boolean)
		// data.get("isLeader"));
		// if (data.get("ramUnits") != null) set_ramUnits((String)
		// data.get("ramUnits"));
		if (data.get("ramFree") != null)
			set_ramFree(getFloat(data.get("ramFree")));
		if (data.get("ramFreePercent") != null)
			set_ramFreePercent(getFloat(data.get("ramFreePercent")));
		// if (data.get("storageUnits") != null) set_storageUnits((String)
		// data.get("storageUnits"));
		if (data.get("storageFree") != null)
			set_storageFree(getFloat(data.get("storageFree")));
		if (data.get("storageFreePercent") != null)
			set_storageFreePercent(getFloat(data.get("storageFreePercent")));
		if (data.get("powerRemainingStatus") != null)
			set_powerRemainingStatus((String) data.get("powerRemainingStatus"));
		if (data.get("powerRemainingStatusSeconds") != null)
			set_powerRemainingStatusSeconds((String) data.get("powerRemainingStatusSeconds"));
		if (data.get("ethernetAddress") != null)
			set_ethernetAddress((String) data.get("ethernetAddress"));
		if (data.get("wifiAddress") != null)
			set_wifiAddress((String) data.get("wifiAddress"));
		if (data.get("ethernetThroughputInfo") != null)
			set_ethernetThroughputInfo((List<String>) data.get("ethernetThroughputInfo"));
		if (data.get("wifiThroughputInfo") != null)
			set_wifiThroughputInfo((List<String>) data.get("wifiThroughputInfo"));
		if (data.get("myLeaderID") != null)
			set_myLeaderID((Map<String, Object>) data.get("myLeaderID"));
	}

}
