package com.example.e_val;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SubjectList extends AppCompatActivity {
    DATABASE_HELPER dbHelper;
    SQLiteDatabase sql;

    ImageView btnBack;
    ListView listviewSubj;
    FloatingActionButton btnAdd;
    Dialog myDialog;

    ArrayList<Subject> subjlist;
    ArrayList<String> subjToOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_list);
        dbHelper = new DATABASE_HELPER(this);

        findView();
        retrieveSubject();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SubjectList.this, AdminMenuPage.class);
                startActivity(i);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(SubjectList.this);
                myDialog.setContentView(R.layout.subject_registration_layout);
                myDialog.show();

                EditText etCode = myDialog.findViewById(R.id.etSubjCode);
                EditText etDescription = myDialog.findViewById(R.id.etSubjDesc);
                Button btnAddSubj = myDialog.findViewById(R.id.btnAdd);

                btnAddSubj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DATABASE_HELPER helper = new DATABASE_HELPER(SubjectList.this);
                        String code = etCode.getText().toString();
                        String description = etDescription.getText().toString();

                        if (code.isEmpty() || description.isEmpty()) {
                            Toast.makeText(SubjectList.this, "Please complete subject details..", Toast.LENGTH_SHORT).show();
                            Toast.makeText(SubjectList.this, "" + code + " , " + description, Toast.LENGTH_SHORT).show();
                        } else {

                            boolean subjectAdded = helper.insertSubjects(code, description);
                            if (subjectAdded) {
                                retrieveSubject();
                                myDialog.dismiss();
                                Snackbar.make(findViewById(R.id.rootSubj), "Subject Added.", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(findViewById(R.id.rootSubj), "Error Adding Subject.", Snackbar.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
            }
        });

        listviewSubj.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                ShowMenu(position);
                return true;

            }
        });

        listviewSubj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                viewSubject(position);
            }
        });


    }


    private void ShowMenu(int position) {
        Dialog menuDLG = new Dialog(SubjectList.this);
        menuDLG.setContentView(R.layout.floating_menu);
        menuDLG.show();

        Button MenuDelete = menuDLG.findViewById(R.id.btnDelete);
        Button MenuEdit = menuDLG.findViewById(R.id.btnEdit);

        Subject sbj = subjlist.get(position);

        String code = sbj.getCode();
        String desc = sbj.getDescription();
        String subject = code + "-" + desc;

        MenuDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(SubjectList.this);

                ad.setTitle("Confirmation").setIcon(R.drawable.baste_logo).setMessage("Are you sure to delete this subject: " + subject + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteSubject(position);
//                                subjlist.remove(position);

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
                editStudent(position);
                menuDLG.dismiss();
            }
        });


    }

    private void editStudent(int position) {
        myDialog = new Dialog(SubjectList.this);
        myDialog.setContentView(R.layout.subject_registration_layout);
        myDialog.show();

        EditText SubjCode = myDialog.findViewById(R.id.etSubjCode);
        EditText SubjDesc = myDialog.findViewById(R.id.etSubjDesc);
        TextView tvCodeTitle = myDialog.findViewById(R.id.tv1);
        TextView tvCode = myDialog.findViewById(R.id.tvCode);
        TextView tvDescTitle = myDialog.findViewById(R.id.tv2);
        TextView tvDesc = myDialog.findViewById(R.id.tvDescription);
        TextView tvLayoutTitle = myDialog.findViewById(R.id.tvLayoutTitle);
        View line = myDialog.findViewById(R.id.view1);
        Button btnSubjAdd = myDialog.findViewById(R.id.btnAdd);
        Button btnSubjUpdate = myDialog.findViewById(R.id.btnSubjUpdate);

        Subject sbj = subjlist.get(position);

        int id = Integer.parseInt(String.valueOf(sbj.getId()));
        String code = sbj.getCode();
        String desc = sbj.getDescription();

        tvLayoutTitle.setVisibility(View.VISIBLE);
        btnSubjAdd.setVisibility(View.GONE);
        btnSubjUpdate.setVisibility(View.VISIBLE);

        SubjCode.setText(code);
        SubjDesc.setText(desc);

        btnSubjUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String codeTXT = SubjCode.getText().toString();
                String descTXT = SubjDesc.getText().toString();

                Subject subject = new Subject(id, codeTXT, descTXT);

                boolean SubjectUpdated = dbHelper.updateSubject(id, codeTXT, descTXT);

                if (SubjectUpdated == true) {
                    Snackbar.make(findViewById(R.id.rootSubj), "Subject Updated", Snackbar.LENGTH_SHORT).show();

                    subjlist.set(position, subject);
                    retrieveSubject();
                    myDialog.dismiss();


                } else
                    Snackbar.make(findViewById(R.id.rootSubj), "Error Updating Subject", Snackbar.LENGTH_SHORT).show();

            }
        });


    }

    private void viewSubject(int position) {
        myDialog = new Dialog(SubjectList.this);
        myDialog.setContentView(R.layout.subject_registration_layout);
        myDialog.show();

        EditText SubjCode = myDialog.findViewById(R.id.etSubjCode);
        EditText SubjDesc = myDialog.findViewById(R.id.etSubjDesc);
        TextView tvCodeTitle = myDialog.findViewById(R.id.tv1);
        TextView tvCode = myDialog.findViewById(R.id.tvCode);
        TextView tvDescTitle = myDialog.findViewById(R.id.tv2);
        TextView tvDesc = myDialog.findViewById(R.id.tvDescription);
        View line = myDialog.findViewById(R.id.view1);
        Button btnSubjAdd = myDialog.findViewById(R.id.btnAdd);
//        Button btnSubjUpdate = myDialog.findViewById(R.id.btnSubjUpdate);

        Subject sbj = subjlist.get(position);

        String code = sbj.getCode();
        String desc = sbj.getDescription();


        SubjCode.setVisibility(View.GONE);
        SubjDesc.setVisibility(View.GONE);
        btnSubjAdd.setVisibility(View.GONE);
        tvCodeTitle.setVisibility(View.VISIBLE);
        tvCode.setVisibility(View.VISIBLE);
        tvDescTitle.setVisibility(View.VISIBLE);
        tvDesc.setVisibility(View.VISIBLE);
        line.setVisibility(View.GONE);

        tvCode.setText(code);
        tvDesc.setText(desc);
    }

    private void retrieveSubject() {

        subjToOutput = new ArrayList<String>();
        subjlist = new ArrayList<>();

        sql = dbHelper.getWritableDatabase();
        Cursor c = sql.rawQuery(" Select * From " + dbHelper.TABLE_SUBJECTS + "", null);


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                int id = Integer.parseInt(c.getString(0));
                String code = c.getString(1);
                String description = c.getString(2);

                subjToOutput.add(code + " - " + description);
                subjlist.add(new Subject(id, code, description));
            }
        } else {
            Toast.makeText(this, "No Subject record..", Toast.LENGTH_SHORT).show();
        }
        c.close();
        sql.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(SubjectList.this, android.R.layout.simple_expandable_list_item_1, subjToOutput);
        adapter.notifyDataSetChanged();
        listviewSubj.setAdapter(adapter);

    }

    public void deleteSubject(int position) {
        Subject sbj = subjlist.get(position);
        String code = sbj.getCode();

        boolean SubjectDeleted = dbHelper.deleteSubject(code);

        if (SubjectDeleted == true) {
            retrieveSubject();
            Snackbar.make(findViewById(R.id.rootSubj), "Subject Deleted", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(findViewById(R.id.rootSubj), "Error Deleting", Snackbar.LENGTH_LONG).show();
        }

    }

    private void findView() {
        btnAdd = findViewById(R.id.floatingAdd);
        listviewSubj = findViewById(R.id.lvSubjects);
        btnBack = findViewById(R.id.btnBack);
    }
}