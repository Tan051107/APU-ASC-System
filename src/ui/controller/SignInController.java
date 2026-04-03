package ui.controller;

import models.User;
import services.AuthService;
import ui.pages.SignInPanel;
import ui.pages.TechnicianMenu;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.*;

public class SignInController {
    private final SignInPanel signInPanel;

    public SignInController(SignInPanel signInPanel){
        this.signInPanel = signInPanel;
        setUpController();
    }

    public void setUpController(){
        signInPanel.signInButton.addActionListener(e -> {
            User loggedInUser = signIn();
            
            if (loggedInUser != null) {
                routeUser(loggedInUser);
            }
        });
    }

    public User signIn() {
        AuthService authService = new AuthService();
        String password = signInPanel.passwordField.getText();
        String email = signInPanel.emailField.getText();
        try{
            User user = authService.login(email,password);
            /* JOptionPane.showMessageDialog(
                    signInPanel,
                    "Welcome Back",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE
            ); */
            return user;
        } catch (LoginException e) {
            JOptionPane.showMessageDialog(
                    signInPanel,
                    e.getMessage(),
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return null;
    }

    private void routeUser(User user) {
        String role = user.getUserType().getDisplayUserType(); 

        // Open dashboard based on the role
        if (role.equalsIgnoreCase("Technician")) {
            
            TechnicianMenu techMenu = new TechnicianMenu(user);
            techMenu.setVisible(true);
            
        } else if (role.equalsIgnoreCase("Manager")) {
            // ManagerMenu managerMenu = new AdminMenu(user.getId());
            // managerMenu.setVisible(true);
            
        } else if (role.equalsIgnoreCase("Customer")) {
            // CustomerMenu customerMenu = new CustomerMenu(user.getId());
            // customerMenu.setVisible(true);
        }

        // Close window
        Window loginWindow = SwingUtilities.getWindowAncestor(signInPanel);
        if (loginWindow != null) {
            loginWindow.dispose();
        }
    }
}
