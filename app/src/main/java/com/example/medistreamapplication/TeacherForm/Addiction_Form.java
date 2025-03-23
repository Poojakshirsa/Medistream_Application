package com.example.medistreamapplication.TeacherForm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.medistreamapplication.R;
import com.example.medistreamapplication.databinding.ActivityAddictionFormBinding;
import com.example.medistreamapplication.model_teacherForm.AddictionModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class Addiction_Form extends AppCompatActivity {

    ActivityAddictionFormBinding binding;
    DatabaseReference databaseReference; // Firebase reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddictionFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Addiction_Form.this, Examination.class);
                saveDataToFirebase();
                startActivity(intent);
            }
        });
    }

    private void saveDataToFirebase() {
        // Get selected answers
        String addictedNow = getSelectedRadioText(binding.radioGroup1);
        String addictedPast = getSelectedRadioText(binding.radioGroup2);
        String addictionType = binding.editTextAddictionType.getText().toString().trim();
        String addictionDuration = getSelectedRadioText(binding.radioGroupDuration);
        String frequency = binding.editTextFrequency.getText().toString().trim();

        // Validate input
        if (addictionType.isEmpty()) addictionType = "Not specified";
        if (frequency.isEmpty()) frequency = "Not specified";

        // Create model object and set values using setters
        AddictionModel model = new AddictionModel();
        model.setAddictedNow(addictedNow);
        model.setAddictedPast(addictedPast);
        model.setAddictionType(addictionType);
        model.setAddictionDuration(addictionDuration);
        model.setFrequency(frequency);


        SharedPreferences sharedPreferences = getSharedPreferences("SchoolSurveyData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(model); // Convert object to JSON
        editor.putString("Addiction_Form", json);
        editor.apply();
    }

    // Helper method to get selected radio button text
    private String getSelectedRadioText(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton radioButton = findViewById(selectedId);
            return radioButton.getText().toString();
        }
        return "Not specified"; // Default if no option is selected
    }
}
