package CIMI;

import java.util.Map;

public class Device extends CIMIResource {

	//An attribute for each field in the CIMI resource spec, with the same name and type.
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	private String deviceID;
	private Boolean isLeader;
	private String os;
	private String arch;
	private String cpuManufacturer;
	private Integer physicalCores;
	private Integer logicalCores;
	private Integer cpuClockSpeed;
	private Integer memory;
	private Integer  storage;
	private Boolean powerPlugged;
	private String networkingStandards;
	private String ethernetAddress;
	private String wifiAddress;
	 	
	public Device(Map<String, Object> objectData) {
		super(objectData);
		this.deviceID = (String) objectData.get("deviceID");
		this.isLeader = (boolean) objectData.get("isLeader");
		this.os = (String) objectData.get("os");
		this.arch = (String) objectData.get("arch");
		this.cpuManufacturer = (String) objectData.get("cpuManufacturer");
		this.physicalCores = (int) objectData.get("physicalCores");
		this.logicalCores = (int) objectData.get("logicalCores");
		this.cpuClockSpeed = (int) objectData.get("cpuClockSpeed");
		this.memory = (int) objectData.get("memory"); 
		this.storage = (int) objectData.get("storage");
		this.powerPlugged = (boolean) objectData.get("powerPlugged");
		this.networkingStandards = (String) objectData.get("networkingStandards");
		this.ethernetAddress = (String) objectData.get("ethernetAddress");
		this.wifiAddress = (String) objectData.get("wifiAddress");
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_deviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	
	public void set_isLeader(boolean isLeader) {
		this.isLeader = isLeader;
	}
	
	public void set_os(String os) {
		this.os = os;
	}
	
	public void set_arch(String arch) {
		this.arch = arch;
	}
	
	public void set_cpuManufacturer(String cpuManufacturer) {
		this.cpuManufacturer = cpuManufacturer;
	}
	
	public void set_physicalCores(int physicalCores) {
		this.physicalCores = physicalCores;
	}
	
	public void set_logicalCores(int logicalCores) {
		this.logicalCores = logicalCores;
	}
	
	public void set_cpuClockSpeed(int cpuClockSpeed) {
		this.cpuClockSpeed = cpuClockSpeed;
	}
	
	public void set_memory(int memory) {
		this.memory = memory; 
	}
	
	public void set_storage(int storage) {
		this.storage = storage;
	}
	
	public void set_powerPlugged(boolean powerPlugged) {
		this.powerPlugged = powerPlugged;
	}
	
	public void set_networkingStandards(String networkingStandards) {
		this.networkingStandards = networkingStandards;
	}
	
	public void set_ethernetAddress(String ethernetAddress) {
		this.ethernetAddress = ethernetAddress;
	}
	
	public void set_wifiAddress(String wifiAddress) {
		this.wifiAddress = wifiAddress;		
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "getCIMIResourceData"
	public Map<String, Object> getCIMIResourceData() {
		Map<String, Object> info = super.getCIMIResourceData();
		if (this.deviceID != null) info.put("deviceID", this.deviceID);
		if (this.isLeader != null) info.put("isLeader", this.isLeader);
		if (this.os != null) info.put("os", this.os);
		if (this.arch != null) info.put("arch", this.arch);
		if (this.cpuManufacturer != null) info.put("cpuManufacturer", this.cpuManufacturer);
		if (this.physicalCores != null) info.put("physicalCores", this.physicalCores);
		if (this.logicalCores != null) info.put("logicalCores", this.logicalCores);
		if (this.cpuClockSpeed != null) info.put("cpuClockSpeed", this.cpuClockSpeed);
		if (this.memory != null) info.put("memory", this.memory);
		if (this.storage != null) info.put("storage", this.storage);
		if (this.powerPlugged != null) info.put("powerPlugged", this.powerPlugged);
		if (this.networkingStandards != null) info.put("networkingStandards", this.networkingStandards);
		if (this.ethernetAddress != null) info.put("ethernetAddress", this.ethernetAddress);
		if (this.wifiAddress != null) info.put("wifiAddress", this.wifiAddress);
		return info;
	}
	
	public void updateAllData(Map<String, Object> data) {
		super.setCIMIResourceData(data);
		if (data.get("deviceID") != null) set_deviceID((String) data.get("deviceID"));
		if (data.get("isLeader") != null) set_isLeader((boolean) data.get("isLeader"));
		if (data.get("os") != null) set_os((String) data.get("os"));
		if (data.get("arch") != null) set_arch((String) data.get("arch"));
		if (data.get("cpuManufacturer") != null) set_cpuManufacturer((String) data.get("cpuManufacturer"));
		if (data.get("physicalCores") != null) set_physicalCores((int) data.get("physicalCores"));
		if (data.get("logicalCores") != null) set_logicalCores((int) data.get("logicalCores"));
		if (data.get("cpuClockSpeed") != null) set_cpuClockSpeed((int) data.get("cpuClockSpeed"));
		if (data.get("memory") != null) set_memory((int) data.get("memory"));
		if (data.get("storage") != null) set_storage((int) data.get("storage"));
		if (data.get("powerPlugged") != null) set_powerPlugged((boolean) data.get("powerPlugged"));
		if (data.get("networkingStandards") != null) set_networkingStandards((String) data.get("networkingStandards"));
		if (data.get("ethernetAddress") != null) set_ethernetAddress((String) data.get("ethernetAddress"));
		if (data.get("wifiAddress") != null) set_wifiAddress((String) data.get("wifiAddress"));

	}

}
