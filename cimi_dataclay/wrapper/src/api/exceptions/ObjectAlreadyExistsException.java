package api.exceptions;

public class ObjectAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -6230102370426592050L;

	public ObjectAlreadyExistsException(final String type, final String id) {
		super("There is already a '" + type + "' with id=" + id);
	}
}
