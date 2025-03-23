package com.example.medistreamapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medistreamapplication.Details.DoctorDetailActivity;
import com.example.medistreamapplication.Firebase.FirebaseConstants;
import com.example.medistreamapplication.R;
import com.example.medistreamapplication.model_form1.AdminModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> {

    private List<AdminModel> adminList;
    private Context context;
    private DatabaseReference databaseReference;

    public AdminAdapter(List<AdminModel> adminList, Context context) {
        this.adminList = adminList;
        this.context = context;
        this.databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseConstants.ADMIN_PATH);
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        AdminModel admin = adminList.get(position);
        holder.textViewName.setText(admin.getName());

        holder.textViewPhoneNumber.setText(admin.getPhoneNumber());

        // Load image using Glide
        if (admin.getImageUrl() != null && !admin.getImageUrl().isEmpty()) {
            Glide.with(holder.imageViewAdmin.getContext())
                    .load(admin.getImageUrl())
                    .placeholder(R.drawable.baseline_person_24) // Optional placeholder
                    .into(holder.imageViewAdmin);
        }

        // Set long click listener to show delete confirmation dialog
        holder.itemView.setOnLongClickListener(v -> {
            showDeleteConfirmationDialog(admin, position);
            return true;
        });

        // Set click listener to save admin details in SharedPreferences
        holder.itemView.setOnClickListener(v -> {
            // Save admin details in SharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("AdminDetails", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("admin_name", admin.getName());
            editor.putString("admin_phone_number", admin.getPhoneNumber());
            editor.putString("admin_address", admin.getAddress());
            editor.putString("admin_education", admin.getEducation());
            editor.putString("admin_email", admin.getEmail());
            editor.putString("admin_image_url", admin.getImageUrl()); // Save image URL
            editor.putString("Experience", admin.getExperience());
            editor.apply();
            Log.d("AdminAdapter", "Saving professional experience: " + admin.getExperience());

            // Start DoctorDetailActivity
            Intent intent = new Intent(context, DoctorDetailActivity.class);
            context.startActivity(intent);
        });

    }
    public void filterList(List<AdminModel> filteredList) {
        adminList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return adminList.size();
    }

    private void showDeleteConfirmationDialog(AdminModel admin, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this admin?")
                .setPositiveButton("Delete", (dialog, which) -> deleteItemFromFirebase(admin, position))
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteItemFromFirebase(AdminModel admin, int position) {
        // Assuming admin has a unique identifier (e.g., address or name)
        String adminId = admin.getId(); // You should have an ID or unique field in AdminModel

        // Deleting the admin from Firebase
        databaseReference.child(adminId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    if (position >= 0 && position < adminList.size()) {
                        adminList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Admin deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to delete admin: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewAddress, textViewPhoneNumber;
        ImageView imageViewAdmin;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);

            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            imageViewAdmin = itemView.findViewById(R.id.imageViewAdmin);
        }
    }
}
