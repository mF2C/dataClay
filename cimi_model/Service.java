package CIMI;

import java.util.Map;

public class Service extends CIMIResource {

    private String exec;
    private String exec_type;
    private int[] exec_ports;
    private Map<String, Object> category;

    //Constructor, with a parameter for each attribute in this class and in CIMIResource
    public Service(String resourceID, String resourceName, String resourceDescription, String resourceURI
            , String exec, String exec_type, int[] exec_ports, Map<String, Object> category) {
        super(resourceID, resourceName, resourceDescription, resourceURI);
        this.exec = exec;
        this.exec_type = exec_type;
        this.exec_ports = exec_ports;
        this.category = category;
    }

    public String getExec() {
        return exec;
    }

    public void setExec(String exec) {
        this.exec = exec;
    }

    public String getExecType() {
        return exec_type;
    }

    public void setExecType(String exec_type) {
        this.exec_type = exec_type;
    }

    public int[] getExecPorts() {
        return exec_ports;
    }

    public void setExecPorts(int[] exec_ports) {
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
