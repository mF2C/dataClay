package model;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unused", "serial" })
public class Agent extends CIMIResource {

    /* Simplified version */

    private String id; // Agent id
    private Device device;
    private Map<String, Agent> children;
    private boolean isLeader;
    private boolean isCloud;
    // private boolean multiCloudFederation;
    // private AggregatedResourceInfo aggregatedInfo;

    public Agent(String id, Device myDevice, String resourceID, String resourceName, String resourceDescription,
	    String resourceURI) {

	super(resourceID, resourceName, resourceDescription, resourceURI);
	this.id = id;
	this.device = myDevice;
	this.children = new HashMap<String, Agent>();
	this.isLeader = false;
	this.isCloud = false;
    }

    public String getId() {
	return this.id;
    }

    public Device getDevice() {
	return device;
    }

    public void addChild(Agent child) {
	if (isLeader) {
	    children.put(child.id, child);
	    resourceUpdated();
	} else {
	    throw new RuntimeException("Trying to add a child to a normal agent");
	}
    }

    public void removeChild(Agent child) {
	if (isLeader) {
	    children.remove(child.id);
	    resourceUpdated();
	} else {
	    throw new RuntimeException("Trying to remove a child from a normal agent");
	}
    }

    public boolean checkChildExists(String child) {
	if (isLeader) {
	    return this.children.containsKey(child);
	} else {
	    throw new RuntimeException("Trying to check child on a normal agent");
	}
    }

    public Agent getChild(String child) {
	return this.children.get(child);
    }

    public void setCloud() {
	this.isCloud = true;
    }

    public void setLeader() {
	this.isLeader = true;
    }

    public void setHwloc(String text) {
	this.device.setHwloc(text);
    }

    public void setCPUInfo(String text) {
	this.device.setCPUInfo(text);
    }

    public String getHwloc() {
	return this.device.getHwloc();
    }

    public String getCPUInfo() {
	return this.device.getCPUInfo();
    }

}
