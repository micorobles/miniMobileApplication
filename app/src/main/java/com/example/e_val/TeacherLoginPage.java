package com.example.e_val;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TeacherLoginPage extends AppCompatActivity {
    TextView tv_Student, tv_ForgotPass;
    DATABASE_HELPER dbHelper;
    String sql;
    EditText et_ProfID, et_ProfPassword;
    ImageView ImageViewShowHidePwd;
    Button btn_LoginProf;

    String ProfEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_login_page);

        dbHelper = new DATABASE_HELPER(this);

        findView();
        implementSeePassword();

        tv_Student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TeacherLoginPage.this, StudentLoginPage.class);
                startActivity(i);
            }
        });

        btn_LoginProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ID = et_ProfID.getText().toString();
                String password = et_ProfPassword.getText().toString();

                ProfEmail = ID;

                if (ID.isEmpty() || password.isEmpty()) {
                    Toast.makeText(TeacherLoginPage.this, "Please fill all the requirements.", Toast.LENGTH_SHORT).show();
                } else {

                    boolean checkAdminID = dbHelper.checkAdminUsername(ID);
                    boolean checkAdminPass = dbHelper.checkAdminPassword(password);
                    boolean checkStudentAccount = dbHelper.checkProfLogin(ID, password);
                    boolean checkIfExists = dbHelper.checkIfProfAccExists(ID, password);

                    if(checkAdminID == true && checkAdminPass == true){
                        Intent i = new Intent(TeacherLoginPage.this, AdminMenuPage.class);
                        startActivity(i);
                        Toast.makeText(TeacherLoginPage.this, "Hello Admin!", Toast.LENGTH_SHORT).show();
                    }

                    else if (checkStudentAccount == true){
                        Intent i = new Intent(TeacherLoginPage.this, TeacherInterfacePage.class);
                        i.putExtra("profEmail", ProfEmail);
                        startActivity(i);

//                        Toast.makeText(StudentLoginPage.this, ""+ studName, Toast.LENGTH_LONG).show();
                        Toast.makeText(TeacherLoginPage.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        clear();
                    }
                    else if (checkIfExists == true){
                        Toast.makeText(TeacherLoginPage.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                        clear();
                    }

                    else if (checkStudentAccount == false){
                        Toast.makeText(TeacherLoginPage.this, "Professor Account does not exists! Kindly inform your director.", Toast.LENGTH_SHORT).show();
                        clear();
                    }
                }
            }
        });
    }

    private void findView() {
        tv_Student = (TextView) findViewById(R.id.tvStudent);
        et_ProfID = (EditText) findViewById(R.id.et_ProfEmail);
        et_ProfPassword = (EditText) findViewById(R.id.et_ProfPassword);
        tv_ForgotPass = (TextView) findViewById(R.id.tvForgotPass);
        ImageViewShowHidePwd = (ImageView) findViewById(R.id.iv_show_hide_pwd);
        btn_LoginProf = (Button) findViewById(R.id.btnLoginProf);
    }

    private void implementSeePassword(){

        ImageViewShowHidePwd.setImageResource(R.drawable.hide);
        ImageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_ProfPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {

                    //if password visible then Hide it
                    et_ProfPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    //change icon after hiding
                    ImageViewShowHidePwd.setImageResource(R.drawable.hide);
                    et_ProfPassword.setSelection(et_ProfPassword.getText().length());
                } else {

                    et_ProfPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ImageViewShowHidePwd.setImageResource(R.drawable.view);
                    et_ProfPassword.setSelection(et_ProfPassword.getText().length());

                }
            }
        });
    }

    private void clear() {

        et_ProfID.setText("");
        et_ProfPassword.setText("");

    }
}