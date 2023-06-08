package com.example.profind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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

public class e_1_10_Group_profile extends AppCompatActivity implements View.OnClickListener {
    LinearLayout post_layout;
    Button back_btn, banner_btn, group_setting_btn, join_btn, invite_btn, post_btn;
    TextView group_name_txtvw, members_time_txtvw, join_txtvw, invite_txtvw;
    CircleImageView post_banner_img;
    ShapeableImageView banner_img;
    RecyclerView recyclerView;
    String banner_url, name, phone, group_id, group_name, admin_phone, time, total_members;
    boolean is_member, is_invited;
    d_0_0_11_postsAdapter adapter;
    Intent intent;
    int new_total_member;
    ProgressBar progressBar;
    ProgressDialog progressDialog;

    FirebaseStorage storage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docIdRef;

    int adrslayout_width = LinearLayout.LayoutParams.MATCH_PARENT;
    int adrslayout_height = LinearLayout.LayoutParams.WRAP_CONTENT;
    LinearLayout.LayoutParams lp_empty = new LinearLayout.LayoutParams(0, 0);
    LinearLayout.LayoutParams lp_not_empty = new LinearLayout.LayoutParams(adrslayout_width, adrslayout_height);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_e_1_10_group_profile);
        getSupportActionBar().hide();

        progressBar = new ProgressBar(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgressDrawable(null);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        storage = FirebaseStorage.getInstance();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        name = userDetails.get(b_0_1_Session.KEY_NAME);
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        post_layout = findViewById(R.id.group_page_post_layout_id);

        back_btn = findViewById(R.id.group_page_back_btn_id);
        banner_btn = findViewById(R.id.group_page_banner_img_btn_id);
        group_setting_btn = findViewById(R.id.group_page_setting_btn_id);
        join_btn = findViewById(R.id.group_page_join_btn_id);
        invite_btn = findViewById(R.id.group_page_invite_btn_id);
        post_btn = findViewById(R.id.group_page_post_anything_btn_id);

        group_name_txtvw = findViewById(R.id.group_page_name_id);
        members_time_txtvw = findViewById(R.id.group_page_members_time_id);
        join_txtvw = findViewById(R.id.group_page_join_text_id);
        invite_txtvw = findViewById(R.id.group_page_invite_text_id);

        post_banner_img = findViewById(R.id.group_page_post_anything_image_id);
        banner_img = findViewById(R.id.group_page_banner_img_id);

        recyclerView = findViewById(R.id.group_page_recyclerview_id);

        group_id = getIntent().getStringExtra("group_id_key");

        docIdRef = db.collection("groups").document(group_id);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        e_1_4_0_Group_create_model model = document.toObject(e_1_4_0_Group_create_model.class);
                        banner_url = document.getString("banner");
                        group_name = document.getString("group_name");
                        admin_phone = document.getString("phone");
                        time = document.getString("time");
                        total_members = document.getString("total_members");
                        new_total_member = Integer.parseInt(total_members);

                        Picasso.get()
                                .load(banner_url)
                                .placeholder(R.drawable.profile_img)
                                .into(banner_img);
                        Picasso.get()
                                .load(banner_url)
                                .placeholder(R.drawable.profile_img)
                                .into(post_banner_img);
                        group_name_txtvw.setText(" " + group_name);
                        members_time_txtvw.setText("Total : " + total_members + " Members | Since : " + time);
                        for (String invited : model.getInvited()) {
                            if (phone.equals(invited)) {
                                is_invited = true;
                            }
                        }
                        for (String members : model.getMembers()) {
                            if (phone.equals(members)) {
                                is_member = true;
                            }
                        }

                        if (is_member) {
                            post_layout.setLayoutParams(lp_not_empty);
                            join_txtvw.setBackgroundResource(R.drawable.custom_button_12);
                            join_txtvw.setText("Joined");
                            join_txtvw.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_done, 0, 0, 0);
                            invite_txtvw.setBackgroundResource(R.drawable.custom_button_11);
                            invite_btn.setEnabled(true);

                        } else {
                            post_layout.setLayoutParams(lp_empty);
                            join_txtvw.setBackgroundResource(R.drawable.custom_button_11);
                            join_txtvw.setText("Join");
                            join_txtvw.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_add, 0, 0, 0);
                            invite_txtvw.setBackgroundResource(R.drawable.custom_button_12);
                            invite_btn.setEnabled(false);

                        }
                    }
                }
            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManagerWrapper(getApplicationContext(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width_half = (dm.widthPixels / 2) - 5;

        Query query = db.collection("posts")
                .whereEqualTo("group_id",group_id)
                .orderBy("posted_at", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<d_0_0_10_posts_model> datalist = new
                FirestoreRecyclerOptions.Builder<d_0_0_10_posts_model>()
                .setQuery(query, d_0_0_10_posts_model.class)
                .build();

        adapter = new d_0_0_11_postsAdapter(datalist, width_half);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setAdapter(adapter);

        back_btn.setOnClickListener(this);
        banner_btn.setOnClickListener(this);
        group_setting_btn.setOnClickListener(this);
        join_btn.setOnClickListener(this);
        invite_btn.setOnClickListener(this);
        post_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.group_page_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.group_page_banner_img_btn_id) {
            BottomSheetDialog sheetDialog = new BottomSheetDialog(this,
                    R.style.BottomSheetDialogTheme);
            View sheetView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.activity_e_0_0_group_profile_bottomsheet_1,
                            findViewById(R.id.group_profile_banner_bottomsheet));
            sheetView.findViewById(R.id.group_profile_img_bottomsheet_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (banner_url != null) {
                        Intent intent = new Intent(getApplicationContext(), d_0_0_3_Full_image_view.class);
                        intent.putExtra("fullimgkey", banner_url);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Profile Image exist !", Toast.LENGTH_SHORT).show();
                    }
                    sheetDialog.dismiss();
                }
            });
            sheetView.findViewById(R.id.group_profile_img_bottomsheet_upload).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (is_member) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, 10);
                        sheetDialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "You are not a member!", Toast.LENGTH_SHORT).show();
                        sheetDialog.dismiss();
                    }
                }
            });
            sheetDialog.setContentView(sheetView);
            sheetDialog.show();

        } else if (view.getId() == R.id.group_page_setting_btn_id) {
            BottomSheetDialog sheetDialog = new BottomSheetDialog(this,
                    R.style.BottomSheetDialogTheme);
            View sheetView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.activity_e_0_0_group_profile_bottomsheet_2,
                            findViewById(R.id.group_profile_setting_bottomsheet));
            sheetView.findViewById(R.id.group_profile_setting_bottomsheet_rename).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (admin_phone.equals(phone)) {
                        Intent intent = new Intent(view.getContext(), e_1_12_Group_rename.class);
                        intent.putExtra("group_id_key", group_id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                        sheetDialog.dismiss();
                    }else {
                        Toast.makeText(getApplicationContext(), "Only admin can 'Rename'", Toast.LENGTH_LONG).show();
                        sheetDialog.dismiss();
                    }
                }
            });
            sheetView.findViewById(R.id.group_profile_setting_bottomsheet_members).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), e_1_11_Group_members.class);
                    intent.putExtra("group_id_key", group_id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                    sheetDialog.dismiss();
                }
            });
            sheetView.findViewById(R.id.group_profile_setting_bottomsheet_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (admin_phone.equals(phone)) {
                        DeleteGroupDialogue();
                        sheetDialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Only admin can 'Delete'", Toast.LENGTH_LONG).show();
                        sheetDialog.dismiss();
                    }
                }
            });
            sheetDialog.setContentView(sheetView);
            sheetDialog.show();
        } else if (view.getId() == R.id.group_page_join_btn_id) {
            if (admin_phone.equals(phone)) {
                Toast.makeText(getApplicationContext(), "Admin can't leave the group", Toast.LENGTH_SHORT).show();
            } else if (!is_invited && !is_member && !admin_phone.equals(phone)) {
                Toast.makeText(getApplicationContext(), "Can't join without invitation", Toast.LENGTH_SHORT).show();
            } else if (is_invited && is_member && !admin_phone.equals(phone)) {
                is_member = false;
                new_total_member = new_total_member - 1;
                post_layout.setLayoutParams(lp_empty);
                join_txtvw.setBackgroundResource(R.drawable.custom_button_11);
                join_txtvw.setText("Join");
                join_txtvw.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_add, 0, 0, 0);
                invite_txtvw.setBackgroundResource(R.drawable.custom_button_12);
                members_time_txtvw.setText("Total : " + new_total_member + " Members | Since : " + time);
                invite_btn.setEnabled(false);
                db.collection("groups").document(group_id).update("members", FieldValue.arrayRemove(phone));
                db.collection("users").document(phone).update("groups", FieldValue.arrayRemove(group_id));
                db.collection("groups").document(group_id).update("total_members", new_total_member + "");
            } else if (is_invited && !is_member && !admin_phone.equals(phone)) {
                is_member = true;
                new_total_member = new_total_member + 1;
                post_layout.setLayoutParams(lp_not_empty);
                join_txtvw.setBackgroundResource(R.drawable.custom_button_12);
                join_txtvw.setText("Joined");
                join_txtvw.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_done, 0, 0, 0);
                invite_txtvw.setBackgroundResource(R.drawable.custom_button_11);
                members_time_txtvw.setText("Total : " + new_total_member + " Members | Since : " + time);
                invite_btn.setEnabled(true);
                db.collection("groups").document(group_id).update("members", FieldValue.arrayUnion(phone));
                db.collection("users").document(phone).update("groups", FieldValue.arrayUnion(group_id));
                db.collection("groups").document(group_id).update("total_members", new_total_member + "");
            }


        } else if (view.getId() == R.id.group_page_invite_btn_id) {
            intent = new Intent(view.getContext(), e_1_6_Group_add_members.class);
            intent.putExtra("group_id_key", group_id);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);

        } else if (view.getId() == R.id.group_page_post_anything_btn_id) {
            intent = new Intent(this, e_1_13_Group_post_anything.class);
            intent.putExtra("group_id_key", group_id);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 10 && data.getData() != null) {
                Uri uri = data.getData();
                banner_img.setImageURI(uri);

                final StorageReference storageRef = storage.getReference().child("groups").child(phone)
                        .child(group_name).child("grp_banner");

                storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                db.collection("groups").document(group_id).update("banner", uri.toString());
                            }
                        });
                    }
                });

            }
        } catch (Exception e) {

        }

    }

    private void DeleteGroupDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to 'Delete' the account ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        progressDialog.show();
                        db.collection("users")
                                .whereArrayContains("groups", group_id)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            if (document.exists()) {
                                                db.collection("users").document(document.getString("Phone_Number")).update("groups", FieldValue.arrayRemove(group_id));
                                            }
                                        }
                                    }
                                });
                        db.collection("posts")
                                .whereEqualTo("group_id", group_id)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            if (document.exists()) {
                                                db.collection("posts").document(document.getId()).delete();
                                            }
                                        }
                                    }
                                });
                        db.collection("groups").document(group_id).delete();
                        progressDialog.dismiss();
                        finish();
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