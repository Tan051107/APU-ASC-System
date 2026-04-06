package ui.pages.CounterStaffPanels.components.ComboBoxItems;

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

    @Override
    public boolean equals(Object object) {
        if(this == object)return true;
        if(!(object instanceof ServiceComboBoxItem))return false;
        return this.id.equals(((ServiceComboBoxItem) object).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
