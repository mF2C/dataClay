package demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import api.DataClayWrapper;
import dataclay.api.DataClay;
import dataclay.api.DataClayException;

public class AgentTest {

		
	public static void main(final String[] args) throws Exception {
		try {
			if (args.length < 2) {
				System.err.println("Usage: <agent type> <operation> <agent path> <device and device dynamic path>");
				System.exit(-1);
			}
			final String agent = args[0];
			final String operation = args[1];

			System.out.println("---------------------------------");
			System.out.print(" Agent " + agent + " running operation " + operation + " with arguments: ");
			for (int i = 2; i < args.length; ++i) { 
				System.out.print(args[i] + " ");
			}
			System.out.println();
			System.out.println("---------------------------------");

			DataClay.init();
			DataClayWrapper.init();
			
			String obj, type, id;
			switch (operation) {
			case "create":
				obj = readFile(args[2]);
				createObjects(obj);
				break;
			case "query": 
				type = args[2];
				final int expectedResult = Integer.valueOf(args[3]);
				checkQuery(type, expectedResult);
				break;
			case "update": 
				type = args[2];
				id = args[3];
				obj = readFile(args[4]);
				update(type, id, obj);
				break;
			case "check-equals": 
				type = args[2];
				id = args[3];
				obj = readFile(args[4]);
				checkEqualsProvided(type, id, obj);
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
		final JSONArray array = new JSONArray(json);
		for (final Object element : array) {
			final JSONObject obj = (JSONObject) element;
			final String resourceId = obj.getString("id");
			final String type = resourceId.substring(0, resourceId.indexOf("/"));
			final String id = resourceId.substring(resourceId.indexOf("/") + 1);
			System.out.println("Parsing " + type + ": " + obj);
			DataClayWrapper.create(type, id, obj.toString());
			System.out.println(type + "/" + id + " created");
		}
	}

	private static String readFile(final String path) throws IOException {
		final byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}

	private static void checkQuery(final String type, final int expectedSize) throws Exception {
		final List<String> result = DataClayWrapper.query(type, null, null, null);
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
	
	
	private static void update(final String type, final String id, 
			final String updateRequestJson) throws Exception { 
		final JSONArray array = new JSONArray(updateRequestJson);
		for (final Object element : array) {
			final JSONObject jsonUpdate = (JSONObject) element;
			final String updatedFields = jsonUpdate.toString();
			DataClayWrapper.update(type, id, updatedFields);
			System.out.println("Object " + type + "/" + id + " updated");

		}
	}
	
	private static void checkEqualsProvided(final String type, final String id,
			final String requestJson) throws Exception {
		final JSONArray array = new JSONArray(requestJson);
		for (final Object element : array) {
			final JSONObject json = (JSONObject) element;
			final String storedValues = DataClayWrapper.read(type, id);
			System.out.println("Found object " + type + "/" + id + ": " + storedValues);
			final Map<String, Object> expectedResults = json.toMap();
			final Map<String, Object> objectData = new JSONObject(storedValues).toMap();
			for (final Entry<String, Object> entry : expectedResults.entrySet()) {
				final String key = entry.getKey();
				final Object value = entry.getValue();
				if (objectData.get(key) == null) { 
					throw new Exception(key + " not found in updated object " + type + "/" + id);
				}
				if (!objectData.get(key).equals(value)) { 
					throw new Exception("Expected value " + value 
							+ " is different to " + objectData.get(key) + " in updated object " + type + "/" + id);
				}
			}
		}
		System.out.println("Object " + type + "/" + id + " matches provided Json");
	}

}
