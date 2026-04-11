package ui.controller;

import enums.UserType;
import exceptions.*;
import models.User;
import services.UserService;
import ui.pages.Manager.forms.AddUserForm;
import utils.DialogUtil;
import utils.validators.ValidationResult;
import utils.validators.Validator;

import java.awt.*;


public class AddUserFormController {
    private final AddUserForm form;
    private final UserService userService = new UserService();
    //private final Logger logger = Logger.getLogger(AddUserFormController.class.getName());

    public AddUserFormController(AddUserForm form) {
        this.form = form;
        initListeners();
    }

    private void initListeners() {
        form.addUserButton.addActionListener(e -> {
            if(form.isEdit()){
                updateUser();
            }
            else{
                addUser();
            }
        });
        setUpForm();
    }

    private void setUpForm(){
        if(form.isEdit() && form.getUserToEdit() !=null){
            form.nameField.setText(form.getUserToEdit().getName());
            form.nameField.setForeground(Color.BLACK);
            form.emailField.setText(form.getUserToEdit().getEmail());
            form.emailField.setForeground(Color.BLACK);
            form.passwordField.setText(form.getUserToEdit().getPassword());
            form.passwordField.setForeground(Color.BLACK);
            form.phoneField.setText(form.getUserToEdit().getContactNumber());
            form.phoneField.setForeground(Color.BLACK);
            form.confirmPasswordField.setText(form.getUserToEdit().getPassword());
            form.confirmPasswordField.setForeground(Color.BLACK);
            form.roleComboBox.setSelectedItem(form.getUserToEdit().getUserType());
        }
    }

    private void addUser() {
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

    private void updateUser(){
        try {
            ValidationResult validationResult = new ValidationResult();
            User user = getUser(validationResult);
            User userToEdit = form.getUserToEdit();
            user.setId(userToEdit.getId());
            user.setCreatedAt(userToEdit.getCreatedAt());
            Validator.validatePassword(validationResult,"Password" , userToEdit.getPassword());
            if(validationResult.hasError()){
                throw new ValidationException(validationResult.getErrors());
            }
            user.setPassword(userToEdit.getPassword());
            userService.updateUser(user);
            DialogUtil.showInfoMessage("Updated Successfully" , String.format("Successfully updated %s." , user.getName()));
            form.dispose();
        }
        catch (NotFoundException | ValidationException | UpdateException e ) {
            DialogUtil.showErrorMessage("Failed to Update Customer" , e.getMessage());
        }
        catch (Exception e) {
            DialogUtil.showErrorMessage("Failed to Update Customer" , "Encountered error when updating customer");
            //logger.log(Level.SEVERE ,e.getMessage());
        }
    }


    private User getUser(ValidationResult validationResult) throws ValidationException {
        String name = form.nameField.getText();
        String email = form.emailField.getText();
        String contactNumber = form.phoneField.getText();
        String role = form.roleComboBox.getSelectedItem().toString();
        Validator.required(validationResult, "Name", name);
        Validator.validateEmail(validationResult, email);
        Validator.validatePhone(validationResult , contactNumber);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setContactNumber(contactNumber);
        if (role.equals("Manager")){
            user.setUserType(UserType.MANAGER);
        } else if (role.equals("Counter Staff")){
            user.setUserType(UserType.COUNTER_STAFF);
        } else if (role.equals("Technician")){
            user.setUserType(UserType.TECHNICIAN);
        }
        return user;
    }
}
