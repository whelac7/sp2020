package org.frc1732scoutingapp.adapters;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.frc1732scoutingapp.fragments.SaveDialogBluetoothFragment;
import org.frc1732scoutingapp.fragments.SaveDialogDatabaseFragment;
import org.frc1732scoutingapp.fragments.SaveDialogQRFragment;
import org.frc1732scoutingapp.responses.SubmitToDBCallback;

public class SaveDialogPagerAdapter extends FragmentPagerAdapter {
    private byte[] codeInBytes;
    private SubmitToDBCallback DBListener;

    public SaveDialogPagerAdapter(FragmentManager fm, byte[] codeInBytes, SubmitToDBCallback DBListener) {
        super(fm);
        this.codeInBytes = codeInBytes;
        this.DBListener = DBListener;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "QR";
            case 1:
                return "Bluetooth";
            case 2:
                return "Database (MASTER)";
            default:
                return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new SaveDialogQRFragment();
                Bundle bundledQRCode = new Bundle();
                bundledQRCode.putByteArray("codeInBytes", codeInBytes);
                fragment.setArguments(bundledQRCode);
                break;
            case 1:
                fragment = new SaveDialogBluetoothFragment();
                break;
            case 2:
                fragment = new SaveDialogDatabaseFragment(DBListener);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }


}
