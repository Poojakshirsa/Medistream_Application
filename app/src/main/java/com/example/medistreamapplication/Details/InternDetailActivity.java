package com.example.medistreamapplication.Details;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.medistreamapplication.FullScreenImageActivity;
import com.example.medistreamapplication.R;
import com.example.medistreamapplication.show_form;

public class InternDetailActivity extends AppCompatActivity {

    private TextView textViewName, textViewPhone, textViewAddress, textViewEmail;
    private ImageView imageViewIntern;
    Button show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intern_detail);

        // Initialize views
        textViewName = findViewById(R.id.textViewInternNameDetail);
        textViewPhone = findViewById(R.id.textViewInternPhoneDetail);
        textViewAddress = findViewById(R.id.textViewInternAddressDetail); // New TextView for address
        textViewEmail = findViewById(R.id.textViewInternEmailDetail); // New TextView for email
        imageViewIntern = findViewById(R.id.imageViewInternDetail);
        show=findViewById(R.id.show);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InternDetailActivity.this, show_form.class );
                startActivity(intent);
            }
        });
        // Retrieve data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("InternDetails", MODE_PRIVATE);
        String name = sharedPreferences.getString("intern_name", "N/A");
        String phone = sharedPreferences.getString("intern_phone", "N/A");
        String address = sharedPreferences.getString("intern_address", "N/A"); // Retrieve address
        String email = sharedPreferences.getString("intern_email", "N/A"); // Retrieve email
        String imageUrl = sharedPreferences.getString("intern_image_url", "");
        String id=sharedPreferences.getString("intern_id","");

        // Set data to views with bold labels
        textViewName.setText(Html.fromHtml("<b>Name:</b> " + name));
        textViewPhone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
        textViewAddress.setText(Html.fromHtml("<b>Address:</b> " + address)); // Set address
        textViewEmail.setText(Html.fromHtml("<b>Email:</b> " + email));     // Set email

        // Load image using Glide
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.baseline_person_24) // Replace with your placeholder image
                .error(R.drawable.baseline_person_24)
                .into(imageViewIntern);

        imageViewIntern.setOnClickListener(view -> {
            Intent intent = new Intent(InternDetailActivity.this, FullScreenImageActivity.class);
            intent.putExtra("imageUrl", imageUrl);
            startActivity(intent);
        });


    }
}
