package com.frc1732scoutingapp.tasks;

import android.os.AsyncTask;

import com.frc1732scoutingapp.services.SheetService;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.SheetProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AsyncCreateSheetTask extends AsyncTask<Void, Void, Void> {
    private SheetService sheetService;
    private String name;

    public AsyncCreateSheetTask(SheetService sheetService, String name) {
        this.sheetService = sheetService;
        this.name = name;
    }

    protected Void doInBackground(Void... params) {
        try {
            BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest();
            List<Request> requests = new ArrayList<Request>();
            requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle(name))));
            requestBody.setRequests(requests);
            sheetService.getService().spreadsheets().batchUpdate(sheetService.getSpreadsheetID(), requestBody).execute();
        }
        catch (IOException ex) {
            // Implement exception code
            System.out.println(ex);
        }
        return null;
    }
}
