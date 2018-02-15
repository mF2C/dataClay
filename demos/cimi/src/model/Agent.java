package model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Agent extends CIMIResource {

    /* Simplified version */

    private String id; // Agent id
    private Device device;
    private List<Agent> children;
    private boolean isLeader;
    private boolean isCloud;
    // private boolean multiCloudFederation;
    // private AggregatedResourceInfo aggregatedInfo;

    public Agent(String id, Device myDevice, String resourceID, String resourceName, String resourceDescription,
	    String resourceURI) {

	super(resourceID, resourceName, resourceDescription, resourceURI);
	this.id = id;
	this.device = myDevice;
	this.children = new ArrayList<Agent>();
	this.isLeader = false;
	this.isCloud = false;
    }

    public Device getDevice() {
	return device;
    }

    public void addChild(Agent child) {
	if (isLeader) {
	    children.add(child);
	    resourceUpdated();
	} else {
	    throw new RuntimeException("Trying to add a child to a normal agent");
	}
    }

    public void removeChild(Agent child) {
	if (isLeader) {
	    children.remove(child);
	    resourceUpdated();
	} else {
	    throw new RuntimeException("Trying to remove a child from a normal agent");
	}
    }

    public boolean checkChildExists(Agent child) {
	if (isLeader) {
	    return children.contains(child);
	} else {
	    throw new RuntimeException("Trying to check child on a normal agent");
	}
    }

}
