package com.example.profind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class b_1_0_ChangePassword_phone_page extends AppCompatActivity implements View.OnClickListener {
    Button next_btn;
    TextInputLayout phone;
    Intent intent;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_b_1_0_change_password_phone_page);
        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();
        phone = findViewById(R.id.changepassword_phone_edtxt_id);
        next_btn= findViewById(R.id.changepassword_next_btn_id);
        next_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!validate_phone()) {
            return;
        }

        String _user_phone=phone.getEditText().getText().toString().trim();
        String _phoneN0="+88"+_user_phone;

        db.collection("users")
                .whereEqualTo("Phone_Number", _phoneN0)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "No account exist with that phone number !", Toast.LENGTH_SHORT).show();
                        } else {
                            intent = new Intent(getApplicationContext(), b_2_Otp_2.class);
                            intent.putExtra("phnf",_phoneN0);
                            startActivity(intent);
                        }
                    }
                });

    }
    private Boolean validate_phone() {
        String val=phone.getEditText().getText().toString().trim();
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(val);
        if(val.isEmpty()){
            phone.setError("Field can not be empty");
            return false;
        }
        else if (!m.matches()){
            phone.setError("Must be 11 number");
            return false;
        }
        else if (val.charAt(0)!='0' || val.charAt(1)!='1'){
            phone.setError("Must be start with 01");
            return false;
        }
        else {
            phone.setError(null);
            return true;
        }
    }
}