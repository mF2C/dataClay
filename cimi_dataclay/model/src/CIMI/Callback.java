package CIMI;

import java.util.Map;

public class Callback extends CIMIResource {

	// An attribute for each field in the CIMI resource spec, with the same name
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	private String action;
	private String state;
	private Map<String, Object> targetResource;
	private Map<String, Object> data;
	private String expires;

	// Constructor
	public Callback(final Map<String, Object> objectData) {
		super(objectData);
		this.action = (String) objectData.get("action");
		this.state = (String) objectData.get("state");
		this.targetResource = (Map<String, Object>) objectData.get("targetResource");
		this.data = (Map<String, Object>) objectData.get("data");
		this.expires = (String) objectData.get("expires");
	}

	// Setters (a setter for each property called "set_propertyname")
	public void set_action(final String action) {
		this.action = action;
	}

	public void set_state(final String state) {
		this.state = state;
	}

	public void set_targetResource(final Map<String, Object> targetResource) {
		this.targetResource = targetResource;
	}

	public void set_data(final Map<String, Object> data) {
		this.data = data;
	}

	public void set_expires(final String expires) {
		this.expires = expires;
	}

	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "get_classname_info"
	@Override
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = super.getCIMIResourceData();
		if (this.action != null)
			info.put("action", this.action);
		if (this.state != null)
			info.put("state", this.state);
		if (this.targetResource != null)
			info.put("targetResource", this.targetResource);
		if (this.data != null)
			info.put("data", this.data);
		if (this.expires != null)
			info.put("expires", this.expires);
		return info;
	}

	public void updateAllData(final Map<String, Object> data) {
		setCIMIResourceData(data);
		if (data.get("action") != null)
			set_action((String) data.get("action"));
		if (data.get("state") != null)
			set_state((String) data.get("state"));
		if (data.get("targetResource") != null)
			set_targetResource((Map<String, Object>) data.get("targetResource"));
		if (data.get("data") != null)
			set_data((Map<String, Object>) data.get("data"));
		if (data.get("expires") != null)
			set_expires((String) data.get("expires"));
	}

}