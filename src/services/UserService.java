package services;

import enums.UserType;
import exceptions.*;
import mapper.UserMapper;
import models.User;
import repositories.CrudRepository;
import utils.RandomIdGenerator;
import utils.validators.ValidationResult;
import utils.validators.Validator;

import java.util.List;
import java.util.logging.Logger;

public class UserService {
    private final String USER_FILE = "txt_files/User.txt";
    private final UserMapper userMapper = new UserMapper();
    private final CrudRepository<User> userRepository = new CrudRepository<>(USER_FILE , userMapper);
//    private final Logger logger = Logger.getLogger(UserService.class.getName());

    public List<User> getUsers() throws GetEntityListException {
        try{
            return userRepository.getAll();
        } catch (FileCorruptedException e) {
            throw new GetEntityListException(e.getMessage());
        }
    }

    public User getUserById(String id) throws GetEntityListException {
        try{
            return userRepository.getOne(id);
        } catch (FileCorruptedException e) {
            throw new GetEntityListException(e.getMessage());
        }
    }

    public User getUserByEmail(String email) throws GetEntityListException {
        try{
            List<User> users = userRepository.getAll(user -> user.getEmail().equalsIgnoreCase(email)).stream().toList();
            if(users.isEmpty()){
                return null;
            }
            return userRepository.getAll(user -> user.getEmail().equalsIgnoreCase(email)).getFirst();
        }
        catch (FileCorruptedException e){
            throw new GetEntityListException(e.getMessage());
        }
    }

    public void signUpUser(User userToSignUp) throws SignUpException {
        try {
            boolean userHasExisted = getUserByEmail(userToSignUp.getEmail()) !=null;
            if(userHasExisted){
                throw new SignUpException("Email is taken. Please select another email");
            }
            String userId = generateUserId();
            userToSignUp.setId(userId);
            userRepository.create(userToSignUp);
        } catch (Exception e) {
            throw new SignUpException(e.getMessage());

        }
    }

    public void updateUser(User userToUpdate) throws FileCorruptedException, NotFoundException,UpdateException {
        boolean userHasExisted = !userRepository.getAll(user -> user.getEmail().equalsIgnoreCase(userToUpdate.getEmail()) && !user.getId().equalsIgnoreCase(userToUpdate.getId())).isEmpty();
        if(userHasExisted){
            throw new UpdateException("Email is taken. Please select another email");
        }
        userRepository.update(userToUpdate);
    }

    public void deleteUser(String userId) throws DeleteException {
        userRepository.delete(userId);
    }

    private String generateUserId(){
        while(true){
            String userId = RandomIdGenerator.generateId("TP" , 6);
            try {
                if(getUserById(userId) == null){
                    return userId;
                }
            } catch (GetEntityListException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
