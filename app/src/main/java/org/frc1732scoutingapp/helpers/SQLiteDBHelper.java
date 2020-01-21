package org.frc1732scoutingapp.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "1732scouting";
    public static final String TEAM_TABLE_NAME = "template";
    public static final String TEAM_COLUMN_COMPETITION_ID = "competition_id";
    public static final String TEAM_COLUMN_MATCH_NUMBER = "match_number";
    public static final String TEAM_COLUMN_INIT_LINE = "init_line";
    public static final String TEAM_COLUMN_AUTO_LOWER = "auto_lower";
    public static final String TEAM_COLUMN_AUTO_OUTER = "auto_outer";
    public static final String TEAM_COLUMN_AUTO_INNER = "auto_inner";
    public static final String TEAM_COLUMN_LOWER = "lower";
    public static final String TEAM_COLUMN_OUTER = "outer";
    public static final String TEAM_COLUMN_INNER = "inner";
    public static final String TEAM_COLUMN_ROTATION = "rotation";
    public static final String TEAM_COLUMN_POSITION = "position";
    public static final String TEAM_COLUMN_PARK = "park";
    public static final String TEAM_COLUMN_HANG = "hang";
    public static final String TEAM_COLUMN_LEVEL = "level";
    public static final String TEAM_COLUMN_DISABLE_TIME = "disable_time";

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TEAM_TABLE_NAME + " (" +
                TEAM_COLUMN_MATCH_NUMBER + " INT UNSIGNED, " +
                TEAM_COLUMN_COMPETITION_ID + " TEXT, " +
                TEAM_COLUMN_INIT_LINE + " INT UNSIGNED, " +
                TEAM_COLUMN_AUTO_LOWER + " INT UNSIGNED, " +
                TEAM_COLUMN_AUTO_OUTER + " INT UNSIGNED, " +
                TEAM_COLUMN_AUTO_INNER + " INT UNSIGNED, " +
                TEAM_COLUMN_LOWER + " INT UNSIGNED, " +
                TEAM_COLUMN_OUTER + " INT UNSIGNED, " +
                TEAM_COLUMN_INNER + " INT UNSIGNED, " +
                TEAM_COLUMN_ROTATION + " INT UNSIGNED, " +
                TEAM_COLUMN_POSITION + " INT UNSIGNED, " +
                TEAM_COLUMN_PARK + " INT UNSIGNED, " +
                TEAM_COLUMN_HANG + " INT UNSIGNED, " +
                TEAM_COLUMN_LEVEL + " INT UNSIGNED, " +
                TEAM_COLUMN_DISABLE_TIME + " INT UNSIGNED" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TEAM_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public static void createTableIfNotExists(SQLiteDatabase sqLiteDatabase, String team_table_name) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + team_table_name + " (" +
                TEAM_COLUMN_MATCH_NUMBER + " INT UNSIGNED, " +
                TEAM_COLUMN_COMPETITION_ID + " TEXT, " +
                TEAM_COLUMN_INIT_LINE + " INT UNSIGNED, " +
                TEAM_COLUMN_AUTO_LOWER + " INT UNSIGNED, " +
                TEAM_COLUMN_AUTO_OUTER + " INT UNSIGNED, " +
                TEAM_COLUMN_AUTO_INNER + " INT UNSIGNED, " +
                TEAM_COLUMN_LOWER + " INT UNSIGNED, " +
                TEAM_COLUMN_OUTER + " INT UNSIGNED, " +
                TEAM_COLUMN_INNER + " INT UNSIGNED, " +
                TEAM_COLUMN_ROTATION + " INT UNSIGNED, " +
                TEAM_COLUMN_POSITION + " INT UNSIGNED, " +
                TEAM_COLUMN_PARK + " INT UNSIGNED, " +
                TEAM_COLUMN_HANG + " INT UNSIGNED, " +
                TEAM_COLUMN_LEVEL + " INT UNSIGNED, " +
                TEAM_COLUMN_DISABLE_TIME + " INT UNSIGNED" + ")");
        }

    public static boolean matchExists(SQLiteDatabase database, String teamNumber, String match) {
        Cursor cursor = readFromDB(database, teamNumber, match);
        if (cursor != null) {
            return cursor.getCount() >= 1;
        }
        return false;
    }

    /**
     *
     * @param database, teamNumber, match
     * @return null if no such team exists
     * @throws SQLiteException
     */
    public static Cursor readFromDB(SQLiteDatabase database, String teamNumber) throws SQLiteException {
        String team = "frc" + teamNumber;

        String[] projection = {
                SQLiteDBHelper.TEAM_COLUMN_COMPETITION_ID,
                SQLiteDBHelper.TEAM_COLUMN_MATCH_NUMBER,
                SQLiteDBHelper.TEAM_COLUMN_INIT_LINE,
                SQLiteDBHelper.TEAM_COLUMN_AUTO_LOWER,
                SQLiteDBHelper.TEAM_COLUMN_AUTO_OUTER,
                SQLiteDBHelper.TEAM_COLUMN_AUTO_INNER,
                SQLiteDBHelper.TEAM_COLUMN_LOWER,
                SQLiteDBHelper.TEAM_COLUMN_OUTER,
                SQLiteDBHelper.TEAM_COLUMN_INNER,
                SQLiteDBHelper.TEAM_COLUMN_ROTATION,
                SQLiteDBHelper.TEAM_COLUMN_POSITION,
                SQLiteDBHelper.TEAM_COLUMN_PARK,
                SQLiteDBHelper.TEAM_COLUMN_HANG,
                SQLiteDBHelper.TEAM_COLUMN_LEVEL,
                SQLiteDBHelper.TEAM_COLUMN_DISABLE_TIME
        };

        try {
            Cursor cursor = database.query(
                    team,   // The table to query
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
    public static Cursor readFromDB(SQLiteDatabase database, String teamNumber, String match) throws SQLiteException {
        String team = "frc" + teamNumber;

        String[] projection = {
                SQLiteDBHelper.TEAM_COLUMN_COMPETITION_ID,
                SQLiteDBHelper.TEAM_COLUMN_MATCH_NUMBER,
                SQLiteDBHelper.TEAM_COLUMN_INIT_LINE,
                SQLiteDBHelper.TEAM_COLUMN_AUTO_LOWER,
                SQLiteDBHelper.TEAM_COLUMN_AUTO_OUTER,
                SQLiteDBHelper.TEAM_COLUMN_AUTO_INNER,
                SQLiteDBHelper.TEAM_COLUMN_LOWER,
                SQLiteDBHelper.TEAM_COLUMN_OUTER,
                SQLiteDBHelper.TEAM_COLUMN_INNER,
                SQLiteDBHelper.TEAM_COLUMN_ROTATION,
                SQLiteDBHelper.TEAM_COLUMN_POSITION,
                SQLiteDBHelper.TEAM_COLUMN_PARK,
                SQLiteDBHelper.TEAM_COLUMN_HANG,
                SQLiteDBHelper.TEAM_COLUMN_LEVEL,
                SQLiteDBHelper.TEAM_COLUMN_DISABLE_TIME
        };

        String selection = SQLiteDBHelper.TEAM_COLUMN_MATCH_NUMBER + " = ?";
        String[] selectionArgs = new String[]{match};

        try {
            Cursor cursor = database.query(
                    team,   // The table to query
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
}
