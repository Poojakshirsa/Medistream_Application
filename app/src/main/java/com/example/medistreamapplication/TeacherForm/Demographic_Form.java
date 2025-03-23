package com.example.medistreamapplication.TeacherForm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.medistreamapplication.R;
import com.example.medistreamapplication.ThreeToSix.School_Information_Section_FirstForm;
import com.example.medistreamapplication.databinding.ActivityDemographicFormBinding;
import com.example.medistreamapplication.model_teacherForm.Demographic_Form_Model;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Demographic_Form extends AppCompatActivity {

    ActivityDemographicFormBinding binding;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDemographicFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupGenderSpinner();
        setupMaritalStatus();
        setupSchoolAutoComplete(); // Load school details
        setupDatePickerDOB();

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSchoolData();
                Intent intent = new Intent(Demographic_Form.this, history_examination_form.class);
                startActivity(intent);
            }
        });
    }

    private void setupDatePickerDOB() {
        binding.dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Show DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Demographic_Form.this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            // Format the selected date
                            String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                            binding.dobEditText.setText(formattedDate);
                        },
                        year, month, dayOfMonth
                );

                datePickerDialog.show();
            }
        });
    }

    private void setupSchoolAutoComplete() {
        List<String> schoolDetails = loadSchoolDetails();

        if (schoolDetails.isEmpty()) {
            Log.e("AutoComplete", "No school data loaded! Check schools.csv file.");
        } else {
            Log.d("AutoComplete", "Loaded " + schoolDetails.size() + " schools with details.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, schoolDetails);
        binding.schoolName.setAdapter(adapter);
        binding.schoolName.setThreshold(1); // Show suggestions after typing 1 character
    }
    private String generateUniqueId(String schoolName, String childName, String dob) {
        // Extract only the school name before first comma (if any)
        String cleanedSchoolName = schoolName.split(",")[0].trim();

        // Get initials from the cleaned school name
        String schoolInitials = getInitials(cleanedSchoolName);

        // Get initials from child's name
        String childInitials = getInitials(childName);

        // Format the DOB (dd/mm/yyyy → ddmmyy)
        String formattedDob = dob.replace("/", "").substring(0, 6);

        // Concatenate for unique ID
        return schoolInitials + childInitials + formattedDob;
    }


    // Helper function to get initials
    private String getInitials(String name) {
        StringBuilder initials = new StringBuilder();
        String[] words = name.trim().split("\\s+"); // Split by spaces

        for (String word : words) {
            if (!word.isEmpty()) {
                initials.append(word.charAt(0)); // Get first letter
            }
        }
        return initials.toString().toUpperCase(Locale.ROOT); // Convert to uppercase
    }

    private List<String> loadSchoolDetails() {
        List<String> schoolList = new ArrayList<>();
        try {
            InputStream inputStream = getAssets().open("schools.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(","); // Split CSV by commas

                if (columns.length >= 2) { // Ensure there's at least a name and one detail
                    String schoolDetails = columns[0].trim(); // School Name
                    for (int i = 1; i < columns.length; i++) { // Append all other columns
                        schoolDetails += " - " + columns[i].trim();
                    }
                    schoolList.add(schoolDetails);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return schoolList;
    }


    private void setupGenderSpinner() {
        Spinner genderSpinner = binding.genderSpinner;
        String[] genderOptions = {"Select Gender", "Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
    }

    private void setupMaritalStatus() {
        Spinner maritalSpinner = binding.maritalStatus;
        String[] maritalOptions = {"Select Marital Status", "Unmarried", "Married", "Divorced", "Widow"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, maritalOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maritalSpinner.setAdapter(adapter);
    }

    private void saveSchoolData() {
        // Get data from input fields
        String name = binding.Name.getText().toString().trim();
        String age = binding.age.getText().toString().trim();
        String dateOfBirth = binding.dobEditText.getText().toString().trim(); // ✅ Capture DOB
        String schoolName = binding.schoolName.getText().toString().trim();
        String schoolAddress = binding.schoolAddress.getText().toString().trim();
        String designation = binding.designation.getText().toString().trim();
        String education = binding.education.getText().toString().trim();
        String workingExperience = binding.workingExperience.getText().toString().trim();
        String schoolTiming = binding.schoolTiming.getText().toString().trim();
        String monthlyIncome = binding.monthlyIncome.getText().toString().trim();
        String uniqueId = generateUniqueId(schoolName, name, dateOfBirth);
        // Get selected values from Spinners
        String gender = binding.genderSpinner.getSelectedItem().toString();
        String maritalStatus = binding.maritalStatus.getSelectedItem().toString();

        // ✅ Ensure empty fields save as ""
        name = name.isEmpty() ? "" : name;
        age = age.isEmpty() ? "" : age;
        dateOfBirth = dateOfBirth.isEmpty() ? "" : dateOfBirth; // ✅ Handle empty DOB
        schoolName = schoolName.isEmpty() ? "" : schoolName;
        schoolAddress = schoolAddress.isEmpty() ? "" : schoolAddress;
        designation = designation.isEmpty() ? "" : designation;
        education = education.isEmpty() ? "" : education;
        workingExperience = workingExperience.isEmpty() ? "" : workingExperience;
        schoolTiming = schoolTiming.isEmpty() ? "" : schoolTiming;
        monthlyIncome = monthlyIncome.isEmpty() ? "" : monthlyIncome;

        // ✅ Ensure dropdown values save as "" if not selected
        gender = gender.equals("Select Gender") ? "" : gender;
        maritalStatus = maritalStatus.equals("Select Marital Status") ? "" : maritalStatus;

        // Create and populate the Demographic_Form_Model object
        Demographic_Form_Model demographicFormModel = new Demographic_Form_Model();
        demographicFormModel.setEmployeeName(name);
        demographicFormModel.setAge(age);
        demographicFormModel.setDateOfBirth(dateOfBirth); // ✅ Set DOB
        demographicFormModel.setSchoolName(schoolName);
        demographicFormModel.setSchoolAddress(schoolAddress);
        demographicFormModel.setDesignation(designation);
        demographicFormModel.setEducation(education);
        demographicFormModel.setWorkingExperience(workingExperience);
        demographicFormModel.setSchoolTiming(schoolTiming);
        demographicFormModel.setMonthlyIncome(monthlyIncome);
        demographicFormModel.setGender(gender);
        demographicFormModel.setMaritalStatus(maritalStatus);
        demographicFormModel.setUniqueId(uniqueId);

        // Save to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SchoolSurveyData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(demographicFormModel);
        editor.putString("demographicData", json);
        editor.apply();

        // ✅ Debugging Log
        Log.d("DemographicData", "Saved to SharedPreferences: " + json);
    }


}
