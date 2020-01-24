package org.frc1732scoutingapp.services;

import android.accounts.Account;
import android.content.Context;

import org.frc1732scoutingapp.models.Team;
import org.frc1732scoutingapp.tasks.AsyncConnectToSheetsTask;
import org.frc1732scoutingapp.tasks.AsyncCreateSheetTask;
import org.frc1732scoutingapp.tasks.AsyncGetInformationTask;
import org.frc1732scoutingapp.tasks.AsyncGetMatchInfoTask;
import org.frc1732scoutingapp.tasks.AsyncPushInformationTask;
import org.frc1732scoutingapp.tasks.AsyncPushMatchInfoTask;

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

    public void pushMatchInfo(List<Team> matchResults) {
        new AsyncPushMatchInfoTask(this, matchResults).execute();
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
