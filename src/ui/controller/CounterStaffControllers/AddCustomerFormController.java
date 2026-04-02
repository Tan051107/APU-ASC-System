package ui.controller.CounterStaffControllers;

import enums.UserType;
import exceptions.*;
import models.Customer;
import models.User;
import services.UserService;
import ui.pages.CounterStaffPanels.forms.AddCustomerForm;
import utils.DialogUtil;
import utils.validators.ValidationResult;
import utils.validators.Validator;

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
        String password = form.passwordField.getText();
        String confirmPassword = form.confirmPasswordField.getText();

        if (!confirmPassword.equals(password)) {
            DialogUtil.showWarningMessage("Validation Error" , "Passwords do not match");
            return;
        }
        try {
            ValidationResult validationResult = new ValidationResult();
            User user = getUser(validationResult);
            Validator.validatePassword(validationResult,"Password" , password);
            if(validationResult.hasError()){
                throw new ValidationException(validationResult.getErrors());
            }
            user.setPassword(password);
            userService.signUpUser(user);
            DialogUtil.showInfoMessage("Added Successfully" , String.format("Successfully added %s." , name));
            form.dispose();
        } catch (SignUpException | ValidationException e) {
            DialogUtil.showWarningMessage("Validation Error" , e.getMessage());
        }
    }

    private void updateCustomer(){
        try {
            ValidationResult validationResult = new ValidationResult();
            User user = getUser(validationResult);
            Customer customerToEdit = form.getCustomerToEdit();
            user.setId(customerToEdit.getId());
            user.setCreatedAt(customerToEdit.getCreatedAt());
            Validator.validatePassword(validationResult,"Password" , customerToEdit.getPassword());
            if(validationResult.hasError()){
                throw new ValidationException(validationResult.getErrors());
            }
            user.setPassword(customerToEdit.getPassword());
            userService.updateUser(user);
            DialogUtil.showInfoMessage("Updated Successfully" , String.format("Successfully updated %s." , user.getName()));
            form.dispose();
        }
        catch (NotFoundException | ValidationException | UpdateException e ) {
            DialogUtil.showErrorMessage("Failed to Update Customer" , e.getMessage());
        }
        catch (Exception e) {
            DialogUtil.showErrorMessage("Failed to Update Customer" , "Encountered error when updating customer");
            logger.log(Level.SEVERE ,e.getMessage());
        }
    }


    private User getUser(ValidationResult validationResult) throws ValidationException {
        String name = form.nameField.getText();
        String email = form.emailField.getText();
        String contactNumber = form.phoneField.getText();
        Validator.required(validationResult, "Name", name);
        Validator.validateEmail(validationResult, email);
        Validator.validatePhone(validationResult , contactNumber);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setContactNumber(contactNumber);
        user.setUserType(UserType.CUSTOMER);
        return user;
    }
}
