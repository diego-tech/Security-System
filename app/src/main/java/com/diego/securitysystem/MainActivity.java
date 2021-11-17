package com.diego.securitysystem;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.diego.securitysystem.fragments.LogFragment;
import com.diego.securitysystem.fragments.SettingsFragment;
import com.diego.securitysystem.fragments.StatusFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public BottomNavigationView navMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navMenu = findViewById(R.id.bottom_navigation);
        loadFragment(StatusFragment.newInstance());
        navMenu.setSelectedItemId(R.id.status);

        navMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.status:
                        loadFragment(StatusFragment.newInstance());
                        return true;
                    case R.id.history:
                        loadFragment(LogFragment.newInstance());
                        return true;
                    case R.id.settings:
                        loadFragment(SettingsFragment.newInstance());
                        return true;
                }
                return false;
            }
        });

    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}