package api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import CIMI.Agreement;
import CIMI.CIMIResource;
import CIMI.Callback;
import CIMI.CloudEntryPoint;
import CIMI.Credential;
import CIMI.Device;
import CIMI.DeviceDynamic;
import CIMI.Email;
import CIMI.FogArea;
import CIMI.ResourceCollection;
import CIMI.Service;
import CIMI.ServiceInstance;
import CIMI.ServiceOperationReport;
import CIMI.Session;
import CIMI.SharingModel;
import CIMI.SlaViolation;
import CIMI.User;
import CIMI.UserProfile;
import dataclay.commonruntime.ClientManagementLib;
import dataclay.exceptions.metadataservice.ObjectNotRegisteredException;
import dataclay.util.ids.DataClayInstanceID;

public class DataClayWrapper {

	public static DataClayInstanceID leaderDC = null;
	public static ArrayList<DataClayInstanceID> childrenDCs = new ArrayList<>();
	public static DataClayInstanceID localDC = null;
	public static String[] resourceTypes = { "agreement", "device", "device-dynamic", "fog-area", "service",
			"service-instance", "sharing-model", "sla-violation", "user-profile", "service-operation-report",
			"cloud-entry-point", "email", "user", "credential", "session", "callback" };

	// All this should happen during Discovery. Now simulating it before execution.
	// Assuming a config file that contains the following:
	// LEADER_DC=host:port
	// CHILDREN_DC=host1:port1;host2:port2;...;hostn:portn
	public static void init() {
		final String leaderAddr = System.getenv("LEADER_DC"); // TODO: Can it be unified with some other var required by
		// others?
		final String childrenAddr = System.getenv("CHILDREN_DC"); // TODO: Idem
		String[] hostPort;

		if ((leaderAddr != null && !leaderAddr.isEmpty()) || (childrenAddr != null && !childrenAddr.isEmpty())) {
			localDC = ClientManagementLib.getDataClayID();
		}
		if (leaderAddr != null && !leaderAddr.isEmpty()) {
			hostPort = leaderAddr.split(":");
			// Register the dataClay instance of my leader
			leaderDC = ClientManagementLib.registerExternalDataClay(hostPort[0], Integer.parseInt(hostPort[1]));
			System.out.println("-- My dataClay leader is: " + leaderDC);
		}
		final ArrayList<ResourceCollection> collections = createLocalResourceCollections();
		if (childrenAddr != null && !childrenAddr.isEmpty()) {

			// Create shared object to sync with children
			final String[] childrenHostPorts = childrenAddr.split(";");
			for (final String addr : childrenHostPorts) {
				if (addr == null) {
					continue;
				}
				try {
					hostPort = addr.split(":");
					// Check if child is registered. If not, register it and federate collections
					final DataClayInstanceID childDC = ClientManagementLib.registerExternalDataClay(hostPort[0],
							Integer.parseInt(hostPort[1]));
					System.out.println("-- I have a dataClay child: " + childDC);
					childrenDCs.add(childDC);
					if (collections.size() > 0) {
						System.out.println("Federating collections to children.");
						for (final ResourceCollection col : collections) {
							col.federate(childDC, false);
						}
					}

				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static ArrayList<ResourceCollection> createLocalResourceCollections() {
		final ArrayList<ResourceCollection> result = new ArrayList<>();
		for (final String type : resourceTypes) {
			if (!type.equals("cloud-entry-point")) {
				final String className = javaize(type);
				final ResourceCollection resources = new ResourceCollection();
				try {
					String suffix;
					if (localDC == null) {
						suffix = "";
					} else {
						suffix = localDC.getId().toString();
					}
					resources.makePersistent(className + "Collection" + suffix); // we need to add localID
					// because
					// these collections are
					// shared
					result.add(resources);
				} catch (final Exception e) {
					// already registered, ok.
				}
				// addToCloudEntryPoint(type, resources);
			}
		}
		return result;
	}

	/**
	 * Create object from JSON in dataClay
	 * 
	 * @param type
	 *            resource type name (lower-case part of the resource ID before the
	 *            slash)
	 * @param id
	 *            resource id (part of the resource ID after the slash, which may
	 *            not actually be a uuid)
	 * @param data
	 *            json file passed to the CIMI server by the user, with the
	 *            server-managed fields
	 * @throws Exception
	 *             if something is wrong
	 */
	public static void create(final String type, final String id, final String data) throws Exception {
		// (id, resourceID, created, updated) added/replaced as necessary
		if (type == null)
			throw new IllegalArgumentException("Argument 'type' is empty");
		if (id == null)
			throw new IllegalArgumentException("Argument 'id' is empty");
		if (data == null)
			throw new IllegalArgumentException("Argument 'data' is empty");

		Map<String, Object> objectData = new JSONObject(data).toMap();
		objectData = preProcessSubObjects(type, objectData);
		CIMIResource obj = null;
		switch (type) {
		// mF2C resources
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
		case "service-operation-report":
			obj = new ServiceOperationReport(objectData);
			break;
		// CIMI resources
		case "cloud-entry-point":
			obj = new CloudEntryPoint(objectData);
			break;
		case "email":
			obj = new Email(objectData);
			break;
		case "user":
			obj = new User(objectData);
			break;
		case "user-template":
			// obj = new UserTemplate(objectData);
			break;
		case "credential":
			obj = new Credential(objectData);
			break;
		case "credential-template":
			// obj = new CredentialTemplate(objectData);
			break;
		case "configuration":
			// obj = new Configuration(objectData);
			break;
		case "configuration-template":
			// obj = new ConfigurationTemplate(objectData);
			break;
		case "session":
			obj = new Session(objectData);
			break;
		case "session-template":
			// obj = new SessionTemplate(objectData);
			break;
		case "user-param":
			// obj = new UserParam(objectData);
			break;
		case "user-param-template":
			// obj = new UserParamTemplate(objectData);
			break;
		case "callback":
			obj = new Callback(objectData);
			break;
		case "example-resource":
			// obj = new ExampleResource(objectData);
			break;
		default:
			throw new IllegalArgumentException("Invalid resource type: " + type);
		}
		obj.makePersistent(id); // id is the alias
		if (leaderDC != null) {
			obj.federate(leaderDC, false); // Non-recursive because each object is individually federated
		}

		if (!type.equals("cloud-entry-point")) {
			ResourceCollection resources = null;
			ResourceCollection leaderResources = null;
			final String className = javaize(type);
			String suffix;
			if (localDC == null) {
				suffix = "";
			} else {
				suffix = localDC.getId().toString();
			}
			try {
				// Possible optimization: keep collections in a local variable to avoid
				// "getByAlias"

				resources = (ResourceCollection) ResourceCollection.getByAlias(className + "Collection" + suffix);
			} catch (final Exception e) {
				/*
				 * Version with ResourceCollections created on-the-fly, now they are all created
				 * in the init * resources = new ResourceCollection();
				 * resources.makePersistent(className + "Collection" + localDC.getId()); //
				 * addToCloudEntryPoint(type, resources);
				 */
				System.err.println(
						"ERROR: Resource collection '" + className + "Collection" + suffix + "' does not exist");
				throw e;
			}
			if (leaderDC != null) {
				try {
					// Possible optimization: keep collections in a local variable to avoid
					// "getByAlias"
					leaderResources = (ResourceCollection) ResourceCollection
							.getByAlias(className + "Collection" + leaderDC.getId());

				} catch (final Exception e) {
					/*
					 * Version with ResourceCollections created on-the-fly, now they are all created
					 * in the init * resources = new ResourceCollection();
					 * resources.makePersistent(className + "Collection" + localDC.getId()); //
					 * addToCloudEntryPoint(type, resources);
					 */
					System.err.println("ERROR: Resource collection '" + className + "Collection" + leaderDC.getId()
							+ "' does not exist");
					throw e;
				}
			}
			resources.put(id, obj);
			// Execute "resources.put" in the collection of the leader (not in children
			// because they don't need to see it)
			if (leaderResources != null) {
				leaderResources.runFederated(leaderDC,
						"putFederated(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
						new Object[] { id, obj.getClass().getName(), id });
			}
		}
	}

	/**
	 * Get a JSON representation of a dataClay object of type specified and
	 * identified by id provided
	 * 
	 * @param type
	 *            Type of the object
	 * @param id
	 *            Id of the object
	 * @return JSON representation of dataClay object
	 * @throws IllegalArgumentException
	 *             if type is wrong
	 */
	public static String read(final String type, final String id) throws IllegalArgumentException {
		// returns json file with all the resource data
		if (type == null)
			throw new IllegalArgumentException("Argument 'type' is empty");
		if (id == null)
			throw new IllegalArgumentException("Argument 'id' is empty");

		final CIMIResource obj = getResourceAsObject(type, id);
		Map<String, Object> info = obj.getCIMIResourceData();
		info = postProcessSubObjects(type, info);
		final JSONObject json = new JSONObject(info);
		return json.toString();
	}

	/**
	 * @brief Delete dataClay object of type provided and id specified
	 * @param type
	 *            Type of the object to delete
	 * @param id
	 *            ID of the object
	 * @throws IllegalArgumentException
	 *             if type is wrong
	 */
	public static void delete(final String type, final String id) throws IllegalArgumentException {
		if (type == null)
			throw new IllegalArgumentException("Argument 'type' is empty");
		if (id == null)
			throw new IllegalArgumentException("Argument 'id' is empty");

		switch (type) {
		case "agreement":
			Agreement.deleteAlias(id);
			break;
		case "device":
			Device.deleteAlias(id);
			break;
		case "device-dynamic":
			DeviceDynamic.deleteAlias(id);
			break;
		case "fog-area":
			FogArea.deleteAlias(id);
			break;
		case "service":
			Service.deleteAlias(id);
			break;
		case "service-instance":
			ServiceInstance.deleteAlias(id);
			break;
		case "sharing-model":
			SharingModel.deleteAlias(id);
			break;
		case "sla-violation":
			SlaViolation.deleteAlias(id);
			break;
		case "user-profile":
			UserProfile.deleteAlias(id);
			break;
		case "service-operation-report":
			ServiceOperationReport.deleteAlias(id);
			break;
		// CIMI resources
		case "email":
			Email.deleteAlias(id);
			break;
		case "user":
			User.deleteAlias(id);
			break;
		case "user-template":
			// UserTemplate.deleteAlias(id);
			break;
		case "credential":
			Credential.deleteAlias(id);
			break;
		case "credential-template":
			// CredentialTemplate.deleteAlias(id);
			break;
		case "configuration":
			// Configuration.deleteAlias(id);
			break;
		case "configuration-template":
			// ConfigurationTemplate.deleteAlias(id);
			break;
		case "session":
			Session.deleteAlias(id);
			break;
		case "session-template":
			// SessionTemplate.deleteAlias(id);
			break;
		case "user-param":
			// UserParam.deleteAlias(id);
			break;
		case "user-param-template":
			// UserParamTemplate.deleteAlias(id);
			break;
		case "callback":
			Callback.deleteAlias(id);
			break;
		case "example-resource":
			// ExampleResource.deleteAlias(id);
			break;
		default:
			throw new IllegalArgumentException("Invalid resource type: " + type);
		}
		String suffix;
		if (localDC == null) {
			suffix = "";
		} else {
			suffix = localDC.getId().toString();
		}
		final String colAlias = javaize(type) + "Collection" + suffix;
		final ResourceCollection resources = (ResourceCollection) ResourceCollection.getByAlias(colAlias);
		resources.delete(id);
	}

	/**
	 * Update dataClay object of type specified and id provided with some JSON data
	 * 
	 * @param type
	 *            type of the object
	 * @param id
	 *            id of the object
	 * @param updatedData
	 *            JSON string representing values to update
	 * @throws IllegalArgumentException
	 *             if argument is wrong
	 */
	public static void update(final String type, final String id, final String updatedData)
			throws IllegalArgumentException {
		if (type == null)
			throw new IllegalArgumentException("Argument 'type' is empty");
		if (id == null)
			throw new IllegalArgumentException("Argument 'id' is empty");
		if (updatedData == null)
			throw new IllegalArgumentException("Argument 'data' is empty");

		final JSONObject json = new JSONObject(updatedData);
		Map<String, Object> objectData = json.toMap();
		objectData = preProcessSubObjects(type, objectData);
		switch (type) {
		case "agreement":
			final Agreement agr = (Agreement) Agreement.getByAlias(id);
			agr.updateAllData(objectData);
			break;
		case "device":
			final Device dev = (Device) Device.getByAlias(id);
			dev.updateAllData(objectData);
			break;
		case "device-dynamic":
			final DeviceDynamic dd = (DeviceDynamic) DeviceDynamic.getByAlias(id);
			dd.updateAllData(objectData);
			break;
		case "fog-area":
			final FogArea fa = (FogArea) FogArea.getByAlias(id);
			fa.updateAllData(objectData);
			break;
		case "service":
			final Service serv = (Service) Service.getByAlias(id);
			serv.updateAllData(objectData);
			break;
		case "service-instance":
			final ServiceInstance si = (ServiceInstance) ServiceInstance.getByAlias(id);
			si.updateAllData(objectData);
			break;
		case "sharing-model":
			final SharingModel sm = (SharingModel) SharingModel.getByAlias(id);
			sm.updateAllData(objectData);
			break;
		case "sla-violation":
			final SlaViolation sv = (SlaViolation) SlaViolation.getByAlias(id);
			sv.updateAllData(objectData);
			break;
		case "user-profile":
			final UserProfile up = (UserProfile) UserProfile.getByAlias(id);
			up.updateAllData(objectData);
			break;
		case "service-operation-report":
			final ServiceOperationReport sor = (ServiceOperationReport) ServiceOperationReport.getByAlias(id);
			sor.updateAllData(objectData);
			break;
		// CIMI resources
		case "cloud-entry-point":
			final CloudEntryPoint cep = (CloudEntryPoint) CloudEntryPoint.getByAlias(id);
			cep.updateAllData(objectData);
			break;
		case "email":
			final Email em = (Email) Email.getByAlias(id);
			em.updateAllData(objectData);
			break;
		case "user":
			final User u = (User) User.getByAlias(id);
			u.updateAllData(objectData);
			break;
		case "user-template":
			// final UserTemplate ut = (UserTemplate) UserTemplate.getByAlias(id);
			// ut.updateAllData(objectData);
			break;
		case "credential":
			final Credential cr = (Credential) Credential.getByAlias(id);
			cr.updateAllData(objectData);
			break;
		case "credential-template":
			// final CredentialTemplate ct = (CredentialTemplate)
			// CredentialTemplate.getByAlias(id);
			// ct.updateAllData(objectData);
			break;
		case "configuration":
			// final Configuration cf = (Configuration) Configuration.getByAlias(id);
			// cf.updateAllData(objectData);
			break;
		case "configuration-template":
			// final ConfigurationTemplate cft = (ConfigurationTemplate)
			// ConfigurationTemplate.getByAlias(id);
			// cft.updateAllData(objectData);
			break;
		case "session":
			final Session s = (Session) Session.getByAlias(id);
			s.updateAllData(objectData);
			break;
		case "session-template":
			// final SessionTemplate st = (SessionTemplate) SessionTemplate.getByAlias(id);
			// st.updateAllData(objectData);
			break;
		case "user-param":
			// final UserParam upar = (UserParam) UserParam.getByAlias(id);
			// upar.updateAllData(objectData);
			break;
		case "user-param-template":
			// final UserParamTemplate upt = (UserParamTemplate)
			// UserParamTemplate.getByAlias(id);
			// upt.updateAllData(objectData);
			break;
		case "callback":
			final Callback c = (Callback) Callback.getByAlias(id);
			c.updateAllData(objectData);
			break;
		case "example-resource":
			// final ExampleResource er = (ExampleResource) ExampleResource.getByAlias(id);
			// er.updateAllData(objectData);
			break;
		default:
			throw new IllegalArgumentException("Invalid resource type: " + type);
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
	 */
	public static List<String> query(final String type, final String expression, final String user, final String role) {

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
		String suffix;
		if (localDC == null) {
			suffix = "";
		} else {
			suffix = localDC.getId().toString();
		}
		final String aliasOfCollection = javaize(type) + "Collection" + suffix;
		// System.out.println("Expression: " + expressionWithAcl);
		ResourceCollection collection = null;
		try {
			collection = (ResourceCollection) ResourceCollection.getByAlias(aliasOfCollection);
		} catch (final ObjectNotRegisteredException e) {
			// collection does not exist yet, no object was created of type provided, return
			// empty collection
			return new ArrayList<>();
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
	 */
	public static List<String> queryURL(final String type, final String urlQuery) {

		String suffix;
		if (localDC == null) {
			suffix = "";
		} else {
			suffix = localDC.getId().toString();
		}
		final String aliasOfCollection = javaize(type) + "Collection" + suffix;
		ResourceCollection collection = null;
		try {
			collection = (ResourceCollection) ResourceCollection.getByAlias(aliasOfCollection);
		} catch (final ObjectNotRegisteredException e) {
			// collection does not exist yet, no object was created of type provided, return
			// empty collection
			e.printStackTrace();
			return new ArrayList<>();
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
		final int slash = className.indexOf("-");
		// I assume we only have a single slash in a resource type (all of them are like
		// this now)
		if (slash > 0) {
			// Separate first and second parts, without the slash
			final String first = className.substring(0, slash);
			String second = className.substring(slash + 1);
			// First character of second to upper case
			second = capitalize(second);
			className = first.concat(second);
		}
		return className;
	}

	private static String capitalize(final String word) {
		return word.substring(0, 1).toUpperCase() + word.substring(1);
	}

	private static CIMIResource getResourceAsObject(final String type, final String id)
			throws IllegalArgumentException {
		CIMIResource obj = null;
		switch (type) {
		// mF2C resources
		case "agreement":
			obj = (Agreement) Agreement.getByAlias(id);
			break;
		case "device":
			obj = (Device) Device.getByAlias(id);
			break;
		case "device-dynamic":
			obj = (DeviceDynamic) DeviceDynamic.getByAlias(id);
			break;
		case "fog-area":
			obj = (FogArea) FogArea.getByAlias(id);
			break;
		case "service":
			obj = (Service) Service.getByAlias(id);
			break;
		case "service-instance":
			obj = (ServiceInstance) ServiceInstance.getByAlias(id);
			break;
		case "sharing-model":
			obj = (SharingModel) SharingModel.getByAlias(id);
			break;
		case "sla-violation":
			obj = (SlaViolation) SlaViolation.getByAlias(id);
			break;
		case "user-profile":
			obj = (UserProfile) UserProfile.getByAlias(id);
			break;
		case "service-operation-report":
			obj = (ServiceOperationReport) ServiceOperationReport.getByAlias(id);
			break;
		// CIMI resources
		case "cloud-entry-point":
			obj = (CloudEntryPoint) CloudEntryPoint.getByAlias(id);
			break;
		case "email":
			obj = (Email) Email.getByAlias(id);
			break;
		case "user":
			obj = (User) User.getByAlias(id);
			break;
		case "user-template":
			// obj = (UserTemplate) UserTemplate.getByAlias(id);
			break;
		case "credential":
			obj = (Credential) Credential.getByAlias(id);
			break;
		case "credential-template":
			// obj = (CredentialTemplate) CredentialTemplate.getByAlias(id);
			break;
		case "configuration":
			// obj = (Configuration) Configuration.getByAlias(id);
			break;
		case "configuration-template":
			// obj = (ConfigurationTemplate) ConfigurationTemplate.getByAlias(id);
			break;
		case "session":
			obj = (Session) Session.getByAlias(id);
			break;
		case "session-template":
			// obj = (SessionTemplate) SessionTemplate.getByAlias(id);
			break;
		case "user-param":
			// obj = (UserParam) UserParam.getByAlias(id);
			break;
		case "user-param-template":
			// obj = (UserParamTemplate) UserParamTemplate.getByAlias(id);
			break;
		case "callback":
			obj = (Callback) Callback.getByAlias(id);
			break;
		case "example-resource":
			// obj = (ExampleResource) ExampleResource.getByAlias(id);
			break;
		default:
			throw new IllegalArgumentException("Invalid resource type: " + type);
		}
		return obj;
	}

	private static Map<String, Object> preProcessSubObjects(final String type, final Map<String, Object> objectData)
			throws IllegalArgumentException {
		CIMIResource obj = null;
		switch (type) {
		case "device-dynamic":
			Map<String, Object> link = (Map<String, Object>) objectData.get("device");
			String resourceId = (String) link.get("href");
			String subType = resourceId.substring(0, resourceId.indexOf("/"));
			String subId = resourceId.substring(resourceId.indexOf("/") + 1);
			if (resourceId != null) {
				obj = getResourceAsObject(subType, subId);
				objectData.put("device", obj);
			}
			break;
		case "fog-area":
			link = (Map<String, Object>) objectData.get("leaderDevice");
			resourceId = (String) link.get("href");
			subType = resourceId.substring(0, resourceId.indexOf("/"));
			subId = resourceId.substring(resourceId.indexOf("/") + 1);
			if (resourceId != null) {
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
