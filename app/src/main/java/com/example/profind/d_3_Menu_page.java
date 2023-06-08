package com.example.profind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class d_3_Menu_page extends AppCompatActivity implements View.OnClickListener {
    FirebaseFirestore db;

    LinearLayout warningLayout;
    Button back_btn, profile_btn, appointment_btn, setting_btn, complainbox_btn, invitefrnd_btn, needhelp_btn, logout_btn;
    CircleImageView Profile_img;
    TextView profile_name_txt;
    String name, phone, activated,pro_account;
    Intent intent;
    boolean bol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_3_menu_page);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        name = userDetails.get(b_0_1_Session.KEY_NAME);
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        warningLayout = findViewById(R.id.menu_page_warning_layout_id);
        back_btn = findViewById(R.id.menu_page_back_btn_id);
        Profile_img = findViewById(R.id.menu_page_profile_image_btn_id);
        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("Phone_Number", phone)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if(!document.getString("Profile_Photo").isEmpty()){
                                String urlp = document.getString("Profile_Photo");
                                Picasso.get()
                                        .load(urlp)
                                        .placeholder(R.drawable.profile_img)
                                        .into(Profile_img);
                            }
                            pro_account=document.getString("Pro_Account");
                            activated = document.getString("Activated");
                            int adrslayout_width = LinearLayout.LayoutParams.MATCH_PARENT;
                            int adrslayout_height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            LinearLayout.LayoutParams lp_empty = new LinearLayout.LayoutParams(0, 0);
                            LinearLayout.LayoutParams lp_not_empty = new LinearLayout.LayoutParams(adrslayout_width, adrslayout_height);
                            bol=activated.equals("true");
                            if (!bol) {
                                warningLayout.setLayoutParams(lp_not_empty);
                            }

                        }
                    }
                });

        profile_name_txt = findViewById(R.id.menu_page_profile_name_btn_id);
        profile_name_txt.setText(name);

        profile_btn = findViewById(R.id.menu_page_profile_btn_id);

        appointment_btn = findViewById(R.id.menu_page_appointment_btn_id);
        setting_btn = findViewById(R.id.menu_page_setting_btn_id);
        complainbox_btn = findViewById(R.id.menu_page_complain_box_btn_id);
        invitefrnd_btn = findViewById(R.id.menu_page_invite_frnd_btn_id);
        needhelp_btn = findViewById(R.id.menu_page_need_help_btn_id);
        logout_btn = findViewById(R.id.menu_page_logout_btn_id);

        warningLayout.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        profile_btn.setOnClickListener(this);
        appointment_btn.setOnClickListener(this);
        setting_btn.setOnClickListener(this);
        complainbox_btn.setOnClickListener(this);
        invitefrnd_btn.setOnClickListener(this);
        needhelp_btn.setOnClickListener(this);
        logout_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.menu_page_back_btn_id) {
            finish();
        }else if (view.getId() == R.id.menu_page_warning_layout_id) {
            intent = new Intent(this, d_0_0_5_Profile_setting.class);
            startActivity(intent);
        } else if (view.getId() == R.id.menu_page_profile_btn_id) {
            intent = new Intent(this, d_0_0_Profile.class);
            startActivity(intent);

        } else if (view.getId() == R.id.menu_page_appointment_btn_id && bol) {
            if(pro_account.equals("Yes")){
                intent = new Intent(this, d_0_0_4_Appointments.class);
                startActivity(intent);
            }else {
                intent = new Intent(this, d_0_0_4_4_asaclient.class);
                startActivity(intent);
            }
        } else if (view.getId() == R.id.menu_page_setting_btn_id&& bol) {

        } else if (view.getId() == R.id.menu_page_complain_box_btn_id&& bol) {
            intent = new Intent(this, d_3_0_complain_box_page.class);
            startActivity(intent);
        } else if (view.getId() == R.id.menu_page_invite_frnd_btn_id&& bol) {

        } else if (view.getId() == R.id.menu_page_need_help_btn_id&& bol) {

        } else if (view.getId() == R.id.menu_page_logout_btn_id) {
            b_0_1_Session session = new b_0_1_Session(this);
            if (session.checkLogin()) {
                session.logoutUserFromSession();
                intent = new Intent(this, b_0_Login.class);
                startActivity(intent);
            }
        }
    }

}