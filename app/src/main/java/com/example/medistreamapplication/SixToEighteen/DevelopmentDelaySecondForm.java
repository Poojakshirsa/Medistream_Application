package com.example.medistreamapplication.SixToEighteen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.medistreamapplication.databinding.ActivityDevelopmentDelaySecondFormBinding;
import com.example.medistreamapplication.model_form2.DevelopmentDelayModelFormTwo;
import com.google.gson.Gson;

public class DevelopmentDelaySecondForm extends AppCompatActivity {

    ActivityDevelopmentDelaySecondFormBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDevelopmentDelaySecondFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a model instance
                DevelopmentDelayModelFormTwo model = new DevelopmentDelayModelFormTwo();

                // Set the checkbox states in the model
                model.setDifficultyInSeeing(binding.checkboxDifficultyInSeeing.isChecked());
                model.setDelayInWalking(binding.checkboxDelayInWalking.isChecked());
                model.setStiffnessOrFloppiness(binding.checkboxStiffnessOrFloppiness.isChecked());
                model.setFitsOrJerks(binding.checkboxFitsOrJerks.isChecked());
                model.setDifficultyInReadingWriting(binding.checkboxDifficultyInReadingWriting.isChecked());
                model.setDifficultyInSpeaking(binding.checkboxDifficultyInSpeaking.isChecked());
                model.setDifficultyInHearing(binding.checkboxDifficultyInHearing.isChecked());
                model.setDifficultyInLearning(binding.checkboxDifficultyInLearning.isChecked());
                model.setDifficultyInAttention(binding.checkboxDifficultyInAttention.isChecked());

                // Pass the model data to the next activity (optional)
                Intent intent = new Intent(DevelopmentDelaySecondForm.this, Adolescent_Specific.class);
                // Convert the model to JSON
                Gson gson = new Gson();
                String json = gson.toJson(model);

                // Save the JSON to SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("SurveyDataFormTwo", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("developmentalDelays", json);
                editor.apply();
                startActivity(intent);
            }
        });

    }
}