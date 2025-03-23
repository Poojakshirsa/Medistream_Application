package com.example.medistreamapplication.SixToEighteen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medistreamapplication.databinding.ActivityAdolescentSpecificBinding;
import com.example.medistreamapplication.model_form2.AdolescentFormModel;
import com.google.gson.Gson;


public class Adolescent_Specific extends AppCompatActivity {

    ActivityAdolescentSpecificBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdolescentSpecificBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.Nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Collect data from checkboxes
                AdolescentFormModel formData = new AdolescentFormModel();
                formData.setDifficultyHandlingLifeChanges( binding.checkboxDifficultyHandlingLifeChanges.isChecked());
                formData.setSayingNo( binding.checkboxSayingNo.isChecked());
                formData.setFeelingTiredOrDepressed(binding.checkboxFeelingTiredOrDepressed.isChecked());
                formData.setMenstrualCycles( binding.checkboxMenstrualCycles.isChecked());
                formData.setRegularPeriods(binding.checkboxRegularPeriods.isChecked());
                formData.setDischarge( binding.checkboxDischarge.isChecked());
                formData.setPainWhileUrinating( binding.checkboxPainWhileUrinating.isChecked());
                formData.setExtremePainMenstruation( binding.checkboxExtremePainMenstruation.isChecked());

                // Pass data to the next activity
                Intent intent = new Intent(Adolescent_Specific.this, PreliminaryFindings_SecondForm.class);
                Gson gson = new Gson();
                String json = gson.toJson(formData);
                Log.d("AdolescentData", "JSON: " + json);


                // Save the JSON to SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("SurveyDataFormTwo", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("adolescentForm", json);
                editor.apply();
                startActivity(intent);
            }
        });
    }
}
