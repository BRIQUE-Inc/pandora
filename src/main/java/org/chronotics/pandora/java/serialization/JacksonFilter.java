package org.chronotics.pandora.java.serialization;

public class JacksonFilter {
    public static String convertNAString(String strString){
        String strToString = strString.toLowerCase().trim();

        if (strToString.equals("na") || strToString.equals("n/a") || strToString.equals("nan")) {
            return "";
        } else {
            return strString;
        }
    }

    public static Boolean checkNAString(String strString){
        String strToString = strString.toLowerCase().trim();

        if (strToString.equals("na") || strToString.equals("n/a") || strToString.equals("nan")) {
            return true;
        } else {
            return false;
        }
    }
}
