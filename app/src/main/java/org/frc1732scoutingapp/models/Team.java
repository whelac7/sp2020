package org.frc1732scoutingapp.models;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Team {
    private Integer number;
    private Integer initLine;
    private Integer autoLower;
    private Integer autoOuter;
    private Integer autoInner;
    private Integer lower;
    private Integer outer;
    private Integer inner;
    private Integer rotation;
    private Integer position;
    private Integer park;
    private Integer hang;
    private Integer level;
    private Integer disableTime;

    public Team(Integer number, Integer initLine, Integer autoLower, Integer autoOuter, Integer autoInner, Integer lower, Integer outer, Integer inner, Integer rotation, Integer position, Integer park, Integer hang, Integer level, Integer disableTime) {
        this.number = number;
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
            return number == ((Team)obj).number;
        }
        return false;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getInitLine() {
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

    public Integer getRotation() {
        return rotation;
    }

    public Integer getPosition() {
        return position;
    }

    public Integer getPark() {
        return park;
    }

    public Integer getHang() {
        return hang;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getDisableTime() {
        return disableTime;
    }
}
