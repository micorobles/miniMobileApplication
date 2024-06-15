package com.example.e_val;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StudentRegistrationPage extends AppCompatActivity {
    DATABASE_HELPER dbHelper;

    EditText studNum, studEmail, studLname, studFname, studMname, studPassword;
    ImageView studImg;
    Button btnUploadImg, btnRegister;

    private Bitmap imageToStore;
    private final int GALLERY_REQ_CODE = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_registration_page);

        dbHelper = new DATABASE_HELPER(this);
        findView();

        btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setType("image/*");
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number = studNum.getText().toString();
                String email = studEmail.getText().toString();
                String Lname = studLname.getText().toString();
                String Fname = studFname.getText().toString();
                String Mname = studMname.getText().toString();
                String password = studPassword.getText().toString();

                if (number.isEmpty() || email.isEmpty() || Lname.isEmpty() || Fname.isEmpty() || Mname.isEmpty() || password.isEmpty()
                        || imageToStore == null) {
                    Toast.makeText(StudentRegistrationPage.this, "Please complete student details..", Toast.LENGTH_SHORT).show();
                } else {

                    boolean checkIfExist = dbHelper.checkStudentNumber(number);
                    if (checkIfExist == false) {

                        boolean StudentInserted = dbHelper.insertStudents(imageToStore, number, email,
                                Lname, Fname, Mname, password);

                        if (StudentInserted) {
                            Toast.makeText(StudentRegistrationPage.this, "Student successfully registered!", Toast.LENGTH_SHORT).show();
                            clear();
                        } else {
                            Toast.makeText(StudentRegistrationPage.this, "Error, student can not be registered!", Toast.LENGTH_SHORT).show();
                            clear();
                        }
                    } else {
                        Toast.makeText(StudentRegistrationPage.this, "Student Number already exists!", Toast.LENGTH_SHORT).show();
                        clear();
                    }

                }

            }
        });

    }

    public void GoBack(View v){
        Intent intent = new Intent(StudentRegistrationPage.this, AdminMenuPage.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {

                super.onActivityResult(requestCode, resultCode, data);
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                studImg.setImageBitmap(imageToStore);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

    }

    private void findView() {
        studImg = findViewById(R.id.ivStudImg);
        btnUploadImg = findViewById(R.id.btnUploadImg);
        btnRegister = findViewById(R.id.btnRegister);
        studNum = findViewById(R.id.etStudNum);
        studEmail = findViewById(R.id.etStudEmail);
        studLname = findViewById(R.id.etStudLname);
        studFname = findViewById(R.id.etStudFname);
        studMname = findViewById(R.id.etStudMname);
        studPassword = findViewById(R.id.etStudPassword);

    }

    public void clear(){

        studImg.setImageResource(R.drawable.ic_baseline_image_24);
        studNum.setText("");
        studEmail.setText("");
        studLname.setText("");
        studFname.setText("");
        studMname.setText("");
        studPassword.setText("");

    }

}