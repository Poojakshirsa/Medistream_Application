package com.example.medistreamapplication.ThreeToSix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.example.medistreamapplication.databinding.ActivityDeficienciesSectionFirstFormBinding;
import com.example.medistreamapplication.model_form1.DeficiencyData;
import com.google.gson.Gson;

public class Deficiencies_Section_FirstForm extends AppCompatActivity {
    ActivityDeficienciesSectionFirstFormBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeficienciesSectionFirstFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupCheckBoxes();
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDeficienciesData();

                Intent intent = new Intent(Deficiencies_Section_FirstForm.this, Developmental_Delays_Form_FirstForm.class);
                startActivity(intent);
            }
        });
    }
    private void setupCheckBoxes() {
        // Set up the checkbox text
        binding.checkBoxB1.setText(HtmlCompat.fromHtml("<b>SAM</b> - Weight for Height/Length: refer if the child is less than -3SD as per WHO chart, counsel if &lt; -2SD", HtmlCompat.FROM_HTML_MODE_LEGACY));
        binding.checkBoxB2.setText(HtmlCompat.fromHtml("<b>SAM</b> - Oedema- Bilateral pitting Oedema", HtmlCompat.FROM_HTML_MODE_LEGACY));
        binding.checkBoxB3.setText(HtmlCompat.fromHtml("<b>Severe Anaemia</b> - Look for severe palmar pallor", HtmlCompat.FROM_HTML_MODE_LEGACY));
        binding.checkBoxB4.setText(HtmlCompat.fromHtml("<b>Vitamin A Deficiency</b> - Ask for night blindness/look for Bitot's spot (White Patches on sclera)", HtmlCompat.FROM_HTML_MODE_LEGACY));
        binding.checkBoxB5.setText(HtmlCompat.fromHtml("<b>Vitamin D Deficiency</b> – Look for Wrist Widening/Bowing of legs", HtmlCompat.FROM_HTML_MODE_LEGACY));
        binding.checkBoxC1.setText(HtmlCompat.fromHtml("<b>Convulsive Disorders</b> – Did the child ever have/had spells of unconsciousness and fits?", HtmlCompat.FROM_HTML_MODE_LEGACY));
        binding.checkBoxC2.setText(HtmlCompat.fromHtml("<b>Otitis Media</b> – Did the child have more than 3 episodes of ear discharge in the last 1 year? Look for active discharge from ear.", HtmlCompat.FROM_HTML_MODE_LEGACY));
        binding.checkBoxC3.setText(HtmlCompat.fromHtml("<b>Dental Condition</b> – Look for white demineralized/brown tooth, discoloration, cavitations, swollen/bleeding/red gums.", HtmlCompat.FROM_HTML_MODE_LEGACY));
        binding.checkBoxC4.setText(HtmlCompat.fromHtml("<b>Skin Condition</b> – Does the child complain of itching on skin (especially at night)? Look for round or oval scaly patches/pustules in finger webs. Any other lesion on the skin.", HtmlCompat.FROM_HTML_MODE_LEGACY));
        binding.checkBoxC5.setText(HtmlCompat.fromHtml("<b>Reactive Airway Disease</b> – More than 3 episodes of increased shortness of breath, difficult breathing, and wheezing in the past 6 months.", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }
    private void saveDeficienciesData() {
        // Create the DeficiencyData object and set values based on checkboxes
        DeficiencyData deficiencyData = new DeficiencyData();
        deficiencyData.setSamWeightForHeight(binding.checkBoxB1.isChecked());
        deficiencyData.setSamOedema(binding.checkBoxB2.isChecked());
        deficiencyData.setSevereAnaemia(binding.checkBoxB3.isChecked());
        deficiencyData.setVitaminADeficiency(binding.checkBoxB4.isChecked());
        deficiencyData.setVitaminDDeficiency(binding.checkBoxB5.isChecked());
        deficiencyData.setConvulsiveDisorders(binding.checkBoxC1.isChecked());
        deficiencyData.setOtitisMedia(binding.checkBoxC2.isChecked());
        deficiencyData.setDentalCondition(binding.checkBoxC3.isChecked());
        deficiencyData.setSkinCondition(binding.checkBoxC4.isChecked());
        deficiencyData.setReactiveAirwayDisease(binding.checkBoxB5.isChecked());

        // Convert the DeficiencyData object to a JSON string
        Gson gson = new Gson();
        String deficiencyDataJson = gson.toJson(deficiencyData);

        // Save the JSON string to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SurveyData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("deficiencies", deficiencyDataJson);
        editor.apply();
    }
}
