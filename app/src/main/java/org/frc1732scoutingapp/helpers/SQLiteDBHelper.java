package org.frc1732scoutingapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.frc1732scoutingapp.models.MatchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "1732scouting";
    public static final String COMPETITION_TABLE_NAME = "matches";
    public static final String COMPETITION_COLUMN_CODE = "competition_code";
    public static final String COMPETITION_COLUMN_TEAM_NUMBER = "team_number";
    public static final String COMPETITION_COLUMN_MATCH_NUMBER = "match_number";
    public static final String COMPETITION_COLUMN_ALLIANCE = "alliance";
    public static final String COMPETITION_COLUMN_INIT_LINE = "init_line";
    public static final String COMPETITION_COLUMN_AUTO_LOWER = "auto_lower";
    public static final String COMPETITION_COLUMN_AUTO_OUTER = "auto_outer";
    public static final String COMPETITION_COLUMN_AUTO_INNER = "auto_inner";
    public static final String COMPETITION_COLUMN_TELEOP_LOWER = "teleop_lower";
    public static final String COMPETITION_COLUMN_TELEOP_OUTER = "teleop_outer";
    public static final String COMPETITION_COLUMN_TELEOP_INNER = "inner";
    public static final String COMPETITION_COLUMN_ROTATION = "rotation";
    public static final String COMPETITION_COLUMN_POSITION = "position";
    public static final String COMPETITION_COLUMN_PARK = "park";
    public static final String COMPETITION_COLUMN_HANG = "hang";
    public static final String COMPETITION_COLUMN_LEVEL = "level";
    public static final String COMPETITION_COLUMN_DISABLE_TIME = "disable_time";

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + COMPETITION_TABLE_NAME + " (" +
                COMPETITION_COLUMN_CODE + " VARCHAR(255), " +
                COMPETITION_COLUMN_TEAM_NUMBER + " INT UNSIGNED, " +
                COMPETITION_COLUMN_MATCH_NUMBER + " INT UNSIGNED, " +
                COMPETITION_COLUMN_ALLIANCE + " VARCHAR(255), " +
                COMPETITION_COLUMN_INIT_LINE + " BOOLEAN, " +
                COMPETITION_COLUMN_AUTO_LOWER + " INT UNSIGNED, " +
                COMPETITION_COLUMN_AUTO_OUTER + " INT UNSIGNED, " +
                COMPETITION_COLUMN_AUTO_INNER + " INT UNSIGNED," +
                COMPETITION_COLUMN_TELEOP_LOWER + " INT UNSIGNED, " +
                COMPETITION_COLUMN_TELEOP_OUTER + " INT UNSIGNED, " +
                COMPETITION_COLUMN_TELEOP_INNER + " INT UNSIGNED, " +
                COMPETITION_COLUMN_ROTATION + " BOOLEAN, " +
                COMPETITION_COLUMN_POSITION + " BOOLEAN, " +
                COMPETITION_COLUMN_PARK + " BOOLEAN, " +
                COMPETITION_COLUMN_HANG + " BOOLEAN, " +
                COMPETITION_COLUMN_LEVEL + " BOOLEAN, " +
                COMPETITION_COLUMN_DISABLE_TIME + " INT UNSIGNED, " +
                "UNIQUE(" + COMPETITION_COLUMN_CODE + ", " + COMPETITION_COLUMN_TEAM_NUMBER + ", " + COMPETITION_COLUMN_MATCH_NUMBER + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + COMPETITION_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

//    public static void createTableIfNotExists(SQLiteDatabase sqLiteDatabase, String table_name) {
//        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + table_name + " (" +
//                COMPETITION_COLUMN_TEAM_NUMBER + " INT UNSIGNED, " +
//                COMPETITION_COLUMN_MATCH_NUMBER + " INT UNSIGNED, " +
//                COMPETITION_COLUMN_ALLIANCE + " VARCHAR(255), " +
//                COMPETITION_COLUMN_INIT_LINE + " BOOLEAN, " +
//                COMPETITION_COLUMN_AUTO_LOWER + " INT UNSIGNED, " +
//                COMPETITION_COLUMN_AUTO_OUTER + " INT UNSIGNED, " +
//                COMPETITION_COLUMN_AUTO_INNER + " INT UNSIGNED," +
//                COMPETITION_COLUMN_TELEOP_LOWER + " INT UNSIGNED, " +
//                COMPETITION_COLUMN_TELEOP_OUTER + " INT UNSIGNED, " +
//                COMPETITION_COLUMN_TELEOP_INNER + " INT UNSIGNED, " +
//                COMPETITION_COLUMN_ROTATION + " BOOLEAN, " +
//                COMPETITION_COLUMN_POSITION + " BOOLEAN, " +
//                COMPETITION_COLUMN_PARK + " BOOLEAN, " +
//                COMPETITION_COLUMN_HANG + " BOOLEAN, " +
//                COMPETITION_COLUMN_LEVEL + " BOOLEAN, " +
//                COMPETITION_COLUMN_DISABLE_TIME + " INT UNSIGNED, " +
//                "UNIQUE(" + COMPETITION_COLUMN_CODE + ", " + COMPETITION_COLUMN_TEAM_NUMBER + ", " + COMPETITION_COLUMN_MATCH_NUMBER + "))");
//    }

    /**
     *
     * @param database, teamNumber, match
     * @return null if no such team exists
     * @throws SQLiteException
     */
    public static Cursor readFromDB(SQLiteDatabase database, String compCode) throws SQLiteException {
        String[] projection = {
                SQLiteDBHelper.COMPETITION_COLUMN_CODE,
                SQLiteDBHelper.COMPETITION_COLUMN_MATCH_NUMBER,
                SQLiteDBHelper.COMPETITION_COLUMN_ALLIANCE,
                SQLiteDBHelper.COMPETITION_COLUMN_INIT_LINE,
                SQLiteDBHelper.COMPETITION_COLUMN_AUTO_LOWER,
                SQLiteDBHelper.COMPETITION_COLUMN_AUTO_OUTER,
                SQLiteDBHelper.COMPETITION_COLUMN_AUTO_INNER,
                SQLiteDBHelper.COMPETITION_COLUMN_TELEOP_LOWER,
                SQLiteDBHelper.COMPETITION_COLUMN_TELEOP_OUTER,
                SQLiteDBHelper.COMPETITION_COLUMN_TELEOP_INNER,
                SQLiteDBHelper.COMPETITION_COLUMN_ROTATION,
                SQLiteDBHelper.COMPETITION_COLUMN_POSITION,
                SQLiteDBHelper.COMPETITION_COLUMN_PARK,
                SQLiteDBHelper.COMPETITION_COLUMN_HANG,
                SQLiteDBHelper.COMPETITION_COLUMN_LEVEL,
                SQLiteDBHelper.COMPETITION_COLUMN_DISABLE_TIME
        };

        try {
            Cursor cursor = database.query(
                    SQLiteDBHelper.COMPETITION_TABLE_NAME,   // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                      // don't sort
            );
            Log.d("SqliteDatabaseActivity", "The total cursor count is " + cursor.getCount());
            return cursor;
        } catch (SQLiteException ex) {
            if (ex.getMessage().startsWith("no such table")) {
                System.out.println(ex.getMessage());
                return null;
            }
            throw ex;
        }
    }

    /**
     *
     * @param database, teamNumber, match
     * @return null if no such team exists
     * @throws SQLiteException
     */
    public static Cursor readFromDB(SQLiteDatabase database, String compCode, String match) throws SQLiteException {
        String[] projection = {
                SQLiteDBHelper.COMPETITION_COLUMN_CODE,
                SQLiteDBHelper.COMPETITION_COLUMN_MATCH_NUMBER,
                SQLiteDBHelper.COMPETITION_COLUMN_ALLIANCE,
                SQLiteDBHelper.COMPETITION_COLUMN_INIT_LINE,
                SQLiteDBHelper.COMPETITION_COLUMN_AUTO_LOWER,
                SQLiteDBHelper.COMPETITION_COLUMN_AUTO_OUTER,
                SQLiteDBHelper.COMPETITION_COLUMN_AUTO_INNER,
                SQLiteDBHelper.COMPETITION_COLUMN_TELEOP_LOWER,
                SQLiteDBHelper.COMPETITION_COLUMN_TELEOP_OUTER,
                SQLiteDBHelper.COMPETITION_COLUMN_TELEOP_INNER,
                SQLiteDBHelper.COMPETITION_COLUMN_ROTATION,
                SQLiteDBHelper.COMPETITION_COLUMN_POSITION,
                SQLiteDBHelper.COMPETITION_COLUMN_PARK,
                SQLiteDBHelper.COMPETITION_COLUMN_HANG,
                SQLiteDBHelper.COMPETITION_COLUMN_LEVEL,
                SQLiteDBHelper.COMPETITION_COLUMN_DISABLE_TIME
        };

        String selection = SQLiteDBHelper.COMPETITION_COLUMN_MATCH_NUMBER + " = ?";
        String[] selectionArgs = new String[]{match};

        try {
            Cursor cursor = database.query(
                    SQLiteDBHelper.COMPETITION_TABLE_NAME,   // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                      // don't sort
            );
            Log.d("SqliteDatabaseActivity", "The total cursor count is " + cursor.getCount());
            return cursor;
        } catch (SQLiteException ex) {
            if (ex.getMessage().startsWith("no such table")) {
                return null;
            }
            throw ex;
        }
    }

    public static JsonObject saveDBMatchToJson(SQLiteDatabase database, Context context, String compCode, String teamNumber, String matchNumber) {
        Cursor result = database.rawQuery(String.format("SELECT * FROM %s WHERE %s=%s AND %s=%s AND %s=%s", COMPETITION_TABLE_NAME, COMPETITION_COLUMN_CODE, compCode, COMPETITION_COLUMN_TEAM_NUMBER, teamNumber, COMPETITION_COLUMN_MATCH_NUMBER, matchNumber), null);
        if (result.moveToFirst()) {
            JsonObject match = JsonHelper.saveMatchToJson(
                    context,
                    compCode,
                    ScoutingUtils.tryParseInt(result.getString(result.getColumnIndex(COMPETITION_COLUMN_TEAM_NUMBER))),
                    ScoutingUtils.tryParseInt(result.getString(result.getColumnIndex(COMPETITION_COLUMN_MATCH_NUMBER))),
                    result.getString(result.getColumnIndex(COMPETITION_COLUMN_ALLIANCE)),
                    ScoutingUtils.intToBool(ScoutingUtils.tryParseInt(result.getString(result.getColumnIndex(COMPETITION_COLUMN_INIT_LINE)))),
                    ScoutingUtils.tryParseInt(result.getString(result.getColumnIndex(COMPETITION_COLUMN_AUTO_LOWER))),
                    ScoutingUtils.tryParseInt(result.getString(result.getColumnIndex(COMPETITION_COLUMN_AUTO_OUTER))),
                    ScoutingUtils.tryParseInt(result.getString(result.getColumnIndex(COMPETITION_COLUMN_AUTO_INNER))),
                    ScoutingUtils.tryParseInt(result.getString(result.getColumnIndex(COMPETITION_COLUMN_TELEOP_LOWER))),
                    ScoutingUtils.tryParseInt(result.getString(result.getColumnIndex(COMPETITION_COLUMN_TELEOP_OUTER))),
                    ScoutingUtils.tryParseInt(result.getString(result.getColumnIndex(COMPETITION_COLUMN_TELEOP_INNER))),
                    ScoutingUtils.intToBool(ScoutingUtils.tryParseInt(result.getString(result.getColumnIndex(COMPETITION_COLUMN_ROTATION)))),
                    ScoutingUtils.intToBool(ScoutingUtils.tryParseInt(result.getString(result.getColumnIndex(COMPETITION_COLUMN_POSITION)))),
                    ScoutingUtils.intToBool(ScoutingUtils.tryParseInt(result.getString(result.getColumnIndex(COMPETITION_COLUMN_PARK)))),
                    ScoutingUtils.intToBool(ScoutingUtils.tryParseInt(result.getString(result.getColumnIndex(COMPETITION_COLUMN_HANG)))),
                    ScoutingUtils.intToBool(ScoutingUtils.tryParseInt(result.getString(result.getColumnIndex(COMPETITION_COLUMN_LEVEL)))),
                    ScoutingUtils.tryParseInt(result.getString(result.getColumnIndex(COMPETITION_COLUMN_DISABLE_TIME))));
            return match;
        }
        return null;
    }

    private static ContentValues putMatchValues(String compCode, MatchResult matchResult) {
        ContentValues values = new ContentValues();

        values.put(SQLiteDBHelper.COMPETITION_COLUMN_CODE, compCode);
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_TEAM_NUMBER, matchResult.getTeamNumber());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_MATCH_NUMBER, matchResult.getMatchNumber());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_ALLIANCE, matchResult.getAlliance());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_INIT_LINE, matchResult.getInitLine());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_AUTO_LOWER, matchResult.getAutoLower());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_AUTO_OUTER, matchResult.getAutoInner());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_AUTO_INNER, 0);
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_TELEOP_LOWER, matchResult.getLower());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_TELEOP_OUTER, matchResult.getOuter());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_TELEOP_INNER, 0);
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_ROTATION, matchResult.getRotation());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_POSITION, matchResult.getPosition());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_PARK, matchResult.getPark());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_HANG, matchResult.getHang());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_LEVEL, matchResult.getLevel());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_DISABLE_TIME, matchResult.getDisableTime());

        return values;
    }

    //TODO: Test with new schema
    public static void insertTeamMatchJsonToDB(SQLiteDatabase database, Context context, String compCode, int teamNumber, int matchNumber) {
        JsonObject match = JsonHelper.readTeamMatchFromJson(context, compCode, teamNumber, matchNumber);
        MatchResult matchResult = new Gson().fromJson(match, MatchResult.class);
        ContentValues values = putMatchValues(compCode, matchResult);
        database.insert(COMPETITION_TABLE_NAME, null, values);
    }

    //TODO: Test with new schema
    public static void insertCompetitionJsonToDB(SQLiteDatabase database, Context context, String compCode) throws IOException {
        JsonObject competition = JsonHelper.readCompetitionFromJson(context, compCode);

        if (competition == null) {
            throw new IOException("There is no competition JSON with the name: " + compCode);
        }

        Gson gson = new Gson();
        JsonArray matches = competition.get("matches").getAsJsonArray();
        database.beginTransaction();
        for (int i = 0; i < matches.size(); i++) {
            JsonObject match = matches.get(i).getAsJsonObject();
            MatchResult matchResult = gson.fromJson(match, MatchResult.class);
            ContentValues values = putMatchValues(compCode, matchResult);
            database.insert(COMPETITION_TABLE_NAME, null, values);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    //TODO: Test with new schema
    public static List<String> getCompetitions(SQLiteDatabase database) {
        Cursor competitions = database.rawQuery("SELECT DISTINCT " + COMPETITION_COLUMN_CODE + " FROM " + COMPETITION_TABLE_NAME, null);
        List<String> competitionList = new ArrayList<String>();
        if (competitions.moveToFirst()) {
            do {
                String table = competitions.getString(0);
                competitionList.add(table);
            } while (competitions.moveToNext());
        }
        return competitionList;
    }

    //TODO: Test with new Schema
    public static List<MatchResult> getMatchResults(SQLiteDatabase database, String compCode) {
        Cursor results = database.rawQuery("SELECT * FROM " + COMPETITION_TABLE_NAME + " WHERE " + COMPETITION_COLUMN_CODE + "=" + "'" + compCode + "'" +  " ORDER BY " + COMPETITION_COLUMN_MATCH_NUMBER, null);
        List<MatchResult> matchResults = new ArrayList<MatchResult>();
        System.out.println("Results count: " + results.getCount());
        if (results.moveToFirst()) {
            do {
                matchResults.add(new MatchResult(
                        ScoutingUtils.tryParseInt(results.getString(results.getColumnIndex(COMPETITION_COLUMN_TEAM_NUMBER))),
                        ScoutingUtils.tryParseInt(results.getString(results.getColumnIndex(COMPETITION_COLUMN_MATCH_NUMBER))),
                        results.getString(results.getColumnIndex(COMPETITION_COLUMN_ALLIANCE)),
                        ScoutingUtils.intToBool(ScoutingUtils.tryParseInt(results.getString(results.getColumnIndex(COMPETITION_COLUMN_INIT_LINE)))),
                        ScoutingUtils.tryParseInt(results.getString(results.getColumnIndex(COMPETITION_COLUMN_AUTO_LOWER))),
                        ScoutingUtils.tryParseInt(results.getString(results.getColumnIndex(COMPETITION_COLUMN_AUTO_OUTER))),
                        ScoutingUtils.tryParseInt(results.getString(results.getColumnIndex(COMPETITION_COLUMN_AUTO_INNER))),
                        ScoutingUtils.tryParseInt(results.getString(results.getColumnIndex(COMPETITION_COLUMN_TELEOP_LOWER))),
                        ScoutingUtils.tryParseInt(results.getString(results.getColumnIndex(COMPETITION_COLUMN_TELEOP_OUTER))),
                        ScoutingUtils.tryParseInt(results.getString(results.getColumnIndex(COMPETITION_COLUMN_TELEOP_INNER))),
                        ScoutingUtils.intToBool(ScoutingUtils.tryParseInt(results.getString(results.getColumnIndex(COMPETITION_COLUMN_ROTATION)))),
                        ScoutingUtils.intToBool(ScoutingUtils.tryParseInt(results.getString(results.getColumnIndex(COMPETITION_COLUMN_POSITION)))),
                        ScoutingUtils.intToBool(ScoutingUtils.tryParseInt(results.getString(results.getColumnIndex(COMPETITION_COLUMN_PARK)))),
                        ScoutingUtils.intToBool(ScoutingUtils.tryParseInt(results.getString(results.getColumnIndex(COMPETITION_COLUMN_HANG)))),
                        ScoutingUtils.intToBool(ScoutingUtils.tryParseInt(results.getString(results.getColumnIndex(COMPETITION_COLUMN_LEVEL)))),
                        ScoutingUtils.tryParseInt(results.getString(results.getColumnIndex(COMPETITION_COLUMN_DISABLE_TIME)))
                ));
            } while (results.moveToNext());
        }
        return matchResults;
    }
}
