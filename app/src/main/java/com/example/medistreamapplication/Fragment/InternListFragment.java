package com.example.medistreamapplication.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medistreamapplication.Firebase.FirebaseConstants;
import com.example.medistreamapplication.Adapter.InternAdapter;
import com.example.medistreamapplication.model_form1.InternModel;
import com.example.medistreamapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InternListFragment extends Fragment {

    private RecyclerView recyclerView;
    private InternAdapter internAdapter;
    private List<InternModel> InternList;

    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intern_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewInterns);
        // Set GridLayoutManager with 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseConstants.INTERN_PATH);

        InternList = new ArrayList<>();
        internAdapter = new InternAdapter(getContext(),InternList);
        recyclerView.setAdapter(internAdapter);

        fetchInterns();
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
        List<InternModel> filteredList = new ArrayList<>();
        for (InternModel intern :InternList) {
            if (intern.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(intern);
            }
        }
        internAdapter.filterList(filteredList);
    }
    private void fetchInterns() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                InternList.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    InternModel intern = snapshot.getValue(InternModel.class);
                    InternList.add(intern);
                }
                internAdapter.notifyDataSetChanged(); // Notify adapter of data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("InternListFragment", "Failed to read interns", databaseError.toException());
            }
        });
    }

}
