package com.example.profind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class d_0_0_5_Profile_setting extends AppCompatActivity implements View.OnClickListener {
    Button back_btn, deactivate_account_btn;
    FirebaseFirestore db;
    String phone;
    String activated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_0_0_5_profile_setting);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("Phone_Number", phone)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            activated = document.getString("Activated");
                            if(activated.equals("true")){
                                deactivate_account_btn.setText("Deactivate Account");
                                deactivate_account_btn.setTextColor(Color.parseColor("#027C64"));
                            }else if(activated.equals("false")){
                                deactivate_account_btn.setText("Activate Account");
                                deactivate_account_btn.setTextColor(Color.parseColor("#E91E63"));
                            }
                        }
                    }
                });

        back_btn = findViewById(R.id.profile_setting_page_back_btn_id);
        deactivate_account_btn = findViewById(R.id.profile_setting_page_block_profile_btn_id);

        back_btn.setOnClickListener(this);
        deactivate_account_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.profile_setting_page_back_btn_id) {

        } else if (view.getId() == R.id.profile_setting_page_block_profile_btn_id) {
            if(activated.equals("true")){
                db.collection("users").document(phone).update("Activated", "false");
                deactivate_account_btn.setText("Activate Account");
                deactivate_account_btn.setTextColor(Color.parseColor("#E91E63"));
            }else if(activated.equals("false")){
                db.collection("users").document(phone).update("Activated", "true");
                deactivate_account_btn.setText("Deactivate Account");
                deactivate_account_btn.setTextColor(Color.parseColor("#027C64"));
            }
        }
    }
}