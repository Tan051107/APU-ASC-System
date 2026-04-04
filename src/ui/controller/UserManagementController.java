package ui.controller;

import exceptions.DeleteException;
import exceptions.FileCorruptedException;
import models.User;
import services.UserService;
import ui.pages.Manager.forms.AddUserForm;
import ui.controller.AddUserFormController;
import ui.pages.ManagerMenu;
import utils.DialogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserManagementController {
    private final ManagerMenu managerMenu;
    //private final UserService userService = new UserService();
    //Logger logger = Logger.getLogger(UserManagementController.class.getName());

    public UserManagementController(ManagerMenu managerMenu) {
        this.managerMenu = managerMenu;
        initListeners();
    }

    private void initListeners() {
        managerMenu.addUser.addActionListener(e -> openAddUserForm(false, null));
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
}
