package com.example.medistreamapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.medistreamapplication.R;

import java.io.File;
import java.io.IOException;

public class PdfViewerActivity extends AppCompatActivity {

    private LinearLayout pagesContainer;
    private ImageButton btnShare;
    private PdfRenderer pdfRenderer;
    private ParcelFileDescriptor fileDescriptor;
    private File pdfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        pagesContainer = findViewById(R.id.pagesContainer);
        btnShare = findViewById(R.id.btnShare);

        // Get the PDF file path from the intent
        String pdfPath = getIntent().getStringExtra("pdfPath");
        if (pdfPath != null) {
            pdfFile = new File(pdfPath);
            openPdf(pdfFile);
        } else {
            Toast.makeText(this, "PDF file not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Share PDF button functionality
        btnShare.setOnClickListener(v -> sharePdf());
    }

    private void openPdf(File file) {
        try {
            fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(fileDescriptor);
            renderAllPages();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error opening PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void renderAllPages() {
        if (pdfRenderer == null) return;

        // Get device width to scale pages appropriately.
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int deviceWidth = metrics.widthPixels;

        // Loop through each page in the PDF
        for (int i = 0; i < pdfRenderer.getPageCount(); i++) {
            PdfRenderer.Page page = pdfRenderer.openPage(i);

            // Calculate scale factor based on device width
            float pageWidth = page.getWidth();
            float scale = (float) deviceWidth / pageWidth;
            int bitmapWidth = (int) (pageWidth * scale);
            int bitmapHeight = (int) (page.getHeight() * scale);

            Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
            // Render the page into the bitmap
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            page.close();

            // Create an ImageView to display the page bitmap
            ImageView pageView = new ImageView(this);
            pageView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            pageView.setAdjustViewBounds(true);
            pageView.setImageBitmap(bitmap);

            // Add the ImageView to the container
            pagesContainer.addView(pageView);
        }
    }

    private void sharePdf() {
        if (pdfFile == null) {
            Toast.makeText(this, "No PDF file to share", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri pdfUri = FileProvider.getUriForFile(this, "com.example.medistreamapplication.fileprovider", pdfFile);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share PDF via"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (pdfRenderer != null) {
                pdfRenderer.close();
            }
            if (fileDescriptor != null) {
                fileDescriptor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
