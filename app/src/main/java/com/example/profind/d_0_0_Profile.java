package com.example.profind;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class d_0_0_Profile extends AppCompatActivity implements View.OnClickListener {
    LinearLayout warningLayout, normal_topLayout, pro_topLayout, post_layout,
            emailLayout, nidLayout, hscregLayout, univrstyLayout, addressLayout, followed_byLayout;
    Button back_btn, makeproAccount_btn, profileImage_btn, backgroundImage_btn, profileSetting_btn,
            editDetails_btn, appointments_btn, post_btn;
    TextView pro_profile_profession_name, rating_txt, profileName_txt, phoneNumber_txt, email_txt, nid_txt, hscreg_txt, university_txt,
            address_txt, followedby_txt, followings_txt;
    CircleImageView pro_profile_profession_logo, profile_imgvw, post_imgvw;
    RatingBar profile_rating_bar;
    ShapeableImageView background_imgvw;
    RecyclerView recyclerView;
    String profession, rating, name, phone, activated,pro_account;
    String urlp, urlc;
    boolean bol;
    d_0_0_11_postsAdapter adapter;
    Intent intent;


    FirebaseStorage storage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docIdRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_0_0_profile);
        getSupportActionBar().hide();

        storage = FirebaseStorage.getInstance();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        name = userDetails.get(b_0_1_Session.KEY_NAME);
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        profile_rating_bar = findViewById(R.id.pro_profile_page_ratingbar_id);

        warningLayout = findViewById(R.id.profile_page_warning_layout_id);
        normal_topLayout = findViewById(R.id.profile_page_top_layout_1_id);
        pro_topLayout = findViewById(R.id.profile_page_top_layout_2_id);
        post_layout = findViewById(R.id.profile_page_post_layout_id);
        emailLayout = findViewById(R.id.profile_page_profile_emaillayout_id);
        nidLayout = findViewById(R.id.profile_page_profile_nidlayout_id);
        hscregLayout = findViewById(R.id.profile_page_profile_hscregnumberlayout_id);
        univrstyLayout = findViewById(R.id.profile_page_profile_universitylayout_id);
        addressLayout = findViewById(R.id.profile_page_profile_addresslayout_id);
        followed_byLayout = findViewById(R.id.profile_page_profile_followedbylayout_id);

        back_btn = findViewById(R.id.profile_page_back_btn_id);
        makeproAccount_btn = findViewById(R.id.profile_page_pro_account_btn_id);
        profileImage_btn = findViewById(R.id.profile_page_profile_img_btn_id);
        backgroundImage_btn = findViewById(R.id.profile_page_background_img_btn_id);
        profileSetting_btn = findViewById(R.id.profile_page_profile_setting_btn_id);
        editDetails_btn = findViewById(R.id.profile_page_edit_details_btn_id);
        appointments_btn = findViewById(R.id.profile_page_appointments_btn_id);
        post_btn = findViewById(R.id.pro_profile_page_post_anything_btn_id);

        pro_profile_profession_name = findViewById(R.id.pro_profile_page_pro_account_txtvw_id);
        rating_txt = findViewById(R.id.pro_profile_page_rating_number_id);
        profileName_txt = findViewById(R.id.profile_page_profile_name_id);
        profileName_txt.setText(name);
        phoneNumber_txt = findViewById(R.id.profile_page_profile_phone_number_id);
        phoneNumber_txt.setText(phone);
        email_txt = findViewById(R.id.profile_page_profile_email_id);
        nid_txt = findViewById(R.id.profile_page_profile_nid_id);
        hscreg_txt = findViewById(R.id.profile_page_profile_hscregnumber_id);
        university_txt = findViewById(R.id.profile_page_profile_university_id);
        address_txt = findViewById(R.id.profile_page_profile_address_id);

        followedby_txt = findViewById(R.id.profile_page_profile_followedby_id);
        followedby_txt.setText("Followed by " + 0 + " people");
        followings_txt = findViewById(R.id.profile_page_profile_following_id);
        followings_txt.setText("Following " + 0 + " people");

        pro_profile_profession_logo = findViewById(R.id.pro_profile_page_pro_account_logo_id);
        profile_imgvw = findViewById(R.id.profile_page_profile_img_id);
        background_imgvw = findViewById(R.id.profile_page_background_img_id);
        post_imgvw = findViewById(R.id.profile_page_post_anything_image_id);

        recyclerView = findViewById(R.id.pro_profile_page_recyclerview_id);

        docIdRef = db.collection("Requests").document(phone);
        db.collection("users")
                .whereEqualTo("Phone_Number", phone)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            int adrslayout_width = LinearLayout.LayoutParams.MATCH_PARENT;
                            int adrslayout_height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            int adrslayout_height2 = LinearLayout.LayoutParams.MATCH_PARENT;
                            LinearLayout.LayoutParams lp_empty = new LinearLayout.LayoutParams(0, 0);
                            LinearLayout.LayoutParams lp_not_empty = new LinearLayout.LayoutParams(adrslayout_width, adrslayout_height);
                            LinearLayout.LayoutParams lp_not_empty2 = new LinearLayout.LayoutParams(adrslayout_width, adrslayout_height2);
                            pro_account=document.getString("Pro_Account");

                            if (!document.getString("Pro_Account").equals("Yes")) {
                                normal_topLayout.setLayoutParams(lp_not_empty2);
                                post_layout.setLayoutParams(lp_empty);
                            }

                            if (document.getString("Pro_Account").equals("Yes")) {

                                pro_topLayout.setLayoutParams(lp_not_empty2);
                                post_layout.setLayoutParams(lp_not_empty2);
                                profession = document.getString("Profession");
                                rating = document.getString("Rating");
                                pro_profile_profession_name.setText(profession);
                                if (profession.equals("Builder")) {
                                    pro_profile_profession_logo.setImageResource(R.drawable.builder_icon);
                                } else if (profession.equals("Doctor")) {
                                    pro_profile_profession_logo.setImageResource(R.drawable.doctor_icon);
                                } else if (profession.equals("Lawyer")) {
                                    pro_profile_profession_logo.setImageResource(R.drawable.lawyer_icon);
                                } else if (profession.equals("Teacher")) {
                                    pro_profile_profession_logo.setImageResource(R.drawable.teacher_icon);
                                }
                                profile_rating_bar.setRating(Float.parseFloat(rating));
                                rating_txt.setText(rating + "/5");
                            }

                            if (!document.getString("Profile_Photo").isEmpty()) {
                                urlp = document.getString("Profile_Photo");
                                Picasso.get()
                                        .load(urlp)
                                        .placeholder(R.drawable.profile_img)
                                        .into(profile_imgvw);
                                Picasso.get()
                                        .load(urlp)
                                        .placeholder(R.drawable.profile_img)
                                        .into(post_imgvw);
                            }
                            if (!document.getString("Cover_Photo").isEmpty()) {
                                urlc = document.getString("Cover_Photo");
                                Picasso.get()
                                        .load(urlc)
                                        .placeholder(R.drawable.background_img)
                                        .into(background_imgvw);
                            }

                            String adrs = document.getString("Address");
                            String email = document.getString("Email");
                            String HscRN = document.getString("Hsc_reg_number");
                            String nid = document.getString("Nid_number");
                            String univrsty = document.getString("University");
                            String followed_by = document.getString("Followed_by");
                            String following = document.getString("Following");

                            activated = document.getString("Activated");
                            bol = activated.equals("true");

                            if (!bol) {
                                warningLayout.setLayoutParams(lp_not_empty);
                            }
                            if (!email.isEmpty()) {
                                emailLayout.setLayoutParams(lp_not_empty);
                                email_txt.setText(email);
                            }
                            if (!nid.isEmpty()) {
                                nidLayout.setLayoutParams(lp_not_empty);
                                nid_txt.setText(nid);
                            }
                        if (!HscRN.isEmpty()) {
                                hscregLayout.setLayoutParams(lp_not_empty);
                                hscreg_txt.setText(HscRN);
                            }
                            if (!univrsty.isEmpty()) {
                                univrstyLayout.setLayoutParams(lp_not_empty);
                                university_txt.setText(univrsty);
                            }
                            if (!adrs.isEmpty()) {
                                addressLayout.setLayoutParams(lp_not_empty);
                                address_txt.setText(adrs);
                            }
                            if (!followed_by.isEmpty()) {
                                followed_byLayout.setLayoutParams(lp_not_empty);
                                followedby_txt.setText("Followed by " + followed_by + " people");
                            }
                            if (!following.isEmpty()) {
                                followings_txt.setText("Following " + following + " people");
                            }


                        }
                    }
                });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManagerWrapper(getApplicationContext(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(mLayoutManager);

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width_half = (dm.widthPixels / 2) - 5;

        Query query = db.collection("posts").whereEqualTo("phone", phone)
                .orderBy("posted_at", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<d_0_0_10_posts_model> datalist = new
                FirestoreRecyclerOptions.Builder<d_0_0_10_posts_model>()
                .setQuery(query, d_0_0_10_posts_model.class)
                .build();

        adapter = new d_0_0_11_postsAdapter(datalist, width_half);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        warningLayout.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        makeproAccount_btn.setOnClickListener(this);
        profileImage_btn.setOnClickListener(this);
        backgroundImage_btn.setOnClickListener(this);
        profileSetting_btn.setOnClickListener(this);
        editDetails_btn.setOnClickListener(this);
        appointments_btn.setOnClickListener(this);
        post_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.profile_page_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.profile_page_warning_layout_id) {
            intent = new Intent(this, d_0_0_5_Profile_setting.class);
            startActivity(intent);
        } else if (view.getId() == R.id.profile_page_pro_account_btn_id && bol) {
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            showProAcountDialogue();
                            //Toast.makeText(getApplicationContext(), "You already have applied !\nWaite for 5-72 hours", Toast.LENGTH_SHORT).show();
                        } else {
                            intent = new Intent(getApplicationContext(), d_0_0_6_make_pro_account.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed !", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        // Profile image
        else if (view.getId() == R.id.profile_page_profile_img_btn_id) {
            BottomSheetDialog sheetDialog = new BottomSheetDialog(this,
                    R.style.BottomSheetDialogTheme);
            View sheetView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.activity_d_0_0_1_profile_bottomsheet_1,
                            findViewById(R.id.profile_img_bottomsheet));
            sheetView.findViewById(R.id.profile_img_bottomsheet_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (urlp.equals("")) {
                        Toast.makeText(getApplicationContext(), "No Profile Image exist !", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), d_0_0_3_Full_image_view.class);
                        intent.putExtra("fullimgkey", urlp);
                        startActivity(intent);
                    }
                    sheetDialog.dismiss();
                }
            });
            sheetView.findViewById(R.id.profile_img_bottomsheet_upload).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, 10);
                    sheetDialog.dismiss();
                }
            });
            sheetView.findViewById(R.id.profile_img_bottomsheet_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (urlp.equals("")) {
                        Toast.makeText(getApplicationContext(), "No Profile Image exist !", Toast.LENGTH_SHORT).show();
                    } else {
                        final StorageReference storageRef = storage.getReference().child("profile_photo").child(phone);
                        storageRef.delete();
                        db.collection("users").document(phone).update("Profile_Photo", "");
                        Toast.makeText(getApplicationContext(), "Successfully Deleted\nRefresh the page", Toast.LENGTH_SHORT).show();
                    }
                    sheetDialog.dismiss();
                }
            });
            sheetDialog.setContentView(sheetView);
            sheetDialog.show();

        }


        // Background Image
        else if (view.getId() == R.id.profile_page_background_img_btn_id) {
            BottomSheetDialog sheetDialog = new BottomSheetDialog(this,
                    R.style.BottomSheetDialogTheme);
            View sheetView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.activity_d_0_0_1_profile_bottomsheet_2,
                            findViewById(R.id.background_img_bottomsheet));

            sheetView.findViewById(R.id.background_img_bottomsheet_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (urlc != null) {
                        Intent intent = new Intent(getApplicationContext(), d_0_0_3_Full_image_view.class);
                        intent.putExtra("fullimgkey", urlc);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "No background Image exist !", Toast.LENGTH_SHORT).show();
                    }
                    sheetDialog.dismiss();
                }
            });
            sheetView.findViewById(R.id.background_img_bottomsheet_upload).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, 11);
                    sheetDialog.dismiss();
                }
            });
            sheetView.findViewById(R.id.background_img_bottomsheet_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (urlc != null) {
                        final StorageReference storageRef = storage.getReference().child("cover_photo").child(phone);
                        storageRef.delete();
                        db.collection("users").document(phone).update("Cover_Photo", "");
                        Toast.makeText(getApplicationContext(), "Successfully Deleted\nRefresh the page", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "No background Image exist !", Toast.LENGTH_SHORT).show();
                    }
                    sheetDialog.dismiss();
                }
            });
            sheetDialog.setContentView(sheetView);
            sheetDialog.show();

        } else if (view.getId() == R.id.profile_page_profile_setting_btn_id) {
            intent = new Intent(this, d_0_0_5_Profile_setting.class);
            startActivity(intent);

        } else if (view.getId() == R.id.profile_page_edit_details_btn_id && bol) {
            intent = new Intent(this, d_0_0_2_Edit_details_profile.class);
            startActivity(intent);

        } else if (view.getId() == R.id.profile_page_appointments_btn_id && bol) {
            if(pro_account.equals("Yes")){
                intent = new Intent(this, d_0_0_4_Appointments.class);
                startActivity(intent);
            }else {
                intent = new Intent(this, d_0_0_4_4_asaclient.class);
                startActivity(intent);
            }

        } else if (view.getId() == R.id.pro_profile_page_post_anything_btn_id && bol) {
            intent = new Intent(this, d_0_0_7_post_anything.class);
            startActivity(intent);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 10 && data.getData() != null) {
                Uri uri = data.getData();
                profile_imgvw.setImageURI(uri);

                final StorageReference storageRef = storage.getReference().child("profile_photo")
                        .child(phone);
                storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                db.collection("users").document(phone).update("Profile_Photo", uri.toString());
                            }
                        });
                    }
                });

            } else if (requestCode == 11 && data.getData() != null) {

                Uri uri = data.getData();
                background_imgvw.setImageURI(uri);

                final StorageReference storageRef = storage.getReference().child("cover_photo")
                        .child(phone);
                storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                db.collection("users").document(phone).update("Cover_Photo", uri.toString());
                            }
                        });
                    }
                });

            }
        } catch (Exception e) {

        }

    }


    //Custom diagoues
    private void showProAcountDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You already have applied !\nWaite for 5-72 hours\nDo you want to delete the application ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final StorageReference storageRef = storage.getReference().child("Requests_photo/" + phone);
                        storageRef.delete();
                        db.collection("Requests").document(phone).delete();
                        Toast.makeText(getApplicationContext(), "Application deleted successfully", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
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

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}