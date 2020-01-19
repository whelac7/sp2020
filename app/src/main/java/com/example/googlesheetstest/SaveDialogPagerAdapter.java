package com.example.googlesheetstest;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SaveDialogPagerAdapter extends FragmentPagerAdapter {
    private byte[] codeInBytes;

    public SaveDialogPagerAdapter(FragmentManager fm, byte[] codeInBytes) {
        super(fm);
        this.codeInBytes = codeInBytes;
    }

    @Override
    public DialogFragment getItem(int position) {
        DialogFragment fragment = null;
        switch (position) {
            case 0:
                fragment = new SaveDataDialog();
                Bundle bundledQRCode = new Bundle();
                bundledQRCode.putByteArray("codeInBytes", codeInBytes);
                fragment.setArguments(bundledQRCode);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }


}
