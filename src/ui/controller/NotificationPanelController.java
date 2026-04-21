package ui.controller;

import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import models.Notification;
import services.NotificationService;
import ui.pages.NotificationPanel;
import utils.DialogUtil;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationPanelController {

    private final NotificationPanel notificationPanel;
    private final String userId;
    Logger logger = Logger.getLogger(NotificationPanelController.class.getName());

    public NotificationPanelController(NotificationPanel notificationPanel , String userId){
        this.notificationPanel = notificationPanel;
        this.userId = userId;
        refreshNotifications();
    }

    public void refreshNotifications(){
        NotificationService notificationService = new NotificationService();
        try {
            notificationPanel.clearNotifications();
            List<Notification> notifications = notificationService.getNotifications(userId);
            if(!notifications.isEmpty()){
                for(Notification notification :notifications){
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
                    String appointmentDateTimeString = notification.getCreatedAt().format(dateTimeFormatter);
                    notificationPanel.addNotificationCard(
                            notification.getTitle(),
                            notification.getMessage(),
                            appointmentDateTimeString
                    );
                }
            }
        } catch (FileCorruptedException | GetEntityListException e) {
            logger.log(Level.SEVERE , e.getMessage());
            DialogUtil.showErrorMessage("Encountered Error" , "Failed to show notifications");
        }
    }
}
