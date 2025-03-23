package com.example.medistreamapplication.TeacherForm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.medistreamapplication.R;
import com.example.medistreamapplication.databinding.ActivityHistoryExaminationFormBinding;
import com.example.medistreamapplication.model_teacherForm.HistoryExaminationModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class history_examination_form extends AppCompatActivity {

    ActivityHistoryExaminationFormBinding binding;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryExaminationFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(history_examination_form.this, Dietary_History.class);
                saveDataToFirebase();
                startActivity(intent);
            }
        });
    }

    private void saveDataToFirebase() {
        // Create an instance of the model class
        HistoryExaminationModel historyExaminationModel = new HistoryExaminationModel();

        // Set checkbox values individually
        historyExaminationModel.setDiabetes(binding.checkboxDiabetes.isChecked());
        historyExaminationModel.setHypertension(binding.checkboxHypertension.isChecked());
        historyExaminationModel.setIschemicHeartDisease(binding.checkboxIschemicHeartDisease.isChecked());
        historyExaminationModel.setTuberculosis(binding.checkboxTuberculosis.isChecked());
        historyExaminationModel.setAsthma(binding.checkboxAsthma.isChecked());
        historyExaminationModel.setAnyOther(binding.checkboxAnyOther.isChecked());

        // Set input field values
        historyExaminationModel.setSinceYears(binding.editTextSinceYears.getText().toString().trim());
        historyExaminationModel.setMedicationDetails(binding.editTextMedicationDetails.getText().toString().trim());
        historyExaminationModel.setRegularity(binding.editTextRegularity.getText().toString().trim());

        // Set family history checkboxes individually
        historyExaminationModel.setFamilyDiabetes(binding.checkboxFamilyDiabetes.isChecked());
        historyExaminationModel.setFamilyHypertension(binding.checkboxFamilyHypertension.isChecked());
        historyExaminationModel.setFamilyIschemicHeartDisease(binding.checkboxFamilyIschemicHeartDisease.isChecked());
        historyExaminationModel.setFamilyTuberculosis(binding.checkboxFamilyTuberculosis.isChecked());
        historyExaminationModel.setFamilyAsthma(binding.checkboxFamilyAsthma.isChecked());
        historyExaminationModel.setFamilyAnyOther(binding.checkboxFamilyAnyOther.isChecked());

        // Save to SharedPreferences (local storage)
        SharedPreferences sharedPreferences = getSharedPreferences("SchoolSurveyData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(historyExaminationModel); // Convert object to JSON
        editor.putString("historyExaminationData", json);
        editor.apply();
    }

}
