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
import android.media.Image;
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

public class TeachingLoad extends AppCompatActivity {
    ListView lvTeachLoad;
    FloatingActionButton btnAddLoad;
    ImageView btnBack;

    SQLiteDatabase sql;
    DATABASE_HELPER dbHelper;

    ArrayList<Teacher> tList;
    ArrayList<Subject> subjlist;

    ArrayList<TeacherProgram> programlist ;
    ArrayList<TeachLoad> teachLoadList ;
    ArrayList<String> LoadToOutput ;


    String ProfEmail;
    String SubjectCode;

    int fk_prof;
    int fk_subj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teaching_load);

        dbHelper = new DATABASE_HELPER(TeachingLoad.this);

        findView();
        retrieveLoad();
//        checkOnly();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TeachingLoad.this, AdminMenuPage.class);
                startActivity(i);
            }
        });
        btnAddLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog myDialog = new Dialog(TeachingLoad.this);
                myDialog.setContentView(R.layout.admin_teaching_load);
                myDialog.show();

                TextView chooseProf = myDialog.findViewById(R.id.tvProfChoose);
                TextView chooseSubj = myDialog.findViewById(R.id.tvSubjChoose);
                EditText etProf = myDialog.findViewById(R.id.etProfFK);
                EditText etSubj = myDialog.findViewById(R.id.etSubjFK);
                Button btnAddLoad = myDialog.findViewById(R.id.btnAddTeachLoad);

                etProf.setEnabled(false);
                etSubj.setEnabled(false);

                chooseProf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog profDlg = new Dialog(TeachingLoad.this);

                        ArrayList<String> ProfToOutput = new ArrayList<>();
                        ArrayList<Teacher> tList = new ArrayList<>();

                        profDlg.setContentView(R.layout.teacher_profile_page);
                        profDlg.show();

                        ImageView BaseLogo = profDlg.findViewById(R.id.ivLogo);
                        RecyclerView recyclerView = profDlg.findViewById(R.id.rv_profProfile);
                        ListView listviewProf = profDlg.findViewById(R.id.lvProf);
                        ImageView profUIBack = profDlg.findViewById(R.id.btnBack);

                        recyclerView.setVisibility(View.GONE);
                        listviewProf.setVisibility(View.VISIBLE);
                        BaseLogo.setVisibility(View.GONE);

                        profUIBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                profDlg.dismiss();
                            }
                        });

                        SQLiteDatabase sql = dbHelper.getWritableDatabase();
                        Cursor c = sql.rawQuery(" Select * From " + dbHelper.TABLE_PROFESSORS + "", null);

                        if (c.moveToFirst()) {
                            do {
                                int id = Integer.parseInt(c.getString(0));
                                byte[] image = c.getBlob(1);
                                String email = c.getString(2);
                                String position = c.getString(3);
                                String lname = c.getString(4);
                                String fname = c.getString(5);
                                String mname = c.getString(6);
                                String password = c.getString(7);

                                ProfToOutput.add(fname + " " + mname + ". " + lname);
                                Teacher model = new Teacher(id, image, email, position, lname, fname, mname, password);
                                tList.add(model);

                            } while (c.moveToNext());
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(TeachingLoad.this, android.R.layout.simple_expandable_list_item_1, ProfToOutput);
                            adapter.notifyDataSetChanged();
                            listviewProf.setAdapter(adapter);

                        }

                        listviewProf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                                profDlg.dismiss();
                                Teacher tchr = tList.get(position);

                                fk_prof = tchr.getId();
                                String lname = tchr.getLname();
                                String fname = tchr.getFname();
                                String mname = tchr.getMname();

                                etProf.setText(fname + " " + mname + ". " + lname);
                            }
                        });

                    }
                });

                chooseSubj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog subjDlg = new Dialog(TeachingLoad.this);

                        ArrayList<String> subjToOutput = new ArrayList<>();
                        ArrayList<Subject> subjlist = new ArrayList<>();

                        subjDlg.setContentView(R.layout.subject_list);
                        subjDlg.show();

                        ImageView BaseLogo = subjDlg.findViewById(R.id.basteLogo);
                        RecyclerView recyclerView = subjDlg.findViewById(R.id.rv_profProfile);
                        ListView listviewSubj = subjDlg.findViewById(R.id.lvSubjects);
                        ImageView subjUIBack = subjDlg.findViewById(R.id.btnBack);

                        BaseLogo.setVisibility(View.GONE);
                        subjUIBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                subjDlg.dismiss();
                            }
                        });

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
                            Toast.makeText(TeachingLoad.this, "No Subject record..", Toast.LENGTH_SHORT).show();
                        }
                        c.close();
                        sql.close();

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(TeachingLoad.this, android.R.layout.simple_expandable_list_item_1, subjToOutput);
                        adapter.notifyDataSetChanged();
                        listviewSubj.setAdapter(adapter);

                        listviewSubj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                                subjDlg.dismiss();
                                Subject sbj = subjlist.get(position);

                                fk_subj = sbj.getId();
                                String code = sbj.getCode();
                                String desc = sbj.getDescription();
                                etSubj.setText(code + " - " + desc);

                                SubjectCode = code;
                            }
                        });
                    }
                });

                btnAddLoad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        boolean checkProf = dbHelper.checkProfID(fk_prof);
                        boolean checkSubj = dbHelper.checkSubjID(fk_subj);
//                        boolean checkforDuplication = dbHelper.checkTeachingLoadForDuplication(fk_prof, fk_subj);

                        if (checkProf == false && checkSubj == false) {

                            boolean LoadInserted = dbHelper.insertTeachingLoad(fk_prof, fk_subj);
                            if (LoadInserted == true) {
                                String prof = etProf.getText().toString();
                                String subject = etSubj.getText().toString();

//                            programlist.add(new TeacherProgram(prof, subject));
                                retrieveLoad();
//                            LoadToOutput.add( prof + "  |  " + subject);

                                myDialog.dismiss();
                                Snackbar.make(findViewById(R.id.rootLoad), "Teaching Load Added.", Snackbar.LENGTH_SHORT).show();

                            } else {
                                Snackbar.make(findViewById(R.id.rootLoad), "Error Adding Teaching Load.", Snackbar.LENGTH_SHORT).show();
                            }


                        } else {
                            myDialog.dismiss();
                            Snackbar.make(findViewById(R.id.rootLoad), "Professor or Subject is existing. You cannot duplicate it.", Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });


        lvTeachLoad.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                ShowMenu(position);
                return true;
            }
        });
    }

    private void retrieveLoad() {

        LoadToOutput = new ArrayList<String>();
        programlist = new ArrayList<>();
        teachLoadList = new ArrayList<>();

        sql = dbHelper.getWritableDatabase();
        String query = " SELECT " + dbHelper.TEACHLOAD_ID_COLUMN + " , " + dbHelper.PROF_ID_COLUMN + " ," + dbHelper.PROF_FNAME_COLUMN + " , " + dbHelper.PROF_MNAME_COLUMN
                + " , " + dbHelper.PROF_LNAME_COLUMN + " , " + dbHelper.SUBJ_ID_COLUMN + " , " + dbHelper.SUBJ_CODE_COLUMN + " , " + dbHelper.SUBJ_DESC_COLUMN
                + " FROM " + dbHelper.TABLE_TEACHLOAD
                + " JOIN " + dbHelper.TABLE_PROFESSORS
                + " ON " + dbHelper.FK_PROF_COLUMN + " = " + dbHelper.PROF_ID_COLUMN
                + " JOIN " + dbHelper.TABLE_SUBJECTS
                + " ON " + dbHelper.FK_SUBJ_COLUMN + " = " + dbHelper.SUBJ_ID_COLUMN;

        Cursor c = sql.rawQuery(query, null);

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
                LoadToOutput.add(id + ") "+ Prof + "  |  " + Subject);
                teachLoadList.add(new TeachLoad(id, p_id, s_id));
//                TeachLoad model = new TeachLoad(id, p_id, s_id);
//                teachLoadList.add(model);
                programlist.add(new TeacherProgram(id, Prof, Subject));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(TeachingLoad.this, android.R.layout.simple_expandable_list_item_1, LoadToOutput);
            adapter.notifyDataSetChanged();
            lvTeachLoad.setAdapter(adapter);

        }
//
//        Cursor c = sql.rawQuery(" SELECT " + "t." + dbHelper.TEACHLOAD_ID_COLUMN + "p." +dbHelper.PROF_FNAME_COLUMN
//                       + " FROM " + dbHelper.TABLE_TEACHLOAD + " t "
//                        + " JOIN " + dbHelper.TABLE_PROFESSORS + " p "
//                        + " ON t." + dbHelper.FK_PROF_COLUMN + " = p." + dbHelper.PROF_ID_COLUMN );
//        Cursor c = sql.rawQuery(" Select " +dbHelper.TABLE_PROFESSORS+" . "+dbHelper.PROF_ID_COLUMN+ " AS prof name From "     )

//        sql = dbHelper.getWritableDatabase();
//        Cursor c = sql.rawQuery(" Select * From " + dbHelper.TABLE_TEACHLOAD + "", null);
//
//        if (c.getCount() > 0) {
//            while (c.moveToNext()) {
//                int id = Integer.parseInt(c.getString(0));
//                int prof_id = Integer.parseInt(c.getString(1));
//                int sub_id = Integer.parseInt(c.getString(2));
//                LoadToOutput.add(prof_id + " - " + sub_id);
//                teachLoadList.add(new TeachLoad(id, prof_id, sub_id));
////                getProfandSubj(prof_id, sub_id);
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(TeachingLoad.this, android.R.layout.simple_expandable_list_item_1, LoadToOutput);
//                adapter.notifyDataSetChanged();
//                lvTeachLoad.setAdapter(adapter);
//            }
//        } else {
//            Toast.makeText(this, "No Teaching Load record..", Toast.LENGTH_SHORT).show();
//        }
//        c.close();
//        sql.close();

    }

    //    private void getProfandSubj(int prof_id, int subj_id){
//        SQLiteDatabase sql = dbHelper.getWritableDatabase();
//        Cursor c = sql.rawQuery(" Select * From " + dbHelper.TABLE_PROFESSORS + " where prof_id = ?", new String[]{Integer.toString(prof_id)});
//        Cursor cursor = sql.rawQuery(" Select * From " + dbHelper.TABLE_SUBJECTS + " where subj_id = ?", new String[]{Integer.toString(subj_id)});
//        if (c.getCount() > 0){
////            prof_id = Integer.parseInt(c.getString(0));
//            int id = Integer.parseInt(c.getString(0));
//            byte[] image = c.getBlob(1);
//            String email = c.getString(2);
//            String position = c.getString(3);
//            String lname = c.getString(4);
//            String fname = c.getString(5);
//            String mname = c.getString(6);
//            String password = c.getString(7);
//            String name = fname + " " + mname + ". "+lname;
//        }
//
//        if (cursor.getCount() > 0 ){
//            String code = cursor.getString(1);
//            String desc = cursor.getString(2);
//
//            String subj = code + " - " + desc;
//        }
//
//        TeacherProgram model = new TeacherProgram(name, subj);
//        programlist.add(model);
//        LoadToOutput.add(name + "  |  " + subj);
//
//
//    }
    private void ShowMenu(int position) {
        Dialog menuDLG = new Dialog(TeachingLoad.this);
        menuDLG.setContentView(R.layout.floating_menu);
        menuDLG.show();

//        teachLoadList = new ArrayList<>();

        Button MenuDelete = menuDLG.findViewById(R.id.btnDelete);
        Button MenuEdit = menuDLG.findViewById(R.id.btnEdit);
        MenuEdit.setVisibility(View.GONE);
//        TeacherProgram tL = programlist.get(position);
        TeachLoad tLd = teachLoadList.get(position);

        int id = Integer.parseInt(String.valueOf(tLd.getLoad_ID()));

        MenuDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(TeachingLoad.this);

                ad.setTitle("Confirmation").setIcon(R.drawable.baste_logo).setMessage("Are you sure to delete this load with an id of: " + id + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteLoad(position);
//
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
    }

    public void deleteLoad(int position) {
        TeachLoad tL = teachLoadList.get(position);

        int id = Integer.parseInt(String.valueOf(tL.getLoad_ID()));

        boolean LoadDeleted = dbHelper.deleteLoad(id);

        if (LoadDeleted == true) {
            retrieveLoad();
            Snackbar.make(findViewById(R.id.rootLoad), "Load Deleted", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(findViewById(R.id.rootLoad), "Error Deleting", Snackbar.LENGTH_LONG).show();
        }

    }

    private void checkOnly() {
        DATABASE_HELPER dbHelper = new DATABASE_HELPER(TeachingLoad.this);
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        Cursor c = sql.rawQuery(" Select * From " + dbHelper.TABLE_TEACHLOAD + "", null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {

                String profID = c.getString(1);
                String subjID = c.getString(2);

                Toast.makeText(this, "prof: " + profID + ",  subj: " + subjID, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No professor record..", Toast.LENGTH_SHORT).show();
        }
    }

    private void findView() {

        lvTeachLoad = findViewById(R.id.lvTeachingLoad);
        btnAddLoad = findViewById(R.id.floatingAddLoad);
        btnBack = findViewById(R.id.btnBack);
    }

}