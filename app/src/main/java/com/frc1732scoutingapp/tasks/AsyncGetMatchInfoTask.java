package com.frc1732scoutingapp.tasks;

import android.os.AsyncTask;

import com.frc1732scoutingapp.services.SheetService;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.List;

public class AsyncGetMatchInfoTask extends AsyncTask<Void, Void, Void> {
    private SheetService sheetService;

    public AsyncGetMatchInfoTask(SheetService sheetService) {
        this.sheetService = sheetService;
    }

    protected Void doInBackground(Void... params) {
        try {
            ValueRange response = sheetService.getService().spreadsheets().values()
                    .get(sheetService.getSpreadsheetID(), "B1:Q14")
                    .execute();
            List<List<Object>> values = response.getValues();
            for (List row : values) {
                System.out.printf("%s\n", row.get(0));
            }
        }
        catch (IOException ex) {
            System.out.println(ex);
        }

        return null;
    }
}
