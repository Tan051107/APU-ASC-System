package ui.controller;

import models.User;
import services.AuthService;
import ui.pages.Login;
import ui.pages.CounterStaffMenu;
import ui.pages.TechnicianMenu;
import ui.pages.ManagerMenu;
import utils.validators.ValidationResult;
import utils.validators.Validator;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.*;

public class SignInController {
    private final Login loginPage;

    public SignInController(Login loginPage){
        this.loginPage = loginPage;
        setUpController();
    }

    public void setUpController(){

        loginPage.signInButton.addActionListener(e -> signIn());
        loginPage.demoTechnicianButton.addActionListener(e -> demoTechnicianLogin());
        loginPage.demoCustomerButton.addActionListener(e->demoCustomerLogin());
        loginPage.demoManagerButton.addActionListener(e->demoManagerLogin());
        loginPage.demoCounterStaffButton.addActionListener(e->demoCounterStaffLogin());
    }

    public void signIn() {
        AuthService authService = new AuthService();
        char[] passwordChars = loginPage.passwordField.getPassword();
        String password = new String(passwordChars);
        String email = loginPage.emailField.getText();
        try{
            ValidationResult validationResult = new ValidationResult();
            Validator.validateEmail(validationResult,email);
            Validator.validatePassword(validationResult,"Password" ,password);
            if(validationResult.hasError()){
                throw new LoginException(validationResult.getErrors());
            }
            User user = authService.login(email,password);
            routeUser(user);
        } catch (LoginException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void demoManagerLogin(){
        loginPage.emailField.setText("Jacob@gmail.com");
        loginPage.passwordField.setText("12345678");
        loginPage.emailField.setForeground(Color.BLACK);
        loginPage.passwordField.setForeground(Color.BLACK);
    }
    public void demoCustomerLogin(){
        loginPage.emailField.setText("Ben@gmail.com");
        loginPage.passwordField.setText("12345678");
        loginPage.emailField.setForeground(Color.BLACK);
        loginPage.passwordField.setForeground(Color.BLACK);
    }
    public void demoTechnicianLogin(){
        loginPage.emailField.setText("Smith@gmail.com");
        loginPage.passwordField.setText("12345678");
        loginPage.emailField.setForeground(Color.BLACK);
        loginPage.passwordField.setForeground(Color.BLACK);
    }
    public void demoCounterStaffLogin(){
        loginPage.emailField.setText("Abu@gmail.com");
        loginPage.passwordField.setText("12345678");
        loginPage.emailField.setForeground(Color.BLACK);
        loginPage.passwordField.setForeground(Color.BLACK);
    }

    private void routeUser(User user) {
        switch (user.getUserType()){
            case CUSTOMER:
                // CustomerMenu customerMenu = new CustomerMenu(user.getId());
                // customerMenu.setVisible(true);
                break;
            case MANAGER:
                ManagerMenu managerMenu = new ManagerMenu();
                managerMenu.setVisible(true);
                break;
            case COUNTER_STAFF:
                loginPage.dispose();
                new CounterStaffMenu(user).setVisible(true);
                break;
            case TECHNICIAN:
                TechnicianMenu techMenu = new TechnicianMenu(user);
                techMenu.setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(
                        null,
                        "User Role not found",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                );
        }
        // Close window
        loginPage.dispose();
    }
}
