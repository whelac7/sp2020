package com.example.googlesheetstest.tasks;

import android.os.AsyncTask;

import com.example.googlesheetstest.services.SheetService;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.List;

public class AsyncGetInformationTask extends AsyncTask<Void, Void, Void> {
    private SheetService sheetService;

    public AsyncGetInformationTask(SheetService sheetService) {
        this.sheetService = sheetService;
    }

    protected Void doInBackground(Void... params) {
        try {
            ValueRange response = sheetService.getService().spreadsheets().values()
                    .get(sheetService.getSpreadsheetID(), "A1:A2")
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
}
