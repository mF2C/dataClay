package CIMI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dataclay.DataClayObject;
import dataclay.util.filtering.ASTUtil;

@SuppressWarnings("serial")
public class ResourceCollection extends DataClayObject implements Iterable<CIMIResource> {
	Map<String, CIMIResource> resources;

	public ResourceCollection() {
		resources = new HashMap<>();
	}

	public CIMIResource get(final String id) {
		return resources.get(id);
	}

	public void put(final String id, final CIMIResource newResource) {
		resources.put(id, newResource);
	}

	public void delete(final String id) {
		resources.remove(id);
	}

	@Override
	public Iterator<CIMIResource> iterator() {
		return resources.values().iterator();
	}

	public List<CIMIResource> filterResources(final String query) {
		final List<CIMIResource> result = new ArrayList<>();
		final List<Object> queryResult = new ArrayList<Object>();
		if (query == null || query.isEmpty()) {
			queryResult.addAll(resources.values());
		} else {
			queryResult.addAll(filterByAST(ASTUtil.cimiToAST(query)));
		}
		for (final Object curElement : queryResult) {
			result.add((CIMIResource) curElement);
		}
		return result;
	}

	public boolean isEmpty() {
		return resources.isEmpty();
	}
}