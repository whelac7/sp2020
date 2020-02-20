package org.frc1732scoutingapp.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.zxing.WriterException;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.activities.QRScannerActivity;
import org.frc1732scoutingapp.databinding.FragmentSqliteDatabaseBinding;
import org.frc1732scoutingapp.helpers.QRCodeHelper;
import org.frc1732scoutingapp.helpers.SQLiteDBHelper;
import org.frc1732scoutingapp.models.Alliance;
import org.frc1732scoutingapp.models.RequestCodes;
import org.frc1732scoutingapp.responses.SubmitToDBCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import static android.app.Activity.RESULT_OK;

public class SQLLiteDatabaseFragment extends Fragment implements SubmitToDBCallback {

    private FragmentSqliteDatabaseBinding fragmentBinding;
    private String TAG = "SqliteDatabaseActivity";
    private SQLiteDatabase database;
    private boolean isMaster;
    private String alliance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sqlite_database, container, false);
        View view = fragmentBinding.getRoot();
        database = new SQLiteDBHelper(getActivity()).getReadableDatabase();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        isMaster = sharedPref.getBoolean("toggle_master", false);
        alliance = sharedPref.getString("alliance", null);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_fragment_log_match);

        fragmentBinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSaveDialog();
            }
        });

        fragmentBinding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentBinding.matchEditText.getText().toString().trim().isEmpty()) {
                    SQLiteDBHelper.readFromDB(database, fragmentBinding.teamNumberEditText.getText().toString());
                }
                else {
                    displayQueryResult(SQLiteDBHelper.readFromDB(database, fragmentBinding.teamNumberEditText.getText().toString(), fragmentBinding.matchEditText.getText().toString()));
                }
            }
        });

        if (isMaster) {
            fragmentBinding.scanQRButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent QRIntent = new Intent(getActivity(), QRScannerActivity.class);
                    startActivityForResult(QRIntent, RequestCodes.QR_SCAN.getValue());
                }
            });
        }
        else {
            fragmentBinding.scanQRButton.setVisibility(View.GONE);
        }

        return view;
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
        fragmentBinding.teamNumberEditText.setText(teamNumber);
        fragmentBinding.matchEditText.setText(matchNumber);
        fragmentBinding.initLineEditText.setText(initLine);
        fragmentBinding.autoLowerEditText.setText(autoLower);
        fragmentBinding.autoOuterEditText.setText(autoOuter);
        fragmentBinding.autoInnerEditText.setText(autoInner);
    }

    private void startSaveDialog() {
        try {
            processInputs();
            Bitmap code = QRCodeHelper.createQRCode(String.format("app=1732ScoutingApp,teamNumber=%s,matchNumber=%s,initLine=%s,autoLower=%s,autoOuter=%s,autoInner=%s,lower=%s,outer=%s,inner=%s,rotation=%s,position=%s,park=%s,hang=%s,level=%s,disableTime=%s,notes=%s",
                    fragmentBinding.teamNumberEditText.getText().toString(),
                    fragmentBinding.matchEditText.getText().toString(),
                    fragmentBinding.initLineEditText.getText().toString(),
                    fragmentBinding.autoLowerEditText.getText().toString(),
                    fragmentBinding.autoOuterEditText.getText().toString(),
                    fragmentBinding.autoInnerEditText.getText().toString(),
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

            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            Fragment prev = getChildFragmentManager().findFragmentByTag("Save Data Dialog");
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
                Toast.makeText(getActivity(), "You must specify a team number.", Toast.LENGTH_LONG).show();
            } else if (ex.getMessage().startsWith("match exists")) {
                Toast.makeText(getActivity(), "Match " + fragmentBinding.matchEditText.getText().toString() + " already exists.", Toast.LENGTH_LONG).show();
            } else if (ex.getMessage().startsWith("no match specified")) {
                Toast.makeText(getActivity(), "You must specify a match number.", Toast.LENGTH_LONG).show();
            } else if (ex.getMessage().startsWith("empty parameter")) {
                String msg = ex.getMessage();
                int reasonIndex = msg.indexOf(":") + 2;
                Toast.makeText(getActivity(), "You left a field empty: " + msg.substring(reasonIndex, msg.length()), Toast.LENGTH_LONG).show();
            }
        }
    }

    private ContentValues processInputs() throws IllegalArgumentException {
        if (fragmentBinding.teamNumberEditText.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("no team specified: You must specify a team number.");
        } else if (fragmentBinding.matchEditText.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("no match specified: You must specify a match number.");
        } else if (SQLiteDBHelper.matchExists(database, fragmentBinding.teamNumberEditText.getText().toString(), fragmentBinding.matchEditText.getText().toString())) {
            throw new IllegalArgumentException("match exists: Match " + fragmentBinding.matchEditText.getText().toString() + " already exists.");
        } else if (fragmentBinding.initLineEditText.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("empty parameter: Init Line");
        } else if (fragmentBinding.autoLowerEditText.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("empty parameter: Auto Lower");
        } else if (fragmentBinding.autoOuterEditText.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("empty parameter: Auto Outer");
        } else if (fragmentBinding.autoInnerEditText.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("empty parameter: Auto Inner");
        }

        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.TEAM_COLUMN_COMPETITION_ID, 0); // Have to figure out how to get the COMPETITION_ID
        values.put(SQLiteDBHelper.TEAM_COLUMN_MATCH_NUMBER, fragmentBinding.matchEditText.getText().toString());
        values.put(SQLiteDBHelper.TEAM_COLUMN_ALLIANCE, alliance);
        values.put(SQLiteDBHelper.TEAM_COLUMN_INIT_LINE, fragmentBinding.initLineEditText.getText().toString());
        values.put(SQLiteDBHelper.TEAM_COLUMN_AUTO_LOWER, fragmentBinding.autoLowerEditText.getText().toString());
        values.put(SQLiteDBHelper.TEAM_COLUMN_AUTO_OUTER, fragmentBinding.autoOuterEditText.getText().toString());
        values.put(SQLiteDBHelper.TEAM_COLUMN_AUTO_INNER, fragmentBinding.autoInnerEditText.getText().toString());
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
        SQLiteDBHelper.createTableIfNotExists(database, "frc" + fragmentBinding.teamNumberEditText.getText().toString());
        long newRowId = database.insert("frc" + fragmentBinding.teamNumberEditText.getText().toString(), null, processInputs());
        Toast.makeText(getActivity(), "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
    }

    private void displayQueryResult(Cursor cursor) {
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                Toast.makeText(getActivity(), "No results found.", Toast.LENGTH_LONG).show();
                return;
            }
            cursor.moveToFirst();
            String result = "";
            for (int i = 0; i < cursor.getCount(); i++, cursor.moveToNext()) {
                result += cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_MATCH_NUMBER)) + ": " + cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_INIT_LINE)) + "\n";
            }
            cursor.close();
            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
        }
    }
}