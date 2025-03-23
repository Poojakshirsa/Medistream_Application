package com.example.medistreamapplication.TeacherForm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.medistreamapplication.R;
import com.example.medistreamapplication.databinding.ActivityPhysicalBinding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.medistreamapplication.R;
import com.example.medistreamapplication.databinding.ActivityPhysicalBinding;
import com.example.medistreamapplication.model_teacherForm.PhysicalActivityModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class Physical_Activity extends AppCompatActivity {

    ActivityPhysicalBinding binding;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhysicalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Physical_Activity.this, Addiction_Form.class);
                saveDataToDatabase();
                startActivity(intent);
            }


        });
    }

    private void saveDataToDatabase() {
        // Get selected answers
        String exerciseAtPresent = getSelectedRadioText(binding.exerciseRadioGroup);
        String exerciseFrequency = getSelectedRadioText(binding.frequencyRadioGroup);
        String reasonForNotExercising = getSelectedRadioText(binding.reasonRadioGroup);

        // Validate input
        if (exerciseAtPresent.isEmpty() || exerciseFrequency.isEmpty() || reasonForNotExercising.isEmpty()) {
            Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create model object
        PhysicalActivityModel model = new PhysicalActivityModel();
        model.setExerciseAtPresent(exerciseAtPresent);
        model.setExerciseFrequency(exerciseFrequency);
        model.setReasonForNotExercising(reasonForNotExercising);



        SharedPreferences sharedPreferences = getSharedPreferences("SchoolSurveyData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(model); // Convert object to JSON
        editor.putString("Physical_Activity", json);
        editor.apply();
    }

    private String getSelectedRadioText(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return selectedRadioButton.getText().toString();
        }
        return "";
    }
}
