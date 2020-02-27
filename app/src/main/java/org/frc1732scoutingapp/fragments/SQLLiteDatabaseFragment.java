package org.frc1732scoutingapp.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.zxing.WriterException;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.activities.QRScannerActivity;
import org.frc1732scoutingapp.databinding.FragmentSqliteDatabaseBinding;
import org.frc1732scoutingapp.helpers.QRCodeHelper;
import org.frc1732scoutingapp.helpers.SQLiteDBHelper;
import org.frc1732scoutingapp.helpers.ScoutingUtils;
import org.frc1732scoutingapp.helpers.SingleToast;
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
    private String defaultAlliance;
    private String competitionCode;

    public int outerPortAuto = 0;
    public int outerPortTeleop = 0;
    public int lowerPortAuto = 0;
    public int lowerPortTeleop = 0;

    public int initLineCrossed;
    public int rotOut;
    public int posOut;
    public int parkOut;
    public int hangOut;
    public int levelOut;

    public int placeholder;

    public boolean toggleTeleopAuto = true;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sqlite_database, container, false);
        View view = fragmentBinding.getRoot();
        database = new SQLiteDBHelper(getActivity()).getReadableDatabase();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        isMaster = sharedPref.getBoolean("toggle_master", false);
        defaultAlliance = sharedPref.getString("defaultAlliance", "BLUE");
        competitionCode = sharedPref.getString("compCode", null);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Alliance, android.R.layout.simple_spinner_dropdown_item);
        fragmentBinding.allianceSpinner.setAdapter(adapter);

        if (defaultAlliance.toUpperCase().equals("BLUE")) {
            fragmentBinding.allianceSpinner.setSelection(Alliance.BLUE.getValue());
        }
        else {
            fragmentBinding.allianceSpinner.setSelection(Alliance.RED.getValue());
        }

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_fragment_log_match);

        ImageButton outerPortUp =  fragmentBinding.outerPortUp;
        ImageButton outerPortDown = fragmentBinding.outerPortDown;
        ImageButton lowerPortUp = fragmentBinding.lowerPortUp;
        ImageButton lowerPortDown = fragmentBinding.lowerPortDown;

        ToggleButton initLine = fragmentBinding.initLine;
        ToggleButton rotationControl = fragmentBinding.rotationControl;
        ToggleButton positionControl = fragmentBinding.positionControl;
        ToggleButton park = fragmentBinding.park;
        ToggleButton hang = fragmentBinding.hang;
        ToggleButton level = fragmentBinding.level;

        Button toggleTeleop = fragmentBinding.teleopToggle;
        Button toggleAuto = fragmentBinding.autoToggle;

        TextView outerPortOutputAuto = fragmentBinding.outerPortOutputAuto;
        TextView outerPortOutputTeleop = fragmentBinding.outerPortOutputTeleop;
        TextView lowerPortOutputAuto = fragmentBinding.lowerPortOutputAuto;
        TextView lowerPortOutputTeleop = fragmentBinding.lowerPortOutputTeleop;

        if (savedInstanceState != null) {
            outerPortAuto = savedInstanceState.getInt("outerPortAuto");
            outerPortTeleop = savedInstanceState.getInt("outerPortTeleop");
            lowerPortAuto = savedInstanceState.getInt("lowerPortAuto");
            lowerPortTeleop = savedInstanceState.getInt("lowerPortTeleop");
            fragmentBinding.outerPortOutputAuto.setText("" + outerPortAuto);
            fragmentBinding.outerPortOutputTeleop.setText("" + outerPortTeleop);
            fragmentBinding.lowerPortOutputAuto.setText("" + lowerPortAuto);
            fragmentBinding.lowerPortOutputTeleop.setText("" + lowerPortTeleop);

            SaveDataDialog saveDataDialog = (SaveDataDialog)getChildFragmentManager().findFragmentByTag("SaveDataDialog");
            if (saveDataDialog != null) {
                saveDataDialog.setDBListener(this);
            }
        }

        initLineCrossed = toggle(initLine);
        rotOut = toggle(rotationControl);
        posOut = toggle(positionControl);
        parkOut = toggle(park);
        hangOut = toggle(hang);
        levelOut = toggle(level);

        toggleAuto.setOnClickListener(v -> {
            SingleToast.show(getActivity(), "Inputting for auto!", Toast.LENGTH_SHORT);
            outerPortUp.setOnClickListener(v1 -> {
                outerPortAuto++;
                outerPortOutputAuto.setText("" + outerPortAuto);
            });
            outerPortDown.setOnClickListener(v1 -> {
                outerPortAuto--;
                outerPortAuto = minimum(outerPortAuto);
                outerPortOutputAuto.setText("" + outerPortAuto);
            });
            lowerPortUp.setOnClickListener(v2 -> {
                lowerPortAuto++;
                lowerPortOutputAuto.setText("" + lowerPortAuto);
            });
            lowerPortDown.setOnClickListener(v2 -> {
                lowerPortAuto--;
                lowerPortAuto = minimum(lowerPortAuto);
                lowerPortOutputAuto.setText("" + lowerPortAuto);
            });
        });
        toggleTeleop.setOnClickListener(v -> {
            SingleToast.show(getActivity(), "Inputting for teleop!", Toast.LENGTH_SHORT);
            outerPortUp.setOnClickListener(v1 -> {
                outerPortTeleop++;
                outerPortOutputTeleop.setText("" + outerPortTeleop);
            });
            outerPortDown.setOnClickListener(v1 -> {
                outerPortTeleop--;
                outerPortTeleop = minimum(outerPortTeleop);
                outerPortOutputTeleop.setText("" + outerPortTeleop);
            });
            lowerPortUp.setOnClickListener(v2 -> {
                lowerPortTeleop++;
                lowerPortOutputTeleop.setText("" + lowerPortTeleop);
            });
            lowerPortDown.setOnClickListener(v2 -> {
                lowerPortTeleop--;
                lowerPortTeleop = minimum(lowerPortTeleop);
                lowerPortOutputTeleop.setText("" + lowerPortTeleop);

            });
        });

        fragmentBinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSaveDialog();
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

    public int minimum(int c) {
        if(c < 0) {
            c = 0;
        }
        return c;
    }
    public int outputCheck(boolean b) {
        int c;
        if(b){
            c = 1;
        } else{
            c = 0;
        }
        return c;
    }

    public int toggle(ToggleButton t) {
        t.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                placeholder = outputCheck(isChecked);
            }
        });
        return placeholder;
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
                System.out.println(propertyString);
            }
            populateFields(properties.get("teamNumber"), properties.get("matchNumber"), properties.get("alliance"), properties.get("initLine"), properties.get("autoLower"), properties.get("autoOuter"), properties.get("autoInner"), properties.get("teleopLower"), properties.get("teleopOuter"), properties.get("rotation"), properties.get("position"), properties.get("parked"), properties.get("hung"), properties.get("leveled"), properties.get("disableTime"));
        }
    }

    private void populateFields(String teamNumber, String matchNumber, String alliance, String initLine, String autoLower, String autoOuter, String autoInner, String teleopLower, String teleopOuter, String rotationOut, String positionOut, String parked, String hanging, String leveled, String disableTime) {
        fragmentBinding.teamNumberEditText.setText(teamNumber);
        fragmentBinding.matchEditText.setText(matchNumber);
        if (alliance.toUpperCase().equals("BLUE")) {
            fragmentBinding.allianceSpinner.setSelection(Alliance.BLUE.getValue());
        }
        else {
            fragmentBinding.allianceSpinner.setSelection(Alliance.RED.getValue());
        }
        fragmentBinding.initLine.setChecked(ScoutingUtils.stringToBool(initLine));
        fragmentBinding.lowerPortOutputAuto.setText(autoLower);
        fragmentBinding.outerPortOutputAuto.setText(autoOuter);
        Integer.toString(0); // autoInner?
        fragmentBinding.lowerPortOutputTeleop.setText(teleopLower);
        fragmentBinding.outerPortOutputTeleop.setText(teleopOuter);
        Integer.toString(0); // teleopInner?
        fragmentBinding.rotationControl.setChecked(ScoutingUtils.stringToBool(rotationOut));
        fragmentBinding.positionControl.setChecked(ScoutingUtils.stringToBool(positionOut));
        fragmentBinding.park.setChecked(ScoutingUtils.stringToBool(parked));
        fragmentBinding.hang.setChecked(ScoutingUtils.stringToBool(hanging));
        fragmentBinding.level.setChecked(ScoutingUtils.stringToBool(leveled));
        fragmentBinding.disTime.setText(disableTime);
    }

    private void startSaveDialog() {
        try {
            processInputs();
            Bitmap code = QRCodeHelper.createQRCode(String.format("app=1732ScoutingApp,teamNumber=%s,matchNumber=%s,alliance=%s,initLine=%s,autoLower=%s,autoOuter=%s,autoInner=%s,teleopLower=%s,teleopOuter=%s,teleopInner=%s,rotation=%s,position=%s,parked=%s,hung=%s,leveled=%s,disableTime=%s,notes=%s",
                    fragmentBinding.teamNumberEditText.getText().toString(),
                    fragmentBinding.matchEditText.getText().toString(),
                    fragmentBinding.allianceSpinner.getSelectedItem().toString(),
                    fragmentBinding.initLine.isChecked(),
                    lowerPortAuto,
                    outerPortAuto,
                    Integer.toString(0),
                    lowerPortTeleop,
                    outerPortTeleop,
                    Integer.toString(0),
                    fragmentBinding.rotationControl.isChecked(),
                    fragmentBinding.positionControl.isChecked(),
                    fragmentBinding.park.isChecked(),
                    fragmentBinding.hang.isChecked(),
                    fragmentBinding.level.isChecked(),
                    fragmentBinding.disTime.getText(),
                    "Blank"
            ));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            code.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] codeInBytes = stream.toByteArray();
            Bundle bundledCode = new Bundle();
            bundledCode.putByteArray("codeInBytes", codeInBytes);

            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            Fragment prev = getChildFragmentManager().findFragmentByTag("SaveDataDialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            SaveDataDialog saveDataDialog = new SaveDataDialog();
            saveDataDialog.setDBListener(this);
            saveDataDialog.setArguments(bundledCode);
            saveDataDialog.show(ft, "SaveDataDialog");
        } catch (IOException | WriterException ex) {
            System.out.println(ex);
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage().startsWith("no team specified")) {
                SingleToast.show(getActivity(), "You must specify a team number.", Toast.LENGTH_LONG);
            } else if (ex.getMessage().startsWith("match exists")) {
                SingleToast.show(getActivity(), "Match " + fragmentBinding.matchEditText.getText().toString() + " already exists.", Toast.LENGTH_LONG);
            } else if (ex.getMessage().startsWith("no match specified")) {
                SingleToast.show(getActivity(), "You must specify a match number.", Toast.LENGTH_LONG);
            } else if (ex.getMessage().startsWith("empty parameter")) {
                String msg = ex.getMessage();
                int reasonIndex = msg.indexOf(":") + 2;
                SingleToast.show(getActivity(), "You left a field empty: " + msg.substring(reasonIndex, msg.length()), Toast.LENGTH_LONG);
            }
            else {
                throw ex;
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
        } else if (fragmentBinding.disTime.getText().toString().isEmpty()) {
            throw new IllegalArgumentException("empty parameter: Disable Time");
        }

        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_ID, 0); // Have to figure out how to get the COMPETITION_ID
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_TEAM_NUMBER, fragmentBinding.teamNumberEditText.getText().toString());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_MATCH_NUMBER, fragmentBinding.matchEditText.getText().toString());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_ALLIANCE, fragmentBinding.allianceSpinner.getSelectedItem().toString());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_INIT_LINE, ScoutingUtils.boolToInt(fragmentBinding.initLine.isChecked()));
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_AUTO_LOWER, fragmentBinding.lowerPortOutputAuto.getText().toString());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_AUTO_OUTER, fragmentBinding.outerPortOutputAuto.getText().toString());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_AUTO_INNER, 0);
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_TELEOP_LOWER, fragmentBinding.lowerPortOutputTeleop.getText().toString());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_TELEOP_OUTER, fragmentBinding.outerPortOutputTeleop.getText().toString());
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_TELEOP_INNER, 0);
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_ROTATION, ScoutingUtils.boolToInt(fragmentBinding.rotationControl.isChecked()));
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_POSITION, ScoutingUtils.boolToInt(fragmentBinding.positionControl.isChecked()));
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_PARK, ScoutingUtils.boolToInt(fragmentBinding.park.isChecked()));
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_HANG, ScoutingUtils.boolToInt(fragmentBinding.hang.isChecked()));
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_LEVEL, ScoutingUtils.boolToInt(fragmentBinding.level.isChecked()));
        values.put(SQLiteDBHelper.COMPETITION_COLUMN_DISABLE_TIME, fragmentBinding.disTime.getText().toString());
        return values;
    }

    public void saveToDB(DialogFragment dialog) {
        SQLiteDBHelper.createTableIfNotExists(database, "_" + competitionCode);
        ContentValues values = processInputs();
        long newRowId = database.insert("_" + competitionCode, null, values);
        SingleToast.show(getActivity(), "Match " + values.get(SQLiteDBHelper.COMPETITION_COLUMN_MATCH_NUMBER) + " entered for Team " + values.get(SQLiteDBHelper.COMPETITION_COLUMN_TEAM_NUMBER), Toast.LENGTH_LONG);
        dialog.dismiss();
    }

    private void displayQueryResult(Cursor cursor) {
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                SingleToast.show(getActivity(), "No results found.", Toast.LENGTH_LONG);
                return;
            }
            cursor.moveToFirst();
            String result = "";
            for (int i = 0; i < cursor.getCount(); i++, cursor.moveToNext()) {
                result += cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COMPETITION_COLUMN_MATCH_NUMBER)) + ": " + cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COMPETITION_COLUMN_INIT_LINE)) + "\n";
            }
            cursor.close();
            SingleToast.show(getActivity(), result, Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("outerPortAuto", outerPortAuto);
        savedInstanceState.putInt("outerPortTeleop", outerPortTeleop);
        savedInstanceState.putInt("lowerPortAuto", lowerPortAuto);
        savedInstanceState.putInt("lowerPortTeleop", lowerPortTeleop);
    }
}