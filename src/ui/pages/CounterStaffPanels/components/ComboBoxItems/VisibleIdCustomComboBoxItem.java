package ui.pages.CounterStaffPanels.components.ComboBoxItems;

public class VisibleIdCustomComboBoxItem {
    private String id;
    private String name;

    public VisibleIdCustomComboBoxItem(String id, String name) {
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
        if(!(object instanceof VisibleIdCustomComboBoxItem))return false;
        return this.id.equals(((VisibleIdCustomComboBoxItem) object).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String toString(){
        return id + name;
    }
}
