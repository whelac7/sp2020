package com.example.googlesheetstest;

import android.accounts.Account;
import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class SheetService {
    private static String SPREADSHEETS_SCOPE = "https://www.googleapis.com/auth/spreadsheets";

    public static void sheetTest(Account account, Context context) {
        final Account ACCOUNT = account;
        final Context CONTEXT = context;
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            protected String doInBackground(Void... params) {
                GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(CONTEXT, Collections.singleton(SPREADSHEETS_SCOPE));
                credential.setSelectedAccount(ACCOUNT);
                Sheets sheetsService = new Sheets.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                        .setApplicationName("Sheets Test")
                        .build();
                try {
                    ValueRange response = sheetsService.spreadsheets().values()
                            .get("1W6nyokTCIyZyuXbHV-zY_VHs0qT4zC2EHUAMUBJwa90", "A1:A2")
                            .execute();
                    List<List<Object>> values = response.getValues();
                    if (values == null || values.isEmpty()) {
                        System.out.println("No data found.");
                    } else {
                        System.out.println("Some test data");
                        for (List row : values) {
                            System.out.printf("%s\n", row.get(0));
                        }
                    }
                }
                catch (IOException ex) {
                    System.out.println(ex);
                }

                return "Hello";
            }
        };
        task.execute();
    }
}
