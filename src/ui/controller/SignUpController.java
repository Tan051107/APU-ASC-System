package ui.controller;

import enums.UserType;
import exceptions.GetUsersException;
import exceptions.SignUpException;
import model.Customer;
import service.CustomerService;
import ui.pages.SignUpPanel;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUpController {
    private final SignUpPanel signUpPanel;
    private final CustomerService customerService = new CustomerService();
    private final Logger logger = Logger.getLogger(SignUpController.class.getName());

    public SignUpController(SignUpPanel signUpPanel){
        this.signUpPanel = signUpPanel;
        setUpListener();
    }

    private void setUpListener(){
        signUpPanel.userTypeSelectionComboBox.addActionListener(e -> validateToShowCarModelAndCarPlateField());
        signUpPanel.completeSignUpButton.addActionListener(e -> signUpUser());

    }

    private void validateToShowCarModelAndCarPlateField(){
        int selectedUserType =signUpPanel.userTypeSelectionComboBox.getSelectedIndex();
        if(selectedUserType < 1){
            return;
        }
        System.out.println(selectedUserType);
        boolean showCarModelAndCarPlateField = selectedUserType == 4;
        if(showCarModelAndCarPlateField){
            signUpPanel.carModelLabel.setVisible(true);
            signUpPanel.carModelField.setVisible(true);
            signUpPanel.carModelBottomSpacing.setVisible(true);
            signUpPanel.carPlateLabel.setVisible(true);
            signUpPanel.carPlateField.setVisible(true);
            signUpPanel.carPlateBottomSpacing.setVisible(true);
        }
    }

    private void signUpUser(){
        Object selectedUserTypeItem = signUpPanel.userTypeSelectionComboBox.getSelectedItem();
        String selectedUserType = (selectedUserTypeItem == null) ? "" : selectedUserTypeItem.toString();
        String name = signUpPanel.nameField.getText();
        String email = signUpPanel.emailField.getText();
        String password = signUpPanel.passwordField.getText();
        String contactNumber = signUpPanel.phoneField.getText();
        UserType userType = UserType.fromString(selectedUserType);
        String carModel = signUpPanel.carModelField.getText();
        String carPlate =signUpPanel.carPlateField.getText();
        String confirmPassword = signUpPanel.confirmPasswordField.getText();
        switch (selectedUserType){
            case "Customer":
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(email);
                customer.setPassword(password);
                customer.setContactNumber(contactNumber);
                customer.setUserType(userType);
                customer.setCarModel(carModel);
                customer.setCarPlate(carPlate);
                try{
                    customerService.signUpCustomer(customer);
                    if(!confirmPassword.equals(password)){
                        throw new SignUpException("Please confirm password correctly");
                    }
                    JOptionPane.showMessageDialog(
                            signUpPanel,
                            "Successfully signed up a new account",
                            "Registration Completed",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
                catch (GetUsersException | SignUpException e){
                    JOptionPane.showMessageDialog(
                            signUpPanel,
                            e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (Exception e) {
                    logger.log(Level.SEVERE,e.getMessage());
                }
                break;
            case "Manager":
                break;
            case "Counter Staff":
                break;
            case "Technician":
                break;
            default:
                JOptionPane.showMessageDialog(
                        signUpPanel,
                        "Please select a valid user type",
                        "Invalid user type",
                        JOptionPane.WARNING_MESSAGE
                );
        }

    }
}
