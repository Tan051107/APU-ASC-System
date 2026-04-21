package enums;

public enum UserType {
    MANAGER("Manager"),
    COUNTER_STAFF("Counter Staff"),
    TECHNICIAN ("Technician"),
    CUSTOMER("Customer"),
    SUPER_MANAGER("Super Manager");

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
