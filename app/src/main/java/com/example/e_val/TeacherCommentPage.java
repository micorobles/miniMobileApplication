package com.example.e_val;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class TeacherCommentPage extends AppCompatActivity {

    DATABASE_HELPER dbHelper = new DATABASE_HELPER(this);
    SQLiteDatabase sql;
    RecyclerView rvComments;
    ArrayList<Comments> commentList = new ArrayList<>();
    TeacherCommentAdapter adapter =  new TeacherCommentAdapter(commentList);


    String profEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacer_comment_page);

        rvComments = findViewById(R.id.rvComments);
        profEmail = getIntent().getStringExtra("profEmail");
        retrieveComments();

    }


    private void retrieveComments(){
        sql = dbHelper.getWritableDatabase();
        String query = " SELECT " + dbHelper.STUDENT_ID_COLUMN + " , " + dbHelper.TEACHLOAD_ID_COLUMN + " ," + dbHelper.PROF_ID_COLUMN + " , " + dbHelper.PROF_IMAGE_COLUMN
                + " , " + dbHelper.PROF_FNAME_COLUMN + " , " + dbHelper.PROF_MNAME_COLUMN + " , " + dbHelper.PROF_LNAME_COLUMN + " , " + dbHelper.SUBJ_CODE_COLUMN
                + " , " + dbHelper.SUBJ_DESC_COLUMN + " , " + dbHelper.AVERAGE_SCORE_COLUMN + " , " + dbHelper.AVERAGE_EFFORT_COLUMN + " , " + dbHelper.COMMENT_COLUMN
                + " FROM " + dbHelper.TABLE_EVALUATION
                + " JOIN " + dbHelper.TABLE_STUDENTS
                + " ON " + dbHelper.FK_STUD_COLUMN + " = " + dbHelper.STUDENT_ID_COLUMN
                + " JOIN " + dbHelper.TABLE_TEACHLOAD
                + " ON " + dbHelper.FK_TEACHLOAD_COLUMN + " = " + dbHelper.TEACHLOAD_ID_COLUMN
                + " JOIN " + dbHelper.TABLE_PROFESSORS
                + " ON " + dbHelper.FK_PROF_COLUMN + " = " + dbHelper.PROF_ID_COLUMN
                + " JOIN " + dbHelper.TABLE_SUBJECTS
                + " ON " + dbHelper.FK_SUBJ_COLUMN + " = " + dbHelper.SUBJ_ID_COLUMN
                + " WHERE " + dbHelper.PROF_EMAIL_COLUMN + "  = ? ";
//                + " WHERE email = ? ";

        Cursor c = sql.rawQuery(query, new String[]{profEmail});

        if (c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String student_comments = c.getString(11);
                c.moveToNext();

                Comments model = new Comments(student_comments);
                commentList.add(model);
            }
            c.close();


        } else {
            Toast.makeText(this, "DI NAGANA", Toast.LENGTH_SHORT).show();
        }

        int screenHeight = 600;
        int itemHeight = screenHeight / 3; // Calculate item height

//        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
//         adapter = new TeacherCommentAdapter(commentList, itemHeight);

        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new GridLayoutManager(this, 2));
//        rvComments.setLayoutManager(layoutManager);


    }

    public void GoBack(View view) {
        Intent i = new Intent(TeacherCommentPage.this, TeacherInterfacePage.class);
        i.putExtra("profEmail", profEmail);
        startActivity(i);
    }
}