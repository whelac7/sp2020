package org.frc1732scoutingapp.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Spinner;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class ScoutingUtils {
    public static void log(Context context, String logName, String tag, String action) {
        Date timestamp = Calendar.getInstance().getTime();
        Log.i(tag, timestamp + ": " + action);

        File logDirectory = IOHelper.getLogDirectory(context);
        String actionLogPath = logDirectory.getPath() + "/" + logName + ".log";
        String verboseLogPath = logDirectory.getPath() + "/verbose.log";
        IOHelper.writeToFile(context, actionLogPath, String.format("%s: (%s) - %s\n", timestamp, tag, action), true);
        IOHelper.writeToFile(context, verboseLogPath, String.format("%s: (%s) - %s\n", timestamp, tag, action), true);
    }

    public static void logAction(Context context, String tag, String action) {
        log(context, "actions", tag, action);
    }

    public static void logException(Context context, Exception ex, String tag) {
        Date timestamp = Calendar.getInstance().getTime();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        String message = ex.toString();
        for (StackTraceElement element : stackTrace) {
            message += "\n" + element.toString();
        }
        Log.e(tag, message);
        log(context, "exceptions", tag, message);
    }

    public static void logException(Context context, String tag, String error) {
        log(context, "exceptions", tag, error);
    }

    public static void logExceptionWithNoFile(Exception ex, String tag) {
        Date timestamp = Calendar.getInstance().getTime();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        String message = ex.toString();
        for (StackTraceElement element : stackTrace) {
            message += "\n" + element.toString();
        }
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
