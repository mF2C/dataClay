package api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import CIMI.Agent;
import CIMI.Agreement;
import CIMI.CIMIResource;
import CIMI.Callback;
import CIMI.CloudEntryPoint;
import CIMI.Credential;
import CIMI.Device;
import CIMI.DeviceDynamic;
import CIMI.Email;
import CIMI.Event;
import CIMI.FogArea;
import CIMI.ResourceCollection;
import CIMI.Service;
import CIMI.ServiceInstance;
import CIMI.ServiceOperationReport;
import CIMI.Session;
import CIMI.SessionTemplate;
import CIMI.SharingModel;
import CIMI.SlaViolation;
import CIMI.User;
import CIMI.UserProfile;
import api.exceptions.DataClayFederationException;
import api.exceptions.ObjectAlreadyExistsException;
import api.exceptions.ObjectDoesNotExistException;
import api.exceptions.ResourceCollectionDoesNotExistException;
import api.exceptions.TypeDoesNotExistException;
import dataclay.commonruntime.ClientManagementLib;
import dataclay.exceptions.metadataservice.ObjectNotRegisteredException;
import dataclay.util.ids.DataClayInstanceID;

public class DataClayWrapper {

	/** Leader DataClay ID, if present. */
	public static DataClayInstanceID leaderDC = null;

	/** All CIMI resource types in model. */
	public static String[] resourceTypes = { "agent", "agreement", "device", "device-dynamic", "fog-area", "service",
			"service-instance", "sharing-model", "sla-violation", "user-profile", "service-operation-report",
			"cloud-entry-point", "email", "user", "credential", "session", "session-template", "callback", "event" };

	/** Suffix for aliases of Resource collections. */
	private static final String RESOURCE_COLLECTION_ALIAS_SUFFIX = "Collection";

	/** LogicModule port. */
	private static final int LOGICMODULE_PORT = 1034;
	
	/**
	 * Simulates a discovery process if LEADER_DC=host:port environment variable is set. 
	 * In any case, it creates the required resource collections in the agent.
	 * 
	 * @throws DataClayFederationException
	 *             Error while connecting to the dataClay in the leader
	 */
	public static void init() throws DataClayFederationException {
		final String leaderAddr = System.getenv("LEADER_DC");
		if (leaderAddr != null && !leaderAddr.isEmpty()) {
			// No discovery in this case, environment variable indicates leader for testing
			String leaderIP = leaderAddr.split(":")[0];
			connectToLeaderDataClay(leaderIP); 
		}
		createLocalResourceCollections();
	}

	/**
	 * Create a Resource collection per type in current dataClay
	 */
	private static void createLocalResourceCollections() {
		for (final String type : resourceTypes) {
			if (!type.equals("cloud-entry-point")) {
				final String className = javaize(type);
				final String alias = className + RESOURCE_COLLECTION_ALIAS_SUFFIX;
				try {
					ResourceCollection.getByAlias(alias);
				} catch (final Exception e) {
					final ResourceCollection resources = new ResourceCollection();
					resources.makePersistent(alias);
					System.out.println("-- Created collection: " + alias);
				}
			}
		}
	}

	/**
	 * Create object from JSON in dataClay
	 * 
	 * @param type
	 * 			resource type name (lower-case part of the resource ID before the
	 *            slash)
	 * @param id
	 * 			resource id (part of the resource ID after the slash, which may
	 *            not actually be a uuid)
	 * @param data
	 *			json file passed to the CIMI server by the user, with the
	 *          server-managed fields
	 * @throws IllegalArgumentException
	 *          if some argument is empty
	 * @throws TypeDoesNotExistException
	 * 			if argument 'type' is not a known resource type
	 * @throws ObjectDoesNotExistException
	 * 			if data includes a link to another resource that does not exist
	 * @throws ObjectAlreadyExistsException
	 * 			if there is already an object of 'type' with the same 'id'
	 * @throws DataClayFederationException
	 * 			if the new object could not be federated with the leader
	 * @throws ResourceCollectionDoesNotExistException 
	 * 			if the 'type' resource collection could not be found 
	 * 			
	 */
	public static void create(final String type, final String id, final String data) 
			throws IllegalArgumentException, TypeDoesNotExistException, 
			ObjectDoesNotExistException, ObjectAlreadyExistsException,
			ResourceCollectionDoesNotExistException, DataClayFederationException {
		try {
		if (type == null)
			throw new IllegalArgumentException("Argument 'type' is empty");
		if (id == null)
			throw new IllegalArgumentException("Argument 'id' is empty");
		if (data == null)
			throw new IllegalArgumentException("Argument 'data' is empty");

		Map<String, Object> objectData = new JSONObject(data).toMap();
		//Throws TypeDoesNotExistException, ObjectDoesNotExistException
		objectData = preProcessSubObjects(type, objectData);
		
		CIMIResource obj = null;
		switch (type) {
		//Throws ObjectAlreadyExistException, DataClayFederationException
		// mF2C resources
		case "agent":
			obj = new Agent(objectData);
			store(obj, type, id);
			String leaderAddr = (String) objectData.get("leaderIP");
			// Agent resource must be the first one created in the tests!!
			if (leaderAddr != null && !leaderAddr.isEmpty()) {
				connectToLeaderDataClay((String) objectData.get("leaderIP"));
			}
			break;
		case "agreement":
			obj = new Agreement(objectData);
			storeAndFederate(obj, type, id);
			break;
		case "device":
			obj = new Device(objectData);
			storeAndFederate(obj, type, id);
			break;
		case "device-dynamic":
			obj = new DeviceDynamic(objectData);
			storeAndFederate(obj, type, id);
			break;
		case "fog-area":
			obj = new FogArea(objectData);
			storeAndFederate(obj, type, id);
			break;
		case "service":
			obj = new Service(objectData);
			storeAndFederate(obj, type, id);
			break;
		case "service-instance":
			obj = new ServiceInstance(objectData);
			storeAndFederate(obj, type, id);
			break;
		case "sharing-model":
			obj = new SharingModel(objectData);
			storeAndFederate(obj, type, id);
			break;
		case "sla-violation":
			obj = new SlaViolation(objectData);
			storeAndFederate(obj, type, id);
			break;
		case "user-profile":
			obj = new UserProfile(objectData);
			storeAndFederate(obj, type, id);
			break;
		case "service-operation-report":
			obj = new ServiceOperationReport(objectData);
			storeAndFederate(obj, type, id);
			break;
		// CIMI resources
		case "cloud-entry-point":
			obj = new CloudEntryPoint(objectData);
			store(obj, type, id);
			break;
		case "email":
			obj = new Email(objectData);
			store(obj, type, id);
			break;
		case "user":
			obj = new User(objectData);
			store(obj, type, id);
			break;
		case "credential":
			obj = new Credential(objectData);
			store(obj, type, id);
			break;
		case "session":
			obj = new Session(objectData);
			store(obj, type, id);
			break;
		case "session-template":
			obj = new SessionTemplate(objectData);
			store(obj, type, id);
			break;
		case "callback":
			obj = new Callback(objectData);
			store(obj, type, id);
			break;
		case "event":
			obj = new Event(objectData);
			store(obj, type, id);
			break;
		/*
		 * case "user-template": obj = new UserTemplate(objectData); break; case
		 * "credential-template": obj = new CredentialTemplate(objectData); break; case
		 * "configuration": obj = new Configuration(objectData); break; case
		 * "configuration-template": obj = new ConfigurationTemplate(objectData); break;
		 * case "user-param": obj = new UserParam(objectData); break; case
		 * "user-param-template": obj = new UserParamTemplate(objectData); break; case
		 * "example-resource": obj = new ExampleResource(objectData); break;
		 */
		default:
			throw new TypeDoesNotExistException(type);
		}
		if (!type.equals("cloud-entry-point")) {
			ResourceCollection resources = null;
			final String className = javaize(type);
			try {
				resources = ResourceCollection
						.getByAlias(className + RESOURCE_COLLECTION_ALIAS_SUFFIX);
			} catch (final Exception e) {
				/*
				 * Version with ResourceCollections created on-the-fly, now they are all created
				 * in the init * resources = new ResourceCollection();
				 */
				throw new ResourceCollectionDoesNotExistException(type);
			}
			// this executes in current dataClay (maybe children)
			resources.put(id, obj);
		}
		} catch (Exception e) { 
			e.printStackTrace();
			throw e;
		}
	}

	private static void connectToLeaderDataClay(String leaderIP) throws DataClayFederationException {
		// Register the dataClay instance of my leader
		leaderDC = ClientManagementLib.registerExternalDataClay(leaderIP, LOGICMODULE_PORT);
		if (leaderDC == null) {
			throw new DataClayFederationException("Could not connect to the leader dataClay");
		} else {
			System.out.println("-- My dataClay leader is: " + leaderDC);
		}
	}

	private static void storeAndFederate(final CIMIResource obj, final String type, final String id) 
			throws ObjectAlreadyExistsException, DataClayFederationException {
		try {
			obj.makePersistent(type + id); // type+id is the alias
		} catch (Exception e) {
			throw new ObjectAlreadyExistsException(type, id);
		}
		if (leaderDC != null) {
			try {
				obj.federate(leaderDC, false); // Non-recursive because each object is individually federated
				// The whenFederated method in CIMIResource will be executed in the leader and
				// put obj in its corresponding resource collection
			} catch (Exception e) {
				throw new DataClayFederationException("Could not federate object '" + id + "' of type '" + type + "'");
			}
		}

	}
	
	private static void store(final CIMIResource obj, final String type, final String id) 
			throws ObjectAlreadyExistsException {
		try {
			obj.makePersistent(type + id); // type+id is the alias
		} catch (Exception e) {
			throw new ObjectAlreadyExistsException(type, id);
		}
	}
	/**
	 * Get a JSON representation of a dataClay object of type specified and
	 * identified by id provided
	 * 
	 * @param type
	 * 			Type of the object
	 * @param id
	 * 			Id of the object
	 * @return JSON representation of dataClay object
	 * @throws IllegalArgumentException
	 * 			if some argument is empty
	 * @throws TypeDoesNotExistException
	 * 			if argument 'type' is not a known resource type
	 * @throws ObjectDoesNotExistException
	 * 			if the object requested cannot be found
	 */
	public static String read(final String type, final String id) throws IllegalArgumentException, 
		TypeDoesNotExistException, ObjectDoesNotExistException {
		try {
			// returns json file with all the resource data
			if (type == null)
				throw new IllegalArgumentException("Argument 'type' is empty");
			if (id == null)
				throw new IllegalArgumentException("Argument 'id' is empty");
	
			//Throws TypeDoesNotExistException, ObjectDoesNotExistException
			final CIMIResource obj = getResourceAsObject(type, id);
			Map<String, Object> info = obj.getCIMIResourceData();
			info = postProcessSubObjects(type, info);
			final JSONObject json = new JSONObject(info);
			return json.toString();
		} catch (Exception e) { 
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Delete dataClay object of type provided and id specified.
	 * @param type
	 * 			Type of the object to delete
	 * @param id
	 * 			ID of the object
	 * @throws IllegalArgumentException
	 * 			if some argument is empty or type="agent"
	 * @throws TypeDoesNotExistException
	 * 			if argument 'type' is not a known resource type
	 * @throws ObjectDoesNotExistException
	 * 			if the object to be deleted cannot be found
	 * @throws ResourceCollectionDoesNotExistException
	 * 			if the resource collection for 'type' could not be found
	 */
	public static void delete(final String type, final String id) throws IllegalArgumentException, 
		TypeDoesNotExistException, ObjectDoesNotExistException, ResourceCollectionDoesNotExistException {
		if (type == null)
			throw new IllegalArgumentException("Argument 'type' is empty");
		if (id == null)
			throw new IllegalArgumentException("Argument 'id' is empty");
		if (type.equals("agent"))
			throw new IllegalArgumentException("Resources of type 'agent' cannot be deleted");

		try {
			switch (type) {
			case "agreement":
				Agreement.deleteAlias(type + id);
				break;
			case "device":
				Device.deleteAlias(type + id);
				break;
			case "device-dynamic":
				DeviceDynamic.deleteAlias(type + id);
				break;
			case "fog-area":
				FogArea.deleteAlias(type + id);
				break;
			case "service":
				Service.deleteAlias(type + id);
				break;
			case "service-instance":
				ServiceInstance.deleteAlias(type + id);
				break;
			case "sharing-model":
				SharingModel.deleteAlias(type + id);
				break;
			case "sla-violation":
				SlaViolation.deleteAlias(type + id);
				break;
			case "user-profile":
				UserProfile.deleteAlias(type + id);
				break;
			case "service-operation-report":
				ServiceOperationReport.deleteAlias(type + id);
				break;
			// CIMI resources
			case "email":
				Email.deleteAlias(type + id);
				break;
			case "user":
				User.deleteAlias(type + id);
				break;
			case "credential":
				Credential.deleteAlias(type + id);
				break;
			case "session":
				Session.deleteAlias(type + id);
				break;
			case "session-template":
				SessionTemplate.deleteAlias(type + id);
				break;
			case "callback":
				Callback.deleteAlias(type + id);
				break;
			case "event":
				Callback.deleteAlias(type + id);
				break;
			/*
			 * case "user-template": UserTemplate.deleteAlias(type+id); break; case
			 * "credential-template": CredentialTemplate.deleteAlias(type+id); break; case
			 * "configuration": Configuration.deleteAlias(type+id); break; case
			 * "configuration-template": ConfigurationTemplate.deleteAlias(type+id); break;
			 * case "user-param": UserParam.deleteAlias(type+id); break; case
			 * "user-param-template": UserParamTemplate.deleteAlias(type+id); break; case
			 * "example-resource": ExampleResource.deleteAlias(type+id); break;
			 */
			default:
				throw new TypeDoesNotExistException(type);
			}
		} catch (Exception e) {
			throw new ObjectDoesNotExistException(type, id);
		}
		final String colAlias = javaize(type) + RESOURCE_COLLECTION_ALIAS_SUFFIX;
		try {
			final ResourceCollection resources = ResourceCollection.getByAlias(colAlias);
			resources.delete(id);
		} catch (Exception e) {
			throw new ResourceCollectionDoesNotExistException(type);
		}
	}

	/**
	 * Update dataClay object of type specified and id provided with some JSON data
	 * 
	 * @param type
	 *          type of the object
	 * @param id
	 *          id of the object
	 * @param updatedData
	 *          JSON string representing values to update
	 * @throws IllegalArgumentException
	 *          if some argument is empty
	 * @throws TypeDoesNotExistException
	 * 			if argument 'type' is not a known resource type
	 * @throws ObjectDoesNotExistException
	 * 			if the object to be updated cannot be found
	 */
	public static void update(final String type, final String id, final String updatedData)
			throws IllegalArgumentException, TypeDoesNotExistException, ObjectDoesNotExistException {
		try {
			if (type == null)
				throw new IllegalArgumentException("Argument 'type' is empty");
			if (id == null)
				throw new IllegalArgumentException("Argument 'id' is empty");
			if (updatedData == null)
				throw new IllegalArgumentException("Argument 'data' is empty");
	
			final JSONObject json = new JSONObject(updatedData);
			Map<String, Object> objectData = json.toMap();
			//Throws TypeDoesNotExistException, ObjectDoesNotExistException
			objectData = preProcessSubObjects(type, objectData);
		
			switch (type) {
			case "agent":
				final Agent ag = Agent.getByAlias(type + id);
				ag.updateAllData(objectData);
				String updatedLeader = (String) objectData.get("leaderIP");
				if (updatedLeader !=null && !updatedLeader.isEmpty()) {
					//TODO whatever we need to do, i.e 
					//register new dC, federate all other resources?, unfederate previous??
					//TODO how/when to re-join and sync the data that has been updated while disconnected 
					//with the new leader?
				}
				break;
			case "agreement":
				final Agreement agr = Agreement.getByAlias(type + id);
				agr.updateAllData(objectData);
				break;
			case "device":
				final Device dev = Device.getByAlias(type + id);
				dev.updateAllData(objectData);
				break;
			case "device-dynamic":
				final DeviceDynamic dd = DeviceDynamic.getByAlias(type + id);
				dd.updateAllData(objectData);
				break;
			case "fog-area":
				final FogArea fa = FogArea.getByAlias(type + id);
				fa.updateAllData(objectData);
				break;
			case "service":
				final Service serv = Service.getByAlias(type + id);
				serv.updateAllData(objectData);
				break;
			case "service-instance":
				final ServiceInstance si = ServiceInstance.getByAlias(type + id);
				si.updateAllData(objectData);
				break;
			case "sharing-model":
				final SharingModel sm = SharingModel.getByAlias(type + id);
				sm.updateAllData(objectData);
				break;
			case "sla-violation":
				final SlaViolation sv = SlaViolation.getByAlias(type + id);
				sv.updateAllData(objectData);
				break;
			case "user-profile":
				final UserProfile up = UserProfile.getByAlias(type + id);
				up.updateAllData(objectData);
				break;
			case "service-operation-report":
				final ServiceOperationReport sor = ServiceOperationReport.getByAlias(type + id);
				sor.updateAllData(objectData);
				break;
			// CIMI resources
			case "cloud-entry-point":
				final CloudEntryPoint cep = CloudEntryPoint.getByAlias(type + id);
				cep.updateAllData(objectData);
				break;
			case "email":
				final Email em = Email.getByAlias(type + id);
				em.updateAllData(objectData);
				break;
			case "user":
				final User u = User.getByAlias(type + id);
				u.updateAllData(objectData);
				break;
			case "credential":
				final Credential cr = Credential.getByAlias(type + id);
				cr.updateAllData(objectData);
				break;
			case "session":
				final Session s = Session.getByAlias(type + id);
				s.updateAllData(objectData);
				break;
			case "session-template":
				final SessionTemplate st = SessionTemplate.getByAlias(type + id);
				st.updateAllData(objectData);
				break;
			case "callback":
				final Callback c = Callback.getByAlias(type + id);
				c.updateAllData(objectData);
				break;
			case "event":
				final Event e = Event.getByAlias(type + id);
				e.updateAllData(objectData);
				break;
			/*
			 * case "user-template": // final UserTemplate ut = (UserTemplate)
			 * UserTemplate.getByAlias(type+id); // ut.updateAllData(objectData); break;
			 * case "credential-template": // final CredentialTemplate ct =
			 * (CredentialTemplate) // CredentialTemplate.getByAlias(type+id); //
			 * ct.updateAllData(objectData); break; case "configuration": // final
			 * Configuration cf = (Configuration) Configuration.getByAlias(type+id); //
			 * cf.updateAllData(objectData); break; case "configuration-template": // final
			 * ConfigurationTemplate cft = (ConfigurationTemplate) //
			 * ConfigurationTemplate.getByAlias(type+id); // cft.updateAllData(objectData);
			 * break; case "user-param": // final UserParam upar = (UserParam)
			 * UserParam.getByAlias(type+id); // upar.updateAllData(objectData); break; case
			 * "user-param-template": // final UserParamTemplate upt = (UserParamTemplate)
			 * // UserParamTemplate.getByAlias(type+id); // upt.updateAllData(objectData);
			 * break; case "example-resource": // final ExampleResource er =
			 * (ExampleResource) // ExampleResource.getByAlias(type+id); //
			 * er.updateAllData(objectData); break;
			 */
			default:
				throw new TypeDoesNotExistException(type);
			}
		} catch (ObjectNotRegisteredException e) {
			throw new ObjectDoesNotExistException(type, id);
		} catch (Exception e) { 
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Get a list of JSON strings representing dataClay objects that match the query
	 * expression provided.
	 * 
	 * @param type
	 *            Resource type on which to execute the query
	 * @param expression
	 *            query to execute. If it is empty, all resources of the type for
	 *            which the user/role has access are returned
	 * @param user
	 *            user who executes the query. Can be empty.
	 * @param role
	 *            role of the user who executes the query. Simplification for IT-1:
	 *            a user has a single role. Can be empty.
	 * @return list of JSON strings representing dataClay objects that match the
	 *         query
	 * @throws ResourceCollectionDoesNotExistException
	 * 			if resource collection of 'type' is not found
	 */
	public static List<String> query(final String type, final String expression, final String user, 
			final String role) throws ResourceCollectionDoesNotExistException{
		try {
			final String aclCheck;
			final String expressionWithAcl;
			if (expression != null && !expression.isEmpty()) {
				final String exprWithoutFilter = expression.substring(9); // Remove the "[:Filter " at the beginning
				if (user != null && !user.isEmpty()) {
					if (role != null && !role.isEmpty()) {
						aclCheck = generateAclCheckComplete(user, role);
					} else {
						aclCheck = generateAclCheckSimple(user);
					}
					expressionWithAcl = aclCheck + " " + exprWithoutFilter + "]";
				} else {
					if (role != null && !role.isEmpty()) {
						aclCheck = generateAclCheckSimple(role);
						expressionWithAcl = aclCheck + " " + exprWithoutFilter + "]";
					} else {
						expressionWithAcl = expression;
					}
				}
			} else {
				if (user != null && !user.isEmpty()) {
					if (role != null && !role.isEmpty()) {
						aclCheck = generateAclCheckComplete(user, role);
					} else {
						aclCheck = generateAclCheckSimple(user);
					}
					expressionWithAcl = aclCheck + "]]";
				} else {
					if (role != null && !role.isEmpty()) {
						aclCheck = generateAclCheckSimple(role);
						expressionWithAcl = aclCheck + "]]";
					} else {
						expressionWithAcl = null;
					}
				}
			}
			final String aliasOfCollection = javaize(type) + RESOURCE_COLLECTION_ALIAS_SUFFIX;
			// System.out.println("Expression: " + expressionWithAcl);
			ResourceCollection collection = null;
			try {
				collection = ResourceCollection.getByAlias(aliasOfCollection);
			} catch (final ObjectNotRegisteredException e) {
				throw new ResourceCollectionDoesNotExistException(type);
			}
			final List<String> result = new ArrayList<>();
			if (expressionWithAcl != null) {
				final List<CIMIResource> resultSet = collection.filterResources(expressionWithAcl);
				for (final CIMIResource obj : resultSet) {
					Map<String, Object> info = obj.getCIMIResourceData();
					info = postProcessSubObjects(type, info);
					final JSONObject json = new JSONObject(info);
					result.add(json.toString());
				}
			} else {
				final Map<String, CIMIResource> resources = collection.getResources();
				for (final CIMIResource obj : resources.values()) {
					Map<String, Object> info = obj.getCIMIResourceData();
					info = postProcessSubObjects(type, info);
					final JSONObject json = new JSONObject(info);
					result.add(json.toString());
				}
			}
			return result;
		} catch (Exception e) { 
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Get a list of JSON strings representing dataClay objects that match the query
	 * expression provided.
	 * 
	 * @param type
	 *            Resource type on which to execute the query
	 * @param urlQuery URL query
	 * @return list of JSON strings representing dataClay objects that match the
	 *         query
	 * @throws ResourceCollectionDoesNotExistException
	 * 			if resource collection of 'type' is not found
	 */
	public static List<String> queryURL(final String type, final String urlQuery) 
			throws ResourceCollectionDoesNotExistException{
		try {
			final String aliasOfCollection = javaize(type) + RESOURCE_COLLECTION_ALIAS_SUFFIX;
			ResourceCollection collection = null;
			try {
				collection = ResourceCollection.getByAlias(aliasOfCollection);
			} catch (final ObjectNotRegisteredException e) {
				throw new ResourceCollectionDoesNotExistException(type);
			}
			final List<String> result = new ArrayList<>();
			final List<Object> queryResult = collection.filterStream(urlQuery);
			final List<CIMIResource> resultSet = new ArrayList<>();
			for (final Object curElement : queryResult) {
				resultSet.add((CIMIResource) curElement);
			}
			for (final CIMIResource obj : resultSet) {
				Map<String, Object> info = obj.getCIMIResourceData();
				info = postProcessSubObjects(type, info);
				final JSONObject json = new JSONObject(info);
				result.add(json.toString());
			}
			return result;
		} catch (Exception e) { 
			e.printStackTrace();
			throw e;
		}
	}

	private static String generateAclCheckComplete(final String user, final String role) {
		return "[:Filter [:AndExpr [:Comp [:Filter [:AndExpr [:Comp [:Attribute \"owner\"] [:EqOp \"=\"] "
				+ "[:SingleQuoteString \"'" + user + "'\"]]] [:Filter [:AndExpr [:Comp [:Attribute \"permissions\"] "
				+ "[:EqOp \"=\"] [:SingleQuoteString \"'" + user
				+ "'\"]]] [:Filter [:AndExpr [:Comp [:Attribute \"owner\"] " + "[:EqOp \"=\"] [:SingleQuoteString \"'"
				+ role + "'\"]]] [:Filter [:AndExpr [:Comp "
				+ "[:Attribute \"permissions\"] [:EqOp \"=\"] [:SingleQuoteString \"'" + role + "'\"]]]]]]]]";
	}

	private static String generateAclCheckSimple(final String userOrRole) {
		return "[:Filter [:AndExpr [:Comp [:Filter [:AndExpr [:Comp [:Attribute \"owner\"] [:EqOp \"=\"] "
				+ "[:SingleQuoteString \"'" + userOrRole
				+ "'\"]]] [:Filter [:AndExpr [:Comp [:Attribute \"permissions\"] "
				+ "[:EqOp \"=\"] [:SingleQuoteString \"'" + userOrRole + "'\"]]]]]]";
	}

	private static String javaize(final String type) {
		// First character to upper case
		String className = capitalize(type);
		int hyphen = className.indexOf("-");
		while (hyphen > 0) {
			// Separate first part, without the slash
			final String part = className.substring(0, hyphen);
			String nextPart = className.substring(hyphen + 1);
			// First character of second to upper case
			nextPart = capitalize(nextPart);
			className = part.concat(nextPart);
			hyphen = className.indexOf("-");
		}
		return className;
	}

	private static String capitalize(final String word) {
		return word.substring(0, 1).toUpperCase() + word.substring(1);
	}

	private static CIMIResource getResourceAsObject(final String type, final String id)
			throws TypeDoesNotExistException, ObjectDoesNotExistException {
		CIMIResource obj = null;
		try {
			switch (type) {
			// mF2C resources
			case "agent":
				obj = Agent.getByAlias(type + id);
				break;
			case "agreement":
				obj = Agreement.getByAlias(type + id);
				break;
			case "device":
				obj = Device.getByAlias(type + id);
				break;
			case "device-dynamic":
				obj = DeviceDynamic.getByAlias(type + id);
				break;
			case "fog-area":
				obj = FogArea.getByAlias(type + id);
				break;
			case "service":
				obj = Service.getByAlias(type + id);
				break;
			case "service-instance":
				obj = ServiceInstance.getByAlias(type + id);
				break;
			case "sharing-model":
				obj = SharingModel.getByAlias(type + id);
				break;
			case "sla-violation":
				obj = SlaViolation.getByAlias(type + id);
				break;
			case "user-profile":
				obj = UserProfile.getByAlias(type + id);
				break;
			case "service-operation-report":
				obj = ServiceOperationReport.getByAlias(type + id);
				break;
			// CIMI resources
			case "cloud-entry-point":
				obj = CloudEntryPoint.getByAlias(type + id);
				break;
			case "email":
				obj = Email.getByAlias(type + id);
				break;
			case "user":
				obj = User.getByAlias(type + id);
				break;
			case "credential":
				obj = Credential.getByAlias(type + id);
				break;
			case "session":
				obj = Session.getByAlias(type + id);
				break;
			case "session-template":
				obj = SessionTemplate.getByAlias(type + id);
				break;
			case "callback":
				obj = Callback.getByAlias(type + id);
				break;
			case "event":
				obj = Event.getByAlias(type + id);
				break;
			/*
			 * case "user-template": obj = (UserTemplate) UserTemplate.getByAlias(type+id);
			 * break; case "credential-template": obj = (CredentialTemplate)
			 * CredentialTemplate.getByAlias(type+id); break; case "configuration": obj =
			 * (Configuration) Configuration.getByAlias(type+id); break; case
			 * "configuration-template": obj = (ConfigurationTemplate)
			 * ConfigurationTemplate.getByAlias(type+id); break; case "user-param": obj =
			 * (UserParam) UserParam.getByAlias(type+id); break; case "user-param-template":
			 * obj = (UserParamTemplate) UserParamTemplate.getByAlias(type+id); break; case
			 * "example-resource": obj = (ExampleResource)
			 * ExampleResource.getByAlias(type+id); break;
			 */
			default:
				throw new TypeDoesNotExistException(type);
			}
			return obj;
		} catch (Exception e) {
			throw new ObjectDoesNotExistException(type, id);
		}
	}

	private static Map<String, Object> preProcessSubObjects(final String type, final Map<String, Object> objectData)
	throws TypeDoesNotExistException, ObjectDoesNotExistException {
		CIMIResource obj = null;
		CIMIResource obj2 = null;
		
		switch (type) {
		case "device-dynamic":
			Map<String, Object> link = (Map<String, Object>) objectData.get("device");
			String resourceId = (String) link.get("href");
			String subType = resourceId.substring(0, resourceId.indexOf("/"));
			String subId = resourceId.substring(resourceId.indexOf("/") + 1);
			if (resourceId != null) {
				//Throws TypeDoesNotExistException, ObjectDoesNotExistException
				obj = getResourceAsObject(subType, subId);
				objectData.put("device", obj);
			}
			link = (Map<String, Object>) objectData.get("myLeaderID");
			if (link != null) { // this field can be null if no leader is defined
				resourceId = (String) link.get("href");
				subType = resourceId.substring(0, resourceId.indexOf("/"));
				subId = resourceId.substring(resourceId.indexOf("/") + 1);
				if (resourceId != null) {
					//Throws TypeDoesNotExistException, ObjectDoesNotExistException
					obj2 = getResourceAsObject(subType, subId);
					objectData.put("myLeaderID", obj2);
				}
			}
			break;
		case "fog-area":
			link = (Map<String, Object>) objectData.get("leaderDevice");
			resourceId = (String) link.get("href");
			subType = resourceId.substring(0, resourceId.indexOf("/"));
			subId = resourceId.substring(resourceId.indexOf("/") + 1);
			if (resourceId != null) {
				//Throws TypeDoesNotExistException, ObjectDoesNotExistException
				obj = getResourceAsObject(subType, subId);
				objectData.put("leaderDevice", obj);
			}
			break;
		case "service-operation-report":
			link = (Map<String, Object>) objectData.get("serviceInstance");
			resourceId = (String) link.get("href");
			subType = resourceId.substring(0, resourceId.indexOf("/"));
			subId = resourceId.substring(resourceId.indexOf("/") + 1);
			if (resourceId != null) {
				//Throws TypeDoesNotExistException, ObjectDoesNotExistException
				obj = getResourceAsObject(subType, subId);
				objectData.put("serviceInstance", obj);
			}
			break;
		default:
			break;
		}
		return objectData;
	}

	private static Map<String, Object> postProcessSubObjects(final String type, final Map<String, Object> objectData) {
		CIMIResource obj;
		switch (type) {
		case "device-dynamic":
			obj = (CIMIResource) objectData.get("device");
			if (obj != null) {
				final Map<String, String> link = new HashMap<>();
				link.put("href", obj.get_id());
				objectData.put("device", link);
			}
			obj = (CIMIResource) objectData.get("myLeaderID");
			if (obj != null) {
				final Map<String, String> link = new HashMap<>();
				link.put("href", obj.get_id());
				objectData.put("myLeaderID", link);
			}
			break;
		case "fog-area":
			obj = (CIMIResource) objectData.get("leaderDevice");
			if (obj != null) {
				final Map<String, String> link = new HashMap<>();
				link.put("href", obj.get_id());
				objectData.put("leaderDevice", link);
			}
			break;
		case "service-operation-report":
			obj = (CIMIResource) objectData.get("serviceInstance");
			if (obj != null) {
				final Map<String, String> link = new HashMap<>();
				link.put("href", obj.get_id());
				objectData.put("serviceInstance", link);
			}
			break;
		default:
			break;
		}
		return objectData;

	}

}
