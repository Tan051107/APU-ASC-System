package ui.pages.CounterStaffPanels.components.ComboBoxItems;

public class CustomerComboBoxItem {
    private String customerId;
    private String customerName;

    public CustomerComboBoxItem(String customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString(){
        return customerId + customerName;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object)return true;
        if(!(object instanceof CustomerComboBoxItem))return false;
        return this.customerId.equals(((CustomerComboBoxItem) object).customerId);
    }

    @Override
    public int hashCode() {
        return customerId.hashCode();
    }
}
