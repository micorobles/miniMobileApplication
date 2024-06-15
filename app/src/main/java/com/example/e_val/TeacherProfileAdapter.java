package com.example.e_val;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class TeacherProfileAdapter extends RecyclerView.Adapter<TeacherProfileAdapter.ViewHolder> {

    ArrayList<Teacher> profList;
    private final RecyclerViewInterface recyclerViewInterface;

    public TeacherProfileAdapter(ArrayList<Teacher> profList, RecyclerViewInterface recyclerViewInterface) {
        this.profList = profList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public TeacherProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_layout, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TeacherProfileAdapter.ViewHolder holder, int position) {

        Teacher prof = profList.get(position);

        byte[] profImage = prof.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(profImage, 0, profImage.length);

        String lname = prof.getLname();
        String fname = prof.getFname();
        String mname = prof.getMname();
        String fullName = fname + " " + mname + ". " + lname;

        holder.image.setImageBitmap(bitmap);
        holder.name.setText(fullName);
        holder.email.setText(String.valueOf(prof.getEmail()));


    }


    @Override
    public int getItemCount() {
        return profList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, email;
        ShapeableImageView image;
        View pView;

        public ViewHolder(@NonNull View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.tvStudName);
            email = (TextView) view.findViewById(R.id.tvStudNum);
            image = (ShapeableImageView) view.findViewById(R.id.ivStudImage);

            pView = view;

            pView.setOnClickListener(new View.OnClickListener() {
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

            pView.setOnLongClickListener(new View.OnLongClickListener() {
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