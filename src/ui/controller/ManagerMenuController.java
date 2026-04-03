package ui.controller;
import services.UserService;
import services.ServicesService;

import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import exceptions.GetEntityListException;
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
}
