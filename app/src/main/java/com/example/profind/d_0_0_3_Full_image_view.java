package com.example.profind;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

public class d_0_0_3_Full_image_view extends AppCompatActivity {
    TouchImageView full_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_0_0_3_full_image_view);
        getSupportActionBar().hide();

        full_image=findViewById(R.id.fullImageview_id);
        String full_img_url=getIntent().getStringExtra("fullimgkey");
        Picasso.get()
                .load(full_img_url)
                .placeholder(R.drawable.profile_img)
                .into(full_image);
    }
}