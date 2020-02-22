package org.frc1732scoutingapp.helpers;

public class ScoutingUtils {
    public static int boolToInt(Boolean b) {
        if (b) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public static boolean intToBool(int num) {
        if (num == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    public static Integer tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }
}
