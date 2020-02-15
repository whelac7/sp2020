package org.frc1732scoutingapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import org.frc1732scoutingapp.R;

public class SaveDialogQRFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_dialog_qr, container, false);
        ImageView QRImage = (ImageView)view.findViewById(R.id.QRImage);
        byte[] codeInBytes = getArguments().getByteArray("codeInBytes");
        Bitmap qrCode = BitmapFactory.decodeByteArray(codeInBytes, 0, codeInBytes.length);
        QRImage.setImageBitmap(qrCode);
        return view;
    }
}
