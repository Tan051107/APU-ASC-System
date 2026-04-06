package ui.pages.CounterStaffPanels.components.ComboBoxItems;

public class TechnicianComboBoxItem {
    private String technicianId;
    private String technicianName;

    public TechnicianComboBoxItem(String technicianId, String technicianName) {
        this.technicianId = technicianId;
        this.technicianName = technicianName;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    @Override
    public String toString() {
        return technicianId + technicianName;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object)return true;
        if(!(object instanceof TechnicianComboBoxItem))return false;
        return this.technicianId.equals(((TechnicianComboBoxItem) object).technicianId);
    }

    @Override
    public int hashCode() {
        return technicianId.hashCode();
    }
}
