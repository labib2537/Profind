package com.example.profind;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class f_2_builder_page extends AppCompatActivity implements View.OnClickListener {
    TabLayout builder_tabLayout;
    TabItem builder_tabitem1,builder_tabitem2;
    ViewPager viewpager;
    Button back_btn;
    f_2_0_Builder_PagerAdapter builder_pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_f_2_builder_page);
        getSupportActionBar().hide();

        back_btn = (Button) findViewById(R.id.builder_page_back_btn_id);
        builder_tabLayout=(TabLayout) findViewById(R.id.builder_page_tab_layout_id);
        builder_tabitem1=(TabItem) findViewById(R.id.builder_page_tab_item_1_id);
        builder_tabitem2=(TabItem) findViewById(R.id.builder_page_tab_item_2_id);
        viewpager=(ViewPager) findViewById(R.id.builder_page_viewpager_id);
        back_btn.setOnClickListener(this);

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width_half = (dm.widthPixels / 2) - 5;

        builder_pagerAdapter= new f_2_0_Builder_PagerAdapter(getSupportFragmentManager(),builder_tabLayout.getTabCount(), width_half);
        viewpager.setAdapter(builder_pagerAdapter);

        builder_tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0||tab.getPosition()==1){
                    builder_pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(builder_tabLayout));

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.builder_page_back_btn_id) {
            finish();
        }
    }

}