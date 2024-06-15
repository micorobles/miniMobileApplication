package com.example.e_val;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.Toast;

public class TEST extends AppCompatActivity {
    RatingBar rb_1;
    float rateValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        rb_1 = (RatingBar) findViewById(R.id.rb1);

        rb_1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rateValue = rb_1.getRating();
                Toast.makeText(TEST.this, "" + rateValue, Toast.LENGTH_SHORT).show();
            }
        });
    }
}