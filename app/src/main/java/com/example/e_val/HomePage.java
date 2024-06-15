package com.example.e_val;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    DATABASE_HELPER dbHelper = new DATABASE_HELPER(HomePage.this);
    ;
    SQLiteDatabase sql;

    ArrayList<Student> list = new ArrayList<>();
    ArrayList<TeacherProgram> tpList = new ArrayList<>();
    ArrayList<TeachLoad> tl_list = new ArrayList<>();
    ImageView btnBack;
    ShapeableImageView iv_StudImg;
    Button btn_Evaluate;
    TextView tvStudName;
    TextInputLayout tIl1, tIl2;
//    String[] professors = {"Nimfa", "Tony Co", "Randy", "Diaz", "Dituks"};
//    String[] subjects = {"1", "2", "3", "4", "5"};

    ArrayList<String> profList;
    ArrayList<String> subjList;

    AutoCompleteTextView auto_CompleteTxt_Prof, auto_CompleteTxt_Subj;
    ArrayAdapter<String> adapterProfessors;
    ArrayAdapter<String> adapterSubjects;

    String studNameCalled;
    String Call_Prof;
    String Call_Subj;
    //    int load_id;
    int program_id;
    int studID;
//    String studCalled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        findView();
        displayNameAndImg();
        manipulateData();
        DropdownIsEmpty();
//        CreateStudStatus();

        auto_CompleteTxt_Prof.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String professor = parent.getItemAtPosition(position).toString();
                Call_Prof = professor;

                ValidateIfProgramExist(position);
//                Toast.makeText(HomePage.this, "prof: " + Call_Prof, Toast.LENGTH_SHORT).show();

            }
        });

        auto_CompleteTxt_Subj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                String subject = parent.getItemAtPosition(position).toString();
                Call_Subj = subject;
                ValidateIfProgramExist(position);
//                Toast.makeText(HomePage.this, "subj: " + subject, Toast.LENGTH_SHORT).show();

            }
        });

        btn_Evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomePage.this, EvaluationPage.class);
                i.putExtra("pkProf", Call_Prof);
                i.putExtra("studName", studNameCalled);
                i.putExtra("programID", program_id);
                i.putExtra("studID", studID);
                startActivity(i);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder b = new AlertDialog.Builder(HomePage.this);

                b.setTitle("Warning!")
                        .setIcon(R.drawable.baste_logo)
                        .setMessage("Are you sure you want to cancel evaluation? \n")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(HomePage.this, StudentLoginPage.class);
                                startActivity(intent);
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

            }
        });

    }

    private void DropdownIsEmpty() {

//        boolean isCleared = 1;
        if (profList.isEmpty() && subjList.isEmpty()) {

                boolean StudentIsCleared = dbHelper.updateStudStatus(studID);
                if (StudentIsCleared) {                                           //GAWA VALIDATION
                    auto_CompleteTxt_Subj.setEnabled(false);
                    auto_CompleteTxt_Prof.setEnabled(false);
                    Snackbar.make(findViewById(R.id.rootHomePage), "You are cleared for evaluation!", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(findViewById(R.id.rootHomePage), "Error clearing your name.", Snackbar.LENGTH_SHORT).show();
                }
            }
        }



    private void ValidateIfProgramExist(int position) {
        TeacherProgram tp = tpList.get(position);

        String name = tp.getProfName();
        String subj = tp.getSubjName();

//        Toast.makeText(this, "PROF TO SA LIST: "+name+" SUBJ TO SA LIST: "+subj, Toast.LENGTH_LONG).show();
        if (Call_Prof != null && Call_Subj != null) {

            if (Call_Prof == name && Call_Subj == subj) {
                program_id = tp.getProgramID();
                tIl1.setErrorEnabled(false);
                tIl2.setErrorEnabled(false);
                btn_Evaluate.setVisibility(View.VISIBLE);

//                Toast.makeText(this, "prog id: "+program_id, Toast.LENGTH_SHORT).show();
            } else {
                tIl1.setErrorEnabled(true);
                tIl1.setError(" ");
                tIl2.setErrorEnabled(true);
                tIl2.setError("Invalid subject class");
                btn_Evaluate.setVisibility(View.GONE);
//                Toast.makeText(this, "Please make sure to choose the correct professor and subject class.", Toast.LENGTH_SHORT).show();
            }

        } else {

        }
    }

    private void manipulateData() {

        profList = new ArrayList<>();
        subjList = new ArrayList<>();

        sql = dbHelper.getWritableDatabase();
        String query = " SELECT " + dbHelper.TEACHLOAD_ID_COLUMN + " , " + dbHelper.PROF_ID_COLUMN + " ," + dbHelper.PROF_FNAME_COLUMN + " , " + dbHelper.PROF_MNAME_COLUMN
                + " , " + dbHelper.PROF_LNAME_COLUMN + " , " + dbHelper.SUBJ_ID_COLUMN + " , " + dbHelper.SUBJ_CODE_COLUMN + " , " + dbHelper.SUBJ_DESC_COLUMN
                + " FROM " + dbHelper.TABLE_TEACHLOAD
                + " JOIN " + dbHelper.TABLE_PROFESSORS
                + " ON " + dbHelper.FK_PROF_COLUMN + " = " + dbHelper.PROF_ID_COLUMN
                + " JOIN " + dbHelper.TABLE_SUBJECTS
                + " ON " + dbHelper.FK_SUBJ_COLUMN + " = " + dbHelper.SUBJ_ID_COLUMN
                + " WHERE " + dbHelper.PROF_ID_COLUMN + " NOT IN ( SELECT DISTINCT " + dbHelper.FK_PROF_COLUMN
                + " FROM " + dbHelper.TABLE_EVALUATION
                + " LEFT JOIN " + dbHelper.TABLE_TEACHLOAD
                + " ON " + dbHelper.FK_TEACHLOAD_COLUMN + " = " + dbHelper.TEACHLOAD_ID_COLUMN
                + " WHERE " + dbHelper.FK_STUD_COLUMN + " = ? )";

        Cursor c = sql.rawQuery(query, new String[]{Integer.toString(studID)});

        if (c.getCount() > 0) {

            c.moveToFirst();
            while (!c.isAfterLast()) {
                int id = Integer.parseInt(String.valueOf(c.getString(0)));
                int p_id = Integer.parseInt(String.valueOf(c.getString(1)));
                String profName = c.getString(2);
                String profMname = c.getString(3);
                String profLname = c.getString(4);
                int s_id = Integer.parseInt(String.valueOf(c.getString(5)));
                String subjCode = c.getString(6);
                String subjDesc = c.getString(7);

                String Prof = profName + " " + profMname + ". " + profLname;
                String Subject = subjCode + " - " + subjDesc;
//
                c.moveToNext();
//                profList.add(Prof + " | " + Subject);
//                load_id = id;
                profList.add(Prof);
                subjList.add(Subject);
                tpList.add(new TeacherProgram(id, Prof, Subject));
                tl_list.add(new TeachLoad(id, p_id, s_id));

            }

//            Toast.makeText(this, "TeachLoad list: "+profList, Toast.LENGTH_SHORT).show();
            adapterProfessors = new ArrayAdapter<String>(this, R.layout.list_items, profList);
            adapterSubjects = new ArrayAdapter<String>(this, R.layout.list_items, subjList);

            auto_CompleteTxt_Prof.setAdapter(adapterProfessors);
            auto_CompleteTxt_Subj.setAdapter(adapterSubjects);
        }
    }

    public void displayNameAndImg() {

        studNameCalled = getIntent().getStringExtra("studName");
        Call_Prof = getIntent().getStringExtra("pkProf");
        studID = getIntent().getIntExtra("studID", 0);

        sql = dbHelper.getWritableDatabase();

        Cursor c = sql.rawQuery("select * from " + dbHelper.TABLE_STUDENTS + " where " + dbHelper.STUDENT_FNAME_COLUMN + " = ?", new String[]{studNameCalled});

        if (c.getCount() >= 0) {
            while (c.moveToNext()) {

                int id = Integer.parseInt(c.getString(0));
                byte[] image = c.getBlob(1);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

                tvStudName.setText("Hello, " + studNameCalled);
                iv_StudImg.setImageBitmap(bitmap);

            }
        }
    }

    private void findView() {
        iv_StudImg = findViewById(R.id.ivStudImg);
        btn_Evaluate = (Button) findViewById(R.id.btnEvaluate);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        auto_CompleteTxt_Prof = findViewById(R.id.autoCompleteTxtProf);
        auto_CompleteTxt_Subj = findViewById(R.id.autoCompleteTxtSubj);
        tvStudName = findViewById(R.id.tvStudCall);
        tIl1 = findViewById(R.id.dropdown1);
        tIl2 = findViewById(R.id.dropdown2);

    }

    public void ViewProfile(View view) {
//            Toast.makeText(this, "tangina", Toast.LENGTH_SHORT).show();
        Dialog myDialog = new Dialog(HomePage.this);
        myDialog.setContentView(R.layout.clicked_profile_layout);
        myDialog.show();
        ImageView iv_FileBack = myDialog.findViewById(R.id.btnFileBack);
        ImageView fileStudImg = myDialog.findViewById(R.id.FileImage);
        TextView fileFullName = myDialog.findViewById(R.id.FileFullName);
        TextView fileStudNum = myDialog.findViewById(R.id.FileNum);
        TextView fileStudEmail = myDialog.findViewById(R.id.FileEmail);
        TextView fileStudPass = myDialog.findViewById(R.id.FilePassword);
        TextView tvPass = myDialog.findViewById(R.id.tv5);

        fileStudPass.setVisibility(View.GONE);
        tvPass.setVisibility(View.GONE);

        sql = dbHelper.getWritableDatabase();

        Cursor c = sql.rawQuery("select * from " + dbHelper.TABLE_STUDENTS + " where " + dbHelper.STUDENT_FNAME_COLUMN + " = ?", new String[]{studNameCalled});

        if (c.getCount() >= 0) {
            while (c.moveToNext()) {

                byte[] image = c.getBlob(1);
                String num = c.getString(2);
                String email = c.getString(3);
                String lname = c.getString(4);
                String fname = c.getString(5);
                String mname = c.getString(6);

                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                String fullName = lname + ", " + fname + " " + mname;
                fileStudImg.setImageBitmap(bitmap);
                fileFullName.setText(fullName);
                fileStudNum.setText(num);
                fileStudEmail.setText(email);

            }
        }
        iv_FileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
    }

    public void SeeEvaluated(View view) {
        Intent i = new Intent(HomePage.this, EvaluatedPage.class);
        i.putExtra("pkProf", Call_Prof);
        i.putExtra("studName", studNameCalled);
        i.putExtra("programID", program_id);
        i.putExtra("studID", studID);
        startActivity(i);
    }

//    private void CreateStudStatus() {
//        boolean checkifStatusExists = dbHelper.checkifStatusExists(studID);
//
//        if (checkifStatusExists == false) {
//
//            boolean CreateStudStatus = dbHelper.insertStatus(studID);
//            if (CreateStudStatus == true) {
//            }
//        }
//    }
}