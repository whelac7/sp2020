package org.frc1732scoutingapp.tasks;

import android.os.AsyncTask;

import org.frc1732scoutingapp.models.Team;
import org.frc1732scoutingapp.services.SheetService;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AsyncPushMatchInfoTask extends AsyncTask<Void, Void, Void> {
    private SheetService sheetService;
    private List<Team> teams;

    public AsyncPushMatchInfoTask(SheetService sheetService, List<Team> teams) {
        this.sheetService = sheetService;
        this.teams = teams;
    }

    //TODO: Maybe find a cleaner way than using a ssRow counter and several .get() methods to make new rows for each match
    protected Void doInBackground(Void... params) {
        try {
            ValueRange requestBody = new ValueRange();
            requestBody.setMajorDimension("ROWS");

            // Input needs to be a list of a list of objects (the outer list is a row, and the objects inside the inner list is each column)
            List<List<Object>> input = new ArrayList<List<Object>>();
            int ssRow = 0;
            for (int i = 0; i < teams.size(); i++) {
                for (int j = 0; j < teams.get(i).getMatchResults().size(); j++, ssRow++) {
                    input.add(new ArrayList<Object>());
                    input.get(ssRow).add(teams.get(i).getTeamNumber());
                    input.get(ssRow).add(teams.get(i).getMatchResults().get(j).getMatchNumber());
                    input.get(ssRow).add(teams.get(i).getMatchResults().get(j).getInitLine());
                    input.get(ssRow).add(teams.get(i).getMatchResults().get(j).getAutoLower());
                    input.get(ssRow).add(teams.get(i).getMatchResults().get(j).getAutoOuter());
                    input.get(ssRow).add(teams.get(i).getMatchResults().get(j).getAutoInner());
                    input.get(ssRow).add(teams.get(i).getMatchResults().get(j).getLower());
                    input.get(ssRow).add(teams.get(i).getMatchResults().get(j).getOuter());
                    input.get(ssRow).add(teams.get(i).getMatchResults().get(j).getInner());
                    input.get(ssRow).add(teams.get(i).getMatchResults().get(j).getPosition());
                    input.get(ssRow).add(teams.get(i).getMatchResults().get(j).getRotation());
                    input.get(ssRow).add(teams.get(i).getMatchResults().get(j).getPark());
                    input.get(ssRow).add(teams.get(i).getMatchResults().get(j).getHang());
                    input.get(ssRow).add(teams.get(i).getMatchResults().get(j).getLevel());
                    input.get(ssRow).add(teams.get(i).getMatchResults().get(j).getDisableTime());
                }
            }
            requestBody.setValues(input);

            UpdateValuesResponse response = sheetService.getService().spreadsheets().values().update(sheetService.getSpreadsheetID(), "A2:O", requestBody)
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
