package org.frc1732scoutingapp.models;

public enum Alliance {
    BLUE(0, "BLUE"), RED(1, "RED");

    private int alliance;
    private String key;

    private Alliance(int alliance, String key) {
        this.alliance = alliance;
        this.key = key;
    }

    public int getValue() {
        return alliance;
    }

    public String getKey() {
        return key;
    }
}
