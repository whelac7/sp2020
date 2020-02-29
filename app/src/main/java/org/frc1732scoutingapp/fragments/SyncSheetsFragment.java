package org.frc1732scoutingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.helpers.SQLiteDBHelper;
import org.frc1732scoutingapp.helpers.ScoutingUtils;
import org.frc1732scoutingapp.helpers.SingleToast;
import org.frc1732scoutingapp.models.MatchResult;
import org.frc1732scoutingapp.models.RequestCodes;
import org.frc1732scoutingapp.services.SheetService;

import java.util.List;

public class SyncSheetsFragment extends Fragment {
    private Spinner competitionCodeSpinner;
    private Button syncSheetsButton;
    private SQLiteDatabase database;
    private String compCode;
    private GoogleSignInOptions mGoogleSignInOptions;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount mGoogleSignInAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync_sheets, container, false);
        if (isNetworkAvailable()) {
            syncSheetsButton = view.findViewById(R.id.syncSheetsButton);
            database = new SQLiteDBHelper(getContext()).getReadableDatabase();

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            compCode = sharedPref.getString("compCode", null);

            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_fragment_sync_sheets);
            signIntoGoogle();

            List<String> competitions = SQLiteDBHelper.getCompetitions(database);
            competitionCodeSpinner = view.findViewById(R.id.competitionCodeSpinner);
            if (competitions.size() == 0) {
                competitions.add("No competitions saved.");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.competition_spinner_dropdown, competitions);
            adapter.setDropDownViewResource(R.layout.competition_spinner_dropdown);
            competitionCodeSpinner.setAdapter(adapter);
            competitionCodeSpinner.setSelection(ScoutingUtils.getDefaultSpinnerSetting(competitionCodeSpinner, compCode));

            syncSheetsButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   syncToSheets();
               }
            });

            return view;
        }
        else {
            SingleToast.show(getActivity(), "You must be connected to the internet to sync to Google Sheets.", Toast.LENGTH_LONG);
            return null;
        }
    }

    private void syncToSheets() {
        try {
            List<MatchResult> matchResults = SQLiteDBHelper.getMatchResults(database, competitionCodeSpinner.getSelectedItem().toString());
            SheetService sheetService = new SheetService(mGoogleSignInAccount.getAccount(), getContext());
            sheetService.pushMatchInfo(matchResults);
            SingleToast.show(getActivity(), "Data pushed to sheets.", Toast.LENGTH_SHORT);
        }
        catch (SQLiteException ex) {
            if (ex.getMessage().indexOf("no such table") >= 0) {
                SingleToast.show(getActivity(), "You entered an invalid competition code.", Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.RC_SIGN_IN.getValue()) {
            try {
                mGoogleSignInAccount = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
            }
            catch (ApiException ex) {
                Log.w("App", "signInResult:failed code=" + ex.getStatusCode());
            }
        }
    }

    private void signIntoGoogle() {
        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope("https://www.googleapis.com/auth/spreadsheets"))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), mGoogleSignInOptions);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RequestCodes.RC_SIGN_IN.getValue());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
