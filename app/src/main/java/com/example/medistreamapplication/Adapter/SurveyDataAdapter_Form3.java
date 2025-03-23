package com.example.medistreamapplication.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medistreamapplication.Firebase.FirebaseConstants;
import com.example.medistreamapplication.FullscreenImageActivity2;
import com.example.medistreamapplication.R;

import com.example.medistreamapplication.model_teacherForm.SurveyDataFormThree;
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

public class SurveyDataAdapter_Form3 extends RecyclerView.Adapter<SurveyDataAdapter_Form3.SurveyDataViewHolder> {
    private static final int STORAGE_PERMISSION_CODE = 1;
    private List<SurveyDataFormThree> surveyDataList;
    private List<SurveyDataFormThree> surveyDataListFull;
    private Context context;
    private DatabaseReference databaseReference;
    public SurveyDataAdapter_Form3(Context context, List<SurveyDataFormThree> surveyDataList) {

        this.context = context;
        this.surveyDataList = surveyDataList;
        this.surveyDataListFull = new ArrayList<>(surveyDataList);
        this.databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseConstants.FORM_PATH + "TeacherForm");
    }


    public
    SurveyDataAdapter_Form3.SurveyDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_survey_data_three, parent, false);
        return new SurveyDataAdapter_Form3.SurveyDataViewHolder(view);
    }


    public void onBindViewHolder(SurveyDataAdapter_Form3.SurveyDataViewHolder holder, int position) {
        SurveyDataFormThree surveyData = surveyDataList.get(position);
        if (surveyData.getDemographicFormModel() != null) {
            // Display school name and child name with labels
            holder.name.setText(Html.fromHtml("<b>Name: </b> " + surveyData.getDemographicFormModel().getEmployeeName()));
            holder.dateOfBirth.setText(Html.fromHtml("<b>Date Of Birth: </b> " + surveyData.getDemographicFormModel().getDateOfBirth()));
            holder.age.setText(Html.fromHtml("<b>Age: </b> " + surveyData.getDemographicFormModel().getAge()));
            holder.gender.setText(Html.fromHtml("<b>Gender: </b> " + surveyData.getDemographicFormModel().getGender()));
            holder.maritalStatus.setText(Html.fromHtml("<b>Marital Status: </b> " + surveyData.getDemographicFormModel().getMaritalStatus()));
            holder.schoolName.setText(Html.fromHtml("<b>School Name: </b> " + String.valueOf(surveyData.getDemographicFormModel().getSchoolName())));
            holder.address.setText(Html.fromHtml("<b>Address: </b> " + surveyData.getDemographicFormModel().getSchoolAddress()));
            holder.designation.setText(Html.fromHtml("<b>Designation: </b> " + surveyData.getDemographicFormModel().getDesignation()));
            holder.education.setText(Html.fromHtml("<b>Eduction: </b> " + surveyData.getDemographicFormModel().getEducation()));
            holder.experience.setText(Html.fromHtml("<b>Experience: </b> " + surveyData.getDemographicFormModel().getWorkingExperience()));
            holder.timing.setText(Html.fromHtml("<b>Timing:</b> " + surveyData.getDemographicFormModel().getSchoolTiming()));
            holder.income.setText(Html.fromHtml("<b>Mother's Name:</b> " + surveyData.getDemographicFormModel().getMonthlyIncome()));
        }
        holder.btnGeneratePDF.setOnClickListener(v -> {
            if (checkPermission()) {
                createPdf(surveyData);
            } else {
                requestPermission();
            }
        });
        holder.readMore.setOnClickListener(v -> {
            boolean isExpanded = holder.fullInfo.getVisibility() == View.VISIBLE;

            // Toggle visibiclity
            holder.fullInfo.setVisibility(isExpanded ? View.GONE : View.VISIBLE);

            // Update "Read More" text
            holder.readMore.setText(isExpanded ? "Read More" : "Show Less");
        });

        holder.examinationImageView.setOnClickListener(v -> {
            String imageUrl = surveyData.getExaminationModel().getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Intent intent = new Intent(context, FullscreenImageActivity2.class);
                intent.putExtra("image_url", imageUrl);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No image available", Toast.LENGTH_SHORT).show();
            }
        });


        if (surveyData.getAddictionModel() != null) {
            String addictionSummary = getAddictionSummary(surveyData);
            holder.addictionTextView.setText(addictionSummary);
        } else {
            holder.addictionTextView.setText("No addiction data available.");
        }

        if (surveyData.getHistoryExaminationModel() != null) {
            String defectsSummary = getDefectsSummary(surveyData);
            holder.historyTextView.setText(defectsSummary);
        }

        if (surveyData.getDietaryHistoryModel() != null) {
            String dietarySummary = getDietarySummary(surveyData);
            holder.dietaryTextView.setText(dietarySummary);
        } else {
            holder.dietaryTextView.setText("Dietary history not available.");
        }

        if (surveyData.getPhysicalActivityModel() != null) {
            String physicalSummary = getPhysicalSummary(surveyData);
            holder.physicalTextView.setText(physicalSummary);
        } else {
            holder.physicalTextView.setText("No physical activity history available.");
        }

        if (surveyData.getExaminationModel() != null) {
            String examSummary = getExaminationSummary(surveyData);
            holder.examinationTextView.setText(examSummary); // ✅ Always show text

            // ✅ Load Image
            String imageUrl = surveyData.getExaminationModel().getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                holder.examinationImageView.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(imageUrl)
                        .override(500, 500)  // ✅ Resize image to avoid crashes
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error_image)
                        .into(holder.examinationImageView);
            } else {
                holder.examinationImageView.setVisibility(View.GONE);
            }
        } else {
            holder.examinationTextView.setText("No examination data available.");
            holder.examinationImageView.setVisibility(View.GONE);
        }

        if (surveyData.getExaminationModel() != null) {
            String examSummary = getExaminationSummary(surveyData);
            holder.examinationTextView.setText(examSummary);
        } else {
            holder.examinationTextView.setText("No examination data available.");
        }

        holder.itemView.setOnLongClickListener(v -> {
            new android.app.AlertDialog.Builder(v.getContext())
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Remove the item from the database
                        deleteItemFromDatabase(surveyData,position);

                        // Remove the item from the list and notify the adapter
                        surveyDataList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, surveyDataList.size());
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();

            return true; // Indicate that the long click was handled
        });
    }
    private boolean checkPermission() {
        int permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;}
    private void requestPermission() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    private void createPdf(SurveyDataFormThree surveyData) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "TeacherForm." +
                    "" + System.currentTimeMillis() + ".pdf");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri pdfUri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);

            if (pdfUri != null) {
                try (OutputStream outputStream = (OutputStream) context.getContentResolver().openOutputStream(pdfUri)) {
                    generatePdf(outputStream, surveyData);
                }
            }

            String fileName = "SixToEighteen" + System.currentTimeMillis() + ".pdf";
            File appFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
            // Save in App Folder

            try (java.io.OutputStream appOutput = new FileOutputStream(appFolder)) {
                generatePdf((OutputStream) appOutput, surveyData); // Pass OutputStream
            }

            Intent intent = new Intent("com.example.demo.PDF_SAVED");
            context.sendBroadcast(intent);
            Toast.makeText(context.getApplicationContext(), "PDF Created!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generatePdf(OutputStream outputStream, SurveyDataFormThree surveyData) throws IOException {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        Paragraph title = new Paragraph("Survey Data Report")
                .setBold()
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(0, 102, 204));
        document.add(title);
        document.add(new Paragraph("\n"));

        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 7})).useAllAvailableWidth();
        table.setBorder(new SolidBorder(1));

        // Using demographic form data
        if (surveyData.getDemographicFormModel() != null) {
            addRowToTable(table, "Name:", surveyData.getDemographicFormModel().getEmployeeName());
            addRowToTable(table, "DateOfBirth:", surveyData.getDemographicFormModel().getDateOfBirth());
            addRowToTable(table, "Age:", surveyData.getDemographicFormModel().getAge());
            addRowToTable(table, "Gender:", surveyData.getDemographicFormModel().getGender());
            addRowToTable(table, "Marital Status:", surveyData.getDemographicFormModel().getMaritalStatus());
            addRowToTable(table, "School Name:", String.valueOf(surveyData.getDemographicFormModel().getSchoolName()));
            addRowToTable(table, "Address:", surveyData.getDemographicFormModel().getSchoolAddress());
            addRowToTable(table, "Designation:", surveyData.getDemographicFormModel().getDesignation());
            addRowToTable(table, "Education:", surveyData.getDemographicFormModel().getEducation());
            addRowToTable(table, "Experience:", surveyData.getDemographicFormModel().getWorkingExperience());
            addRowToTable(table, "Timing:", surveyData.getDemographicFormModel().getSchoolTiming());
            addRowToTable(table, "Mother's Name:", surveyData.getDemographicFormModel().getMonthlyIncome());
        } else {
            table.addCell(new Cell(1, 2)
                    .add(new Paragraph("Demographic data not available."))
                    .setTextAlignment(TextAlignment.CENTER));
        }
        document.add(table);
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("History and Examination:").setBold().setFontSize(14));
        document.add(new Paragraph(getDefectsSummary(surveyData)));

        document.add(new Paragraph("Dietry History:").setBold().setFontSize(14));
        document.add(new Paragraph(getDietarySummary(surveyData)));

        document.add(new Paragraph(" Physical Activity:").setBold().setFontSize(14));
        document.add(new Paragraph(getPhysicalSummary(surveyData)));

        document.add(new Paragraph(" Addiction:").setBold().setFontSize(14));
        document.add(new Paragraph(getAddictionSummary(surveyData)));

        document.add(new Paragraph("Examination:").setBold().setFontSize(14));
        document.add(new Paragraph(getExaminationSummary(surveyData)));

        document.close();
    }

    private void addRowToTable(Table table, String key, String value) {
        table.addCell(new Cell().add(new Paragraph(key).setBold().setFontSize(12)).setBackgroundColor(new DeviceRgb(230, 230, 230)));
        table.addCell(new Cell().add(new Paragraph(value)).setFontSize(12));
    }

    private String getAddictionSummary(SurveyDataFormThree surveyData) {
        StringBuilder summary = new StringBuilder();
        if (surveyData.getAddictionModel() != null) {
            Object addiction = surveyData.getAddictionModel();
            Method[] methods = addiction.getClass().getDeclaredMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                method.setAccessible(true);
                try {
                    // Process boolean getters starting with "is"
                    if (methodName.startsWith("is") && method.getReturnType() == boolean.class) {
                        boolean value = (boolean) method.invoke(addiction);
                        summary.append(methodName.substring(2))  // Remove "is"
                                .append(": ")
                                .append(value ? "Yes" : "No")
                                .append("\n");
                    }
                    // Process text/number getters starting with "get" (skip getClass)
                    else if (methodName.startsWith("get") && !methodName.equals("getClass")) {
                        Object value = method.invoke(addiction);
                        if (value != null) {
                            String strVal = value.toString().trim();
                            // Skip if the value equals "Not specified" (ignoring case) or is empty
                            if (!strVal.equalsIgnoreCase("Not specified") && !strVal.isEmpty()) {
                                summary.append(methodName.substring(3))  // Remove "get"
                                        .append(": ")
                                        .append(strVal)
                                        .append("\n");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return summary.length() > 0 ? summary.toString() : "No addiction data available.";
    }
    private String getExaminationSummary(SurveyDataFormThree surveyData) {
        StringBuilder summary = new StringBuilder();
        if (surveyData.getExaminationModel() != null) {
            Object exam = surveyData.getExaminationModel();
            Method[] methods = exam.getClass().getDeclaredMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                method.setAccessible(true);
                try {
                    // ✅ Skip getImageUrl() to prevent showing the image URL
                    if (methodName.equals("getImageUrl")) {
                        continue;
                    }

                    // ✅ Process boolean getters (e.g., "isSmoker")
                    if (methodName.startsWith("is") && method.getReturnType() == boolean.class) {
                        boolean value = (boolean) method.invoke(exam);
                        summary.append(methodName.substring(2)) // Remove "is"
                                .append(": ")
                                .append(value ? "Yes" : "No")
                                .append("\n");
                    }
                    // ✅ Process text/number getters (skip getClass)
                    else if (methodName.startsWith("get") && !methodName.equals("getClass")) {
                        Object value = method.invoke(exam);
                        if (value != null) {
                            String strVal = value.toString().trim();
                            // ✅ Skip "Not selected" and empty values
                            if (!strVal.equalsIgnoreCase("Not selected") && !strVal.isEmpty()) {
                                summary.append(methodName.substring(3)) // Remove "get"
                                        .append(": ")
                                        .append(strVal)
                                        .append("\n");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return summary.length() > 0 ? summary.toString() : "No examination data available.";
    }


    // Method to delete item from database
    private void deleteItemFromDatabase(SurveyDataFormThree surveyData, int position) {
        String surveyDataId = surveyData.getId();
        Log.d("SurveyDataAdapter_Form2", "Attempting to delete item with ID: " + surveyDataId);
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

    private String getDefectsSummary(SurveyDataFormThree surveyData) {
        StringBuilder summary = new StringBuilder();
        // Use the history examination model for teacher form history data:
        if (surveyData.getHistoryExaminationModel() != null) {
            Object history = surveyData.getHistoryExaminationModel();
            Method[] methods = history.getClass().getDeclaredMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                method.setAccessible(true);
                try {
                    // Process boolean getters starting with "is"
                    if (methodName.startsWith("is") && method.getReturnType() == boolean.class) {
                        boolean value = (boolean) method.invoke(history);
                        summary.append(methodName.substring(2))  // Remove "is"
                                .append(": ")
                                .append(value ? "Yes" : "No")
                                .append("\n");
                    }
                    // Process text/number getters starting with "get" (skip getClass)
                    else if (methodName.startsWith("get") && !methodName.equals("getClass")) {
                        Object value = method.invoke(history);
                        if (value != null && !value.toString().trim().isEmpty()) {
                            summary.append(methodName.substring(3))  // Remove "get"
                                    .append(": ")
                                    .append(value.toString())
                                    .append("\n");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return summary.length() > 0 ? summary.toString() : "No history data available.";
    }
    private String getPhysicalSummary(SurveyDataFormThree surveyData) {
        StringBuilder summary = new StringBuilder();
        if (surveyData.getPhysicalActivityModel() != null) {
            Object physical = surveyData.getPhysicalActivityModel();
            Method[] methods = physical.getClass().getDeclaredMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                method.setAccessible(true);
                try {
                    // Process boolean getters starting with "is"
                    if (methodName.startsWith("is") && method.getReturnType() == boolean.class) {
                        boolean value = (boolean) method.invoke(physical);
                        summary.append(methodName.substring(2))  // Remove "is"
                                .append(": ")
                                .append(value ? "Yes" : "No")
                                .append("\n");
                    }
                    // Process text/number getters starting with "get" (skip getClass)
                    else if (methodName.startsWith("get") && !methodName.equals("getClass")) {
                        Object value = method.invoke(physical);
                        if (value != null) {
                            String strVal = value.toString().trim();
                            // Skip if the value equals "Not selected" (ignoring case) or is empty
                            if (!strVal.equalsIgnoreCase("Not selected") && !strVal.isEmpty()) {
                                summary.append(methodName.substring(3))  // Remove "get"
                                        .append(": ")
                                        .append(strVal)
                                        .append("\n");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return summary.length() > 0 ? summary.toString() : "No physical activity history available.";
    }

    private String getDietarySummary(SurveyDataFormThree surveyData) {
        StringBuilder summary = new StringBuilder();
        if (surveyData.getDietaryHistoryModel() != null) {
            Object history = surveyData.getDietaryHistoryModel();
            Method[] methods = history.getClass().getDeclaredMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                method.setAccessible(true);
                try {
                    // Process boolean getters starting with "is"
                    if (methodName.startsWith("is") && method.getReturnType() == boolean.class) {
                        boolean value = (boolean) method.invoke(history);
                        summary.append(methodName.substring(2))  // Remove "is"
                                .append(": ")
                                .append(value ? "Yes" : "No")
                                .append("\n");
                    }
                    // Process text/number getters starting with "get" (skip getClass)
                    else if (methodName.startsWith("get") && !methodName.equals("getClass")) {
                        Object value = method.invoke(history);
                        if (value != null) {
                            String strVal = value.toString().trim();
                            // Skip if the value equals "Not selected" (ignoring case)
                            if (!strVal.equalsIgnoreCase("Not selected") && !strVal.isEmpty()) {
                                summary.append(methodName.substring(3))  // Remove "get"
                                        .append(": ")
                                        .append(strVal)
                                        .append("\n");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return summary.length() > 0 ? summary.toString() : "No dietary history available.";
    }
    public void filterList(List<SurveyDataFormThree> filteredList) {
        surveyDataList = filteredList;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return surveyDataList.size();
    }

    public static class SurveyDataViewHolder extends RecyclerView.ViewHolder {

        TextView info, name, dateOfBirth, age, gender, maritalStatus;
        TextView schoolName, address, designation, education, experience, timing, income;
        TextView info1, historyTextView, info2, dietaryTextView, info3, physicalTextView;
        TextView info4, addictionTextView, info5, examinationTextView, readMore;
        Button btnGeneratePDF;
        LinearLayout fullInfo;
        ImageView examinationImageView;


        public SurveyDataViewHolder(View itemView) {
            super(itemView);

            info = itemView.findViewById(R.id.Info);
            name = itemView.findViewById(R.id.Name);
            dateOfBirth = itemView.findViewById(R.id.dateOfBirth);
            age = itemView.findViewById(R.id.age);
            gender = itemView.findViewById(R.id.Gender);
            maritalStatus = itemView.findViewById(R.id.MarritalStatus);

            fullInfo = itemView.findViewById(R.id.fullInfo);
            schoolName = itemView.findViewById(R.id.SchoolName);
            address = itemView.findViewById(R.id.Address);
            designation = itemView.findViewById(R.id.Designation);
            education = itemView.findViewById(R.id.Eduction);
            experience = itemView.findViewById(R.id.Experience);
            timing = itemView.findViewById(R.id.Timing);
            income = itemView.findViewById(R.id.Income);

            info1 = itemView.findViewById(R.id.Info1);
            historyTextView = itemView.findViewById(R.id.HistoryTextView);
            info2 = itemView.findViewById(R.id.Info2);
            dietaryTextView = itemView.findViewById(R.id.DietryTextView);
            info3 = itemView.findViewById(R.id.Info3);
            physicalTextView = itemView.findViewById(R.id.physicalTextView);
            info4 = itemView.findViewById(R.id.Info4);
            addictionTextView = itemView.findViewById(R.id.addictionTextView);
            info5 = itemView.findViewById(R.id.Info5);
            examinationTextView = itemView.findViewById(R.id.examinationTextView);
            examinationImageView = itemView.findViewById(R.id.examinationImageView);

            btnGeneratePDF = itemView.findViewById(R.id.btnGeneratePDF);
            readMore = itemView.findViewById(R.id.ReadMore);
        }
    }


}
