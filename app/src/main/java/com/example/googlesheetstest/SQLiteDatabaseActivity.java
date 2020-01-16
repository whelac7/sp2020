package com.example.googlesheetstest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.googlesheetstest.databinding.ActivitySqliteDatabaseBinding;
import com.example.googlesheetstest.helpers.SQLiteDBHelper;

public class SQLiteDatabaseActivity extends AppCompatActivity {

    private ActivitySqliteDatabaseBinding activityBinding;
    private String TAG = "SqliteDatabaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_sqlite_database);

        activityBinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB();
            }
        });

        activityBinding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFromDB();
            }
        });
    }

    private void saveToDB() {
        SQLiteDBHelper DBHelper = new SQLiteDBHelper(this);
        SQLiteDatabase database = DBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.TEAM_COLUMN_COMPETITION_ID, 0); // Have to figure out how to get the COMPETITION_ID
        values.put(SQLiteDBHelper.TEAM_COLUMN_MATCH_NUMBER, activityBinding.ageEditText.getText().toString());
        values.put(SQLiteDBHelper.TEAM_COLUMN_INIT_LINE, activityBinding.genderEditText.getText().toString());
        values.put(SQLiteDBHelper.TEAM_COLUMN_AUTO_LOWER, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_AUTO_OUTER, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_AUTO_INNER, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_LOWER, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_OUTER, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_INNER, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_ROTATION, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_POSITION, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_PARK, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_HANG, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_LEVEL, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_DISABLE_TIME, 0);

        DBHelper.createTableIfNotExists(database, "frc" + activityBinding.nameEditText.getText().toString());
        long newRowId = database.insert("frc" + activityBinding.nameEditText.getText().toString(), null, values);

        Toast.makeText(this, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
    }

    private void readFromDB() {
        String team = "frc" + activityBinding.nameEditText.getText().toString();
        String match = activityBinding.ageEditText.getText().toString();

        SQLiteDatabase database = new SQLiteDBHelper(this).getReadableDatabase();

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

        String selection = null;
        String[] selectionArgs = null;
        if (!match.isEmpty()) {
            selection =
                    SQLiteDBHelper.TEAM_COLUMN_MATCH_NUMBER + " = ?";
            selectionArgs = new String[]{match};
        }

        Cursor cursor = database.query(
                team,   // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort
        );

        Log.d(TAG, "The total cursor count is " + cursor.getCount());
        cursor.moveToFirst();
        String stuff = "";
        for (int i = 0; i < cursor.getCount(); i++, cursor.moveToNext()) {
            stuff += cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_MATCH_NUMBER)) + ": " + cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_INIT_LINE)) + "\n";
        }
        cursor.close();
        Toast.makeText(this, stuff, Toast.LENGTH_LONG).show();
    }
}
