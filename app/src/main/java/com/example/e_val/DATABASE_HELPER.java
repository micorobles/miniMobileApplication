package com.example.e_val;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.database.CursorWindowCompat;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class DATABASE_HELPER extends SQLiteOpenHelper {
    public static final String DBNAME = "E-val.db";
    public final String TABLE_ADMIN = "admin";
    public final String TABLE_STUDENTS = "students";
    public final String STUDENT_ID_COLUMN = "stud_id";
    public final String STUDENT_FNAME_COLUMN = "firstName";
    public final String STUDENT_LNAME_COLUMN = "lastName";
    public final String STUDENT_MNAME_COLUMN = "middleName";
    public final String STUDENT_ISCLEARED_COLUMN = "isCleared";

    public final String TABLE_PROFESSORS = "professors";
    public final String PROF_ID_COLUMN = "prof_id";
    public final String PROF_IMAGE_COLUMN = "pimage";
    public final String PROF_POSITION_COLUMN = "pPosition";
    public final String PROF_EMAIL_COLUMN = "pemail";
    public final String PROF_LNAME_COLUMN = "pLname";
    public final String PROF_FNAME_COLUMN = "pFname";
    public final String PROF_MNAME_COLUMN = "mName";

    public final String TABLE_SUBJECTS = "subjects";
    public final String SUBJ_ID_COLUMN = "subj_id";
    public final String SUBJ_CODE_COLUMN = "code";
    public final String SUBJ_DESC_COLUMN = "description";

    public final String TABLE_TEACHLOAD = "teachload";
    public final String TEACHLOAD_ID_COLUMN = "teachload_id";
    public final String FK_PROF_COLUMN = "fk_prof";
    public final String FK_SUBJ_COLUMN = "fk_subj";

    public final String TABLE_EVALUATION = "evaluation";
    public final String EVALUATION_ID_COLUMN = "evaluation_id";
    public final String FK_STUD_COLUMN = "fk_stud";
    public final String FK_TEACHLOAD_COLUMN = "fk_teachload";
    public final String AVERAGE_SCORE_COLUMN = "averageScore";
    public final String AVERAGE_EFFORT_COLUMN = "averageEffort";
    public final String COMMENT_COLUMN = "comment";

    public final String TABLE_STUDENT_STATUS = "studStatus";
    public final String STATUS_ID = "status_id";
    public final String STATUS_FK_STUDENT = "status_fkStud";
    public final String STATUS_IS_CLEARED = "isCleared";

//    public final String TABLE_EVAL_SCHED = "eval_sched";
//    public final String EVAL_SCHED_ID = "eval_schedID";
//    public final String EVAL_SCHED_ISOPEN_COLUMN = "is_open";
//    public final String EVAL_SCHED_START = "schedStart";


    public static final int VER = 1;
    byte[] imageInBytes;

    public DATABASE_HELPER(@Nullable Context context) {
        super(context, DBNAME, null, VER);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_STUDENTS + "("+STUDENT_ID_COLUMN+" integer PRIMARY KEY AUTOINCREMENT, " + " image blob NOT NULL, number text, email text, "+STUDENT_LNAME_COLUMN+" text, " +
                ""+STUDENT_FNAME_COLUMN+" text, "+STUDENT_MNAME_COLUMN+" text, password text, " +STUDENT_ISCLEARED_COLUMN+ " integer CHECK(isCleared IN (0,1)))");

        db.execSQL("create table " + TABLE_PROFESSORS + "(" + PROF_ID_COLUMN + " integer PRIMARY KEY AUTOINCREMENT," + PROF_IMAGE_COLUMN + " blob NOT NULL, " +PROF_EMAIL_COLUMN+ " text, "+PROF_POSITION_COLUMN+" text, "+PROF_LNAME_COLUMN+" text, "+PROF_FNAME_COLUMN+" text, "+PROF_MNAME_COLUMN+" text, password text)");

        db.execSQL("create table " + TABLE_ADMIN + "(admin_id integer PRIMARY KEY AUTOINCREMENT, " + " username text,  password text)");

        db.execSQL("create table " + TABLE_SUBJECTS + "(" + SUBJ_ID_COLUMN + " integer PRIMARY KEY AUTOINCREMENT, " +  SUBJ_CODE_COLUMN + "  text,  " +  SUBJ_DESC_COLUMN + " text)");

        db.execSQL("create table " + TABLE_TEACHLOAD + "("+TEACHLOAD_ID_COLUMN+" integer PRIMARY KEY AUTOINCREMENT," +FK_PROF_COLUMN+ " integer, " +FK_SUBJ_COLUMN+ " integer, " +
                "  FOREIGN KEY (fk_prof) REFERENCES "+TABLE_PROFESSORS+" ("+PROF_ID_COLUMN+"), " +
                "  FOREIGN KEY (fk_subj) REFERENCES "+TABLE_SUBJECTS+" ("+SUBJ_ID_COLUMN+"))");

        db.execSQL("create table " + TABLE_EVALUATION + "("+EVALUATION_ID_COLUMN+" integer PRIMARY KEY AUTOINCREMENT," +FK_STUD_COLUMN+ " integer, " +FK_TEACHLOAD_COLUMN+ " integer, " +
                " " +AVERAGE_SCORE_COLUMN+ " float , " +AVERAGE_EFFORT_COLUMN+ " float, " + COMMENT_COLUMN + " text, " +
                "  FOREIGN KEY (fk_stud) REFERENCES "+TABLE_STUDENTS+" ("+STUDENT_ID_COLUMN+"), " +
                "  FOREIGN KEY (fk_teachload) REFERENCES "+TABLE_TEACHLOAD+" ("+TEACHLOAD_ID_COLUMN+"))");

//        db.execSQL("create table " + TABLE_STUDENT_STATUS + "("+STATUS_ID+" integer PRIMARY KEY AUTOINCREMENT," +STATUS_FK_STUDENT+ " integer, " +STATUS_IS_CLEARED+ " integer CHECK(isCleared IN (0,1)), " +
//                "  FOREIGN KEY (status_fkStud) REFERENCES "+TABLE_STUDENTS+" ("+STUDENT_ID_COLUMN+"))") ;

//        db.execSQL("create table " + TABLE_EVAL_SCHED + "(" + EVAL_SCHED_ID + " integer PRIMARY KEY AUTOINCREMENT, "+EVAL_SCHED_ISOPEN_COLUMN+" boolean ,  " +  SUBJ_DESC_COLUMN + " text)");
//                "CONSTRAINT fk_subj + integer REFERENCES "+TABLE_SUBJECTS+"( " + SUBJ_ID_COLUMN + ")";

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + TABLE_STUDENTS + "");
        db.execSQL("drop table if exists " + TABLE_PROFESSORS + "");
        db.execSQL("drop table if exists " + TABLE_ADMIN + "");
        db.execSQL("drop table if exists " + TABLE_SUBJECTS + "");
        db.execSQL("drop table if exists " + TABLE_TEACHLOAD + "");
        db.execSQL("drop table if exists " + TABLE_EVALUATION + "");
        db.execSQL("drop table if exists " + TABLE_STUDENT_STATUS + "");

    }

    //CRUD

//    public boolean insertStatus(int studID){
//
//        SQLiteDatabase sql = getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(STATUS_FK_STUDENT, studID);
//        cv.put(STATUS_IS_CLEARED, 0);
//
//        long result = sql.insert("" + TABLE_STUDENT_STATUS + "", null, cv);
//        if (result <= 0) {
//            return false;
//        } else {
//            return true;
//        }
//
//    }

    public boolean insertStudents(Bitmap image, String num, String email, String lName, String fName, String mName, String password) {

        SQLiteDatabase sql = getWritableDatabase();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        imageInBytes = stream.toByteArray();

        ContentValues cv = new ContentValues();
        cv.put("image", imageInBytes);
        cv.put("number", num);
        cv.put("email", email);
        cv.put(STUDENT_LNAME_COLUMN, lName);
        cv.put(STUDENT_FNAME_COLUMN, fName);
        cv.put(STUDENT_MNAME_COLUMN, mName);
        cv.put("password", password);
        cv.put(STATUS_IS_CLEARED, 0);

        long result = sql.insert("" + TABLE_STUDENTS + "", null, cv);
        if (result <= 0) {

            return false;

        } else {
            return true;
        }
    }

    public boolean insertProfessors(Bitmap image, String email, String position, String lName, String fName, String mName, String password) {

        SQLiteDatabase sql = getWritableDatabase();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        imageInBytes = stream.toByteArray();

        ContentValues cv = new ContentValues();
        cv.put(PROF_IMAGE_COLUMN, imageInBytes);
        cv.put(PROF_EMAIL_COLUMN, email);
        cv.put(PROF_POSITION_COLUMN, position);
        cv.put(PROF_LNAME_COLUMN, lName);
        cv.put(PROF_FNAME_COLUMN, fName);
        cv.put(PROF_MNAME_COLUMN, mName);
        cv.put("password", password);


        long result = sql.insert("" + TABLE_PROFESSORS + "", null, cv);
        if (result <= 0) {

            return false;

        } else {
            return true;
        }
    }

    public boolean insertADMIN(String username , String password) {

        SQLiteDatabase sql = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);

        long result = sql.insert("" + TABLE_ADMIN + "", null, cv);
        if (result <= 0) {

            return false;

        } else {
            return true;
        }
    }

    public boolean insertSubjects(String code, String description){

        SQLiteDatabase sql = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SUBJ_CODE_COLUMN, code);
        cv.put(SUBJ_DESC_COLUMN, description);

        long result = sql.insert("" + TABLE_SUBJECTS + "", null, cv);
        if (result <= 0) {
            return false;
        } else {
            return true;
        }

    }

    public boolean insertTeachingLoad(int fk_prof, int fk_subj){

        SQLiteDatabase sql = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FK_PROF_COLUMN, fk_prof);
        cv.put(FK_SUBJ_COLUMN, fk_subj);

        long result = sql.insert("" + TABLE_TEACHLOAD + "", null, cv);
        if (result <= 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertEvaluation(int fk_stud, int fk_teachload, float aveScore, float aveEffort, String comment){

        SQLiteDatabase sql = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FK_STUD_COLUMN, fk_stud);
        cv.put(FK_TEACHLOAD_COLUMN, fk_teachload);
        cv.put(AVERAGE_SCORE_COLUMN, aveScore);
        cv.put(AVERAGE_EFFORT_COLUMN, aveEffort);
        cv.put(COMMENT_COLUMN, comment);

        long result = sql.insert("" + TABLE_EVALUATION + "", null, cv);
        if (result <= 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean retrieveProf(ArrayList<Teacher> tList){

//        ArrayList<Teacher> tList = new ArrayList<>();

        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery(" Select * From " + TABLE_PROFESSORS + "" , null);

        if (c.moveToFirst()) {
            do {
//                return true;
                int id = Integer.parseInt(c.getString(0));
                byte[] image = c.getBlob(1);
                String email = c.getString(2);
                String position = c.getString(3);
                String lname = c.getString(4);
                String fname = c.getString(5);
                String mname = c.getString(6);
                String password = c.getString(7);

                Teacher model = new Teacher(id, image, email, position, lname, fname, mname, password);
                tList.add(model);

            } while (c.moveToNext());

        } else {
            return false;
        }
        return true;
    }

    public boolean deleteStudent(String number) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_STUDENTS + " where number = ? ", new String[]{number});
        if (c.getCount() > 0) {
            long result = sql.delete("" + TABLE_STUDENTS + "", "number=?", new String[]{number});
            if (result == -1) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

    public boolean deleteProfessor(String email) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_PROFESSORS + " where "+PROF_EMAIL_COLUMN+" = ? ", new String[]{email});
        if (c.getCount() > 0) {
            long result = sql.delete("" + TABLE_PROFESSORS + "", " "+PROF_EMAIL_COLUMN+" =?", new String[]{email});
            if (result == -1) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

    public boolean deleteSubject(String code) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_SUBJECTS + " where code = ? ", new String[]{code});
        if (c.getCount() > 0) {
            long result = sql.delete("" + TABLE_SUBJECTS + "", "code=?", new String[]{code});
            if (result == -1) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

    public boolean deleteLoad(int id) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_TEACHLOAD + " where " + TEACHLOAD_ID_COLUMN + " = ? ", new String[]{Integer.toString(id)});
        if (c.getCount() > 0) {
            long result = sql.delete("" + TABLE_TEACHLOAD + "", " "+ TEACHLOAD_ID_COLUMN +" =?" , new String[]{Integer.toString(id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

    public boolean updateStudStatus(int studID) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(STUDENT_ISCLEARED_COLUMN, 1);

        Cursor c = sql.rawQuery("select * from " + TABLE_STUDENTS + " where " + STUDENT_ID_COLUMN + " = ?", new String[]{Integer.toString(studID)});
        if (c.getCount() > 0) {

            long result = sql.update("" + TABLE_STUDENTS + "", cv, " " + STUDENT_ID_COLUMN + " =? ", new String[]{Integer.toString(studID)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

    public boolean updateStudents(String num, String lName, String fName, String mName, String email, String password, int isCleared) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("number", num);
        cv.put("email", email);
        cv.put(STUDENT_LNAME_COLUMN, lName);
        cv.put(STUDENT_FNAME_COLUMN, fName);
        cv.put(STUDENT_MNAME_COLUMN, mName);
        cv.put("password", password);
        cv.put(STUDENT_ISCLEARED_COLUMN, isCleared);

        Cursor c = sql.rawQuery("select * from " + TABLE_STUDENTS + " where number = ?", new String[]{num});
        if (c.getCount() > 0) {

            long result = sql.update("" + TABLE_STUDENTS + "", cv, "number=?", new String[]{num});
            if (result == -1) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

    public boolean updateProfessor(String email, String position, String lName, String fName, String mName, String password) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PROF_EMAIL_COLUMN, email);
        cv.put(PROF_POSITION_COLUMN, position);
        cv.put(PROF_LNAME_COLUMN, lName);
        cv.put(PROF_FNAME_COLUMN, fName);
        cv.put(PROF_MNAME_COLUMN, mName);
        cv.put("password", password);

        Cursor c = sql.rawQuery("select * from " + TABLE_PROFESSORS + " where " + PROF_EMAIL_COLUMN + " = ?", new String[]{email});
        if (c.getCount() > 0) {

            long result = sql.update("" + TABLE_PROFESSORS + "", cv, "" + PROF_EMAIL_COLUMN  + " = ? " , new String[]{email});
            if (result == -1) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

    public boolean updateSubject(int id, String code, String description) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("code", code);
        cv.put("description", description);

        Cursor c = sql.rawQuery("select * from " + TABLE_SUBJECTS + " where "+SUBJ_ID_COLUMN+" = ?", new String[]{String.valueOf(id)});
        if (c.getCount() > 0) {

            long result = sql.update("" + TABLE_SUBJECTS + "", cv, ""+SUBJ_ID_COLUMN+"=?", new String[]{String.valueOf(id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

    //validations login
    public boolean checkProfID(int id) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_TEACHLOAD + " where "+ FK_PROF_COLUMN +" = ?", new String[]{Integer.toString(id)});

        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkifStatusExists(int id) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_STUDENT_STATUS + " where "+ STATUS_FK_STUDENT +" = ?", new String[]{Integer.toString(id)});

        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkSubjID(int id) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_TEACHLOAD + " where "+ FK_SUBJ_COLUMN +" = ?", new String[]{Integer.toString(id)});

        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkStudentNumber(String number) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_STUDENTS + " where number = ?", new String[]{number});

        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean checkStudentLogin(String number, String password) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_STUDENTS + " where number = ? and password = ?", new String[]{number, password});

        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfStudAccExists(String number, String password) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_STUDENTS + " where number = ? and password != ?", new String[]{number, password});

        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

//    public boolean checkStudentPassword(String password) {
//        SQLiteDatabase sql = getWritableDatabase();
//        Cursor c = sql.rawQuery("select * from " + TABLE_STUDENTS + " where password = ?", new String[]{password});
//
//        if (c.getCount() > 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    public boolean checkAdminUsername(String username) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_ADMIN + " where username = ?", new String[]{username});

        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkAdminIfExists(String username, String password) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_ADMIN + " where username = ? and password = ?", new String[]{username, password});

        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkAdminPassword(String password) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_ADMIN + " where password = ?", new String[]{password});

        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkProfessorEmail(String email) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_PROFESSORS + " where "+PROF_EMAIL_COLUMN+" = ?", new String[]{email});

        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

//    public boolean checkProfessorPassword(String password) {
//        SQLiteDatabase sql = getWritableDatabase();
//        Cursor c = sql.rawQuery("select * from " + TABLE_PROFESSORS + " where password = ?", new String[]{password});
//
//        if (c.getCount() > 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    public boolean checkProfLogin(String email, String password) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_PROFESSORS + " where "+PROF_EMAIL_COLUMN+" = ? and password = ?", new String[]{email, password});

        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfProfAccExists(String email, String password) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor c = sql.rawQuery("select * from " + TABLE_PROFESSORS + " where "+PROF_EMAIL_COLUMN+" = ? and password != ?", new String[]{email, password});

        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


}







