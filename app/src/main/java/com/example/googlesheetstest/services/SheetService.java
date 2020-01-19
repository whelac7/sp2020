package com.example.googlesheetstest.services;

import android.accounts.Account;
import android.content.Context;

import com.example.googlesheetstest.models.Team;
import com.example.googlesheetstest.tasks.AsyncConnectToSheetsTask;
import com.example.googlesheetstest.tasks.AsyncCreateSheetTask;
import com.example.googlesheetstest.tasks.AsyncGetInformationTask;
import com.example.googlesheetstest.tasks.AsyncGetMatchInfoTask;
import com.example.googlesheetstest.tasks.AsyncPushInformationTask;
import com.google.api.services.sheets.v4.Sheets;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SheetService {
    private Sheets sheetsService;
    private static String SPREADSHEETS_SCOPE = "https://www.googleapis.com/auth/spreadsheets";
    private final String SPREADSHEET_ID = "1W6nyokTCIyZyuXbHV-zY_VHs0qT4zC2EHUAMUBJwa90";

    public SheetService(Account account, Context context) {
        try {
            sheetsService = new AsyncConnectToSheetsTask(account, context).execute().get();
        }
        catch (InterruptedException ex) {
            // Implement exception code
            System.out.println(ex);
        }
        catch (ExecutionException ex) {
            // Implement exception code
            System.out.println(ex);
        }
    }

    public Sheets getService() {
        return sheetsService;
    }

    public String getSpreadsheetID() {
        return SPREADSHEET_ID;
    }

    public void getInformation() {
        new AsyncGetInformationTask(this).execute();
    }

    public void pushInformation() {
        new AsyncPushInformationTask(this).execute();
    }

    public void createSheet(String name) {
        new AsyncCreateSheetTask(this, name).execute();
    }

    public void getMatchInfo() {
        new AsyncGetMatchInfoTask(this).execute();
    }

    public void postMatchInfo(List<Team> teams) {
        for (Team team : teams) {
            System.out.println(team);
        }
    }
}
