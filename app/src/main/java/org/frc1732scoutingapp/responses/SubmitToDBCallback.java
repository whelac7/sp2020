package org.frc1732scoutingapp.responses;

import android.os.Parcelable;

import androidx.fragment.app.DialogFragment;

public interface SubmitToDBCallback {
    public void saveToDB(DialogFragment dialog);
}
