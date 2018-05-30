package CIMI;

import java.util.Map;

@SuppressWarnings("serial")
public class SlaViolation extends CIMIResource {
	// An attribute for each field in the CIMI resource spec, with the same name and
	// type.
	// If it contains nested info, it is implemented as a Map<String, Object>
	// where String is the field name, and Object is the value
	private String agreement_id;
	private String guarantee;
	private String datetime;

	// Constructor, with a parameter for each attribute in this class and in
	// CIMIResource
	public SlaViolation(final Map<String, Object> objectData) {
		super(objectData);
		this.agreement_id = (String) objectData.get("agreement_id");
		this.guarantee = (String) objectData.get("guarantee");
		this.datetime = (String) objectData.get("datetime");
	}

	// Setters (a setter for each property called "set_propertyname")
	public void set_agreement_id(final String agreement_id) {
		this.agreement_id = agreement_id;
	}

	public void set_guarantee(final String guarantee) {
		this.guarantee = guarantee;
	}

	public void set_datetime(final String datetime) {
		this.datetime = datetime;
	}

	// A single getter that returns a Map with all the info in this class and in
	// CIMIResource, called "getCIMIResourceData"
	@Override
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = super.getCIMIResourceData();
		if (this.agreement_id != null)
			info.put("agreement_id", this.agreement_id);
		if (this.guarantee != null)
			info.put("guarantee", this.guarantee);
		if (this.datetime != null)
			info.put("datetime", this.datetime);
		return info;
	}

	public void updateAllData(final Map<String, Object> data) {
		super.setCIMIResourceData(data);
		if (data.get("agreement_id") != null)
			set_agreement_id((String) data.get("agreement_id"));
		if (data.get("guarantee") != null)
			set_guarantee((String) data.get("guarantee"));
		if (data.get("datetime") != null)
			set_datetime((String) data.get("datetime"));
	}

}
