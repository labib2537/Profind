package com.example.profind;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class e_1_4_Group_create extends AppCompatActivity implements View.OnClickListener {
    Button back_btn, grp_create_btn, banner_btn;
    ShapeableImageView banner_img;
    TextInputLayout grp_name;
    String phone;
    Uri uri;
    ProgressBar progressBar;
    ProgressDialog progressDialog;

    FirebaseStorage storage;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_e_1_4_group_create);
        getSupportActionBar().hide();

        progressBar = new ProgressBar(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgressDrawable(null);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        back_btn = findViewById(R.id.create_grp_page_back_btn_id);
        grp_create_btn = findViewById(R.id.create_grp_page_create_btn_id);
        banner_btn = findViewById(R.id.create_grp_page_banner_btn_id);
        banner_img = findViewById(R.id.create_grp_page_banner_id);
        grp_name = findViewById(R.id.create_grp_page_grp_name_edtxt_id);

        grp_create_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        banner_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.create_grp_page_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.create_grp_page_banner_btn_id) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 11);
        } else if (view.getId() == R.id.create_grp_page_create_btn_id) {
            if (!validate_grp_name()) {
                return;
            }
            if (uri == null) {
                Toast.makeText(getApplicationContext(), "You must have set group banner", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.show();
                List<String> members = new ArrayList<>();
                List<String> invited = new ArrayList<>();
                members.add(phone);
                invited.add(phone);
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
                String given_name = grp_name.getEditText().getText().toString().trim();
                final StorageReference storageRef = storage.getReference().child("groups")
                        .child(phone)
                        .child(given_name)
                        .child("grp_banner");
                storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri urii) {
                                e_1_4_0_Group_create_model grp_create = new e_1_4_0_Group_create_model(urii.toString(), given_name
                                        , given_name.toLowerCase(), phone, dateFormat.format(date).toString(), "1", members, invited);
                                db.collection("groups").add(grp_create).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        DocumentReference docIdRef;
                                        String grp_id = documentReference.getId();
                                        docIdRef=db.collection("users").document(phone);
                                        docIdRef.update("groups", FieldValue.arrayUnion(grp_id));
                                    }
                                });
                                progressDialog.dismiss();
                                finish();
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 11 && data.getData() != null) {
                uri = data.getData();
                banner_img.setImageURI(uri);
            }
        } catch (Exception e) {

        }

    }

    private boolean validate_grp_name() {
        String val = grp_name.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            grp_name.setError("Field can not be empty");
            return false;
        } else if (val.length() < 4) {
            grp_name.setError("Must be more then 3 character");
            return false;
        } else if (!(Character.isUpperCase(val.charAt(0)))) {
            grp_name.setError("First latter must be upper case");
            return false;
        } else {
            grp_name.setError(null);
            return true;
        }
    }
}