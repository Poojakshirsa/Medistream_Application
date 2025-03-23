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
import com.example.medistreamapplication.model_form2.AdolescentFormModel;
import com.example.medistreamapplication.model_form2.DefectDataFormTwo;
import com.example.medistreamapplication.model_form2.DevelopmentDelayModelFormTwo;
import com.example.medistreamapplication.model_form2.PreliminaryFindingsFormTwo;
import com.example.medistreamapplication.model_form2.SurveyDataFormTwo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.OutputStream;

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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SurveyDataAdapter_Form2 extends RecyclerView.Adapter<SurveyDataAdapter_Form2.SurveyDataViewHolder> {
    private static final int STORAGE_PERMISSION_CODE = 1;
    private List<SurveyDataFormTwo> surveyDataList;
    private List<SurveyDataFormTwo> surveyDataListFull;
    private Context context;
    private DatabaseReference databaseReference;
    public SurveyDataAdapter_Form2(Context context, List<SurveyDataFormTwo> surveyDataList) {
        this.context = context;
        this.surveyDataList = surveyDataList;
        this.surveyDataListFull = new ArrayList<>(surveyDataList);
        this.databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseConstants.FORM_PATH + "SixToEighteen");
    }

    @Override
    public SurveyDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_survey_data_two, parent, false);
        return new SurveyDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SurveyDataViewHolder holder, int position) {
        SurveyDataFormTwo surveyData = surveyDataList.get(position);

        if (surveyData.getSchoolData() != null) {
            holder.schoolName.setText(Html.fromHtml("<b>School Name:</b> " + surveyData.getSchoolData().getSchoolName()));
            holder.childName.setText(Html.fromHtml("<b>Child Name:</b> " + surveyData.getSchoolData().getChildName()));
            holder.dateOfBirth.setText(Html.fromHtml("<b>Date of Birth:</b> " + surveyData.getSchoolData().getDateOfBirth()));
            holder.Address.setText(Html.fromHtml("<b>Address:</b> " + surveyData.getSchoolData().getSchoolAddress()));
            holder.DateOfCheckup.setText(Html.fromHtml("<b>Date of Checkup:</b> " + surveyData.getSchoolData().getDateOfCheckup()));
            holder.Age.setText(Html.fromHtml("<b>Age:</b> " + surveyData.getSchoolData().getChildAge()));
            holder.Gender.setText(Html.fromHtml("<b>Gender:</b> " + surveyData.getSchoolData().getGender()));
            holder.InchargeTeacher.setText(Html.fromHtml("<b>Incharge Teacher Name:</b> " + surveyData.getSchoolData().getTeacherInchargeName()));
            holder.FatherName.setText(Html.fromHtml("<b>Father's Name:</b> " + surveyData.getSchoolData().getFatherName()));
            holder.MotherName.setText(Html.fromHtml("<b>Mother's Name:</b> " + surveyData.getSchoolData().getMotherName()));
            holder.ParentNo.setText(Html.fromHtml("<b>Parent's Contact:</b> " + surveyData.getSchoolData().getParentContact()));
            holder.Weight.setText(Html.fromHtml("<b>Weight:</b> " + surveyData.getSchoolData().getChildWeight()));
            holder.Height.setText(Html.fromHtml("<b>Height:</b> " + surveyData.getSchoolData().getChildHeight()));
            holder.Circumference.setText(Html.fromHtml("<b>BMI:</b> " + surveyData.getSchoolData().getBMI()));
            holder.MUAC.setText(Html.fromHtml("<b>BMI Classification:</b> " + surveyData.getSchoolData().getBMI_Classification()));
        }

        // ✅ Fetch Preliminary Findings Data (Remark, Referred Doctor, Image)
        PreliminaryFindingsFormTwo preliminaryFinding = surveyData.getPreliminaryFinding();
        if (preliminaryFinding != null) {
            holder.remarkTextView.setText(Html.fromHtml("<b>Remark:</b> " + (preliminaryFinding.getRemark() != null ? preliminaryFinding.getRemark() : "Not Provided")));
            holder.referredDoctorTextView.setText(Html.fromHtml("<b>Referred Doctor:</b> " + (preliminaryFinding.getReferredDoctor() != null ? preliminaryFinding.getReferredDoctor() : "Not Provided")));

            // ✅ Load Image using Glide
            if (preliminaryFinding.getImageUrl() != null && !preliminaryFinding.getImageUrl().isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(preliminaryFinding.getImageUrl())
                        .placeholder(R.drawable.placeholder_image)  // ✅ Use placeholder while loading
                        .into(holder.imageView);
            } else {
                holder.imageView.setImageResource(R.drawable.placeholder_image);  // ✅ Default Image
            }
        }

        holder.ReadMore.setOnClickListener(v -> {
            boolean isExpanded = holder.fullInfo.getVisibility() == View.VISIBLE;
            holder.fullInfo.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
            holder.ReadMore.setText(isExpanded ? "Read More" : "Show Less");
        });

        holder.generatePdf.setOnClickListener(v -> {
            if (checkPermission()) {
                createPdf(surveyData);
            } else {
                requestPermission();
            }
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


        // ✅ Check for Defects, Adolescent Data, Developmental Delays, and Findings
        checkDefectsUsingReflection(surveyData.getDefectsData(), holder);
        checkDeficienciesUsingReflection(surveyData.getAdolescentFormModel(), holder);
        checkDevelopmentalDelaysUsingReflection(surveyData.getDevelopmentalDelaysData(), holder);
        checkFindingsUsingReflection(preliminaryFinding, holder);

        // ✅ Handle Item Deletion
        holder.itemView.setOnLongClickListener(v -> {
            new android.app.AlertDialog.Builder(v.getContext())
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


    private boolean checkPermission() {
        int permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void createPdf(SurveyDataFormTwo surveyData) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "SixToEighteen" + System.currentTimeMillis() + ".pdf");
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

    private void generatePdf(OutputStream outputStream, SurveyDataFormTwo surveyData) throws IOException {
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

        addRowToTable(table, "School Name:", surveyData.getSchoolData().getSchoolName());
        addRowToTable(table, "Date of Birth:", surveyData.getSchoolData().getDateOfBirth());
        addRowToTable(table, "Child Name:", surveyData.getSchoolData().getChildName());
        addRowToTable(table, "Address:", surveyData.getSchoolData().getSchoolAddress());
        addRowToTable(table, "Date of Checkup:", surveyData.getSchoolData().getDateOfCheckup());
        addRowToTable(table, "Age:", String.valueOf(surveyData.getSchoolData().getChildAge()));
        addRowToTable(table, "Gender:", surveyData.getSchoolData().getGender());
        addRowToTable(table, "Father's Name:", surveyData.getSchoolData().getFatherName());
        addRowToTable(table, "Mother's Name:", surveyData.getSchoolData().getMotherName());
        addRowToTable(table, "Parent's Contact:", surveyData.getSchoolData().getParentContact());
        addRowToTable(table, "Weight:", surveyData.getSchoolData().getChildWeight() + " kg");
        addRowToTable(table, "Height:", surveyData.getSchoolData().getChildHeight() + " cm");

        document.add(table);
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Defects:").setBold().setFontSize(14));
        document.add(new Paragraph(getDefectsSummary(surveyData)));

        document.add(new Paragraph("Adolescent Data:").setBold().setFontSize(14));
        document.add(new Paragraph(getAdolescentDataSummary(surveyData)));

        document.add(new Paragraph("Developmental Delays:").setBold().setFontSize(14));
        document.add(new Paragraph(getDevelopmentalDelaysSummary(surveyData)));

        document.add(new Paragraph("Preliminary Findings:").setBold().setFontSize(14));
        document.add(new Paragraph(getPreliminaryFindingsSummary(surveyData)));

        document.close();
    }

    private void addRowToTable(Table table, String key, String value) {
        table.addCell(new Cell().add(new Paragraph(key).setBold().setFontSize(12)).setBackgroundColor(new DeviceRgb(230, 230, 230)));
        table.addCell(new Cell().add(new Paragraph(value)).setFontSize(12));
    }

    private String getDefectsSummary(SurveyDataFormTwo surveyData) {
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

    /**
     * This method uses reflection to extract all boolean fields from the AdolescentFormModel.
     */
    private String getAdolescentDataSummary(SurveyDataFormTwo surveyData) {
        StringBuilder adolescentSummary = new StringBuilder();
        if (surveyData.getAdolescentFormModel() != null) {
            for (Method method : surveyData.getDefectsData().getClass().getMethods()) {
                if (method.getName().startsWith("is") && method.getReturnType() == boolean.class) {
                    try {
                        if ((boolean) method.invoke(surveyData.getDefectsData())) {
                            adolescentSummary.append(method.getName().substring(2)).append(", ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            adolescentSummary.append("No adolescent data available.");
        }
        return adolescentSummary.length() > 0 ? adolescentSummary.substring(0, adolescentSummary.length() - 2) : "No defects";

    }

    private String getDevelopmentalDelaysSummary(SurveyDataFormTwo surveyData) {
        StringBuilder delays = new StringBuilder();
        if (surveyData.getDevelopmentalDelaysData() != null) {
            try {
                Method[] methods = surveyData.getDevelopmentalDelaysData().getClass().getMethods();
                for (Method method : methods) {
                    if (method.getName().startsWith("is") && method.getReturnType() == boolean.class) {
                        boolean result = (boolean) method.invoke(surveyData.getDevelopmentalDelaysData());
                        if (result) {
                            delays.append(method.getName().substring(2)).append(", ");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return delays.length() > 0 ? delays.substring(0, delays.length() - 2) : "No developmental delays";
    }

    private String getPreliminaryFindingsSummary(SurveyDataFormTwo surveyData) {
        StringBuilder findings = new StringBuilder();
        if (surveyData.getPreliminaryFinding() != null) {
            try {
                Method[] methods = surveyData.getPreliminaryFinding().getClass().getMethods();
                for (Method method : methods) {
                    if (method.getName().startsWith("is") && method.getReturnType() == boolean.class) {
                        boolean result = (boolean) method.invoke(surveyData.getPreliminaryFinding());
                        if (result) {
                            findings.append(method.getName().substring(2)).append(", ");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return findings.length() > 0 ? findings.substring(0, findings.length() - 2) : "No findings";
    }

    private void deleteItemFromDatabase(SurveyDataFormTwo surveyData, int position) {
        String surveyDataId = surveyData.getId();
        Log.d("SurveyDataAdapter_Form2", "Attempting to delete item with ID: " + surveyDataId);
        databaseReference.child(surveyDataId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    if (position >= 0 && position < surveyDataList.size()) {
                        surveyDataList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void checkDefectsUsingReflection(DefectDataFormTwo defectData, SurveyDataViewHolder holder) {
        StringBuilder defectsText = new StringBuilder();
        if (defectData != null) {
            try {
                Method[] methods = defectData.getClass().getMethods();
                for (Method method : methods) {
                    if (method.getName().startsWith("is") && method.getReturnType() == boolean.class) {
                        boolean result = (boolean) method.invoke(defectData);
                        if (result) {
                            String defectName = method.getName().substring(2);
                            defectsText.append("- ").append(defectName).append("\n");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.defectsTextView.setText(defectsText.toString());
        } else {
            holder.defectsTextView.setText("No defect data available.");
        }
    }

    public void checkDeficienciesUsingReflection(AdolescentFormModel deficiencyData, SurveyDataViewHolder holder) {
        StringBuilder deficienciesText = new StringBuilder();
        if (deficiencyData != null) {
            try {
                Method[] methods = deficiencyData.getClass().getMethods();
                for (Method method : methods) {
                    if (method.getName().startsWith("is") && method.getReturnType() == boolean.class) {
                        boolean result = (boolean) method.invoke(deficiencyData);
                        if (result) {
                            String deficiencyName = method.getName().substring(2);
                            deficienciesText.append("- ").append(deficiencyName).append("\n");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.deficienciesTextView.setText(deficienciesText.toString());
        } else {
            holder.deficienciesTextView.setText("No deficiency data available.");
        }
    }

    public void checkDevelopmentalDelaysUsingReflection(DevelopmentDelayModelFormTwo delaysData, SurveyDataViewHolder holder) {
        StringBuilder delaysText = new StringBuilder();
        if (delaysData != null) {
            try {
                Method[] methods = delaysData.getClass().getMethods();
                for (Method method : methods) {
                    if (method.getName().startsWith("is") && method.getReturnType() == boolean.class) {
                        boolean result = (boolean) method.invoke(delaysData);
                        if (result) {
                            String delayName = method.getName().substring(2);
                            delaysText.append("- ").append(delayName).append("\n");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.developmentalDelaysTextView.setText(delaysText.toString());
        } else {
            holder.developmentalDelaysTextView.setText("No developmental delays data available.");
        }
    }

    public void checkFindingsUsingReflection(PreliminaryFindingsFormTwo preliminaryFinding, SurveyDataViewHolder holder) {
        StringBuilder findingsText = new StringBuilder();
        if (preliminaryFinding != null) {
            try {
                Method[] methods = preliminaryFinding.getClass().getMethods();
                for (Method method : methods) {
                    if (method.getName().startsWith("is") && method.getReturnType() == boolean.class) {
                        boolean result = (boolean) method.invoke(preliminaryFinding);
                        if (result) {
                            String findingName = method.getName().substring(2);
                            findingsText.append("- ").append(findingName).append("\n");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.findingsTextView.setText(findingsText.toString());
        } else {
            holder.findingsTextView.setText("No findings available.");
        }
    }

    public void filterList(List<SurveyDataFormTwo> filteredList) {
        surveyDataList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return surveyDataList.size();
    }

    public static class SurveyDataViewHolder extends RecyclerView.ViewHolder {
        TextView schoolName, childName, dateOfBirth,defectsTextView, Address, DateOfCheckup, Age, Gender,
                InchargeTeacher, FatherName, MotherName, ParentNo, Weight, Height, Circumference, MUAC,
                deficienciesTextView, developmentalDelaysTextView, findingsTextView, ReadMore,
                remarkTextView, referredDoctorTextView; // ✅ Added remark and referredDoctor

        ImageView imageView; // ✅ Added ImageView for uploaded image
        LinearLayout fullInfo;
        Button generatePdf, shareWithParent;

        public SurveyDataViewHolder(View itemView) {
            super(itemView);

            schoolName = itemView.findViewById(R.id.schoolName);
            childName = itemView.findViewById(R.id.childName);
            dateOfBirth = itemView.findViewById(R.id.childDateOfBirth);
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
            deficienciesTextView = itemView.findViewById(R.id.deficienciesTextView);
            developmentalDelaysTextView = itemView.findViewById(R.id.developmentalDelaysTextView);
            findingsTextView = itemView.findViewById(R.id.findingsTextView);
            fullInfo = itemView.findViewById(R.id.fullInfo);
            generatePdf = itemView.findViewById(R.id.btnGeneratePDF);
            ReadMore = itemView.findViewById(R.id.ReadMore);

            // ✅ New Views
            remarkTextView = itemView.findViewById(R.id.remarkTextView);
            referredDoctorTextView = itemView.findViewById(R.id.referredDoctorTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}
