package api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import CIMI.UnfederateNotificationChildren;
import CIMI.UnfederateNotificationLeader;
import api.exceptions.DataClayFederationException;
import api.exceptions.ObjectAlreadyExistsException;
import api.exceptions.ObjectDoesNotExistException;
import api.exceptions.ResourceCollectionDoesNotExistException;
import api.exceptions.TypeDoesNotExistException;
import dataclay.api.DataClay;
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

	/** All classes in Resource model. */
	private static Set<Class<? extends CIMIResource>> allClasses;

	/** Agent resource alias. */
	private static final String AGENT_RESOURCE_ALIAS = "agent/agent";
	
	/** Allowed rights for queries. */
	private static final List<String> QUERY_RIGHTS = Arrays.asList(
			new String[] {"READ", "MODIFY", "ALL"});

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

		// get my IP 
		System.out.println("-- My ID is " + ClientManagementLib.getDataClayID());

		// check that there is an agent with leader defined 
		try {
			final Agent curAgent = Agent.getByAlias(AGENT_RESOURCE_ALIAS);
			leaderAddr = (String) curAgent.getFieldValue("leader_ip");
			if (leaderAddr != null && !leaderAddr.isEmpty()) {
				leaderDC = connectToExternalDataClay(leaderAddr); 
				System.out.println("-- My dataClay leader is: " + leaderDC + " at " + leaderAddr);
			}
			final String backupAddr = (String) curAgent.getFieldValue("backup_ip");
			if (backupAddr != null && !backupAddr.isEmpty()) {
				backupDC = connectToExternalDataClay(backupAddr); 
				System.out.println("-- My dataClay backup is: " + backupDC + " at " + backupAddr);
			}

			// get my ip and publish it again because of a possible restart of DC?
			final String ip = (String) curAgent.getFieldValue("device_ip");
			System.out.println("-- My dataClay IP is: " + ip);
			if (ip != null && !ip.isEmpty()) {
				publishMyIP(ip);
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
		allClasses = reflections.getSubTypesOf(CIMIResource.class);
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

				// Check
				checkAgentIPs(objectData);

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
				obj = new Agent(objectData);
				obj.makePersistent(AGENT_RESOURCE_ALIAS); // type+id is the alias

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

	private static DataClayInstanceID getExternalDataClayID(final String theip) throws DataClayFederationException {

		String ip = theip;
		int port = LOGICMODULE_PORT;
		if (theip.contains(":")) { 
			final String[] addr = theip.split(":");
			ip = addr[0];
			port = Integer.valueOf(addr[1]);
		} 
		final DataClayInstanceID id = ClientManagementLib.getExternalDataClayID(ip, port);
		if (id == null) {
			throw new DataClayFederationException("Could not connect to the external dataClay with IP " + theip);
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

	/**
	 * Store and federate object
	 * @param obj Object to store and federate
	 * @param type Type of the object
	 * @param id ID of the obejct
	 * @throws ObjectAlreadyExistsException indicates object already exists
	 * @throws DataClayFederationException indicates object could not be federated
	 */
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
	 * Check all not null IPs provided are well-formed 
	 * @param objectData JSON of the Agent resource to check
	 */
	@SuppressWarnings("unchecked")
	private static void checkAgentIPs(final Map<String, Object> objectData) { 
		final String updatedIP = (String) objectData.get("device_ip");
		final String updatedLeaderIP = (String) objectData.get("leader_ip");
		final String updatedBackupIP = (String) objectData.get("backup_ip");
		final ArrayList<String> updatedChildrenIPs = (ArrayList<String>) objectData.get("childrenIPs");

		if (!validateIPAddress(updatedIP)) { 
			throw new RuntimeException("Wrongly defined device ip :" + updatedIP);
		}
		if (!validateIPAddress(updatedLeaderIP)) { 
			throw new RuntimeException("Wrongly defined leader ip :" + updatedLeaderIP);
		}
		if (!validateIPAddress(updatedBackupIP)) { 
			throw new RuntimeException("Wrongly defined backup ip :" + updatedBackupIP);
		}
		if (updatedChildrenIPs != null && !updatedChildrenIPs.isEmpty()) { 
			for (final String child : updatedChildrenIPs) { 
				if (!validateIPAddress(child)) { 
					throw new RuntimeException("Wrongly defined or empty child ip :" + child);
				}
			}	
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
		CIMIResource obj = null;
		if (type.equals("agent")) {
			obj = Agent.getByAlias(AGENT_RESOURCE_ALIAS);
		} else {
			obj = getResource(type, id);			
		}
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
		// ===== REMOVE FROM COLLECTION ===== 
		try {
			final String colAlias = javaize(type) + RESOURCE_COLLECTION_ALIAS_SUFFIX;
			final ResourceCollection resources = ResourceCollection.getByAlias(colAlias);
			resources.delete(id);
		} catch (final Exception e) {
			throw new ResourceCollectionDoesNotExistException(type);
		}

		// ===== UNFEDERATE OBJECT IF BELONGS TO CURRENT DATACLAY ===== 
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
	 * @return JSON representation of dataClay object modified
	 * @throws Exception if federation during update of leader failed.
	 */
	@SuppressWarnings("unchecked")
	public static String update(final String type, final String id, final String updatedData)
			throws Exception {
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
				// current ips
				final String currentIP =  (String) ag.getFieldValue("device_ip");
				String currentLeaderIP = ag.getLeaderIP();
				if (currentLeaderIP == null) { 
					currentLeaderIP = ""; //for easy checks
				}
				String currentBackupIP = (String) ag.getFieldValue("backup_ip");
				if (currentBackupIP == null) { 
					currentBackupIP = ""; //for easy checks
				}
				ArrayList<String> currentChildren = (ArrayList<String>) ag.getFieldValue("childrenIPs");
				if (currentChildren == null)  {
					currentChildren = new ArrayList<>();
				}
				// update ips 
				final String updatedIP = (String) objectData.get("device_ip");
				String updatedLeaderIP = (String) objectData.get("leader_ip");
				if (updatedLeaderIP == null) { 
					updatedLeaderIP = ""; //for easy checks
				}
				String updatedBackupIP = (String) objectData.get("backup_ip");
				if (updatedBackupIP == null) { 
					updatedBackupIP = ""; //for easy checks
				}
				ArrayList<String> updatedChildrenIPs = (ArrayList<String>) objectData.get("childrenIPs");
				if (updatedChildrenIPs == null)  {
					updatedChildrenIPs = new ArrayList<>();
				}
				// check new ips
				checkAgentIPs(objectData);
				/** Use cases
					 1 - Leader changed.
					 		Unfederate all objects with ANY dataClay and federate all of them to new leader.
					 		Corner cases:

					 			- Objects already unfederated in leader side: ok - continue, this could happen 
					 			if current agent lost internet connection and leader got notification to remove a child.

					 			- Objects already federated in leader side: ok - continue, this can happen if 
					 			current agent lost connection and then connected to same leader before the leader gets
					 			lost child notification. !!! - discovery should watch that no changed leader notification happen
					 			before leader was notified it lost a child. !!!!

					 2 - Lost children 
							Unfederate all objects with lost children and create a notification to leader to 
							make them unfederate it also. 
							Corner cases: 

								- Objects already unfederated. This could happen in normal exit of children. Continue. 

					3 - New backup 
							Unfederate all my objects with previous backup and federate them with new backup
				 **/ 		

				// UPDATE IP
				if (currentIP != null && !currentIP.isEmpty() && !currentIP.equals(updatedIP)) {
					publishMyIP(updatedIP);
					System.out.println("-- Updated IP is: " + updatedIP);
				}

				// UPDATE LEADER

				// FEDERATE, MIGRATE, UNFEDERATE, PROPAGATE USE CASES
				final DataClayInstanceID currentLeaderID = leaderDC;
				if (updatedLeaderIP.isEmpty() && leaderDC != null) {
					System.out.println("-- New Leader is undefined, unfederating all objects with previous leader");
					DataClay.unfederateAllObjects(leaderDC);
					leaderDC = null;
					

				} else if (!currentLeaderIP.equals(updatedLeaderIP)) { 
					leaderDC = connectToExternalDataClay(updatedLeaderIP);
					System.out.println("-- Updated dataClay leader is: " + leaderDC + " at " + updatedLeaderIP);

					// if there is previous leader, migrate
					if (!currentLeaderIP.isEmpty()) {
						System.out.println("-- Migrating fed.objects from previous leader dataClay " + currentLeaderID
								+ " to " + leaderDC);
						DataClay.migrateFederatedObjects(currentLeaderID, leaderDC);
					} else { 
						// -- previous leader is null or empty 
						// federate current objects to new leader
						System.out.println("-- Federating all objects to new leader " + leaderDC);
						DataClay.federateAllObjects(leaderDC);
					}
					
				}
				
				if (!currentLeaderIP.equals(updatedLeaderIP)
						&& (currentLeaderID != null || leaderDC != null)) { 
					// NOTIFY UNFEDERATION TO CHILDREN before update of children fields
					// lost or new added children do not need to be notified from changing its grandleader

					ArrayList<String> childrenToNotify = currentChildren;
					if (updatedChildrenIPs != null) { 
						childrenToNotify = updatedChildrenIPs; // if we are also updating children, only notify them
					}
					if (childrenToNotify != null && !childrenToNotify.isEmpty()) { 
						System.out.println("---- [New GrandLeader Notification] Notifying children ");
						for (final String child : childrenToNotify) { 
							if (child != null) { 
								final DataClayInstanceID extDataClayID = getExternalDataClayID(child);
								final Map<String, Object> notificationData = new HashMap<>();
								if (currentLeaderID != null) {
									String ip = currentLeaderIP;
									int port = LOGICMODULE_PORT;
									if (currentLeaderIP.contains(":")) { 
										final String[] addr = currentLeaderIP.split(":");
										ip = addr[0];
										port = Integer.valueOf(addr[1]);
									} 
									notificationData.put("previousIP", ip);
									notificationData.put("previousPort", port);
								}
								if (leaderDC != null) {
									String ip = updatedLeaderIP;
									int port = LOGICMODULE_PORT;
									if (updatedLeaderIP.contains(":")) { 
										final String[] addr = updatedLeaderIP.split(":");
										ip = addr[0];
										port = Integer.valueOf(addr[1]);
									} 
									notificationData.put("newIP", ip);
									notificationData.put("newPort", port);
								}
								System.out.println("---- [New GrandLeader Notification] Notify child about grandleader: " + child 
										+ " from " + currentLeaderID + " to " + leaderDC);

								final UnfederateNotificationChildren notification = new UnfederateNotificationChildren(notificationData);
								notification.makePersistent();
								notification.federate(extDataClayID);
								notification.unfederateWithAllDCs(false);
								notification.disableNotification();

							}
						}
					}


				}
				// UPDATE BACKUP
				// FEDERATE, MIGRATE, UNFEDERATE (NO PROPAGATION FOR BACKUP) USE CASES
				final DataClayInstanceID currentBackupID = backupDC;
				if (updatedBackupIP.isEmpty() && backupDC != null) {
					System.out.println("-- New Backup is undefined, unfederating all objects with previous backup");
					DataClay.unfederateAllObjects(backupDC);
					backupDC = null;

				} else if (!currentBackupIP.equals(updatedBackupIP)) { 
					backupDC = connectToExternalDataClay(updatedBackupIP);
					System.out.println("-- Updated dataClay backup is: " + backupDC + " at " + updatedBackupIP);
					if (!currentBackupIP.isEmpty()) {
						System.out.println("-- Migrating fed.objects from previous backup dataClay " + currentBackupID
								+ " to " + backupDC);
						DataClay.migrateFederatedObjects(currentBackupID, backupDC);
					} else { 
						// -- previous backup is null or empty 
						// federate current objects to new backup
						System.out.println("-- Federating all objects to new backup " + backupDC);
						DataClay.federateAllObjects(backupDC);
					}

				}
				// UPDATE CHILDREN
				// UNFEDERATE, PROPAGATE USE CASES
				final ArrayList<DataClayInstanceID> newChildrenIDs = new ArrayList<>();
				for (final String updatedChild : updatedChildrenIPs) { 
					final DataClayInstanceID extDataClayID = getExternalDataClayID(updatedChild);
					newChildrenIDs.add(extDataClayID);
				}
				final ArrayList<DataClayInstanceID> lostChildren = new ArrayList<>();
				for (final String child : currentChildren) { 
					final DataClayInstanceID prevChildID = getExternalDataClayID(child);
					if (!newChildrenIDs.contains(prevChildID)) { 
						System.out.println("---- [Lost Children] Found lost child " + prevChildID);
						lostChildren.add(prevChildID);

					} 
				}										
				for (final DataClayInstanceID lostChild : lostChildren) {
					if (lostChild != null) { 						
						// 1 - unfederate all objects with lost children 						
						System.out.println("---- [Lost Children] Unfederating all objects with lost child " + lostChild);
						DataClay.unfederateAllObjects(lostChild);
						// 2 - notify leader
						if (leaderDC != null)  {
							System.out.println("---- [Lost Children] Notifying grandleaders ");
							final Map<String, Object> notificationData = new HashMap<>();
							notificationData.put("previousID", lostChild.toString());
							final UnfederateNotificationLeader notification = new UnfederateNotificationLeader(notificationData);
							notification.makePersistent();
							notification.federate(leaderDC);
							notification.unfederateWithAllDCs(false);
							notification.disableNotification();

						}
					}
				}


				// now update current agent
				ag.updateAllData(objectData);

			} else { 
				final CIMIResource resource = getResource(type, id);
				resource.updateAllData(objectData);
			}

			return read(type, id);
		} catch (final ObjectNotRegisteredException e) {
			e.printStackTrace();
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
	 * @param jsonRoles
	 *            roles of the user who executes the query. 
	 * @return list of JSON strings representing dataClay objects that match the
	 *         query
	 * @throws ResourceCollectionDoesNotExistException
	 * 			if resource collection of 'type' is not found
	 */
	@SuppressWarnings("unchecked")
	public static List<String> query(final String type, final String expression, final String user, 
			final String jsonRoles) throws ResourceCollectionDoesNotExistException{
		try {
			
			final String aliasOfCollection = javaize(type) + RESOURCE_COLLECTION_ALIAS_SUFFIX;
			System.out.println("[Query] Collection alias: " + aliasOfCollection);
			System.out.println("[Query] Expression: " + expression);
			System.out.println("[Query] User: " + user);
			
			// Parse roles string ["ADMIN" "ANON"] 
			System.out.println("[Query] Received roles: " + jsonRoles);
			/*String[] roles = null;
			if (jsonRoles != null && jsonRoles.startsWith("#")) {
				final String cleanRoles = jsonRoles.replace("#", "").replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\"", "");
				System.out.println("[Query] Processing roles: " + cleanRoles);
				roles = cleanRoles.split(" ");
				System.out.println("[Query] Roles: " + Arrays.toString(roles));
			} else if (jsonRoles != null && jsonRoles.startsWith("[")) {
				final String cleanRoles = jsonRoles.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
				System.out.println("[Query] Processing roles: " + cleanRoles);
				roles = cleanRoles.split(" ");
				System.out.println("[Query] Roles: " + Arrays.toString(roles));
			}*/
			ResourceCollection collection = null;
			try { 
				collection = ResourceCollection.getByAlias(aliasOfCollection);
			} catch (final ObjectNotRegisteredException e) {
				throw new ResourceCollectionDoesNotExistException(type);
			}
			final List<String> result = new ArrayList<>();
			if (expression != null) {
				final List<CIMIResource> resultSet = collection.filterResources(expression);
				for (final CIMIResource obj : resultSet) {
					
										
					final Map<String, Object> info = obj.getCIMIResourceData();
					final Map<String, Object> acl = (Map<String, Object>) info.get("acl");
					//if (checkQueryRights(acl, user, roles)) { 
						final JSONObject json = new JSONObject(info);
						result.add(json.toString());
					//}
				}
			} else {
				final Map<String, CIMIResource> resources = collection.getResources();
				for (final CIMIResource obj : resources.values()) {
					final Map<String, Object> info = obj.getCIMIResourceData();
					final Map<String, Object> acl = (Map<String, Object>) info.get("acl");
					//if (checkQueryRights(acl, user, roles)) { 
						final JSONObject json = new JSONObject(info);
						result.add(json.toString());
					//}
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
	
	/**
	 * Check if for current object ACL and query user and roles the object is accessible
	 * @param acl ACL of the object
	 * @param user User querying the object (can be null) 
	 * @param roles Roles querying the object (can be null)
	 * @return TRUE if the object is accessible for user and roles provided.
	 */
	private static boolean checkQueryRights(final Map<String, Object> acl, final String user, 
			final String[] roles) { 
		if (user != null) {
			// if owner is the user or there is some rule allowing the user, return true
			if (checkRights(acl, user, QUERY_RIGHTS, "USER")) { 
				return true;
			}
		} else { 
			System.out.println("[ACL] ## Not checking user. User is null. ## ");
		}
		if (roles != null) {
			System.out.println("[ACL] User not allowed or not provided. Checking roles... ");
			// if owner is the role or there is some rule allowing the role, return true
			for (final String role : roles) {
				if (checkRights(acl, role, QUERY_RIGHTS, "ROLE")) { 
					return true;
				}
			}
		}  else { 
			System.out.println("[ACL] ## Not checking roles. Roles are null. ## ");
		}
		return false;
	}
	
	/**
	 * Check if the USER or ROLE and rights provided (read, modify...) are allowed in the specified ACL 
	 * @param acl ACL to check
	 * @param userOrRole User or role to verify
	 * @param rights Rights of the user/role (future work: CURRENTLY NOT CHECKED)
	 * @param type can be USER or ROLE
	 * @return TRUE if if the user and rights provided (read, modify...) are allowed in the specified ACL 
	 */
	@SuppressWarnings("unchecked")
	private static boolean checkRights(final Map<String, Object> acl, final String userOrRole, 
			final List<String> rights, final String type) { 
	
		System.out.println("[ACL] ## Checking " + userOrRole + " is an allowed " + type + " ## ");
		// 1 - Check the owner matches provided user/role 
		final Map<String, Object> owner = (Map<String, Object>) acl.get("owner");
		if (owner != null) {
			System.out.println("[ACL] Checking owner " + owner);
			final String ownerType = (String) owner.get("type");
			if (ownerType.equals(type)) { 
				final String ownerUserRole = (String) owner.get("principal");
				System.out.println("[ACL] Object's owner has " + ownerType + " type and name " + ownerUserRole);
				if (ownerUserRole.equals(userOrRole)) { 
					System.out.println("[ACL] ## Owner user or role " + userOrRole + " ALLOWED ##");
					return true;
				}
			} 
		}
		
		// 2 - Check if some rule allows the user/role with rights provided 
		final List<Map<String, Object>> rules = (List<Map<String, Object>>) acl.get("rules");
		if (rules != null) {
			for (final Map<String, Object> currentRule : rules) { 
				System.out.println("[ACL] Checking rule " + currentRule);
				final String ruleType = (String) currentRule.get("type");
				if (ruleType.equals(type)) { 
					final String name = (String) currentRule.get("principal");
					System.out.println("[ACL] Object's rule has " + ruleType + " type and name " + name);
					//String right = (String) currentRule.get("right");
					if (name.equals(userOrRole)) { 
						System.out.println("[ACL] ## Rule with user or role " + userOrRole + " ALLOWED ##");
						return true;
						
					}
				}
			}
		}
		System.out.println("[ACL] ## " + userOrRole + " NOT allowed to read object ##");
		return false;
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

	private static boolean validateIPAddress(final String theip) {
		try {
			if (theip == null || theip.isEmpty()) { 
				return true;
			}
			String ip = theip;
			if (theip.contains(":")) { 
				final String[] addr = theip.split(":");
				ip = addr[0];
			} 

			final String[] parts = ip.split( "\\." );
			if ( parts.length != 4 ) {
				return false;
			}

			for ( final String s : parts ) {
				final int i = Integer.parseInt( s );
				if ( (i < 0) || (i > 255) ) {
					return false;
				}
			}
			if ( ip.endsWith(".") ) {
				return false;
			}

			return true;
		} catch (final NumberFormatException nfe) {
			return false;
		}
	}
}
