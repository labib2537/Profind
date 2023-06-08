package com.example.profind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class e_1_12_Group_rename extends AppCompatActivity implements View.OnClickListener {

    Button back_btn, save_btn;
    TextInputLayout rename_edtxt;
    String group_id;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_e_1_12_group_rename);
        getSupportActionBar().hide();

        back_btn = findViewById(R.id.group_rename_page_back_btn_id);
        save_btn = findViewById(R.id.group_rename_page_save_btn_id);
        rename_edtxt = findViewById(R.id.group_rename_page_name_edtxt_id);

        group_id = getIntent().getStringExtra("group_id_key");

        db = FirebaseFirestore.getInstance();
        db.collection("groups")
                .document(group_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                String nam = document.getString("group_name");
                                rename_edtxt.getEditText().setText(nam);

                            }
                        }
                    }
                });


        back_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.group_rename_page_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.group_rename_page_save_btn_id) {
            if (!validate_grp_name()) {
                return;
            }

            String given_name = rename_edtxt.getEditText().getText().toString().trim();
            db.collection("groups").document(group_id).update("group_name", given_name);
            db.collection("groups").document(group_id).update("group_name_low", given_name.toLowerCase());
            Toast.makeText(getApplicationContext(), "Successfully Renamed", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private boolean validate_grp_name() {
        String val = rename_edtxt.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            rename_edtxt.setError("Field can not be empty");
            return false;
        } else if (val.length() < 4) {
            rename_edtxt.setError("Must be more then 3 character");
            return false;
        } else if (!(Character.isUpperCase(val.charAt(0)))) {
            rename_edtxt.setError("First latter must be upper case");
            return false;
        } else {
            rename_edtxt.setError(null);
            return true;
        }
    }
}