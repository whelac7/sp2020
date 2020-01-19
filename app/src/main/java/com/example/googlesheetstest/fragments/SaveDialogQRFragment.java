package com.example.googlesheetstest.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.googlesheetstest.R;

public class SaveDialogQRFragment extends DialogFragment {
    View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        System.out.println("Creating dialog...");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_save_dialog_qr, null);
        builder.setView(view);

        /*ImageView QRCodeImage = (ImageView)view.findViewById(R.id.QRCodeImage);
        byte[] codeInBytes = getArguments().getByteArray("codeInBytes");
        Bitmap qrCode = BitmapFactory.decodeByteArray(codeInBytes, 0, codeInBytes.length);
        QRCodeImage.setImageBitmap(qrCode);*/

        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("Creating view...");
        inflater.inflate(R.layout.fragment_save_dialog_qr, container, false);
        return view;
    }
}
