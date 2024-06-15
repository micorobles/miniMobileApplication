package com.example.e_val;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminMenuPage extends AppCompatActivity {
    CardView RegStudent, RegProf, StudProfile, ProfProfile, SubjectList, ProfProgram;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_menu_page);

        findView();

        RegStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminMenuPage.this , StudentRegistrationPage.class);
                startActivity(i);
            }
        });

        RegProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMenuPage.this, TeacherRegistrationPage.class);
                startActivity(intent);
            }
        });

        StudProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMenuPage.this, StudentProfilePage.class);
                startActivity(intent);
            }
        });

        ProfProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMenuPage.this, TeacherProfilePage.class);
                startActivity(intent);
            }
        });

        SubjectList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMenuPage.this, SubjectList.class);
                startActivity(intent);
            }
        });

        ProfProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMenuPage.this, TeachingLoad.class);
                startActivity(intent);
            }
        });

    }

    public void GoBack(View v){
        Intent intent = new Intent(AdminMenuPage.this, TeacherLoginPage.class);
        startActivity(intent);
    }

    private void findView(){
        RegStudent = findViewById(R.id.cvRegStud);
        RegProf = findViewById(R.id.cvRegProf);
        StudProfile = findViewById(R.id.cvStudProfile);
        ProfProfile = findViewById(R.id.cvProfProfile);
        SubjectList = findViewById(R.id.cvSubjectList);
        ProfProgram = findViewById(R.id.cvProgram);
    }
}