package com.example.profind;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class e_1_0_Group_PagerAdapter extends FragmentStateAdapter {

    int width_half;
    public e_1_0_Group_PagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,int width_half) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:return new e_1_1_Group_page_fragment_1_posts(width_half);
            case 1:return new e_1_2_Group_page_fragment_2_mygroups();
            case 2:return new e_1_3_Group_page_fragment_3_allgroups();

            default:return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
