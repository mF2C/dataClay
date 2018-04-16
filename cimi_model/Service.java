package CIMI;

import java.util.Map;

public class Service extends CIMIResource {

    private String exec;
    private String exec_type;
    private int[] exec_ports;
    private Map<String, Object> category;

    //Constructor, with a parameter for each attribute in this class and in CIMIResource
    public Service(String exec, String exec_type, int[] exec_ports, Map<String, Object> category,
                  String resourceID, String resourceName, String resourceDescription, String resourceURI) {
        super(resourceID, resourceName, resourceDescription, resourceURI);
        this.exec = exec;
        this.exec_type = exec_type;
        this.exec_ports = exec_ports;
        this.category = category;
    }

    public String get_exec() {
        return exec;
    }

    public void set_exec(String exec) {
        this.exec = exec;
    }

    public String get_exec_type() {
        return exec_type;
    }

    public void set_exec_type(String exec_type) {
        this.exec_type = exec_type;
    }

    public int[] get_exec_ports() {
        return exec_ports;
    }

    public void set_exec_ports(int[] exec_ports) {
        this.exec_ports = exec_ports;
    }

    //Setters (a setter for each property called "set_propertyname")
    public void set_category(Map<String, Object> newCategory) {
        this.category = newCategory;
    }

    //A single getter that returns a Map with all the info in this class and in CIMIResource, called "get_classname_info"
    public Map<String, Object> get_Service_info() {
        Map<String, Object> info = getCIMIResourceInfo();
        info.put("category", this.category);
        return info;
    }

}
