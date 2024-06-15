package com.example.e_val;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class TeacherRegistrationPage extends AppCompatActivity {

    DATABASE_HELPER dbHelper;

    EditText profEmail, profPosition, profLname, profFname, profMname, profPassword;
    ImageView profImg;
    Button btnUploadImg, btnRegister;

    private Bitmap ProfImageToStore;
    private final int GALLERY_REQ_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.techer_registration_page);

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

                String email = profEmail.getText().toString();
                String position = profPosition.getText().toString();
                String Lname = profLname.getText().toString();
                String Fname = profFname.getText().toString();
                String Mname = profMname.getText().toString();
                String password = profPassword.getText().toString();

                if (email.isEmpty() || position.isEmpty() || Lname.isEmpty() || Fname.isEmpty() || Mname.isEmpty() || password.isEmpty()
                        || ProfImageToStore == null ) {

                    Toast.makeText(TeacherRegistrationPage.this, "Please complete professor details..", Toast.LENGTH_SHORT).show();
                } else {

                    boolean checkIfExist = dbHelper.checkProfessorEmail(email);
                    if (checkIfExist == false) {

                        boolean ProfessorInserted = dbHelper.insertProfessors(ProfImageToStore, email, position,
                                Lname, Fname, Mname, password);
                        if (ProfessorInserted) {
                            Toast.makeText(TeacherRegistrationPage.this, "Professor successfully registered!", Toast.LENGTH_SHORT).show();
                            clear();
                        } else {
                            Toast.makeText(TeacherRegistrationPage.this, "Error, professor can not be registered!", Toast.LENGTH_SHORT).show();
                            clear();
                        }
                    } else {
                        Toast.makeText(TeacherRegistrationPage.this, "Professor email already exists!", Toast.LENGTH_SHORT).show();
                        clear();
                    }

                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {

                super.onActivityResult(requestCode, resultCode, data);
                ProfImageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profImg.setImageBitmap(ProfImageToStore);
//                Toast.makeText(this, "image: "+ProfImageToStore, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

    }

    public void GoBack(View v){
        Intent intent = new Intent(TeacherRegistrationPage.this, AdminMenuPage.class);
        startActivity(intent);
    }

    private void findView(){
        profImg = findViewById(R.id.ivProfImg);
        btnUploadImg = findViewById(R.id.btnUploadImg);
        btnRegister = findViewById(R.id.btnRegister);
        profEmail = findViewById(R.id.etProfEmail);
        profPosition = findViewById(R.id.etProfPosition);
        profLname = findViewById(R.id.etProfLname);
        profFname = findViewById(R.id.etProfFname);
        profMname = findViewById(R.id.etProfMname);
        profPassword = findViewById(R.id.etProfPassword);
    }

    private void clear(){
        profImg.setImageResource(R.drawable.ic_baseline_image_24);
        profEmail.setText("");
        profPosition.setText("");
        profLname.setText("");
        profFname.setText("");
        profMname.setText("");
        profPassword.setText("");
    }
}