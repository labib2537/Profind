package com.example.profind;

import static android.R.color.black;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class c_0_HomePage extends AppCompatActivity implements View.OnClickListener {

    LinearLayout warningLayout;
    String activated;
    boolean bol;
    TabLayout home_tabLayout;
    CircleImageView Profile_img;
    TabItem home_tabitem1_home, home_tabitem1_group, home_tabitem1_notification, home_tabitem1_post;
    androidx.appcompat.widget.Toolbar top_toolbar;
    HorizontalScrollView professions_toolbar;
    View top_toolbar_background;
    ViewPager viewpager;
    Button Profile_btn, Search_btn, Massage_btn, Menu_btn, doctor_btn, teacher_btn, builder_btn, lawyer_btn;
    c_0_0_Home_PagerAdapter home_pagerAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_c_0_homepage);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        String phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        ///finding nav bar button
        top_toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.homepage_toolbar_id);
        top_toolbar_background = (View) findViewById(R.id.homepage_toolbar_background_id);
        professions_toolbar = findViewById(R.id.homepage_profession_toolbar_id);
        warningLayout = findViewById(R.id.home_page_warning_layout_id);
        Profile_img = findViewById(R.id.homepage_profile_img_id);
        db.collection("users")
                .whereEqualTo("Phone_Number", phone)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (!document.getString("Profile_Photo").isEmpty()) {
                                String url = document.getString("Profile_Photo");
                                Picasso.get()
                                        .load(url)
                                        .placeholder(R.drawable.profile_img)
                                        .into(Profile_img);
                            }
                            activated = document.getString("Activated");
                            int adrslayout_width = LinearLayout.LayoutParams.MATCH_PARENT;
                            int adrslayout_height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            LinearLayout.LayoutParams lp_empty = new LinearLayout.LayoutParams(0, 0);
                            LinearLayout.LayoutParams lp_not_empty = new LinearLayout.LayoutParams(adrslayout_width, adrslayout_height);
                            bol = activated.equals("true");
                            if (!bol) {
                                warningLayout.setLayoutParams(lp_not_empty);
                            }

                        }
                    }
                });


        Profile_btn = (Button) findViewById(R.id.homepage_profile_img_btn_id);

        Search_btn = findViewById(R.id.homepage_search_btn_id);
        Massage_btn = findViewById(R.id.homepage_massage_btn_id);
        Menu_btn = (Button) findViewById(R.id.homepage_menu_btn_id);

        home_tabLayout = (TabLayout) findViewById(R.id.homepage_tab_layout_1_id);

        db.collection("last_time_checked")
                .document("notification")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String time = documentSnapshot.getString("time");
                            db.collection("notifications")
                                    .whereGreaterThan("time", time)
                                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (DocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
                                        if (documentSnapshot1.exists()) {
                                            BadgeDrawable notificaation_badge = home_tabLayout.getTabAt(2).getOrCreateBadge();
                                            notificaation_badge.setBadgeGravity(BadgeDrawable.TOP_END);
                                            notificaation_badge.setVisible(true);
                                        }
                                    }
                                }
                            });
                        } else {
                            BadgeDrawable notificaation_badge = home_tabLayout.getTabAt(2).getOrCreateBadge();
                            notificaation_badge.setBadgeGravity(BadgeDrawable.TOP_END);
                            notificaation_badge.setVisible(true);
                        }
                    }
                });
        home_tabitem1_home = (TabItem) findViewById(R.id.homepage_tabitem_home_id);
        home_tabitem1_group = (TabItem) findViewById(R.id.homepage_tabitem_group_id);
        home_tabitem1_notification = (TabItem) findViewById(R.id.homepage_tabitem_notification_id);
        home_tabitem1_post = (TabItem) findViewById(R.id.homepage_tabitem_post_id);

        doctor_btn = (Button) findViewById(R.id.doctor_btn_id);
        teacher_btn = (Button) findViewById(R.id.teacher_btn_id);
        builder_btn = (Button) findViewById(R.id.builder_btn_id);
        lawyer_btn = (Button) findViewById(R.id.lawyer_btn_id);
        viewpager = (ViewPager) findViewById(R.id.homepage_viewpager_id);

        ///adding onclick listener
        warningLayout.setOnClickListener(this);
        Profile_btn.setOnClickListener(this);
        Search_btn.setOnClickListener(this);
        Massage_btn.setOnClickListener(this);
        Menu_btn.setOnClickListener(this);
        doctor_btn.setOnClickListener(this);
        teacher_btn.setOnClickListener(this);
        builder_btn.setOnClickListener(this);
        lawyer_btn.setOnClickListener(this);

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width_half = (dm.widthPixels / 2) - 5;

        home_pagerAdapter = new c_0_0_Home_PagerAdapter(getSupportFragmentManager(), home_tabLayout.getTabCount(), width_half, this);
        viewpager.setAdapter(home_pagerAdapter);

        home_tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0 || tab.getPosition() == 1 || tab.getPosition() == 2 || tab.getPosition() == 3) {
                    home_pagerAdapter.notifyDataSetChanged();
                }
                if (tab.getPosition() == 1 || tab.getPosition() == 2 || tab.getPosition() == 3) {
                    hideToolbar();
                }
                if (tab.getPosition() == 0) {
                    showToolbar();
                }
                if (tab.getPosition() == 2) {
                    BadgeDrawable notificaation_badge = home_tabLayout.getTabAt(2).getOrCreateBadge();
                    notificaation_badge.setBadgeGravity(BadgeDrawable.TOP_END);
                    notificaation_badge.setVisible(false);
                    Map<String, String> notification_checked_item = new HashMap<>();
                    notification_checked_item.put("time", new Date().getTime() + "");
                    db.collection("last_time_checked").document("notification").set(notification_checked_item);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(home_tabLayout));


    }


    @Override
    public void onClick(View view) {
        Intent intent;
        if (view.getId() == R.id.homepage_profile_img_btn_id) {
            intent = new Intent(this, d_0_0_Profile.class);
            startActivity(intent);
        } else if (view.getId() == R.id.home_page_warning_layout_id) {
            intent = new Intent(this, d_0_0_5_Profile_setting.class);
            startActivity(intent);
        } else if (view.getId() == R.id.homepage_search_btn_id && bol) {
            intent = new Intent(this, d_1_Search.class);
            startActivity(intent);
        } else if (view.getId() == R.id.homepage_massage_btn_id && bol) {
            intent = new Intent(this, d_2_Massage.class);
            startActivity(intent);
        } else if (view.getId() == R.id.homepage_menu_btn_id) {
            intent = new Intent(this, d_3_Menu_page.class);
            startActivity(intent);
        } else if (view.getId() == R.id.doctor_btn_id && bol) {
            intent = new Intent(this, f_0_doctor_page.class);
            startActivity(intent);
        } else if (view.getId() == R.id.teacher_btn_id && bol) {
            intent = new Intent(this, f_1_teacher_page.class);
            startActivity(intent);
        } else if (view.getId() == R.id.builder_btn_id && bol) {
            intent = new Intent(this, f_2_builder_page.class);
            startActivity(intent);
        } else if (view.getId() == R.id.lawyer_btn_id && bol) {
            intent = new Intent(this, f_3_lawyer_page.class);
            startActivity(intent);
        }
    }

    private void hideToolbar() {
        professions_toolbar.setVisibility(View.GONE);
        top_toolbar.setVisibility(View.GONE);
        top_toolbar_background.setVisibility(View.GONE);
    }

    private void showToolbar() {
        professions_toolbar.setVisibility(View.VISIBLE);
        top_toolbar.setVisibility(View.VISIBLE);
        top_toolbar_background.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);

    }



}