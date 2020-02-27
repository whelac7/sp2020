package org.frc1732scoutingapp.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.frc1732scoutingapp.models.MatchResult;

public class JSONHelper {
    public static JsonObject parseMatchToJSON(int teamNumber, int matchNumber, String alliance, boolean initLine, int autoLower, int autoOuter, int autoInner, int teleopLower, int teleopOuter, int teleopInner, boolean rotationOut, boolean positionOut, boolean parked, boolean hanging, boolean leveled, int disableTime) {
        MatchResult matchResult = new MatchResult(teamNumber, matchNumber, alliance, initLine, autoLower, autoOuter, autoInner, teleopLower, teleopOuter, teleopInner, rotationOut, positionOut, parked, hanging, leveled, disableTime);
        JsonObject jsonObject = JsonParser.parseString(new Gson().toJson(matchResult)).getAsJsonObject();
        return jsonObject;
    }
}
