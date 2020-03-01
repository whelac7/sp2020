package org.frc1732scoutingapp.helpers;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class IOHelper {
    public static String getRootCompDataPath(Context context) {
        return context.getFilesDir() + "/compdata/";
    }

    public static String getCompetitionJsonPath(Context context, String compCode) {
        return getCompetitionDirectoryPath(context, compCode) + String.format("%s.json", compCode);
    }

    public static String getCompetitionDirectoryPath(Context context, String compCode) {
        return getRootCompDataPath(context) + String.format("%s/", compCode);
    }

    public static String getMatchFilePath(Context context, String compCode, int teamNumber, int matchNumber) {
        return getCompetitionDirectoryPath(context, compCode) + String.format("%s_%s_%s.json", compCode, teamNumber, matchNumber);
    }

    public static String getLogDirectoryPath(Context context) {
        return context.getFilesDir() + "/log/";
    }

    public static File getLogDirectory(Context context) {
        File logDirectory = new File(getLogDirectoryPath(context));
        if (!logDirectory.exists()) {
            System.out.println("Does not exist");
            logDirectory.mkdirs();
        }
        return logDirectory;
    }

    public static void writeToFile(Context context, String filePath, String content) {
        try {
            FileWriter fw = new FileWriter(filePath);
            fw.write(content);
            fw.flush();
            fw.close();
        }
        catch (IOException ex) {
            ScoutingUtils.logExceptionWithNoFile(ex, "IOHelper");
        }
    }

    public static void writeToFile(Context context, String filePath, String content, boolean append) {
        try {
            FileWriter fw = new FileWriter(filePath, append);
            fw.write(content);
            fw.flush();
            fw.close();
        }
        catch (IOException ex) {
            ScoutingUtils.logExceptionWithNoFile(ex, "IOHelper");
        }
    }
}
