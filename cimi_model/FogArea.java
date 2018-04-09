package CIMI;

import java.util.Map;

public class FogArea extends CIMIResource {

	//An attribute for each field in the CIMI resource spec, with the same name and type.
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	private Device leaderDevice;
	private int numDevices;
	private String ramUnits;
	private int ramTotal;
	private int ramMax;
	private int ramMin;
	private String storageUnits;
	private int storageTotal;
	private int storageMax;
	private int storageMin;
	private float avgProcessingCapacityPercent;
	private float cpuMaxPercent;
	private float cpuMinPercent;
	private int avgPhysicalCores;
	private int physicalCoresMax;
	private int physicalCoresMin;
	private int avgLogicalCores;
	private int logicalCoresMax;
	private int logicalCoresMin;
	private String powerRemainingMax;
	private String powerRemainingMin;
	
	//Constructor, with a parameter for each attribute in this class and in CIMIResource
	public FogArea(Device leaderDevice, int numDevices, String ramUnits, int ramTotal, int ramMax, int ramMin,
			String storageUnits, int storageTotal, int storageMax, int storageMin, float avgProcessingCapacityPercent,
			float cpuMaxPercent, float cpuMinPercent, int avgPhysicalCores, int physicalCoresMax, int physicalCoresMin,
			int avgLogicalCores, int logicalCoresMax, int logicalCoresMin, String powerRemainingMax, String powerRemainingMin,
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
		
		super(resourceID, resourceName, resourceDescription, resourceURI);
		this.leaderDevice = leaderDevice;
		this.numDevices = numDevices;
		this.ramUnits = ramUnits;
		this.ramTotal = ramTotal;
		this.ramMax = ramMax;
		this.ramMin = ramMin;
		this.storageUnits = storageUnits;
		this.storageTotal = storageTotal;
		this.storageMax = storageMax;
		this.storageMin = storageMin;
		this.avgProcessingCapacityPercent = avgProcessingCapacityPercent;
		this.cpuMaxPercent = cpuMaxPercent;
		this.cpuMinPercent = cpuMinPercent;
		this.avgPhysicalCores = avgPhysicalCores;
		this.physicalCoresMax = physicalCoresMax;
		this.physicalCoresMin = physicalCoresMin;
		this.avgLogicalCores = avgLogicalCores;
		this.logicalCoresMax = logicalCoresMax;
		this.logicalCoresMin = logicalCoresMin;
		this.powerRemainingMax = powerRemainingMax;
		this.powerRemainingMin = powerRemainingMin;
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_leaderDevice(Device leaderDevice) {
		this.leaderDevice = leaderDevice;
	}
	
	public void set_numDevices(int numDevices) {
		this.numDevices = numDevices;
	}
	
	public void set_ramUnits(String ramUnits) {
		this.ramUnits = ramUnits;
	}
	
	public void set_ramTotal(int ramTotal) {
		this.ramTotal = ramTotal;
	}
	
	public void set_ramMax(int ramMax) {
		this.ramMax = ramMax;
	}
	
	public void set_ramMin(int ramMin) {
		this.ramMin = ramMin;
	}
	
	public void set_storageUnits(String storageUnits) {
		this.storageUnits = storageUnits;
	}
	
	public void set_storageTotal(int storageTotal) {
		this.storageTotal = storageTotal;
	}
	
	public void set_storageMax(int storageMax) {
		this.storageMax = storageMax;
	}
	
	public void set_storageMin(int storageMin) {
		this.storageMin = storageMin;
	}
	
	public void set_avgProcessingCapacityPercent(float avgProcessingCapacityPercent) {
		this.avgProcessingCapacityPercent = avgProcessingCapacityPercent;
	}
	
	public void set_cpuMaxPercent(float cpuMaxPercent) {
		this.cpuMaxPercent = cpuMaxPercent;
	}
	
	public void set_cpuMinPercent(float cpuMinPercent) {
		this.cpuMinPercent = cpuMinPercent;
	}
	
	public void set_avgPhysicalCores(int avgPhysicalCores) {
		this.avgPhysicalCores = avgPhysicalCores;
	}
	
	public void set_physicalCoresMax(int physicalCoresMax) {
		this.physicalCoresMax = physicalCoresMax;
	}
	
	public void set_physicalCoresMin(int physicalCoresMin) {
		this.physicalCoresMin = physicalCoresMin;
	}
	
	public void set_avgLogicalCores(int avgLogicalCores) {
		this.avgLogicalCores = avgLogicalCores;
	}
	
	public void set_logicalCoresMax(int logicalCoresMax) {
		this.logicalCoresMax = logicalCoresMax;
	}
	
	public void set_logicalCoresMin(int logicalCoresMin) {
		this.logicalCoresMin = logicalCoresMin;
	}
	
	public void set_powerRemainingMax(String powerRemainingMax) {
		this.powerRemainingMax = powerRemainingMax;
	}
	
	public void set_powerRemainingMin(String powerRemainingMin) {
		this.powerRemainingMin = powerRemainingMin;
	}
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info" 
	public Map<String, Object> get_FogArea_info() {
		Map<String, Object> info = getCIMIResourceInfo();
		info.put("leaderDevice", leaderDevice);
		info.put("numDevices", numDevices);
		info.put("ramUnits", ramUnits);
		info.put("ramTotal", ramTotal);
		info.put("ramMax", ramMax);
		info.put("ramMin", ramMin);
		info.put("storageUnits", storageUnits);
		info.put("storageTotal", storageTotal);
		info.put("storageMax", storageMax);
		info.put("storageMin", storageMin);
		info.put("avgProcessingCapacityPercent", avgProcessingCapacityPercent);
		info.put("cpuMaxPercent", cpuMaxPercent);
		info.put("cpuMinPercent", cpuMinPercent);
		info.put("avgPhysicalCores", avgPhysicalCores);
		info.put("physicalCoresMax", physicalCoresMax);
		info.put("physicalCoresMin", physicalCoresMin);
		info.put("avgLogicalCores", avgLogicalCores);
		info.put("logicalCoresMax", logicalCoresMax);
		info.put("logicalCoresMin", logicalCoresMin);
		info.put("powerRemainingMax", powerRemainingMax);
		info.put("powerRemainingMin", powerRemainingMin);
		return info;
	}

}
