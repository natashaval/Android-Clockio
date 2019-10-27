package com.natasha.clockio.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.natasha.clockio.R;
//https://www.androidhive.info/2017/12/android-working-with-bottom-navigation/
public class DashboardActivity extends AppCompatActivity {

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = getSupportActionBar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.dashboard_navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
//        loadFragment(new ActivityFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment;
                    switch (item.getItemId()) {
                        case R.id.navigation_activity:
                            toolbar.setTitle(R.string.navigation_activity);
                            loadFragment(new ActivityFragment());
                            return true;
                        case R.id.navigation_friend:
                            toolbar.setTitle(R.string.navigation_friend);
                            loadFragment(new FriendFragment());
                            return true;
                        case R.id.navigation_checkin:
                            toolbar.setTitle(R.string.navigation_checkin);
                            return true;
                        case R.id.navigation_notification:
                            toolbar.setTitle(R.string.navigation_notification);
                            return true;
                        case R.id.navigation_profile:
                            loadFragment(new ProfileFragment());
                            toolbar.setTitle(R.string.navigation_profile);
                            return true;
                    }
                    return false;
                }
            };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }
}
