package CIMI;

import java.util.Map;

public class FogArea extends CIMIResource {

	//An attribute for each field in the CIMI resource spec, with the same name and type.
	//If it contains nested info, it is implemented as a Map<String, Object>
	//where String is the field name, and Object is the value
	private Device leaderDevice;
	private Integer numDevices;
	//private String ramUnits;
	private Integer ramTotal;
	private Integer ramMax;
	private Integer ramMin;
	//private String storageUnits;
	private Integer storageTotal;
	private Integer storageMax;
	private Integer storageMin;
	private Float avgProcessingCapacityPercent;
	private Float cpuMaxPercent;
	private Float cpuMinPercent;
	private Integer avgPhysicalCores;
	private Integer physicalCoresMax;
	private Integer physicalCoresMin;
	private Integer avgLogicalCores;
	private Integer logicalCoresMax;
	private Integer logicalCoresMin;
	private String powerRemainingMax;
	private String powerRemainingMin;
	
	//Constructor
	public FogArea(Map<String, Object> objectData) {
		super(objectData);
		this.leaderDevice = (Device) objectData.get("leaderDevice");
		this.numDevices = (int) objectData.get("numDevices");
		//this.ramUnits = (String) objectData.get("ramUnits");
		this.ramTotal = (int) objectData.get("ramTotal");
		this.ramMax = (int) objectData.get("ramMax");
		this.ramMin = (int) objectData.get("ramMin");
		//this.storageUnits = (String) objectData.get("storageUnits");
		this.storageTotal = (int) objectData.get("storageTotal");
		this.storageMax = (int) objectData.get("storageMax");
		this.storageMin = (int) objectData.get("storageMin");
		this.avgProcessingCapacityPercent = (float) objectData.get("avgProcessingCapacityPercent");
		this.cpuMaxPercent = (float) objectData.get("cpuMaxPercent");
		this.cpuMinPercent = (float) objectData.get("cpuMinPercent");
		this.avgPhysicalCores = (int) objectData.get("avgPhysicalCores");
		this.physicalCoresMax = (int) objectData.get("physicalCoresMax");
		this.physicalCoresMin = (int) objectData.get("physicalCoresMin");
		this.avgLogicalCores = (int) objectData.get("avgLogicalCores");
		this.logicalCoresMax = (int) objectData.get("logicalCoresMax");
		this.logicalCoresMin = (int) objectData.get("logicalCoresMin");
		this.powerRemainingMax = (String) objectData.get("powerRemainingMax");
		this.powerRemainingMin = (String) objectData.get("powerRemainingMin");
	}
	
	//Setters (a setter for each property called "set_propertyname")
	public void set_leaderDevice(Device leaderDevice) {
		this.leaderDevice = leaderDevice;
	}
	
	public void set_numDevices(int numDevices) {
		this.numDevices = numDevices;
	}
	
//	public void set_ramUnits(String ramUnits) {
//		this.ramUnits = ramUnits;
//	}
	
	public void set_ramTotal(int ramTotal) {
		this.ramTotal = ramTotal;
	}
	
	public void set_ramMax(int ramMax) {
		this.ramMax = ramMax;
	}
	
	public void set_ramMin(int ramMin) {
		this.ramMin = ramMin;
	}
	
//	public void set_storageUnits(String storageUnits) {
//		this.storageUnits = storageUnits;
//	}
	
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
	
	//A single getter that returns a Map with all the info in this class and in CIMIResource, called "getCIMIResourceData" 
	public Map<String, Object> getCIMIResourceData() {
		Map<String, Object> info = getCIMIResourceData();
		if (this.leaderDevice != null) info.put("leaderDevice", leaderDevice);
		if (this.numDevices != null) info.put("numDevices", numDevices);
//		info.put("ramUnits", ramUnits);
		if (this.ramTotal != null) info.put("ramTotal", ramTotal);
		if (this.ramMax != null) info.put("ramMax", ramMax);
		if (this.ramMin != null) info.put("ramMin", ramMin);
//		info.put("storageUnits", storageUnits);
		if (this.storageTotal != null) info.put("storageTotal", storageTotal);
		if (this.storageMax != null) info.put("storageMax", storageMax);
		if (this.storageMin != null) info.put("storageMin", storageMin);
		if (this.avgProcessingCapacityPercent != null) info.put("avgProcessingCapacityPercent", avgProcessingCapacityPercent);
		if (this.cpuMaxPercent != null) info.put("cpuMaxPercent", cpuMaxPercent);
		if (this.cpuMinPercent != null) info.put("cpuMinPercent", cpuMinPercent);
		if (this.avgPhysicalCores != null) info.put("avgPhysicalCores", avgPhysicalCores);
		if (this.physicalCoresMax != null) info.put("physicalCoresMax", physicalCoresMax);
		if (this.physicalCoresMin != null) info.put("physicalCoresMin", physicalCoresMin);
		if (this.avgLogicalCores != null) info.put("avgLogicalCores", avgLogicalCores);
		if (this.logicalCoresMax != null) info.put("logicalCoresMax", logicalCoresMax);
		if (this.logicalCoresMin != null) info.put("logicalCoresMin", logicalCoresMin);
		if (this.powerRemainingMax != null) info.put("powerRemainingMax", powerRemainingMax);
		if (this.powerRemainingMin != null) info.put("powerRemainingMin", powerRemainingMin);
		return info;
	}
	
	public void updateAllData(Map<String, Object> data) {
		super.setCIMIResourceData(data);
		if (data.get("leaderDevice") != null) set_leaderDevice((Device) data.get("leaderDevice"));
		if (data.get("numDevices") != null) set_numDevices((int) data.get("numDevices"));
		if (data.get("ramTotal") != null) set_ramTotal((int) data.get("ramTotal"));
		if (data.get("ramMax") != null) set_ramMax((int) data.get("ramMax"));
		if (data.get("ramMin") != null) set_ramMin((int) data.get("ramMin"));
		if (data.get("storageTotal") != null) set_storageTotal((int) data.get("storageTotal"));
		if (data.get("storageMax") != null) set_storageMax((int) data.get("storageMax"));
		if (data.get("storageMin") != null) set_storageMin((int) data.get("storageMin"));
		if (data.get("avgProcessingCapacityPercent") != null) 
			set_avgProcessingCapacityPercent((float) data.get("avgProcessingCapacityPercent"));
		if (data.get("cpuMaxPercentcpuMaxPercent") != null) set_cpuMaxPercent((float) data.get("cpuMaxPercent"));
		if (data.get("cpuMinPercent") != null) set_cpuMinPercent((float) data.get("cpuMinPercent"));
		if (data.get("avgPhysicalCores") != null) set_avgPhysicalCores((int) data.get("avgPhysicalCores"));
		if (data.get("physicalCoresMax") != null) set_physicalCoresMax((int) data.get("physicalCoresMax"));
		if (data.get("physicalCoresMin") != null) set_physicalCoresMin((int) data.get("physicalCoresMin"));
		if (data.get("avgLogicalCores") != null) set_avgLogicalCores((int) data.get("avgLogicalCores"));
		if (data.get("logicalCoresMax") != null) set_logicalCoresMax((int) data.get("logicalCoresMax"));
		if (data.get("logicalCoresMin") != null) set_logicalCoresMin((int) data.get("logicalCoresMin"));
		if (data.get("powerRemainingMax") != null) set_powerRemainingMax((String) data.get("powerRemainingMax"));
		if (data.get("powerRemainingMin") != null) set_powerRemainingMin((String) data.get("powerRemainingMin"));
	}

}
