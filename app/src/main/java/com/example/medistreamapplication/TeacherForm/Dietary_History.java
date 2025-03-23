package com.example.medistreamapplication.TeacherForm;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.medistreamapplication.R;
import com.example.medistreamapplication.databinding.ActivityDietaryHistoryBinding;
import com.example.medistreamapplication.model_teacherForm.DietaryHistoryModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class Dietary_History extends AppCompatActivity {
    ActivityDietaryHistoryBinding binding;
    DatabaseReference databaseReference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDietaryHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.nextButton.setOnClickListener(view -> {
            Intent intent=new Intent(Dietary_History.this, Physical_Activity.class);
            saveDataToFirebase();
            startActivity(intent);

        });
    }

    private void saveDataToFirebase() {
        DietaryHistoryModel history = new DietaryHistoryModel();
        history.setDietType(getSelectedRadioButtonText(binding.dietTypeRadioGroup));
        history.setBreakfastDaily(getBooleanFromRadioGroup(binding.breakfastRadioGroup));
        history.setNumberOfMeals(getSelectedRadioButtonText(binding.mealsRadioGroup));
        history.setEatingBetweenMeals(getSelectedRadioButtonText(binding.snacksRadioGroup));
        history.setConsumeFruits(getBooleanFromRadioGroup(binding.fruitConsumptionRadioGroup));
        history.setFruitQuantity(getSelectedRadioButtonText(binding.fruitQuantityRadioGroup));
        history.setEatingOutside(getSelectedRadioButtonText(binding.eatingOutsideRadioGroup));
        history.setCookingOil(getSelectedRadioButtonText(binding.oilRadioGroup));
        history.setChangeCookingOil(getBooleanFromRadioGroup(binding.changeOilRadioGroup));
        history.setExtraSalt(getBooleanFromRadioGroup(binding.extraSaltRadioGroup));
        history.setTeaCoffeeCount(getSelectedRadioButtonText(binding.teaCoffeeRadioGroup));
        history.setNonVegetarian(getBooleanFromRadioGroup(binding.nonVegRadioGroup));
        history.setDietaryHabits(binding.dietaryHabitsEditText.getText().toString().trim());


        SharedPreferences sharedPreferences = getSharedPreferences("SchoolSurveyData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(history); // Convert object to JSON
        editor.putString("Dietary_history", json);
        editor.apply();
    }

    private String getSelectedRadioButtonText(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedButton = findViewById(selectedId);
            return selectedButton.getText().toString();
        }
        return "Not selected"; // Default if nothing is chosen
    }

    private boolean getBooleanFromRadioGroup(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedButton = findViewById(selectedId);
            return selectedButton.getText().toString().equalsIgnoreCase("Yes");
        }
        return false; // Default to false
    }
}
