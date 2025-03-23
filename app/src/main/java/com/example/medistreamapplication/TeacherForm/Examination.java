package com.example.medistreamapplication.TeacherForm;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medistreamapplication.Dashboard.InternDashboard;
import com.example.medistreamapplication.Firebase.FirebaseConstants;
import com.example.medistreamapplication.R;
import com.example.medistreamapplication.model_form1.InternModel;
import com.example.medistreamapplication.model_teacherForm.AddictionModel;
import com.example.medistreamapplication.model_teacherForm.Demographic_Form_Model;
import com.example.medistreamapplication.model_teacherForm.DietaryHistoryModel;
import com.example.medistreamapplication.model_teacherForm.ExaminationModel;
import com.example.medistreamapplication.model_teacherForm.HistoryExaminationModel;
import com.example.medistreamapplication.model_teacherForm.PhysicalActivityModel;
import com.example.medistreamapplication.model_teacherForm.SurveyDataFormThree;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

public class Examination extends AppCompatActivity {

    private TextInputEditText etWeight, etWaistHip, etBloodPressure, etHeight, etBmi, etPulse, etCvs, etRs, etBslRandom;
    private RadioGroup radioGroupCondition;
    private Button btnSubmit;

    private DatabaseReference databaseReference; // Firebase reference
    private EditText etRemarks, etReferredDoctor;
    private ImageView uploadImageView;
    private Button uploadButton;
    private Uri imageUri;
    private StorageReference storageReference;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination); // Ensure correct XML file is used

        // Initialize UI components
        etWeight = findViewById(R.id.et_weight);
        etWaistHip = findViewById(R.id.et_waistHip);
        etBloodPressure = findViewById(R.id.et_bloodPressure);
        etHeight = findViewById(R.id.et_height);
        etBmi = findViewById(R.id.et_bmi);
        etPulse = findViewById(R.id.et_pulse);
        etCvs = findViewById(R.id.et_cvs);
        etRs = findViewById(R.id.et_rs);
        etBslRandom = findViewById(R.id.et_bslRandom);
        radioGroupCondition = findViewById(R.id.radio_group_condition);
        btnSubmit = findViewById(R.id.btn_submit);

        etRemarks = findViewById(R.id.remarkEditText);
        etReferredDoctor = findViewById(R.id.referredDoctorEditText);
        uploadImageView = findViewById(R.id.uploadImageView);
        uploadButton = findViewById(R.id.uploadButton);
        storageReference = FirebaseStorage.getInstance().getReference("examination_images");

        uploadImageView.setOnClickListener(v -> selectImage());
        uploadButton.setOnClickListener(v -> uploadImageAndSaveData());


        // Submit button click listener
        btnSubmit.setOnClickListener(v -> {
            uploadImageAndSaveData(); // First upload image, then save data & submit
        });

    }


    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImageView.setImageURI(imageUri);
        }
    }

    private void uploadImageAndSaveData() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveDataToFirebase(imageUrl);  // Save form data after image is uploaded
                        submitAllDataToFirebase(); // Now submit all data to Firebase
                    }))
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_SHORT).show());
        } else {
            saveDataToFirebase("");  // Save form data without image
            submitAllDataToFirebase(); // Still submit to Firebase
        }
    }



    private void saveDataToFirebase(String imageUrl) {
        // Retrieve existing data to prevent retaining old values
        SharedPreferences sharedPreferences = getSharedPreferences("SchoolSurveyData", MODE_PRIVATE);
        String existingData = sharedPreferences.getString("Examination", "");

        Gson gson = new Gson();
        ExaminationModel examination;

        if (!existingData.isEmpty()) {
            examination = gson.fromJson(existingData, ExaminationModel.class);
        } else {
            examination = new ExaminationModel();
        }

        // Get user inputs (if empty, replace with "")
        examination.setWeight(etWeight.getText().toString().trim());
        examination.setWaistHipRatio(etWaistHip.getText().toString().trim());
        examination.setBloodPressure(etBloodPressure.getText().toString().trim());
        examination.setHeight(etHeight.getText().toString().trim());
        examination.setBmi(etBmi.getText().toString().trim());
        examination.setPulse(etPulse.getText().toString().trim());
        examination.setCondition(radioGroupCondition.getCheckedRadioButtonId() != -1
                ? ((RadioButton) findViewById(radioGroupCondition.getCheckedRadioButtonId())).getText().toString()
                : "");
        examination.setCvs(etCvs.getText().toString().trim());
        examination.setRs(etRs.getText().toString().trim());
        examination.setBslRandom(etBslRandom.getText().toString().trim());
        examination.setRemark(etRemarks.getText().toString().trim());
        examination.setReferredDoctor(etReferredDoctor.getText().toString().trim());
        examination.setImageUrl(imageUrl);  // Update image if available

        // Save updated data to SharedPreferences (overwrite old values)
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(examination);
        editor.putString("Examination", json);
        editor.apply();

        // ✅ Debugging: Log the saved data
        Log.d("ExaminationData", "Updated & Saved: " + json);
    }







    private void submitAllDataToFirebase() {
        SharedPreferences surveySharedPrefs = getSharedPreferences("SchoolSurveyData", MODE_PRIVATE);
        String ExaminationDataJson = surveySharedPrefs.getString("Examination", "");

        // ✅ Debugging Step: Log the retrieved data
        Log.d("FirebaseSubmission", "Retrieved ExaminationDataJson: " + ExaminationDataJson);

        if (ExaminationDataJson.isEmpty()) {
            Toast.makeText(this, "Examination data missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();
        ExaminationModel examinationModel = gson.fromJson(ExaminationDataJson, ExaminationModel.class);

        if (examinationModel == null) {
            Toast.makeText(this, "Failed to parse examination data!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve Intern ID
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String internJson = sharedPreferences.getString("UserLogin", "");
        InternModel intern = gson.fromJson(internJson, InternModel.class);

        if (intern == null || intern.getId() == null) {
            Toast.makeText(this, "Intern data missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        String internId = intern.getId();

        // Retrieve other form data
        String Demographic_Form = surveySharedPrefs.getString("demographicData", "");
        String HistoryDataJson = surveySharedPrefs.getString("historyExaminationData", "");
        String DietryDataJson = surveySharedPrefs.getString("Dietary_history", "");
        String PhysicalDataJson = surveySharedPrefs.getString("Physical_Activity", "");
        String AddictionDataJson = surveySharedPrefs.getString("Addiction_Form", "");

        // ✅ Debugging Step: Check if all data is retrieved before Firebase submission
        Log.d("FirebaseSubmission", "Demographic_Form: " + Demographic_Form);
        Log.d("FirebaseSubmission", "HistoryDataJson: " + HistoryDataJson);
        Log.d("FirebaseSubmission", "DietryDataJson: " + DietryDataJson);
        Log.d("FirebaseSubmission", "PhysicalDataJson: " + PhysicalDataJson);
        Log.d("FirebaseSubmission", "AddictionDataJson: " + AddictionDataJson);
        Log.d("FirebaseSubmission", "ExaminationModel: " + new Gson().toJson(examinationModel));

        // Create SurveyData object
        SurveyDataFormThree surveyData = new SurveyDataFormThree();
        surveyData.setDemographicFormModel(gson.fromJson(Demographic_Form, Demographic_Form_Model.class));
        surveyData.setHistoryExaminationModel(gson.fromJson(HistoryDataJson, HistoryExaminationModel.class));
        surveyData.setDietaryHistoryModel(gson.fromJson(DietryDataJson, DietaryHistoryModel.class));
        surveyData.setPhysicalActivityModel(gson.fromJson(PhysicalDataJson, PhysicalActivityModel.class));
        surveyData.setAddictionModel(gson.fromJson(AddictionDataJson, AddictionModel.class));
        surveyData.setExaminationModel(examinationModel);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference surveyRef = database.getReference(FirebaseConstants.FORM_PATH + "TeacherForm")
                .child(internId);

        String surveyId = surveyRef.push().getKey();
        surveyData.setId(surveyId);

        if (surveyId != null) {
            surveyRef.child(surveyId).setValue(surveyData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Examination.this, "Survey submitted successfully!", Toast.LENGTH_SHORT).show();
                    Log.d("FirebaseSubmission", "Survey submitted successfully.");
                    Intent intent = new Intent(Examination.this, InternDashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Examination.this, "Failed to submit survey.", Toast.LENGTH_SHORT).show();
                    Log.e("FirebaseSubmission", "Survey submission failed: " + task.getException());
                }
            });
        }
    }

}
