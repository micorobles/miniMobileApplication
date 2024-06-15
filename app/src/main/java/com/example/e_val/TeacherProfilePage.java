package com.example.e_val;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TeacherProfilePage extends AppCompatActivity implements RecyclerViewInterface {

    DATABASE_HELPER dbHelper = new DATABASE_HELPER(this);;
    SQLiteDatabase sql;

    RecyclerView recyclerView;

    Dialog myDialog;
    ArrayList<Teacher> tList = new ArrayList<>();
    TeacherProfileAdapter adapter = new TeacherProfileAdapter(tList, TeacherProfilePage.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_profile_page);



        findView();
        retrieveProfessors();
    }

    public void retrieveProfessors() {

//        sql = dbHelper.getWritableDatabase();
//        Cursor c = sql.rawQuery(" Select * From " + dbHelper.TABLE_PROFESSORS + "", null);
//
//        if (c.getCount() > 0) {
//            while (c.moveToNext()) {
//
//                byte[] image = c.getBlob(1);
//                String email = c.getString(2);
//                String position = c.getString(3);
//                String lname = c.getString(4);
//                String fname = c.getString(5);
//                String mname = c.getString(6);
//                String password = c.getString(7);
//                Teacher model = new Teacher(image, email, position, lname, fname, mname, password);
//                tList.add(model);
//            }
//        } else {
//            Toast.makeText(this, "No professor record..", Toast.LENGTH_SHORT).show();
//        }
//        c.close();
//        sql.close();
        boolean checkProf = dbHelper.retrieveProf(tList);
        if (checkProf == true){
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            Toast.makeText(this, "No professor record..", Toast.LENGTH_SHORT).show();
        }

        
    }

    public void GoBack(View v) {
        Intent intent = new Intent(TeacherProfilePage.this, AdminMenuPage.class);
        startActivity(intent);
    }

    public void deleteProfessor(int position) {
        Teacher std = tList.get(position);
        String email = std.getEmail();

        boolean StudentDeleted = dbHelper.deleteProfessor(email);

        if (StudentDeleted == true) {
            Snackbar.make(findViewById(R.id.rootProf), "Professor Deleted", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(findViewById(R.id.rootProf), "Error Deleting", Snackbar.LENGTH_LONG).show();
        }

    }


    @Override
    public void onItemClick(int position) {
        myDialog = new Dialog(TeacherProfilePage.this);
        myDialog.setContentView(R.layout.clicked_profile_layout);
//        myDialog.show();
        ImageView iv_FileBack = myDialog.findViewById(R.id.btnFileBack);
        ImageView fileProfImg = myDialog.findViewById(R.id.FileImage);
        TextView tvName = myDialog.findViewById(R.id.tv1);
        TextView tvNum = myDialog.findViewById(R.id.tv2);
        TextView tvEmail = myDialog.findViewById(R.id.tv3);
        TextView tvPosition = myDialog.findViewById(R.id.tv4);
        TextView tvPass = myDialog.findViewById(R.id.tv5);
        TextView fileProfFullName = myDialog.findViewById(R.id.FileFullName);
        TextView fileStudNum = myDialog.findViewById(R.id.FileNum);
        TextView fileProfEmail = myDialog.findViewById(R.id.FileEmail);
        TextView fileProfPosition = myDialog.findViewById(R.id.FilePosition);
        TextView fileProfPass = myDialog.findViewById(R.id.FilePassword);

        Teacher prf = tList.get(position);

        byte[] profImage = prf.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(profImage, 0, profImage.length);
        String lname = prf.getLname();
        String fname = prf.getFname();
        String mname = prf.getMname();
        String fullName = lname + ", " + fname + " " + mname;

        fileProfImg.setImageBitmap(bitmap);

        tvName.setText("Professor Full Name"); //tv title
        fileProfFullName.setText(fullName);  //tv description

        tvNum.setVisibility(View.GONE);
        fileStudNum.setVisibility(View.GONE);

        tvEmail.setText("Professor Email");
        fileProfEmail.setText(String.valueOf(prf.getEmail()));

        tvPosition.setVisibility(View.VISIBLE);
        fileProfPosition.setVisibility(View.VISIBLE);
        tvPosition.setText("Professor Position");
        fileProfPosition.setText(String.valueOf(prf.getPosition()));

        tvPass.setText("Professor Password");
        fileProfPass.setText(String.valueOf(prf.getPassword()));

        iv_FileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TeacherProfilePage.this));
    }

    @Override
    public void onLongItemClick(int pos) {
        Dialog menuDLG = new Dialog(TeacherProfilePage.this);
        menuDLG.setContentView(R.layout.floating_menu);
        menuDLG.show();

        Button MenuDelete = menuDLG.findViewById(R.id.btnDelete);
        Button MenuEdit = menuDLG.findViewById(R.id.btnEdit);

        Teacher prf = tList.get(pos);

        String lname = prf.getLname();
        String fname = prf.getFname();
        String mname = prf.getMname();
        String fullName = lname + ", " + fname + " " + mname;

        MenuDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(TeacherProfilePage.this);
                ad.setTitle("Confirmation").setIcon(R.drawable.baste_logo).setMessage("Are you sure to delete this professor: " + fullName + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteProfessor(pos);
//                                adapter.notifyDataSetChanged();
                                tList.remove(pos);
                                adapter.notifyItemRemoved(pos);
                                adapter.notifyItemRangeChanged(pos, tList.size());
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
                Dialog tp = new Dialog(TeacherProfilePage.this);
                tp.setContentView(R.layout.techer_registration_page);
                tp.show();

                //find views from student_registration_page
                ImageView updtBtnBack = tp.findViewById(R.id.btnBack);
                ImageView updtPhoto = (ImageView) tp.findViewById(R.id.ivProfImg);
                Button updtBtnUploadImg = tp.findViewById(R.id.btnUploadImg);
                EditText updtLname = tp.findViewById(R.id.etProfLname);
                EditText updtFname = tp.findViewById(R.id.etProfFname);
                EditText updtMname = tp.findViewById(R.id.etProfMname);
                EditText updtPosition = tp.findViewById(R.id.etProfPosition);
                EditText updtEmail = tp.findViewById(R.id.etProfEmail);
                EditText updtPassword = tp.findViewById(R.id.etProfPassword);
                Button updtBtnRegister = tp.findViewById(R.id.btnRegister);
                LinearLayout updtLinearUpdate = tp.findViewById(R.id.LinearUpdateBtns);
                Button updtBtnCancel = tp.findViewById(R.id.btnCancel);
                Button updtBtnUpdate = tp.findViewById(R.id.btnUpdate);
                TextView updtTvTitle = tp.findViewById(R.id.tvRegistrationTitle);

                //setText to the ContentView
                byte[] profImg = prf.getImage();
                Bitmap imgBitmap = BitmapFactory.decodeByteArray(profImg, 0, profImg.length);

                int id = Integer.parseInt(String.valueOf(prf.getId()));
                updtPhoto.setImageBitmap(imgBitmap);
                updtLname.setText(String.valueOf(prf.getLname())); //get from the list position
                updtFname.setText(String.valueOf(prf.getFname()));
                updtMname.setText(String.valueOf(prf.getMname()));
                updtEmail.setText(String.valueOf(prf.getEmail()));
                updtPosition.setText(String.valueOf(prf.getPosition()));
                updtPassword.setText(String.valueOf(prf.getPassword()));

                updtTvTitle.setText("Edit Professor Details");
                updtBtnBack.setVisibility(View.INVISIBLE);
                updtBtnUploadImg.setVisibility(View.GONE);
                updtBtnRegister.setVisibility(View.GONE);
                updtLinearUpdate.setVisibility(View.VISIBLE);
//                updtEmail.setEnabled(false);
                updtPassword.setInputType(InputType.TYPE_CLASS_TEXT);

                updtBtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(TeacherProfilePage.this);
                        ad.setTitle("Confirmation").setIcon(R.drawable.baste_logo).setMessage("Are you sure to exit? Any unsaved data will be lost.")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        tp.dismiss();
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
                        String Plname = updtLname.getText().toString();
                        String Pfname = updtFname.getText().toString();
                        String Pmname = updtMname.getText().toString();
                        String Pemail = updtEmail.getText().toString();
                        String Pposition = updtPosition.getText().toString();
                        String Ppassword = updtPassword.getText().toString();

                        Teacher profUpdt = new Teacher(id, profImg, Pemail, Pposition, Plname, Pfname, Pmname, Ppassword);

                        boolean ProfUpdated = dbHelper.updateProfessor(Pemail, Pposition, Plname, Pfname, Pmname, Ppassword);

                        if (ProfUpdated == true) {
                            Snackbar.make(findViewById(R.id.rootProf), "Professor Updated", Snackbar.LENGTH_SHORT).show();
//                            Toast.makeText(TeacherProfilePage.this, "Professor Updated", Toast.LENGTH_SHORT).show();

                            tList.set(pos, profUpdt);
                            adapter.notifyItemChanged(pos);
                            tp.dismiss();


                        } else {
                            tp.dismiss();
                            Snackbar.make(findViewById(R.id.rootProf), "Error Updating Professor", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(TeacherProfilePage.this));
                menuDLG.dismiss();
            }

        });
    }

    private void findView() {
        recyclerView = findViewById(R.id.rv_profProfile);
    }
}