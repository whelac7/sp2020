package com.example.googlesheetstest;

import android.accounts.Account;
import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;

import java.util.Collections;

public class AsyncConnectToSheetsTask extends AsyncTask<Void, Void, Sheets> {
    private Account account;
    private Context context;
    private Sheets sheetsService;
    private static String SPREADSHEETS_SCOPE = "https://www.googleapis.com/auth/spreadsheets";

    public AsyncConnectToSheetsTask(Account account, Context context) {
        this.account = account;
        this.context = context;
    }

    protected Sheets doInBackground(Void... params) {
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(context, Collections.singleton(SPREADSHEETS_SCOPE));
        credential.setSelectedAccount(account);
        Sheets service = new Sheets.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("Sheets Test")
                .build();
        return service;
    }
}
