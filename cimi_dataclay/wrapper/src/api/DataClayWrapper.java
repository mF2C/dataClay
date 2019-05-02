package api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.reflections.Reflections;

import CIMI.Agent;
import CIMI.CIMIResource;
import CIMI.CloudEntryPoint;
import CIMI.ReplicateInLeader;
import CIMI.ResourceCollection;
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

	/** Backup DataClay ID, if present. */
	public static DataClayInstanceID backupDC = null;

	/** Suffix for aliases of Resource collections. */
	private static final String RESOURCE_COLLECTION_ALIAS_SUFFIX = "Collection";

	/** LogicModule port. */
	private static final int LOGICMODULE_PORT = 1034;

	/** All instances of following types must be federated to leader. */
	private static Set<Class<?>> typesToFederate;
	
	/** Agent resource alias. */
	private static final String AGENT_RESOURCE_ALIAS = "agent/agent";

	/**
	 * Simulates a discovery process if LEADER_DC=host:port environment variable is set. 
	 * In any case, it creates the required resource collections in the agent.
	 * 
	 * @throws DataClayFederationException
	 *             Error while connecting to the dataClay in the leader
	 */
	public static void init() throws DataClayFederationException {
		String leaderAddr = System.getenv("LEADER_DC");
		if (leaderAddr != null && !leaderAddr.isEmpty()) {
			// No discovery in this case, environment variable indicates leader for testing
			leaderDC = connectToExternalDataClay(leaderAddr);
			System.out.println("-- My dataClay leader is: " + leaderDC);
		}
		// check that there is an agent with leader defined 
		try {
			final Agent curAgent = Agent.getByAlias(AGENT_RESOURCE_ALIAS);
			leaderAddr = (String) curAgent.getFieldValue("leader_ip");
			if (leaderAddr != null && !leaderAddr.isEmpty()) {
				leaderDC = connectToExternalDataClay(leaderAddr); 
				System.out.println("-- My dataClay leader is: " + leaderDC);
			}
			final String backupAddr = (String) curAgent.getFieldValue("backup_ip");
			if (backupAddr != null && !backupAddr.isEmpty()) {
				backupDC = connectToExternalDataClay(backupAddr); 
				System.out.println("-- My dataClay backup is: " + backupDC);
			}
		} catch (final Exception e) { 
			//ignore, agent not defined yet
		}
		createLocalResourceCollections();
	}

	/**
	 * Create a Resource collection per type in current dataClay
	 */
	private static void createLocalResourceCollections() {

		// get all types in CIMI package
		final Reflections reflections = new Reflections("CIMI");
		final Set<Class<? extends CIMIResource>> allClasses = 
				reflections.getSubTypesOf(CIMIResource.class);
		typesToFederate = reflections.getTypesAnnotatedWith(ReplicateInLeader.class);

		System.out.println("-- Found subtypes: " + allClasses.size());
		System.out.println("-- Types to federate : " + typesToFederate);

		for (final Class<?> type : allClasses) {
			if (!type.equals(CloudEntryPoint.class) && !type.isAnnotation()) {
				final String className = type.getSimpleName();
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

			final Map<String, Object> objectData = new JSONObject(data).toMap();
			CIMIResource obj;
			Class<?> resourceType = null;
			if (type.equals("agent")) {
				obj = new Agent(objectData);
				store(obj, type, id);

				// Check current IP 
				final String currentDataClayIP = (String) objectData.get("device_ip");
				if (currentDataClayIP != null && !currentDataClayIP.isEmpty()) {
					publishMyIP(currentDataClayIP);
					System.out.println("-- My IP is: " + currentDataClayIP);
				}
				final String leaderAddr = (String) objectData.get("leader_ip");
				// Agent resource must be the first one created in the tests!!
				if (leaderAddr != null && !leaderAddr.isEmpty()) {
					leaderDC = connectToExternalDataClay(leaderAddr);
					System.out.println("-- My dataClay leader is: " + leaderDC);
				}
				final String backupAddr = (String) objectData.get("backup_ip");
				// Agent resource must be the first one created in the tests!!
				if (backupAddr != null && !backupAddr.isEmpty()) {
					backupDC = connectToExternalDataClay(backupAddr);
					System.out.println("-- My dataClay backup is: " + backupDC);
				}
				

			} else { 
				try { 
					final String className = "CIMI." + javaize(type);
					resourceType = Class.forName(className);
					obj = (CIMIResource) resourceType.getConstructor(Map.class).newInstance(objectData);

					if (typesToFederate.contains(resourceType)) { 
						System.out.println("Storing and federating " + type + "/" + id);
						storeAndFederate(obj, type, id);
					} else { 
						store(obj, type, id);
					}

				} catch (final Exception e) {
					e.printStackTrace();
					throw new TypeDoesNotExistException(type);
				}
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
					throw new ResourceCollectionDoesNotExistException(className + RESOURCE_COLLECTION_ALIAS_SUFFIX);
				}
				// this executes in current dataClay (maybe children)
				resources.put(id, obj);
			}
		} catch (final Exception e) { 
			e.printStackTrace();
			throw e;
		}
	}

	private static DataClayInstanceID connectToExternalDataClay(final String theip) throws DataClayFederationException {
		// Register the dataClay instance of my leader
		String ip = theip;
		int port = LOGICMODULE_PORT;
		if (theip.contains(":")) { 
			final String[] addr = theip.split(":");
			ip = addr[0];
			port = Integer.valueOf(addr[1]);
		} 
		final DataClayInstanceID id = ClientManagementLib.registerExternalDataClay(ip, port);
		if (id == null) {
			throw new DataClayFederationException("Could not connect to the leader dataClay");
		}
		return id;
	}

	/**
	 * Configure dataclay to give provided address in case external dataClays require to know how
	 * to access current dataClay
	 * @param hostname Hostname to be published (given to external dataClays)
	 * @param port Port to be published
	 */
	private static void publishMyIP(final String theip) throws DataClayFederationException {
		// Register the dataClay instance of my leader
		String ip = theip;
		int port = LOGICMODULE_PORT;
		if (theip.contains(":")) { 
			final String[] addr = theip.split(":");
			ip = addr[0];
			port = Integer.valueOf(addr[1]);
		} 
		ClientManagementLib.publishAddress(ip, port);
	}

	private static void storeAndFederate(final CIMIResource obj, final String type, final String id) 
			throws ObjectAlreadyExistsException, DataClayFederationException {
		store(obj, type, id); 
		System.out.println("Federating " + type + "/" + id);
		if (leaderDC != null) {
			try {
				obj.federate(leaderDC, false); // Non-recursive because each object is individually federated
				// The whenFederated method in CIMIResource will be executed in the leader and
				// put obj in its corresponding resource collection
			} catch (final Exception e) {
				throw new DataClayFederationException("Could not federate object '" + id + "' of type '" + type + "'");
			}
		}

		if (backupDC != null) {
			try {
				obj.federate(backupDC, false);
			} catch (final Exception e) {
				throw new DataClayFederationException("Could not federate object '" + id + "' of type '" + type + "'");
			}
		}

	}

	private static void store(final CIMIResource obj, final String type, final String id) 
			throws ObjectAlreadyExistsException {
		try {
			final String alias = type + "/" + id;
			System.out.println("Storing " + alias);
			obj.makePersistent(alias); // type+id is the alias
		} catch (final Exception e) {
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
		if (type == null)
			throw new IllegalArgumentException("Argument 'type' is empty");
		if (id == null)
			throw new IllegalArgumentException("Argument 'id' is empty");

		final CIMIResource obj = getResource(type, id);			
		final Map<String, Object> info = obj.getCIMIResourceData();
		final JSONObject json = new JSONObject(info);
		return json.toString();
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

			final String alias = type + "/" + id;
			final Class<?> resourceType = Class.forName("CIMI." + javaize(type));
			resourceType.getDeclaredMethod("deleteAlias", new Class<?>[] {String.class})
			.invoke(null, new Object[] {alias});
		} catch (final ClassNotFoundException e1) {
			e1.printStackTrace();
			throw new TypeDoesNotExistException(type);			
		} catch (final Exception e1) {
			e1.printStackTrace();
			throw new ObjectDoesNotExistException(type, id);
		}

		try {
			final String colAlias = javaize(type) + RESOURCE_COLLECTION_ALIAS_SUFFIX;
			final ResourceCollection resources = ResourceCollection.getByAlias(colAlias);
			resources.delete(id);
		} catch (final Exception e) {
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
	 * @return JSON representation of dataClay object modified
	 */
	public static String update(final String type, final String id, final String updatedData)
			throws IllegalArgumentException, TypeDoesNotExistException, ObjectDoesNotExistException {

		try {
			if (type == null)
				throw new IllegalArgumentException("Argument 'type' is empty");
			if (id == null)
				throw new IllegalArgumentException("Argument 'id' is empty");
			if (updatedData == null)
				throw new IllegalArgumentException("Argument 'data' is empty");
			final Map<String, Object> objectData = new JSONObject(updatedData).toMap();
			if (type.equals("agent")) {
				final Agent ag = Agent.getByAlias(AGENT_RESOURCE_ALIAS);
				ag.updateAllData(objectData);
				final String updatedLeader = (String) objectData.get("leaderIP");
				if (updatedLeader !=null && !updatedLeader.isEmpty()) {
					//TODO whatever we need to do, i.e 
					//register new dC, federate all other resources?, unfederate previous??
					//TODO how/when to re-join and sync the data that has been updated while disconnected 
					//with the new leader?
				}
			} else { 
				final CIMIResource resource = getResource(type, id);
				resource.updateAllData(objectData);
			}

			return read(type, id);
		} catch (final ObjectNotRegisteredException e) {
			throw new ObjectDoesNotExistException(type, id);
		} catch (final Exception e) { 
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Get resource
	 * @param type Type of resource
	 * @param id ID of resource
	 * @return CIMI resource with alias type/id 
	 * @throws TypeDoesNotExistException if type does not exist
	 * @throws ObjectDoesNotExistException if resource object does not exist
	 */
	private static CIMIResource getResource(final String type, final String id) throws TypeDoesNotExistException, ObjectDoesNotExistException { 
		try {
			final String alias = type + "/" + id;
			final Class<?> resourceType = Class.forName("CIMI." + javaize(type));
			return (CIMIResource) resourceType.getDeclaredMethod("getByAlias", new Class<?>[] {String.class})
					.invoke(null, new Object[] {alias});
		} catch (final ClassNotFoundException e1) {
			e1.printStackTrace();
			throw new TypeDoesNotExistException(type);			
		} catch (final Exception e1) {
			e1.printStackTrace();
			throw new ObjectDoesNotExistException(type, id);
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
			System.out.println("[Query] Collection alias: " + aliasOfCollection);
			System.out.println("[Query] Expression: " + expressionWithAcl);
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
					final Map<String, Object> info = obj.getCIMIResourceData();
					final JSONObject json = new JSONObject(info);
					result.add(json.toString());
				}
			} else {
				final Map<String, CIMIResource> resources = collection.getResources();
				for (final CIMIResource obj : resources.values()) {
					final Map<String, Object> info = obj.getCIMIResourceData();
					final JSONObject json = new JSONObject(info);
					result.add(json.toString());
				}
			}
			return result;
		} catch (final Exception e) { 
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
				final Map<String, Object> info = obj.getCIMIResourceData();
				final JSONObject json = new JSONObject(info);
				result.add(json.toString());
			}
			return result;
		} catch (final Exception e) { 
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

}
