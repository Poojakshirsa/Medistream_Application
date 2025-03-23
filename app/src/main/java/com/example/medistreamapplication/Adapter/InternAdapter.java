package com.example.medistreamapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medistreamapplication.Firebase.FirebaseConstants;
import com.example.medistreamapplication.Details.InternDetailActivity;
import com.example.medistreamapplication.model_form1.InternModel;
import com.example.medistreamapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class InternAdapter extends RecyclerView.Adapter<InternAdapter.InternViewHolder> {

    private List<InternModel> internList;
    private Context context;
    private DatabaseReference databaseReference;

    public InternAdapter(Context context, List<InternModel> internList) {
        this.internList = internList;
        this.context = context;
        // Initialize the Firebase database reference
        this.databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseConstants.INTERN_PATH );
    }

    @NonNull
    @Override
    public InternViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.intern_item, parent, false);
        return new InternViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InternViewHolder holder, int position) {
        InternModel intern = internList.get(position);
        holder.textViewName.setText(intern.getName());
        holder.textViewPhone.setText(intern.getPhoneNumber());

        Glide.with(holder.itemView.getContext())
                .load(intern.getImageUrl())
                .placeholder(R.drawable.user) // Replace with your placeholder image
                .error(R.drawable.user) // Replace with your error image
                .into(holder.imageViewIntern);


        // Set a long click listener for item deletion
        holder.itemView.setOnLongClickListener(v -> {
            showDeleteConfirmationDialog(intern, position);
            return true;
        });
        holder.itemView.setOnClickListener(v -> {
            // Save intern details in SharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("InternDetails", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("intern_name", intern.getName());
            editor.putString("intern_phone", intern.getPhoneNumber());
            editor.putString("intern_image_url", intern.getImageUrl());
            editor.putString("intern_address", intern.getAddress()); // Save address
            editor.putString("intern_email", intern.getEmail());
          editor.putString("intern_id", intern.getId());
            editor.apply();

            // Start the activity
            Intent intent = new Intent(context, InternDetailActivity.class);
            context.startActivity(intent);
        });

    }
    public void filterList(List<InternModel> filteredList) {
        internList = filteredList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return internList.size();
    }

    private void showDeleteConfirmationDialog(InternModel intern, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Confirmation");
        builder.setMessage("Are you sure you want to delete this intern?");
        builder.setPositiveButton("Yes", (dialog, which) -> deleteIntern(intern, position));
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void deleteIntern(InternModel intern, int position) {
        String internId = intern.getId(); // Get the ID of the intern
        Log.d("InternAdapter", "Deleting intern with ID: " + internId); // Log the ID

        // Check if ID is null or empty before attempting deletion
        if (internId == null || internId.isEmpty()) {
            Log.e("InternAdapter", "Invalid intern ID: " + internId);
            Toast.makeText(context, "Error: Invalid intern ID", Toast.LENGTH_SHORT).show();
            return; // Exit the method early
        }

        // Remove from Firebase
        databaseReference.child(internId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Remove from the local list and notify the adapter
                    if (position >= 0 && position < internList.size()) {
                        internList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Intern deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to delete intern: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    static class InternViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPhone;
        ImageView imageViewIntern; // Add ImageView for the intern's image

        public InternViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            imageViewIntern = itemView.findViewById(R.id.imageViewIntern);
            textViewPhone = itemView.findViewById(R.id.textViewPhoneNumber); // Initialize ImageView
        }
    }
}
