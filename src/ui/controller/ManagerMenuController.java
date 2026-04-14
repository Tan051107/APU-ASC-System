package ui.controller;
import services.UserService;
import ui.pages.Login;
import ui.pages.ManagerMenu;
import ui.pages.Manager.ViewFeedbackPanel;
import utils.DialogUtil;
import services.ServicesService;
import services.FeedbackService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import exceptions.NotFoundException;
import exceptions.UpdateException;
import models.User;
import models.Services;
import models.Feedback;

public class ManagerMenuController {
    private final UserService userService = new UserService();
    private final ServicesService servicesService = new ServicesService();
    private final FeedbackService feedbackService = new FeedbackService();
    private final ManagerMenu managerMenu;
    

    public ManagerMenuController(ManagerMenu managerMenu) {
        this.managerMenu = managerMenu;
    }

    public void initListeners(){
        managerMenu.viewFeedback.addActionListener(e -> {
            int selectedRow = managerMenu.feedbackTable.getSelectedRow();
            
            if (selectedRow == -1) {
                DialogUtil.showWarningMessage("No Selection", "Please select a feedback record to view.");
                return;
            }

            // Grab the Feedback ID from Column 0
            String feedbackId = managerMenu.feedbackTable.getValueAt(selectedRow, 0).toString();

            // Open the UI and let the Controller fetch the data
            ViewFeedbackPanel viewPanel = new ViewFeedbackPanel();
            new ViewFeedbackController(viewPanel, feedbackId);
            viewPanel.setVisible(true);
        });

        managerMenu.btnLogOut.addActionListener(e -> {
            managerMenu.dispose();
            new Login().createUI();
        });
    }

    public DefaultTableModel loadUserToTable() {
        String[] columns = {"User ID", "Name", "Role"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        
        try {
            List<User> userData = userService.getUsers();
            
            for (User user : userData) {
                if (!user.getUserType().getDisplayUserType().equals("Customer")) {
                    Object[] rowData = {
                        user.getId(),
                        user.getName(),
                        user.getUserType().getDisplayUserType()
                    };
                    tableModel.addRow(rowData);
                }
            }
            
        } catch (GetEntityListException e) {
            JOptionPane.showMessageDialog(
                null, 
                "Failed to load user data: " + e.getMessage(), 
                "Data Load Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
        return tableModel;
    }
    
    public DefaultTableModel loadServiceToTable() {
        String[] columns = {"Service ID", "Service Type", "Price"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        
        try {
            List<Services> servicesData = servicesService.getServices();
            
            for (Services services : servicesData) {
                String formattedPrice = String.format("RM %.2f", services.getPrice());
                Object[] rowData = {
                    services.getId(),
                    services.getName(),
                    formattedPrice
                };
                tableModel.addRow(rowData);
            }
            
        } catch (GetEntityListException e) {
            JOptionPane.showMessageDialog(
                null, 
                "Failed to load service data: " + e.getMessage(), 
                "Data Load Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
        return tableModel;
    }

    public Object[] loadServiceDetails(String serviceId) {
        try {
            // 1. Ask your service class to find the exact service directly!
            // No need to loop through the whole list.
            Services service = servicesService.getServicesById(serviceId);
            
            // 2. Check if the service was actually found
            if (service != null) {
                String formattedPrice = String.format("%.2f", service.getPrice());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String lastEdited = (service.getUpdatedAt() != null) 
                                    ? service.getUpdatedAt().format(formatter) 
                                    : "Never";
                // 3. Return a single array containing the data
                return new Object[] {
                    service.getId(),
                    service.getName(),
                    formattedPrice,
                    service.getDetails(),
                    lastEdited
                };
            }
            
        } catch (GetEntityListException e) {
            JOptionPane.showMessageDialog(
                null, 
                "Failed to load service data: " + e.getMessage(), 
                "Data Load Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        // 4. Return null if the service wasn't found or an error occurred
        return null; 
    }

    public boolean updateServicePrice(String serviceId, double newPrice) {
        try {
            // 1. Fetch the complete, existing service from the text file
            Services serviceToUpdate = servicesService.getServicesById(serviceId);

            if (serviceToUpdate == null) {
                JOptionPane.showMessageDialog(null, "Could not find the selected service in the database.", "Not Found", JOptionPane.WARNING_MESSAGE);
                return false; // Update failed
            }

            // 2. Update ONLY the price (the rest of the data stays exactly the same)
            serviceToUpdate.setPrice(newPrice);

            // 3. Send the modified object back to be saved
            servicesService.updateService(serviceToUpdate);

            return true; // Update successful!

        } catch (GetEntityListException | FileCorruptedException | NotFoundException | UpdateException | IOException e) {
            // Catch all the exceptions your backend throws and display them to the manager
            JOptionPane.showMessageDialog(
                null, 
                "Failed to update service price: " + e.getMessage(), 
                "Update Error", 
                JOptionPane.ERROR_MESSAGE
            );
            return false; // Update failed
        }
    }

    public DefaultTableModel loadFeedbackToTable() {
        String[] columns = {"Feedback ID", "Feedback Details", "Appointment ID"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        
        try {
            List<Feedback> feedbacksData = feedbackService.getFeedbacks();
            
            for (Feedback feedbacks : feedbacksData) {
                Object[] rowData = {
                    feedbacks.getId(),
                    feedbacks.getComment(),
                    feedbacks.getAppointmentId()
                };
                tableModel.addRow(rowData);
            }
            
        } catch (GetEntityListException e) {
            JOptionPane.showMessageDialog(
                null, 
                "Failed to load feedback data: " + e.getMessage(), 
                "Data Load Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
        return tableModel;
    }
}
