package org.frc1732scoutingapp.helpers;

import android.content.Context;

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

    public static void writeToFile(String filePath, String content) {
        try {
            FileWriter fw = new FileWriter(filePath);
            fw.write(content);
            fw.flush();
            fw.close();
        }
        catch (IOException ex) {
            ScoutingUtils.logException(ex, "IOHelper.writeToFile()");
        }
    }
}
