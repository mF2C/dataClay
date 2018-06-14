package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import dataclay.api.DataClay;
import dataclay.commonruntime.ClientManagementLib;
import dataclay.util.ids.DataClayInstanceID;
import dataclay.util.management.metadataservice.DataClayInstance;
import model.Agent;
import model.Device;

public class AgentMockup {

	private static String inputMessage;
	private static String config_file_path = "./cfgfiles/mf2c.properties";
	private static String myId;

	public static void main(String[] args) throws Exception {
		// Check input message
		if (args.length > 0) {
			inputMessage = args[0];
		}

		// Init dataClay session
		DataClay.init();

		// Identification
		myId = generateDeviceId();
		System.out.println("\n\n\nHi! I am agent " + myId + " in dataClay " + DataClay.getCommonLib().getDataClayID());
		PrintWriter writer = new PrintWriter(".dcid", "UTF-8");
		writer.println(DataClay.getCommonLib().getDataClayID());
		writer.close();

		// Categorization
		String resourceID = "CIMIResourceID";
		String name = "CIMIName";
		String description = "CIMIDescription";
		String resourceURI = "CIMIResourceURI";
		Agent myAgent;
		try {
			myAgent = (Agent) Agent.getByAlias(myId);
			System.out.println("Found previous agent info.");
		} catch (Exception ex) {
			Device myDevice = initDevice(resourceID, name, description, resourceURI);
			// Re-used the deviceId and also the CIMI fields for the Agent:
			myAgent = new Agent(myId, myDevice, resourceID, name, description, resourceURI);
			// Data operation: store
			myAgent.makePersistent(myAgent.getId(), DataClay.jLOCAL);
		}

		boolean isCloud = checkCloud(config_file_path);
		if (isCloud) {
			// Data operation: update
			myAgent.setCloud();
			runAsCloudAgent();
		} else {
			// Someone has to decide whether the agent is leader
			// For the test it is read from the config file, considering static => leader,
			// mobile => normal
			boolean isLeader = checkStatic(config_file_path);
			if (isLeader) {
				// Data operation: update
				myAgent.setLeader();
				runAsLeaderAgent();
			} else {
				runAsNormalAgent();
			}
		}

		DataClay.finish();
	}

	private static void runAsCloudAgent() {
		// Do whatever a Cloud Agent does
	}

	private static void runAsLeaderAgent() throws Exception {
		// Discovery module (sending welcome messages)
		String discoveredId = sendWelcomeMessage();
		// Data operation: execute
		Agent myAgent = (Agent) Agent.getByAlias(myId);
		System.out.println("Looking for child agent " + discoveredId);
		boolean known = myAgent.checkChildExists(discoveredId);
		if (known) {
			// ...do whatever
			System.out.println("Found child agent " + discoveredId);
			System.out.println("Hwloc after set: " + myAgent.getChild(discoveredId).getHwloc());
			System.out.println("CPUInfo after set: " + myAgent.getChild(discoveredId).getCPUInfo());
		} else {
			boolean available = false;
			Agent newAgent = null;
			while (!available) {
				try {
					newAgent = (Agent) Agent.getByAlias(discoveredId);
					available = true;
					System.out.println("Mobile agent appeared!");
				} catch (Exception ex) {
					System.out.println("Mobile agent not available yet...");
					TimeUnit.SECONDS.sleep(1);
				}
			}

			// Adding new child agent to leader
			myAgent.addChild(newAgent);

			System.out.println("Hwloc before set: " + newAgent.getHwloc());
			System.out.println("CPUInfo before set: " + newAgent.getCPUInfo());

			// Wait...
			System.in.read();

			// Now the value should have been updated in the child and seen in the leader
			System.out.println("Hwloc after set: " + newAgent.getHwloc());
			System.out.println("CPUInfo after set: " + newAgent.getCPUInfo());
		}
	}

	private static void runAsNormalAgent() throws Exception {
		// Discovery module (receiving welcome messages)
		String message = receiveWelcomeMessage();
		if (message == null || message.isEmpty()) {
			System.out.println("Mobile cannot discover the leader.");
			return;
		}
		// Get all the required info from the message to connect to the DM in the leader
		DataClayInstance myLeaderDM = getDataManagementConfig(message);
		// TODO Register the dataClay instance in the leader, with all the required info
		DataClayInstanceID dcID = registerExternalDataManagement(myLeaderDM);

		// Data operation: execute
		Agent myAgent = (Agent) Agent.getByAlias(myId);
		myAgent.federate(dcID); // myAgent (and Device, etc) has been replicated in the leader
		System.out.println("Mobile agent info federated with the leader.");

		System.out.println("Hwloc before set: " + myAgent.getHwloc());
		System.out.println("CPUInfo before set: " + myAgent.getCPUInfo());

		// ... do whatever a normal agent does

		// Wait for some time before updating so that the leader can print current value
		System.in.read();

		// For instance (to test replicas)
		// Data operation: updates
		myAgent.setHwloc("This Hwloc has been set in agent " + myAgent.getId());
		myAgent.setCPUInfo("This CPUInfo has been set in agent " + myAgent.getId());
		System.out.println("Hwloc after set: " + myAgent.getHwloc());
		System.out.println("CPUInfo after set: " + myAgent.getCPUInfo());
	}

	private static String generateDeviceId() throws Exception {
		// For this test I will read it from the config file since I need to control
		// them
		// Normally, this would be generated by Discovery and stored in a configuration
		// file
		try {
			BufferedReader br = new BufferedReader(new FileReader(config_file_path));
			String line = br.readLine();
			// 1st line is mf2c.id=n (cloud=0, leaders=1-2, normal=3)
			br.close();
			int position = line.indexOf('=');
			char value = line.charAt(position + 1);
			return String.valueOf(value);
		} catch (Exception e) {
			throw e;
		}
	}

	private static DataClayInstance getDataManagementConfig(String message) {
		// Parse welcome message to get the needed info
		StringTokenizer st = new StringTokenizer(message, ":");
		String hostname = st.nextToken();
		int port = Integer.valueOf(st.nextToken());
		return new DataClayInstance(null, hostname, port);
	}

	private static DataClayInstanceID registerExternalDataManagement(DataClayInstance externalInfo) {
		return ClientManagementLib.registerExternalDataClay(externalInfo.getHost(), externalInfo.getPort());
	}

	private static String receiveWelcomeMessage() {
		return inputMessage; // TODO: How should we handle this?
	}

	private static String sendWelcomeMessage() {
		return "4"; // This is the mobile device id
	}

	private static Device initDevice(String resourceID, String name, String description, String resourceURI) {
		Random r = new Random(0);
		String operatingSystem = "OS";
		String systemArchitecture = "SysArch";
		String processorMaker = "ProcMaker";
		String processorArchitecture = "ProcArch";
		int numCPUs = r.nextInt(4);
		int CPUClockSpeed = r.nextInt(2000);
		int numCores = r.nextInt(4);
		int RAMSize = r.nextInt(16000);
		int storageSize = r.nextInt(500000);
		boolean limitedPower = r.nextBoolean();
		String graphicsCardInfo = "GraphicsCard";
		return new Device(myId, operatingSystem, systemArchitecture, processorMaker, processorArchitecture, numCPUs,
				CPUClockSpeed, numCores, RAMSize, storageSize, limitedPower, graphicsCardInfo, resourceID, name,
				description, resourceURI);
	}

	private static boolean checkCloud(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();
		line = br.readLine();
		// 2nd line is mf2c.cloud=0/1
		br.close();
		int position = line.indexOf('=');
		char value = line.charAt(position + 1);
		return (value != '0');
	}

	private static boolean checkStatic(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();
		line = br.readLine();
		line = br.readLine();
		// 3rd line is mf2c.static=0/1
		br.close();
		int position = line.indexOf('=');
		char value = line.charAt(position + 1);
		return (value != '0');
	}
}
