package org.frc1732scoutingapp.helpers;

import android.util.Log;
import android.widget.Spinner;

public class ScoutingUtils {
    public static void logException(Exception ex, String tag) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        String message = ex.toString();
        for (StackTraceElement element : stackTrace) {
            message += "\n" + element.toString();
        }
        Log.e(tag, message);
    }

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

    public static boolean stringToBool(String str) {
        if (str.toUpperCase().trim().equals("TRUE")) {
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

    public static int getDefaultSpinnerSetting(Spinner spinner, String setting) {
        if (setting != null) {
            for (int i = 0; i < spinner.getCount(); i++) {
                String currentItem = spinner.getItemAtPosition(i).toString();
                if (currentItem.equals(setting)) {
                    return i;
                }
            }
        }
        return 0;
    }
}
