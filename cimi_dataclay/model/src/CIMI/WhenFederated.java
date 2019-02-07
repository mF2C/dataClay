package CIMI;

import java.util.Map;

public class WhenFederated extends CIMIResource {

	// Constructor
	public WhenFederated(final Map<String, Object> resourceData) {
		super(resourceData);
	}

	@Override
	public void whenFederated() {
		// when the object arrives to current dataClay, it is automatically added to the
		// collection with alias
		// provided. (no need to use runFederated put!)
		final ResourceCollection resources = (ResourceCollection) ResourceCollection
				.getByAlias("WhenFederatedCollection");
		resources.put(super.get_id(), this);
	}
}