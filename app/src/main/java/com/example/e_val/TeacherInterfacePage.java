package com.example.e_val;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

public class TeacherInterfacePage extends AppCompatActivity {

    DATABASE_HELPER dbHelper = new DATABASE_HELPER(TeacherInterfacePage.this);
    SQLiteDatabase sql;
    TextView name, subj;
    RatingBar OvrAllScore, OvrAllEffort;
    ShapeableImageView image;

    String profEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_interface_page);

        findView();
        profEmail = getIntent().getStringExtra("profEmail");

        displayDetails();

        Toast.makeText(this, profEmail, Toast.LENGTH_SHORT).show();
    }

    public void displayDetails(){

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

        Float aveScoresSum = 0.0F;
        Float aveEfforsSum = 0.0F;
        int EvaluationCount = c.getCount();


        if (c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                int student_id = Integer.parseInt(String.valueOf(c.getString(0)));
                int teachload_id = Integer.parseInt(String.valueOf(c.getString(1)));
                int prof_id = Integer.parseInt(String.valueOf(c.getString(2)));
                byte[] profImage = c.getBlob(3);
                String profFname = c.getString(4);
                String profMname = c.getString(5);
                String profLname = c.getString(6);
                String subjCode = c.getString(7);
                String subjDesc = c.getString(8);
                Float student_aveScores = c.getFloat(9);
                Float student_aveEfforts = c.getFloat(10);
                String student_comments = c.getString(11);

                c.moveToNext();

                String profFullName = profFname + " " + profMname + ". " + profLname;
                String SubjectFullName = subjCode + " - " + subjDesc;
                Bitmap bitmap = BitmapFactory.decodeByteArray(profImage, 0, profImage.length);

                aveScoresSum += student_aveScores;

                aveEfforsSum += student_aveEfforts;

                name.setText(profFullName);
                subj.setText(SubjectFullName);
                image.setImageBitmap(bitmap);

            }
            c.close();

            float OvrAllScoreResult = EvaluationCount > 0 ? aveScoresSum / EvaluationCount : 0;
            float OvrAllEffortResult = EvaluationCount > 0 ? aveEfforsSum / EvaluationCount : 0;

            OvrAllScore.setRating(OvrAllScoreResult);
            OvrAllEffort.setRating(OvrAllEffortResult);
            Toast.makeText(this, ""+OvrAllScoreResult+ ", "+OvrAllEffortResult+", "+EvaluationCount, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "DI NAGANA", Toast.LENGTH_SHORT).show();
        }

    }

    private float computeAverage(float percentage) {

        float answer = 0.0F;
        if (percentage >= 10.0F && percentage < 19.0F) {
//            AveScoreStar = 0.5F;
            answer = 0.5F;
        } else if (percentage >= 20.0F && percentage < 29.0F) {
//            AveScoreStar = 1.0F;
            answer = 1.0F;
        } else if (percentage >= 30.0F && percentage < 39.0F) {
//            AveScoreStar = 1.5F;
            answer = 1.5F;
        } else if (percentage >= 40.0F && percentage < 49.0F) {
//            AveScoreStar = 2.0F;
            answer = 2.0F;
        } else if (percentage >= 50.0F && percentage < 59.0F) {
//            AveScoreStar = 2.5F;
            answer = 2.5F;
        } else if (percentage >= 60.0F && percentage < 69.0F) {
//            AveScoreStar = 3.0F;
            answer = 3.0F;
        } else if (percentage >= 70.0F && percentage < 79.0F) {
//            AveScoreStar = 3.5F;
            answer = 3.5F;
        } else if (percentage >= 80.0F && percentage < 89.0F) {
//            AveScoreStar = 4.0F;
            answer = 4.0F;
        } else if (percentage >= 90.0F && percentage < 99.0F) {
//            AveScoreStar = 4.5F;
            answer = 4.5F;
        } else if (percentage == 100.0F) {
//            AveScoreStar = 5.0F;
            answer = 5.0F;
        }
        return answer;
    }

    public void seeComments(View view) {
        Intent i = new Intent(TeacherInterfacePage.this, TeacherCommentPage.class);
        i.putExtra("profEmail", profEmail);
        startActivity(i);
    }

    public void GoBack(View view) {

        AlertDialog.Builder b = new AlertDialog.Builder(TeacherInterfacePage.this);

        b.setTitle("Warning!")
                .setIcon(R.drawable.baste_logo)
                .setMessage("Are you sure you want to logout? \n")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(TeacherInterfacePage.this, TeacherLoginPage.class);
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

    private void findView() {

        name = findViewById(R.id.tvProf);
        subj = findViewById(R.id.tvSubject);
        OvrAllScore = findViewById(R.id.rbAveScore);
        OvrAllEffort = findViewById(R.id.rbAveEffort);
        image = findViewById(R.id.ivProfIMG);

    }
}