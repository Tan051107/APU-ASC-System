package ui.controller;

import exceptions.FileCorruptedException;
import exceptions.NotFoundException;
import exceptions.UpdateException;
import models.User;
import services.UserService;
import ui.pages.ProfilePanel;
import utils.DialogUtil;
import utils.validators.ValidationResult;
import utils.validators.Validator;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfilePanelController {
    private final ProfilePanel profilePanel;
    private final User loginUser;
    Logger logger = Logger.getLogger(ProfilePanelController.class.getName());

    public ProfilePanelController(ProfilePanel profilePanel , User loginUser){
        this.profilePanel = profilePanel;
        this.loginUser = loginUser;
        initPanel();


    }

    public void initPanel(){
        initProfile();
        profilePanel.updateButton.addActionListener(e->updateProfile());
    }

    public void initProfile(){
        if (loginUser == null) {
            return;
        }
        profilePanel.profileNameField.setText(loginUser.getName());
        profilePanel.profileEmailField.setText(loginUser.getEmail());
        profilePanel.profilePhoneField.setText(loginUser.getContactNumber());
    }

    private void updateProfile(){
        if (loginUser == null) {
            DialogUtil.showWarningMessage("Encountered Error", "Unable to load user profile.");
            return;
        }
        UserService userService = new UserService();
        ValidationResult validationResult = new ValidationResult();
        String name = profilePanel.profileNameField.getText();
        String email = profilePanel.profileEmailField.getText();
        String phoneNumber = profilePanel.profilePhoneField.getText();
        Validator.required(validationResult,"Name",name);
        Validator.validateEmail(validationResult,email);
        Validator.validatePhone(validationResult,phoneNumber);
        if(validationResult.hasError()){
            DialogUtil.showWarningMessage("Validation Error" , validationResult.getErrors());
        }
        else{
            loginUser.setName(name);
            loginUser.setContactNumber(phoneNumber);
            loginUser.setEmail(email);
            try {
                userService.updateUser(loginUser);
                DialogUtil.showInfoMessage("Updated Successfully" , "Successfully updated user details");
                profilePanel.profileNameField.setText(name);
                profilePanel.profileEmailField.setText(email);
                profilePanel.profilePhoneField.setText(phoneNumber);
            } catch (FileCorruptedException e) {
                logger.log(Level.SEVERE,e.getMessage());
            } catch (NotFoundException | UpdateException e) {
                DialogUtil.showWarningMessage("Encountered Error" , e.getMessage());
            }
        }
    }
}
