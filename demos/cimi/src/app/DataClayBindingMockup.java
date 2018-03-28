package app;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import dataclay.api.DataClay;
import model.Agent;
import model.CIMIResourceCollection;
import model.Device;
import model.Service;

public class DataClayBindingMockup {
    private static void usage() {
	System.out.println("Usage: app.DataClayBindingMockup [-d <devices_ast_file>] [-s <services_ast_file]");
	System.exit(-1);
    }

    private static String dASTFilePath, sASTFilePath;

    public static void main(String[] args) throws Exception {
	checkArgs(args);

	// Init dataClay session
	DataClay.init();

	// First approach for input ASTs of devices
	if (dASTFilePath != null) {
	    File devicesASTFile = new File(dASTFilePath);
	    String devicesAST = new String(Files.readAllBytes(devicesASTFile.toPath()));

	    // Add some new device and agent
	    Device newDevice = new Device("mf2c-device4", "OS4", "Architecture 4", "ProcMaker 4", "ProcArch 4", 4, 4, 4,
		    4, 4000, true, "Graphics Card 4", "https://cimi.org/resources/devices/4", "device4", "device 4",
		    "https://cimi.org/resources/devices");
	    Agent newAgent = new Agent("mf2c-agent4", newDevice, "https://cimi.org/resources/agents/4", "agent4",
		    "agent 4", "https://cimi.org/resources/agents");

	    // Init devices and agents collection references (and add some new info)
	    CIMIResourceCollection devices = (CIMIResourceCollection) CIMIResourceCollection.getByAlias("Devices");
	    devices.put(newDevice);
	    CIMIResourceCollection agents = (CIMIResourceCollection) CIMIResourceCollection.getByAlias("Agents");
	    agents.put(newAgent);

	    // Compute device filtering by AST
	    System.out.println("\n\n ***** RESULTS ON DEVICES ***** ");
	    System.out.println("\n\n ***** AST: " + devicesAST);
	    List<Object> filteredObjects = devices.filter(devicesAST);
	    for (Object resource : filteredObjects) {
		Device device = (Device) resource;
		System.out.println(device.getName());
	    }
	    System.out.println("\n\n");
	}

	// First approach for input ASTs of services with nested attributes
	if (sASTFilePath != null) {
	    File servicesASTFile = new File(sASTFilePath);
	    String servicesAST = new String(Files.readAllBytes(servicesASTFile.toPath()));

	    // Init service collection reference
	    CIMIResourceCollection services = (CIMIResourceCollection) CIMIResourceCollection.getByAlias("Services");

	    System.out.println("\n\n ***** RESULTS ON SERVICES ***** ");
	    System.out.println("\n\n ***** AST: " + servicesAST);
	    List<Object> filteredObjects = services.filter(servicesAST);
	    for (Object resource : filteredObjects) {
		Service service = (Service) resource;
		System.out.println(service.getName());
	    }
	    System.out.println("\n\n");
	}

	// Finish dataClay session
	DataClay.finish();
    }

    private static void checkArgs(String[] args) {
	if (args.length <= 1) {
	    usage();
	}
	if (args[0].equals("-d")) {
	    dASTFilePath = args[1];
	} else if (args[0].equals("-s")) {
	    sASTFilePath = args[1];
	} else {
	    usage();
	}
	if (args.length > 2) {
	    if (args[2].equals("-d")) {
		dASTFilePath = args[3];
	    } else if (args[2].equals("-s")) {
		sASTFilePath = args[3];
	    } else {
		usage();
	    }
	}
    }
}
