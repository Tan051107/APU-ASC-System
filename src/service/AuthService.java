package service;

import exceptions.GetUsersException;
import model.User;
import validator.ValidationResult;
import validator.Validator;

import javax.security.auth.login.LoginException;

public class AuthService {

    private final UserService userService = new UserService();

    public User login(String email, String password) throws LoginException, GetUsersException {
        ValidationResult validationResult = new ValidationResult();
        Validator.validateEmail(validationResult,email);
        Validator.validatePassword(validationResult,"Password" ,password);
        if(validationResult.hasError()){
            throw new LoginException(validationResult.getErrors());
        }
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
}
