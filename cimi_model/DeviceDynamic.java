package CIMI;

import java.util.List;
import java.util.Map;

public class DeviceDynamic extends CIMIResource {
	
	//An attribute for each field in the CIMI resource spec, with the same name and type.
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	private Device device;
	//private boolean isLeader;
	//private String ramUnits;
	private float ramFree;
	private float ramFreePercent;
	//private String storageUnits;
	private int storageFree;
	private float storageFreePercent;
	private float cpuFreePercent;
	private String powerRemainingStatus;
	private String powerRemainingStatusSeconds; //TODO: It is of type String in resource spec, I think it is a mistake => There is a reason to put it as a string in the fog area side when we retrieve this info then we can able to compare the battery enabled devices with the permanent electric source devices  
	private String ethernetAddress; //TODO: This is here and in Device. I guess it should only be in one place
	private String wifiAddress; //TODO: This is here and in Device. I guess it should only be in one place
	private List<Object> ethernetThroughputInfo; //TODO: The type of this field is not defined in the resource spec, so I don't know how to implement it
	private List<Object> wifiThroughputInfo; //TODO: The type of this field is not defined in the resource spec, so I don't know how to implement it
	private
	
	//Constructor, with a parameter for each attribute in this class and in CIMIResource
	public DeviceDynamic(Device device, String ramUnits, float ramFree, float ramFreePercent, 
			String storageUnits, int storageFree, float storageFreePercent, float cpuFreePercent, 
			String powerRemainingStatus, int powerRemainingStatusSeconds, String ethernetAddress, 
			String wifiAddress, List<Object> ethernetThroughputInfo, List<Object> wifiThroughputInfo, Device myleaderID,  
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
		super(resourceID, resourceName, resourceDescription, resourceURI);
		this.device = device;
		//this.isLeader = isLeader;
		this.ramUnits = ramUnits;
		this.ramFree = ramFree;
		this.ramFreePercent = ramFreePercent;
		this.storageUnits = storageUnits;
		this.storageFree = storageFree;
		this.storageFreePercent = storageFreePercent;
		this.cpuFreePercent = cpuFreePercent;
		this.powerRemainingStatus = powerRemainingStatus;
		this.powerRemainingStatusSeconds = powerRemainingStatusSeconds;
		this.ethernetAddress = ethernetAddress;
		this.wifiAddress = wifiAddress;
		this.ethernetThroughputInfo = ethernetThroughputInfo;
		this.wifiThroughputInfo = wifiThroughputInfo;
		this.myleaderID = myleaderID;
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_device(Device device) {
		this.device = device;
	}
	
	public void set_isLeader(boolean isLeader) {
		this.isLeader = isLeader;
	}
	
	public void set_ramUnits(String ramUnits) {
		this.ramUnits = ramUnits;
	}
	
	public void set_ramFree(float ramFree) {
		this.ramFree = ramFree;
	}
	
	public void set_ramFreePercent(float ramFreePercent) {
		this.ramFreePercent = ramFreePercent;
	}
	
	public void set_storageUnits(String storageUnits) {
		this.storageUnits = storageUnits;
	}
	
	public void set_storageFree(int storageFree) {
		this.storageFree = storageFree;
	}
	
	public void set_storageFreePercent(float storageFreePercent) {
		this.storageFreePercent = storageFreePercent;
	}
	
	public void set_cpuFreePercent(float cpuFreePercent) {
		this.cpuFreePercent = cpuFreePercent;
	}
	
	public void set_powerRemainingStatus(String powerRemainingStatus) {
		this.powerRemainingStatus = powerRemainingStatus;
	}
	
	public void set_powerRemainingStatusSeconds(int powerRemainingStatusSeconds) {
		this.powerRemainingStatusSeconds = powerRemainingStatusSeconds;
	}
	
	public void set_ethernetAddress(String ethernetAddress) {
		this.ethernetAddress = ethernetAddress;
	}
	
	public void set_wifiAddress(String wifiAddress) {
		this.wifiAddress = wifiAddress;
	}
	
	public void set_ethernetThroughputInfo(List<Object> ethernetThroughputInfo) {
		this.ethernetThroughputInfo = ethernetThroughputInfo;
	}
	
	public void set_wifiThroughputInfo(List<Object> wifiThroughputInfo) {
		this.wifiThroughputInfo = wifiThroughputInfo;
	}
	public void set_myleaderID(Device myleaderID) {
		this.myleaderID = myleaderID;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
	public Map<String, Object> get_DeviceDynamic_info() {
		Map<String, Object> info = getCIMIResourceInfo();
		info.put("device", this.device);
		//info.put("isLeader", this.isLeader);
		info.put("ramUnits", this.ramUnits);
		info.put("ramFree", this.ramFree);
		info.put("ramFreePercent", this.ramFreePercent);
		info.put("storageUnits", this.storageUnits);
		info.put("storageFree", this.storageFree);
		info.put("storageFreePercent", this.storageFreePercent);
		info.put("cpuFreePercent", this.cpuFreePercent);
		info.put("powerRemainingStatus", this.powerRemainingStatus);
		info.put("powerRemainingStatusSeconds", this.powerRemainingStatusSeconds);
		info.put("ethernetAddress", this.ethernetAddress);
		info.put("wifiAddress", this.wifiAddress);
		info.put("ethernetThroughputInfo", this.ethernetThroughputInfo);
		info.put("wifiThroughputInfo", this.wifiThroughputInfo);
		info.put("myleaderID", this.myleaderID);
		return info;
	}

}
