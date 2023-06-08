package com.example.profind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class d_0_0_12_img_vdo_show extends AppCompatActivity {
    CircleImageView profile_img_imgvw;
    TextView name_txtvw,description_txtvw;
    View img_vdo_line;
    RecyclerView img_vdo_recyclervw;
    String[] single_images,imgvdo_indicator;
    d_0_0_13_img_vdo_showAdapter adapter;


    String profile_img,profile_name,description,img_vdo,img_vdo_indicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_0_0_12_img_vdo_show);
        getSupportActionBar().hide();


        profile_img_imgvw=findViewById(R.id.img_vdo_show_profile_imgvw_id);
        name_txtvw=findViewById(R.id.img_vdo_show_profile_nametxtvw_id);
        description_txtvw=findViewById(R.id.img_vdo_show_descriptiontxtvw_id);
        img_vdo_line=findViewById(R.id.img_vdo_show_page_line_id);
        img_vdo_recyclervw=findViewById(R.id.img_vdo_show_page_recyclerview_id);

        profile_img = getIntent().getStringExtra("profile_img_key");
        profile_name = getIntent().getStringExtra("profile_name_key");
        description = getIntent().getStringExtra("description_key");
        img_vdo = getIntent().getStringExtra("img_vdo_key");
        img_vdo_indicator = getIntent().getStringExtra("img_vdo_indicator_key");

        single_images= img_vdo.split(" ");
        imgvdo_indicator= img_vdo_indicator.split(" ");

        LinearLayout.LayoutParams lout_empty = new LinearLayout.LayoutParams(0, 0);
        Picasso.get()
                .load( profile_img)
                .placeholder(R.drawable.profile_img)
                .into(profile_img_imgvw);
        name_txtvw.setText(profile_name);
        if(description.length()==0){
            description_txtvw.setLayoutParams(lout_empty);
            img_vdo_line.setLayoutParams(lout_empty);
        }else {
            description_txtvw.setText(description);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        img_vdo_recyclervw.setLayoutManager(layoutManager);
        adapter = new d_0_0_13_img_vdo_showAdapter(this,single_images,imgvdo_indicator);
        img_vdo_recyclervw.setNestedScrollingEnabled(false);
        img_vdo_recyclervw.setAdapter(adapter);

    }
}