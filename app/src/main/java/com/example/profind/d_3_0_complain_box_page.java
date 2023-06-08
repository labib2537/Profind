package com.example.profind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class d_3_0_complain_box_page extends AppCompatActivity implements View.OnClickListener {

    Button back_btn, submit_btn;
    String phone,complain_txt;
    TextInputEditText complain_box;
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_3_0_complain_box_page);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        back_btn= findViewById(R.id.complain_page_back_btn_id);
        submit_btn= findViewById(R.id.complain_page_submit_btn_id);
        complain_box= findViewById(R.id.complain_edtxt_id);

        back_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.complain_page_back_btn_id) {
            finish();
        }else if (view.getId() == R.id.complain_page_submit_btn_id) {
            complain_txt=complain_box.getText().toString().trim();
            if(complain_txt.equals("")){
                Toast.makeText(getApplicationContext(), "Write something first", Toast.LENGTH_LONG).show();
            }else {
                Map<String, String> complains = new HashMap<>();
                complains.put("phone", phone);
                complains.put("complain_text", complain_txt);
                db.collection("complain_box").add(complains).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Successfully complained", Toast.LENGTH_LONG).show();
                    }
                });
                finish();
            }
        }
    }
}