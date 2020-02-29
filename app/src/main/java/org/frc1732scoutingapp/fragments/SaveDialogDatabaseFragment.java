package org.frc1732scoutingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.responses.SubmitToDBCallback;

public class SaveDialogDatabaseFragment extends DialogFragment {
    private Button pushToDBButton;
    private SubmitToDBCallback DBListener;

    public void setDBListener(SubmitToDBCallback DBListener) {
        this.DBListener = DBListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_dialog_database, container, false);
        pushToDBButton = view.findViewById(R.id.pushToDBButton);

        if (savedInstanceState != null) {
            SaveDataDialog sdd = (SaveDataDialog)getParentFragment();
            if (sdd != null) {
                setDBListener(sdd.getDBListener());
            }
        }

        pushToDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBListener.saveToDB((DialogFragment)SaveDialogDatabaseFragment.this.getParentFragment());
            }
        });

        return view;
    }

}
