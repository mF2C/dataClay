package model;

import java.util.HashSet;
import java.util.Set;

import dataclay.DataClayObject;

@SuppressWarnings({ "unused", "serial" })
public class Agent extends CIMIResource {

	/* Simplified version */

	private String id; // Agent id
	private String deviceAlias;
	private Set<String> children;
	private boolean isLeader;
	private boolean isCloud;
	// private boolean multiCloudFederation;
	// private AggregatedResourceInfo aggregatedInfo;

	public Agent(String id, String myDevice, String resourceID, String resourceName, String resourceDescription,
			String resourceURI) {

		super(resourceID, resourceName, resourceDescription, resourceURI);
		this.id = id;
		this.deviceAlias = myDevice;
		this.children = new HashSet<>();
		this.isLeader = false;
		this.isCloud = false;
	}

	public String getId() {
		return this.id;
	}

	public Device getDevice() {
		return (Device) DataClayObject.getByAlias("CIMIFED_NS.model.Device", deviceAlias);
	}
	
	public Agent getAgent(final String agentID) {
		return (Agent) DataClayObject.getByAlias("CIMIFED_NS.model.Agent", agentID);
	}

	public void addChild(String child) {
		if (isLeader) {
			children.add(child);
			resourceUpdated();
		} else {
			throw new RuntimeException("Trying to add a child to a normal agent");
		}
	}

	public void removeChild(Agent child) {
		if (isLeader) {
			children.remove(child.getId());
			resourceUpdated();
		} else {
			throw new RuntimeException("Trying to remove a child from a normal agent");
		}
	}

	public boolean checkChildExists(String child) {
		if (isLeader) {
			return this.children.contains(child);
		} else {
			throw new RuntimeException("Trying to check child on a normal agent");
		}
	}

	public Agent getChild(String child) {
		checkChildExists(child);
		return getAgent(child);		
	}

	public void setCloud() {
		this.isCloud = true;
	}

	public void setLeader() {
		this.isLeader = true;
	}

	public void setHwloc(String text) {
		getDevice().setHwloc(text);
	}

	public void setCPUInfo(String text) {
		getDevice().setCPUInfo(text);
	}

	public String getHwloc() {
		return getDevice().getHwloc();
	}

	public String getCPUInfo() {
		return getDevice().getCPUInfo();
	}

}
