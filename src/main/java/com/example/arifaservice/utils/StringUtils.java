package com.example.arifaservice.utils;

public class StringUtils {

    public static String formatPhoneNumber(String phoneNumber) {
        if (isNullOrEmpty(phoneNumber)) {
            return null;
        }
        if (phoneNumber.matches("\\d{10}")) {
            return "254" + phoneNumber.substring(1);
        } else if (phoneNumber.matches("\\+\\d{12}")) {
            return phoneNumber.substring(1);
        }

        return phoneNumber;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
