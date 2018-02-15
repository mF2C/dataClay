package app;

import java.io.FileReader;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dataclay.api.DataClay;
import model.Agent;
import model.CIMIResource;
import model.CIMIResourceCollection;
import model.Device;

public class DataGenerator {
    private static void usage() {
	System.out.println("Usage: app.DataGenerator <JSONfile_Devices> <JSONFile_Agents>");
	System.exit(-1);
    }

    public static void main(String[] args) throws Exception {
	if (args.length != 2) {
	    usage();
	}
	// Init dataClay session
	DataClay.init();

	genResources(args[0], "Devices");
	genResources(args[1], "Agents");

	// Finish dataClay session
	DataClay.finish();
    }

    /**
     * @brief Method that parses input JSON file to register a set of resources
     * @param inputJSON
     *            filepath of JSON to be parsed
     * @throws Exception
     *             if the file is not present or contains a malformed JSON
     */
    private static void genResources(final String inputJSON, String resourceType) throws Exception {
	CIMIResourceCollection resources = new CIMIResourceCollection();
	CIMIResourceCollection devicesColl = null;
	if (resourceType.equals("Agents")) {
	    devicesColl = (CIMIResourceCollection) CIMIResourceCollection.getByAlias("Devices");
	}

	JsonParser parser = new JsonParser();
	JsonObject json = (JsonObject) parser.parse(new FileReader(inputJSON));
	JsonArray resourceArray = json.getAsJsonArray(resourceType);
	Iterator<JsonElement> it = resourceArray.iterator();
	while (it.hasNext()) {
	    JsonObject resource = it.next().getAsJsonObject();
	    CIMIResource cimiResource = null;
	    if (resourceType.equals("Devices")) {

		cimiResource = new Device(resource.get("id").getAsString(),
			resource.get("operatingSystem").getAsString(), resource.get("systemArchitecture").getAsString(),
			resource.get("processorMaker").getAsString(),
			resource.get("processorArchitecture").getAsString(), resource.get("numCPUs").getAsInt(),
			resource.get("CPUClockSpeed").getAsInt(), resource.get("numCores").getAsInt(),
			resource.get("RAMSize").getAsInt(), resource.get("storageSize").getAsInt(),
			resource.get("limitedPower").getAsBoolean(), resource.get("graphicsCardInfo").getAsString(),
			resource.get("resourceId").getAsString(), resource.get("name").getAsString(),
			resource.get("description").getAsString(), resource.get("resourceURI").getAsString());

	    } else if (resourceType.equals("Agents")) {

		Device myDevice = (Device) devicesColl.get(resource.get("device").getAsString());

		cimiResource = new Agent(resource.get("id").getAsString(), myDevice,
			resource.get("resourceId").getAsString(), resource.get("name").getAsString(),
			resource.get("description").getAsString(), resource.get("resourceURI").getAsString());
	    }
	    resources.put(cimiResource);
	}

	resources.makePersistent(resourceType);
    }
}
