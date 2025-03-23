package com.example.medistreamapplication.Dashboard;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.medistreamapplication.Fragment.AddDoctorFragment;
import com.example.medistreamapplication.Fragment.DoctorListFragment;
import com.example.medistreamapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set default fragment when activity starts
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddDoctorFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Switch fragments based on selected item using if-else
            if (item.getItemId() == R.id.nav_add_admin) {
                selectedFragment = new AddDoctorFragment();
            } else if (item.getItemId() == R.id.nav_admin_list) {
                selectedFragment = new DoctorListFragment();
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
        itemHome.setIcon(selectedItemId == R.id.nav_add_admin ? R.drawable.filluser : R.drawable.emptyuser);
        itemSearch.setIcon(selectedItemId == R.id.nav_admin_list ? R.drawable.filllist : R.drawable.emptylist);

    }
}
