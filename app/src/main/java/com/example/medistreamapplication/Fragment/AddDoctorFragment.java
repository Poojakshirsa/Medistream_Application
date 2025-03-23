package com.example.medistreamapplication.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medistreamapplication.R;
import com.example.medistreamapplication.databinding.FragmentAddDoctorBinding;
import com.example.medistreamapplication.model_form1.AdminModel;
import com.example.medistreamapplication.Firebase.FirebaseConstants;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddDoctorFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private FragmentAddDoctorBinding binding;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddDoctorBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize Firebase references
        databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseConstants.ADMIN_PATH); // Use the admin path
        storageReference = FirebaseStorage.getInstance().getReference("admin_images");

        // Set image click listener
        binding.linearImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        binding.imageViewAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // Set submit button click listener
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            binding.imageViewAdmin.setImageURI(imageUri);
        }
    }

    private void uploadData() {
        // Get input data
        String name = binding.editTextName.getText().toString().trim();
        String address = binding.editTextAddress.getText().toString().trim();
        String education = binding.editTextEducation.getText().toString().trim();
        String professionalExperience = binding.editTextProfessionalExperience.getText().toString().trim();
        String phoneNumber = binding.editTextPhoneNumber.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(education) ||
                TextUtils.isEmpty(professionalExperience) || TextUtils.isEmpty(phoneNumber) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress bar
        binding.progressBar.setVisibility(View.VISIBLE);

        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get image URL after upload
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    // Save user data to Firebase Database
                                    saveUserData(name, address, education, professionalExperience, phoneNumber, email, password, imageUrl);
                                }
                            }).addOnFailureListener(e -> {
                                Toast.makeText(getActivity(), "Failed to get download URL", Toast.LENGTH_SHORT).show();
                                saveUserData(name, address, education, professionalExperience, phoneNumber, email, password, null);
                            });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Image upload failed", Toast.LENGTH_SHORT).show();
                        saveUserData(name, address, education, professionalExperience, phoneNumber, email, password, null);
                    });
        } else {
            saveUserData(name, address, education, professionalExperience, phoneNumber, email, password, null);
        }
    }

    private void saveUserData(String name, String address, String education, String professionalExperience,
                              String phoneNumber, String email, String password, String imageUrl) {
        String id = databaseReference.push().getKey();

        AdminModel admin = new AdminModel();
        admin.setId(id);
        admin.setName(name);
        admin.setAddress(address);
        admin.setEducation(education);
        admin.setExperience(professionalExperience);
        admin.setPhoneNumber(phoneNumber);
        admin.setEmail(email);
        admin.setPassword(password);
        admin.setImageUrl(imageUrl);

        databaseReference.child(id).setValue(admin)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                            clearFields();
                        } else {
                            Toast.makeText(getActivity(), "Data upload failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void clearFields() {
        binding.editTextName.setText("");
        binding.editTextAddress.setText("");
        binding.editTextEducation.setText("");
        binding.editTextProfessionalExperience.setText("");
        binding.editTextPhoneNumber.setText("");
        binding.editTextEmail.setText("");
        binding.editTextPassword.setText("");
        binding.imageViewAdmin.setImageResource(R.drawable.baseline_cloud_upload_24);

    }
}
