package org.frc1732scoutingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

public class SyncSheetsActivity extends AppCompatActivity {
    private EditText teamNumberEditText;
    private Button syncSheetsButton;
    private SQLiteDatabase database;
    private GoogleSignInOptions mGoogleSignInOptions;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount mGoogleSignInAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNetworkAvailable()) {
            setContentView(R.layout.activity_sync_sheets);
            teamNumberEditText = findViewById(R.id.teamNumberEditText);
            syncSheetsButton = findViewById(R.id.syncSheetsButton);
            signIntoGoogle();

            syncSheetsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (database == null) {
                        database = new SQLiteDBHelper(SyncSheetsActivity.this).getReadableDatabase();
                    }
                    syncToSheets();
                }
            });
        }
        else {
            Toast.makeText(this, "You must be connected to the internet to sync to Google Sheets.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void syncToSheets() {
        Cursor cursor = SQLiteDBHelper.readFromDB(database, teamNumberEditText.getText().toString());
        if (cursor.getCount() == 0) {
            Toast.makeText(SyncSheetsActivity.this, "No results found.", Toast.LENGTH_LONG).show();
            return;
        }
        cursor.moveToFirst();
        String result = "";
        ArrayList<Team> matchResults = new ArrayList<Team>();
        for (int i = 0; i < cursor.getCount(); i++, cursor.moveToNext()) {
            matchResults.add(new Team(
                    tryParseInt(teamNumberEditText.getText().toString()),
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
        Toast.makeText(SyncSheetsActivity.this, result, Toast.LENGTH_LONG).show();

        SheetService sheetService = new SheetService(mGoogleSignInAccount.getAccount(), this);
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
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions);
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
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
