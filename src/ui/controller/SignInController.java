package ui.controller;

import models.User;
import services.AuthService;
import ui.pages.SignInPanel;

import javax.security.auth.login.LoginException;
import javax.swing.*;

public class SignInController {
    private final SignInPanel signInPanel;

    public SignInController(SignInPanel signInPanel){
        this.signInPanel = signInPanel;
        setUpController();
    }

    public void setUpController(){
        signInPanel.signInButton.addActionListener(e -> signIn());
    }

    public User signIn() {
        AuthService authService = new AuthService();
        String password = signInPanel.passwordField.getText();
        String email = signInPanel.emailField.getText();
        try{
            User user = authService.login(email,password);
            JOptionPane.showMessageDialog(
                    signInPanel,
                    "Welcome Back",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );
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
}
