package service;

import exceptions.DeleteUserException;
import exceptions.GetUsersException;
import exceptions.SignUpException;
import model.User;
import validator.ValidationResult;
import validator.Validator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final String USER_FILE = "txt_files/User.txt";

    public List<User> getUsers() throws GetUsersException {
        List<User> users = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))){
            String line;
            while((line = reader.readLine()) != null ){
                String [] data = line.split("\\|");
                if(data.length < 6){
                    throw new GetUsersException("User record contains missing data");
                }
                User user = new User(data);
                users.add(user);
            }
            return users;
        } catch (FileNotFoundException e) {
            throw new GetUsersException("User file not found");
        } catch (IOException e) {
            throw new GetUsersException("Failed to read user file");
        }
    }

    public User getUserById(String id) throws GetUsersException {
        List<User> users = getUsers();

        return users.stream()
                .filter(user -> user.getId().equalsIgnoreCase(id))
                .findAny()
                .orElse(null);
    }

    public User getUserByEmail(String email) throws GetUsersException {
        List<User> users = getUsers();
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findAny()
                .orElse(null);
    }

    public void signUpUser(User userToSignUp){
        boolean userHasExisted = getUserById(userToSignUp.getId()) != null;
        if(userHasExisted){
            throw new SignUpException("User has existed. Please select another user id to sign up user");
        }
        try {
            ValidationResult validationResult = new ValidationResult();
            Validator.required(validationResult, "Name", userToSignUp.getName());
            Validator.validateEmail(validationResult, userToSignUp.getEmail());
            Validator.validatePassword(validationResult,"Password" , userToSignUp.getPassword());
            Validator.validatePhone(validationResult , userToSignUp.getContactNumber());
            if(validationResult.hasError()){
                throw new SignUpException(validationResult.getErrors());
            }
            System.out.println(new File(USER_FILE).getAbsolutePath());
            try (PrintWriter userFileWriter = new PrintWriter(new BufferedWriter(new FileWriter(USER_FILE)))) {
                String signUpUserString = userToSignUp.toString();
                userFileWriter.println(signUpUserString);
            }
        } catch (IOException e) {
            throw new SignUpException("Failed to write to user file");
        }
    }

    public void deleteUser(String userId){
        List<User> users = getUsers();
        boolean hasRemovedUser = users.removeIf(user -> user.getId().equalsIgnoreCase(userId));
        if(!hasRemovedUser){
            throw new DeleteUserException("User id not found in user file");
        }
        try{
            PrintWriter userFileWriter = new PrintWriter(new BufferedWriter(new FileWriter(USER_FILE)));
            for(User user: users){
                String userString = user.toString();
                userFileWriter.println(userString);
            }
        } catch (IOException e) {
            throw new DeleteUserException("Failed to write to user file");
        }
    }
}
