//TODO: Better exception handling

package org.frc1732scoutingapp.helpers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.frc1732scoutingapp.models.MatchResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JsonHelper {
    public static JsonObject parseMatchToJson(int teamNumber, int matchNumber, String alliance, boolean initLine, int autoLower, int autoOuter, int autoInner, int teleopLower, int teleopOuter, int teleopInner, boolean rotationOut, boolean positionOut, boolean parked, boolean hanging, boolean leveled, int disableTime) {
        MatchResult matchResult = new MatchResult(teamNumber, matchNumber, alliance, initLine, autoLower, autoOuter, autoInner, teleopLower, teleopOuter, teleopInner, rotationOut, positionOut, parked, hanging, leveled, disableTime);
        JsonObject jsonObject = JsonParser.parseString(new Gson().toJson(matchResult)).getAsJsonObject();
        return jsonObject;
    }

    public static JsonObject saveMatchToJson(Context context, String compCode, int teamNumber, int matchNumber, String alliance, boolean initLine, int autoLower, int autoOuter, int autoInner, int teleopLower, int teleopOuter, int teleopInner, boolean rotationOut, boolean positionOut, boolean parked, boolean hanging, boolean leveled, int disableTime) {
        MatchResult matchResult = new MatchResult(teamNumber, matchNumber, alliance, initLine, autoLower, autoOuter, autoInner, teleopLower, teleopOuter, teleopInner, rotationOut, positionOut, parked, hanging, leveled, disableTime);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String jsonString = gson.toJson(matchResult);
        File saveDir = new File(IOHelper.getCompetitionDirectoryPath(context, compCode));
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        IOHelper.writeToFile(context, IOHelper.getMatchFilePath(context, compCode, teamNumber, matchNumber) + ".json", jsonString);
        return gson.fromJson(jsonString, JsonObject.class);
    }

    public static JsonObject readCompetitionFromJson(Context context, String compCode) {
        return readFromJson(context, compCode, new File(IOHelper.getCompetitionJsonPath(context, compCode)));
    }

    public static JsonObject readTeamMatchFromJson(Context context, String compCode, int teamNumber, int matchNumber) {
        return readFromJson(context, compCode, new File(IOHelper.getMatchFilePath(context, compCode, teamNumber, matchNumber)));
    }

    public static JsonObject readFromJson(Context context, String compCode, File json) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(json));
            Gson gson = new Gson();
            JsonObject readJson = gson.fromJson(reader, JsonObject.class);
            reader.close();
            return readJson;
        }
        catch (IOException ex) {
            ScoutingUtils.logException(context, ex, "JsonHelper.readFromJson()");
            return null;
        }
    }

    public static JsonObject buildCompetitionJson(Context context, String compCode) {
        try {
            File compFolder = new File(IOHelper.getCompetitionDirectoryPath(context, compCode));
            if (compFolder == null) {
                throw new IOException("Could not find directory: " + IOHelper.getCompetitionDirectoryPath(context, compCode));
            }
            else {
                JsonObject competitionJson = new JsonObject();
                JsonArray matchJsons = new JsonArray();
                File[] files = compFolder.listFiles();

                for (File file : files) {
                    if (file.isFile() && file.getName().contains(compCode) && !file.getName().equals(compCode + ".json")) {
                        JsonObject match = readFromJson(context, compCode, file);
                        matchJsons.add(match);
                    }
                }
                competitionJson.addProperty("competitionCode", compCode);
                competitionJson.add("matches", matchJsons);

                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();
                IOHelper.writeToFile(context, IOHelper.getCompetitionJsonPath(context, compCode), gson.toJson(competitionJson));
                return competitionJson;
            }
        }
        catch (IOException ex) {
            ScoutingUtils.logException(context, ex, "JsonHelper.buildCompetitionJson()");
            return null;
        }
    }
}
