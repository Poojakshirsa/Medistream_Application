package com.example.medistreamapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.medistreamapplication.R;
import com.example.medistreamapplication.model_form1.SchoolData;
import java.util.List;

public class SchoolDataAdapter extends RecyclerView.Adapter<SchoolDataAdapter.SchoolDataViewHolder> {

    private List<SchoolData> schoolDataList;

    public SchoolDataAdapter(List<SchoolData> schoolDataList) {
        this.schoolDataList = schoolDataList;
    }

    @Override
    public SchoolDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.school_data_item, parent, false);
        return new SchoolDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SchoolDataViewHolder holder, int position) {
        SchoolData schoolData = schoolDataList.get(position);
        holder.schoolNameTextView.setText(schoolData.getSchoolName());
        holder.schoolAddressTextView.setText(schoolData.getSchoolAddress());
        holder.childNameTextView.setText(schoolData.getChildName());
        holder.childAgeTextView.setText(schoolData.getChildAge());
        // Set other fields in a similar manner
    }

    @Override
    public int getItemCount() {
        return schoolDataList.size();
    }

    public static class SchoolDataViewHolder extends RecyclerView.ViewHolder {
        TextView schoolNameTextView, schoolAddressTextView, childNameTextView, childAgeTextView;

        public SchoolDataViewHolder(View itemView) {
            super(itemView);
            schoolNameTextView = itemView.findViewById(R.id.schoolNameTextView);
            schoolAddressTextView = itemView.findViewById(R.id.schoolAddressTextView);
            childNameTextView = itemView.findViewById(R.id.childNameTextView);
            childAgeTextView = itemView.findViewById(R.id.childAgeTextView);
            // Initialize other views here
        }
    }
}
