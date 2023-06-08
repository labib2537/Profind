package com.example.profind;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

public class f_0_doctor_page extends AppCompatActivity implements View.OnClickListener {
    TabLayout doctor_tabLayout;
    TabItem doctor_tabitem1, doctor_tabitem2;
    ViewPager viewpager;
    Button back_btn;
    f_0_0_Doctor_PagerAdapter doctor_pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_f_0_doctor_page);
        getSupportActionBar().hide();


        back_btn = (Button) findViewById(R.id.doctor_page_back_btn_id);
        doctor_tabLayout = (TabLayout) findViewById(R.id.doctor_page_tab_layout_id);
        doctor_tabitem1 = (TabItem) findViewById(R.id.doctor_page_tab_item_1_id);
        doctor_tabitem2 = (TabItem) findViewById(R.id.doctor_page_tab_item_2_id);
        viewpager=(ViewPager) findViewById(R.id.doctor_page_viewpager_id);
        back_btn.setOnClickListener(this);


        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width_half = (dm.widthPixels / 2) - 5;

        doctor_pagerAdapter=new f_0_0_Doctor_PagerAdapter(getSupportFragmentManager(),doctor_tabLayout.getTabCount(),width_half);
        viewpager.setAdapter(doctor_pagerAdapter);


        doctor_tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0 || tab.getPosition() == 1) {
                    doctor_pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(doctor_tabLayout));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.doctor_page_back_btn_id) {
            finish();
        }
    }

}
