package com.example.e_val;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class StudentProfilePage extends AppCompatActivity implements RecyclerViewInterface {
    DATABASE_HELPER dbHelper;
    SQLiteDatabase sql;

    RecyclerView recyclerView;

    Dialog myDialog;
    ArrayList<Student> list = new ArrayList<>();
    ArrayList<ClearedStudents> stdClr = new ArrayList<>();
    StudentProfileAdapter adapter = new StudentProfileAdapter(list, this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile_page);

        dbHelper = new DATABASE_HELPER(this);

        findView();
        retrieveStudents();
//        NEWretrieveStudent();
    }

    private void retrieveStudents() {

        sql = dbHelper.getWritableDatabase();
        Cursor c = sql.rawQuery(" Select * From " + dbHelper.TABLE_STUDENTS + "", null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                int id = Integer.parseInt(c.getString(0));
                byte[] image = c.getBlob(1);
                String num = c.getString(2);
                String email = c.getString(3);
                String lname = c.getString(4);
                String fname = c.getString(5);
                String mname = c.getString(6);
                String password = c.getString(7);
                int isCleared = Integer.parseInt(c.getString(8));
                Student model = new Student(id, image, num, email, lname, fname, mname, password, isCleared);
                list.add(model);

            }
        } else {
            Toast.makeText(this, "No student record..", Toast.LENGTH_SHORT).show();
        }
        c.close();
        sql.close();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void findView() {
        recyclerView = findViewById(R.id.rv_studProfile);

    }

    public void GoBack(View v) {
        Intent intent = new Intent(StudentProfilePage.this, AdminMenuPage.class);
        startActivity(intent);
    }

    public void deleteStudent(int position) {
        Student std = list.get(position);
        String number = std.getNum();

        boolean StudentDeleted = dbHelper.deleteStudent(number);

        if (StudentDeleted == true) {
            Snackbar.make(findViewById(R.id.rootStud), "Student Deleted", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(findViewById(R.id.rootStud), "Error Deleting", Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemClick(int position) {

        myDialog = new Dialog(StudentProfilePage.this);
        myDialog.setContentView(R.layout.clicked_profile_layout);
//        myDialog.show();
        ImageView iv_FileBack = myDialog.findViewById(R.id.btnFileBack);
        ImageView fileStudImg = myDialog.findViewById(R.id.FileImage);
        TextView fileFullName = myDialog.findViewById(R.id.FileFullName);
        TextView fileStudNum = myDialog.findViewById(R.id.FileNum);
        TextView fileStudEmail = myDialog.findViewById(R.id.FileEmail);
        TextView fileStudPass = myDialog.findViewById(R.id.FilePassword);

        Student std = list.get(position);

        byte[] studImage = std.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(studImage, 0, studImage.length);
        String lname = std.getLname();
        String fname = std.getFname();
        String mname = std.getMname();
        String fullName = lname + ", " + fname + " " + mname;

        fileStudImg.setImageBitmap(bitmap);
        fileFullName.setText(fullName);
        fileStudNum.setText(String.valueOf(std.getNum()));
        fileStudEmail.setText(String.valueOf(std.getEmail()));
        fileStudPass.setText(String.valueOf(std.getPassword()));

        iv_FileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(StudentProfilePage.this));
    }

    @Override
    public void onLongItemClick(int position) {
        Dialog menuDLG = new Dialog(StudentProfilePage.this);
        menuDLG.setContentView(R.layout.floating_menu);
        menuDLG.show();

        Button MenuDelete = menuDLG.findViewById(R.id.btnDelete);
        Button MenuEdit = menuDLG.findViewById(R.id.btnEdit);

        Student std = list.get(position);

        String lname = std.getLname();
        String fname = std.getFname();
        String mname = std.getMname();
        String fullName = lname + ", " + fname + " " + mname;

        MenuDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(StudentProfilePage.this);

                ad.setTitle("Confirmation").setIcon(R.drawable.baste_logo).setMessage("Are you sure to delete this student: " + fullName + "?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteStudent(position);
//                                adapter.notifyDataSetChanged();
                                list.remove(position);
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(position, list.size());
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();

                menuDLG.dismiss();
            }
        });

        MenuEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(StudentProfilePage.this);
                myDialog.setContentView(R.layout.student_registration_page);
                myDialog.show();

                //find views from student_registration_page
                ImageView updtBtnBack = myDialog.findViewById(R.id.btnBack);
                ImageView updtPhoto = (ImageView) myDialog.findViewById(R.id.ivStudImg);
                Button updtBtnUploadImg = myDialog.findViewById(R.id.btnUploadImg);
                EditText updtLname = myDialog.findViewById(R.id.etStudLname);
                EditText updtFname = myDialog.findViewById(R.id.etStudFname);
                EditText updtMname = myDialog.findViewById(R.id.etStudMname);
                EditText updtNumber = myDialog.findViewById(R.id.etStudNum);
                EditText updtEmail = myDialog.findViewById(R.id.etStudEmail);
                EditText updtPassword = myDialog.findViewById(R.id.etStudPassword);
                Button updtBtnRegister = myDialog.findViewById(R.id.btnRegister);
                LinearLayout updtLinearUpdate = myDialog.findViewById(R.id.LinearUpdateBtns);
                Button updtBtnCancel = myDialog.findViewById(R.id.btnCancel);
                Button updtBtnUpdate = myDialog.findViewById(R.id.btnUpdate);
                TextView updtTvTitle = myDialog.findViewById(R.id.tvRegistrationTitle);

                //setText to the ContentView
                byte[] studImg = std.getImage();
                Bitmap imgBitmap = BitmapFactory.decodeByteArray(studImg, 0, studImg.length);

                updtPhoto.setImageBitmap(imgBitmap);
                updtLname.setText(String.valueOf(std.getLname())); //get from the list position
                updtFname.setText(String.valueOf(std.getFname()));
                updtMname.setText(String.valueOf(std.getMname()));
                updtNumber.setText(String.valueOf(std.getNum()));
                updtEmail.setText(String.valueOf(std.getEmail()));
                updtPassword.setText(String.valueOf(std.getPassword()));

                updtTvTitle.setText("Edit Student Details");
                updtBtnBack.setVisibility(View.INVISIBLE);
                updtBtnUploadImg.setVisibility(View.GONE);
                updtBtnRegister.setVisibility(View.GONE);
                updtLinearUpdate.setVisibility(View.VISIBLE);
                updtNumber.setEnabled(false);
                updtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                updtBtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(StudentProfilePage.this);
                        ad.setTitle("Confirmation").setIcon(R.drawable.baste_logo).setMessage("Are you sure to exit? Any unsaved data will be lost.")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        myDialog.dismiss();
                                    }
                                })

                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                    }
                });

                updtBtnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = std.getId();
                        String num = updtNumber.getText().toString();
                        String lname = updtLname.getText().toString();
                        String fname = updtFname.getText().toString();
                        String mname = updtMname.getText().toString();
                        String email = updtEmail.getText().toString();
                        String password = updtPassword.getText().toString();
                        int isCleared = std.getIsCleared();
                        Student studUpdt = new Student(id, studImg, num, email, lname, fname, mname, password, isCleared);

                        Boolean StudentUpdated = dbHelper.updateStudents(num, lname, fname, mname, email, password, isCleared);

                        if (StudentUpdated == true) {
                            Snackbar.make(findViewById(R.id.rootStud), "Student Updated", Snackbar.LENGTH_SHORT).show();

                            list.set(position, studUpdt);
                            adapter.notifyItemChanged(position);


                        } else {
                            myDialog.dismiss();
                            Snackbar.make(findViewById(R.id.rootStud), "Error Updating Student", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(StudentProfilePage.this));
                menuDLG.dismiss();

            }
        });


    }

//    private void NEWretrieveStudent(){
//        //TRY JOIN TABLE CLEAR STUDENT KASAMA MGA ATTRB NG STUDENT TAS YUN NA IPAPASOK SA ADAPTER
//        sql = dbHelper.getWritableDatabase();
////        String query = " SELECT * FROM " + dbHelper.TABLE_STUDENT_STATUS
////                + " JOIN " + dbHelper.TABLE_STUDENTS
////                + " ON " + dbHelper.STATUS_FK_STUDENT + " = " + dbHelper.STUDENT_ID_COLUMN;
//        int isCleared = 1;
//        String query = " SELECT "+dbHelper.STATUS_FK_STUDENT+" FROM " + dbHelper.TABLE_STUDENT_STATUS
//                + " JOIN " + dbHelper.TABLE_STUDENTS
//                + " ON " + dbHelper.STATUS_FK_STUDENT + " = " + dbHelper.STUDENT_ID_COLUMN
//                + " WHERE " + dbHelper.STATUS_IS_CLEARED + " = ?";
//        Cursor c = sql.rawQuery(query, new String[]{Integer.toString(isCleared)});
//
//        if (c.getCount()>=0){
//            while (c.moveToNext()) {
//                int clearedStud = Integer.parseInt(c.getString(0));
////                Toast.makeText(this, "Cleared Student: "+ clearedStud, Toast.LENGTH_SHORT).show();
////                int statusID = Integer.parseInt(c.getString(0));
////                int fk_studID = Integer.parseInt(c.getString(1));
////                boolean isCLeared = Boolean.parseBoolean(c.getString(2));
////                int studID = Integer.parseInt(c.getString(3));
////                byte[] image = c.getBlob(4);
////                String num = c.getString(5);
////                String email = c.getString(6);
////                String lname = c.getString(7);
////                String fname = c.getString(8);
////                String mname = c.getString(9);
////                String password = c.getString(10);
//
////                ClearedStudents model = new ClearedStudents(clearedStud);
////                stdClr.add(model);
////                Toast.makeText(this, "cleared: " + isCLeared, Toast.LENGTH_LONG).show();
//            }
//
//        } else {
//            Toast.makeText(this, "No student record..", Toast.LENGTH_SHORT).show();
//        }
//        c.close();
//        sql.close();
//
////        recyclerView.setAdapter(adapter);
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//    }

}













