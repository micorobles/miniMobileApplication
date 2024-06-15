package com.example.e_val;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class EvaluatedAdapter extends RecyclerView.Adapter<EvaluatedAdapter.ViewHolder> {
    private ArrayList<String> profs;
    private ArrayList<String> subjects;

    private ArrayList<Evaluated> result;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProf, tvSubj, comment;
        private ShapeableImageView profImg;
        private RatingBar aveScore, aveEffort;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            tvProf = (TextView) view.findViewById(R.id.tvProf);
            tvSubj = (TextView) view.findViewById(R.id.tvSubject);
            comment = (TextView) view.findViewById(R.id.etComment);
            profImg = view.findViewById(R.id.ivProfIMG);
            aveScore = view.findViewById(R.id.rbAveScore);
            aveEffort = view.findViewById(R.id.rbAveEffort);
        }
    }

    public EvaluatedAdapter( ArrayList<Evaluated> resultList) {
        result = resultList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Evaluated rl = result.get(position);

        byte[] profImage = rl.getProfImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(profImage, 0, profImage.length);

        holder.tvProf.setText(rl.getProfName());
        holder.tvSubj.setText(rl.getSubjName());
        holder.profImg.setImageBitmap(bitmap);
        holder.aveScore.setRating(rl.getAveScore());
        holder.aveEffort.setRating(rl.getAveEffort());
        holder.comment.setText(rl.getComment());

    }


    @Override
    public int getItemCount() {
        return result.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
