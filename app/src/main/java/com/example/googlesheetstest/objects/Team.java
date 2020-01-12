package com.example.googlesheetstest.objects;

public class Team {
    private int initLine;
    private int autoLower;
    private int autoOuter;
    private int autoInner;
    private int lower;
    private int outer;
    private int inner;
    private int rotation;
    private int position;
    private int park;
    private int hang;
    private int level;
    private int disableTime;

    public Team(int initLine, int autoLower, int autoOuter, int autoInner, int lower, int outer, int inner, int rotation, int position, int park, int hang, int level, int disableTime) {
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

    public int getInitLine() {
        return initLine;
    }

    public int getAutoLower() {
        return autoLower;
    }

    public int getAutoOuter() {
        return autoOuter;
    }

    public int getAutoInner() {
        return autoInner;
    }

    public int getLower() {
        return lower;
    }

    public int getOuter() {
        return outer;
    }

    public int getInner() {
        return inner;
    }

    public int getRotation() {
        return rotation;
    }

    public int getPosition() {
        return position;
    }

    public int getPark() {
        return park;
    }

    public int getHang() {
        return hang;
    }

    public int getLevel() {
        return level;
    }

    public int getDisableTime() {
        return disableTime;
    }
}
