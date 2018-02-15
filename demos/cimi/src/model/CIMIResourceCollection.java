package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dataclay.DataClayObject;

@SuppressWarnings("serial")
public class CIMIResourceCollection extends DataClayObject implements Iterable<CIMIResource> {
    Map<String, CIMIResource> resources;

    public CIMIResourceCollection() {
	resources = new HashMap<>();
    }

    public CIMIResource get(final String id) {
	return resources.get(id);
    }

    public void put(final CIMIResource newResource) {
	resources.put(newResource.getResourceId(), newResource);
    }
    
    public void delete(final String id) {
	resources.remove(id);
    }

    @Override
    public Iterator<CIMIResource> iterator() {
	return resources.values().iterator();
    }

    @Override
    public List<Object> filter(String conditions) {
	return filterObject(conditions);
    }
}
