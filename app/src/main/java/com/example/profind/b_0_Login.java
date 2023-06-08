package com.example.profind;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class b_0_Login extends AppCompatActivity implements View.OnClickListener {
    Button create_account_btn, forgot_password_btn, login_btn;
    Intent intent;
    TextInputLayout phone, password;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressBar progressBar;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_b_0_login);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        if (session.checkLogin()) {
            intent = new Intent(getApplicationContext(), c_0_HomePage.class);
            startActivity(intent);
        }else {
            progressBar=new ProgressBar(this);
            progressDialog=new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgressDrawable(null);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            forgot_password_btn = findViewById(R.id.login_page_forgot_password_btn_id);
            phone = findViewById(R.id.login_page_phone_number_id);
            password = findViewById(R.id.login_page_password_id);
            create_account_btn = findViewById(R.id.login_page_create_account_btn_id);
            login_btn = findViewById(R.id.login_page_login_btn_id);

            create_account_btn.setOnClickListener(this);
            login_btn.setOnClickListener(this);
            forgot_password_btn.setOnClickListener(this);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (!isInternetConnected(this)) {
            showInternetConnectionDialogue();
            return;
        } else if (view.getId() == R.id.login_page_login_btn_id) {
            if (!validate_phone_and_password()) {
                return;
            } else {
                progressDialog.show();
                String phonen = phone.getEditText().getText().toString().trim();
                String _phone = "+88" + phonen;
                String _password = password.getEditText().getText().toString().trim();

                db.collection("users")
                        .whereEqualTo("Phone_Number", _phone)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.isEmpty()) {
                                    progressDialog.dismiss();
                                    showAccountDontexistDialogue();
                                } else {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        phone.setError(null);
                                        phone.setErrorEnabled(false);
                                        String phon = document.getString("Phone_Number");
                                        String passwrd = document.getString("Password");

                                        if (passwrd.equals(_password)) {
                                            password.setError(null);
                                            password.setErrorEnabled(false);

                                            String name = document.getString("Name");

                                            b_0_1_Session session = new b_0_1_Session(b_0_Login.this);
                                            session.createLoginSession(name, phon);

                                            progressDialog.dismiss();

                                            intent = new Intent(getApplicationContext(), c_0_HomePage.class);
                                            startActivity(intent);
                                        } else {
                                            progressDialog.dismiss();
                                            showIncorrectPasswordDialogue();
                                        }
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }

        } else if (view.getId() == R.id.login_page_forgot_password_btn_id) {
            intent = new Intent(this, b_1_0_ChangePassword_phone_page.class);
            startActivity(intent);
        } else if (view.getId() == R.id.login_page_create_account_btn_id) {
            intent = new Intent(this, b_1_CreateAccount.class);
            startActivity(intent);
        }
    }

    private void showInternetConnectionDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Connect to the Internet");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Connect",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });

        builder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showAccountDontexistDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No account exist with that phone number !\nDo you want to create an account ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        intent = new Intent(getApplicationContext(), b_1_CreateAccount.class);
                        startActivity(intent);
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showIncorrectPasswordDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Password is Incorrect");
        builder.setCancelable(true);

        builder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isInternetConnected(b_0_Login b_0_login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) b_0_login.getSystemService(b_0_Login.CONNECTIVITY_SERVICE);
        NetworkInfo wificonnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo ethernetconnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if ((wificonnection != null && wificonnection.isConnected()) || (mobileconnection != null && mobileconnection.isConnected())
                || (ethernetconnection != null && ethernetconnection.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean validate_phone_and_password() {
        String val = phone.getEditText().getText().toString().trim();
        String val_2 = password.getEditText().getText().toString().trim();
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
        } else if (val_2.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        } else {
            phone.setError(null);
            password.setError(null);
            return true;
        }
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
        //System.exit(0);
    }
}