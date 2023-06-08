package com.example.profind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class d_0_0_Profile_view extends AppCompatActivity implements View.OnClickListener {

    LinearLayout pro_topLayout, post_layout,
            emailLayout, nidLayout, hscregLayout, univrstyLayout, addressLayout, followed_byLayout;
    Button back_btn, profileImage_btn, backgroundImage_btn, follow_btn, massage_btn, appointment_btn;
    TextView pro_profile_profession_name, rating_txt, profileName_txt, phoneNumber_txt, email_txt, nid_txt, hscreg_txt, university_txt,
            address_txt, followedby_txt, followings_txt, follow_txt;
    CircleImageView pro_profile_profession_logo, profile_imgvw;
    RatingBar profile_rating_bar;
    ShapeableImageView background_imgvw;
    RecyclerView recyclerView;
    String profession, rating, name, phone, user_phone;
    String urlp, urlc;
    int is_follwing, followed_by, present_following;
    Map<String, String> follown = new HashMap<>();
    Map<String, String> followr = new HashMap<>();
    boolean bol;
    d_0_0_11_postsAdapter adapter;
    Intent intent;


    FirebaseStorage storage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_0_0_profile_view);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        user_phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        phone = getIntent().getStringExtra("profile_id_key");
        storage = FirebaseStorage.getInstance();

        profile_rating_bar = findViewById(R.id.view_pro_profile_page_ratingbar_id);

        pro_topLayout = findViewById(R.id.view_profile_page_top_layout_2_id);
        post_layout = findViewById(R.id.view_profile_page_post_layout_id);
        emailLayout = findViewById(R.id.view_profile_page_profile_emaillayout_id);
        nidLayout = findViewById(R.id.view_profile_page_profile_nidlayout_id);
        hscregLayout = findViewById(R.id.view_profile_page_profile_hscregnumberlayout_id);
        univrstyLayout = findViewById(R.id.view_profile_page_profile_universitylayout_id);
        addressLayout = findViewById(R.id.view_profile_page_profile_addresslayout_id);
        followed_byLayout = findViewById(R.id.view_profile_page_profile_followedbylayout_id);

        back_btn = findViewById(R.id.view_profile_page_back_btn_id);
        profileImage_btn = findViewById(R.id.view_profile_page_profile_img_btn_id);
        backgroundImage_btn = findViewById(R.id.view_profile_page_background_img_btn_id);
        follow_btn = findViewById(R.id.view_profile_page_follow_btn_id);
        massage_btn = findViewById(R.id.view_profile_page_massage_btn_id);
        appointment_btn = findViewById(R.id.view_profile_page_appointment_btn_id);

        pro_profile_profession_name = findViewById(R.id.view_pro_profile_page_pro_account_txtvw_id);
        rating_txt = findViewById(R.id.view_pro_profile_page_rating_number_id);
        profileName_txt = findViewById(R.id.view_profile_page_profile_name_id);
        phoneNumber_txt = findViewById(R.id.view_profile_page_profile_phone_number_id);
        email_txt = findViewById(R.id.view_profile_page_profile_email_id);
        nid_txt = findViewById(R.id.view_profile_page_profile_nid_id);
        hscreg_txt = findViewById(R.id.view_profile_page_profile_hscregnumber_id);
        university_txt = findViewById(R.id.view_profile_page_profile_university_id);
        address_txt = findViewById(R.id.view_profile_page_profile_address_id);

        follow_txt = findViewById(R.id.view_profile_page_follow_txtvw_id);
        followedby_txt = findViewById(R.id.view_profile_page_profile_followedby_id);
        followedby_txt.setText("Followed by " + 0 + " people");
        followings_txt = findViewById(R.id.view_profile_page_profile_following_id);
        followings_txt.setText("Following " + 0 + " people");

        pro_profile_profession_logo = findViewById(R.id.view_pro_profile_page_pro_account_logo_id);
        profile_imgvw = findViewById(R.id.view_profile_page_profile_img_id);
        background_imgvw = findViewById(R.id.view_profile_page_background_img_id);

        recyclerView = findViewById(R.id.view_pro_profile_page_recyclerview_id);


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
                            name = document.getString("Name");
                            profileName_txt.setText(name);
                            phone = document.getString("Phone_Number");
                            phoneNumber_txt.setText(phone);

                            if (!document.getString("Pro_Account").equals("Yes")) {
                                post_layout.setLayoutParams(lp_empty);
                                follow_btn.setLayoutParams(lp_empty);
                                follow_txt.setLayoutParams(lp_empty);
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
                            followed_by = Integer.parseInt(document.getString("Followed_by"));
                            String following = document.getString("Following");



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
                            if (!document.getString("Followed_by").isEmpty()) {
                                followed_byLayout.setLayoutParams(lp_not_empty);
                                followedby_txt.setText("Followed by " + followed_by + " people");
                            }
                            if (!following.isEmpty()) {
                                followings_txt.setText("Following " + following + " people");
                            }

                        }
                    }
                });



        DocumentReference addedDocRef = db.collection("followings").document(user_phone);
        addedDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String res = documentSnapshot.getString(phone);
                    if (res == null) {
                        res = "false";
                    }
                    if (res.equals("true")) {
                        follow_txt.setBackgroundResource(R.drawable.custom_button_12);
                        follow_txt.setText("  Following");
                        follow_txt.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_following_icon_2, 0, 0, 0);
                        is_follwing = 1;
                    } else if (res.equals("false")) {
                        follow_txt.setBackgroundResource(R.drawable.custom_button_11);
                        follow_txt.setText("  Follow");
                        follow_txt.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_follow_icon_2, 0, 0, 0);
                        is_follwing = 0;
                    }
                }
            }
        });
        db.collection("users")
                .whereEqualTo("Phone_Number", user_phone)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            present_following = Integer.parseInt(document.getString("Following"));
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

        back_btn.setOnClickListener(this);
        profileImage_btn.setOnClickListener(this);
        backgroundImage_btn.setOnClickListener(this);
        follow_btn.setOnClickListener(this);
        massage_btn.setOnClickListener(this);
        appointment_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.view_profile_page_back_btn_id) {
            finish();
        }
        // Profile image
        else if (view.getId() == R.id.view_profile_page_profile_img_btn_id) {
            if (urlp != null) {
                Intent intent = new Intent(getApplicationContext(), d_0_0_3_Full_image_view.class);
                intent.putExtra("fullimgkey", urlp);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "No Profile Image exist !", Toast.LENGTH_SHORT).show();
            }

        }

        // Background Image
        else if (view.getId() == R.id.view_profile_page_background_img_btn_id) {
            if (urlc != null) {
                Intent intent = new Intent(getApplicationContext(), d_0_0_3_Full_image_view.class);
                intent.putExtra("fullimgkey", urlc);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "No background Image exist !", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.view_profile_page_follow_btn_id) {

            if (is_follwing == 0) {
                is_follwing = 1;
                followed_by = followed_by + 1;
                present_following = present_following + 1;
                follown.put(phone, "true");
                followr.put(user_phone, "true");

                db.collection("followings").document(user_phone).set(follown, SetOptions.merge());
                db.collection("users").document(user_phone).update("Following", Integer.toString(present_following))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                follow_txt.setBackgroundResource(R.drawable.custom_button_12);
                                follow_txt.setText("  Following");
                                follow_txt.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_following_icon_2, 0, 0, 0);
                            }
                        });
                db.collection("followers")
                        .document(phone)
                        .set(followr, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                db.collection("users").document(phone).update("Followed_by", Integer.toString(followed_by));
                            }
                        });

                followr.clear();
                follown.clear();

            } else if (is_follwing == 1) {
                is_follwing = 0;
                followed_by = followed_by - 1;
                present_following = present_following - 1;

                db.collection("followings").document(user_phone).update(phone, "false");
                db.collection("users").document(user_phone).update("Following", Integer.toString(present_following))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                follow_txt.setBackgroundResource(R.drawable.custom_button_11);
                                follow_txt.setText("  Follow");
                                follow_txt.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_follow_icon_2, 0, 0, 0);
                            }
                        });
                db.collection("followers")
                        .document(phone).update(user_phone, "false")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                db.collection("users").document(phone).update("Followed_by", Integer.toString(followed_by));
                            }
                        });

            }

        } else if (view.getId() == R.id.view_profile_page_massage_btn_id) {
            intent = new Intent(view.getContext(), d_2_0_Massage_person.class);
            intent.putExtra("profile_id_key", phone);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        } else if (view.getId() == R.id.view_profile_page_appointment_btn_id) {
            db.collection("appointment_schedule")
                    .whereEqualTo("phone", phone)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Doesn't have any Schedule yet !", Toast.LENGTH_SHORT).show();
                            } else {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    if (document.getString("appointment_duration").equals("")) {
                                        Toast.makeText(getApplicationContext(), "Doesn't have any Schedule yet !", Toast.LENGTH_SHORT).show();
                                    } else {
                                        intent = new Intent(view.getContext(), d_0_0_14_Take_appointment.class);
                                        intent.putExtra("profile_id_key", phone);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        view.getContext().startActivity(intent);
                                    }
                                }
                            }
                        }
                    });
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