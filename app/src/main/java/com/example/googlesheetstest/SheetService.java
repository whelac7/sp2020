package com.example.googlesheetstest;

import android.accounts.Account;
import android.content.Context;
import android.os.AsyncTask;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SheetService {
    private Sheets sheetsService;
    private static String SPREADSHEETS_SCOPE = "https://www.googleapis.com/auth/spreadsheets";

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

    public void getInformation() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                try {
                    ValueRange response = sheetsService.spreadsheets().values()
                            .get("1W6nyokTCIyZyuXbHV-zY_VHs0qT4zC2EHUAMUBJwa90", "A1:A2")
                            .execute();
                    List<List<Object>> values = response.getValues();
                    if (values == null || values.isEmpty()) {
                        System.out.println("No data found.");
                    }
                    else {
                        System.out.println("Some test data");
                        for (List row : values) {
                            System.out.printf("%s\n", row.get(0));
                        }
                    }
                }
                catch (IOException ex) {
                    System.out.println(ex);
                }

                return null;
            }
        }.execute();
    }

    public void pushInformation() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                try {
                    ValueRange requestBody = new ValueRange();
                    requestBody.setMajorDimension("ROWS");

                    // Input needs to be a list of a list of objects (the outer list is a row, and the objects inside the inner list is each column)
                    List<List<Object>> input = new ArrayList<List<Object>>();
                    input.add(new ArrayList<Object>());
                    input.add(new ArrayList<Object>());
                    input.get(0).add("Hello4");
                    input.get(1).add("Hello6");
                    requestBody.setValues(input);

                    UpdateValuesResponse response = sheetsService.spreadsheets().values().update("1W6nyokTCIyZyuXbHV-zY_VHs0qT4zC2EHUAMUBJwa90", "A1:A2", requestBody)
                            .setValueInputOption("USER_ENTERED")
                            .execute();
                    System.out.println(response);
                }
                catch (IOException ex) {
                    // Implement exception code
                    System.out.println(ex);
                }

                return null;
            }
        }.execute();
    }
}
