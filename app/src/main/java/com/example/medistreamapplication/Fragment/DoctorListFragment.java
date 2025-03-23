package com.example.medistreamapplication.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medistreamapplication.Adapter.AdminAdapter;
import com.example.medistreamapplication.model_form1.AdminModel;
import com.example.medistreamapplication.Firebase.FirebaseConstants;
import com.example.medistreamapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DoctorListFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdminAdapter adminAdapter;
    private List<AdminModel> adminList;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_list, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.AdminRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize the list
        adminList = new ArrayList<>();

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseConstants.ADMIN_PATH);

        // Fetch data from Firebase
        fetchAdminData();

        // Initialize SearchView
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        SearchView searchView = view.findViewById(R.id.searchView); // Make sure your SearchView has this id
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterAdmins(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterAdmins(newText);
                return false;
            }
        });

        return view;
    }
    private void filterAdmins(String query) {
        List<AdminModel> filteredList = new ArrayList<>();
        for (AdminModel admin : adminList) {
            if (admin.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(admin);
            }
        }
        adminAdapter.filterList(filteredList);
    }
    private void fetchAdminData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdminModel admin = snapshot.getValue(AdminModel.class);
                    // Make sure to set the ID if needed in AdminModel
                    adminList.add(admin);
                }

                // Set the adapter
                adminAdapter = new AdminAdapter(adminList, getContext());
                recyclerView.setAdapter(adminAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }


}
