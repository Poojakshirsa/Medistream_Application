package com.example.medistreamapplication.TeacherForm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.medistreamapplication.Adapter.SurveyDataAdapter;
import com.example.medistreamapplication.Adapter.SurveyDataAdapter_Form3;
import com.example.medistreamapplication.Firebase.FirebaseConstants;
import com.example.medistreamapplication.R;
import com.example.medistreamapplication.model_form1.InternModel;
import com.example.medistreamapplication.model_teacherForm.AddictionModel;
import com.example.medistreamapplication.model_teacherForm.Demographic_Form_Model;
import com.example.medistreamapplication.model_teacherForm.DietaryHistoryModel;
import com.example.medistreamapplication.model_teacherForm.HistoryExaminationModel;
import com.example.medistreamapplication.model_teacherForm.PhysicalActivityModel;
import com.example.medistreamapplication.model_teacherForm.SurveyDataFormThree;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowForm_TeacherForm extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SurveyDataAdapter_Form3 adapter;
    private List<SurveyDataFormThree> surveyDataList;
    private DatabaseReference surveyRef;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_form_teacher_form);
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String internJson = sharedPreferences.getString("UserLogin", "");
        ImageView btnGenerateExcel = findViewById(R.id.img_excel);
        // Convert JSON back to InternModel object
        Gson gson = new Gson();
        InternModel intern = gson.fromJson(internJson, InternModel.class);

        if (intern == null || intern.getId() == null) {
            Toast.makeText(this, "Intern data missing!", Toast.LENGTH_SHORT).show();
            return;
        }
        btnGenerateExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateExcelFile();
            }
        });
        String internId = intern.getId();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search");
        surveyDataList = new ArrayList<>();
        adapter = new SurveyDataAdapter_Form3(this,surveyDataList);
        recyclerView.setAdapter(adapter);

        surveyRef = FirebaseDatabase.getInstance()
                .getReference(FirebaseConstants.FORM_PATH + "TeacherForm")
                .child(internId); // Fetch only forms under current intern ID


        surveyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                surveyDataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SurveyDataFormThree surveyData = snapshot.getValue(SurveyDataFormThree.class);
                    if (surveyData != null) {
                        surveyDataList.add(surveyData);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ShowForm_ThreeToSix", "Error fetching data from Firebase", databaseError.toException());
            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return false;
            }
        });
    }



    private void filterData(String query) {
        List<SurveyDataFormThree> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase();

        for (SurveyDataFormThree surveyData : surveyDataList) {
            boolean matches = false;

            // Check fields of all nested objects
            if (surveyData.getDemographicFormModel()!= null) {
                matches = checkObjectFields(surveyData.getDemographicFormModel(), lowerCaseQuery);
            }
            if (!matches && surveyData.getHistoryExaminationModel() != null) {
                matches = checkObjectFields(surveyData.getHistoryExaminationModel(), lowerCaseQuery);
            }
            if (!matches && surveyData.getDietaryHistoryModel() != null) {
                matches = checkObjectFields(surveyData.getDietaryHistoryModel(), lowerCaseQuery);
            }
            if (!matches && surveyData.getPhysicalActivityModel() != null) {
                matches = checkObjectFields(surveyData.getPhysicalActivityModel(), lowerCaseQuery);
            }
            if (!matches && surveyData.getAddictionModel() != null) {
                matches = checkObjectFields(surveyData.getAddictionModel(), lowerCaseQuery);
            }
            if (!matches && surveyData.getExaminationModel() != null) {
                matches = checkObjectFields(surveyData.getExaminationModel(), lowerCaseQuery);
            }

            if (matches) {
                filteredList.add(surveyData);
            }
        }

        if (filteredList.isEmpty()) {
            Log.d("Search", "No matching results for query: " + query);
        }


       adapter.filterList(filteredList);
    }
//    private void generateExcelFile() {
//        // Step 1: Check if there is data to export
//        if (surveyDataList.isEmpty()) {
//            Toast.makeText(this, "No data to export!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Step 2: Create an Excel workbook and sheet
//        Workbook workbook = new HSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Survey Data Teacher Form");
//
//        // Step 3: Define the list of POJO classes to extract data from
//        List<Class<?>> pojoClasses = Arrays.asList(
//                Demographic_Form_Model.class,
//                HistoryExaminationModel.class,
//                DietaryHistoryModel.class,
//                PhysicalActivityModel.class,
//                AddictionModel.class
//        );
//
//        // Step 4: Create the header row dynamically
//        Row headerRow = sheet.createRow(0);
//        List<Field> allFields = new ArrayList<>();
//        int columnIndex = 0;
//
//        for (Class<?> pojoClass : pojoClasses) {
//            Field[] fields = pojoClass.getDeclaredFields();
//            for (Field field : fields) {
//                allFields.add(field); // Store field reference
//                Cell cell = headerRow.createCell(columnIndex++);
//                cell.setCellValue(field.getName()); // Set header names
//            }
//        }
//
//        // Step 5: Populate data rows
//        int rowIndex = 1;
//        for (SurveyDataFormThree surveyDataFormThree : surveyDataList) {
//            Row row = sheet.createRow(rowIndex++);
//
//            // Get the corresponding POJO objects
//            List<Object> pojoObjects = Arrays.asList(
//                    surveyDataFormThree.getDemographicFormModel(),
//                    surveyDataFormThree.getHistoryExaminationModel(),
//                    surveyDataFormThree.getDietaryHistoryModel(),
//                    surveyDataFormThree.getPhysicalActivityModel(),
//                    surveyDataFormThree.getAddictionModel()
//            );
//
//            columnIndex = 0;
//            for (int i = 0; i < pojoClasses.size(); i++) {
//                Object pojo = pojoObjects.get(i);
//                Field[] fields = pojoClasses.get(i).getDeclaredFields();
//
//                for (Field field : fields) {
//                    field.setAccessible(true);
//                    try {
//                        Object value = (pojo != null) ? field.get(pojo) : null;
//                        row.createCell(columnIndex++).setCellValue(value != null ? value.toString() : "");
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
//        // Step 6: Save the Excel file
//        saveExcelFile(workbook);
//    }
//
//
//    private void saveExcelFile(Workbook workbook) {
//        OutputStream outputStream;
//        Uri fileUri = null;
//        String fileName = "SurveyDataTeacherForm.xls";
//
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
//                values.put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.ms-excel");
//                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
//
//                ContentResolver resolver = getContentResolver();
//                fileUri = resolver.insert(MediaStore.Files.getContentUri("external"), values);
//
//                if (fileUri != null) {
//                    outputStream = resolver.openOutputStream(fileUri);
//                    workbook.write(outputStream);
//                    outputStream.close();
//                    workbook.close();
//                    Toast.makeText(this, "Excel file saved to Downloads!", Toast.LENGTH_LONG).show();
//                }
//            } else {
//                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
//                outputStream = new FileOutputStream(file);
//                workbook.write(outputStream);
//                outputStream.close();
//                workbook.close();
//                Toast.makeText(this, "Excel file saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Failed to save Excel file", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void generateExcelFile() {
        if (surveyDataList.isEmpty()) {
            Toast.makeText(this, "No data to export!", Toast.LENGTH_SHORT).show();
            return;
        }

        Workbook workbook = new XSSFWorkbook(); // Using XSSFWorkbook for .xlsx files
        Sheet sheet = workbook.createSheet("Survey Data Teacher Form");

        // Create cell styles for header and data cells with wrapping enabled
        CellStyle wrapTextStyle = workbook.createCellStyle();
        wrapTextStyle.setWrapText(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setWrapText(true);

        List<Class<?>> pojoClasses = Arrays.asList(
                Demographic_Form_Model.class,
                HistoryExaminationModel.class,
                DietaryHistoryModel.class,
                PhysicalActivityModel.class,
                AddictionModel.class
        );

        // Create header row with increased height
        Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(60); // Set header row height (adjust as needed)
        List<Field> allFields = new ArrayList<>();
        int columnIndex = 0;
        for (Class<?> pojoClass : pojoClasses) {
            Field[] fields = pojoClass.getDeclaredFields();
            for (Field field : fields) {
                allFields.add(field);
                Cell cell = headerRow.createCell(columnIndex++);
                cell.setCellValue(field.getName());
                cell.setCellStyle(headerStyle);
            }
        }

        int rowIndex = 1;
        for (SurveyDataFormThree surveyDataFormThree : surveyDataList) {
            Row row = sheet.createRow(rowIndex++);
            // Set a high row height so that wrapped text is visible (adjust as needed)
            row.setHeightInPoints(80);

            List<Object> pojoObjects = Arrays.asList(
                    surveyDataFormThree.getDemographicFormModel(),
                    surveyDataFormThree.getHistoryExaminationModel(),
                    surveyDataFormThree.getDietaryHistoryModel(),
                    surveyDataFormThree.getPhysicalActivityModel(),
                    surveyDataFormThree.getAddictionModel()
            );

            columnIndex = 0;
            for (int i = 0; i < pojoClasses.size(); i++) {
                Object pojo = pojoObjects.get(i);
                Field[] fields = pojoClasses.get(i).getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        Object value = (pojo != null) ? field.get(pojo) : null;
                        Cell cell = row.createCell(columnIndex++);
                        cell.setCellValue(value != null ? value.toString() : "");
                        cell.setCellStyle(wrapTextStyle);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Set fixed column widths to force wrapping (adjust as needed)
        for (int i = 0; i < columnIndex; i++) {
            int desiredWidth = 15 * 256;  // 15 characters wide
            sheet.setColumnWidth(i, desiredWidth);
        }

        saveExcelFile(workbook);
    }

    private void saveExcelFile(Workbook workbook) {
        OutputStream outputStream;
        Uri fileUri = null;
        String fileName = "SurveyDataTeacherForm.xlsx"; // Use .xlsx for XSSFWorkbook

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                values.put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                ContentResolver resolver = getContentResolver();
                fileUri = resolver.insert(MediaStore.Files.getContentUri("external"), values);

                if (fileUri != null) {
                    outputStream = resolver.openOutputStream(fileUri);
                    workbook.write(outputStream);
                    outputStream.close();
                    workbook.close();
                    Toast.makeText(this, "Excel file saved to Downloads!", Toast.LENGTH_LONG).show();
                }
            } else {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                outputStream = new FileOutputStream(file);
                workbook.write(outputStream);
                outputStream.close();
                workbook.close();
                Toast.makeText(this, "Excel file saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save Excel file", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean checkObjectFields(Object object, String query) {
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(object);
                if (value != null && value.toString().toLowerCase().contains(query)) {
                    return true;
                }
            }
        } catch (IllegalAccessException e) {
            Log.e("Reflection", "Error accessing fields", e);
        }
        return false;
    }

}