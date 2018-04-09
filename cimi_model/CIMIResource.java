package CIMI;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public abstract class CIMIResource extends DataClayObject {
	private String id;
    private String name;
    private String description;
    private String resourceURI;
    private Calendar created;
    private Calendar updated;

    public CIMIResource(final String resourceID, final String resourceName, final String resourceDescription,
	    String resourceTypeURI) {
    	
    	this.id = resourceID.toString();
    	this.name = resourceName;
    	this.description = resourceDescription;
    	this.resourceURI = resourceTypeURI.toString();
    	this.created = Calendar.getInstance();
    	this.updated = Calendar.getInstance();
    }
  
    public void resourceUpdated() {
    	updated = Calendar.getInstance();
    }

    public String getResourceId() {
    	return id;
    }

    public String getName() {
    	return name;
    }
    
    public Calendar getCreated() {
    	return created;
    }

    public Calendar getUpdated() {
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
    
    public Map<String, Object> getCIMIResourceInfo() {
    	Map<String, Object> info = new HashMap<>();
    	info.put("id", this.id);
    	info.put("name", this.name);
    	info.put("description", this.description);
    	info.put("resourceURI", this.resourceURI);
    	info.put("created", this.created);
    	info.put("updated", this.updated);
    	return info;
    }
	
}
