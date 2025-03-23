package com.example.medistreamapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medistreamapplication.PdfViewerActivity;
import com.example.medistreamapplication.R;

import java.io.File;
import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {

    private Context context;
    private List<File> pdfList;

    public PdfAdapter(Context context, List<File> pdfList) {
        this.context = context;
        this.pdfList = pdfList;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pdf, parent, false);
        return new PdfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        File pdfFile = pdfList.get(position);
        holder.tvPdfName.setText(pdfFile.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PdfViewerActivity.class);
            intent.putExtra("pdfPath", pdfFile.getAbsolutePath());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    public static class PdfViewHolder extends RecyclerView.ViewHolder {
        TextView tvPdfName;

        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPdfName = itemView.findViewById(R.id.tvPdfName);
        }
    }
}
