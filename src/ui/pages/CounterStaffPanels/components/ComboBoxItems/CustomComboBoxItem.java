package ui.pages.CounterStaffPanels.components.ComboBoxItems;

public class CustomComboBoxItem {
    private String id;
    private String name;

    public CustomComboBoxItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object)return true;
        if(!(object instanceof CustomComboBoxItem))return false;
        return this.id.equals(((CustomComboBoxItem) object).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String toString(){
        return id + name;
    }
}
