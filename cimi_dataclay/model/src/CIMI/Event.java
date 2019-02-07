package CIMI;

import java.util.Map;

public class Event extends CIMIResource {
	// An attribute for each field in the CIMI resource spec, with the same name and
	// type.
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	private String timestamp;
	private Map<String, Object> content;
	// content is a nested field
	// resource: String - reference to a CIMI resource
	// state: String 
	private String type; //enum controlled by CIMI
	private String severity; //enum controlled by CIMI
	
	public Event(final Map<String, Object> objectData) {
		super(objectData);
		this.timestamp = (String) objectData.get("timestamp");
		this.content = (Map<String, Object>) objectData.get("content");
		this.type = (String) objectData.get("type");
		this.severity = (String) objectData.get("severity");
	}
	
	// Setters (a setter for each property called "set_propertyname")
	public void set_timestamp(final String timestamp) {
		this.timestamp = timestamp;
	}
	
	public void set_content(final Map<String, Object> content) {
		this.content = content;
	}
	
	public void set_type(final String type) {
		this.type = type;
	}
	
	public void set_severity(final String severity) {
		this.severity = severity;
	}
	
	
	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "getCIMIResourceData"
	@Override
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = super.getCIMIResourceData();
		if (this.timestamp != null)
			info.put("timestamp", this.timestamp);
		if (this.content != null)
			info.put("content", this.content);
		if (this.type != null)
			info.put("type", this.type);
		if (this.severity != null)
			info.put("severity", this.severity);
		return info;
	}
	
	public void updateAllData(final Map<String, Object> data) {
		super.setCIMIResourceData(data);
		if (data.get("timestamp") != null)
			set_timestamp((String) data.get("timestamp"));
		if (data.get("content") != null)
			set_content((Map<String, Object>) data.get("content"));
		if (data.get("type") != null)
			set_type((String) data.get("type"));
		if (data.get("severity") != null)
			set_severity((String) data.get("severity"));
	}	
	
}
