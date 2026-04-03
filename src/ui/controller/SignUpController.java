package ui.controller;
import dto.AddCustomerDto;
import enums.UserType;
import exceptions.SignUpException;
import dto.AddCarDto;
import models.User;
import services.CustomerService;
import ui.pages.SignUpPanel;
import utils.DialogUtil;

import javax.swing.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUpController {
    private final SignUpPanel signUpPanel;
    private final CustomerService customerService = new CustomerService();
    private final Logger logger = Logger.getLogger(SignUpController.class.getName());

    public SignUpController(SignUpPanel signUpPanel){
        this.signUpPanel = signUpPanel;
        initListeners();
    }

    private void initListeners() {
        signUpPanel.userTypeSelectionComboBox.addActionListener(e -> validateToShowCarModelAndCarPlateField());
        signUpPanel.completeSignUpButton.addActionListener(e -> signUpUser());
    }

    private void handleCustomerSignUp(User user) throws SignUpException {
        customerService.addCustomer(user);
    }

    private void validateToShowCarModelAndCarPlateField(){
        signUpPanel.updateAddCarButtonVisibility();
        int selectedUserType = signUpPanel.userTypeSelectionComboBox.getSelectedIndex();
        signUpPanel.carFieldsContainer.setVisible(selectedUserType == 4);
    }

    private void signUpUser(){
        Object selectedUserTypeItem = signUpPanel.userTypeSelectionComboBox.getSelectedItem();
        String selectedUserType = (selectedUserTypeItem == null) ? "" : selectedUserTypeItem.toString();
        String name = signUpPanel.nameField.getText();
        String email = signUpPanel.emailField.getText();
        String password = signUpPanel.passwordField.getText();
        String contactNumber = signUpPanel.phoneField.getText();
        String confirmPassword = signUpPanel.confirmPasswordField.getText();
        if(!confirmPassword.equals(password)){
            DialogUtil.showErrorMessage("Sign Up Error" ,"Please confirm password correctly" );
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setContactNumber(contactNumber);
        try{
            switch (selectedUserType){
                case "Customer":
                    handleCustomerSignUp(user);
                    break;
                case "Manager":
                    break;
                case "Counter Staff":
                    break;
                case "Technician":
                    break;
                default:
                    DialogUtil.showWarningMessage("Invalid User Type" , "Please select a valid user type");
            }
            DialogUtil.showInfoMessage("Registration Successful" ,"Successfully Registered" );
        }
        catch (SignUpException e) {
            DialogUtil.showErrorMessage("Sign Up Error" , e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
    }
}
