package com.example.e_val;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class StudentProfileAdapter extends RecyclerView.Adapter<StudentProfileAdapter.ViewHolder> {

    ArrayList<Student> Studlist = new ArrayList<>();
//    static ArrayList<ClearedStudents> clrdStdList;
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    DATABASE_HELPER dbHelper = new DATABASE_HELPER(context);
    SQLiteDatabase sql;

    public StudentProfileAdapter(ArrayList<Student> Studlist, RecyclerViewInterface recyclerViewInterface) {
        this.Studlist = Studlist;
//        this.clrdStdList = clrdStdList;
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_layout, parent, false);
        return new ViewHolder(v, recyclerViewInterface);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Student stud = Studlist.get(position);
//        ClearedStudents clrdStd = clrdStdList.get(position);
        byte[] studImage = stud.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(studImage, 0, studImage.length);
        int Stud_id = stud.getId();
        String lname = stud.getLname();
        String fname = stud.getFname();
        String mname = stud.getMname();
        String fullName = fname + " " + mname + ". " + lname;

        int isCleared = stud.getIsCleared();

        if (isCleared == 1){

            holder.ivCheck.setVisibility(View.VISIBLE);
        }

        holder.image.setImageBitmap(bitmap);
        holder.name.setText(fullName);
        holder.student.setText(String.valueOf(stud.getNum()));

//        if (clrdStdList.contains(stud.getId())){
//
//            holder.image.setImageBitmap(bitmap);
//            holder.name.setText(fullName);
//            holder.student.setText(String.valueOf(stud.getNum()));
//            holder.ivCheck.setVisibility(View.VISIBLE);
//        } else {
//
//
//        }

    }

    @Override
    public int getItemCount() {
        return Studlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, student;
        ShapeableImageView image;
        ImageView ivCheck;
        View mView;


        public ViewHolder(@NonNull View view, RecyclerViewInterface recyclerViewInterface) {
            super(view);

            name = (TextView) view.findViewById(R.id.tvStudName);
            student = (TextView) view.findViewById(R.id.tvStudNum);
            image = (ShapeableImageView) view.findViewById(R.id.ivStudImage);
            ivCheck = view.findViewById(R.id.ivCheck);
            mView = view;

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);

                        }
                    }
                }
            });

            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onLongItemClick(pos);

                        }
                    }
                    return false;
                }
            });
        }

    }

}