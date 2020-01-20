package org.frc1732scoutingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.responses.SubmitToDBCallback;

public class SaveDialogDatabaseFragment extends Fragment {
    private Button pushToDBButton;
    private SubmitToDBCallback DBListener;

    public SaveDialogDatabaseFragment(SubmitToDBCallback DBListener) {
        this.DBListener = DBListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_dialog_database, container, false);
        pushToDBButton = view.findViewById(R.id.pushToDBButton);

        pushToDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBListener.saveToDB();
                getActivity().onBackPressed();
            }
        });

        return view;
    }

}
