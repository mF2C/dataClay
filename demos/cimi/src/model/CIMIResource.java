package model;

import java.util.Calendar;

public class CIMIResource {
    private String resourceID;
    private String name;
    private String description;
    private String resourceURI;
    private String created;
    private String updated;

    public CIMIResource(final String resourceID, final String resourceName, final String resourceDescription,
	    String resourceURI) {
	this.resourceID = resourceID;
	this.name = resourceName;
	this.description = resourceDescription;
	this.resourceURI = resourceURI;
	created = Calendar.getInstance().toString();
	updated = Calendar.getInstance().toString();
    }
    
    public void resourceUpdated() {
	updated = Calendar.getInstance().toString();
    }

    public String getResourceId() {
	return resourceID;
    }

    public String getName() {
	return name;
    }
    
    public String getCreated() {
	return created;
    }

    public String getUpdated() {
	return updated;
    }

    public String getDescription() {
	return description;
    }
    
    public void updateDescription(String newDescription) {
	this.description = newDescription;
	resourceUpdated();
    }

    public String getResourceURI() {
	return resourceURI;
    }

    public void updateResourceURI(String newResourceURI) {
	this.resourceURI = newResourceURI;
	resourceUpdated();
    }
}
