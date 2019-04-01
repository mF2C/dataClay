package demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import api.DataClayWrapper;
import dataclay.api.DataClay;

public class ClojureMockup {

	public static void main(final String[] args) throws Exception {
		if (args.length < 5) {
			System.err.println("Missing JSON paths to: Services, Devices, DeviceDynamics, ServiceInstances, "
					+ "ServiceTest, AgreementTest");
			System.exit(-1);
		}
		final String services = readFile(args[0]);
		final String devices = readFile(args[1]);
		final String deviceDynamics = readFile(args[2]);
		final String serviceInstances = readFile(args[3]);
		final String agreements = readFile(args[4]);
		final String serviceTest = readFile(args[5]);
		final String agreementsFull = readFile(args[6]);
		final String callbackFull = readFile(args[7]);
		final String deviceFull = readFile(args[8]);
		final String deviceDynamicFull = readFile(args[9]);
		final String emailFull = readFile(args[10]);
		final String fogAreaFull = readFile(args[11]);
		final String serviceFull = readFile(args[12]);
		final String serviceInstanceFull = readFile(args[13]);
		final String serviceOperationReportFull = readFile(args[14]);
		final String credentialFull = readFile(args[15]);
		final String events = readFile(args[16]);
		final String qos = readFile(args[17]);

		DataClay.init();
		DataClayWrapper.init();

		// CREATE
		// First create the CloudEntryPoint
		final Map<String, Object> cloudData = new HashMap<>();
		cloudData.put("id", "cloud-entry-point");
		cloudData.put("name", "Cloud entry point");
		cloudData.put("description", "This is the Cloud entry point");
		cloudData.put("resourceURI", "Cloud URI");
		final JSONObject jsonData = new JSONObject(cloudData);
		DataClayWrapper.create("cloud-entry-point", "cloud-entry-point", jsonData.toString());

		System.out.println("Cloud Entry Point created: ");
		System.out.println(DataClayWrapper.read("cloud-entry-point", "cloud-entry-point"));

		// CREATE
		create_from_json_string(services);
		create_from_json_string(devices);
		create_from_json_string(deviceDynamics);
		create_from_json_string(serviceInstances);
		create_from_json_string(agreements);
		create_from_json_string(serviceTest);

		// READ
		System.out.println("A Device: " + DataClayWrapper.read("device", "12345"));
		System.out.println("A Service: " + DataClayWrapper.read("service", "12345"));
		System.out.println("A DeviceDynamic: " + DataClayWrapper.read("device-dynamic", "12345"));
		System.out.println("A ServiceInstance: " + DataClayWrapper.read("service-instance", "12345"));

		// UPDATE
		final Map<String, Object> newData = new HashMap<>();
		newData.put("storage", 3.1);
		newData.put("os", "Windows");
		final String updatedData = new JSONObject(newData).toString();
		DataClayWrapper.update("device", "12345", updatedData);
		System.out.println("Device after update: " + DataClayWrapper.read("device", "12345"));

		// QUERY
		// final String expression = "[:Filter [:AndExpr [:Comp [:Attribute \"os\"]
		// [:EqOp \"=\"] [:SingleQuoteString 'Windows']]]]";
		final String expression = "[:Filter [:AndExpr [:Comp [:Attribute \"os\"] [:EqOp \"=\"] [:SingleQuoteString \"'Windows'\"]]]]";

		// "Query by owner" (ha de donar 1 resultat)
		List<String> result = DataClayWrapper.query("device", expression, "User1", "USER");
		System.out.println("** 1st query results : " + result);
		System.out.println("** 1st query results size : " + result.size());
		assert result.size() == 1;

		// "Query by role with permissions" (ha de donar 1 resultat)
		result = DataClayWrapper.query("device", expression, "User1", "ADMIN");
		System.out.println("** 2nd query results : " + result);
		System.out.println("** 2nd query results size : " + result.size());
		assert result.size() == 1;

		// "Query without permissions" (no ha de donar cap resultat)
		result = DataClayWrapper.query("device", expression, "User2", "USER");
		System.out.println("** 3rd query results : " + result);
		System.out.println("** 3rd query results size : " + result.size());
		assert result.size() == 0;

		// Empty query, empty user, empty role (1 resultat)
		result = DataClayWrapper.query("device", null, null, null);
		System.out.println("** 4th query results : " + result);
		System.out.println("** 4th query results size : " + result.size());
		assert result.size() == 1;

		// Empty query, empty user, role = ADMIN (1 resultat)
		result = DataClayWrapper.query("device", null, null, "ADMIN");
		System.out.println("** 5th query results : " + result);
		System.out.println("** 5th query results size : " + result.size());
		assert result.size() == 1;

		// Empty query, empty user, role = USER (0 resultat)
		result = DataClayWrapper.query("device", null, null, "USER");
		System.out.println("** 6th query results : " + result);
		System.out.println("** 6th query results size : " + result.size());
		assert result.size() == 0;

		// Empty query, user = User1, empty role (1 resultat)
		result = DataClayWrapper.query("device", null, "User1", null);
		System.out.println("** 7th query results : " + result);
		System.out.println("** 7th query results size : " + result.size());
		assert result.size() == 1;

		// Empty query, user = User2, empty role (0 resultat)
		result = DataClayWrapper.query("device", null, "User2", null);
		System.out.println("** 8th query results : " + result);
		System.out.println("** 8th query results size : " + result.size());
		assert result.size() == 0;

		// Non-empty query, empty user, empty role (1 resultat)
		result = DataClayWrapper.query("device", expression, null, null);
		System.out.println("** 9th query results : " + result);
		System.out.println("** 9th query results size : " + result.size());
		assert result.size() == 1;

		// Non-empty query, empty user, role = ADMIN (1 resultat)
		result = DataClayWrapper.query("device", expression, null, "ADMIN");
		System.out.println("** 10th query results : " + result);
		System.out.println("** 10th query results size : " + result.size());
		assert result.size() == 1;

		// Non-empty query, empty user, role = USER (0 resultat)
		result = DataClayWrapper.query("device", expression, null, "USER");
		System.out.println("** 11th query results : " + result);
		System.out.println("** 11th query results size : " + result.size());
		assert result.size() == 0;

		// Non-empty query, user = User1, empty role (1 resultat)
		result = DataClayWrapper.query("device", expression, "User1", null);
		System.out.println("** 12th query results : " + result);
		System.out.println("** 12th query results size : " + result.size());
		assert result.size() == 1;

		// Non-empty query, user = User2, empty role (0 resultat)
		result = DataClayWrapper.query("device", expression, "User2", null);
		System.out.println("** 13th query results : " + result);
		System.out.println("** 13th query results size : " + result.size());
		assert result.size() == 0;

		// DELETE
		DataClayWrapper.delete("device", "12345");
		System.out.println("Device deleted");
		try {
			System.out.println("Trying to read deleted device: " + DataClayWrapper.read("device", "12345"));
		} catch (final Exception dce) {
			System.out.println("Expected exception caught :)");
		}
		// query result after deleted device (no ha de donar cap resultat)
		result = DataClayWrapper.query("device", expression, "User1", "USER");
		System.out.println("** 14th query results : " + result);
		System.out.println("** 14th query results size : " + result.size());
		assert result.size() == 0;

		// CREATE FROM JSON FILE

		create_from_json_string(agreementsFull);
		create_from_json_string(callbackFull);
		create_from_json_string(deviceFull);
		create_from_json_string(deviceDynamicFull);
		create_from_json_string(emailFull);
		create_from_json_string(fogAreaFull);
		create_from_json_string(serviceFull);
		create_from_json_string(serviceInstanceFull);
		create_from_json_string(serviceOperationReportFull);
		create_from_json_string(credentialFull);
		create_from_json_string(events);
		create_from_json_string(qos);

		// READS
		System.out.println("********* READING ***********");
		DataClayWrapper.read("agreement", "agreement");
		DataClayWrapper.read("callback", "callback");
		DataClayWrapper.read("device", "device");
		DataClayWrapper.read("device-dynamic", "device-dynamic");
		DataClayWrapper.read("email", "email");
		DataClayWrapper.read("fog-area", "fog-area");
		DataClayWrapper.read("service", "service");
		DataClayWrapper.read("service-instance", "service-instance");
		DataClayWrapper.read("service-operation-report", "service-operation-report");
		DataClayWrapper.read("credential", "credential");
		System.out.println(DataClayWrapper.read("event", "event"));
		System.out.println(DataClayWrapper.read("qos-model", "qos-model"));

		// UPDATE
		final Map<String, Object> newQos = new HashMap<>();
		newQos.put("next_state", new float[] {(float) 5.55});
		final String newupdatedData = new JSONObject(newQos).toString();
		System.out.println(newupdatedData);
		DataClayWrapper.update("qos-model", "qos-model", newupdatedData);
		System.out.println("QosModel after update: " + DataClayWrapper.read("qos-model", "qos-model"));
		
		DataClay.finish();
	}

	private static void create_from_json_string(final String data) throws Exception {
		System.out.println("Parsing: " + data);
		final JSONArray array = new JSONArray(data);
		for (final Object element : array) {
			call_create((JSONObject) element);
		}
		System.out.println("New Data created");
	}

	public static void call_create(final JSONObject obj) throws Exception {
		final String resourceId = obj.getString("id");
		final String type = resourceId.substring(0, resourceId.indexOf("/"));
		final String id = resourceId.substring(resourceId.indexOf("/") + 1);
		DataClayWrapper.create(type, id, obj.toString());

	}

	public static String readFile(final String path) throws IOException {
		final byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}

}
