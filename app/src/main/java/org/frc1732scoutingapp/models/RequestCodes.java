package org.frc1732scoutingapp.models;

public enum RequestCodes {
    QR_SCAN(100), SYNC_TO_SHEETS(101), RC_SIGN_IN(9001);

    private int requestCode;

    private RequestCodes(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getValue() {
        return requestCode;
    }
}
