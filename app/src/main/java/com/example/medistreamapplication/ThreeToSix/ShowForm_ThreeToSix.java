package com.example.medistreamapplication.ThreeToSix;
import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.medistreamapplication.Adapter.SurveyDataAdapter;
import com.example.medistreamapplication.Firebase.FirebaseConstants;
import com.example.medistreamapplication.R;
import com.example.medistreamapplication.model_form1.DefectData;
import com.example.medistreamapplication.model_form1.DeficiencyData;
import com.example.medistreamapplication.model_form1.DevelopmentalDelaysData;
import com.example.medistreamapplication.model_form1.InternModel;
import com.example.medistreamapplication.model_form1.PreliminaryFinding;
import com.example.medistreamapplication.model_form1.SchoolData;
import com.example.medistreamapplication.model_form1.SurveyData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
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
public class ShowForm_ThreeToSix extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private RecyclerView recyclerView;
    SchoolData schoolData;
    private SurveyDataAdapter adapter;
    private List<SurveyData> surveyDataList;
    private DatabaseReference surveyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_form_three_to_six);
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String internJson = sharedPreferences.getString("UserLogin", "");
        ImageView btnGenerateExcel = findViewById(R.id.img_excel);
        // Convert JSON back to InternModel object
        Gson gson = new Gson();
        InternModel intern = gson.fromJson(internJson, InternModel.class);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) { // Only needed for Android 10 and below
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

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
        adapter = new SurveyDataAdapter(this,surveyDataList);
        recyclerView.setAdapter(adapter);

        surveyRef = FirebaseDatabase.getInstance()
                .getReference(FirebaseConstants.FORM_PATH + "ThreeToSixYear")
                .child(internId); // Fetch only forms under current intern ID


        surveyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                surveyDataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SurveyData surveyData = snapshot.getValue(SurveyData.class);
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
        List<SurveyData> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase();

        for (SurveyData surveyData : surveyDataList) {
            boolean matches = false;

            // Check fields of all nested objects
            if (surveyData.getSchoolData() != null) {
                matches = checkObjectFields(surveyData.getSchoolData(), lowerCaseQuery);
            }
            if (!matches && surveyData.getDefectsData() != null) {
                matches = checkObjectFields(surveyData.getDefectsData(), lowerCaseQuery);
            }
            if (!matches && surveyData.getDeficienciesData() != null) {
                matches = checkObjectFields(surveyData.getDeficienciesData(), lowerCaseQuery);
            }
            if (!matches && surveyData.getDevelopmentalDelaysData() != null) {
                matches = checkObjectFields(surveyData.getDevelopmentalDelaysData(), lowerCaseQuery);
            }
            if (!matches && surveyData.getPreliminaryFinding() != null) {
                matches = checkObjectFields(surveyData.getPreliminaryFinding(), lowerCaseQuery);
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
    private void generateExcelFile() {
        // Step 1: Check if there is data to export
        if (surveyDataList.isEmpty()) {
            Toast.makeText(this, "No data to export!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Step 2: Create an Excel workbook and sheet
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Survey Data Three To Six");

        // Step 3: Define the list of POJO classes to extract data from
        List<Class<?>> pojoClasses = Arrays.asList(
                SchoolData.class,
                DefectData.class,
                DeficiencyData.class,
                DevelopmentalDelaysData.class,
                PreliminaryFinding.class
        );

        // Step 4: Create the header row dynamically
        Row headerRow = sheet.createRow(0);
        List<Field> allFields = new ArrayList<>();
        int columnIndex = 0;

        for (Class<?> pojoClass : pojoClasses) {
            Field[] fields = pojoClass.getDeclaredFields();
            for (Field field : fields) {
                allFields.add(field); // Store field reference
                Cell cell = headerRow.createCell(columnIndex++);
                cell.setCellValue(field.getName()); // Set header names
            }
        }

        // Step 5: Populate data rows
        int rowIndex = 1;
        for (SurveyData surveyData : surveyDataList) {
            Row row = sheet.createRow(rowIndex++);

            // Get the corresponding POJO objects
            List<Object> pojoObjects = Arrays.asList(
                    surveyData.getSchoolData(),
                    surveyData.getDefectsData(),
                    surveyData.getDeficienciesData(),
                    surveyData.getDevelopmentalDelaysData(),
                    surveyData.getPreliminaryFinding()
            );

            columnIndex = 0;
            for (int i = 0; i < pojoClasses.size(); i++) {
                Object pojo = pojoObjects.get(i);
                Field[] fields = pojoClasses.get(i).getDeclaredFields();

                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        Object value = (pojo != null) ? field.get(pojo) : null;
                        row.createCell(columnIndex++).setCellValue(value != null ? value.toString() : "");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Step 6: Save the Excel file
        saveExcelFile(workbook);
    }


//    private void generateExcelFile() {
//        if (surveyDataList.isEmpty()) {
//            Toast.makeText(this, "No data to export!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Workbook workbook = new HSSFWorkbook(); // Use HSSFWorkbook for .xls format
//        Sheet sheet = workbook.createSheet("Survey Data");
//
//        // Create header row
//        Row headerRow = sheet.createRow(0);
//        Field[] fields = SchoolData.class.getDeclaredFields(); // Get fields dynamically
//        for (int i = 0; i < fields.length; i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(fields[i].getName()); // Use field names as headers
//        }
//
//        int rowIndex = 1; // Initialize row index before loop
//
//        // Populate data
//        for (SurveyData surveyData : surveyDataList) {
//            Row row = sheet.createRow(rowIndex++);
//            SchoolData schoolData = surveyData.getSchoolData();
//            if (schoolData != null) {
//
//                for (Field field : fields) {
//                    field.setAccessible(true); // Allow access to private fields
//                    try {
//                        Object value = field.get(schoolData);
//                        Log.d("SchoolData", field.getName() + ": " + (value != null ? value.toString() : "null"));
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//
//            for (int i = 0; i < fields.length; i++) {
//                fields[i].setAccessible(true);
//                try {
//                    Object value = fields[i].get(schoolData);
//                    row.createCell(i).setCellValue(value != null ? value.toString() : "");
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        saveExcelFile(workbook);
//    }

    private void saveExcelFile(Workbook workbook) {
        OutputStream outputStream;
        Uri fileUri = null;
        String fileName = "SurveyDataThreeToSix.xls";

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                values.put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.ms-excel");
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

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generateExcelFile();
            } else {
                Toast.makeText(this, "Permission denied! Can't save file.", Toast.LENGTH_SHORT).show();
            }
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
