package org.example.utils;

import java.util.regex.Pattern;

public class Validator {

    public static boolean checkStringRegex(String testString, String regexPattern){
        return Pattern.compile(regexPattern)
                .matcher(testString)
                .matches();
    }

    public static boolean isEmailValid(String email){
        return checkStringRegex(email, "^\\S+@\\S+\\.\\S+$");
    }

    public static boolean isPasswordValid(String password) {
        return checkStringRegex(password, "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{4,20}$");
    }
}
