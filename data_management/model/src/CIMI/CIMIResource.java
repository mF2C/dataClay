package CIMI;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import dataclay.DataClayExecutionObject;
import dataclay.DataClayObject;
import dataclay.util.replication.Replication;

@SuppressWarnings({ "unchecked", "serial" })
public class CIMIResource extends DataClayObject {
	/** 
	 * ==== READ THIS! ====
	 * An attribute for each field in the CIMI resource spec, with the same name and type.
	 * 
	 * 1) If it contains nested info or its a href, it is implemented as a Map<String, Object>
	 *  where String is the field name, and Object is the value
	 *  
	 * 2) If you want your class to be shared/visible in the Leader, please annotate the class 
	 * 		with @ReplicateInLeader 
	 *  
	 * 3) if you want to synchronize your field with agent leaders: add following annotations to your field: 
	 *		@Replication.InMaster and 
	 * 		@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	 * 
	 * 4) WARNING: All fields must be public or no modifier defined.
	 * 
	 * 5) All types defined must be Object types (Integer,, List...) and not primitive types (int,...) 
	 *    WARNING!!: Use Double for floating point numbers due to JSON restrictions
	 * 
	 * 6) Once finished, please do a Pull request and after testing and verifications dataClay will publish 
	 * a new docker image with your changes/new resource model. 
	 **/
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

	public CIMIResource(final Map<String, Object> resourceData) {
		// Set all fields
		updateAllData(resourceData);
		
	}

	/**
	 * Get all cimi resource data
	 * @return Resource data representation for JSON
	 */
	public Map<String, Object> getCIMIResourceData() {
		final Map<String, Object> info = new HashMap<>();
		Class<?> currentClass = this.getClass(); 
		while (!currentClass.equals(DataClayObject.class) 
				&& !currentClass.equals(DataClayExecutionObject.class)) {
			for (final Field declaredField : currentClass.getDeclaredFields()) { 
				final String fieldName = declaredField.getName();
				info.put(fieldName, getFieldValue(fieldName));
			}
			currentClass = currentClass.getSuperclass();
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
	 * Cast parameter to double if needed
	 * @param obj parameter to cast
	 * @return double type representation of parameter provided
	 */
	public Double castToDouble(final Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Integer) {
			return ((Integer) obj).doubleValue();
		} else if (obj instanceof Long) {
			return ((Long) obj).doubleValue();
		} else {
			return (Double) obj;
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
		Field declaredField = null;
		while (!currentClass.equals(DataClayObject.class) 
				&& !currentClass.equals(DataClayExecutionObject.class) && !found) {
			try { 
				declaredField = currentClass.getDeclaredField(fieldName);
				final boolean accessible = declaredField.isAccessible();
				if (declaredField.getType().equals(Double.class)) { 
					// Cast to Double
					fieldValue = castToDouble(fieldValue);
				}
				declaredField.setAccessible(true);
				
				try { 
					final Method setUpdate = currentClass.getDeclaredMethod("$$setUpdate$$" + fieldName, new Class<?>[] {declaredField.getType(), Boolean.class});
					setUpdate.invoke(this, new Object[] {fieldValue, new Boolean(true)});
				} catch (final NoSuchMethodException em) {
					declaredField.set(this, fieldValue);
				}
				declaredField.setAccessible(accessible);
				found = true;
			} catch (final NoSuchFieldException e) { 				
				currentClass = currentClass.getSuperclass();
			} catch (final IllegalArgumentException e) {
				throw new RuntimeException("Could not set field " + fieldName 
						+ " for class " + this.getClass().getName() + ": type mismatch, "
								+ " check JSON provided or field type defined."
								+ " Provided type: " + fieldValue.getClass().getName()
								+ " and expected: " + declaredField.getType().getName());
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
			} catch (final InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!found) { 
			System.err.println("Could not set field " + fieldName 
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
		while (!currentClass.equals(DataClayObject.class) 
				&& !currentClass.equals(DataClayExecutionObject.class) && !found) {
			try {
				final Field declaredField = currentClass.getDeclaredField(fieldName);
				final boolean accessible = declaredField.isAccessible();
				declaredField.setAccessible(true);
				fieldValue = declaredField.get(this);
				if (declaredField.getType().equals(Double.class)) { 
					// Cast to Double
					fieldValue = castToDouble(fieldValue);
				}
				declaredField.setAccessible(accessible);
				found = true;
			} catch (final NoSuchFieldException e) { 				
				currentClass = currentClass.getSuperclass();
			} catch (final IllegalArgumentException e) {
				e.printStackTrace();
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (!found) {				
			System.err.println("Could not get field " + fieldName 
					+ " for class " + this.getClass().getName() + ". Check if defined.");
		}
		return fieldValue;
	}

	@Override
	public void whenUnfederated() {
		// when the object arrives to current dataClay, it is automatically removed from the
		// corresponding resource collection.

		// first part of the class name, in execution runtime, is the namespace
		final String completeClassName = this.getClass().getName();
		final String className = completeClassName.substring(this.getClass().getName().lastIndexOf('.') + 1);
		final ResourceCollection resources = (ResourceCollection) ResourceCollection
				.getByAliasExt(className + "Collection");
		System.out.println("REMOVING " + this.id);
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
				.getByAliasExt(className + "Collection");
		resources.put(this.id, this);

		// PROPAGATE
		final Agent agent = Agent.getByAliasExt("agent/agent");

		// to leader
		// FIXME: Agent must call a function due to issues in dataClay (not included in accessed implementations so no
		// renaming of signatures)
		propagate(agent.getLeaderIP());

	}

	protected void propagate(final String ip) { 
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
