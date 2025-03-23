package com.example.medistreamapplication.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.medistreamapplication.Firebase.FirebaseConstants;
import com.example.medistreamapplication.model_form1.InternModel;
import com.example.medistreamapplication.R;
import com.example.medistreamapplication.databinding.FragmentAddInternBinding; // Import binding class
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class AddInternFragment extends Fragment {

    private FragmentAddInternBinding binding; // ViewBinding variable
    private Uri imageUri;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    public AddInternFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize ViewBinding
        binding = FragmentAddInternBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize Firebase references
        databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseConstants.INTERN_PATH);
        storageReference = FirebaseStorage.getInstance().getReference();

        // Set up listeners
        binding.imageViewIntern.setOnClickListener(v -> openImageChooser());
        binding.linearImage.setOnClickListener(v -> openImageChooser());
        binding.buttonSubmit.setOnClickListener(v -> uploadDataToFirebase());

        return view;
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(binding.imageViewIntern);
        }
    }

    private void uploadDataToFirebase() {
        binding.progressBar.setVisibility(View.VISIBLE); // Show ProgressBar

        // Get user input
        String name = binding.editTextName.getText().toString().trim();
        String address = binding.editTextAddress.getText().toString().trim();
        String phoneNumber = binding.editTextPhoneNumber.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        // Validate input fields
        if (name.isEmpty()) {
            Toast.makeText(getActivity(), "Name is required", Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE); // Hide ProgressBar
            return;
        }

        if (address.isEmpty()) {
            Toast.makeText(getActivity(), "Address is required", Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
            return;
        }

        if (phoneNumber.isEmpty() || !isValidPhoneNumber(phoneNumber)) {
            Toast.makeText(getActivity(), "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
            return;
        }

        if (email.isEmpty() || !isValidEmail(email)) {
            Toast.makeText(getActivity(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(getActivity(), "Password is required", Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
            return;
        }

        // Create a new intern model
        InternModel intern = new InternModel();
        intern.setName(name);
        intern.setAddress(address);
        intern.setPhoneNumber(phoneNumber);
        intern.setEmail(email);
        intern.setPassword(password);

        if (imageUri != null) {
            // If an image is selected, upload it
            String imageId = UUID.randomUUID().toString();
            StorageReference fileReference = storageReference.child("images/" + imageId);
            fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Set image URL
                    intern.setImageUrl(uri.toString());
                    uploadInternData(intern); // Upload data with image URL
                });
            }).addOnFailureListener(e -> {
                binding.progressBar.setVisibility(View.GONE); // Hide ProgressBar
                Toast.makeText(getActivity(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            // If no image, upload intern data without image URL
            intern.setImageUrl(null);
            uploadInternData(intern);
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Basic phone number validation (adjust the pattern as needed)
        String phonePattern = "^\\+?[0-9]{10,13}$";  // Validates a phone number with optional + and 10 to 13 digits
        return phoneNumber.matches(phonePattern);
    }

    private boolean isValidEmail(String email) {
        // Basic email validation (adjust the pattern as needed)
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailPattern);
    }



    private void uploadInternData(InternModel intern) {
        String internId = databaseReference.push().getKey(); // Generate unique ID for the intern
        intern.setId(internId); // Set the ID on the intern object
        databaseReference.child(internId).setValue(intern).addOnCompleteListener(task -> {
            binding.progressBar.setVisibility(View.GONE); // Hide ProgressBar
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), "Intern added successfully", Toast.LENGTH_SHORT).show();
                clearInputFields();
            } else {
                Toast.makeText(getActivity(), "Failed to add intern: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void clearInputFields() {
        binding.editTextName.setText("");
        binding.editTextAddress.setText("");
        binding.editTextPhoneNumber.setText("");
        binding.editTextEmail.setText("");
        binding.editTextPassword.setText("");
        binding.imageViewIntern.setImageResource(R.drawable.baseline_cloud_upload_24);
        imageUri = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}
