package services;

import exceptions.BusinessRuleException;
import exceptions.GetEntityListException;
import models.User;

public class AuthService {

    private final UserService userService = new UserService();


    public User login(String email, String password) throws BusinessRuleException, GetEntityListException {
        User loginUser = userService.getUserByEmail(email);
        boolean userHasExisted = loginUser != null;
        if(!userHasExisted){
            throw new BusinessRuleException("Email not found");
        }
        if(!loginUser.getPassword().equals(password)) {
            throw new BusinessRuleException("Incorrect password");
        }
        return loginUser;
    }
}
