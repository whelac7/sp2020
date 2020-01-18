package com.example.googlesheetstest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

public class QRCodeDialog extends AppCompatDialogFragment {
    private ImageView qrCodeImage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        System.out.println("Dialog creating...");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_qrcode_popup, null);

        builder.setView(view)
                .setTitle("QR Code");

        qrCodeImage = view.findViewById(R.id.QRImage);
        byte[] codeBytes = getArguments().getByteArray("codeInBytes");
        Bitmap qrCode = BitmapFactory.decodeByteArray(codeBytes, 0, codeBytes.length);
        qrCodeImage.setImageBitmap(qrCode);

        return builder.create();
    }
}
