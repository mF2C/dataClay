import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class DataClayWrapper {
	
	//TODO: return error or exception if it exists?
	public static void create(String type, String id, String data) throws Exception {
		//type: resource type name (lower-case part of the resource ID before the slash)
		//id: resource id (part of the resource ID after the slash, which may not actually be a uuid)
		//data: json file passed to the CIMI server by the user, with the server-managed fields 
		//      (id, resourceID, created, updated) added/replaced as necessary
		if (type == null) 
			throw new IllegalArgumentException("Argument 'type' is empty");
		if (id == null) 
			throw new IllegalArgumentException("Argument 'id' is empty");
		if (data == null)
			throw new IllegalArgumentException("Argument 'data' is empty");
		
		Map<String, Object> objectData = new JSONObject(data).toMap();
		objectData = preProcessSubObjects(type, objectData);
		CIMIResource obj;
		switch (type) {
			case "agreement":
				obj = new Agreement(objectData);
				break;
			case "device":
				obj = new Device(objectData);
				break;
			case "device-dynamic":
				obj = new DeviceDynamic(objectData);
				break;
			case "fog-area":
				obj = new FogArea(objectData);
				break;
			case "service":
				obj = new Service(objectData);
				break;
			case "service-instance":
				obj = new ServiceInstance(objectData);
				break;
			case "sharing-model":
				obj = new SharingModel(objectData);
				break;
			case "sla-violation":
				obj = new SlaViolation(objectData);
				break;
			case "user-profile":
				obj = new UserProfile(objectData);
				break;
			default:
				throw new IllegalArgumentException("Invalid resource type: " + type);
		}
		obj.makePersistent(id); //id is the alias
		
		ResourceCollection resources;
		String className = javaize(type);
		try {
			resources = 
					(ResourceCollection) ResourceCollection.getByAlias(className+"Collection");
		} catch (Exception e) { 
			resources = new ResourceCollection();
			resources.makePersistent(className+"Collection");
		}
		resources.put(obj);
			
	}
	
	//TODO is it ok that the method throws an exception (from getResourceAsObject)?
	public static String read(String type, String id) throws Exception {
		//returns json file with all the resource data
		if (type == null) 
			throw new IllegalArgumentException("Argument 'type' is empty");
		if (id == null) 
			throw new IllegalArgumentException("Argument 'id' is empty");
		
		CIMIResource obj = getResourceAsObject(type, id);
		Map<String, Object> info = obj.getCIMIResourceData();
		info = postProcessSubObjects(type, info);
		JSONObject json = new JSONObject(info);
		return json.toString();
	}
	
	//TODO is it ok that the method throws an exception (from getResourceAsObject)?
	public static void delete(String type, String id) throws Exception {
		if (type == null) 
			throw new IllegalArgumentException("Argument 'type' is empty");
		if (id == null) 
			throw new IllegalArgumentException("Argument 'id' is empty");
		
		CIMIResource obj = getResourceAsObject(type, id);
		obj.deleteAlias("id");
		ResourceCollection resources = 
				(ResourceCollection) ResourceCollection.getByAlias(javaize(type)+"Collection");
		resources.delete("id");
	}
	
	//TODO is it ok that the method throws an exception (from getResourceAsObject)?
	public static void update(String type, String id, String updatedData) throws Exception {
		if (type == null) 
			throw new IllegalArgumentException("Argument 'type' is empty");
		if (id == null) 
			throw new IllegalArgumentException("Argument 'id' is empty");
		if (updatedData == null)
			throw new IllegalArgumentException("Argument 'data' is empty");
		
		JSONObject json = new JSONObject(updatedData);
		Map<String, Object> objectData = json.toMap();
		objectData = preProcessSubObjects(type, objectData);
		switch (type) {
		case "agreement":
			Agreement agr = (Agreement) Agreement.getByAlias("id");
			agr.updateAllData(objectData);
			break;
		case "device":
			Device dev = (Device) Device.getByAlias("id");
			dev.updateAllData(objectData);
			break;
		case "device-dynamic":
			DeviceDynamic dd = (DeviceDynamic) DeviceDynamic.getByAlias("id");
			dd.updateAllData(objectData);
			break;
		case "fog-area":
			FogArea fa = (FogArea) FogArea.getByAlias("id");
			fa.updateAllData(objectData);
			break;
		case "service":
			Service serv = (Service) Service.getByAlias("id");
			serv.updateAllData(objectData);
			break;
		case "service-instance":
			ServiceInstance si = (ServiceInstance) ServiceInstance.getByAlias("id");
			si.updateAllData(objectData);
			break;
		case "sharing-model":
			SharingModel sm = (SharingModel) SharingModel.getByAlias("id");
			sm.updateAllData(objectData);
			break;
		case "sla-violation":
			SlaViolation sv = (SlaViolation) SlaViolation.getByAlias("id");
			sv.updateAllData(objectData);
			break;
		case "user-profile":
			UserProfile up = (UserProfile) UserProfile.getByAlias("id");
			up.updateAllData(objectData);
			break;
		default:
			throw new IllegalArgumentException("Invalid resource type: " + type);
		}
	}
	
	//Simplification of ACLs for IT-1: name of the user who executes the query, and its single role
	public static List<String> query(String type, String expression, String user, String role) {
		String aclUser = aclQuery(user); 
		String aclRole = aclQuery(role);
		String aclExpression =  "[:OrExpr [" + aclUser + "] + [" + aclRole + "]]";
		String removedFilter = expression.substring(9); //Remove the "[:Filter " at the beginning
		String expressionWithAcl = "[:Filter [:AndExpr " + aclExpression + removedFilter + "]";
		
		ResourceCollection collection = (ResourceCollection) ResourceCollection.getByAlias(javaize(type)+"Collection");
		List<CIMIResource> resultSet = collection.filterResources(expressionWithAcl);
		List<String> result = new ArrayList<String>(); 
		for (CIMIResource obj: resultSet) {
			Map<String, Object> info = obj.getCIMIResourceData();
			info = postProcessSubObjects(type, info);
			JSONObject json = new JSONObject(info);
			result.add(json.toString());
		}
		return result;
	}
	
	private static String aclQuery(String value) {
		return "[:OrExpr "
				+ "[:Comp [:Attribute owner] [:EqOp =] [:SingleQuoteString '" + value + "']] "
				+ "[:Comp [:Attribute permissions] [:EqOp =] [:SingleQuoteString '" + value + "']] "
				+ "]";
	}
	
	private static String javaize(String type) {
		//First character to upper case
		String className = capitalize(type);
		int slash = className.indexOf("-");
		//I assume we only have a single slash in a resource type (all of them are like this now) 
		if (slash > 0) {
			//Separate first and second parts, without the slash
			String first = className.substring(0, slash);
			String second = className.substring(slash+1);
			//First character of second to upper case
			second = capitalize(second);
			className = first.concat(second);
		}
		return className;
	}
	
	private static String capitalize(String word) {
		return word.substring(0,1).toUpperCase() + word.substring(1);
	}
	
	private static CIMIResource getResourceAsObject(String type, String id) throws Exception {
		CIMIResource obj;
		switch (type) {
			case "agreement":
				obj = (Agreement) Agreement.getByAlias("id");
				break;
			case "device":
				obj = (Device) Device.getByAlias("id");
				break;
			case "device-dynamic":
				obj = (DeviceDynamic) DeviceDynamic.getByAlias("id");
				break;
			case "fog-area":
				obj = (FogArea) FogArea.getByAlias("id");
				break;
			case "service":
				obj = (Service) Service.getByAlias("id");
				break;
			case "service-instance":
				obj = (ServiceInstance) ServiceInstance.getByAlias("id");
				break;
			case "sharing-model":
				obj = (SharingModel) SharingModel.getByAlias("id");
				break;
			case "sla-violation":
				obj = (SlaViolation) SlaViolation.getByAlias("id");
				break;
			case "user-profile":
				obj = (UserProfile) UserProfile.getByAlias("id");
				break;
			default:
				throw new IllegalArgumentException("Invalid resource type: " + type);
		}
		return obj;
	}
	
	private static Map<String, Object> preProcessSubObjects(String type, Map<String, Object> objectData) throws Exception {
		CIMIResource obj;
		String resourceId = (String) objectData.get("id");
		String subType = resourceId.substring(0, resourceId.indexOf("/"));
		String subId = resourceId.substring(resourceId.indexOf("/"));
		switch(type) {
		case "device-dynamic":
			resourceId = (String) objectData.get("device");
			if (resourceId != null) {
				obj = getResourceAsObject(subType, subId);
				objectData.put("device", obj);
			}
			break;
		case "fog-area":
			resourceId = (String) objectData.get("leaderDevice");
			if (resourceId != null) {
				obj = getResourceAsObject(subType, subId);
				objectData.put("leaderDevice", obj);
			}
			break;
		default:
			break;
		}
		return objectData;
	}
	
	private static Map<String, Object> postProcessSubObjects(String type, Map<String, Object> objectData) {
		CIMIResource obj;
		switch(type) {
		case "device-dynamic":
			obj = (CIMIResource) objectData.get("device");
			if (obj != null) {
				objectData.put("device", obj.get_id());
			}
			break;
		case "fog-area":
			obj = (CIMIResource) objectData.get("leaderDevice");
			if (obj != null) {
				objectData.put("leaderDevice", obj.get_id());
			}
			break;
		default:
			break;
		}
		return objectData;
		
	}
	

}
