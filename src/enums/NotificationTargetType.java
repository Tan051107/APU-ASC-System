package enums;

public enum NotificationTargetType {
    ALL("All"),
    ROLE("Role"),
    USER("User");

    private final String displayNotificationTargetType;

    NotificationTargetType(String displayNotificationTargetType){
        this.displayNotificationTargetType = displayNotificationTargetType;
    }

    public String toString(){
        return displayNotificationTargetType;
    }

    public static NotificationTargetType fromString(String notificationTargetTypeString){
        for(NotificationTargetType notificationTargetType : NotificationTargetType.values()){
            if(notificationTargetType.displayNotificationTargetType.equalsIgnoreCase(notificationTargetTypeString)){
                return notificationTargetType;
            }
        }
        throw new IllegalArgumentException("Notification target type not found");
    }
}



