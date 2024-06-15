package com.example.e_val;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StudentLoginPage extends AppCompatActivity {
    DATABASE_HELPER dbHelper;
    SQLiteDatabase sql;
    EditText et_StudID, et_Password;
    ImageView ImageViewShowHidePwd, iv_Transparent;
    TextView tv_ForgotPass, tv_Faculty;
    Button btn_LoginStudent;

    String studName;
    byte[] studImage;
    int studID;
    ArrayList<Student> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_login_page);

        dbHelper = new DATABASE_HELPER(this);


        findView();
        implementSeePassword();


        tv_Faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StudentLoginPage.this, TeacherLoginPage.class);
                startActivity(i);
            }
        });

        btn_LoginStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ID = et_StudID.getText().toString();
                String password = et_Password.getText().toString();


                getName(ID);

                if (ID.isEmpty() || password.isEmpty()) {
                    Toast.makeText(StudentLoginPage.this, "Please fill all the requirements.", Toast.LENGTH_SHORT).show();
                } else {

                    boolean checkID = dbHelper.checkStudentNumber(ID);
//                    boolean checkPass = dbHelper.checkStudentPassword(password);
                    boolean checkStudentAccount = dbHelper.checkStudentLogin(ID, password);
                    boolean checkIfExists = dbHelper.checkIfStudAccExists(ID, password);

                    if (checkStudentAccount == true){
                        Intent i = new Intent(StudentLoginPage.this, HomePage.class);
                             i.putExtra("studName", studName);
                             i.putExtra("studID", studID);
                             startActivity(i);
                             Toast.makeText(StudentLoginPage.this, "Login successful!", Toast.LENGTH_SHORT).show();
                             clear();
                    }
                    else if (checkIfExists == true){
                        Toast.makeText(StudentLoginPage.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                        clear();
                    }

                    else if (checkStudentAccount == false){
                        Toast.makeText(StudentLoginPage.this, "Student Account does not exists! Kindly inform your program's director.", Toast.LENGTH_SHORT).show();
                        clear();
                    }
                }
            }
        });

    }

    public void getName(String number){
        sql = dbHelper.getWritableDatabase();

        Cursor c = sql.rawQuery("select * from " + dbHelper.TABLE_STUDENTS + " where number = ?", new String[]{number});

        if (c.getCount() >= 0){
            while (c.moveToNext()) {
                int id = Integer.parseInt(c.getString(0));
                byte[] image = c.getBlob(1);
                String fname = c.getString(5);

                studImage = image;
                studName  = fname;
                studID = id;
            }
        }

    }

    private void implementSeePassword(){
        ImageViewShowHidePwd.setImageResource(R.drawable.hide);
        ImageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_Password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {

                    //if password visible then Hide it
                    et_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    //change icon after hiding
                    ImageViewShowHidePwd.setImageResource(R.drawable.hide);
                    et_Password.setSelection(et_Password.getText().length());
                } else {

                    et_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ImageViewShowHidePwd.setImageResource(R.drawable.view);
                    et_Password.setSelection(et_Password.getText().length());

                }
            }
        });
    }

    private void findView() {
        iv_Transparent = (ImageView) findViewById(R.id.ivTransparent);
        et_StudID = (EditText) findViewById(R.id.et_StudId);
        et_Password = (EditText) findViewById(R.id.et_Password);
        tv_ForgotPass = (TextView) findViewById(R.id.tvForgotPass);
        tv_Faculty = (TextView) findViewById(R.id.tvFaculty);
        ImageViewShowHidePwd = (ImageView) findViewById(R.id.iv_show_hide_pwd);
        btn_LoginStudent = (Button) findViewById(R.id.btnLoginStudent);


    }

    private void clear(){
        et_StudID.setText("");
        et_Password.setText("");
    }

}


