package com.example.googlesheetstest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class SaveDataDialog extends AppCompatDialogFragment {
    private ImageView qrCodeImage;
    private byte[] codeInBytes;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_savedatadialog, null);
        builder.setView(view);

        codeInBytes = getArguments().getByteArray("codeInBytes");
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new SaveDialogPagerAdapter(getChildFragmentManager(), codeInBytes));
        TabLayout dialogTabLayout = view.findViewById(R.id.dialog_qrtab);
        dialogTabLayout.setupWithViewPager(viewPager);

        return builder.create();
    }
}
