package org.frc1732scoutingapp;

public enum RequestCodes {
    QR_SCAN(100), RC_SIGN_IN(9001);

    private int requestCode;

    private RequestCodes(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getValue() {
        return requestCode;
    }
}
