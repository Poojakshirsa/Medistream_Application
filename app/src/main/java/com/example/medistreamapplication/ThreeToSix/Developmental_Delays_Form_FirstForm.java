package com.example.medistreamapplication.ThreeToSix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medistreamapplication.databinding.ActivityDevelopmentalDelaysFormFirstFormBinding;
import com.example.medistreamapplication.model_form1.DevelopmentalDelaysData;
import com.google.gson.Gson;

public class Developmental_Delays_Form_FirstForm extends AppCompatActivity {

    ActivityDevelopmentalDelaysFormFirstFormBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDevelopmentalDelaysFormFirstFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Set text for checkboxes
        setCheckboxTexts();
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDevelopmentalDelaysData();

                // Send JSON data to next activity
                Intent intent = new Intent(Developmental_Delays_Form_FirstForm.this, Preliminary_Finding_FirstForm.class);
                startActivity(intent);
            }
        });
    }
    private void setCheckboxTexts() {
        binding.checkBoxD1.setText("Does the child have difficulty in seeing, either during day or night? (without spectacles) (V)");
        binding.checkBoxD2.setText("Compared with other children of his/her age, did the child have any delay in walking? (GM)");
        binding.checkBoxD3.setText("Does the child have stiffness or floppiness and/or reduced strength in his/her arms or legs? (GM/NMI)");
        binding.checkBoxD4.setText("Has the child ever had fits or became rigid or had sudden jerks or spasms of arms, legs, or whole body?");
        binding.checkBoxD5.setText("Has your child ever lost consciousness?");
        binding.checkBoxD6.setText("Does the child have difficulty in reading or writing or to do simple calculations? (LD)");
        binding.checkBoxD7.setText("Does the child have any difficulty in speaking as compared to other children of his/her age? (SP)");
        binding.checkBoxD8.setText("Is the child's speech in any way different from other children of his/her age? (SP)");
        binding.checkBoxD9.setText("Does the child have difficulty in hearing? (without hearing aid) (H)");
        binding.checkBoxD10.setText("Does the child have difficulty in learning new things? (LD/C)");
        binding.checkBoxD11.setText("Does the child have difficulty in sustaining attention on activities at school, home, or play? (C)");
    }

    private void saveDevelopmentalDelaysData() {
        // Create the model object and populate it with data from checkboxes
        DevelopmentalDelaysData developmentalDelaysData = new DevelopmentalDelaysData();
        developmentalDelaysData.setDifficultyInSeeing(binding.checkBoxD1.isChecked());
        developmentalDelaysData.setDelayInWalking(binding.checkBoxD2.isChecked());
        developmentalDelaysData.setStiffnessOrFloppiness(binding.checkBoxD3.isChecked());
        developmentalDelaysData.setConvulsiveDisorder(binding.checkBoxD4.isChecked());
        developmentalDelaysData.setLossOfConsciousness(binding.checkBoxD5.isChecked());
        developmentalDelaysData.setDifficultyInReadingWriting(binding.checkBoxD6.isChecked());
        developmentalDelaysData.setDifficultyInSpeaking(binding.checkBoxD7.isChecked());
        developmentalDelaysData.setSpeechDifference(binding.checkBoxD8.isChecked());
        developmentalDelaysData.setDifficultyInHearing(binding.checkBoxD9.isChecked());
        developmentalDelaysData.setDifficultyInLearning(binding.checkBoxD10.isChecked());
        developmentalDelaysData.setDifficultyInSustainingAttention(binding.checkBoxD11.isChecked());

        // Convert the model to JSON
        Gson gson = new Gson();
        String json = gson.toJson(developmentalDelaysData);

        // Save the JSON to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SurveyData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("developmentalDelays", json);
        editor.apply();
    }
}
