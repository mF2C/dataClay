package CIMI;

import java.util.ArrayList;
import java.util.Map;

import dataclay.util.replication.Replication;

@SuppressWarnings({ "unchecked", "serial" })
public class QosModel extends CIMIResource {

	// An attribute for each field in the CIMI resource spec, with the same name
		// If it contains nested info, it is implemented as a Map<String, Object>
		// where String is the field name, and Object is the value
		@Replication.InMaster
		@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
		private Map<String, Object> service;
		@Replication.InMaster
		@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
		private Map<String, Object> agreement;
		@Replication.InMaster
		@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
		private ArrayList<Map<String, Object>> agents;
		@Replication.InMaster
		@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
		private String config; 
		@Replication.InMaster
		@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
		private Integer num_service_instances;
		@Replication.InMaster
		@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
		private Integer num_service_failures;
		@Replication.InMaster
		@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
		private ArrayList<Float> current_state;
		@Replication.InMaster
		@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
		private ArrayList<Float> next_state;

		// Constructor
		public QosModel(final Map<String, Object> objectData) {
			super(objectData);
			this.service = (Map<String, Object>) objectData.get("service");
			this.agreement = (Map<String, Object>) objectData.get("agreement");
			this.agents = (ArrayList<Map<String, Object>>) objectData.get("agents");
			this.config = (String) objectData.get("config");
			this.num_service_instances = (Integer) objectData.get("num_service_instances");
			this.num_service_failures = (Integer) objectData.get("num_service_failures");
			this.current_state = (ArrayList<Float>) objectData.get("current_state");
			this.next_state = (ArrayList<Float>) objectData.get("next_state");
		}

		// Setters (a setter for each property called "set_propertyname")
		public void set_service(final Map<String, Object> service) {
			this.service = service;
		}

		public void set_agreement(final Map<String, Object> agreement) {
			this.agreement = agreement;
		}

		public void set_agents(final ArrayList<Map<String, Object>> agents) {
			this.agents = agents;
		}
		
		public void set_config(final String config) {
			this.config = config;
		}
		
		public void set_num_service_instances(final Integer num_service_instances) {
			this.num_service_instances = num_service_instances;
		}
		
		public void set_num_service_failures(final Integer num_service_failures) {
			this.num_service_failures = num_service_failures;
		}
		
		public void set_current_state(final ArrayList<Float> current_state) {
			this.current_state = current_state;
		}
		public void set_next_state(final ArrayList<Float> next_state) {
			this.next_state = next_state;
		}

		// A single getter that returns a Map with all the info in this class and in
		// CIMIResource, called "getCIMIResourceData"
		@Override
		public Map<String, Object> getCIMIResourceData() {
			final Map<String, Object> info = super.getCIMIResourceData();
			if (this.service != null)
				info.put("service", this.service);
			if (this.agreement != null)
				info.put("agreement", this.agreement);
			if (this.agents != null)
				info.put("agents", this.agents);
			if (this.config != null)
				info.put("config", this.config);
			if (this.num_service_instances != null)
				info.put("num_service_instances", this.num_service_instances);
			if (this.num_service_failures != null)
				info.put("num_service_failures", this.num_service_failures);
			if (this.current_state != null)
				info.put("current_state", this.current_state);
			if (this.next_state != null)
				info.put("next_state", this.next_state);
			return info;
		}

		public void updateAllData(final Map<String, Object> data) {
			super.setCIMIResourceData(data);
			if (data.get("service") != null)
				set_service((Map<String, Object>) data.get("service"));
			if (data.get("agreement") != null)
				set_agreement((Map<String, Object>) data.get("agreement"));
			if (data.get("agents") != null)
				set_agents((ArrayList<Map<String, Object>>) data.get("agents"));
			if (data.get("config") != null)
				set_config((String) data.get("config"));
			if (data.get("num_service_instances") != null)
				set_num_service_instances((Integer) data.get("num_service_instances"));
			if (data.get("num_service_failures") != null)
				set_num_service_failures((Integer) data.get("num_service_failures"));
			if (data.get("current_state") != null)
				set_current_state((ArrayList<Float>) data.get("current_state"));
			if (data.get("next_state") != null)
				set_next_state((ArrayList<Float>) data.get("next_state"));
		}
	
}
