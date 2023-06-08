package com.example.profind;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class e_1_Group_page_fragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    Button create_grp_btn, search_grp_btn;

    int width_half;
    TabLayout group_tabLayout;
    ViewPager2 viewpager;
    e_1_0_Group_PagerAdapter group_pagerAdapter;


    public e_1_Group_page_fragment(int width_half) {
        this.width_half = width_half;
    }

    public static e_1_Group_page_fragment newInstance(String param1, String param2, int width_half) {
        e_1_Group_page_fragment fragment = new e_1_Group_page_fragment(width_half);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_e_1_group_page_fragment, container, false);

        create_grp_btn = (Button) view.findViewById(R.id.group_page_create_grp_btn_id);
        search_grp_btn = (Button) view.findViewById(R.id.group_page_search_grp_btn_id);
        group_tabLayout = (TabLayout) view.findViewById(R.id.groups_page_tab_layout_id);
        viewpager = (ViewPager2) view.findViewById(R.id.groups_page_viewpager_id);

        group_pagerAdapter = new e_1_0_Group_PagerAdapter(getChildFragmentManager(), getLifecycle(), width_half);
        viewpager.setAdapter(group_pagerAdapter);
        viewpager.setUserInputEnabled(false);

        group_tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0 || tab.getPosition() == 1 || tab.getPosition() == 2) {
                    group_pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        create_grp_btn.setOnClickListener(this);
        search_grp_btn.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        if (view.getId() == R.id.group_page_create_grp_btn_id) {
            intent = new Intent(view.getContext(), e_1_4_Group_create.class);
            startActivity(intent);
        } else if (view.getId() == R.id.group_page_search_grp_btn_id) {
            intent = new Intent(view.getContext(), e_1_5_Group_search.class);
            startActivity(intent);
        }

    }
}