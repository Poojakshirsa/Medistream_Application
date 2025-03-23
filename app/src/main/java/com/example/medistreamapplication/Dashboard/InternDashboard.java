package com.example.medistreamapplication.Dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.medistreamapplication.Download;
import com.example.medistreamapplication.SixToEighteen.ShowForm_SixToEighteen;
import com.example.medistreamapplication.TeacherForm.Demographic_Form;
import com.example.medistreamapplication.TeacherForm.ShowForm_TeacherForm;
import com.example.medistreamapplication.ThreeToSix.School_Information_Section_FirstForm;
import com.example.medistreamapplication.SixToEighteen.School_Information_Section_SecondForm;
import com.example.medistreamapplication.ThreeToSix.ShowForm_ThreeToSix;


public class InternDashboard extends AppCompatActivity {

    com.example.medistreamapplication.databinding.ActivityInternDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= com.example.medistreamapplication.databinding.ActivityInternDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnFillForm36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InternDashboard.this, School_Information_Section_FirstForm.class);
                startActivity(intent);
            }
        });
        binding.btnFillForm618.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InternDashboard.this, School_Information_Section_SecondForm.class);
                startActivity(intent);
            }
        });

        binding.btnViewForm36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InternDashboard.this, ShowForm_ThreeToSix.class);
                startActivity(intent);
            }
        });

        binding.btnFillFormTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InternDashboard.this, Demographic_Form.class);
                startActivity(intent);
            }
        });

        binding.btnViewForm618.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    Intent intent=new Intent(InternDashboard.this, ShowForm_SixToEighteen.class);
                    startActivity(intent);
                }

        });
        binding.btnViewFormTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InternDashboard.this, ShowForm_TeacherForm.class);
                startActivity(intent);
            }
        });
        binding.layoutdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InternDashboard.this, Download.class);
                startActivity(intent);
            }
        });
    }
}