package org.frc1732scoutingapp.responses;

import org.frc1732scoutingapp.objects.Team;

import java.util.List;

public class GetMatchInfoResponse {
    private List<Team> teams;

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
