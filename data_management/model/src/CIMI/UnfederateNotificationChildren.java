package CIMI;

import java.util.Map;

import dataclay.DataClayObject;
import dataclay.util.ids.DataClayInstanceID;

@SuppressWarnings("serial")
public class UnfederateNotificationChildren extends CIMIResource {
    String previousIP;
    Integer previousPort;
    String newIP;
    Integer newPort;
    boolean disableNotification = false;

	public UnfederateNotificationChildren(final Map<String, Object> objectData) {
		super(objectData);
	}
	
	@Override
	public void whenFederated() {
		if (disableNotification) { 
			return;
		}
		// when unfederate notification arrives it means that all objects belonging to 
		// leader ID provided (uuid) should be unfederated 
		DataClayInstanceID prevDataClayID = null;
		DataClayInstanceID newDataClayID = null;
		System.out.println("NOTIFICATION previous: " + previousIP + ":" + previousPort);
		System.out.println("NOTIFICATION new: " + newIP + ":" + newPort);

		if (previousIP != null) { 
			System.out.println("Registering/obtaining previous id " + previousIP + ":" + previousPort);
			prevDataClayID = DataClayObject.getLib().registerExternalDataClay(previousIP, previousPort);
			System.out.println("Previous id is " + prevDataClayID);

		}
		if (newIP != null) { 
			System.out.println("Registering/obtaining new id " + previousIP + ":" + previousPort);
			newDataClayID = DataClayObject.getLib().registerExternalDataClay(newIP, newPort);
			System.out.println("New id is " + newDataClayID);
		}
		
		if (prevDataClayID != null && newDataClayID != null) { 
			// change grandleader
			System.out.println("Migrating all objects from notification from " + prevDataClayID + " to " + newDataClayID);
			DataClayObject.getLib().migrateFederatedObjects(prevDataClayID, newDataClayID);
		} else if (prevDataClayID == null && newDataClayID != null) { 
			// new grandleader
			System.out.println("Federating all objects from notification to " + newDataClayID);
			DataClayObject.getLib().federateAllObjects(newDataClayID);
		} else if (prevDataClayID != null && newDataClayID == null) { 
			// lost grandleader
			System.out.println("Unfederating all objects from notification from " + prevDataClayID);
			DataClayObject.getLib().unfederateAllObjects(prevDataClayID);
		}
		
		// propagate notification to children
		final Agent agent = Agent.getByAliasExt("agent/agent");
		
		for (final String child : agent.getChildrenIPs()) {
			//propagate(child);
		}
	}
	
	public void disableNotification()  {
		// WARNING: in case a notification is federated due to a federateAll, do not trigger whenFederated
		disableNotification = true;
	}

}
