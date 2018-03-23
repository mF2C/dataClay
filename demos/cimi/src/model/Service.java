package model;

import java.util.Map;

public class Service extends CIMIResource {
	
	private Map<String, Object> category;
	
	//cpu, memory and storage are encoded as int: 1=low, 2=medium, 3=high  
	public Service(int cpu, int memory, int storage, boolean inclinometer, 
			boolean temperature, boolean jammer, boolean location, 
			String resourceID, String resourceName, String resourceDescription, String resourceURI) {
			
		super(resourceID, resourceName, resourceDescription, resourceURI);
		this.category.put("cpu", cpu);
		this.category.put("memory", memory);
		this.category.put("storage", storage);
		this.category.put("inclinometer", inclinometer);
		this.category.put("temperature", temperature);
		this.category.put("jammer", jammer);
		this.category.put("location", location);
	}
	
	public void setCategory(Map<String, Object> newCategory) {
		this.category = newCategory;
		resourceUpdated();
	}
	
	public Map<String, Object> getCategory() {
		return this.category;
	}
}