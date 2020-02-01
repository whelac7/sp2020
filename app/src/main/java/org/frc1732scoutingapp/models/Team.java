package org.frc1732scoutingapp.models;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class Team {
    private Integer teamNumber;
    private List<IndividualMatchResult> matchResults;

    public Team(Integer teamNumber, List<IndividualMatchResult> matchResults) {
        this.teamNumber = teamNumber;
        this.matchResults = matchResults;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Team) {
            return teamNumber == ((Team)obj).teamNumber;
        }
        return false;
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public List<IndividualMatchResult> getMatchResults() {
        return matchResults;
    }
}
