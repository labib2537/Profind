package com.example.profind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class b_2_Otp extends AppCompatActivity implements View.OnClickListener {
    PinView pinFromUser;
    Button otp_login_btn;
    Intent intent;
    String codeBySystem;
    String name, phone, password;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_b_2_otp);
        getSupportActionBar().hide();

        pinFromUser = findViewById(R.id.otp_pnvw_id);
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phn");
        password = getIntent().getStringExtra("pswrd");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        sendVerifivationCodeUser(phone);
        otp_login_btn = findViewById(R.id.otp_login_btn_id);
        otp_login_btn.setOnClickListener(this);
    }

    private void sendVerifivationCodeUser(String phoneNo) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinFromUser.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    showTryagainLaterDialogue();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DocumentReference DR = db.collection("users").document(phone);
                            // Create a new user with a first and last name
                            Map<String, String> user = new HashMap<>();
                            user.put("Phone_Number", phone);
                            user.put("Name", name);
                            user.put("Name_lower", name.toLowerCase());
                            user.put("Password", password);
                            user.put("Activated", "true");
                            user.put("Address", "");
                            user.put("Cover_Photo", "");
                            user.put("Profile_Photo", "");
                            user.put("Email", "");
                            user.put("Hsc_reg_number", "");
                            user.put("Nid_number", "");
                            user.put("Rating", "");
                            user.put("Profession", "");
                            user.put("Pro_Account", "No");
                            user.put("University", "");
                            user.put("Followed_by", "0");
                            user.put("Following", "0");


                            // Add a new document with a generated ID
                            db.collection("users")
                                    .document(phone)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            b_0_1_Session session = new b_0_1_Session(b_2_Otp.this);
                                            session.createLoginSession(name, phone);

                                            intent = new Intent(getApplicationContext(), c_0_HomePage.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showTryagainLaterDialogue();
                                        }
                                    });


                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                showVarificationNotCompletedDialogue();
                            }
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {

        String code = pinFromUser.getText().toString();
        if (!code.isEmpty()) {
            verifyCode(code);
        }

    }
    private void showVarificationNotCompletedDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Verification Not Completed!\nDo you want to try again ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        intent = new Intent(getApplicationContext(),b_1_CreateAccount.class);
                        startActivity(intent);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
    private void showVarificationNotCompletedWaiteDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Verification Not Completed!\nDo you want to waite for a while ?");
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
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
    private void showTryagainLaterDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Too many attempt with that Phone Number!\nTry again after a while ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        intent = new Intent(getApplicationContext(),b_0_Login.class);
                        startActivity(intent);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onBackPressed() {
        showVarificationNotCompletedWaiteDialogue();
        //System.exit(0);
    }
}