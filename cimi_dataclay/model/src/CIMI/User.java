package CIMI;

import java.util.Map;


@SuppressWarnings("serial")
public class User extends CIMIResource {
	
	/** 
	 * ==== READ THIS! ====
	 * An attribute for each field in the CIMI resource spec, with the same name and type.
	 * 
	 * 1) If it contains nested info or its a href, it is implemented as a Map<String, Object>
	 *  where String is the field name, and Object is the value
	 *  
	 * 2) if you want to synchronize your field with agent leaders: add following annotations to your field: 
	 *		@Replication.InMaster and 
	 * 		@Replication.AfterUpdate(method = "replicateToDataClaysObjectIsFederatedWith", clazz = "dataclay.util.replication.SequentialConsistency")
	 * 
	 * 3) WARNING: All fields must be public or no modifier defined.
	 * 
	 * 4) All types defined must be Object types (Integer, Float, List...) and not primitive types (int, float,...) 
	 * 
	 * 5) Once finished, please do a Pull request and after testing and verifications dataClay will publish 
	 * a new docker image with your changes/new resource model. 
	 **/
    String username;
    String emailAddress;
    String firstName;
    String lastName;
    String organization;
    String method;
    String href;
    String password;
    String roles;
    Boolean isSuperUser;
    String state;
    Boolean deleted;
    String githublogin;
    String creation;
    String lastOnline;
    String lastExecute;
    String activeSince;
	
	//Constructor
    public User(final Map<String, Object> objectData) {
		super(objectData);
	}

}