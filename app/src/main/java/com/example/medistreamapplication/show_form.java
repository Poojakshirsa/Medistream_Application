package com.example.medistreamapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.medistreamapplication.Adapter.SurveyDataAdapter;
import com.example.medistreamapplication.Firebase.FirebaseConstants;
import com.example.medistreamapplication.model_form1.InternModel;
import com.example.medistreamapplication.model_form1.SurveyData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class show_form extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SurveyDataAdapter adapter;
    private List<SurveyData> surveyDataList;
    private DatabaseReference surveyRef;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_form);
        SharedPreferences sharedPreferences = getSharedPreferences("InternDetails", MODE_PRIVATE);
        String name = sharedPreferences.getString("intern_name", "N/A");
        String phone = sharedPreferences.getString("intern_phone", "N/A");
        String address = sharedPreferences.getString("intern_address", "N/A"); // Retrieve address
        String email = sharedPreferences.getString("intern_email", "N/A"); // Retrieve email
        String imageUrl = sharedPreferences.getString("intern_image_url", "");
        String id=sharedPreferences.getString("intern_id","");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SearchView searchView = findViewById(R.id.searchView2);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search");
        surveyDataList = new ArrayList<>();
        adapter = new SurveyDataAdapter(this,surveyDataList);
        recyclerView.setAdapter(adapter);

        surveyRef = FirebaseDatabase.getInstance()
                .getReference(FirebaseConstants.FORM_PATH + "ThreeToSixYear")
                .child(id); // Fetch only forms under current intern ID


        surveyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                surveyDataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SurveyData surveyData = snapshot.getValue(SurveyData.class);
                    if (surveyData != null) {
                        surveyDataList.add(surveyData);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ShowForm_ThreeToSix", "Error fetching data from Firebase", databaseError.toException());
            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return false;
            }
        });
    }



    private void filterData(String query) {
        List<SurveyData> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase();

        for (SurveyData surveyData : surveyDataList) {
            boolean matches = false;

            // Check fields of all nested objects
            if (surveyData.getSchoolData() != null) {
                matches = checkObjectFields(surveyData.getSchoolData(), lowerCaseQuery);
            }
            if (!matches && surveyData.getDefectsData() != null) {
                matches = checkObjectFields(surveyData.getDefectsData(), lowerCaseQuery);
            }
            if (!matches && surveyData.getDeficienciesData() != null) {
                matches = checkObjectFields(surveyData.getDeficienciesData(), lowerCaseQuery);
            }
            if (!matches && surveyData.getDevelopmentalDelaysData() != null) {
                matches = checkObjectFields(surveyData.getDevelopmentalDelaysData(), lowerCaseQuery);
            }
            if (!matches && surveyData.getPreliminaryFinding() != null) {
                matches = checkObjectFields(surveyData.getPreliminaryFinding(), lowerCaseQuery);
            }

            if (matches) {
                filteredList.add(surveyData);
            }
        }

        if (filteredList.isEmpty()) {
            Log.d("Search", "No matching results for query: " + query);
        }

//        adapter.filterList(filteredList);
    }

    private boolean checkObjectFields(Object object, String query) {
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(object);
                if (value != null && value.toString().toLowerCase().contains(query)) {
                    return true;
                }
            }
        } catch (IllegalAccessException e) {
            Log.e("Reflection", "Error accessing fields", e);
        }
        return false;
    }
    }
