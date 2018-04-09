package CIMI;

import java.util.Map;

public class Device extends CIMIResource {

	//An attribute for each field in the CIMI resource spec, with the same name and type.
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	private String deviceID;
	private boolean isLeader;
	private String os;
	private String arch;
	private String cpuManufacturer;
	private int physicalCores;
	private int logicalCores;
	private int cpuClockSpeed;
	private int memory;
	private int storage;
	private boolean powerPlugged;
	private String networkingStandards;
	private String ethernetAddress;
	private String wifiAddress;
	
	//Constructor, with a parameter for each attribute in this class and in CIMIResource
	public Device(String deviceID, boolean isLeader, String os, String arch, String cpuManufacturer,
			int physicalCores, int logicalCores, int cpuClockSpeed, int memory, int storage, 
			boolean powerPlugged, String networkingStandards, String ethernetAddress, String wifiAddress,
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
		super(resourceID, resourceName, resourceDescription, resourceURI);
		this.deviceID = deviceID;
		this.isLeader = isLeader;
		this.os = os;
		this.arch = arch;
		this.cpuManufacturer = cpuManufacturer;
		this.physicalCores = physicalCores;
		this.logicalCores = logicalCores;
		this.cpuClockSpeed = cpuClockSpeed;
		this.memory = memory; 
		this.storage = storage;
		this.powerPlugged = powerPlugged;
		this.networkingStandards = networkingStandards;
		this.ethernetAddress = ethernetAddress;
		this.wifiAddress = wifiAddress;
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
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> get_Device_info() {
		Map<String, Object> info = getCIMIResourceInfo();
		info.put("deviceID", this.deviceID);
		info.put("isLeader", this.isLeader);
		info.put("os", this.os);
		info.put("arch", this.arch);
		info.put("cpuManufacturer", this.cpuManufacturer);
		info.put("physicalCores", this.physicalCores);
		info.put("logicalCores", this.logicalCores);
		info.put("cpuClockSpeed", this.cpuClockSpeed);
		info.put("memory", this.memory);
		info.put("storage", this.storage);
		info.put("powerPlugged", this.powerPlugged);
		info.put("networkingStandards", this.networkingStandards);
		info.put("ethernetAddress", this.ethernetAddress);
		info.put("wifiAddress", this.wifiAddress);
		return info;
	}

}
