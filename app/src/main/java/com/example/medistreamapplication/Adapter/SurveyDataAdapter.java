package com.example.medistreamapplication.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medistreamapplication.Firebase.FirebaseConstants;
import com.example.medistreamapplication.FullscreenImageActivity2;
import com.example.medistreamapplication.R;
import com.example.medistreamapplication.model_form1.SurveyData;
import com.example.medistreamapplication.model_form1.DefectData;
import com.example.medistreamapplication.model_form1.DeficiencyData;
import com.example.medistreamapplication.model_form1.DevelopmentalDelaysData;
import com.example.medistreamapplication.model_form1.PreliminaryFinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SurveyDataAdapter extends RecyclerView.Adapter<SurveyDataAdapter.SurveyDataViewHolder> {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private List<SurveyData> surveyDataList;
    private List<SurveyData> surveyDataListFull;
    private Context context;
    private DatabaseReference databaseReference;
    public SurveyDataAdapter(Context context, List<SurveyData> surveyDataList) {
        this.context = context;
        this.surveyDataList = surveyDataList;
        this.surveyDataListFull = new ArrayList<>(surveyDataList);
        this.databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseConstants.FORM_PATH + "ThreeToSixYear");
    }


    @Override
    public SurveyDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_survey_data, parent, false);
        return new SurveyDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SurveyDataViewHolder holder, int position) {
        SurveyData surveyData = surveyDataList.get(position);

        // ✅ Display School & Child Information
        holder.schoolName.setText(Html.fromHtml("<b>School Name:</b> " + surveyData.getSchoolData().getSchoolName()));
        holder.childName.setText(Html.fromHtml("<b>Child Name:</b> " + surveyData.getSchoolData().getChildName()));
        holder.dateOfBirth.setText(Html.fromHtml("<b>Date Of Birth:</b> " + surveyData.getSchoolData().getDateOfBirth()));
        holder.Address.setText(Html.fromHtml("<b>Address:</b> " + surveyData.getSchoolData().getSchoolAddress()));
        holder.DateOfCheckup.setText(Html.fromHtml("<b>Date of Checkup:</b> " + surveyData.getSchoolData().getDateOfCheckup()));
        holder.Age.setText(Html.fromHtml("<b>Age:</b> " + surveyData.getSchoolData().getChildAge()));
        holder.Gender.setText(Html.fromHtml("<b>Gender:</b> " + surveyData.getSchoolData().getGender()));
        holder.InchargeTeacher.setText(Html.fromHtml("<b>Incharge Teacher:</b> " + surveyData.getSchoolData().getTeacherIncharge()));
        holder.FatherName.setText(Html.fromHtml("<b>Father's Name:</b> " + surveyData.getSchoolData().getFatherName()));
        holder.MotherName.setText(Html.fromHtml("<b>Mother's Name:</b> " + surveyData.getSchoolData().getMotherName()));
        holder.ParentNo.setText(Html.fromHtml("<b>Parent's Contact:</b> " + surveyData.getSchoolData().getParentContact()));
        holder.Weight.setText(Html.fromHtml("<b>Weight:</b> " + surveyData.getSchoolData().getChildWeight()));
        holder.Height.setText(Html.fromHtml("<b>Height:</b> " + surveyData.getSchoolData().getChildHeight()));
        holder.Circumference.setText(Html.fromHtml("<b>Head Circumference:</b> " + surveyData.getSchoolData().getHeadCircumference()));
        holder.MUAC.setText(Html.fromHtml("<b>MUAC:</b> " + surveyData.getSchoolData().getMuacValue()));

        // ✅ Fetch & Display Preliminary Findings
        PreliminaryFinding finding = surveyData.getPreliminaryFinding();
        if (finding != null) {
            holder.referredDoctorTextView.setText(Html.fromHtml("<b>Referred Doctor:</b> " +
                    (finding.getReferredDoctor() != null ? finding.getReferredDoctor() : "Not Provided")));
            holder.remarkTextView.setText(Html.fromHtml("<b>Remark:</b> " +
                    (finding.getRemark() != null ? finding.getRemark() : "No Remarks")));

            // ✅ Load Image using Glide (if exists)
            if (finding.getImageUrl() != null && !finding.getImageUrl().isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(finding.getImageUrl())
                        .placeholder(R.drawable.placeholder_image) // Placeholder image
                        .into(holder.imageView);
            } else {
                holder.imageView.setImageResource(R.drawable.no_imagr_available); // Default image if none
            }
        } else {
            holder.referredDoctorTextView.setText("Referred Doctor: Not Available");
            holder.remarkTextView.setText("Remark: Not Available");
            holder.imageView.setImageResource(R.drawable.no_imagr_available);
        }

        // ✅ Expand "Read More" Section
        holder.ReadMore.setOnClickListener(v -> {
            boolean isExpanded = holder.fullInfo.getVisibility() == View.VISIBLE;
            holder.fullInfo.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
            holder.ReadMore.setText(isExpanded ? "Read More" : "Show Less");
        });

        holder.imageView.setOnClickListener(v -> {
            String imageUrl = surveyData.getPreliminaryFinding().getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Intent intent = new Intent(context, FullscreenImageActivity2.class);
                intent.putExtra("image_url", imageUrl);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No image available", Toast.LENGTH_SHORT).show();
            }
        });


        // ✅ Populate Defects, Deficiencies, and Developmental Delays
        if (holder.fullInfo.getVisibility() == View.VISIBLE) {
            holder.defectsTextView.setText("Defects: " + getDefectsSummary(surveyData));
            holder.deficienciesTextView.setText("Deficiencies: " + getDeficienciesSummary(surveyData));
            holder.developmentalDelaysTextView.setText("Developmental Delays: " + getDevelopmentalDelaysSummary(surveyData));
            holder.findingsTextView.setText("Findings: " + getFindingsSummary(surveyData));
        }

        // ✅ Generate PDF Button
        holder.generatepdf.setOnClickListener(v -> {
            if (checkPermission()) {
                createPdf(surveyData);
            } else {
                requestPermission();
            }
        });

        // ✅ Share with Parent via WhatsApp
        holder.sharewithParent.setOnClickListener(view -> {
            String parentNumber = surveyData.getSchoolData().getParentContact();
            File pdfFile = createPdf(surveyData);
            if (pdfFile != null) {
                sendToWhatsApp(parentNumber, pdfFile);
            } else {
                Toast.makeText(view.getContext(), "PDF send Failed", Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ Check Defects, Deficiencies, and Findings Using Reflection
        checkDefectsUsingReflection(surveyData.getDefectsData(), holder);
        checkDeficienciesUsingReflection(surveyData.getDeficienciesData(), holder);
        checkDevelopmentalDelaysUsingReflection(surveyData.getDevelopmentalDelaysData(), holder);
        checkFindingsUsingReflection(surveyData.getPreliminaryFinding(), holder);

        // ✅ Delete Confirmation on Long Click
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        deleteItemFromDatabase(surveyData, position);
                        surveyDataList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, surveyDataList.size());
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        });
    }

    private String getFindingsSummary(SurveyData surveyData) {
        PreliminaryFinding finding = surveyData.getPreliminaryFinding();
        if (finding == null) {
            return "No Findings Available";
        }

        StringBuilder summary = new StringBuilder();

        if (finding.isNeuralTubeDefect()) summary.append("Neural Tube Defect, ");
        if (finding.isDownsSyndrome()) summary.append("Down's Syndrome, ");
        if (finding.isCleftLipAndPalate()) summary.append("Cleft Lip & Palate, ");
        if (finding.isTalipesClubFoot()) summary.append("Talipes Club Foot, ");
        if (finding.isCongenitalCataract()) summary.append("Congenital Cataract, ");
        if (finding.isCongenitalDeafness()) summary.append("Congenital Deafness, ");
        if (finding.isCongenitalHeartDisease()) summary.append("Congenital Heart Disease, ");
        if (finding.isRetinopathyOfPrematurity()) summary.append("Retinopathy of Prematurity, ");
        if (finding.isVisionImpairment()) summary.append("Vision Impairment, ");
        if (finding.isHearingImpairment()) summary.append("Hearing Impairment, ");
        if (finding.isNeuroMotorImpairment()) summary.append("Neuro Motor Impairment, ");
        if (finding.isMotorDelay()) summary.append("Motor Delay, ");
        if (finding.isCognitiveDelay()) summary.append("Cognitive Delay, ");
        if (finding.isSpeechAndLanguageDelay()) summary.append("Speech & Language Delay, ");
        if (finding.isBehaviorDisorder()) summary.append("Behavior Disorder, ");
        if (finding.isLearningDisorder()) summary.append("Learning Disorder, ");
        if (finding.isAdhd()) summary.append("ADHD, ");

        // Remove last comma & space
        if (summary.length() > 2) {
            summary.setLength(summary.length() - 2);
        } else {
            summary.append("No Major Findings");
        }

        return summary.toString();
    }


    // Method to delete item from database
    private void deleteItemFromDatabase(SurveyData surveyData,int position) {
        String surveyDataId = surveyData.getId();
        Log.d("SurveyDataAdapter", "Attempting to delete item with ID: " + surveyDataId);
// Ensure that this ID is correct and unique
        databaseReference.child(surveyDataId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Remove from the local list and notify the adapter
                    if (position >= 0 && position < surveyDataList.size()) {
                        surveyDataList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Intern deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to delete intern: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private void sendToWhatsApp(String phoneNumber, File file) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");

        // Get URI using FileProvider
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, "Here is your child's school report.");

        // Grant permission
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            intent.setPackage("com.whatsapp");
            context.startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            showWhatsAppNotInstalledDialog();
        }
    }


    private void showWhatsAppNotInstalledDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("WhatsApp Not Installed");
        builder.setMessage("WhatsApp is not installed on this device. Please install WhatsApp to share the report.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private String getDefectsSummary(SurveyData surveyData) {
        StringBuilder defects = new StringBuilder();
        if (surveyData.getDefectsData() != null) {
            for (Method method : surveyData.getDefectsData().getClass().getMethods()) {
                if (method.getName().startsWith("is") && method.getReturnType() == boolean.class) {
                    try {
                        if ((boolean) method.invoke(surveyData.getDefectsData())) {
                            defects.append(method.getName().substring(2)).append(", ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return defects.length() > 0 ? defects.substring(0, defects.length() - 2) : "No defects";
    }
    public void checkDefectsUsingReflection(DefectData defectData, SurveyDataViewHolder holder) {
        StringBuilder defectsText = new StringBuilder();

        // Check if defectData is not null before accessing its methods
        if (defectData != null) {
            try {
                // Get all methods in the DefectData class
                Method[] methods = defectData.getClass().getMethods();

                // Iterate through the methods to find methods that start with 'is'
                for (Method method : methods) {
                    // Check if the method name starts with "is" and returns a boolean value
                    if (method.getName().startsWith("is") && method.getReturnType() == boolean.class) {
                        // Invoke the method dynamically
                        boolean result = (boolean) method.invoke(defectData);

                        // If the result is true, append to the defectsText
                        if (result) {
                            String defectName = method.getName().substring(2);  // Remove 'is' from the method name
                            defectsText.append("- "+defectName).append("\n");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Set the defects data to the TextView
            holder.defectsTextView.setText(defectsText.toString());
        } else {
            // Handle the case where defectData is null
            holder.defectsTextView.setText("No defect data available.");
        }
    }
    public void checkDeficienciesUsingReflection(DeficiencyData deficiencyData, SurveyDataViewHolder holder) {
        StringBuilder deficienciesText = new StringBuilder();

        // Check if deficiencyData is not null before accessing its methods
        if (deficiencyData != null) {
            try {
                // Get all methods in the DeficiencyData class
                Method[] methods = deficiencyData.getClass().getMethods();

                // Iterate through the methods to find methods that start with 'is'
                for (Method method : methods) {
                    // Check if the method name starts with "is" and returns a boolean value
                    if (method.getName().startsWith("is") && method.getReturnType() == boolean.class) {
                        // Invoke the method dynamically
                        boolean result = (boolean) method.invoke(deficiencyData);

                        // If the result is true, append to the deficienciesText
                        if (result) {
                            String deficiencyName = method.getName().substring(2);  // Remove 'is' from the method name
                            deficienciesText.append("- " + deficiencyName).append("\n");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Set the deficiency data to the TextView
            holder.deficienciesTextView.setText(deficienciesText.toString());
        } else {
            // Handle the case where deficiencyData is null
            holder.deficienciesTextView.setText("No deficiency data available.");
        }
    }
    public void checkDevelopmentalDelaysUsingReflection(DevelopmentalDelaysData delaysData, SurveyDataViewHolder holder) {
        StringBuilder delaysText = new StringBuilder();

        if (delaysData != null) {
            try {
                // Get all methods in the DevelopmentalDelaysData class
                Method[] methods = delaysData.getClass().getMethods();

                // Iterate through the methods to find methods that start with 'is'
                for (Method method : methods) {
                    // Check if the method name starts with "is" and returns a boolean value
                    if (method.getName().startsWith("is") && method.getReturnType() == boolean.class) {
                        // Invoke the method dynamically
                        boolean result = (boolean) method.invoke(delaysData);

                        // If the result is true, append to the delaysText
                        if (result) {
                            String delayName = method.getName().substring(2);  // Remove 'is' from the method name
                            delaysText.append("- ").append(delayName).append("\n");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Set the developmental delays data to the TextView
            holder.developmentalDelaysTextView.setText(delaysText.toString());
        } else {
            // Handle the case where delaysData is null
            holder.developmentalDelaysTextView.setText("No developmental delays data available.");
        }
    }
    public void checkFindingsUsingReflection(PreliminaryFinding preliminaryFinding, SurveyDataViewHolder holder) {
        StringBuilder findingsText = new StringBuilder();

        // Check if preliminaryFinding is not null before accessing its methods
        if (preliminaryFinding != null) {
            try {
                // Get all methods in the PreliminaryFinding class
                Method[] methods = preliminaryFinding.getClass().getMethods();

                // Iterate through the methods to find methods that start with 'is'
                for (Method method : methods) {
                    // Check if the method name starts with "is" and returns a boolean value
                    if (method.getName().startsWith("is") && method.getReturnType() == boolean.class) {
                        // Invoke the method dynamically
                        boolean result = (boolean) method.invoke(preliminaryFinding);

                        // If the result is true, append to the findingsText
                        if (result) {
                            String findingName = method.getName().substring(2);  // Remove 'is' from the method name
                            findingsText.append("- ").append(findingName).append("\n");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Set the findings data to the TextView
            holder.findingsTextView.setText(findingsText.toString());
        } else {
            // Handle the case when preliminaryFinding is null
            holder.findingsTextView.setText("No findings available.");
        }
    }
    public void filterList(List<SurveyData> filteredList) {
        surveyDataList = filteredList;
        notifyDataSetChanged();
    }
    private boolean checkPermission() {
        int permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }
    private File createPdf(SurveyData surveyData) {
        try {
            // Save in Downloads
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,   ".pdf");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri pdfUri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);

            if (pdfUri != null) {
                try (OutputStream outputStream = context.getContentResolver().openOutputStream(pdfUri)) {
                    generatePdf(outputStream, surveyData); // Pass OutputStream
                }
            }
            String fileName = "ThreeToSix" + System.currentTimeMillis() + ".pdf";
            File appFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
            // Save in App Folder

            try (OutputStream appOutput = new FileOutputStream(appFolder)) {
                generatePdf(appOutput, surveyData); // Pass OutputStream
            }

//            // Notify Fragment to Refresh List
//            context.sendBroadcast(new Intent("com.example.demo.PDF_SAVED"));
            Intent intent = new Intent("com.example.demo.PDF_SAVED");
            context.sendBroadcast(intent);

            Toast.makeText(context.getApplicationContext(), "PDF Created!", Toast.LENGTH_SHORT).show();
            return appFolder;
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void generatePdf(OutputStream outputStream, SurveyData surveyData) throws IOException {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Title with bold & centered
        Paragraph title = new Paragraph("Survey Data Report")
                .setBold()
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(0, 102, 204)); // Blue color
        document.add(title);

        // Add space
        document.add(new Paragraph("\n"));

        // Table for survey data
        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 7})).useAllAvailableWidth();
        table.setBorder(new SolidBorder(1));

        addRowToTable(table, "School Name:", surveyData.getSchoolData().getSchoolName());
        addRowToTable(table, "Child Name:", surveyData.getSchoolData().getChildName());
        addRowToTable(table, "Date Of Birth:", surveyData.getSchoolData().getDateOfBirth());
        addRowToTable(table, "Address:", surveyData.getSchoolData().getSchoolAddress());
        addRowToTable(table, "Date of Checkup:", surveyData.getSchoolData().getDateOfCheckup());
        addRowToTable(table, "Age:", surveyData.getSchoolData().getChildAge() + " years");
        addRowToTable(table, "Gender:", surveyData.getSchoolData().getGender());
        addRowToTable(table, "Incharge Teacher:", surveyData.getSchoolData().getTeacherIncharge());
        addRowToTable(table, "Father's Name:", surveyData.getSchoolData().getFatherName());
        addRowToTable(table, "Mother's Name:", surveyData.getSchoolData().getMotherName());
        addRowToTable(table, "Parent's Contact:", surveyData.getSchoolData().getParentContact());
        addRowToTable(table, "Weight:", surveyData.getSchoolData().getChildWeight() + " kg");
        addRowToTable(table, "Height:", surveyData.getSchoolData().getChildHeight() + " cm");
        addRowToTable(table, "Head Circumference:", surveyData.getSchoolData().getHeadCircumference() + " cm");
        addRowToTable(table, "MUAC:", surveyData.getSchoolData().getMuacValue() + " cm");

        document.add(table);

        // Add spacing
        document.add(new Paragraph("\n"));

        // Add Defects Section
        document.add(new Paragraph("Defects:").setBold().setFontSize(14));
        document.add(new Paragraph(getDefectsSummary(surveyData)));

        // Add Deficiencies Section
        document.add(new Paragraph("Deficiencies:").setBold().setFontSize(14));
        document.add(new Paragraph(getDeficienciesSummary(surveyData)));

        // Add Developmental Delays Section
        document.add(new Paragraph("Developmental Delays:").setBold().setFontSize(14));
        document.add(new Paragraph(getDevelopmentalDelaysSummary(surveyData)));

        // Add Preliminary Findings Section
        document.add(new Paragraph("Preliminary Findings:").setBold().setFontSize(14));
        document.add(new Paragraph(getPreliminaryFindingsSummary(surveyData)));

        // Close Document
        document.close();
    }

    private void addRowToTable(Table table, String key, String value) {
        table.addCell(new Cell().add(new Paragraph(key).setBold().setFontSize(12)).setBackgroundColor(new DeviceRgb(230, 230, 230))); // Light gray background
        table.addCell(new Cell().add(new Paragraph(value)).setFontSize(12));
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    private String getDeficienciesSummary(SurveyData surveyData) {
        StringBuilder deficiencies = new StringBuilder();
        if (surveyData.getDeficienciesData() != null) {
            for (Method method : surveyData.getDeficienciesData().getClass().getMethods()) {
                if (method.getName().startsWith("is") && method.getReturnType() == boolean.class) {
                    try {
                        if ((boolean) method.invoke(surveyData.getDeficienciesData())) {
                            deficiencies.append(method.getName().substring(2)).append(", ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return deficiencies.length() > 0 ? deficiencies.substring(0, deficiencies.length() - 2) : "No deficiencies";
    }

    private String getDevelopmentalDelaysSummary(SurveyData surveyData) {
        StringBuilder delays = new StringBuilder();
        if (surveyData.getDevelopmentalDelaysData() != null) {
            for (Method method : surveyData.getDevelopmentalDelaysData().getClass().getMethods()) {
                if (method.getName().startsWith("is") && method.getReturnType() == boolean.class) {
                    try {
                        if ((boolean) method.invoke(surveyData.getDevelopmentalDelaysData())) {
                            delays.append(method.getName().substring(2)).append(", ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return delays.length() > 0 ? delays.substring(0, delays.length() - 2) : "No developmental delays";
    }

    private String getPreliminaryFindingsSummary(SurveyData surveyData) {
        StringBuilder findings = new StringBuilder();
        if (surveyData.getPreliminaryFinding() != null) {
            for (Method method : surveyData.getPreliminaryFinding().getClass().getMethods()) {
                if (method.getName().startsWith("is") && method.getReturnType() == boolean.class) {
                    try {
                        if ((boolean) method.invoke(surveyData.getPreliminaryFinding())) {
                            findings.append(method.getName().substring(2)).append(", ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return findings.length() > 0 ? findings.substring(0, findings.length() - 2) : "No preliminary findings";
    }

    @Override
    public int getItemCount() {
        return surveyDataList.size();
    }

    public static class SurveyDataViewHolder extends RecyclerView.ViewHolder {

        TextView schoolName, dateOfBirth, childName, defectsTextView, Address, DateOfCheckup, Age, Gender,
                InchargeTeacher, FatherName, MotherName, ParentNo, Weight, Height, Circumference, MUAC,
                deficienciesTextView, developmentalDelaysTextView, findingsTextView, ReadMore,
                referredDoctorTextView, remarkTextView;
        LinearLayout fullInfo;
        Button generatepdf, sharewithParent;
        ImageView imageView;  // ✅ Added ImageView for uploaded image

        public SurveyDataViewHolder(View itemView) {
            super(itemView);

            // ✅ School & Basic Info
            schoolName = itemView.findViewById(R.id.schoolName);
            childName = itemView.findViewById(R.id.childName);
            dateOfBirth = itemView.findViewById(R.id.dateOfBirth);
            defectsTextView = itemView.findViewById(R.id.defectsTextView);
            Address = itemView.findViewById(R.id.schoolAddress);
            DateOfCheckup = itemView.findViewById(R.id.Date_of_Checkup);
            Age = itemView.findViewById(R.id.Age);
            Gender = itemView.findViewById(R.id.Gender);
            InchargeTeacher = itemView.findViewById(R.id.teacher_incharge);
            FatherName = itemView.findViewById(R.id.Father_Name);
            MotherName = itemView.findViewById(R.id.Mother_Name);
            ParentNo = itemView.findViewById(R.id.ContactNo);
            Weight = itemView.findViewById(R.id.Weight);
            Height = itemView.findViewById(R.id.height);
            Circumference = itemView.findViewById(R.id.circumference);
            MUAC = itemView.findViewById(R.id.MUAC);

            // ✅ Preliminary Findings Section
            referredDoctorTextView = itemView.findViewById(R.id.referredDoctorTextView);  // ✅ Added
            remarkTextView = itemView.findViewById(R.id.remarkTextView);  // ✅ Added
            imageView = itemView.findViewById(R.id.imageView);  // ✅ Added

            // ✅ Screening Sections
            deficienciesTextView = itemView.findViewById(R.id.deficienciesTextView);
            developmentalDelaysTextView = itemView.findViewById(R.id.developmentalDelaysTextView);
            findingsTextView = itemView.findViewById(R.id.findingsTextView);

            // ✅ Read More / Expandable Section
            fullInfo = itemView.findViewById(R.id.fullInfo);
            ReadMore = itemView.findViewById(R.id.ReadMore);

            // ✅ Action Buttons
            generatepdf = itemView.findViewById(R.id.btnGeneratePDF);
            sharewithParent = itemView.findViewById(R.id.btnShareWithParent);
        }
    }

}
