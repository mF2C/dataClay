package CIMI;

import java.util.Map;
import java.util.UUID;

import dataclay.DataClayObject;
import dataclay.util.ids.DataClayInstanceID;

@SuppressWarnings("serial")
public class UnfederateNotificationLeader extends CIMIResource {
    String previousID;
	public UnfederateNotificationLeader(final Map<String, Object> objectData) {
		super(objectData);
	}
	
	@Override
	public void whenFederated() {
		// when unfederate notification arrives it means that all objects belonging to 
		// ID provided (uuid) should be unfederated 
		final DataClayInstanceID prevDataClayID = new DataClayInstanceID(UUID.fromString(previousID));
		DataClayObject.getLib().unfederateAllObjects(prevDataClayID);
		
		// propagate notification to leader
		final Agent agent = Agent.getByAlias("agent/agent");
		propagate(agent.getLeaderIP());
	}

}
