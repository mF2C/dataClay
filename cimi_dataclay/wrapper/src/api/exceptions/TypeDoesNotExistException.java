package api.exceptions;

public class TypeDoesNotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1708079466728353915L;

	public TypeDoesNotExistException(final String type) {
		super("Unknown resource type '" + type + "'");
	}
}
