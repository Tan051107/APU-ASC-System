package ui.controller;
import services.UserService;
import services.ServicesService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import exceptions.NotFoundException;
import exceptions.UpdateException;
import models.User;
import models.Services;

public class ManagerMenuController {
    private final UserService userService = new UserService();
    private final ServicesService servicesService = new ServicesService();
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
                String formattedPrice = String.format("RM %.2f", services.getServicePrice());
                Object[] rowData = {
                    services.getId(),
                    services.getServiceName(),
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
                String formattedPrice = String.format("%.2f", service.getServicePrice());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String lastEdited = (service.getUpdatedAt() != null) 
                                    ? service.getUpdatedAt().format(formatter) 
                                    : "Never";
                // 3. Return a single array containing the data
                return new Object[] {
                    service.getId(),
                    service.getServiceName(),
                    formattedPrice,
                    service.getServiceDetails(),
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
            serviceToUpdate.setServicePrice(newPrice);

            // 3. Send the modified object back to be saved
            servicesService.updateService(serviceToUpdate);

            return true; // Update successful!

        } catch (GetEntityListException | FileCorruptedException | NotFoundException | UpdateException e) {
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
}
