package com.example.googlesheetstest.tasks;

import android.os.AsyncTask;

import com.example.googlesheetstest.MainActivity;
import com.example.googlesheetstest.SheetService;
import com.example.googlesheetstest.objects.Team;
import com.example.googlesheetstest.responses.GetMatchInfoResponse;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AsyncGetMatchInfoTask extends AsyncTask<Void, Void, List<Team>> {
    private SheetService sheetService;
    private GetMatchInfoResponse response;

    public AsyncGetMatchInfoTask(SheetService sheetService) {
        this.sheetService = sheetService;
        this.response = response;
    }

    protected List<Team> doInBackground(Void... params) {
        try {
            Sheets.Spreadsheets.Values.BatchGet request = sheetService.getService().spreadsheets().values()
                    .batchGet(sheetService.getSpreadsheetID());
            request.setMajorDimension("COLUMNS");
            request.setRanges(new ArrayList<String>(Arrays.asList("Match 1!B1:B14", "Match 1!D1:D14", "Match 1!F1:F14", "Match 1!L1:L14", "Match 1!N1:N14", "Match 1!P1:P14")));

            BatchGetValuesResponse response = request.execute();
            List<ValueRange> valueRanges = response.getValueRanges();

            List<Team> teams = new ArrayList<Team>();
            for (ValueRange alliance : valueRanges) {
                List<List<Object>> rawTeams = alliance.getValues();
                for (List team : rawTeams) {
                    List<Integer> mappedProperties = new ArrayList<Integer>();
                    for (int i = 0; i < 14; i++) {
                        try {
                            mappedProperties.add(Integer.parseInt(team.get(i).toString()));
                        }
                        catch (NumberFormatException ex) {
                            mappedProperties.add(null);
                        }
                    }
                    teams.add(new Team(mappedProperties.get(0), mappedProperties.get(1), mappedProperties.get(2), mappedProperties.get(3), mappedProperties.get(4), mappedProperties.get(5), mappedProperties.get(6), mappedProperties.get(7), mappedProperties.get(8), mappedProperties.get(9), mappedProperties.get(10), mappedProperties.get(11), mappedProperties.get(12), mappedProperties.get(13)));
                }
            }
            return teams;
        }
        catch (IOException ex) {
            System.out.println(ex);
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Team> teams) {
        sheetService.postMatchInfo(teams);
    }
}
