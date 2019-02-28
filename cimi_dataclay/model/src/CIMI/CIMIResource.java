package CIMI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dataclay.DataClayObject;
import dataclay.util.replication.Replication;

@SuppressWarnings({ "unchecked", "serial" })
public abstract class CIMIResource extends DataClayObject {
	private String id;
	private String name;
	private String description;
	private String resourceURI;
	private String created;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	private String updated;
	private Map<String, Object> acl;
	// owner: Map
	// principal: String
	// type: String
	// rules: List<Map>
	// principal: String
	// type: String
	// right: String
	// Aux fields to implement the filter more easily:
	@SuppressWarnings("unused")
	private String owner;
	@SuppressWarnings("unused")
	private String permissions;
	private String href;

	public CIMIResource(final Map<String, Object> resourceData) {
		setCIMIResourceData(resourceData);
		if (acl != null) {
			final Map<String, Object> ownerValue = (Map<String, Object>) acl.get("owner");
			this.owner = (String) ownerValue.get("principal");
			final List<Map<String, Object>> rulesValue = (List<Map<String, Object>>) acl.get("rules");
			// TODO For the moment we assume there is only one element
			if (rulesValue != null) {
				final Map<String, Object> permission = rulesValue.get(0);
				this.permissions = (String) permission.get("principal");
			} else {
				this.permissions = this.owner;
			}
		}
	}

	public String get_id() {
		return id;
	}

	public String get_name() {
		return name;
	}

	public void set_name(final String name) {
		this.name = name;
	}

	public String get_created() {
		return created;
	}

	public void set_created(final String created) {
		this.created = created;
	}

	public String get_updated() {
		return updated;
	}

	public void set_updated(final String updated) {
		this.updated = updated;
	}

	public String get_description() {
		return description;
	}

	public void set_description(final String newDescription) {
		this.description = newDescription;
	}

	public String get_resourceURI() {
		return resourceURI;
	}

	public void set_resourceURI(final String newResourceURI) {
		this.resourceURI = newResourceURI;
	}

	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = new HashMap<>();
		if (this.id != null)
			info.put("id", this.id);
		if (this.name != null)
			info.put("name", this.name);
		if (this.description != null)
			info.put("description", this.description);
		if (this.resourceURI != null)
			info.put("resourceURI", this.resourceURI);
		if (this.created != null)
			info.put("created", this.created);
		if (this.updated != null)
			info.put("updated", this.updated);
		if (this.acl != null)
			info.put("acl", this.acl);
		return info;
	}

	public void setCIMIResourceData(final Map<String, Object> newData) {
		if (newData.get("id") != null)
			this.id = (String) newData.get("id");
		if (newData.get("name") != null)
			this.name = (String) newData.get("name");
		if (newData.get("description") != null)
			this.description = (String) newData.get("description");
		if (newData.get("resourceURI") != null)
			this.resourceURI = (String) newData.get("resourceURI");
		if (newData.get("created") != null)
			this.created = (String) newData.get("created");
		if (newData.get("updated") != null)
			this.updated = (String) newData.get("updated");
		if (newData.get("acl") != null)
			this.acl = (Map<String, Object>) newData.get("acl");
		if (newData.get("href") != null)
			this.href = (String) newData.get("href");
		else
			this.href = this.id;
	}

	public Float getFloat(final Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Integer) {
			return ((Integer) obj).floatValue();
		} else if (obj instanceof Double) {
			return ((Double) obj).floatValue();
		} else if (obj instanceof Long) {
			return ((Long) obj).floatValue();
		} else {
			return (Float) obj;
		}
	}

	@Override
	public void whenUnfederated() {
		// when the object arrives to current dataClay, it is automatically added to the
		// corresponding resource collection.

		// first part of the class name, in execution runtime, is the namespace
		final String completeClassName = this.getClass().getName();
		final String className = completeClassName.substring(this.getClass().getName().lastIndexOf('.') + 1);
		final ResourceCollection resources = (ResourceCollection) ResourceCollection
				.getByAlias(className + "Collection");
		resources.delete(this.id);
	}
	
	@Override
	public void whenFederated() {
		// when the object arrives to current dataClay, it is automatically added to the
		// corresponding resource collection.

		// first part of the class name, in execution runtime, is the namespace
		final String completeClassName = this.getClass().getName();
		final String className = completeClassName.substring(this.getClass().getName().lastIndexOf('.') + 1);
		final ResourceCollection resources = (ResourceCollection) ResourceCollection
				.getByAlias(className + "Collection");
		resources.put(this.id, this);

		// PROPAGATE
		final Agent agent = Agent.getByAlias("agent");
			
		// to leader
		propagate(agent.get_leaderIP());

		// to backup
		propagate(agent.get_backupIP());


	}

	private void propagate(final String ip) { 
		final String addr = ip;
		if (addr != null && !addr.isEmpty()) {
			System.out.println("Refederating to " + addr);
			String addrIP = addr;
			int port = 1034;
			if (addrIP.contains(":")) { 
				final String[] addrSplit = addr.split(":");
				addrIP = addrSplit[0];
				port = Integer.valueOf(addrSplit[1]);
			} 
			this.federate(super.getExternalDataClayID(addrIP, port));
		}
	}

}
