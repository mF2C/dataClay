package CIMI;

import java.util.Map;

import dataclay.util.replication.Replication;

@SuppressWarnings("serial")
public class Device extends CIMIResource {

	// An attribute for each field in the CIMI resource spec, with the same name and
	// type.
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	private String deviceID;
	private Boolean isLeader;
	private String os;
	private String arch;
	private String cpuManufacturer;
	private Integer physicalCores;
	private Integer logicalCores;
	private String cpuClockSpeed;
	private Float memory;
	private Float storage;
	private Boolean powerPlugged;
	private String agentType;
	private String actuatorInfo;
	private String networkingStandards;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToSlaves", clazz = "dataclay.util.replication.SequentialConsistency")
	private String ethernetAddress;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToSlaves", clazz = "dataclay.util.replication.SequentialConsistency")
	private String wifiAddress;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToSlaves", clazz = "dataclay.util.replication.SequentialConsistency")
	private String hwloc;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToSlaves", clazz = "dataclay.util.replication.SequentialConsistency")
	private String cpuinfo;

	public Device(final Map<String, Object> objectData) {
		super(objectData);
		this.deviceID = (String) objectData.get("deviceID");
		this.isLeader = (Boolean) objectData.get("isLeader");
		this.os = (String) objectData.get("os");
		this.arch = (String) objectData.get("arch");
		this.cpuManufacturer = (String) objectData.get("cpuManufacturer");
		this.physicalCores = (Integer) objectData.get("physicalCores");
		this.logicalCores = (Integer) objectData.get("logicalCores");
		this.cpuClockSpeed = (String) objectData.get("cpuClockSpeed");
		this.memory = (Float) objectData.get("memory");
		this.storage = (Float) objectData.get("storage");
		this.powerPlugged = (Boolean) objectData.get("powerPlugged");
		this.agentType = (String) objectData.get("agentType");
		this.actuatorInfo = (String) objectData.get("actuatorInfo");
		this.networkingStandards = (String) objectData.get("networkingStandards");
		this.ethernetAddress = (String) objectData.get("ethernetAddress");
		this.wifiAddress = (String) objectData.get("wifiAddress");
		this.hwloc = (String) objectData.get("hwloc");
	}

	// Setters (a setter for each property called "set_propertyname")
	public void set_deviceID(final String deviceID) {
		this.deviceID = deviceID;
	}

	public void set_isLeader(final Boolean isLeader) {
		this.isLeader = isLeader;
	}

	public void set_os(final String os) {
		this.os = os;
	}

	public void set_arch(final String arch) {
		this.arch = arch;
	}

	public void set_cpuManufacturer(final String cpuManufacturer) {
		this.cpuManufacturer = cpuManufacturer;
	}

	public void set_physicalCores(final Integer physicalCores) {
		this.physicalCores = physicalCores;
	}

	public void set_logicalCores(final Integer logicalCores) {
		this.logicalCores = logicalCores;
	}

	public void set_cpuClockSpeed(final String cpuClockSpeed) {
		this.cpuClockSpeed = cpuClockSpeed;
	}

	public void set_memory(final Float memory) {
		this.memory = memory;
	}

	public void set_storage(final Float storage) {
		this.storage = storage;
	}

	public void set_powerPlugged(final Boolean powerPlugged) {
		this.powerPlugged = powerPlugged;
	}

	public void set_agentType(final String agentType) {
		this.agentType = agentType;
	}

	public void set_actuatorInfo(final String actuatorInfo) {
		this.actuatorInfo = actuatorInfo;
	}

	public void set_networkingStandards(final String networkingStandards) {
		this.networkingStandards = networkingStandards;
	}

	public void set_ethernetAddress(final String ethernetAddress) {
		this.ethernetAddress = ethernetAddress;
	}

	public void set_wifiAddress(final String wifiAddress) {
		this.wifiAddress = wifiAddress;
	}

	public void set_hwloc(final String hwloc) {
		this.hwloc = hwloc;
	}

	public void set_cpuinfo(final String cpuinfo) {
		this.cpuinfo = cpuinfo;
	}



	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "getCIMIResourceData"
	@Override
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = super.getCIMIResourceData();
		if (this.deviceID != null)
			info.put("deviceID", this.deviceID);
		if (this.isLeader != null)
			info.put("isLeader", this.isLeader);
		if (this.os != null)
			info.put("os", this.os);
		if (this.arch != null)
			info.put("arch", this.arch);
		if (this.cpuManufacturer != null)
			info.put("cpuManufacturer", this.cpuManufacturer);
		if (this.physicalCores != null)
			info.put("physicalCores", this.physicalCores);
		if (this.logicalCores != null)
			info.put("logicalCores", this.logicalCores);
		if (this.cpuClockSpeed != null)
			info.put("cpuClockSpeed", this.cpuClockSpeed);
		if (this.memory != null)
			info.put("memory", this.memory);
		if (this.storage != null)
			info.put("storage", this.storage);
		if (this.powerPlugged != null)
			info.put("powerPlugged", this.powerPlugged);
		if (this.agentType != null)
			info.put("agentType", this.agentType);
		if (this.actuatorInfo != null)
			info.put("actuatorInfo", this.actuatorInfo);
		if (this.networkingStandards != null)
			info.put("networkingStandards", this.networkingStandards);
		if (this.ethernetAddress != null)
			info.put("ethernetAddress", this.ethernetAddress);
		if (this.wifiAddress != null)
			info.put("wifiAddress", this.wifiAddress);
		if (this.hwloc != null)
			info.put("hwloc", this.hwloc);
		if (this.cpuinfo != null)
			info.put("cpuinfo", this.cpuinfo);
		return info;
	}

	public void updateAllData(final Map<String, Object> data) {
		super.setCIMIResourceData(data);
		if (data.get("deviceID") != null)
			set_deviceID((String) data.get("deviceID"));
		if (data.get("isLeader") != null)
			set_isLeader((Boolean) data.get("isLeader"));
		if (data.get("os") != null)
			set_os((String) data.get("os"));
		if (data.get("arch") != null)
			set_arch((String) data.get("arch"));
		if (data.get("cpuManufacturer") != null)
			set_cpuManufacturer((String) data.get("cpuManufacturer"));
		if (data.get("physicalCores") != null)
			set_physicalCores((Integer) data.get("physicalCores"));
		if (data.get("logicalCores") != null)
			set_logicalCores((Integer) data.get("logicalCores"));
		if (data.get("cpuClockSpeed") != null)
			set_cpuClockSpeed((String) data.get("cpuClockSpeed"));
		if (data.get("memory") != null)
			set_memory((float) data.get("memory"));
		if (data.get("storage") != null)
			set_storage((float) data.get("storage"));
		if (data.get("powerPlugged") != null)
			set_powerPlugged((Boolean) data.get("powerPlugged"));
		if (data.get("agentType") != null)
			set_agentType((String) data.get("agentType"));
		if (data.get("actuatorInfo") != null)
			set_actuatorInfo((String) data.get("actuatorInfo"));
		if (data.get("networkingStandards") != null)
			set_networkingStandards((String) data.get("networkingStandards"));
		if (data.get("ethernetAddress") != null)
			set_ethernetAddress((String) data.get("ethernetAddress"));
		if (data.get("wifiAddress") != null)
			set_wifiAddress((String) data.get("wifiAddress"));
		if (data.get("hwloc") != null)
			set_hwloc((String) data.get("hwloc"));
		if (data.get("cpuinfo") != null)
			set_cpuinfo((String) data.get("cpuinfo"));

	}

}
