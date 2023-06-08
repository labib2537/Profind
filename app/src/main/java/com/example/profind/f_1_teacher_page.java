package com.example.profind;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class f_1_teacher_page extends AppCompatActivity implements View.OnClickListener {
    TabLayout teacher_tabLayout;
    TabItem teacher_tabitem1,teacher_tabitem2;
    ViewPager viewpager;
    Button back_btn;
    f_1_0_Teacher_PagerAdapter teacher_pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_f_1_teacher_page);
        getSupportActionBar().hide();

        back_btn = (Button) findViewById(R.id.teacher_page_back_btn_id);
        teacher_tabLayout=(TabLayout) findViewById(R.id.teacher_page_tab_layout_id);
        teacher_tabitem1=(TabItem) findViewById(R.id.teacher_page_tab_item_1_id);
        teacher_tabitem2=(TabItem) findViewById(R.id.teacher_page_tab_item_2_id);
        viewpager=(ViewPager) findViewById(R.id.teacher_page_viewpager_id);
        back_btn.setOnClickListener(this);

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width_half = (dm.widthPixels / 2) - 5;

        teacher_pagerAdapter= new f_1_0_Teacher_PagerAdapter(getSupportFragmentManager(),teacher_tabLayout.getTabCount(),width_half);
        viewpager.setAdapter(teacher_pagerAdapter);

        teacher_tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0||tab.getPosition()==1){
                    teacher_pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(teacher_tabLayout));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.teacher_page_back_btn_id) {
            finish();
        }
    }

}