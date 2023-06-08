package com.example.profind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class d_0_0_6_make_pro_account extends AppCompatActivity implements View.OnClickListener {
    FirebaseFirestore db;
    FirebaseStorage storage;

    Uri passport_size_img_uri, nid_img_uri, certificate_img_uri;
    String phone;
    TextInputLayout email, hsc_reg_bumber, nid_number, university_name, present_address;
    String email_value, reg_number_value, nid_number_value, univrsty_name_value, address_value;
    ImageView passPort_photo, nid_card_photo, university_certificate_photo;
    Button back_btn, passPort_photo_btn, nid_card_photo_btn, university_certificate_photo_btn, register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_0_0_6_make_pro_account);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        email = findViewById(R.id.make_pro_acnt_page_email_inptlout_id);
        hsc_reg_bumber = findViewById(R.id.make_pro_acnt_page_reg_number_inptlout_id);
        nid_number = findViewById(R.id.make_pro_acnt_page_nid_number_inptlout_id);
        university_name = findViewById(R.id.make_pro_acnt_page_institution_inptlout_id);
        present_address = findViewById(R.id.make_pro_acnt_page_address_inptlout_id);

        passPort_photo = findViewById(R.id.make_pro_acnt_page_passport_imgvw_id);
        nid_card_photo = findViewById(R.id.make_pro_acnt_page_nid_imgvw_id);
        university_certificate_photo = findViewById(R.id.make_pro_acnt_page_uni_certificate_imgvw_id);

        back_btn = findViewById(R.id.make_pro_acnt_page_back_btn_id);
        passPort_photo_btn = findViewById(R.id.make_pro_acnt_page_passport_btn_id);
        nid_card_photo_btn = findViewById(R.id.make_pro_acnt_page_nid_btn_id);
        university_certificate_photo_btn = findViewById(R.id.make_pro_acnt_page_uni_certificate_btn_id);
        register_btn = findViewById(R.id.make_pro_acnt_page_register_btn_id);


        back_btn.setOnClickListener(this);
        passPort_photo_btn.setOnClickListener(this);
        nid_card_photo_btn.setOnClickListener(this);
        university_certificate_photo_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.make_pro_acnt_page_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.make_pro_acnt_page_passport_btn_id) {
            ImagePicker.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start(11);
        } else if (view.getId() == R.id.make_pro_acnt_page_nid_btn_id) {
            ImagePicker.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)//Final image resolution will be less than 1080 x 1080(Optional)
                    .start(12);
        } else if (view.getId() == R.id.make_pro_acnt_page_uni_certificate_btn_id) {
            ImagePicker.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start(13);
        } else if (view.getId() == R.id.make_pro_acnt_page_register_btn_id) {
            if (!validate_email() | !validate_hsc_reg() | !validate_nid() | !validate_univrsty() | !validate_address()) {
                return;
            } else if (passport_size_img_uri == null | nid_img_uri == null | certificate_img_uri == null) {
                Toast.makeText(getApplicationContext(), "Any 'Photo' field can nnot be empty !", Toast.LENGTH_SHORT).show();
            } else {
                Map<String, String> user_request = new HashMap<>();
                user_request.put("1.Phone", phone);
                user_request.put("2.Email", email_value);
                user_request.put("3.Hsc_reg_number", reg_number_value);
                user_request.put("4.Nid_number", nid_number_value);
                user_request.put("5.University", univrsty_name_value);
                user_request.put("6.Address", address_value);

                db.collection("Requests")
                        .document(phone)
                        .set(user_request)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Successfully Applied !", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Faild Submission !\nTry again later", Toast.LENGTH_SHORT).show();
                            }
                        });

                final StorageReference storageRef1, storageRef2, storageRef3;
                storageRef1 = storage.getReference().child("Requests_photo").child(phone).child("passport_size_photo");
                storageRef1.putFile(passport_size_img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                db.collection("Requests").document(phone).update("7.Passport_size_photo", uri.toString());
                            }
                        });
                    }
                });
                storageRef2 = storage.getReference().child("Requests_photo").child(phone).child("nid_photo");
                storageRef2.putFile(nid_img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                db.collection("Requests").document(phone).update("7.Nid_card_photo", uri.toString());
                            }
                        });
                    }
                });
                storageRef3 = storage.getReference().child("Requests_photo").child(phone).child("7.certificate_photo");
                storageRef3.putFile(certificate_img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                db.collection("Requests").document(phone).update("7.Certificate_photo", uri.toString());
                            }
                        });
                    }
                });

            }

        }
    }

    private Boolean validate_email() {
        email_value = email.getEditText().getText().toString().trim();
        Pattern p = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
        Matcher m = p.matcher(email_value);
        if (email_value.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        } else if (!m.matches()) {
            email.setError("Must be in abc#@domain.com formate");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private Boolean validate_hsc_reg() {
        reg_number_value = hsc_reg_bumber.getEditText().getText().toString().trim();
        if (reg_number_value.isEmpty()) {
            hsc_reg_bumber.setError("Field can not be empty");
            return false;
        } else {
            hsc_reg_bumber.setError(null);
            return true;
        }
    }

    private Boolean validate_nid() {
        nid_number_value = nid_number.getEditText().getText().toString().trim();
        if (nid_number_value.isEmpty()) {
            nid_number.setError("Field can not be empty");
            return false;
        } else {
            nid_number.setError(null);
            return true;
        }
    }

    private Boolean validate_univrsty() {
        univrsty_name_value = university_name.getEditText().getText().toString().trim();
        if (univrsty_name_value.isEmpty()) {
            university_name.setError("Field can not be empty");
            return false;
        } else {
            university_name.setError(null);
            return true;
        }
    }

    private Boolean validate_address() {
        address_value = present_address.getEditText().getText().toString().trim();
        if (address_value.isEmpty()) {
            present_address.setError("Field can not be empty");
            return false;
        } else {
            present_address.setError(null);
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 11 && data.getData() != null) {
                passport_size_img_uri = data.getData();
                passPort_photo.setPadding(15, 15, 15, 15);
                passPort_photo.setImageURI(passport_size_img_uri);

            } else if (requestCode == 12 && data.getData() != null) {

                nid_img_uri = data.getData();
                nid_card_photo.setPadding(15, 15, 15, 15);
                nid_card_photo.setImageURI(nid_img_uri);

            } else if (requestCode == 13 && data.getData() != null) {

                certificate_img_uri = data.getData();
                university_certificate_photo.setPadding(15, 15, 15, 15);
                university_certificate_photo.setImageURI(certificate_img_uri);

            }
        } catch (Exception e) {

        }

    }
}