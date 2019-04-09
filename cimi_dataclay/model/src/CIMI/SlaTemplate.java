package CIMI;

import java.util.Map;

import dataclay.util.replication.Replication;

@SuppressWarnings({ "unchecked", "serial" })
public class SlaTemplate extends CIMIResource {

	// An attribute for each field in the CIMI resource spec, with the same name and
	// type.
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private String state; // "started", "stopped" or "terminated"
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private Map<String, Object> details;
	// details is a nested field
	// id: String
	// type: String - Agreement or Template
	// name: String
	// provider: Map<String, String> - another nested field: id, name
	// client: Map<String, String> - another nested field: id, name
	// creation: String
	// expiration: String
	// guarantees: List<Map<String, String>> - another nested field: name,
	// constraint

	public SlaTemplate(final Map<String, Object> objectData) {
		super(objectData);
		this.state = (String) objectData.get("state");
		this.details = (Map<String, Object>) objectData.get("details");
	}

	// Setters (a setter for each property called "set_propertyname")
	public void set_state(final String state) {
		this.state = state;
	}

	public void set_details(final Map<String, Object> details) {
		this.details = details;
	}

	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "getCIMIResourceData"
	@Override
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = super.getCIMIResourceData();
		if (this.state != null)
			info.put("state", this.state);
		if (this.details != null)
			info.put("details", this.details);
		return info;
	}

	public void updateAllData(final Map<String, Object> data) {
		setCIMIResourceData(data);
		if (data.get("state") != null)
			set_state((String) data.get("state"));
		if (data.get("details") != null)
			set_details((Map<String, Object>) data.get("details"));
	}

}
