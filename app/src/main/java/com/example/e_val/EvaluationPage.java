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
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class EvaluationPage extends AppCompatActivity {
    DATABASE_HELPER dbHelper = new DATABASE_HELPER(EvaluationPage.this);
    SQLiteDatabase sql;

    TextView tv_ProfName;
    RatingBar score1, score2, score3, score4, score5, effort1, effort2, effort3, effort4, effort5;
    EditText et_Comment;
    Button btn_Submit;
    ShapeableImageView profImg;

    String studName;
    String Call_Prof;
    int program_id;
    int studentID;

    ArrayList<TeachLoad> tl_list = new ArrayList<>();
    float scrRating1, scrRating2, scrRating3, scrRating4, scrRating5, efrtRating1, efrtRating2, efrtRating3, efrtRating4, efrtRating5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluation_page);

        findView();
        displayNameAndImgAndIntents();
//        profImg.setImageBitmap(profImgProfile);

        score1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                scrRating1 = score1.getRating();
            }
        });

        score2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                scrRating2 = score2.getRating();
            }
        });

        score3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                scrRating3 = score3.getRating();
            }
        });

        score4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                scrRating4 = score4.getRating();
            }
        });

        score5.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                scrRating5 = score5.getRating();
            }
        });

        effort1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                efrtRating1 = effort1.getRating();
            }
        });

        effort2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                efrtRating2 = effort2.getRating();
            }
        });

        effort3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                efrtRating3 = effort3.getRating();
            }
        });

        effort4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                efrtRating4 = effort4.getRating();
            }
        });

        effort5.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                efrtRating5 = effort5.getRating();
            }
        });

        et_Comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                float ScoreSum = scrRating1 + scrRating2 + scrRating3 + scrRating4 + scrRating5;
                float ScorePercentage = (ScoreSum / 25F) * 100F;
                float averageScore = computeAverage(ScorePercentage);

                float EffortSum = efrtRating1 + efrtRating2 + efrtRating3 + efrtRating4 + efrtRating5;
                float EffortPercentage = (EffortSum / 25F) * 100F;
                float averageEffort = computeAverage(EffortPercentage);
                int studID = getStudId();
                int loadID = program_id;

                String comment = et_Comment.getText().toString();

                if (scrRating1 == 0.0F || scrRating2 == 0.0F || scrRating3 == 0.0F || scrRating4 == 0.0F || scrRating5 == 0.0F ||
                        efrtRating1 == 0.0F || efrtRating2 == 0.0F || efrtRating3 == 0.0F || efrtRating4 == 0.0F || efrtRating5 == 0.0F ||
                        comment.isEmpty()) {

//                    Toast.makeText(EvaluationPage.this, "stud id: "+ getStudId() + ", program id: "+loadID, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(EvaluationPage.this, "TeachLoad List: "+tl_list, Toast.LENGTH_SHORT).show();
                    Toast.makeText(EvaluationPage.this, "Please complete the requirements.", Toast.LENGTH_SHORT).show();
                } else {

                    boolean Evaluated = dbHelper.insertEvaluation(studID, loadID, averageScore, averageEffort, comment);

                    if (Evaluated) {
                        Toast.makeText(EvaluationPage.this, "Evaluation Submitted", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(EvaluationPage.this, EvaluatedPage.class);
                        i.putExtra("studName", studName);
                        i.putExtra("programID", program_id);
                        i.putExtra("profName", Call_Prof);
                        i.putExtra("studID", studentID);
                        startActivity(i);
                    } else {
                        Snackbar.make(findViewById(R.id.rootEvaluation), "Error Submitting Evaluation!", Snackbar.LENGTH_SHORT).show();
                    }
                }

//                Toast.makeText(EvaluationPage.this, "Comment: " + comment, Toast.LENGTH_SHORT).show();
//                Toast.makeText(EvaluationPage.this, "total rating: " + EffortSum + ", percentage:  " + EffortPercentage + ", average " + averageEffort, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void displayNameAndImgAndIntents() {

        studName = getIntent().getStringExtra("studName");
        program_id = getIntent().getIntExtra("programID", 0);
        Call_Prof = getIntent().getStringExtra("pkProf");
        studentID = getIntent().getIntExtra("studID", 0);
//        Toast.makeText(this, ""+studentID, Toast.LENGTH_SHORT).show();

        tv_ProfName.setText(Call_Prof);

        sql = dbHelper.getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + dbHelper.TABLE_PROFESSORS + " where " +
                "( "+ dbHelper.PROF_FNAME_COLUMN + " || ' ' || "+ dbHelper.PROF_MNAME_COLUMN + " || '. ' || "+ dbHelper.PROF_LNAME_COLUMN + " )= ?", new String[]{Call_Prof});

        if (c.getCount() >= 0) {
            while (c.moveToNext()) {

                byte[] image = c.getBlob(1);
//                String email = c.getString(2);
//                String position = c.getString(3);
//                String lname = c.getString(4);
//                String fname = c.getString(5);
//                String mname = c.getString(6);
//                String password = c.getString(7);

                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                profImg.setImageBitmap(bitmap);

            }
        }
    }


    private int getStudId() {

        sql = dbHelper.getWritableDatabase();

        Cursor c = sql.rawQuery("select * from " + dbHelper.TABLE_STUDENTS + " where " + dbHelper.STUDENT_FNAME_COLUMN + " = ?", new String[]{studName});

        int id = 0;
        if (c.getCount() >= 0) {
            while (c.moveToNext()) {
                id = Integer.parseInt(c.getString(0));

            }
        }
        return id;
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

    private void findView() {
        et_Comment = (EditText) findViewById(R.id.etComment);
        tv_ProfName = (TextView) findViewById(R.id.tvProfName);
        score1 = (RatingBar) findViewById(R.id.score1);
        score2 = (RatingBar) findViewById(R.id.score2);
        score3 = (RatingBar) findViewById(R.id.score3);
        score4 = (RatingBar) findViewById(R.id.score4);
        score5 = (RatingBar) findViewById(R.id.score5);
        effort1 = (RatingBar) findViewById(R.id.effort1);
        effort2 = (RatingBar) findViewById(R.id.effort2);
        effort3 = (RatingBar) findViewById(R.id.effort3);
        effort4 = (RatingBar) findViewById(R.id.effort4);
        effort5 = (RatingBar) findViewById(R.id.effort5);
        btn_Submit = (Button) findViewById(R.id.btnSubmit);
        profImg = (ShapeableImageView) findViewById(R.id.ivProfImg);
    }

    public void GoBack(View view) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(EvaluationPage.this);
        dlg.setTitle("Warning")
                .setIcon(R.drawable.baste_logo)
                .setMessage("Are you sure to cancel evaluation? rating will not be submitted.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(EvaluationPage.this, HomePage.class);
                        intent.putExtra("studName", studName);
                        intent.putExtra("studID", studentID);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();


    }

    public void ViewProfProfile(View view) {
//        Toast.makeText(this, "tangina", Toast.LENGTH_SHORT).show();
        Dialog myDialog = new Dialog(EvaluationPage.this);
        myDialog.setContentView(R.layout.clicked_profile_layout);
        myDialog.show();

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


        sql = dbHelper.getWritableDatabase();

        Cursor c = sql.rawQuery("select * from " + dbHelper.TABLE_PROFESSORS + " where " +
                "( "+ dbHelper.PROF_FNAME_COLUMN + " || ' ' || "+ dbHelper.PROF_MNAME_COLUMN + " || '. ' || "+ dbHelper.PROF_LNAME_COLUMN + " )= ?", new String[]{Call_Prof});

        if (c.getCount() >= 0) {
            while (c.moveToNext()) {

                byte[] image = c.getBlob(1);
                String email = c.getString(2);
                String position = c.getString(3);
                String lname = c.getString(4);
                String fname = c.getString(5);
                String mname = c.getString(6);
                String password = c.getString(7);

                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
//                profImgProfile = bitmap;

                String fullName = lname + ", " + fname + " " + mname;

                fileProfImg.setImageBitmap(bitmap);

                tvName.setText("Professor Full Name"); //tv title
                fileProfFullName.setText(fullName);  //tv description

                tvNum.setVisibility(View.GONE);
                fileStudNum.setVisibility(View.GONE);

                tvEmail.setText("Professor Email");
                fileProfEmail.setText(email);

                tvPosition.setVisibility(View.VISIBLE);
                fileProfPosition.setVisibility(View.VISIBLE);
                tvPosition.setText("Professor Position");
                fileProfPosition.setText(position);

                tvPass.setVisibility(View.GONE);
                fileProfPass.setVisibility(View.GONE);

            }
        }
        iv_FileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
    }

    private String getProfessorAttr() {

        sql = dbHelper.getWritableDatabase();
        String query = " SELECT " + dbHelper.TEACHLOAD_ID_COLUMN + " , " + dbHelper.PROF_ID_COLUMN + " ," + dbHelper.PROF_FNAME_COLUMN + " , " + dbHelper.PROF_MNAME_COLUMN
                + " , " + dbHelper.PROF_LNAME_COLUMN + " , " + dbHelper.SUBJ_ID_COLUMN + " , " + dbHelper.SUBJ_CODE_COLUMN + " , " + dbHelper.SUBJ_DESC_COLUMN
                + " FROM " + dbHelper.TABLE_TEACHLOAD
                + " JOIN " + dbHelper.TABLE_PROFESSORS
                + " ON " + dbHelper.FK_PROF_COLUMN + " = " + dbHelper.PROF_ID_COLUMN
                + " JOIN " + dbHelper.TABLE_SUBJECTS
                + " ON " + dbHelper.FK_SUBJ_COLUMN + " = " + dbHelper.SUBJ_ID_COLUMN;

        Cursor c = sql.rawQuery(query, null);

        String Prof = null;
        if (c.getCount() > 0) {
            c.moveToFirst();

            while (!c.isAfterLast()) {
                int id = Integer.parseInt(String.valueOf(c.getString(0)));
                int p_id = Integer.parseInt(String.valueOf(c.getString(1)));
                String profFname = c.getString(2);
                String profMname = c.getString(3);
                String profLname = c.getString(4);
                int s_id = Integer.parseInt(String.valueOf(c.getString(5)));
                String subjCode = c.getString(6);
                String subjDesc = c.getString(7);

                c.moveToNext();
                Prof = profFname;

//                LoadToOutput.add(id + " ) "+ Prof + "  |  " + Subject);
//                teachLoadList.add(new TeachLoad(id, p_id, s_id));
//                TeachLoad model = new TeachLoad(id, p_id, s_id);
//                teachLoadList.add(model);
//                programlist.add(new TeacherProgram(id, Prof, Subject));
            }
        }
        return Prof;
    }
}