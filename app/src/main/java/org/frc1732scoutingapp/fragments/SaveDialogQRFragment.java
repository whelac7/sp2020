package org.frc1732scoutingapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.responses.SubmitToDBCallback;

public class SaveDialogQRFragment extends Fragment {
    private Button saveToDB;
    private SubmitToDBCallback DBListener;

    public void setDBListener(SubmitToDBCallback DBListener) {
        this.DBListener = DBListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_dialog_qr, container, false);
        saveToDB = view.findViewById(R.id.saveToDBButton);

        if (savedInstanceState != null) {
            SaveDataDialog sdd = (SaveDataDialog)getParentFragment();
            if (sdd != null) {
                setDBListener(sdd.getDBListener());
            }
        }

        System.out.println("DBListener: " + DBListener);

        saveToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBListener.saveToDB((DialogFragment)SaveDialogQRFragment.this.getParentFragment());
            }
        });

        ImageView QRImage = (ImageView)view.findViewById(R.id.QRImage);
        byte[] codeInBytes = getArguments().getByteArray("codeInBytes");
        Bitmap qrCode = BitmapFactory.decodeByteArray(codeInBytes, 0, codeInBytes.length);
        QRImage.setImageBitmap(qrCode);
        return view;
    }


}
