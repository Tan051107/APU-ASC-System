package ui.controller;

import exceptions.DeleteException;
import models.User;
import services.UserService;
import ui.pages.Manager.forms.AddUserForm;
import ui.pages.ManagerMenu;
import utils.DialogUtil;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserManagementController {
    private final ManagerMenu managerMenu;
    private final UserService userService = new UserService();
    //Logger logger = Logger.getLogger(UserManagementController.class.getName());

    public UserManagementController(ManagerMenu managerMenu) {
        this.managerMenu = managerMenu;
        initListeners();
    }

    private void initListeners() {
        // Add New User
        managerMenu.addUser.addActionListener(e -> openAddUserForm(false, null));

        // Edit User Listener
        managerMenu.editUser.addActionListener(e -> {
            int selectedRow = managerMenu.userTable.getSelectedRow();
            
            if (selectedRow == -1) {
                DialogUtil.showWarningMessage("No Selection", "Please select a user from the table to edit.");
                return;
            }

            try {
                String targetUserId = managerMenu.userTable.getValueAt(selectedRow, 0).toString();
                User userToEdit = userService.getUserById(targetUserId); 

                if (userToEdit != null) {
                    openAddUserForm(true, userToEdit);
                } else {
                    DialogUtil.showInfoMessage("User Details Not Found!", "User details could not be found.");
                }
                
            } catch (Exception ex) {
                DialogUtil.showErrorMessage("Error", "Error loading user data: " + ex.getMessage());
                // logger.log(Level.SEVERE, ex.getMessage());
            }
        });

        managerMenu.deleteUser.addActionListener(e ->{
            int selectedRow = managerMenu.userTable.getSelectedRow();
            
            if (selectedRow == -1) {
                DialogUtil.showWarningMessage("No Selection", "Please select a user from the table to delete.");
                return;
            }

            try {
                String targetUserId = managerMenu.userTable.getValueAt(selectedRow, 0).toString();
                if (targetUserId.equals(managerMenu.getUser().getId())) {
                    DialogUtil.showWarningMessage("Action Denied", "You cannot delete yourself");
                    return;
                }
                User userToDelete = userService.getUserById(targetUserId); 
                if (userToDelete != null) {
                    deleteUser(userToDelete);
                } else {
                    DialogUtil.showInfoMessage("User Details Not Found!", "User details could not be found.");
                }
                
            } catch (Exception ex) {
                DialogUtil.showErrorMessage("Error", "Error loading user data: " + ex.getMessage());
                // logger.log(Level.SEVERE, ex.getMessage());
            }
        });
    }

    private void openAddUserForm(boolean isEdit, User userToEdit) {
        AddUserForm form = new AddUserForm(isEdit, userToEdit);
        new AddUserFormController(form);
        form.setVisible(true);
        // We might want to reload customers after the form is closed
        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                managerMenu.refreshUserTable();
            }
        });
    }

    private void deleteUser(User user){
        boolean confirmDeleteCustomer = DialogUtil.showConfirmationMessage("Confirm Delete?" , String.format("Are you sure you want to delete %s?" , user.getName()));
        if (confirmDeleteCustomer){
            try{
                userService.deleteUser(user.getId());
                DialogUtil.showInfoMessage("Deleted Successfully" , String.format("Successfully deleted %s." , user.getName()));
                managerMenu.refreshUserTable();
            } catch (DeleteException e){
                DialogUtil.showErrorMessage("Failed to Delete Customer" , e.getMessage());
            } catch (Exception e){
                DialogUtil.showErrorMessage("Failed to Delete Customer" , e.getMessage());
                // logger.log(Level.SEVERE, ex.getMessage());
            }
        }
    }

}
