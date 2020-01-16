package com.example.googlesheetstest.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "1732scouting";
    public static final String TEAM_TABLE_NAME = "template";
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

    public void createTableIfNotExists(SQLiteDatabase sqLiteDatabase, String team_table_name) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + team_table_name + " (" +
                TEAM_COLUMN_MATCH_NUMBER + " INT UNSIGNED, " +
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
}
