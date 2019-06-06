package CIMI;

import java.util.Map;
import java.util.UUID;

import dataclay.DataClayObject;
import dataclay.util.ids.DataClayInstanceID;

@SuppressWarnings("serial")
public class UnfederateNotificationChildren extends CIMIResource {
    String previousID;
    String newID;
	public UnfederateNotificationChildren(final Map<String, Object> objectData) {
		super(objectData);
	}
	
	@Override
	public void whenFederated() {
		// when unfederate notification arrives it means that all objects belonging to 
		// leader ID provided (uuid) should be unfederated 
		final DataClayInstanceID prevDataClayID = new DataClayInstanceID(UUID.fromString(previousID));		
		if (newID != null) { 
			// federate all objects to new
			final DataClayInstanceID newDataClayID = new DataClayInstanceID(UUID.fromString(newID));		
			DataClayObject.getLib().migrateFederatedObjects(prevDataClayID, newDataClayID);
		} else { 
			DataClayObject.getLib().unfederateAllObjects(prevDataClayID);
		}
		
		// propagate notification to children
		final Agent agent = Agent.getByAlias("agent/agent");
		
		for (final String child : agent.getChildrenIPs()) {
			propagate(child);
		}
	}

}
