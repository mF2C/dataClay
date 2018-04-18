package CIMI;

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

    public void put(final CIMIResource newResource) {
	resources.put(newResource.get_id(), newResource);
    }

    public void delete(final String id) {
	resources.remove(id);
    }

    public Iterator<CIMIResource> iterator() {
	return resources.values().iterator();
    }

    public List<CIMIResource> filterResources(String query) {
	return (List<CIMIResource>) filterByAST(ASTUtil.cimiToAST(query));
    }
    
    public boolean isEmpty() {
	return resources.isEmpty();
    }
}