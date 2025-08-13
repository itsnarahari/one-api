package com.one.utils;

public class Utils {

    public static String normalizeMobile(String mobile) {
        if (mobile == null || mobile.trim().isEmpty()) {
            return null; // or "" if you prefer empty string instead of null
        }

        mobile = mobile.trim();

        if (mobile.startsWith("+91") && mobile.length() > 3) {
            return mobile.substring(3);
        }

        return mobile;
    }
}
