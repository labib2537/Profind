package com.example.profind;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class c_0_0_Home_PagerAdapter extends FragmentPagerAdapter {
    int tabcount;
    int width_half;
    Context context;

    public c_0_0_Home_PagerAdapter(@NonNull FragmentManager fm, int behavior, int width_half, Context context) {
        super(fm, behavior);
        tabcount = behavior;
        this.width_half = width_half;
        this.context=context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: return new e_0_Home_page_fragment(width_half);
            case 1: return new e_1_Group_page_fragment(width_half);
            case 2: return new e_2_Notification_page_fragment(width_half,context);
            case 3: return new e_3_post_page_fragment(width_half);

            default: return null;
        }

    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
