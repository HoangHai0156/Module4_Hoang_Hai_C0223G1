package com.cg.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {
    private static String REGEX;

    public static boolean isEmailValid(String email){
        REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,3}$";
        return Pattern.matches(REGEX,email);
    }
    public static boolean isPhoneValid(String email){
        REGEX = "^\\+\\d{1,2}\\s\\(\\d{3}\\)\\s\\d{3}-\\d{4}$";
        return Pattern.matches(REGEX,email);
    }

    public static boolean isNameValid(String name){
        REGEX = "^[a-zA-Z\\s]{7,30}$";
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean isAddressValid(String address){
        REGEX = "^[a-zA-Z0-9\\s.,]{3,50}$";
        return Pattern.matches(REGEX,address);
    }

    public static boolean isNumberValid(String number) {
        REGEX = "\\d+";
        return Pattern.compile(REGEX).matcher(number).matches();
    }
    public static boolean isDateValid(String date) {
        REGEX = "^(0?[1-9]|[12][0-9]|3[01])\\/(0?[1-9]|1[0-2])\\/\\d{4}$";
        return Pattern.compile(REGEX).matcher(date).matches();
    }
}
