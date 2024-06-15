package com.example.e_val;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeacherCommentAdapter extends RecyclerView.Adapter<TeacherCommentAdapter.ViewHolder>{

    ArrayList<Comments> commentList;

    public TeacherCommentAdapter(ArrayList<Comments> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public TeacherCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prof_comments_design, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TeacherCommentAdapter.ViewHolder holder, int position) {

        Comments comments = commentList.get(position);

        holder.tvComment.setText(comments.getComments());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvComment;

        public ViewHolder(@NonNull View view) {
            super(view);

            tvComment = view.findViewById(R.id.tvComment);
        }
    }
}
