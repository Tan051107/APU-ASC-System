package models;

import enums.NotificationTargetType;
import enums.UserType;

import java.time.LocalDateTime;

public class Notification extends BaseModel{

    private String title;
    private String message;
    private NotificationTargetType targetType;
    private UserType userType;
    private String userId;

    public Notification(String title, String message, NotificationTargetType targetType, UserType userType, String userId) {
        this.title = title;
        this.message = message;
        this.targetType = targetType;
        this.userType = userType;
        this.userId = userId;
    }

    public Notification(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String title, String message, NotificationTargetType targetType, UserType userType, String userId) {
        super(id, createdAt, updatedAt);
        this.title = title;
        this.message = message;
        this.targetType = targetType;
        this.userType = userType;
        this.userId = userId;
    }

    public Notification() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationTargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(NotificationTargetType targetType) {
        this.targetType = targetType;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
