package com.example.googlesheetstest.tasks;

import android.os.AsyncTask;

import com.example.googlesheetstest.services.SheetService;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AsyncPushInformationTask extends AsyncTask<Void, Void, Void> {
    private SheetService sheetService;

    public AsyncPushInformationTask(SheetService sheetService) {
        this.sheetService = sheetService;
    }

    protected Void doInBackground(Void... params) {
        try {
            ValueRange requestBody = new ValueRange();
            requestBody.setMajorDimension("ROWS");

            // Input needs to be a list of a list of objects (the outer list is a row, and the objects inside the inner list is each column)
            List<List<Object>> input = new ArrayList<List<Object>>();
            input.add(new ArrayList<Object>());
            input.add(new ArrayList<Object>());
            input.get(0).add("Hello4");
            input.get(0).add("Hello293");
            input.get(1).add("Hello6");
            requestBody.setValues(input);

            UpdateValuesResponse response = sheetService.getService().spreadsheets().values().update(sheetService.getSpreadsheetID(), "A1:B", requestBody)
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
}
