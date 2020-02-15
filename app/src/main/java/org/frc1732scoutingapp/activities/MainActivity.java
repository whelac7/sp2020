package org.frc1732scoutingapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.widget.Toast;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.fragments.SQLLiteDatabaseFragment;
import org.frc1732scoutingapp.fragments.SettingsFragment;
import org.frc1732scoutingapp.fragments.SyncSheetsFragment;
import org.frc1732scoutingapp.fragments.HomeFragment;
import org.frc1732scoutingapp.models.RequestCodes;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isMaster = sharedPref.getBoolean("toggle_master", false);
        verifyPermissions();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFragment, new HomeFragment()).commit();
                        break;
                    case R.id.nav_log_match:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFragment, new SQLLiteDatabaseFragment(isMaster)).commit();
                        break;
                    case R.id.nav_sync:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFragment, new SyncSheetsFragment()).commit();
                        break;
                    case R.id.settings:
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                }

                drawer.closeDrawers();
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_app_bar_open_drawer_description, R.string.nav_app_bar_navigate_up_description);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void verifyPermissions() {
        try {
            List<String> ungrantedPerms = new ArrayList<String>();
            PackageInfo requiredPerms = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (requiredPerms.requestedPermissions != null) {
                for (int i = 0; i < requiredPerms.requestedPermissions.length; i++) {
                    if((requiredPerms.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) == 0) {
                        ungrantedPerms.add(requiredPerms.requestedPermissions[i]);
                    }
                }
            }
            if (ungrantedPerms.size() > 0) {
                String[] ungrantedPermsArr = new String[ungrantedPerms.size()];
                ungrantedPermsArr = ungrantedPerms.toArray(ungrantedPermsArr);
                ActivityCompat.requestPermissions(this, ungrantedPermsArr, RequestCodes.PERMISSION_REQUEST.getValue());
            }
        }
        catch (PackageManager.NameNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.homeFragment);
        if (fragment instanceof HomeFragment) {
            super.onBackPressed();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFragment, new HomeFragment()).commit();
        }
    }
}

