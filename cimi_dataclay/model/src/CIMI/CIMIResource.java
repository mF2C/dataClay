package CIMI;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dataclay.DataClayObject;
import dataclay.util.replication.Replication;

@SuppressWarnings({ "unchecked", "serial" })
public abstract class CIMIResource extends DataClayObject {

	String id;
	String name;
	String description;
	String resourceURI;
	String created;
	@Replication.InMaster
	@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	String updated;

	Map<String, Object> acl;
	// owner: Map
	// principal: String
	// type: String
	// rules: List<Map>
	// principal: String
	// type: String
	// right: String
	// Aux fields to implement the filter more easily:
	String owner;
	String permissions;

	public CIMIResource(final Map<String, Object> resourceData) {
		// Set all fields
		updateAllData(resourceData);
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

	/**
	 * Get all cimi resource data
	 * @return Resource data representation for JSON
	 */
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = new HashMap<>();
		for (final Field declaredField : this.getClass().getDeclaredFields()) { 
			final String fieldName = declaredField.getName();
			info.put(fieldName, getFieldValue(fieldName));
		}

		return info;
	}

	public void updateAllData(final Map<String, Object> resourceData) {
		for (final Entry<String, Object> entry : resourceData.entrySet()) { 
			final String key = entry.getKey();
			final Object value = entry.getValue();
			setFieldValue(key, value);
		}
	}

	/**
	 * Cast parameter to float if needed
	 * @param obj parameter to cast
	 * @return Float type representation of parameter provided
	 */
	public Float castToFloat(final Object obj) {
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

	/**
	 * Set field with name provided and value specified
	 * @param fieldName Field to set
	 * @param newvalue Value to set
	 */
	public void setFieldValue(final String fieldName, final Object newvalue) { 

		Object fieldValue = newvalue;
		Class<?> currentClass = this.getClass(); 
		boolean found = false;
		while (!currentClass.equals(DataClayObject.class) && !found) {
			try { 
				final Field declaredField = currentClass.getDeclaredField(fieldName);
				final boolean accessible = declaredField.isAccessible();
				if (declaredField.getType().equals(Float.class)) { 
					// Cast to Float since JSON have double or other types 
					fieldValue = castToFloat(fieldValue);
				}
				declaredField.setAccessible(true);
				declaredField.set(this, fieldValue);
				declaredField.setAccessible(accessible);
				found = true;
			} catch (final NoSuchFieldException e) { 				
				currentClass = currentClass.getSuperclass();
			} catch (final IllegalArgumentException e) {
				throw new RuntimeException("Could not set field " + fieldName 
						+ " for class " + this.getClass().getName() + ": type mismatch, check JSON provided or field type defined.");
			} catch (final IllegalAccessException e) {
				throw new RuntimeException("Could not set field " + fieldName 
						+ " for class " + this.getClass().getName() + ": internal error.");
			}
		}
		if (!found) { 
			throw new RuntimeException("Could not set field " + fieldName 
					+ " for class " + this.getClass().getName() + ". Check if defined.");
		}
	}

	/**
	 * Get value of field with name provided
	 * @param fieldName Field name
	 * @return Field value
	 */
	public Object getFieldValue(final String fieldName) { 
		Class<?> currentClass = this.getClass(); 
		boolean found = false;
		Object fieldValue = null;
		while (!currentClass.equals(DataClayObject.class) && !found) {
			try {
				final Field declaredField = currentClass.getDeclaredField(fieldName);
				final boolean accessible = declaredField.isAccessible();
				declaredField.setAccessible(true);
				fieldValue = declaredField.get(this);
				if (declaredField.getType().equals(Float.class)) { 
					// Cast to Float since JSON have double or other types 
					fieldValue = castToFloat(fieldValue);
				}
				declaredField.setAccessible(accessible);
				found = true;
			} catch (final NoSuchFieldException e) { 				
				currentClass = currentClass.getSuperclass();
			} catch (final IllegalArgumentException e) {
				throw new RuntimeException("Could not set field " + fieldName 
						+ " for class " + this.getClass().getName() + ": type mismatch, check JSON provided or field type defined.");
			} catch (final IllegalAccessException e) {
				throw new RuntimeException("Could not set field " + fieldName 
						+ " for class " + this.getClass().getName() + ": internal error.");
			}
		}
		if (!found) {				
			throw new RuntimeException("Could not get field " + fieldName 
					+ " for class " + this.getClass().getName() + ". Check if defined.");
		}
		return fieldValue;
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
		propagate((String) agent.getFieldValue("leader_ip"));

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
