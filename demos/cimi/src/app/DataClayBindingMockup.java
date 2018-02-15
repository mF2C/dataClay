package app;

import java.util.List;

import dataclay.api.DataClay;
import model.Agent;
import model.CIMIResourceCollection;
import model.Device;

public class DataClayBindingMockup {
    private static void usage() {
	System.out.println("Usage: app.DataClayBindingMockup <value>");
	System.exit(-1);
    }

    public static void main(String[] args) throws Exception {
	if (args.length != 1) {
	    usage();
	}
	// Init dataClay session
	DataClay.init();
	
	Device newDevice = new Device("mf2c-device4", "OS4", "Architecture 4", "ProcMaker 4", "ProcArch 4", 4, 4, 4, 4,
		4000, true, "Graphics Card 4", "https://cimi.org/resources/devices/4", "device4", "device 4",
		"https://cimi.org/resources/devices");

	Agent newAgent = new Agent("mf2c-agent4", newDevice, "https://cimi.org/resources/agents/4", "agent4", "agent 4",
		"https://cimi.org/resources/agents");

	CIMIResourceCollection devices = (CIMIResourceCollection) CIMIResourceCollection.getByAlias("Devices");
	devices.put(newDevice);

	CIMIResourceCollection agents = (CIMIResourceCollection) CIMIResourceCollection.getByAlias("Agents");
	agents.put(newAgent);
	
	System.out.println("\n\n ***** RESULTS ***** ");
	List<Object> filteredObjects = devices.filter("numCores >= " + args[0]);
	for (Object resource : filteredObjects) {
	    Device device = (Device) resource;
	    System.out.println(device.getName());
	}
	System.out.println("\n\n");

	// Finish dataClay session
	DataClay.finish();
    }
}
