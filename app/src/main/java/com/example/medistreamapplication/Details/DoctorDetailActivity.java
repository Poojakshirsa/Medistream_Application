package com.example.medistreamapplication.Details;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.medistreamapplication.R;

public class DoctorDetailActivity extends AppCompatActivity {

    private TextView textViewName, textViewPhoneNumber, textViewAddress, textViewEducation, textViewProfessionalExperience, textViewEmail;
    private ImageView imageViewAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);

        textViewName = findViewById(R.id.textViewName);
        textViewPhoneNumber = findViewById(R.id.textViewPhoneNumber);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewEducation = findViewById(R.id.textViewEducation);
        textViewProfessionalExperience = findViewById(R.id.textViewProfessionalExperience);
        textViewEmail = findViewById(R.id.textViewEmail);
        imageViewAdmin = findViewById(R.id.imageViewAdmin);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show the back arrow
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back when the back arrow is clicked
                onBackPressed();
            }
        });
        // Retrieve data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AdminDetails", MODE_PRIVATE);
        String name = sharedPreferences.getString("admin_name", "N/A");
        String phoneNumber = sharedPreferences.getString("admin_phone_number", "N/A");
        String address = sharedPreferences.getString("admin_address", "N/A");
        String education = sharedPreferences.getString("admin_education", "N/A");
        String experience = sharedPreferences.getString("Experience", "N/A"); // Ensure this key matches
        String email = sharedPreferences.getString("admin_email", "N/A");
        String imageUrl = sharedPreferences.getString("admin_image_url", null);

        // Set the retrieved data to the TextViews with bold labels using HTML
        textViewName.setText(Html.fromHtml("<b>Name:</b> " + name));
        textViewPhoneNumber.setText(Html.fromHtml("<b>Phone Number:</b> " + phoneNumber));
        textViewAddress.setText(Html.fromHtml("<b>Address:</b> " + address));
        textViewEducation.setText(Html.fromHtml("<b>Education:</b> " + education));
        textViewProfessionalExperience.setText(Html.fromHtml("<b>Experience:</b> " + experience));
        textViewEmail.setText(Html.fromHtml("<b>Email:</b> " + email));


        // Load image using Glide if URL is available
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(imageViewAdmin);
        }
    }
}
