package model;

@SuppressWarnings("unused")
public class Device extends CIMIResource {
    // private User owner;
    private String id; // mF2C id of the device
    // StaticInfo
    private String operatingSystem;
    private String systemArchitecture;
    private String processorMaker;
    private String processorArchitecture;
    private int numCPUs;
    private String CPUClockSpeed;
    private int numCores;
    private float RAMSize;
    private float storageSize;
    private String agentType;
    private String actuatorInfo;
    private boolean limitedPower;
    private String graphicsCardInfo;
    private String CPU_info; // For the Landscaper collector
    private String hwloc; // For the Landscaper collector
    // private List<Component> attachedComponents;
    // private SharingModel sharingModel;
    // DynamicInfo
    // ... TBD
    // NetworkInfo
    // ... TBD
    // BehaviourInfo
    // ... TBD
    // SecurityInfo
    // ...TBD

    public Device(final String deviceId, final String deviceOS, final String deviceSysArch,
	    final String deviceProcMaker, final String deviceProcArch, final int deviceNumCPUs,
	    final String deviceClockSpeed, final int deviceNumCores, final float deviceRAMSize, final float deviceStorageSize, final String deviceAgentType, final String deviceActuatorInfo,
	    final boolean deviceLimitedPower, final String deviceGraphicsCard, final String resourceID,
	    final String resourceName, final String resourceDescription, String resourceURI) {

	// CIMI default fields
	super(resourceID, resourceName, resourceDescription, resourceURI);
	// Device fields
	this.id = deviceId;
	this.operatingSystem = deviceOS;
	this.systemArchitecture = deviceSysArch;
	this.processorMaker = deviceProcMaker;
	this.processorArchitecture = deviceProcArch;
	this.numCPUs = deviceNumCPUs;
	this.numCores = deviceNumCores;
	this.RAMSize = deviceRAMSize;
	this.storageSize = deviceStorageSize;
	this.agentType = deviceAgentType;
	this.actuatorInfo = deviceActuatorInfo;
	this.limitedPower = deviceLimitedPower;
	this.graphicsCardInfo = deviceGraphicsCard;
	this.CPU_info = null;
	this.hwloc = null;
    }

    public String getId() {
	return this.id;
    }

    public void setOperatingSystem(String newOperatingSystem) {
	this.operatingSystem = newOperatingSystem;
	resourceUpdated();
    }

    public void setNumCores(int newNumCores) {
	this.numCores = newNumCores;
	resourceUpdated();
    }

    public void setRAMSize(float newRAMSize) {
	RAMSize = newRAMSize;
	resourceUpdated();
    }

    public void setStorageSize(float newStorageSize) {
	this.storageSize = newStorageSize;
	resourceUpdated();
    }

    public void setCPUInfo(String cpuInfo) {
	this.CPU_info = cpuInfo;
	resourceUpdated();
    }

    public String getCPUInfo() {
	return this.CPU_info;
    }

    public void setHwloc(String hwloc) {
	this.hwloc = hwloc;
	resourceUpdated();
    }

    public String getHwloc() {
	return this.hwloc;
    }
}