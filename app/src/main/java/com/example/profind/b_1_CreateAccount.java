package com.example.profind;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.*;
import java.util.regex.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class b_1_CreateAccount extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout name, phone, password_1, password_2;
    Button register_btn;
    Intent intent;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_b_1_create_account);
        getSupportActionBar().hide();


        db = FirebaseFirestore.getInstance();

        name = findViewById(R.id.createaccount_username_edtxt_id);
        phone = findViewById(R.id.createaccount_phone_edtxt_id);
        password_1 = findViewById(R.id.createaccount_password_1_edtxt_id);
        password_2 = findViewById(R.id.createaccount_password_2_edtxt_id);


        register_btn = findViewById(R.id.createaccount_register_btn_id);
        register_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.createaccount_register_btn_id) {
            if (!validate_name() | !validate_phone() | !validate_password_1() | !validate_password_2()) {
                return;
            }

            String _user_name = name.getEditText().getText().toString().trim();
            String _user_phone = phone.getEditText().getText().toString().trim();
            String _phoneN0 = "+88" + _user_phone;
            String _user_password = password_2.getEditText().getText().toString().trim();

            db.collection("users")
                    .whereEqualTo("Phone_Number", _phoneN0)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                showHaveAccountDialogue();

                            } else {
                                intent = new Intent(getApplicationContext(), b_2_Otp.class);
                                intent.putExtra("phn", _phoneN0);
                                intent.putExtra("name", _user_name);
                                intent.putExtra("pswrd", _user_password);
                                startActivity(intent);
                            }
                        }
                    });
        }
    }

    private Boolean validate_name() {
        String val = name.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            name.setError("Field can not be empty");
            return false;
        } else if (val.length() < 4) {
            name.setError("Must be more then 3 character");
            return false;
        } else if (!(Character.isUpperCase(val.charAt(0)))) {
            name.setError("First latter must be upper case");
            return false;
        } else {
            name.setError(null);
            return true;
        }
    }

    private Boolean validate_phone() {
        String val = phone.getEditText().getText().toString().trim();
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(val);
        if (val.isEmpty()) {
            phone.setError("Field can not be empty");
            return false;
        } else if (!m.matches()) {
            phone.setError("Must be 11 number");
            return false;
        } else if (val.charAt(0) != '0' || val.charAt(1) != '1') {
            phone.setError("Must be start with 01");
            return false;
        } else {
            phone.setError(null);
            return true;
        }
    }

    private Boolean validate_password_1() {
        String val = password_1.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            password_1.setError("Field can not be empty");
            return false;
        } else if (val.length() < 9) {
            password_1.setError("Must be more then 8 character");
            return false;
        } else {
            password_1.setError(null);
            return true;
        }
    }

    private Boolean validate_password_2() {
        String val_1 = password_1.getEditText().getText().toString().trim();
        String val_2 = password_2.getEditText().getText().toString().trim();
        if (val_2.isEmpty()) {
            password_2.setError("Field can not be empty");
            return false;
        } else if (!val_1.equals(val_2)) {
            password_2.setError("Password doesn't match with previous one");
            return false;
        } else {
            password_2.setError(null);
            return true;
        }
    }
    private void showHaveAccountDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You already have an Account with that phone number !\nTry with another Phone Number");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }


}