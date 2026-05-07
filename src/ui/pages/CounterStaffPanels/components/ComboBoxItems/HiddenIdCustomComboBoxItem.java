package ui.pages.CounterStaffPanels.components.ComboBoxItems;

public class HiddenIdCustomComboBoxItem extends VisibleIdCustomComboBoxItem {
    public HiddenIdCustomComboBoxItem(String id, String name) {
        super(id, name);
    }

    public String toString(){
        return getName();
    }
}
