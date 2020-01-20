package org.frc1732scoutingapp.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.zxing.WriterException;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.databinding.ActivitySqliteDatabaseBinding;
import org.frc1732scoutingapp.fragments.SaveDataDialog;
import org.frc1732scoutingapp.helpers.QRCodeHelper;
import org.frc1732scoutingapp.helpers.SQLiteDBHelper;
import org.frc1732scoutingapp.models.RequestCodes;
import org.frc1732scoutingapp.responses.SubmitToDBCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

public class SQLiteDatabaseActivity extends AppCompatActivity implements SubmitToDBCallback {

    private ActivitySqliteDatabaseBinding activityBinding;
    private String TAG = "SqliteDatabaseActivity";
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_sqlite_database);
        database = new SQLiteDBHelper(this).getReadableDatabase();

        activityBinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSaveDialog();
            }
        });

        activityBinding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayQueryResult(SQLiteDBHelper.readFromDB(database, activityBinding.teamNumberEditText.getText().toString(), activityBinding.matchEditText.getText().toString()));
            }
        });

        activityBinding.scanQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent QRIntent = new Intent(SQLiteDatabaseActivity.this, QRScannerActivity.class);
                startActivityForResult(QRIntent, RequestCodes.QR_SCAN.getValue());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCodes.QR_SCAN.getValue() && resultCode == RESULT_OK) {
            String[] propertyStrings = data.getStringExtra("data").split(",");
            Dictionary<String, String> properties = new Hashtable<String, String>();
            for (String propertyString : propertyStrings) {
                String[] nameToValue = propertyString.split("=");
                properties.put(nameToValue[0], nameToValue[1]);
            }
            populateFields(properties.get("teamNumber"), properties.get("matchNumber"), properties.get("initLine"), properties.get("autoLower"), properties.get("autoOuter"), properties.get("autoInner"));
        }
    }

    private void populateFields(String teamNumber, String matchNumber, String initLine, String autoLower, String autoOuter, String autoInner) {
        activityBinding.teamNumberEditText.setText(teamNumber);
        activityBinding.matchEditText.setText(matchNumber);
        activityBinding.initLineEditText.setText(initLine);
        activityBinding.autoLowerEditText.setText(autoLower);
        activityBinding.autoOuterEditText.setText(autoOuter);
        activityBinding.autoInnerEditText.setText(autoInner);
    }

    private void startSaveDialog() {
        try {
            processInputs();
            Bitmap code = QRCodeHelper.createQRCode(String.format("app=1732ScoutingApp,teamNumber=%s,matchNumber=%s,initLine=%s,autoLower=%s,autoOuter=%s,autoInner=%s,lower=%s,outer=%s,inner=%s,rotation=%s,position=%s,park=%s,hang=%s,level=%s,disableTime=%s,notes=%s",
                    activityBinding.teamNumberEditText.getText().toString(),
                    activityBinding.matchEditText.getText().toString(),
                    activityBinding.initLineEditText.getText().toString(),
                    activityBinding.autoLowerEditText.getText().toString(),
                    activityBinding.autoOuterEditText.getText().toString(),
                    activityBinding.autoInnerEditText.getText().toString(),
                    "0",
                    "0",
                    "0",
                    "0",
                    "0",
                    "0",
                    "0",
                    "0",
                    "0",
                    "0"));
            //activityBinding.QRCodeImage.setImageBitmap(code);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            code.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] codeInBytes = stream.toByteArray();
            Bundle bundledCode = new Bundle();
            bundledCode.putByteArray("codeInBytes", codeInBytes);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("Save Data Dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            SaveDataDialog saveDataDialog = new SaveDataDialog(this);
            saveDataDialog.setArguments(bundledCode);
            saveDataDialog.show(ft, "Save Data Dialog");
        } catch (IOException | WriterException ex) {
            System.out.println(ex);
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage().startsWith("no team specified")) {
                Toast.makeText(this, "You must specify a team number.", Toast.LENGTH_LONG).show();
            } else if (ex.getMessage().startsWith("match exists")) {
                Toast.makeText(this, "Match " + activityBinding.matchEditText.getText().toString() + " already exists.", Toast.LENGTH_LONG).show();
            } else if (ex.getMessage().startsWith("no match specified")) {
                Toast.makeText(this, "You must specify a match number.", Toast.LENGTH_LONG).show();
            } else if (ex.getMessage().startsWith("empty parameter")) {
                String msg = ex.getMessage();
                int reasonIndex = msg.indexOf(":") + 2;
                Toast.makeText(this, "You left a field empty: " + msg.substring(reasonIndex, msg.length()), Toast.LENGTH_LONG).show();
            }
        }
    }

    private ContentValues processInputs() throws IllegalArgumentException {
        if (activityBinding.teamNumberEditText.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("no team specified: You must specify a team number.");
        } else if (activityBinding.matchEditText.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("no match specified: You must specify a match number.");
        } else if (SQLiteDBHelper.matchExists(database, activityBinding.teamNumberEditText.getText().toString(), activityBinding.matchEditText.getText().toString())) {
            throw new IllegalArgumentException("match exists: Match " + activityBinding.matchEditText.getText().toString() + " already exists.");
        } else if (activityBinding.initLineEditText.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("empty parameter: Init Line");
        } else if (activityBinding.autoLowerEditText.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("empty parameter: Auto Lower");
        } else if (activityBinding.autoOuterEditText.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("empty parameter: Auto Outer");
        } else if (activityBinding.autoInnerEditText.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("empty parameter: Auto Inner");
        }

        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.TEAM_COLUMN_COMPETITION_ID, 0); // Have to figure out how to get the COMPETITION_ID
        values.put(SQLiteDBHelper.TEAM_COLUMN_MATCH_NUMBER, activityBinding.matchEditText.getText().toString());
        values.put(SQLiteDBHelper.TEAM_COLUMN_INIT_LINE, activityBinding.initLineEditText.getText().toString());
        values.put(SQLiteDBHelper.TEAM_COLUMN_AUTO_LOWER, activityBinding.autoLowerEditText.getText().toString());
        values.put(SQLiteDBHelper.TEAM_COLUMN_AUTO_OUTER, activityBinding.autoOuterEditText.getText().toString());
        values.put(SQLiteDBHelper.TEAM_COLUMN_AUTO_INNER, activityBinding.autoInnerEditText.getText().toString());
        values.put(SQLiteDBHelper.TEAM_COLUMN_LOWER, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_OUTER, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_INNER, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_ROTATION, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_POSITION, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_PARK, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_HANG, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_LEVEL, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_DISABLE_TIME, 0);
        return values;
    }

    public void saveToDB() {
        SQLiteDBHelper.createTableIfNotExists(database, "frc" + activityBinding.teamNumberEditText.getText().toString());
        long newRowId = database.insert("frc" + activityBinding.teamNumberEditText.getText().toString(), null, processInputs());
        Toast.makeText(this, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
    }

    private void displayQueryResult(Cursor cursor) {
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No results found.", Toast.LENGTH_LONG).show();
                return;
            }
            cursor.moveToFirst();
            String result = "";
            for (int i = 0; i < cursor.getCount(); i++, cursor.moveToNext()) {
                result += cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_MATCH_NUMBER)) + ": " + cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_INIT_LINE)) + "\n";
            }
            cursor.close();
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        }
    }
}