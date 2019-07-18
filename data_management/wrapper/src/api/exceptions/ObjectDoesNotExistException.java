package api.exceptions;

public class ObjectDoesNotExistException extends Exception {

	private static final long serialVersionUID = -6306659556346810490L;
	
	public ObjectDoesNotExistException(final String type, final String id) {
		super("There is no object of class '" + type + "' with id=" + id);
	}

}
