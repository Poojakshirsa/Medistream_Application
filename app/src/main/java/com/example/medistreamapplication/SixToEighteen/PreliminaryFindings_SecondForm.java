package com.example.medistreamapplication.SixToEighteen;

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
import com.example.medistreamapplication.databinding.ActivityPreliminaryFindingsSecondFormBinding;
import com.example.medistreamapplication.model_form1.InternModel;
import com.example.medistreamapplication.model_form2.AdolescentFormModel;
import com.example.medistreamapplication.model_form2.DefectDataFormTwo;
import com.example.medistreamapplication.model_form2.DevelopmentDelayModelFormTwo;
import com.example.medistreamapplication.model_form2.PreliminaryFindingsFormTwo;
import com.example.medistreamapplication.model_form2.SchoolDataSecondForm;
import com.example.medistreamapplication.model_form2.SurveyDataFormTwo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

public class PreliminaryFindings_SecondForm extends AppCompatActivity {
    ActivityPreliminaryFindingsSecondFormBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreliminaryFindingsSecondFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Firebase Storage reference
        storageReference = FirebaseStorage.getInstance().getReference("preliminary_images_secondForm");

        // Submit button
        binding.submitButton.setOnClickListener(v -> {
            uploadImageAndSaveData(); // ✅ First upload the image, then save data
        });

        // Image selection
        binding.uploadImageView.setOnClickListener(v -> selectImage());
    }

    // Select image from gallery
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            binding.uploadImageView.setImageURI(imageUri);
        }
    }

    // ✅ Upload Image & Save Data
    private void uploadImageAndSaveData() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
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
        PreliminaryFindingsFormTwo model = new PreliminaryFindingsFormTwo();
        model.setNeuralTubeDefect(binding.checkBox1.isChecked());
        model.setDownsSyndrome(binding.checkBox2.isChecked());
        model.setCleftLipPalate(binding.checkBox3.isChecked());
        model.setTalipes(binding.checkBox4.isChecked());
        model.setDevelopmentalDysplasiaHip(binding.checkBox5.isChecked());
        model.setCongenitalCataract(binding.checkBox6.isChecked());
        model.setCongenitalDeafness(binding.checkBox7.isChecked());
        model.setCongenitalHeartDisease(binding.checkBox8.isChecked());
        model.setRetinopathyPrematurity(binding.checkBox9.isChecked());
        model.setAnaemia(binding.checkBox10.isChecked());
        model.setVitaminADeficiency(binding.checkBox11.isChecked());
        model.setVitaminDDeficiency(binding.checkBox12.isChecked());
        model.setSamStunting(binding.checkBox13.isChecked());
        model.setGoiter(binding.checkBox14.isChecked());
        model.setSkinConditions(binding.checkBox15.isChecked());
        model.setOtitisMedia(binding.checkBox16.isChecked());
        model.setRheumaticHeartDisease(binding.checkBox17.isChecked());
        model.setReactiveAirwayDisease(binding.checkBox18.isChecked());
        model.setDentalCaries(binding.checkBox19.isChecked());
        model.setConvulsiveDisorders(binding.checkBox20.isChecked());
        model.setVisionImpairment(binding.checkBox21.isChecked());
        model.setHearingImpairment(binding.checkBox22.isChecked());
        model.setNeuroMotorImpairment(binding.checkBox23.isChecked());
        model.setMotorDelay(binding.checkBox24.isChecked());
        model.setCognitiveDelay(binding.checkBox25.isChecked());
        model.setSpeechLanguageDelay(binding.checkBox26.isChecked());
        model.setBehaviorDisorder(binding.checkBox27.isChecked());
        model.setLearningDisorder(binding.checkBox28.isChecked());
        model.setAdhd(binding.checkBox29.isChecked());

        // ✅ Save remarks, referredDoctor, and Image URL
        model.setRemark(remarks);
        model.setReferredDoctor(referredDoctor);
        model.setImageUrl(imageUrl);

        // ✅ Save to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SurveyDataFormTwo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String preliminaryDataJson = gson.toJson(model);
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

        // ✅ Retrieve All Sections from SharedPreferences
        SharedPreferences surveySharedPrefs = getSharedPreferences("SurveyDataFormTwo", MODE_PRIVATE);
        String schoolDataJson = surveySharedPrefs.getString("schoolDataForm2", "");
        String defectsDataJson = surveySharedPrefs.getString("defectsAtBirth", "");
        String adolescentSpecificJson = surveySharedPrefs.getString("adolescentForm", "");
        String developmentalDelaysDataJson = surveySharedPrefs.getString("developmentalDelays", "");
        String preliminaryDataJson = surveySharedPrefs.getString("preliminary", "");

        // ✅ Create Full SurveyDataFormTwo Object
        SurveyDataFormTwo surveyData = new SurveyDataFormTwo();
        surveyData.setSchoolData(gson.fromJson(schoolDataJson, SchoolDataSecondForm.class));
        surveyData.setDefectsData(gson.fromJson(defectsDataJson, DefectDataFormTwo.class));
        surveyData.setDevelopmentalDelaysData(gson.fromJson(developmentalDelaysDataJson, DevelopmentDelayModelFormTwo.class));
        surveyData.setAdolescentFormModel(gson.fromJson(adolescentSpecificJson, AdolescentFormModel.class));
        surveyData.setPreliminaryFinding(gson.fromJson(preliminaryDataJson, PreliminaryFindingsFormTwo.class));

        // ✅ Firebase Reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference surveyRef = database.getReference(FirebaseConstants.FORM_PATH + "SixToEighteen")
                .child(internId);

        // ✅ Push Data to Firebase
        String surveyId = surveyRef.push().getKey();
        surveyData.setId(surveyId);

        surveyRef.child(surveyId).setValue(surveyData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Survey submitted successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, InternDashboard.class));
                finish();
            } else {
                Toast.makeText(this, "Failed to submit survey!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
