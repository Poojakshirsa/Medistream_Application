package com.example.medistreamapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.medistreamapplication.Dashboard.AdminDashboard;
import com.example.medistreamapplication.Dashboard.DoctorDashboard;
import com.example.medistreamapplication.Dashboard.InternDashboard;
import com.example.medistreamapplication.databinding.ActivityLoginBinding;
import com.example.medistreamapplication.model_form1.InternModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private DatabaseReference adminDatabase, DoctorDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Database reference for 'admin' node
        adminDatabase = FirebaseDatabase.getInstance().getReference("project1/admin");
        DoctorDatabase= FirebaseDatabase.getInstance().getReference("project1/doctor");

        // Set onClickListener for login button
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        Glide.with(this)
                .asGif()
                .load(R.drawable.login) // Replace with your actual GIF file
                .into(binding.imageView);

    }

private void loginUser() {
        // Get the entered phone number and password
        String phone = binding.editTextPhone.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(phone)) {
            binding.editTextPhone.setError("Please enter your phone number");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            binding.editTextPassword.setError("Please enter your password");
            return;
        }
    // Static login check for "admin" username and password
    if (phone.equals("admin") && password.equals("admin")) {
        // Navigate to the next activity (e.g., HomeDashboard)
        Intent intent = new Intent(LoginActivity.this, AdminDashboard.class);
        startActivity(intent);
        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
    } else {
        // Show error if credentials don't match
        Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
    }
        // Fetch admin data from Firebase and authenticate
        adminDatabase.orderByChild("phoneNumber").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String storedPassword = snapshot.child("password").getValue(String.class);

                        if (storedPassword != null && storedPassword.equals(password)) {
                            // Login successful, navigate to the admin dashboard
                            Intent intent = new Intent(LoginActivity.this, DoctorDashboard.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Incorrect password
                            Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Phone number not found
                    Toast.makeText(LoginActivity.this, "Admin not found with this phone number", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Login failed: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

     DoctorDatabase.orderByChild("phoneNumber").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String storedPassword = snapshot.child("password").getValue(String.class);
                    InternModel intern = snapshot.getValue(InternModel.class);
                    if (storedPassword != null && storedPassword.equals(password)) {
                        // Login successful, navigate to the admin dashboard
                        Intent intent = new Intent(LoginActivity.this, InternDashboard.class);
                        // Convert the UserModel object to JSON using Gson
                        Gson gson = new Gson();
                        String json = gson.toJson(intern); // Store user details instead of intent
                        // Save login data in SharedPreferences
                        SharedPreferences.Editor editor = getSharedPreferences("AppPrefs", MODE_PRIVATE).edit();

                        editor.putString("UserLogin", json); // Save the user data as JSON
                        editor.apply();

                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Incorrect password
                        Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                // Phone number not found
                Toast.makeText(LoginActivity.this, "Admin not found with this phone number", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(LoginActivity.this, "Login failed: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    }
}
