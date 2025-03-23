package com.example.medistreamapplication.SixToEighteen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.medistreamapplication.databinding.ActivityDefectDeficiencyDiseaseBinding;
import com.example.medistreamapplication.model_form2.DefectDataFormTwo;
import com.google.gson.Gson;

public class Defect_Deficiency_Disease extends AppCompatActivity {

    ActivityDefectDeficiencyDiseaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDefectDeficiencyDiseaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set checkbox texts dynamically
        setDynamicCheckboxTexts();

        // Button click listener
        binding.NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Defect_Deficiency_Disease.this, DevelopmentDelaySecondForm.class);
                saveDefectsData();
                startActivity(intent);
            }
        });
    }

    private void setDynamicCheckboxTexts() {
        // Defects at Birth Section
        binding.checkboxVisibleDefect.setText("Cleft Lip/Palate/Club foot/Down’s syndrome/Cataract or any other visible defect");

        // Nutrition Deficiency Check Section
        binding.checkboxSevereAnaemia.setText("Severe anaemia - Look for severe palmar pallor");
        binding.checkboxVitaminADeficiency.setText("Vitamin A Deficiency - Ask for night blindness/look for Bitot’s spot (white patches on sclera)");
        binding.checkboxVitaminDDeficiency.setText("Vitamin D Deficiency – Look for Wrist Widening/Bowing of legs");
        binding.checkboxGoitre.setText("Goitre - Any swelling in the neck region");
        binding.checkboxOedemaFeet.setText("Oedema of both feet");

        // Diseases Section
        binding.checkboxConvulsiveDisorders.setText("Convulsive Disorders - Did the child ever have spells of unconsciousness and fits?");
        binding.checkboxOtitisMedia.setText("Otitis Media - Did the child have more than 3 episodes of ear discharge in last 1 year? Look for Active discharge from ear");
        binding.checkboxDentalCondition.setText("Dental Condition - Look for white demineralized/brown tooth, Discolouration, cavitations, Swollen/bleeding/red gums, Visible Plaque/stains");
        binding.checkboxSkinCondition.setText("Skin Condition - Does the child c/o itching on skin (especially at night)? Look for round or oval scaly patches/pustules in finger webs. Any other lesion on the skin.");
        binding.checkboxRheumaticHeartDisease.setText("Rheumatic Heart Disease - Auscultate for Murmur");
        binding.checkboxOthers.setText("Others [Tuberculosis – cough > 2 weeks, Asthma – More than 3 Episodes of increased shortness of breath and difficult breathing and wheezing in past 6 months]");
    }
    private void saveDefectsData() {
        // Get data from the checkboxes
        boolean visibleDefect = binding.checkboxVisibleDefect.isChecked();
        boolean severeAnaemia = binding.checkboxSevereAnaemia.isChecked();
        boolean vitaminADeficiency = binding.checkboxVitaminADeficiency.isChecked();
        boolean vitaminDDeficiency = binding.checkboxVitaminDDeficiency.isChecked();
        boolean goitre = binding.checkboxGoitre.isChecked();
        boolean oedemaFeet = binding.checkboxOedemaFeet.isChecked();
        boolean convulsiveDisorders = binding.checkboxConvulsiveDisorders.isChecked();
        boolean otitisMedia = binding.checkboxOtitisMedia.isChecked();
        boolean dentalCondition = binding.checkboxDentalCondition.isChecked();
        boolean skinCondition = binding.checkboxSkinCondition.isChecked();
        boolean rheumaticHeartDisease = binding.checkboxRheumaticHeartDisease.isChecked();
        boolean others = binding.checkboxOthers.isChecked();

        // Create and populate the model
        DefectDataFormTwo defectDataFormTwo = new DefectDataFormTwo();
        defectDataFormTwo.setVisibleDefect(visibleDefect);
        defectDataFormTwo.setSevereAnaemia(severeAnaemia);
        defectDataFormTwo.setVitaminADeficiency(vitaminADeficiency);
        defectDataFormTwo.setVitaminDDeficiency(vitaminDDeficiency);
        defectDataFormTwo.setGoitre(goitre);
        defectDataFormTwo.setOedemaFeet(oedemaFeet);
        defectDataFormTwo.setConvulsiveDisorders(convulsiveDisorders);
        defectDataFormTwo.setOtitisMedia(otitisMedia);
        defectDataFormTwo.setDentalCondition(dentalCondition);
        defectDataFormTwo.setSkinCondition(skinCondition);
        defectDataFormTwo.setRheumaticHeartDisease(rheumaticHeartDisease);
        defectDataFormTwo.setOthers(others);

        // Save data to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SurveyDataFormTwo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Saving the defectData as a JSON string
        String defectDataJson = new Gson().toJson(defectDataFormTwo);
        editor.putString("defectsAtBirth", defectDataJson);
        editor.apply();
    }

}
