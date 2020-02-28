package org.frc1732scoutingapp.models;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class MatchResult {
    private Integer teamNumber;
    private Integer matchNumber;
    private String alliance;
    private boolean initLine;
    private Integer autoLower;
    private Integer autoOuter;
    private Integer autoInner;
    private Integer lower;
    private Integer outer;
    private Integer inner;
    private boolean rotation;
    private boolean position;
    private boolean park;
    private boolean hang;
    private boolean level;
    private Integer disableTime;

    public MatchResult(Integer teamNumber, Integer matchNumber, String alliance, Boolean initLine, Integer autoLower, Integer autoOuter, Integer autoInner, Integer lower, Integer outer, Integer inner, Boolean rotation, Boolean position, Boolean park, Boolean hang, Boolean level, Integer disableTime) {
        this.teamNumber = teamNumber;
        this.matchNumber = matchNumber;
        this.alliance = alliance;
        this.initLine = initLine;
        this.autoLower = autoLower;
        this.autoOuter = autoOuter;
        this.autoInner = autoInner;
        this.lower = lower;
        this.outer = outer;
        this.inner = inner;
        this.rotation = rotation;
        this.position = position;
        this.park = park;
        this.hang = hang;
        this.level = level;
        this.disableTime = disableTime;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Team) {
            return teamNumber == ((MatchResult)obj).teamNumber;
        }
        return false;
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public Integer getMatchNumber() {
        return matchNumber;
    }

    public String getAlliance() {
        return alliance;
    }

    public boolean getInitLine() {
        return initLine;
    }

    public Integer getAutoLower() {
        return autoLower;
    }

    public Integer getAutoOuter() {
        return autoOuter;
    }

    public Integer getAutoInner() {
        return autoInner;
    }

    public Integer getLower() {
        return lower;
    }

    public Integer getOuter() {
        return outer;
    }

    public Integer getInner() {
        return inner;
    }

    public boolean getRotation() {
        return rotation;
    }

    public boolean getPosition() {
        return position;
    }

    public boolean getPark() {
        return park;
    }

    public boolean getHang() {
        return hang;
    }

    public boolean getLevel() {
        return level;
    }

    public Integer getDisableTime() {
        return disableTime;
    }
}
