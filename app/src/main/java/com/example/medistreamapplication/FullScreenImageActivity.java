package com.example.medistreamapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        @SuppressLint("MissingInflatedId") SubsamplingScaleImageView zoomableImageView = findViewById(R.id.zoomableImageView);

        // Get the image URL from the intent
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Load the image directly
        if (imageUrl != null) {
            zoomableImageView.setImage(ImageSource.uri(imageUrl));
        } else {
            // Fallback to a default image if the URL is null
            zoomableImageView.setImage(ImageSource.resource(R.drawable.baseline_person_24));
        }
    }
}
