package ui.controller;
import services.UserService;

import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import exceptions.GetEntityListException;
import models.User;

public class ManagerMenuController {
    private final UserService userService = new UserService();
    public DefaultTableModel loadUserToTable() {
        String[] columns = {"User ID", "Name", "Role"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        
        try {
            List<User> userData = userService.getUsers();
            
            for (User user : userData) {
                System.out.println(user.getUserType());
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
    
}
