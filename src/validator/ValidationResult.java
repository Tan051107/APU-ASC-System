package validator;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private List<String> errors = new ArrayList<>();

    public void addError(String error){
        errors.add(error);
    }

    public boolean hasError(){
        return !errors.isEmpty();
    }

    public String getErrors(){
        return String.join("\n" , errors);
    }


}
