package com.example.profind;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class b_1_1_ChangePassword_page extends AppCompatActivity implements View.OnClickListener {
    Button chngpaswrd_login_btn, back_btn;
    TextInputLayout password_1, password_2;
    Intent intent;
    String user_phone;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_b_1_1_change_password_page);
        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();
        user_phone = getIntent().getStringExtra("user_phn");
        back_btn = findViewById(R.id.change_password_back_btn_id);
        password_1 = findViewById(R.id.change_password_1_edtxt_id);
        password_2 = findViewById(R.id.change_password_2_edtxt_id);
        chngpaswrd_login_btn = findViewById(R.id.change_password_page_login_btn_id);
        back_btn.setOnClickListener(this);
        chngpaswrd_login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.change_password_page_login_btn_id) {
            finish();
        } else if (view.getId() == R.id.change_password_page_login_btn_id) {
            if (!validate_password_1() | !validate_password_2()) {
                return;
            } else {
                String val_2 = password_2.getEditText().getText().toString().trim();
                db.collection("users").document(user_phone).update("Password", val_2);

                db.collection("users")
                        .whereEqualTo("Phone_Number", user_phone)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    String phon = document.getString("Phone_Number");
                                    String name = document.getString("Name");

                                    b_0_1_Session session = new b_0_1_Session(b_1_1_ChangePassword_page.this);
                                    session.createLoginSession(name, phon);

                                    intent = new Intent(getApplicationContext(), c_0_HomePage.class);
                                    startActivity(intent);

                                }
                            }
                        });
            }
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

    @Override
    public void onBackPressed() {
        showDontChangePasswordDialogue();
        //System.exit(0);
    }

    private void showDontChangePasswordDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Don't you want to change the password ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        intent = new Intent(getApplicationContext(), b_0_Login.class);
                        startActivity(intent);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}