package org.frc1732scoutingapp.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.Menu;

import org.frc1732scoutingapp.R;
import org.frc1732scoutingapp.fragments.JsonToDBFragment;
import org.frc1732scoutingapp.fragments.LogMatchFragment;
import org.frc1732scoutingapp.fragments.SyncSheetsFragment;
import org.frc1732scoutingapp.fragments.HomeFragment;
import org.frc1732scoutingapp.helpers.IOHelper;
import org.frc1732scoutingapp.models.RequestCodes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private boolean isMaster;
    private String compCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isMaster = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("toggle_master", false);
        compCode = PreferenceManager.getDefaultSharedPreferences(this).getString("compCode", null);

        File rootCompDataDirectory = new File(IOHelper.getRootCompDataPath(getApplicationContext()));
        if (!rootCompDataDirectory.exists()) {
            rootCompDataDirectory.mkdir();
        }

        if (compCode != null && !compCode.trim().isEmpty()) {
            File compDataDirectory = new File(IOHelper.getCompetitionDirectoryPath(getApplicationContext(), compCode));
            if (!compDataDirectory.exists()) {
                compDataDirectory.mkdir();
            }
        }

        verifyPermissions();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController controller = navHostFragment.getNavController();
        NavInflater inflater = controller.getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.nav_graph);

        if (!isMaster) {
            Menu nav_menu = navigationView.getMenu();
            nav_menu.findItem(R.id.nav_home).setVisible(false);
            nav_menu.findItem(R.id.nav_sync).setVisible(false);
            nav_menu.findItem(R.id.nav_json_to_db).setVisible(false);
            getSupportFragmentManager().beginTransaction().replace(graph.getStartDestination(), new LogMatchFragment(), "LogMatchFragment").commit();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(graph.getStartDestination(), new HomeFragment(), "HomeFragment").commit();
                        break;
                    case R.id.nav_log_match:
                        getSupportFragmentManager().beginTransaction().replace(graph.getStartDestination(), new LogMatchFragment(), "LogMatchFragment").commit();
                        break;
                    case R.id.nav_sync:
                        getSupportFragmentManager().beginTransaction().replace(graph.getStartDestination(), new SyncSheetsFragment(), "SyncSheetsFragment").commit();
                        break;
                    case R.id.nav_json_to_db:
                        getSupportFragmentManager().beginTransaction().replace(graph.getStartDestination(), new JsonToDBFragment(), "JsonToDBFragment").commit();
                        break;
                    case R.id.nav_settings:
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
        if (fragment instanceof HomeFragment || !isMaster) {
            super.onBackPressed();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFragment, new HomeFragment()).commit();
        }
    }
}

