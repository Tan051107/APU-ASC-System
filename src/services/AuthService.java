package services;

import exceptions.GetEntityListException;
import models.User;
import validators.ValidationResult;
import validators.Validator;

import javax.security.auth.login.LoginException;

public class AuthService {

    private final UserService userService = new UserService();


    public User login(String email, String password) throws LoginException{
        ValidationResult validationResult = new ValidationResult();
        Validator.validateEmail(validationResult,email);
        Validator.validatePassword(validationResult,"Password" ,password);
        if(validationResult.hasError()){
            throw new LoginException(validationResult.getErrors());
        }
        try{
            User loginUser = userService.getUserByEmail(email);
            boolean userHasExisted = loginUser != null;
            if(!userHasExisted){
                throw new LoginException("Email not found");
            }
            if(!loginUser.getPassword().equals(password)) {
                throw new LoginException("Incorrect password");
            }
            return loginUser;
        }
        catch(GetEntityListException e){
            throw new LoginException(e.getMessage());
        }
    }
}
