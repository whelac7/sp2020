package org.frc1732scoutingapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import org.frc1732scoutingapp.R;

public class SaveDialogBluetoothFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_dialog_bluetooth, container, false);
        ((TextView)view.findViewById(R.id.testText)).setText("XD!");
        return view;
    }
}
