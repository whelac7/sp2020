package org.frc1732scoutingapp.fragments;


import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.helpers.IOHelper;
import org.frc1732scoutingapp.helpers.JsonHelper;
import org.frc1732scoutingapp.helpers.SQLiteDBHelper;
import org.frc1732scoutingapp.helpers.ScoutingUtils;
import org.frc1732scoutingapp.helpers.SingleToast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonToDBFragment extends Fragment {
    private Spinner competitionCodeSpinner;
    private Button buildJsonButton;
    private Button syncJsonToDBButton;
    private Button buildAndSyncButton;
    private SQLiteDatabase database;
    private String compCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_json_to_db, container, false);
        buildJsonButton = view.findViewById(R.id.buildJsonButton);
        syncJsonToDBButton = view.findViewById(R.id.syncJsonToDb);
        buildAndSyncButton = view.findViewById(R.id.buildAndSyncButton);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        compCode = sharedPref.getString("compCode", null);

        File competitionData = new File(IOHelper.getRootCompDataPath(getActivity()));
        if (competitionData.exists()) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Merge JSON into DB");
            database = new SQLiteDBHelper(getContext()).getReadableDatabase();
            File[] competitions = competitionData.listFiles();
            List<String> competitionList = new ArrayList<String>();
            for (File competition : competitions) {
                if (competition.isDirectory()) {
                    competitionList.add(competition.getName());
                }
            }
            if (competitionList.size() == 0) {
                competitionList.add("No competitions found.");
            }

            competitionCodeSpinner = view.findViewById(R.id.competitionCodeSpinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.competition_spinner_dropdown, competitionList);
            adapter.setDropDownViewResource(R.layout.competition_spinner_dropdown);
            competitionCodeSpinner.setAdapter(adapter);
            competitionCodeSpinner.setSelection(ScoutingUtils.getDefaultSpinnerSetting(competitionCodeSpinner, compCode));

            buildJsonButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JsonHelper.buildCompetitionJson(getActivity(), competitionCodeSpinner.getSelectedItem().toString());
                    SingleToast.show(getActivity(), "Competition JSON built.", Toast.LENGTH_SHORT);
                }
            });

            syncJsonToDBButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        SQLiteDBHelper.insertCompetitionJsonToDB(database, getActivity(), competitionCodeSpinner.getSelectedItem().toString());
                        SingleToast.show(getActivity(), "Competition JSON: " + competitionCodeSpinner.getSelectedItem().toString() + " merged into database.", Toast.LENGTH_SHORT);
                    }
                    catch (IOException ex) {
                        ScoutingUtils.logException(ex, "JsonToDBFragment.syncJsonToDB");
                        SingleToast.show(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT);
                    }
                }
            });

            buildAndSyncButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String competition = competitionCodeSpinner.getSelectedItem().toString();
                        JsonHelper.buildCompetitionJson(getActivity(), competition);
                        SQLiteDBHelper.insertCompetitionJsonToDB(database, getActivity(), competition);
                        SingleToast.show(getActivity(), "Competition JSON: " + competition + " built and merged into database.", Toast.LENGTH_SHORT);
                    }
                    catch (IOException ex) {
                        ScoutingUtils.logException(ex, "JsonToDBFragment.buildAndSyncJsonToDB");
                        SingleToast.show(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT);
                    }
                }
            });

            return view;
        }
        else {
            SingleToast.show(getActivity(), "No match data has been saved through JSON.", Toast.LENGTH_LONG);
            return null;
        }
    }

}
