package ui.controller.CounterStaffControllers;

import enums.UserType;
import exceptions.NotFoundException;
import exceptions.SignUpException;
import models.Customer;
import models.User;
import services.UserService;
import ui.pages.CounterStaffPanels.forms.AddCustomerForm;
import utils.DialogUtil;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddCustomerFormController {
    private final AddCustomerForm form;
    private final UserService userService = new UserService();
    private final Logger logger = Logger.getLogger(AddCustomerFormController.class.getName());

    public AddCustomerFormController(AddCustomerForm form) {
        this.form = form;
        initListeners();
    }

    private void initListeners() {
        form.addCustomerButton.addActionListener(e -> {
            if(form.isEdit()){
                updateCustomer();
            }
            else{
                addCustomer();
            }
        });
        setUpForm();
    }

    private void setUpForm(){
        if(form.isEdit() && form.getCustomerToEdit() !=null){
            form.nameField.setText(form.getCustomerToEdit().getName());
            form.nameField.setForeground(Color.BLACK);
            form.emailField.setText(form.getCustomerToEdit().getEmail());
            form.emailField.setForeground(Color.BLACK);
            form.passwordField.setText(form.getCustomerToEdit().getPassword());
            form.passwordField.setForeground(Color.BLACK);
            form.phoneField.setText(form.getCustomerToEdit().getContactNumber());
            form.phoneField.setForeground(Color.BLACK);
            form.confirmPasswordField.setText(form.getCustomerToEdit().getPassword());
            form.confirmPasswordField.setForeground(Color.BLACK);
        }
    }

    private void addCustomer() {
        String name = form.nameField.getText();
        String email = form.emailField.getText();
        String password = form.passwordField.getText();
        String contactNumber = form.phoneField.getText();
        String confirmPassword = form.confirmPasswordField.getText();

        if (!confirmPassword.equals(password)) {
            DialogUtil.showWarningMessage("Validation Error" , "Passwords do not match");
            return;
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setContactNumber(contactNumber);
        user.setUserType(UserType.CUSTOMER);
        try {
            userService.signUpUser(user);
            form.dispose();
            DialogUtil.showInfoMessage("Added Successfully" , String.format("Successfully added %s." , name));
        } catch (SignUpException e) {
            DialogUtil.showWarningMessage("Validation Error" , e.getMessage());
        }
    }

    private void updateCustomer(){
        Customer customerToEdit = form.getCustomerToEdit();
        String name = form.nameField.getText();
        String email = form.emailField.getText();
        String contactNumber = form.phoneField.getText();
        User user = new User();
        user.setId(customerToEdit.getId());
        user.setName(name);
        user.setEmail(email);
        user.setPassword(customerToEdit.getPassword());
        user.setContactNumber(contactNumber);
        user.setUserType(UserType.CUSTOMER);
        user.setCreatedAt(customerToEdit.getCreatedAt());
        try {
            userService.updateUser(user);
            form.dispose();
            DialogUtil.showInfoMessage("Updated Successfully" , "Successfully updated customer");
        }
        catch (NotFoundException e) {
            DialogUtil.showErrorMessage("Failed to Update Customer" , e.getMessage());
        }
        catch (Exception e) {
            DialogUtil.showErrorMessage("Failed to Update Customer" , "Encountered error when updating customer");
            logger.log(Level.SEVERE ,e.getMessage());
        }
    }
}
