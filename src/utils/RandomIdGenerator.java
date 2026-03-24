package utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomIdGenerator{

    public static int generateRandomDigit(){
        return ThreadLocalRandom.current().nextInt(0,10);
    }
    public static String generateId(String prefix , int length ){
        StringBuilder id = new StringBuilder();
        for(int i = 0 ; i<length ; i++){
            id.append(generateRandomDigit());
        }
        return prefix+ id;
    }
}
