package demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import api.DataClayWrapper;
import dataclay.api.DataClay;
import dataclay.api.DataClayException;

public class AgentTest {


	public static void main(final String[] args) throws Exception {
		try {
			if (args.length < 2) {
				System.err.println("Usage: <agent type> <operation> ...");
				System.exit(-1);
			}
			final String agent = args[0];
			final String operation = args[1];

			System.out.println("---------------------------------");
			System.out.print(" Agent " + agent + " running operation " + operation + " with arguments: ");
			for (int i = 2; i < args.length; ++i) { 
				System.out.print("\"" + args[i] + "\" ");
			}
			System.out.println();
			System.out.println("---------------------------------");

			if (operation.equals("init")) {
				System.out.println("-- Waiting for dataClay to be ready...");
			}
			DataClay.init();
			
			if (!operation.equals("init")) {
				DataClayWrapper.init();
			}

			String obj, type, type_and_id;
			switch (operation) {
			case "init":
				System.out.println("-- dataClay " + DataClay.getDataClayID() + " is ready!");
				break;
			case "create":
				obj = readFile(args[2]);
				createObjects(obj);
				break;
			case "query": 
				type = args[2];
				final String expression = args[3];
				final String user = args[4];
				final String role = args[5];
				final int expectedResult = Integer.valueOf(args[6]);
				checkQuery(type, expression, user, role, expectedResult);
				break;
			case "update": 
				type_and_id = args[2];
				obj = readFile(args[3]);
				update(type_and_id, obj);
				break;
			case "check-equals": 
				type_and_id = args[2];
				obj = readFile(args[3]);
				checkEqualsProvided(type_and_id, obj);
				break;
			default:
				DataClay.finish();
				throw new IllegalArgumentException("Operation " + operation + " not available");
			}
		} catch (final DataClayException err) {
			err.printStackTrace();
			throw err;
		} catch (final Exception e) {
			throw e;
		} finally {
			DataClay.finish();
		}

	}

	private static void createObjects(final String json) throws Exception {
		final JSONObject obj = new JSONObject(json);
		final String resourceId = obj.getString("id");
		final String type = resourceId.substring(0, resourceId.indexOf("/"));
		final String id = resourceId.substring(resourceId.indexOf("/") + 1);
		System.out.println("Parsing " + type + ": " + obj);
		DataClayWrapper.create(type, id, obj.toString());
		System.out.println("CREATED:" + type + "/" + id);
	}

	private static String readFile(final String path) throws IOException {
		final byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}
	
	private static void checkQuery(final String type, final String expression, 
			final String user, final String role,
			final int expectedSize) throws Exception { 
		
		final String thetype = type;
		String theexpression = expression; 
		if (theexpression.equals("null")) { 
			theexpression = null;
		}
		String theuser = user; 
		if (theuser.equals("null")) { 
			theuser = null;
		}
		String therole = role; 
		if (therole.equals("null")) { 
			therole = null;
		}
		final List<String> result = DataClayWrapper.query(thetype, theexpression, theuser, therole);
		System.out.println("All " + type + ":");
		for (final String curResult : result) {
			System.out.println(" -- " + curResult);
		}
		System.out.println();
		if (result.size() != expectedSize) { 
			throw new Exception("Number of " + type
					+ " (" + result.size() + ") found do not match expected " + expectedSize);
		}
	}

	private static void update(final String type_and_id,
			final String updateRequestJson) throws Exception { 
		final String[] typeIDs = type_and_id.split("/");
		final String type = typeIDs[0];
		final String id = typeIDs[1];
		final JSONObject jsonUpdate = new JSONObject(updateRequestJson);
		final String updatedFields = jsonUpdate.toString();
		DataClayWrapper.update(type, id, updatedFields);
		System.out.println("Object " + type + "/" + id + " updated");
		
	}

	private static void checkEqualsProvided(final String type_and_id,
			final String requestJson) throws Exception {
		final String[] typeIDs = type_and_id.split("/");
		final String type = typeIDs[0];
		final String id = typeIDs[1];
		final JSONObject json = new JSONObject(requestJson);
		final String storedValues = DataClayWrapper.read(type, id);
		System.out.println("Found object " + type + "/" + id + ": " + storedValues);
		final Map<String, Object> expectedResults = json.toMap();
		final Map<String, Object> objectData = new JSONObject(storedValues).toMap();
		for (final Entry<String, Object> entry : expectedResults.entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			Object stored = objectData.get(key);
			if (stored == null) { 
				throw new Exception(stored + " not found in updated object " + type + "/" + id);
			}
			if (value.getClass().equals(Double.class)) {
				stored = castToDouble(stored);
			}
			
			if (value.getClass().equals(ArrayList.class)) {
				final ArrayList expectedList = (ArrayList) value;
				final ArrayList storedList = (ArrayList) stored;
				for (int i = 0; i < expectedList.size(); ++i) { 
					if (expectedList.get(i).getClass().equals(Double.class)) {
						storedList.set(i, castToDouble(storedList.get(i)));
					}
				}

			}
			if (!stored.equals(value)) { 
				throw new Exception("Expected value " + value + " of type " + value.getClass().getName()
							+ " is different to " + stored + " in object " + type + "/" + id
							+ " and field " + key);
			}
			

		}
		System.out.println("Object " + type + "/" + id + " matches provided Json");
	}

	/**
	 * Cast parameter to double if needed
	 * @param obj parameter to cast
	 * @return double type representation of parameter provided
	 */
	private static Double castToDouble(final Object obj) {
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
	
}
