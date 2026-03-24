package enums;

public enum Gender{
    MALE("Male"),
    Female("Female");


    private final String displayGender;

    Gender(String displayGender){
        this.displayGender = displayGender;
    }

    public String getDisplayGender() {
        return displayGender;
    }

    public static Gender fromString (String genderString){
        for (Gender gender : Gender.values()){
            if(gender.displayGender.equalsIgnoreCase(genderString)){
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender selected");
    }


}
