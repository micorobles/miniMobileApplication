package com.example.e_val;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EvaluatedPage extends AppCompatActivity {
    DATABASE_HELPER dbHelper = new DATABASE_HELPER(EvaluatedPage.this);
    SQLiteDatabase sql;

    RecyclerView results;

//    ArrayList<String> profs = new ArrayList<>();
//    ArrayList<String> subjects = new ArrayList<>();
//    ArrayList<Float> aveScore = new ArrayList<>();
//    ArrayList<Float> aveEffort = new ArrayList<>();
//    ArrayList<String> comments = new ArrayList<>();

    ArrayList<Evaluated> resultList = new ArrayList<>();
    EvaluatedAdapter adapter = new EvaluatedAdapter(resultList);
    LinearLayoutManager layout = new LinearLayoutManager(this);

    String studName;
    String Call_Prof;
    int program_id;
    int studID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluated_page);

        results = (RecyclerView) findViewById(R.id.rv_Results);

        Intents();
//        getProfAndSubj();
        getResult();
//        Toast.makeText(this, ""+studID, Toast.LENGTH_SHORT).show();

//        EvaluatedAdapter adapter = new EvaluatedAdapter(profs, subjects);
//        results.setAdapter(adapter);
//        LinearLayoutManager layout = new LinearLayoutManager(this);
//        results.setLayoutManager(layout);

//        profs.add("Tony Co");
//        profs.add("Nimfa Taupo");
//        subjects.add("Filipino");
//        subjects.add("Science");

//        results.setLayoutManager(new LinearLayoutManager(this));

    }

    private void getResult() {

        sql = dbHelper.getWritableDatabase();
        String query = " SELECT " + dbHelper.EVALUATION_ID_COLUMN + " , " + dbHelper.STUDENT_ID_COLUMN + " ," + dbHelper.TEACHLOAD_ID_COLUMN + " ," + dbHelper.FK_PROF_COLUMN
                + ", " + dbHelper.PROF_IMAGE_COLUMN + ", " + dbHelper.PROF_FNAME_COLUMN + " , " + dbHelper.PROF_MNAME_COLUMN + " , " + dbHelper.PROF_LNAME_COLUMN
                + ", " + dbHelper.FK_SUBJ_COLUMN + " , " + dbHelper.SUBJ_CODE_COLUMN + ", " + dbHelper.SUBJ_DESC_COLUMN
                + ", " + dbHelper.AVERAGE_SCORE_COLUMN + " , " + dbHelper.AVERAGE_EFFORT_COLUMN + " , " + dbHelper.COMMENT_COLUMN
                + " FROM " + dbHelper.TABLE_EVALUATION
                + " JOIN " + dbHelper.TABLE_STUDENTS
                + " ON " + dbHelper.FK_STUD_COLUMN + " = " + dbHelper.STUDENT_ID_COLUMN
                + " JOIN " + dbHelper.TABLE_TEACHLOAD
                + " ON " + dbHelper.FK_TEACHLOAD_COLUMN + " = " + dbHelper.TEACHLOAD_ID_COLUMN
                + " JOIN " + dbHelper.TABLE_PROFESSORS
                + " ON " + dbHelper.FK_PROF_COLUMN + " = " + dbHelper.PROF_ID_COLUMN
                + " JOIN " + dbHelper.TABLE_SUBJECTS
                + " ON " + dbHelper.FK_SUBJ_COLUMN + " = " + dbHelper.SUBJ_ID_COLUMN
                + " WHERE " + dbHelper.FK_STUD_COLUMN + " = ? ";

        Cursor c = sql.rawQuery(query, new String[]{Integer.toString(studID)});

        if (c.getCount() > 0) {

            c.moveToFirst();

            while (!c.isAfterLast()) {
                int eval_id = Integer.parseInt(String.valueOf(c.getString(0)));
                int stud_id = Integer.parseInt(String.valueOf(c.getString(1)));
                int teachLoad_id = Integer.parseInt(String.valueOf(c.getString(2)));
                int prof_id = Integer.parseInt(String.valueOf(c.getString(3)));
                byte[] prof_image = c.getBlob(4);
                String prof_fname = c.getString(5);
                String prof_mname = c.getString(6);
                String prof_lname = c.getString(7);
                int subj_id = Integer.parseInt(String.valueOf(c.getString(8)));
                String subj_code = c.getString(9);
                String subj_desc = c.getString(10);
                float aveScore = c.getFloat(11);
                float aveEffort = c.getFloat(12);
                String comment = c.getString(13);

                String prof_fullname = prof_fname + " " + prof_mname + ". " + prof_lname;
                String subj_name = subj_code + " - " + subj_desc;
                Bitmap bitmap = BitmapFactory.decodeByteArray(prof_image, 0, prof_image.length);

                c.moveToNext();

                Evaluated model = new Evaluated(eval_id, stud_id, teachLoad_id, prof_id, prof_image, prof_fullname, subj_id, subj_name, aveScore, aveEffort, comment);
                resultList.add(model);

                results.setAdapter(adapter);
                results.setLayoutManager(layout);
            }
        } else {
            Snackbar.make(findViewById(R.id.rootEvaluated), "No evaluations yet..", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void getProfAndSubj() {
        sql = dbHelper.getWritableDatabase();
        String query = " SELECT " + dbHelper.TEACHLOAD_ID_COLUMN + " , " + dbHelper.PROF_ID_COLUMN + " ," + dbHelper.PROF_FNAME_COLUMN + " , " + dbHelper.PROF_MNAME_COLUMN
                + " , " + dbHelper.PROF_LNAME_COLUMN + " , " + dbHelper.SUBJ_ID_COLUMN + " , " + dbHelper.SUBJ_CODE_COLUMN + " , " + dbHelper.SUBJ_DESC_COLUMN
                + " FROM " + dbHelper.TABLE_TEACHLOAD
                + " JOIN " + dbHelper.TABLE_PROFESSORS
                + " ON " + dbHelper.FK_PROF_COLUMN + " = " + dbHelper.PROF_ID_COLUMN
                + " JOIN " + dbHelper.TABLE_SUBJECTS
                + " ON " + dbHelper.FK_SUBJ_COLUMN + " = " + dbHelper.SUBJ_ID_COLUMN
                + " where " +
                "( " + dbHelper.PROF_FNAME_COLUMN + " || ' ' || " + dbHelper.PROF_MNAME_COLUMN + " || '. ' || " + dbHelper.PROF_LNAME_COLUMN + " ) = ? ";

        Cursor c = sql.rawQuery(query, new String[]{Call_Prof});

        if (c.getCount() > 0) {

            Toast.makeText(this, "true!", Toast.LENGTH_SHORT).show();
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
//                LoadToOutput.add(id + " ) "+ Prof + "  |  " + Subject);
//                teachLoadList.add(new TeachLoad(id, p_id, s_id));
////                TeachLoad model = new TeachLoad(id, p_id, s_id);
////                teachLoadList.add(model);
//                programlist.add(new TeacherProgram(id, Prof, Subject));
            }

//            ArrayAdapter<String> adapter = new ArrayAdapter<>(TeachingLoad.this, android.R.layout.simple_expandable_list_item_1, LoadToOutput);
//            adapter.notifyDataSetChanged();
//            lvTeachLoad.setAdapter(adapter);

        }
    }

    public void Intents() {
        studName = getIntent().getStringExtra("studName");
        program_id = getIntent().getIntExtra("programID", 0);
        Call_Prof = getIntent().getStringExtra("profName");
        studID = getIntent().getIntExtra("studID", 0);
    }

    public void GoBack(View view) {
        Intent i = new Intent(EvaluatedPage.this, HomePage.class);
        i.putExtra("studName", studName);
        i.putExtra("programID", program_id);
        i.putExtra("pkProf", Call_Prof);
        i.putExtra("studID", studID);
        startActivity(i);
    }

    public void evaluateAgain(View view) {
        Intent i = new Intent(EvaluatedPage.this, HomePage.class);
        i.putExtra("studName", studName);
        i.putExtra("programID", program_id);
        i.putExtra("pkProf", Call_Prof);
        i.putExtra("studID", studID);
        startActivity(i);
    }
}