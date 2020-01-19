package com.example.googlesheetstest.responses;

import com.example.googlesheetstest.models.Team;

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
