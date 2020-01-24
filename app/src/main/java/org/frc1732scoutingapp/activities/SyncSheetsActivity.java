package org.frc1732scoutingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.helpers.SQLiteDBHelper;
import org.frc1732scoutingapp.models.RequestCodes;
import org.frc1732scoutingapp.models.Team;
import org.frc1732scoutingapp.services.SheetService;
import org.frc1732scoutingapp.tasks.AsyncPushMatchInfoTask;

import java.util.ArrayList;
import java.util.List;

public class SyncSheetsActivity extends Fragment {
    private EditText teamNumberEditText;
    private Button syncSheetsButton;
    private Button syncAllSheetsButton;
    private SQLiteDatabase database;
    private GoogleSignInOptions mGoogleSignInOptions;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount mGoogleSignInAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sync_sheets, container, false);
        if (isNetworkAvailable()) {
            teamNumberEditText = view.findViewById(R.id.teamNumberEditText);
            syncSheetsButton = view.findViewById(R.id.syncSheetsButton);
            syncAllSheetsButton = view.findViewById(R.id.syncAllSheetsButton);
            signIntoGoogle();

            // TODO: Application breaks if the team number is not valid - fix.
            syncSheetsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (database == null) {
                        database = new SQLiteDBHelper(getContext()).getReadableDatabase();
                    }
                    syncToSheets(teamNumberEditText.getText().toString());
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

    private void syncAllToSheets() {
        Cursor teams = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        List<Team> allMatchResults = new ArrayList<Team>();
        if (teams.moveToFirst()) {
            for (int i = 0; i < teams.getCount(); i++, teams.moveToNext()) {
                String teamString = teams.getString(0);
                String target = "frc";
                if (teamString.startsWith(target)) {
                    String teamNumber = teamString.substring(3);
                    System.out.println(teamNumber);
                    allMatchResults.addAll(parseMatches(teamNumber));
                }
            }
        }
        teams.close();

        SheetService sheetService = new SheetService(mGoogleSignInAccount.getAccount(), getContext());
        sheetService.pushMatchInfo(allMatchResults);
    }

    private List<Team> parseMatches(String teamNumber) {
        System.out.println(teamNumber);
        Cursor cursor = SQLiteDBHelper.readFromDB(database, teamNumber);
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No results found.", Toast.LENGTH_LONG).show();
            return null;
        }
        cursor.moveToFirst();
        String result = "";
        ArrayList<Team> matchResults = new ArrayList<Team>();
        for (int i = 0; i < cursor.getCount(); i++, cursor.moveToNext()) {
            matchResults.add(new Team(
                    tryParseInt(teamNumber),
                    tryParseInt(cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_MATCH_NUMBER))),
                    tryParseInt(cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_INIT_LINE))),
                    tryParseInt(cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_AUTO_LOWER))),
                    tryParseInt(cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_AUTO_OUTER))),
                    tryParseInt(cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_AUTO_INNER))),
                    tryParseInt(cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_LOWER))),
                    tryParseInt(cursor.getString(cursor.getColumnIndex((SQLiteDBHelper.TEAM_COLUMN_OUTER)))),
                    tryParseInt(cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_INNER))),
                    tryParseInt(cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_POSITION))),
                    tryParseInt(cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_ROTATION))),
                    tryParseInt(cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_PARK))),
                    tryParseInt(cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_HANG))),
                    tryParseInt(cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_LEVEL))),
                    tryParseInt(cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.TEAM_COLUMN_DISABLE_TIME)))
            ));
        }
        cursor.close();

        return matchResults;
    }

    private void syncToSheets(String teamNumber, List<Team> matchResults) {
        SheetService sheetService = new SheetService(mGoogleSignInAccount.getAccount(), getContext());
        sheetService.pushMatchInfo(matchResults);
    }

    private void syncToSheets(String teamNumber) {
        List<Team> matchResults = parseMatches(teamNumber);
        SheetService sheetService = new SheetService(mGoogleSignInAccount.getAccount(), getContext());
        sheetService.pushMatchInfo(matchResults);
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

    private Integer tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
