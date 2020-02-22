package org.frc1732scoutingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.helpers.SQLiteDBHelper;
import org.frc1732scoutingapp.models.MatchResult;
import org.frc1732scoutingapp.models.RequestCodes;
import org.frc1732scoutingapp.models.Team;
import org.frc1732scoutingapp.services.SheetService;

import java.util.ArrayList;
import java.util.List;

public class SyncSheetsFragment extends Fragment {
    private EditText teamNumberEditText;
    private Button syncSheetsButton;
    private Button syncAllSheetsButton;
    private SQLiteDatabase database;
    private GoogleSignInOptions mGoogleSignInOptions;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount mGoogleSignInAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync_sheets, container, false);
        if (isNetworkAvailable()) {
            teamNumberEditText = view.findViewById(R.id.teamNumberEditText);
            syncSheetsButton = view.findViewById(R.id.syncSheetsButton);
            syncAllSheetsButton = view.findViewById(R.id.syncAllSheetsButton);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_fragment_sync_sheets);
            signIntoGoogle();

            // TODO: Application breaks if the team number is not valid - fix.
            syncSheetsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (database == null) {
                        database = new SQLiteDBHelper(getContext()).getReadableDatabase();
                    }
                    //syncToSheets(teamNumberEditText.getText().toString());
                }
            });

            syncAllSheetsButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (database == null) {
                       database = new SQLiteDBHelper(getContext()).getReadableDatabase();
                   }
                   syncAllToSheets();
               }
            });

            return view;
        }
        else {
            Toast.makeText(getContext(), "You must be connected to the internet to sync to Google Sheets.", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    //TODO: Need to find a way to keep team number associated with IndividualMatchResult
    private void syncAllToSheets() {
        List<MatchResult> matchResults = SQLiteDBHelper.getAllTeamResults(database);
        SheetService sheetService = new SheetService(mGoogleSignInAccount.getAccount(), getContext());
        sheetService.pushMatchInfo(matchResults);

    }

//    private Team parseTeam(String team) {
//        Team teamObj = SQLiteDBHelper.parseTeamMatches(database, team);
//
//        if (teamObj != null) {
//            return teamObj;
//        }
//        else {
//            Toast.makeText(getContext(), "No results found.", Toast.LENGTH_LONG).show();
//            return null;
//        }
//    }

//    private void syncToSheets(String teamNumber, List<Team> matchResults) {
//        SheetService sheetService = new SheetService(mGoogleSignInAccount.getAccount(), getContext());
//        sheetService.pushMatchInfo(matchResults);
//    }

//    //TODO: Find a smoother way to sync a single team without using placeholderList
//    private void syncToSheets(String teamNumber) {
//        Team teamResults = parseTeam(teamNumber);
//        List<Team> placeholderList = new ArrayList<Team>();
//        placeholderList.add(teamResults);
//        SheetService sheetService = new SheetService(mGoogleSignInAccount.getAccount(), getContext());
//        sheetService.pushMatchInfo(placeholderList);
//    }

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
