package com.example.medistreamapplication.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.medistreamapplication.Download;
import com.example.medistreamapplication.Fragment.AddInternFragment;
import com.example.medistreamapplication.Fragment.DownloadPdfFragment;
import com.example.medistreamapplication.Fragment.InternListFragment;
import com.example.medistreamapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DoctorDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set default fragment when activity starts
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddInternFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Switch fragments based on selected item using if-else
            if (item.getItemId() == R.id.nav_add_admin) {
                selectedFragment = new AddInternFragment();
            } else if (item.getItemId() == R.id.nav_admin_list) {
                selectedFragment = new InternListFragment();
            }
            else if (item.getItemId() == R.id.nav_download) {
                selectedFragment = new DownloadPdfFragment();
            }

            // Replace the fragment
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            updateIcons(item.getItemId());
            return true;
        });
    }


    private void updateIcons(int selectedItemId) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        MenuItem itemHome = bottomNavigationView.getMenu().findItem(R.id.nav_add_admin);
        MenuItem itemSearch = bottomNavigationView.getMenu().findItem(R.id.nav_admin_list);


        // Update icons based on the selected item
        itemHome.setIcon(selectedItemId == R.id.nav_add_admin ? R.drawable.baseline_person_add_24 : R.drawable.baseline_person_add_alt_24);
        itemSearch.setIcon(selectedItemId == R.id.nav_admin_list ? R.drawable.filllist : R.drawable.emptylist);

    }
    }
