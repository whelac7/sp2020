package org.frc1732scoutingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.adapters.SaveDialogPagerAdapter;
import org.frc1732scoutingapp.responses.SubmitToDBCallback;

public class SaveDataDialog extends DialogFragment {
    private byte[] codeInBytes;
    private SubmitToDBCallback DBListener;
    private boolean isMaster;

    public void setDBListener(SubmitToDBCallback DBListener) {
        this.DBListener = DBListener;
    }

    protected SubmitToDBCallback getDBListener() {
        return DBListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_data_dialog_menu, container, false);
        isMaster = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("toggle_master", false);
        if (!isMaster) {
            TabLayout tabLayout = view.findViewById(R.id.tabLayout);
            ViewPager viewPager = view.findViewById(R.id.viewpager);
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            tabLayout.setVisibility(View.GONE);
        }
        codeInBytes = getArguments().getByteArray("codeInBytes");
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        SaveDialogPagerAdapter adapter = new SaveDialogPagerAdapter(getChildFragmentManager());
        adapter.setDBListener(DBListener);
        adapter.setCodeInBytes(codeInBytes);
        viewPager.setAdapter(adapter);
        TabLayout dialogTabLayout = view.findViewById(R.id.tabLayout);
        dialogTabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
