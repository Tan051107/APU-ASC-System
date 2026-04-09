package ui.pages.CounterStaffPanels.components.ComboBoxItems;

public class ServiceComboBoxItem extends CustomComboBoxItem{
    public ServiceComboBoxItem(String id, String name) {
        super(id, name);
    }

    public String toString(){
        return getName();
    }
}
