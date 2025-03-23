package com.example.medistreamapplication.ThreeToSix;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.example.medistreamapplication.ReadMoreFirstForm;
import com.example.medistreamapplication.databinding.ActivityDefectsAtBirthFirstformBinding;
import com.example.medistreamapplication.model_form1.DefectData;
import com.google.gson.Gson;

public class Defects_at_birth_firstform extends AppCompatActivity {
    ActivityDefectsAtBirthFirstformBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDefectsAtBirthFirstformBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.defectEyes.setText(" Eyes \n Abnormality");
        binding.defectHead.setText(" Head Size \n Abnormality");
        binding.defectEars.setText(" Ears \n Abnormality");
        binding.defectLimbs.setText(" Limbs \n Deformity");
        binding.defectLipsPalate.setText(" Lips & \n Palate Cleft");
        binding.defectNeck.setText(" Neck \n Abnormality");
        binding.defectSpine.setText(" Spine \n Deformity");
        binding.defectHeart.setText(" Congenital \n Heart Disease");
        // Eye checkbox with HTML for bold text
        binding.a8aEye.setText(Html.fromHtml("<b>Eye</b> - Upward slant of eyes and/or epicanthic fold", Html.FROM_HTML_MODE_LEGACY));
        // Nose checkbox with HTML for bold text
        binding.a8bNose.setText(Html.fromHtml("<b>Nose</b> - Depressed Bridge", Html.FROM_HTML_MODE_LEGACY));
        // Ears checkbox with HTML for bold text
        binding.a8cEars.setText(Html.fromHtml("<b>Ears</b> - Low Set Ears", Html.FROM_HTML_MODE_LEGACY));
        // Palm checkbox with HTML for bold text
        binding.a8dPalm.setText(Html.fromHtml("<b>Palm</b> - Single crease across centre of palm\\n (Simian crease)S", Html.FROM_HTML_MODE_LEGACY));
        // Feet checkbox with HTML for bold text
        binding.a8eFeet.setText(Html.fromHtml("<b>Feet</b> - Wide Gap between great and first toe", Html.FROM_HTML_MODE_LEGACY));
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save defects at birth data
                saveDefectsData();

                // Move to next form
                Intent intent = new Intent(Defects_at_birth_firstform.this, Deficiencies_Section_FirstForm.class);
                startActivity(intent);
            }
        });
        binding.buttonReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to next form
                Intent intent = new Intent(Defects_at_birth_firstform.this, ReadMoreFirstForm.class);
                startActivity(intent);
            }
        });
    }

    private void saveDefectsData() {
        // Get data from the checkboxes
        boolean eyesAbnormality = binding.defectEyes.isChecked();
        boolean headSizeAbnormality = binding.defectHead.isChecked();
        boolean earsAbnormality = binding.defectEars.isChecked();
        boolean limbsDeformity = binding.defectLimbs.isChecked();
        boolean lipsPalateCleft = binding.defectLipsPalate.isChecked();
        boolean neckAbnormality = binding.defectNeck.isChecked();
        boolean spineDeformity = binding.defectSpine.isChecked();
        boolean congenitalHeartDisease = binding.defectHeart.isChecked();

        boolean eyeSyndrome = binding.a8aEye.isChecked();
        boolean noseSyndrome = binding.a8bNose.isChecked();
        boolean earsSyndrome = binding.a8cEars.isChecked();
        boolean palmSyndrome = binding.a8dPalm.isChecked();
        boolean feetSyndrome = binding.a8eFeet.isChecked();

        // Create DefectData object
        DefectData defectData = new DefectData();
        defectData.setEyesAbnormality(eyesAbnormality);
        defectData.setHeadSizeAbnormality(headSizeAbnormality);
        defectData.setEarsAbnormality(earsAbnormality);
        defectData.setLimbsDeformity(limbsDeformity);
        defectData.setLipsPalateCleft(lipsPalateCleft);
        defectData.setNeckAbnormality(neckAbnormality);
        defectData.setSpineDeformity(spineDeformity);
        defectData.setCongenitalHeartDisease(congenitalHeartDisease);
        defectData.setEyeSyndrome(eyeSyndrome);
        defectData.setNoseSyndrome(noseSyndrome);
        defectData.setEarsSyndrome(earsSyndrome);
        defectData.setPalmSyndrome(palmSyndrome);
        defectData.setFeetSyndrome(feetSyndrome);

        // Save data to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SurveyData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Saving the defectData as a JSON string
        String defectDataJson = new Gson().toJson(defectData);
        editor.putString("defectsAtBirth", defectDataJson);
        editor.apply();
    }
}
