package api.exceptions;

public class ResourceCollectionDoesNotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2180733285475664651L;

	public ResourceCollectionDoesNotExistException(final String type) {
		super("Resource collection for type '" + type + "' could not be found");
	}
}
