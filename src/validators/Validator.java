package validators;

import java.util.List;

public class Validator {

    public static void required(ValidationResult result, String fieldName,String value){
        if(value == null || value.trim().isEmpty()){
            String errorMessage = fieldName + " is required.";
            result.addError(errorMessage);
        }
    }

    public static void validatePassword(ValidationResult result ,String fieldName ,String value){
        if(value == null || value.trim().isEmpty()){
            String errorMessage = fieldName + " is required.";
            result.addError(errorMessage);
            return;
        }
        if(value.length() < 8){
            String errorMessage = fieldName + " must have at least 8 character";
            result.addError(errorMessage);
        }
    }

    public static void validateEmail(ValidationResult result , String value){
        if(value == null || value.trim().isEmpty()){
            String errorMessage = "Email" + " is required.";
            result.addError(errorMessage);
            return;
        }
        if (!value.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            result.addError("Invalid email format");
        }
    }

    public static void validatePhone(ValidationResult result , String value){
        if(value == null || value.trim().isEmpty()){
            String errorMessage = "Phone Number" + " is required.";
            result.addError(errorMessage);
            return;
        }
        try{
            Integer.parseInt(value);
        }
        catch(NumberFormatException e){
            result.addError("Invalid phone number");
            return;
        }
        if(value.trim().length() != 10){
            result.addError("Phone number length must be 10");
        }
    }

    public static <T> void validateRequiredList(ValidationResult result , String fieldName, List<T> values){
        if(values.isEmpty()){
            String errorMessage = fieldName + " is required";
            result.addError(errorMessage);
        }
    }

    public static void validateInteger(ValidationResult result , String fieldName ,String value){
        if(value == null || value.trim().isEmpty()){
            String errorMessage = fieldName + " is required.";
            result.addError(errorMessage);
            return;
        }
        try{
            Integer.parseInt(value);
        }
        catch(NumberFormatException e){
            result.addError(fieldName + " must be an integer");
        }
    }

    public static void validateDouble(ValidationResult result , String fieldName , String value){
        if(value == null || value.trim().isEmpty()){
            String errorMessage = fieldName + " is required.";
            result.addError(errorMessage);
            return;
        }
        try{
            Double.parseDouble(value);
        }
        catch(NumberFormatException e){
            result.addError(fieldName + " must be numeric");
        }
    }


}
