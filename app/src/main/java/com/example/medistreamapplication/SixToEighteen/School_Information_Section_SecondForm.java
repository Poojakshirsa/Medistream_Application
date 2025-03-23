package com.example.medistreamapplication.SixToEighteen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.AutoCompleteTextView;

import com.example.medistreamapplication.R;
import com.example.medistreamapplication.ThreeToSix.School_Information_Section_FirstForm;
import com.example.medistreamapplication.databinding.ActivitySchoolInformationSectionSecondFormBinding;
import com.example.medistreamapplication.model_form2.SchoolDataSecondForm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class School_Information_Section_SecondForm extends AppCompatActivity {
    ActivitySchoolInformationSectionSecondFormBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySchoolInformationSectionSecondFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupGenderSpinner();
        setupSchoolAutoComplete(); // ✅ Load school names from CSV
        setupDatePickerDOB();
        setupDatePicker();

        binding.nextButton.setOnClickListener(view -> {
            saveSchoolData();
            Intent intent = new Intent(School_Information_Section_SecondForm.this, Defect_Deficiency_Disease.class);
            startActivity(intent);
        });

        setupBMIClassificationSpinner();
        setupBPSpinner();
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
                        School_Information_Section_SecondForm.this,
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

    private void setupDatePicker() {
        binding.dateOfCheckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Show the DatePickerDialog with custom theme
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        School_Information_Section_SecondForm.this,
                        R.style.CustomDatePickerDialog, // Use the custom theme here
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Format and set the date in the TextInputEditText
                                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                binding.dateOfCheckup.setText(selectedDate);
                            }
                        },
                        year, month, dayOfMonth);
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
        Spinner genderSpinner = binding.GenderSpinner;
        String[] genderOptions = {"Select Gender", "Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
    }

    private void setupBMIClassificationSpinner() {
        Spinner bmiSpinner = binding.BMIClassificationLayout;
        String[] bmiOptions = {"Select BMI Classification", "Normal", "Underweight", "Obese"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bmiOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bmiSpinner.setAdapter(adapter);
    }

    private void setupBPSpinner() {
        Spinner bpSpinner = binding.BPLayout;
        String[] bpOptions = {"Select BP (mm Hg, Systolic)", "Normal", "Pre HTN", "Stage 1 HTN", "Stage 2 HTN"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bpOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bpSpinner.setAdapter(adapter);
    }

    private void saveSchoolData() {
        // Get all form data
        String schoolName = binding.schoolName.getText().toString();
        String schoolAddress = binding.schoolAddress.getText().toString();
        String checkupDate = binding.dateOfCheckup.getText().toString();
        String dob = binding.dobEditText.getText().toString(); // ✅ Get DOB from input field
        String childName = binding.childName.getText().toString();
        String childAge = binding.childAge.getText().toString();
        String selectedGender = binding.GenderSpinner.getSelectedItem().toString();
        String section = binding.ClassSection.getText().toString();
        String teacherInChargeName = binding.teacherInchargeName.getText().toString();
        String teacherInChargeContact = binding.teacherInchargeContact.getText().toString();
        String fatherName = binding.fatherName.getText().toString();
        String motherName = binding.motherName.getText().toString();
        String parentContact = binding.parentContact.getText().toString();
        String childWeight = binding.weight.getText().toString();
        String childHeight = binding.height.getText().toString();
        String BMI = binding.BMI.getText().toString();
        String BMI_Classification_layout = binding.BMIClassificationLayout.getSelectedItem().toString();
        String Bp_Layout = binding.BPLayout.getSelectedItem().toString();
        String uniqueId = generateUniqueId(schoolName, childName, dob);
        // Create SchoolData object
        SchoolDataSecondForm schoolData = new SchoolDataSecondForm();
        schoolData.setSchoolName(schoolName);
        schoolData.setSchoolAddress(schoolAddress);
        schoolData.setDateOfCheckup(checkupDate);
        schoolData.setDateOfBirth(dob); // ✅ Set DOB
        schoolData.setChildName(childName);
        schoolData.setChildAge(childAge);
        schoolData.setGender(selectedGender);
        schoolData.setSection(section);
        schoolData.setTeacherInchargeName(teacherInChargeName);
        schoolData.setTeacherInchargeContact(teacherInChargeContact);
        schoolData.setFatherName(fatherName);
        schoolData.setMotherName(motherName);
        schoolData.setParentContact(parentContact);
        schoolData.setChildWeight(childWeight);
        schoolData.setChildHeight(childHeight);
        schoolData.setBMI(BMI);
        schoolData.setBMI_Classification(BMI_Classification_layout);
        schoolData.setBp_layout(Bp_Layout);
        schoolData.setUniqueId(uniqueId);

        // Convert SchoolData to JSON and save it in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SurveyDataFormTwo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("schoolDataForm2", schoolData.toJson());
        editor.apply();
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

}
