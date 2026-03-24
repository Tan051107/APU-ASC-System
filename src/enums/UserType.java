package enums;

public enum UserType {
    Manager("Manager"),
    CounterStaff("Counter Staff"),
    Technician ("Technician"),
    Customer("Customer");

    private final String displayUserType;

    UserType(String displayUserType){
        this.displayUserType = displayUserType;
    }

    public String getDisplayUserType() {
        return displayUserType;
    }

    public static UserType fromString(String userTypeString){
        for (UserType userType : UserType.values()){
            if(userType.displayUserType.equalsIgnoreCase(userTypeString)){
                return userType;
            }
        }
        throw new IllegalArgumentException("Invalid user type selected");
    }
}
