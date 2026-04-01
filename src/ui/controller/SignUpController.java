package ui.controller;
import dto.AddCustomerDto;
import enums.UserType;
import exceptions.SignUpException;
import dto.AddCarDto;
import models.User;
import services.CustomerService;
import ui.pages.SignUpPanel;

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
        List<AddCarDto> cars = signUpPanel.getCarData();
        cars.removeIf(car->car.getCarPlate().trim().isEmpty() || car.getCarModel().trim().isEmpty());
        AddCustomerDto addCustomerDto = new AddCustomerDto(user , cars);
        customerService.addCustomer(addCustomerDto);
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
            JOptionPane.showMessageDialog(
                    signUpPanel,
                    "Please confirm password correctly",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setContactNumber(contactNumber);
        try{
            switch (selectedUserType){
                case "Customer":
                    user.setUserType(UserType.CUSTOMER);
                    handleCustomerSignUp(user);
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
            JOptionPane.showMessageDialog(
                    signUpPanel,
                    "Successfully registered",
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
        catch (SignUpException e) {
            JOptionPane.showMessageDialog(
                    signUpPanel,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception e) {
            logger.log(Level.SEVERE,e.getMessage());
        }


    }
}
