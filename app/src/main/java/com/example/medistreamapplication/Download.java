package com.example.medistreamapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.example.medistreamapplication.Adapter.PdfAdapter;
import com.example.medistreamapplication.Adapter.SurveyDataAdapter;
import com.example.medistreamapplication.model_form1.SurveyData;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Download extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PdfAdapter pdfAdapter;
    private List<File> pdfList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        recyclerView = findViewById(R.id.recycler_downloaded_pdfs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadPdfFiles();
        registerReceiver(pdfReceiver, new IntentFilter("com.example.demo.PDF_SAVED"));


    }

    private void loadPdfFiles() {
        pdfList.clear(); // Clear previous list to avoid duplicates
        File appFolder = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        if (appFolder != null && appFolder.exists()) {
            File[] files = appFolder.listFiles();
            if (files != null) {
                Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed()); // Sort by latest
                for (File file : files) {
                    if (file.getName().endsWith(".pdf")) {
                        pdfList.add(file);
                    }
                }
            }
        }

        if (pdfAdapter == null) {
            pdfAdapter = new PdfAdapter(this, pdfList);
            recyclerView.setAdapter(pdfAdapter);
        } else {
            pdfAdapter.notifyDataSetChanged();
        }
    }

    private final BroadcastReceiver pdfReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadPdfFiles();
            Toast.makeText(context, "New PDF added!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
      unregisterReceiver(pdfReceiver);
    }
}

