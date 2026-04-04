package ui.ComboBoxItems;

public class ServiceComboBoxItem {
    private String id;
    private String serviceName;

    public ServiceComboBoxItem(String id, String serviceName) {
        this.id = id;
        this.serviceName = serviceName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString(){
        return serviceName;
    }

}
