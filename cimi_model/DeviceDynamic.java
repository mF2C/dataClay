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
	private Float ramFree;
	private Float ramFreePercent;
	//private String storageUnits;
	private Integer storageFree;
	private Float storageFreePercent;
	private Float cpuFreePercent;
	private String powerRemainingStatus;
	private Integer powerRemainingStatusSeconds; //TODO: It is of type String in resource spec, I think it is a mistake
	private String ethernetAddress; //TODO: This is here and in Device. I guess it should only be in one place
	private String wifiAddress; //TODO: This is here and in Device. I guess it should only be in one place
	private List<?> ethernetThroughputInfo; //TODO: The type of this field is not defined in the resource spec, so I don't know how to implement it
	private List<?> wifiThroughputInfo; //TODO: The type of this field is not defined in the resource spec, so I don't know how to implement it
	
	public DeviceDynamic(Map<String, Object> objectData) {
		super(objectData);
		
		this.device = (Device) objectData.get("device");
		//this.isLeader = (boolean) objectData.get("isLeader");
		//this.ramUnits = (String) objectData.get("ramUnits");
		this.ramFree = (float) objectData.get("ramFree");
		this.ramFreePercent = (float) objectData.get("ramFreePercent");
		//this.storageUnits = (String) objectData.get("storageUnits");
		this.storageFree = (int) objectData.get("storageFree");
		this.storageFreePercent = (float) objectData.get("storageFreePercent");
		this.cpuFreePercent = (float) objectData.get("cpuFreePercent");
		this.powerRemainingStatus = (String) objectData.get("powerRemainingStatus");
		this.powerRemainingStatusSeconds = (int) objectData.get("powerRemainingStatusSeconds");
		this.ethernetAddress = (String) objectData.get("ethernetAddress");
		this.wifiAddress = (String) objectData.get("wifiAddress");
		this.ethernetThroughputInfo = (List<Object>) objectData.get("ethernetThroughputInfo");
		this.wifiThroughputInfo = (List<Object>) objectData.get("wifiThroughputInfo");
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_device(Device device) {
		this.device = device;
	}
	
//	public void set_isLeader(boolean isLeader) {
//		this.isLeader = isLeader;
//	}
	
//	public void set_ramUnits(String ramUnits) {
//		this.ramUnits = ramUnits;
//	}
	
	public void set_ramFree(float ramFree) {
		this.ramFree = ramFree;
	}
	
	public void set_ramFreePercent(float ramFreePercent) {
		this.ramFreePercent = ramFreePercent;
	}
	
//	public void set_storageUnits(String storageUnits) {
//		this.storageUnits = storageUnits;
//	}
	
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
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "getCIMIResourceData"
	public Map<String, Object> getCIMIResourceData() {
		Map<String, Object> info = getCIMIResourceData();
		if (this.device != null) info.put("device", this.device);
//		info.put("isLeader", this.isLeader);
//		info.put("ramUnits", this.ramUnits);
		if (this.ramFree != null) info.put("ramFree", this.ramFree);
		if (this.ramFreePercent != null) info.put("ramFreePercent", this.ramFreePercent);
//		info.put("storageUnits", this.storageUnits);
		if (this.storageFree != null) info.put("storageFree", this.storageFree);
		if (this.storageFreePercent != null) info.put("storageFreePercent", this.storageFreePercent);
		if (this.cpuFreePercent != null) info.put("cpuFreePercent", this.cpuFreePercent);
		if (this.powerRemainingStatus != null) info.put("powerRemainingStatus", this.powerRemainingStatus);
		if (this.powerRemainingStatusSeconds != null) info.put("powerRemainingStatusSeconds", this.powerRemainingStatusSeconds);
		if (this.ethernetAddress != null) info.put("ethernetAddress", this.ethernetAddress);
		if (this.wifiAddress != null) info.put("wifiAddress", this.wifiAddress);
		if (this.ethernetThroughputInfo != null) info.put("ethernetThroughputInfo", this.ethernetThroughputInfo);
		if (this.wifiThroughputInfo != null) info.put("wifiThroughputInfo", this.wifiThroughputInfo);
		return info;
	}
	
	public void updateAllData(Map<String, Object> data) {
		super.setCIMIResourceData(data);
		if (data.get("device") != null) set_device((Device) data.get("device"));
//		if (data.get("isLeader") != null) set_isLeader((boolean) data.get("isLeader"));
//		if (data.get("ramUnits") != null) set_ramUnits((String) data.get("ramUnits"));
		if (data.get("ramFree") != null) set_ramFree((float) data.get("ramFree"));
		if (data.get("ramFreePercent") != null) set_ramFreePercent((float) data.get("ramFreePercent"));
//		if (data.get("storageUnits") != null) set_storageUnits((String) data.get("storageUnits"));
		if (data.get("storageFreePercent") != null) set_storageFreePercent((float) data.get("storageFreePercent"));
		if (data.get("powerRemainingStatus") != null) set_powerRemainingStatus((String) data.get("powerRemainingStatus"));
		if (data.get("ethernetAddress") != null) set_ethernetAddress((String) data.get("ethernetAddress"));
		if (data.get("wifiAddress") != null) set_wifiAddress((String) data.get("wifiAddress"));
		if (data.get("ethernetThroughputInfo") != null) set_ethernetThroughputInfo((List<Object>) data.get("ethernetThroughputInfo"));
		if (data.get("wifiThroughputInfo") != null) set_wifiThroughputInfo((List<Object>) data.get("wifiThroughputInfo"));


	}

}
