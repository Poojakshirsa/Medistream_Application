package com.example.medistreamapplication.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medistreamapplication.Adapter.PdfAdapter;
import com.example.medistreamapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadPdfFragment extends Fragment {

    private RecyclerView recyclerView;
    private PdfAdapter pdfAdapter;
    private List<File> pdfList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_download_pdf_fragment, container, false);

        recyclerView = view.findViewById(R.id.recycler_downloaded_pdfs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadPdfFiles();
        requireActivity().registerReceiver(pdfReceiver, new IntentFilter("com.example.demo.PDF_SAVED"));

        return view;
    }

    private void loadPdfFiles() {
        pdfList.clear();
        File appFolder = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        if (appFolder != null && appFolder.exists()) {
            File[] files = appFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".pdf")) {
                        pdfList.add(file);
                    }
                }
            }
        }

        pdfAdapter = new PdfAdapter(getContext(), pdfList);
        recyclerView.setAdapter(pdfAdapter);
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
        requireActivity().unregisterReceiver(pdfReceiver);
    }
}

