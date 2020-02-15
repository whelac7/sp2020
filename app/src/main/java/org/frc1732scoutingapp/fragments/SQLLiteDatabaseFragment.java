
package org.frc1732scoutingapp.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.zxing.WriterException;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.activities.QRScannerActivity;
import org.frc1732scoutingapp.databinding.FragmentSqliteDatabaseBinding;
import org.frc1732scoutingapp.helpers.QRCodeHelper;
import org.frc1732scoutingapp.helpers.SQLiteDBHelper;
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
;
        initLineCrossed = toggle(initLine);
        rotOut = toggle(rotationControl);
        posOut = toggle(positionControl);
        parkOut = toggle(park);
        hangOut = toggle(hang);
        levelOut = toggle(level);

        toggleAuto.setOnClickListener(v -> {
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



//        fragmentBinding.saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startSaveDialog();
//            }
//        });

//        fragmentBinding.scanQRButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent QRIntent = new Intent(getActivity(), QRScannerActivity.class);
//                startActivityForResult(QRIntent, RequestCodes.QR_SCAN.getValue());
//            }
//        });
//
//        return view;
//    }

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
            populateFields(properties.get("teamNumber"), properties.get("matchNumber"), properties.get("initLine"), properties.get("autoLower"), properties.get("autoOuter"), properties.get("autoInner"), properties.get("teleopLower"), properties.get("teleopOuter"), properties.get("teleopInner"), properties.get("rotationControl"), properties.get("positionControl"), properties.get("park"), properties.get("hang"), properties.get("level"), properties.get("disableTime"));
        }
    }

    private void populateFields(String teamNumber, String matchNumber, String initLine, String autoLower, String autoOuter, String autoInner, String teleopLower, String teleopOuter, String teleopInner, String rotationControl, String positionControl, String park, String hang, String level, String disableTime) {
        fragmentBinding.teamNumberEditText.setText(teamNumber);
        fragmentBinding.matchEditText.setText(matchNumber);
        Integer.toString(initLineCrossed);
        Integer.toString(lowerPortAuto);
        Integer.toString(outerPortAuto);
        Integer.toString(0);
        Integer.toString(lowerPortTeleop);
        Integer.toString(outerPortTeleop);
        Integer.toString(0);
        Integer.toString(rotOut);
        Integer.toString(posOut);
        Integer.toString(parkOut);
        Integer.toString(hangOut);
        Integer.toString(levelOut);
        fragmentBinding.disTime.getText();
    }

    private void startSaveDialog() {
        try {
            processInputs();
            Bitmap code = QRCodeHelper.createQRCode(String.format("app=1732ScoutingApp,teamNumber=%s,matchNumber=%s,initLine=%s,autoLower=%s,autoOuter=%s,autoInner=%s,lower=%s,outer=%s,inner=%s,rotation=%s,position=%s,park=%s,hang=%s,level=%s,disableTime=%s,notes=%s",
                    fragmentBinding.teamNumberEditText.getText().toString(),
                    fragmentBinding.matchEditText.getText().toString(),
                    Integer.toString(initLineCrossed),
                    Integer.toString(lowerPortAuto),
                    Integer.toString(outerPortAuto),
                    Integer.toString(0),
                    Integer.toString(lowerPortTeleop),
                    Integer.toString(outerPortTeleop),
                    Integer.toString(0),
                    Integer.toString(rotOut),
                    Integer.toString(posOut),
                    Integer.toString(parkOut),
                    Integer.toString(hangOut),
                    Integer.toString(levelOut),
                    fragmentBinding.disTime.getText()
            ));

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
//        if (fragmentBinding.teamNumberEditText.getText().toString().trim().isEmpty()) {
//            throw new IllegalArgumentException("no team specified: You must specify a team number.");
//        } else if (fragmentBinding.matchEditText.getText().toString().trim().isEmpty()) {
//            throw new IllegalArgumentException("no match specified: You must specify a match number.");
//        } else if (SQLiteDBHelper.matchExists(database, fragmentBinding.teamNumberEditText.getText().toString(), fragmentBinding.matchEditText.getText().toString())) {
//            throw new IllegalArgumentException("match exists: Match " + fragmentBinding.matchEditText.getText().toString() + " already exists.");
//        } else if (fragmentBinding.initLineEditText.getText().toString().trim().isEmpty()) {
//            throw new IllegalArgumentException("empty parameter: Init Line");
//        } else if (fragmentBinding.autoLowerEditText.getText().toString().trim().isEmpty()) {
//            throw new IllegalArgumentException("empty parameter: Auto Lower");
//        } else if (fragmentBinding.autoOuterEditText.getText().toString().trim().isEmpty()) {
//            throw new IllegalArgumentException("empty parameter: Auto Outer");
//        } else if (fragmentBinding.autoInnerEditText.getText().toString().trim().isEmpty()) {
//            throw new IllegalArgumentException("empty parameter: Auto Inner");
//        }

        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.TEAM_COLUMN_COMPETITION_ID, 0); // Have to figure out how to get the COMPETITION_ID
        values.put(SQLiteDBHelper.TEAM_COLUMN_MATCH_NUMBER, fragmentBinding.matchEditText.getText().toString());
        values.put(SQLiteDBHelper.TEAM_COLUMN_INIT_LINE, initLineCrossed);
        values.put(SQLiteDBHelper.TEAM_COLUMN_AUTO_LOWER, lowerPortAuto);
        values.put(SQLiteDBHelper.TEAM_COLUMN_AUTO_OUTER, outerPortAuto);
        values.put(SQLiteDBHelper.TEAM_COLUMN_AUTO_INNER, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_LOWER, lowerPortTeleop);
        values.put(SQLiteDBHelper.TEAM_COLUMN_OUTER, outerPortTeleop);
        values.put(SQLiteDBHelper.TEAM_COLUMN_INNER, 0);
        values.put(SQLiteDBHelper.TEAM_COLUMN_ROTATION, rotOut);
        values.put(SQLiteDBHelper.TEAM_COLUMN_POSITION, posOut);
        values.put(SQLiteDBHelper.TEAM_COLUMN_PARK, parkOut);
        values.put(SQLiteDBHelper.TEAM_COLUMN_HANG, hangOut);
        values.put(SQLiteDBHelper.TEAM_COLUMN_LEVEL, levelOut);
        values.put(SQLiteDBHelper.TEAM_COLUMN_DISABLE_TIME, fragmentBinding.disTime.getText().toString());
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