package org.frc1732scoutingapp.helpers;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class IOHelper {
    public static String getRootCompDataPath(Context context) {
        return context.getFilesDir() + "/compdata/";
    }

    public static String getCompetitionPath(Context context, String compCode) {
        return context.getFilesDir() + String.format("/compdata/%s/", compCode);
    }

    public static String getMatchFilePath(Context context, String compCode, int teamNumber, int matchNumber) {
        return context.getFilesDir() + String.format("/compdata/%s/%s_%s_%s.json", compCode, compCode, teamNumber, matchNumber);
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
