package ui.controller;

import models.User;
import services.AuthService;
import ui.pages.Login;
import ui.pages.CounterStaffMenu;
import ui.pages.managermenu;

import javax.security.auth.login.LoginException;
import javax.swing.*;

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

    public User signIn() {
        AuthService authService = new AuthService();
        String password = loginPage.passwordField.getText();
        String email = loginPage.emailField.getText();
        try{
            User user = authService.login(email,password);
            switch (user.getUserType()){
                case CUSTOMER:
                    break;
                case MANAGER:
                    loginPage.dispose();
                    new managermenu().setVisible(true);
                    break;
                case COUNTER_STAFF:
                    loginPage.dispose();
                    new CounterStaffMenu().setVisible(true);
                    break;
                case TECHNICIAN:
                    break;
                default:
                    JOptionPane.showMessageDialog(
                    null,
                    "User Role not found",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
                    );
            }
            return user;
        } catch (LoginException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return null;
    }

    public void demoManagerLogin(){
        loginPage.emailField.setText("Jacob@gmail.com");
        loginPage.passwordField.setText("12345678");
    }
    public void demoCustomerLogin(){
        loginPage.emailField.setText("Ben@gmail.com");
        loginPage.passwordField.setText("12345678");
    }
    public void demoTechnicianLogin(){
        loginPage.emailField.setText("Smith@gmail.com");
        loginPage.passwordField.setText("12345678");
    }
    public void demoCounterStaffLogin(){
        loginPage.emailField.setText("Abu@gmail.com");
        loginPage.passwordField.setText("12345678");
    }
}
