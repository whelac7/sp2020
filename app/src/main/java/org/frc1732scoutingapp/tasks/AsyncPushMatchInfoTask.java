package org.frc1732scoutingapp.tasks;

import android.os.AsyncTask;

import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.frc1732scoutingapp.models.MatchResult;
import org.frc1732scoutingapp.models.Team;
import org.frc1732scoutingapp.services.SheetService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AsyncPushMatchInfoTask extends AsyncTask<Void, Void, Void> {
    private SheetService sheetService;
    private List<MatchResult> matchResults;

    public AsyncPushMatchInfoTask(SheetService sheetService, List<MatchResult> matchResults) {
        this.sheetService = sheetService;
        this.matchResults = matchResults;
    }

    //TODO: Maybe find a cleaner way than using a ssRow counter and several .get() methods to make new rows for each match
    protected Void doInBackground(Void... params) {
        try {
            ValueRange requestBody = new ValueRange();
            requestBody.setMajorDimension("ROWS");

            // Input needs to be a list of a list of objects (the outer list is a row, and the objects inside the inner list is each column)
            List<List<Object>> input = new ArrayList<List<Object>>();
            int ssRow = 0;
            for (int i = 0; i < matchResults.size(); i++) {
                input.add(new ArrayList<Object>());
                input.get(i).add(matchResults.get(i).getTeamNumber());
                input.get(i).add(matchResults.get(i).getMatchNumber());
                input.get(i).add(matchResults.get(i).getAlliance());
                input.get(i).add(matchResults.get(i).getInitLine());
                input.get(i).add(matchResults.get(i).getAutoLower());
                input.get(i).add(matchResults.get(i).getAutoOuter());
                input.get(i).add(matchResults.get(i).getAutoInner());
                input.get(i).add(matchResults.get(i).getLower());
                input.get(i).add(matchResults.get(i).getOuter());
                input.get(i).add(matchResults.get(i).getInner());
                input.get(i).add(matchResults.get(i).getPosition());
                input.get(i).add(matchResults.get(i).getRotation());
                input.get(i).add(matchResults.get(i).getPark());
                input.get(i).add(matchResults.get(i).getHang());
                input.get(i).add(matchResults.get(i).getLevel());
                input.get(i).add(matchResults.get(i).getDisableTime());
            }
            requestBody.setValues(input);

            UpdateValuesResponse response = sheetService.getService().spreadsheets().values().update(sheetService.getSpreadsheetID(), "A2:P", requestBody)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
            System.out.println(response);
        }
        catch (IOException ex) {
            // TODO: Implement exception code
            System.out.println(ex);
        }

        return null;
    }
}
