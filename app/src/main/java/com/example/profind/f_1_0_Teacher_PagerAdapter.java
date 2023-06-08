package com.example.profind;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class f_1_0_Teacher_PagerAdapter extends FragmentPagerAdapter {
    int tabcount;
    int width_half;
    public f_1_0_Teacher_PagerAdapter(@NonNull FragmentManager fm, int behavior,int width_half) {
        super(fm, behavior);
        tabcount=behavior;
        this.width_half=width_half;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new f_1_1_Teacher_page_fragment_1(width_half);
            case 1:return new f_1_2_Teacher_page_fragment_2();

            default:return null;
        }
    }
    @Override
    public int getCount() {
        return tabcount;
    }
}
