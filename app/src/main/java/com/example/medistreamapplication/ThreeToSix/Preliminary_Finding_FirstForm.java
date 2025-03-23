package com.example.medistreamapplication.ThreeToSix;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import com.example.medistreamapplication.Dashboard.InternDashboard;
import com.example.medistreamapplication.Firebase.FirebaseConstants;
import com.example.medistreamapplication.R;
import com.example.medistreamapplication.model_form1.InternModel;
import com.example.medistreamapplication.model_form1.SurveyData;
import com.example.medistreamapplication.databinding.ActivityPreliminaryFindingFirstFormBinding;
import com.example.medistreamapplication.model_form1.DefectData;
import com.example.medistreamapplication.model_form1.DeficiencyData;
import com.example.medistreamapplication.model_form1.DevelopmentalDelaysData;
import com.example.medistreamapplication.model_form1.PreliminaryFinding;
import com.example.medistreamapplication.model_form1.SchoolData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;

public class Preliminary_Finding_FirstForm extends AppCompatActivity {
    ActivityPreliminaryFindingFirstFormBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreliminaryFindingFirstFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Firebase Storage reference
        storageReference = FirebaseStorage.getInstance().getReference("preliminary_images");

        // Submit button
        binding.submitButton.setOnClickListener(v -> {
            uploadImageAndSaveData();  // ✅ First upload the image, then save all data
        });

        // Image selection and upload
        binding.uploadImageView.setOnClickListener(v -> selectImage());
    }

    // Select image from gallery
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            binding.uploadImageView.setImageURI(imageUri);
        }
    }

    // ✅ Upload Image & Save All Data
    private void uploadImageAndSaveData() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        // ✅ Save Image URL and Proceed to Save Form Data
                        savePreliminaryData(imageUrl);
                        submitAllDataToFirebase();
                    }))
                    .addOnFailureListener(e -> Toast.makeText(this, "Image Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            // ✅ No Image Selected - Save Data Without Image
            savePreliminaryData("");
            submitAllDataToFirebase();
        }
    }

    // ✅ Save Preliminary Data Using Model Class
    private void savePreliminaryData(String imageUrl) {
        String remarks = binding.remarkEditText.getText().toString().trim();
        String referredDoctor = binding.referredDoctorEditText.getText().toString().trim();

        // ✅ Ensure all checkboxes & fields are correctly set
        PreliminaryFinding preliminaryFinding = new PreliminaryFinding();
        preliminaryFinding.setNeuralTubeDefect(binding.checkBox1.isChecked());
        preliminaryFinding.setDownsSyndrome(binding.checkBox2.isChecked());
        preliminaryFinding.setCleftLipAndPalate(binding.checkBox3.isChecked());
        preliminaryFinding.setTalipesClubFoot(binding.checkBox4.isChecked());
        preliminaryFinding.setDevelopmentalDysplasiaOfHip(binding.checkBox5.isChecked());
        preliminaryFinding.setCongenitalCataract(binding.checkBox6.isChecked());
        preliminaryFinding.setCongenitalDeafness(binding.checkBox7.isChecked());
        preliminaryFinding.setCongenitalHeartDisease(binding.checkBox8.isChecked());
        preliminaryFinding.setRetinopathyOfPrematurity(binding.checkBox9.isChecked());
        preliminaryFinding.setAnaemia(binding.checkBox10.isChecked());
        preliminaryFinding.setVitaminADeficiency(binding.checkBox11.isChecked());
        preliminaryFinding.setVitaminDDeficiency(binding.checkBox12.isChecked());
        preliminaryFinding.setSamStunting(binding.checkBox13.isChecked());
        preliminaryFinding.setGoiter(binding.checkBox14.isChecked());
        preliminaryFinding.setSkinConditions(binding.checkBox15.isChecked());
        preliminaryFinding.setOtitisMedia(binding.checkBox16.isChecked());
        preliminaryFinding.setRheumaticHeartDisease(binding.checkBox17.isChecked());
        preliminaryFinding.setReactiveAirwayDisease(binding.checkBox18.isChecked());
        preliminaryFinding.setDentalCaries(binding.checkBox19.isChecked());
        preliminaryFinding.setConvulsiveDisorders(binding.checkBox20.isChecked());
        preliminaryFinding.setVisionImpairment(binding.checkBox21.isChecked());
        preliminaryFinding.setHearingImpairment(binding.checkBox22.isChecked());
        preliminaryFinding.setNeuroMotorImpairment(binding.checkBox23.isChecked());
        preliminaryFinding.setMotorDelay(binding.checkBox24.isChecked());
        preliminaryFinding.setCognitiveDelay(binding.checkBox25.isChecked());
        preliminaryFinding.setSpeechAndLanguageDelay(binding.checkBox26.isChecked());
        preliminaryFinding.setBehaviorDisorder(binding.checkBox27.isChecked());
        preliminaryFinding.setLearningDisorder(binding.checkBox28.isChecked());
        preliminaryFinding.setAdhd(binding.checkBox29.isChecked());
        preliminaryFinding.setOthersSpecify(binding.checkBox30.isChecked());

        preliminaryFinding.setRemark(remarks);
        preliminaryFinding.setReferredDoctor(referredDoctor);
        preliminaryFinding.setImageUrl(imageUrl);

        // ✅ Save Data in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SurveyData", MODE_PRIVATE);
        Gson gson = new Gson();
        String preliminaryDataJson = gson.toJson(preliminaryFinding);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("preliminary", preliminaryDataJson);
        editor.apply();
    }

    // ✅ Submit All Data to Firebase
    private void submitAllDataToFirebase() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String internJson = sharedPreferences.getString("UserLogin", "");

        Gson gson = new Gson();
        InternModel intern = gson.fromJson(internJson, InternModel.class);

        if (intern == null || intern.getId() == null) {
            Toast.makeText(this, "Intern data missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        String internId = intern.getId(); // Get internId

        // Retrieve form data
        SharedPreferences surveySharedPrefs = getSharedPreferences("SurveyData", MODE_PRIVATE);
        String schoolDataJson = surveySharedPrefs.getString("schoolData", "");
        String defectsDataJson = surveySharedPrefs.getString("defectsAtBirth", "");
        String deficienciesDataJson = surveySharedPrefs.getString("deficiencies", "");
        String developmentalDelaysDataJson = surveySharedPrefs.getString("developmentalDelays", "");
        String preliminaryDataJson = surveySharedPrefs.getString("preliminary", "");

        // Create SurveyData object
        SurveyData surveyData = new SurveyData();
        surveyData.setSchoolData(gson.fromJson(schoolDataJson, SchoolData.class));
        surveyData.setDefectsData(gson.fromJson(defectsDataJson, DefectData.class));
        surveyData.setDeficienciesData(gson.fromJson(deficienciesDataJson, DeficiencyData.class));
        surveyData.setDevelopmentalDelaysData(gson.fromJson(developmentalDelaysDataJson, DevelopmentalDelaysData.class));
        surveyData.setPreliminaryFinding(gson.fromJson(preliminaryDataJson, PreliminaryFinding.class));

        // Firebase reference under Intern ID
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference surveyRef = database.getReference(FirebaseConstants.FORM_PATH + "ThreeToSixYear")
                .child(internId);

        // Push data to Firebase
        String surveyId = surveyRef.push().getKey();
        surveyData.setId(surveyId);

        surveyRef.child(surveyId).setValue(surveyData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Survey submitted successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, InternDashboard.class));
                finish();
            }
        });
    }
}
