package com.example.profind;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class d_0_0_11_postsAdapter extends FirestoreRecyclerAdapter<d_0_0_10_posts_model, d_0_0_11_postsAdapter.post_holder> {

    String phone, user_phone, time, place, description, images, img_vdo_indicator;
    String _name, _profession, _rating, _profile_img;
    float time_1, time_2, duration;
    int t, width_half;
    int present_react;
    View view;
    Intent intent;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, String> react = new HashMap<>();
    ArrayList<c_3_Comment_model> comment_list;
    c_4_Comment_Adapter adapter;

    public d_0_0_11_postsAdapter(@NonNull FirestoreRecyclerOptions<d_0_0_10_posts_model> options, int width_half) {
        super(options);
        this.width_half = width_half;
    }

    @Override
    protected void onBindViewHolder(@NonNull post_holder holder, @SuppressLint("RecyclerView") int position, @NonNull d_0_0_10_posts_model model) {

        try {

            int layout_width = LinearLayout.LayoutParams.MATCH_PARENT;
            int layout_height = LinearLayout.LayoutParams.WRAP_CONTENT;
            LinearLayout.LayoutParams lout_empty = new LinearLayout.LayoutParams(0, 0);
            LinearLayout.LayoutParams lout_not_empty = new LinearLayout.LayoutParams(layout_width, layout_height);

            if (model.getPost_type().equals("personal")) {
                holder.group_post_lout.setLayoutParams(lout_empty);
                holder.personal_post_lout.setLayoutParams(lout_not_empty);
            } else if (model.getPost_type().equals("group")) {
                holder.group_post_lout.setLayoutParams(lout_not_empty);
                holder.personal_post_lout.setLayoutParams(lout_empty);
                db.collection("groups")
                        .document(model.getGroup_id())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Picasso.get()
                                            .load(documentSnapshot.getString("banner"))
                                            .placeholder(R.drawable.background_img)
                                            .into(holder.grp_banner_img);
                                    holder.grp_name.setText(Html.fromHtml("<b>" + documentSnapshot.getString("group_name") + "</b>"));
                                }
                            }
                        });
            }
            phone = model.getPhone();
            images = model.getImages();
            img_vdo_indicator = model.getImg_vdo_indicator();
            set_images(holder, images, img_vdo_indicator, width_half);
            db.collection("users")
                    .whereEqualTo("Phone_Number", phone)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                _name = document.getString("Name");
                                _profession = document.getString("Profession");
                                _rating = document.getString("Rating");
                                _profile_img = document.getString("Profile_Photo");
                                Picasso.get()
                                        .load(_profile_img)
                                        .placeholder(R.drawable.profile_img)
                                        .into(holder.profile_img);
                                Picasso.get()
                                        .load(_profile_img)
                                        .placeholder(R.drawable.profile_img)
                                        .into(holder.grp_post_profile_img);
                                holder.name.setText(_name);
                                holder.user_name.setText(Html.fromHtml("<b>" + _name + "</b>  -(posted by)"));
                                holder.profession.setText(_profession);
                                holder.rating.setText(_rating + "/5");
                                holder.profile_ratingbar.setRating(Float.parseFloat(_rating));

                                if (_profession.equals("Builder")) {
                                    holder.profession_img.setImageResource(R.drawable.builder_icon);
                                } else if (_profession.equals("Doctor")) {
                                    holder.profession_img.setImageResource(R.drawable.doctor_icon);
                                } else if (_profession.equals("Lawyer")) {
                                    holder.profession_img.setImageResource(R.drawable.lawyer_icon);
                                } else if (_profession.equals("Teacher")) {
                                    holder.profession_img.setImageResource(R.drawable.teacher_icon);
                                }
                            }
                        }

                    });


            time_1 = Float.parseFloat(model.getPosted_at());
            place = model.getPlace();
            description = model.getDescription();

            if (place.length() == 0) {
                holder.post_place_img.setLayoutParams(lout_empty);
                holder.grp_post_place_img.setLayoutParams(lout_empty);
            }
            if (description.length() == 0) {
                holder.description.setLayoutParams(lout_empty);
            }
            time_2 = new Date().getTime();
            duration = (time_2 - time_1) / 1000;
            calculate_time(duration);


            reacts_counter(holder, model.getReacts());
            comment_counter(holder, model.getComments());
            holder.post_time.setText(time);
            holder.grp_post_time.setText(time);
            holder.post_place.setText(place);
            holder.grp_post_place.setText(place);
            holder.description.setText(description);

            holder.grp_banner_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(view.getContext(), e_1_10_Group_profile.class);
                    intent.putExtra("group_id_key", model.getGroup_id());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                }
            });
            holder.grp_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(view.getContext(), e_1_10_Group_profile.class);
                    intent.putExtra("group_id_key", model.getGroup_id());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                }
            });
            holder.profile_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (model.getPhone().equals(user_phone)) {
                        intent = new Intent(view.getContext(), d_0_0_Profile.class);
                    } else {

                        intent = new Intent(view.getContext(), d_0_0_Profile_view.class);
                        intent.putExtra("profile_id_key", model.getPhone());
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                }
            });
            holder.grp_post_profile_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (model.getPhone().equals(user_phone)) {
                        intent = new Intent(view.getContext(), d_0_0_Profile.class);
                    } else {

                        intent = new Intent(view.getContext(), d_0_0_Profile_view.class);
                        intent.putExtra("profile_id_key", model.getPhone());
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                }
            });
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (model.getPhone().equals(user_phone)) {
                        intent = new Intent(view.getContext(), d_0_0_Profile.class);
                    } else {

                        intent = new Intent(view.getContext(), d_0_0_Profile_view.class);
                        intent.putExtra("profile_id_key", model.getPhone());
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                }
            });
            holder.user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (model.getPhone().equals(user_phone)) {
                        intent = new Intent(view.getContext(), d_0_0_Profile.class);
                    } else {

                        intent = new Intent(view.getContext(), d_0_0_Profile_view.class);
                        intent.putExtra("profile_id_key", model.getPhone());
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                }
            });
            holder.img_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(view.getContext(), d_0_0_12_img_vdo_show.class);
                    intent.putExtra("profile_img_key", _profile_img);
                    intent.putExtra("profile_name_key", _name);
                    intent.putExtra("description_key", model.getDescription());
                    intent.putExtra("img_vdo_key", model.getImages());
                    intent.putExtra("img_vdo_indicator_key", model.getImg_vdo_indicator());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                }
            });
            holder.img_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(view.getContext(), d_0_0_12_img_vdo_show.class);
                    intent.putExtra("profile_img_key", _profile_img);
                    intent.putExtra("profile_name_key", _name);
                    intent.putExtra("description_key", model.getDescription());
                    intent.putExtra("img_vdo_key", model.getImages());
                    intent.putExtra("img_vdo_indicator_key", model.getImg_vdo_indicator());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                }
            });
            holder.img_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(view.getContext(), d_0_0_12_img_vdo_show.class);
                    intent.putExtra("profile_img_key", _profile_img);
                    intent.putExtra("profile_name_key", _name);
                    intent.putExtra("description_key", model.getDescription());
                    intent.putExtra("img_vdo_key", model.getImages());
                    intent.putExtra("img_vdo_indicator_key", model.getImg_vdo_indicator());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                }
            });
            holder.img_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(view.getContext(), d_0_0_12_img_vdo_show.class);
                    intent.putExtra("profile_img_key", _profile_img);
                    intent.putExtra("profile_name_key", _name);
                    intent.putExtra("description_key", model.getDescription());
                    intent.putExtra("img_vdo_key", model.getImages());
                    intent.putExtra("img_vdo_indicator_key", model.getImg_vdo_indicator());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                }
            });
            holder.vdo_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(view.getContext(), d_0_0_12_img_vdo_show.class);
                    intent.putExtra("profile_img_key", _profile_img);
                    intent.putExtra("profile_name_key", _name);
                    intent.putExtra("description_key", model.getDescription());
                    intent.putExtra("img_vdo_key", model.getImages());
                    intent.putExtra("img_vdo_indicator_key", model.getImg_vdo_indicator());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                }
            });
            holder.vdo_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(view.getContext(), d_0_0_12_img_vdo_show.class);
                    intent.putExtra("profile_img_key", _profile_img);
                    intent.putExtra("profile_name_key", _name);
                    intent.putExtra("description_key", model.getDescription());
                    intent.putExtra("img_vdo_key", model.getImages());
                    intent.putExtra("img_vdo_indicator_key", model.getImg_vdo_indicator());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                }
            });
            holder.vdo_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(view.getContext(), d_0_0_12_img_vdo_show.class);
                    intent.putExtra("profile_img_key", _profile_img);
                    intent.putExtra("profile_name_key", _name);
                    intent.putExtra("description_key", model.getDescription());
                    intent.putExtra("img_vdo_key", model.getImages());
                    intent.putExtra("img_vdo_indicator_key", model.getImg_vdo_indicator());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                }
            });
            holder.vdo_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(view.getContext(), d_0_0_12_img_vdo_show.class);
                    intent.putExtra("profile_img_key", _profile_img);
                    intent.putExtra("profile_name_key", _name);
                    intent.putExtra("description_key", model.getDescription());
                    intent.putExtra("img_vdo_key", model.getImages());
                    intent.putExtra("img_vdo_indicator_key", model.getImg_vdo_indicator());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                }
            });
            holder.extra_img_vdo_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(view.getContext(), d_0_0_12_img_vdo_show.class);
                    intent.putExtra("profile_img_key", _profile_img);
                    intent.putExtra("profile_name_key", _name);
                    intent.putExtra("description_key", model.getDescription());
                    intent.putExtra("img_vdo_key", model.getImages());
                    intent.putExtra("img_vdo_indicator_key", model.getImg_vdo_indicator());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                }
            });

            db.collection("reacts").document(getSnapshots().getSnapshot(position).getId())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String ract = documentSnapshot.getString(user_phone);
                        if (ract == null) {
                            ract = "false";
                        }
                        if (ract.equals("true")) {
                            holder.react_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_loved_icon, 0, 0, 0);
                        } else if (ract.equals("false")) {
                            holder.react_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_love_not_given_icon, 0, 0, 0);
                        }
                    }
                }
            });
            holder.grp_post_setting_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BottomSheetDialog sheetDialog = new BottomSheetDialog(view.getContext(), R.style.BottomSheetDialogTheme);
                    View sheetView = LayoutInflater.from(view.getContext())
                            .inflate(R.layout.activity_c_1_homepage_posts_bottomsheet_1, holder.itemView.findViewById(R.id.post_setting_bottomsheet));

                    sheetView.findViewById(R.id.post_bottomsheet_report).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (model.getPhone().equals(user_phone)) {
                                Toast.makeText(view.getContext(), "You cant 'Report' on your oun post", Toast.LENGTH_SHORT).show();
                                sheetDialog.dismiss();
                            } else {
                                db.collection("posts")
                                        .document(getSnapshots().getSnapshot(position).getId())
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            int present_rprts = Integer.parseInt(documentSnapshot.getString("reports"));
                                            present_rprts = present_rprts + 1;
                                            String new_present_reports = Integer.toString(present_rprts);
                                            db.collection("posts").document(getSnapshots().getSnapshot(position).getId()).update("reports", new_present_reports);
                                            Toast.makeText(view.getContext(), "Successfully Reported", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                sheetDialog.dismiss();
                            }
                        }
                    });
                    sheetView.findViewById(R.id.post_bottomsheet_delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (model.getPhone().equals(user_phone)) {
                                db.collection("posts").document(getSnapshots().getSnapshot(position).getId()).delete();
                                db.collection("reacts").document(getSnapshots().getSnapshot(position).getId()).delete();
                                Toast.makeText(view.getContext(), "Successfully  Deleted", Toast.LENGTH_SHORT).show();
                                sheetDialog.dismiss();
                            } else {
                                Toast.makeText(view.getContext(), "You are not permitted to 'Delete' this post", Toast.LENGTH_SHORT).show();
                                sheetDialog.dismiss();
                            }
                        }
                    });
                    sheetDialog.setContentView(sheetView);
                    sheetDialog.show();
                }
            });
            holder.post_setting_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BottomSheetDialog sheetDialog = new BottomSheetDialog(view.getContext(), R.style.BottomSheetDialogTheme);
                    View sheetView = LayoutInflater.from(view.getContext())
                            .inflate(R.layout.activity_c_1_homepage_posts_bottomsheet_1, holder.itemView.findViewById(R.id.post_setting_bottomsheet));

                    sheetView.findViewById(R.id.post_bottomsheet_report).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (model.getPhone().equals(user_phone)) {
                                Toast.makeText(view.getContext(), "You cant 'Report' on your oun post", Toast.LENGTH_SHORT).show();
                                sheetDialog.dismiss();
                            } else {
                                db.collection("posts")
                                        .document(getSnapshots().getSnapshot(position).getId())
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            int present_rprts = Integer.parseInt(documentSnapshot.getString("reports"));
                                            present_rprts = present_rprts + 1;
                                            String new_present_reports = Integer.toString(present_rprts);
                                            db.collection("posts").document(getSnapshots().getSnapshot(position).getId()).update("reports", new_present_reports);
                                            Toast.makeText(view.getContext(), "Successfully Reported", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                sheetDialog.dismiss();
                            }
                        }
                    });
                    sheetView.findViewById(R.id.post_bottomsheet_delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (model.getPhone().equals(user_phone)) {
                                db.collection("posts").document(getSnapshots().getSnapshot(position).getId()).delete();
                                db.collection("reacts").document(getSnapshots().getSnapshot(position).getId()).delete();
                                Toast.makeText(view.getContext(), "Successfully  Deleted", Toast.LENGTH_SHORT).show();
                                sheetDialog.dismiss();
                            } else {
                                Toast.makeText(view.getContext(), "You are not permitted to 'Delete' this post", Toast.LENGTH_SHORT).show();
                                sheetDialog.dismiss();
                            }
                        }
                    });
                    sheetDialog.setContentView(sheetView);
                    sheetDialog.show();
                }
            });

            holder.react_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.collection("reacts").document(getSnapshots().getSnapshot(position).getId())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String ract = documentSnapshot.getString(user_phone);
                                if (ract == null) {
                                    ract = "false";
                                }
                                if (ract.equals("true")) {
                                    holder.react_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_love_not_given_icon, 0, 0, 0);
                                    db.collection("posts").document(getSnapshots().getSnapshot(position).getId())
                                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                present_react = Integer.parseInt(documentSnapshot.getString("reacts"));
                                                present_react = present_react - 1;
                                                String new_present_reacts = Integer.toString(present_react);
                                                db.collection("reacts").document(getSnapshots().getSnapshot(position).getId()).update(user_phone, "false")
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                db.collection("posts").document(getSnapshots().getSnapshot(position).getId()).update("reacts", new_present_reacts)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                reacts_counter(holder, new_present_reacts);
                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        }
                                    });


                                } else if (ract.equals("false")) {
                                    holder.react_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_loved_icon, 0, 0, 0);
                                    db.collection("posts").document(getSnapshots().getSnapshot(position).getId())
                                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                present_react = Integer.parseInt(documentSnapshot.getString("reacts"));
                                                present_react = present_react + 1;
                                                String new_present_reacts = Integer.toString(present_react);
                                                react.put(user_phone, "true");
                                                db.collection("reacts")
                                                        .document(getSnapshots().getSnapshot(position).getId())
                                                        .set(react, SetOptions.merge())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                db.collection("posts").document(getSnapshots().getSnapshot(position).getId()).update("reacts", new_present_reacts)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                reacts_counter(holder, new_present_reacts);
                                                                                notification(model.getPhone(), "react", getSnapshots().getSnapshot(position).getId());
                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        }
                                    });
                                    react.clear();
                                }
                        }
                    });


                }
            });
            holder.comment_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    BottomSheetDialog sheetDialog = new BottomSheetDialog(view.getContext(), R.style.BottomSheetDialogTheme);
                    View sheetView = LayoutInflater.from(view.getContext())
                            .inflate(R.layout.activity_c_2_homepage_posts_bottomsheet_2, holder.itemView.findViewById(R.id.comment_bottomsheet));

                    TextInputEditText comment_text = sheetView.findViewById(R.id.comment_edtxt_id);
                    RecyclerView comment_recyclerview = sheetView.findViewById(R.id.comment_recyclerview_id);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                    layoutManager.setStackFromEnd(true);
                    comment_recyclerview.setLayoutManager(layoutManager);
                    comment_recyclerview.setHasFixedSize(true);

                    comment_list = new ArrayList<>();
                    adapter = new c_4_Comment_Adapter(comment_list);

                    db.collection("comments")
                            .whereEqualTo("post_id", getSnapshots().getSnapshot(position).getId())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    List<DocumentSnapshot> list = value.getDocuments();
                                    comment_list.clear();
                                    for (DocumentSnapshot documentSnapshot : list) {
                                        c_3_Comment_model obj = documentSnapshot.toObject(c_3_Comment_model.class);
                                        obj.setPost_id(documentSnapshot.getId());
                                        comment_list.add(obj);
                                    }
                                    comment_recyclerview.setAdapter(adapter);
                                }
                            });

                    sheetView.findViewById(R.id.send_comment_btn_id).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (comment_text.getText().toString().equals("")) {
                                Toast.makeText(view.getContext(), "Write something in the Comment section", Toast.LENGTH_SHORT).show();
                            } else {
                                String time = new Date().getTime() + "";
                                Map<String, String> comment_item = new HashMap<>();
                                comment_item.put("user_id", user_phone);
                                comment_item.put("time", time);
                                comment_item.put("comment_text", comment_text.getText().toString());
                                comment_item.put("post_id", getSnapshots().getSnapshot(position).getId());
                                comment_text.setText("");

                                db.collection("comments")
                                        .add(comment_item)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {

                                                db.collection("posts").document(getSnapshots().getSnapshot(position).getId())
                                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        if (documentSnapshot.exists()) {
                                                            int present_comments = Integer.parseInt(documentSnapshot.getString("comments"));
                                                            present_comments = present_comments + 1;
                                                            String new_present_comments = Integer.toString(present_comments);
                                                            db.collection("posts").document(getSnapshots().getSnapshot(position).getId())
                                                                    .update("comments", new_present_comments)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            comment_counter(holder, new_present_comments);
                                                                            notification(model.getPhone(), "comment", getSnapshots().getSnapshot(position).getId());
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });
                                            }
                                        });
                            }
                        }
                    });

                    sheetDialog.setContentView(sheetView);
                    sheetDialog.show();

                }
            });

        } catch (Exception e) {
            Toast.makeText(view.getContext(), e + "", Toast.LENGTH_SHORT).show();
        }

    }

    //React Counter function
    private void reacts_counter(@NonNull post_holder holder, String _reacts) {
        int reacts = Integer.parseInt(_reacts);
        if (reacts > 999 && reacts < 1000000) {
            reacts = reacts / 1000;
            holder.total_react.setText(reacts + "K Reacts");
        } else if (reacts > 1000000) {
            reacts = reacts / 1000000;
            holder.total_react.setText(reacts + "K Reacts");
        } else {
            holder.total_react.setText(reacts + " Reacts");
        }
    }

    //Notify function
    void notification(String post_phone, String notify_for, String post_id) {
        Map<String, String> notification_item = new HashMap<>();
        notification_item.put("commenter_liker_appointer_phone", user_phone);
        notification_item.put("poster_phone", post_phone);
        notification_item.put("time", new Date().getTime() + "");
        notification_item.put("type", notify_for);
        notification_item.put("clicked", "no");

        db.collection("notifications").document(post_id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    int present_notifications = Integer.parseInt(documentSnapshot.getString("total_notifications"));
                    present_notifications = present_notifications + 1;
                    String new_present_notifications = Integer.toString(present_notifications);
                    notification_item.put("total_notifications", new_present_notifications);
                    db.collection("notifications").document(post_id).set(notification_item);
                }else {
                    notification_item.put("total_notifications", "1");
                    db.collection("notifications").document(post_id).set(notification_item);
                }
            }
        });
    }

    //Comments Counter function
    private void comment_counter(@NonNull post_holder holder, String _comments) {
        int comments = Integer.parseInt(_comments);
        if (comments > 999 && comments < 1000000) {
            comments = comments / 1000;
            holder.total_comment.setText(comments + "K Comments");
        } else if (comments > 1000000) {
            comments = comments / 1000000;
            holder.total_comment.setText(comments + "K Comments");
        } else {
            holder.total_comment.setText(comments + " Comments");
        }
    }

    //Setting posted images function
    private void set_images(@NonNull post_holder holder, String images, String img_vdo_indicator, int _width_half) {


        int layout_width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = _width_half;

        LinearLayout.LayoutParams lout_empty = new LinearLayout.LayoutParams(0, 0);
        LinearLayout.LayoutParams lout_not_empty = new LinearLayout.LayoutParams(layout_width, height);
        LinearLayout.LayoutParams lout_not_empty_2 = new LinearLayout.LayoutParams(_width_half, height);
        LinearLayout.LayoutParams lout_not_empty_3 = new LinearLayout.LayoutParams(_width_half, height);
        lout_not_empty_3.setMargins(-_width_half, 0, 0, 0);

        String[] single_images = images.split(" ");
        String[] imgvdo_indicator = img_vdo_indicator.split(" ");
        int count = single_images.length;

        for (int i = 0; i < count; i++) {

            if (i == 0) {
                if (imgvdo_indicator[i].contains("0")) {
                    Picasso.get()
                            .load(single_images[i])
                            .placeholder(R.drawable.profile_img)
                            .into(holder.img_1);
                    holder.img_1.setLayoutParams(lout_not_empty);
                    holder.img_vdo_1.setLayoutParams(lout_not_empty);
                    holder.img_vdo_1_1.setLayoutParams(lout_not_empty);
                    holder.img_vdo_1_1.setPadding(0, 10, 0, 10);
                    holder.vdo_1.setLayoutParams(lout_empty);
                    holder.img_vdo_2.setLayoutParams(lout_empty);
                } else if (imgvdo_indicator[i].contains("1")) {
                    Uri vdo_uri;
                    vdo_uri = Uri.parse(single_images[i]);
                    holder.vdo_1.setVideoURI(vdo_uri);
                    holder.vdo_1.setLayoutParams(lout_not_empty);
                    holder.img_vdo_1.setLayoutParams(lout_not_empty);
                    holder.img_vdo_1_1.setLayoutParams(lout_not_empty);
                    holder.img_vdo_1_1.setPadding(0, 10, 0, 10);
                    holder.img_1.setLayoutParams(lout_empty);
                    holder.img_vdo_2.setLayoutParams(lout_empty);
                    video_controler(holder.vdo_1);
                }


            } else if (i == 1) {
                if (imgvdo_indicator[i].contains("0")) {
                    Picasso.get()
                            .load(single_images[i])
                            .placeholder(R.drawable.profile_img)
                            .into(holder.img_2);
                    holder.img_2.setLayoutParams(lout_not_empty_2);
                    holder.img_1.setLayoutParams(lout_not_empty_2);
                    holder.img_vdo_1_1.setLayoutParams(lout_not_empty_2);
                    holder.img_vdo_1.setLayoutParams(lout_not_empty);
                    holder.img_vdo_1_2.setLayoutParams(lout_not_empty_2);
                    holder.img_vdo_1_1.setPadding(0, 10, 5, 10);
                    holder.img_vdo_1_2.setPadding(5, 10, 0, 10);
                    holder.vdo_2.setLayoutParams(lout_empty);
                    holder.img_vdo_2.setLayoutParams(lout_empty);

                } else if (imgvdo_indicator[i].contains("1")) {
                    Uri vdo_uri;
                    vdo_uri = Uri.parse(single_images[i]);
                    holder.vdo_2.setVideoURI(vdo_uri);
                    holder.vdo_2.setLayoutParams(lout_not_empty_2);
                    holder.img_1.setLayoutParams(lout_not_empty_2);
                    holder.img_vdo_1_1.setLayoutParams(lout_not_empty_2);
                    holder.img_vdo_1.setLayoutParams(lout_not_empty);
                    holder.img_vdo_1_2.setLayoutParams(lout_not_empty_2);
                    holder.img_vdo_1_1.setPadding(0, 10, 5, 10);
                    holder.img_vdo_1_2.setPadding(5, 10, 0, 10);
                    holder.img_2.setLayoutParams(lout_empty);
                    holder.img_vdo_2.setLayoutParams(lout_empty);
                    video_controler(holder.vdo_2);
                }
            } else if (i == 2) {
                if (imgvdo_indicator[i].contains("0")) {
                    Picasso.get()
                            .load(single_images[i])
                            .placeholder(R.drawable.profile_img)
                            .into(holder.img_3);
                    holder.img_3.setLayoutParams(lout_not_empty);
                    holder.img_vdo_2_1.setLayoutParams(lout_not_empty);
                    holder.img_vdo_2_1.setPadding(0, 0, 0, 10);
                    holder.img_vdo_2_2.setLayoutParams(lout_empty);
                    holder.vdo_3.setLayoutParams(lout_empty);
                    holder.img_vdo_2.setLayoutParams(lout_not_empty);

                } else if (imgvdo_indicator[i].contains("1")) {
                    Uri vdo_uri;
                    vdo_uri = Uri.parse(single_images[i]);
                    holder.vdo_3.setVideoURI(vdo_uri);
                    holder.vdo_3.setLayoutParams(lout_not_empty);
                    holder.img_vdo_2_1.setLayoutParams(lout_not_empty);
                    holder.img_vdo_2_1.setPadding(0, 0, 0, 10);
                    holder.img_vdo_2_2.setLayoutParams(lout_empty);
                    holder.img_3.setLayoutParams(lout_empty);
                    holder.img_vdo_2.setLayoutParams(lout_not_empty);
                    video_controler(holder.vdo_3);
                }
            } else if (i == 3) {
                if (imgvdo_indicator[i].contains("0")) {
                    Picasso.get()
                            .load(single_images[i])
                            .placeholder(R.drawable.profile_img)
                            .into(holder.img_4);
                    holder.img_4.setLayoutParams(lout_not_empty_2);
                    holder.img_vdo_2_1.setLayoutParams(lout_not_empty_2);
                    holder.img_vdo_2_2.setLayoutParams(lout_not_empty_2);
                    holder.img_vdo_2_1.setPadding(0, 0, 5, 10);
                    holder.img_vdo_2_2.setPadding(5, 0, 0, 10);
                    holder.img_vdo_2.setLayoutParams(lout_not_empty);
                    holder.vdo_4.setLayoutParams(lout_empty);
                    holder.extra_img_vdo_txt.setLayoutParams(lout_empty);


                } else if (imgvdo_indicator[i].contains("1")) {
                    Uri vdo_uri;
                    vdo_uri = Uri.parse(single_images[i]);
                    holder.vdo_4.setVideoURI(vdo_uri);
                    holder.vdo_4.setLayoutParams(lout_not_empty_2);
                    holder.img_vdo_2_1.setLayoutParams(lout_not_empty_2);
                    holder.img_vdo_2_2.setLayoutParams(lout_not_empty_2);
                    holder.img_vdo_2_1.setPadding(0, 0, 5, 10);
                    holder.img_vdo_2_2.setPadding(5, 0, 0, 10);
                    holder.img_4.setLayoutParams(lout_empty);
                    holder.extra_img_vdo_txt.setLayoutParams(lout_empty);
                    holder.img_vdo_2.setLayoutParams(lout_not_empty);
                    if (count < 5) {
                        video_controler(holder.vdo_4);
                    }
                }
            } else if (i > 3) {
                holder.extra_img_vdo_txt.setText("+" + (count - 3));
                holder.extra_img_vdo_txt.setLayoutParams(lout_not_empty_3);
            }
        }
    }

    // video controler
    public void video_controler(VideoView v) {
        MediaController mediaController = new MediaController(v.getContext());
        mediaController.setAnchorView(v);
        v.setMediaController(mediaController);
    }

    //Time calculation function
    void calculate_time(float duration) {
        if (duration > 59 && duration < 3600) {
            duration = duration / 60;
            t = Math.round(duration);
            time = t + " min";
        } else if (duration > 3600 && duration < 86400) {
            duration = duration / 60 / 60;
            t = Math.round(duration);
            time = t + " hr";
        } else if (duration > 86400 && duration < 604800) {
            duration = duration / 60 / 60 / 24;
            t = Math.round(duration);
            time = t + " day";
        } else if (duration > 604800 && duration < 2592000) {
            duration = duration / 60 / 60 / 24 / 7;
            t = Math.round(duration);
            time = t + " week";
        } else if (duration > 2592000 && duration < 31536000) {
            duration = duration / 60 / 60 / 24 / 30;
            t = Math.round(duration);
            time = t + " month";
        } else if (duration > 31536000) {
            duration = duration / 60 / 60 / 24 / 365;
            t = Math.round(duration);
            time = t + " yr";
        } else {
            time = "1 min";
        }
    }

    @NonNull
    @Override
    public post_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_g_0_post_frame, parent, false);
        return new post_holder(view);
    }

    class post_holder extends RecyclerView.ViewHolder {
        TextView grp_name, user_name, grp_post_time, grp_post_place, name, description, profession, rating, post_time,
                post_place, total_react, total_comment, extra_img_vdo_txt;
        CircleImageView profile_img, profession_img, post_place_img,
                grp_banner_img, grp_post_profile_img, grp_post_place_img;
        RatingBar profile_ratingbar;
        Button grp_post_setting_btn, post_setting_btn, react_btn, comment_btn;
        ImageView img_1, img_2, img_3, img_4;
        VideoView vdo_1, vdo_2, vdo_3, vdo_4;
        LinearLayout personal_post_lout, group_post_lout, description_layout,
                img_vdo_1, img_vdo_1_1, img_vdo_1_2, img_vdo_2, img_vdo_2_1, img_vdo_2_2;

        public post_holder(View itemview) {
            super(itemview);
            view = itemview;
            b_0_1_Session session = new b_0_1_Session(itemview.getContext());
            HashMap<String, String> userDetails = session.getuserdetailFromSession();
            user_phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

            grp_name = itemView.findViewById(R.id.group_page_group_name_id);
            user_name = itemView.findViewById(R.id.group_page_user_name_id);
            grp_post_time = itemView.findViewById(R.id.group_post_time_id);
            grp_post_place = itemView.findViewById(R.id.group_post_placetxtvw_id);
            name = itemView.findViewById(R.id.post_profile_nametxtvw_id);
            description = itemView.findViewById(R.id.post_descriptiontxtvw_id);
            profession = itemView.findViewById(R.id.post_profile_professiontxtvw_id);
            rating = itemView.findViewById(R.id.post_profile_ratingtxtvw_id);
            post_time = itemView.findViewById(R.id.post_time_id);
            post_place = itemView.findViewById(R.id.post_placetxtvw_id);
            total_react = itemView.findViewById(R.id.post_reacttxtvw_id);
            total_comment = itemView.findViewById(R.id.post_commenttxtvw_id);
            extra_img_vdo_txt = itemView.findViewById(R.id.post_extra_img_vdo_txtvw_id);

            grp_banner_img = itemView.findViewById(R.id.group_page_banner_image_id);
            grp_post_profile_img = itemView.findViewById(R.id.group_page_user_image_id);
            grp_post_place_img = itemView.findViewById(R.id.group_post_placeimg_id);
            profile_img = itemView.findViewById(R.id.post_profile_imgvw_id);
            profession_img = itemView.findViewById(R.id.post_profile_profession_imgvw_id);
            post_place_img = itemView.findViewById(R.id.post_placeimg_id);
            profile_ratingbar = itemView.findViewById(R.id.post_profile_ratingbar_id);

            post_setting_btn = itemView.findViewById(R.id.post_setting_btn_id);
            grp_post_setting_btn = itemView.findViewById(R.id.group_setting_btn_id);
            react_btn = itemView.findViewById(R.id.post_react_btn_id);
            comment_btn = itemView.findViewById(R.id.post_comment_btn_id);

            personal_post_lout = itemView.findViewById(R.id.post_personal_layout_id);
            group_post_lout = itemView.findViewById(R.id.post_group_layout_id);
            description_layout = itemView.findViewById(R.id.post_page_description_lout_id);

            img_vdo_1 = itemView.findViewById(R.id.post_img_vdo_lout_1_id);
            img_vdo_1_1 = itemView.findViewById(R.id.post_img_vdo_lout_1_1_id);
            img_vdo_1_2 = itemView.findViewById(R.id.post_img_vdo_lout_1_2_id);
            img_vdo_2 = itemView.findViewById(R.id.post_img_vdo_lout_2_id);
            img_vdo_2_1 = itemView.findViewById(R.id.post_img_vdo_lout_2_1_id);
            img_vdo_2_2 = itemView.findViewById(R.id.post_img_vdo_lout_2_2_id);

            img_1 = itemView.findViewById(R.id.post_img_1_id);
            img_2 = itemView.findViewById(R.id.post_img_2_id);
            img_3 = itemView.findViewById(R.id.post_img_3_id);
            img_4 = itemView.findViewById(R.id.post_img_4_id);
            vdo_1 = itemView.findViewById(R.id.post_vdo_1_id);
            vdo_2 = itemView.findViewById(R.id.post_vdo_2_id);
            vdo_3 = itemView.findViewById(R.id.post_vdo_3_id);
            vdo_4 = itemView.findViewById(R.id.post_vdo_4_id);


        }
    }
}
