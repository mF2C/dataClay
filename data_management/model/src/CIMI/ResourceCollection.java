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

	public Map<String, CIMIResource> getResources() {
		return resources;
	}

	@Override
	public Iterator<CIMIResource> iterator() {
		return resources.values().iterator();
	}

	public List<CIMIResource> filterResources(final String query) {
		final List<CIMIResource> result = new ArrayList<>();
		final List<Object> queryResult = new ArrayList<>();
		if (query == null || query.isEmpty()) {
			queryResult.addAll(resources.values());
		} else {
			queryResult.addAll(filterByAST(cimiToAST(query)));
		}
		for (final Object curElement : queryResult) {
			result.add((CIMIResource) curElement);
		}
		return result;
	}
	
	public List<Object> cimiToAST(final String cimiAST) {
		System.out.println("[CIMItoAST] Received " + cimiAST);
		final String jsonAST = cimiToJSON(cimiAST);
		System.out.println("[CIMItoAST] Post processed " + jsonAST);
		return ASTUtil.jsonToAST(jsonAST);
	}

	public String cimiToJSON(final String cimiAST) {
		String result = cimiAST;
		// nested attributes
		result = result.replace("\" \"/\" \"", "/");
		// Spaces are commas in JSON
		result = result.replace(" ", ",");
		// [:token => ["token"
		result = result.replaceAll("\\[:([a-zA-Z]+)", "[\"$1\"");
		// remove inner quote on strings
		result = result.replace("\"\\\"", "\"");  // open double quote
		result = result.replace("\\\"\"", "\"");  // close double quote
		result = result.replace("\"'", "\"");  // open single quote
		result = result.replace("'\"", "\"");  // close single quote
		
		return result;
	}

	@Override
	public List<Object> filterStream(final String query) {
		return super.filterStream(query);
	}

	public boolean isEmpty() {
		return resources.isEmpty();
	}
}