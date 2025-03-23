package com.example.medistreamapplication.ThreeToSix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.example.medistreamapplication.R;
import com.example.medistreamapplication.databinding.ActivityDiseasesFirstBinding;
import com.example.medistreamapplication.model_form1.DiseaseData;
import com.google.gson.Gson;

public class Diseases_FirstActivity extends AppCompatActivity {
    ActivityDiseasesFirstBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiseasesFirstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the passed data
        String deficiencyDataJson = getIntent().getStringExtra("deficiencyData");
        String defectDataJson = getIntent().getStringExtra("defectData");
        String schoolDataJson = getIntent().getStringExtra("schoolData");

        // Set up checkboxes for diseases
        setupCheckBoxes();

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an instance of DiseaseData
                DiseaseData diseaseData = new DiseaseData();

                // Set the values based on the CheckBox states
                diseaseData.setConvulsiveDisorders(binding.checkBoxC1.isChecked());
                diseaseData.setOtitisMedia(binding.checkBoxC2.isChecked());
                diseaseData.setDentalCondition(binding.checkBoxC3.isChecked());
                diseaseData.setSkinCondition(binding.checkBoxC4.isChecked());
                diseaseData.setReactiveAirwayDisease(binding.checkBoxC5.isChecked());

                // Convert to JSON
                String diseaseDataJson = new Gson().toJson(diseaseData);

                // Create intent to start next activity
                Intent intent = new Intent(Diseases_FirstActivity.this, Developmental_Delays_Form_FirstForm.class);
                // Put the JSON data in the intent
                intent.putExtra("deficiencyData", deficiencyDataJson);
                intent.putExtra("defectData", defectDataJson);
                intent.putExtra("schoolData", schoolDataJson);
                intent.putExtra("diseaseData", diseaseDataJson);
                // Start the next activity
                startActivity(intent);
            }
        });
    }

    private void setupCheckBoxes() {
        // Checkbox for Convulsive Disorders (C1)
        CheckBox checkBoxC1 = findViewById(R.id.checkBox_c1);
        checkBoxC1.setText(HtmlCompat.fromHtml("<b>Convulsive Disorders</b> – Did the child ever have/had spells of unconsciousness and fits?", HtmlCompat.FROM_HTML_MODE_LEGACY));

        // Checkbox for Otitis Media (C2)
        CheckBox checkBoxC2 = findViewById(R.id.checkBox_c2);
        checkBoxC2.setText(HtmlCompat.fromHtml("<b>Otitis Media</b> – Did the child have more than 3 episodes of ear discharge in the last 1 year? Look for active discharge from ear.", HtmlCompat.FROM_HTML_MODE_LEGACY));

        // Checkbox for Dental Condition (C3)
        CheckBox checkBoxC3 = findViewById(R.id.checkBox_c3);
        checkBoxC3.setText(HtmlCompat.fromHtml("<b>Dental Condition</b> – Look for white demineralized/brown tooth, discoloration, cavitations, swollen/bleeding/red gums.", HtmlCompat.FROM_HTML_MODE_LEGACY));

        // Checkbox for Skin Condition (C4)
        CheckBox checkBoxC4 = findViewById(R.id.checkBox_c4);
        checkBoxC4.setText(HtmlCompat.fromHtml("<b>Skin Condition</b> – Does the child complain of itching on skin (especially at night)? Look for round or oval scaly patches/pustules in finger webs. Any other lesion on the skin.", HtmlCompat.FROM_HTML_MODE_LEGACY));

        // Checkbox for Reactive Airway Disease (C5)
        CheckBox checkBoxC5 = findViewById(R.id.checkBox_c5);
        checkBoxC5.setText(HtmlCompat.fromHtml("<b>Reactive Airway Disease</b> – More than 3 episodes of increased shortness of breath, difficult breathing, and wheezing in the past 6 months.", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }
}
