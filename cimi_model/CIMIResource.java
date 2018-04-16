package CIMI;

import java.util.HashMap;
import java.util.Map;

public abstract class CIMIResource extends DataClayObject {
    private String id;
    private String name;
    private String description;
    private String resourceURI;
    private String created;
    private String updated;

    public CIMIResource(final String resourceID, final String resourceName, final String resourceDescription,
	    String resourceTypeURI, String dateCreated, String dateUpdated) {
    	
    	this.id = resourceID;
    	this.name = resourceName;
    	this.description = resourceDescription;
    	this.resourceURI = resourceTypeURI;
    	this.created = dateCreated;
    	this.updated = dateUpdated;
    }
  
    public String getResourceId() {
    	return id;
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
    }

    public String getResourceURI() {
    	return resourceURI;
    }

    public void updateResourceURI(String newResourceURI) {
    	this.resourceURI = newResourceURI;
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
