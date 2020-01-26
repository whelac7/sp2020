package org.frc1732scoutingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.fragments.SQLLiteDatabaseFragment;
import org.frc1732scoutingapp.fragments.SyncSheetsFragment;

public class HomeFragment extends Fragment {
    private GoogleSignInOptions gso;
    private GoogleSignInClient  mGoogleSignInClient;
    private GoogleSignInAccount account;
    private Context context;
    private static final String TAG = "SignInActivity";
    private static String SPREADSHEETS_SCOPE = "https://www.googleapis.com/auth/spreadsheets";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_fragment_home);
        return view;
    }
}
