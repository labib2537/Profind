package com.example.profind;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class f_0_0_0_idsAdepter extends FirestoreRecyclerAdapter<f_0_0_0_ids_model, f_0_0_0_idsAdepter.ids_holder> {

    String _name, _profession, _rating, _profile_img, _place, user_phone;
    int  present_following, present_followed_by;
    View view;
    Map<String, String> follown = new HashMap<>();
    Map<String, String> followr = new HashMap<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public f_0_0_0_idsAdepter(@NonNull FirestoreRecyclerOptions<f_0_0_0_ids_model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ids_holder holder, @SuppressLint("RecyclerView") int position, @NonNull f_0_0_0_ids_model model) {

        try {
            LinearLayout.LayoutParams lp_empty = new LinearLayout.LayoutParams(0, 0);
            _name = model.getName();
            _profession = model.getProfession();
            _rating = model.getRating();
            _profile_img = model.getProfile_Photo();
            _place = model.getAddress();
            holder.name.setText(_name);
            if (!_profile_img.isEmpty()) {
                Picasso.get()
                        .load(_profile_img)
                        .placeholder(R.drawable.profile_img)
                        .into(holder.user_id_img);
            }
            if (!_profession.isEmpty()) {
                if (_profession.equals("Doctor")) {
                    holder.profession_img.setImageResource(R.drawable.doctor_icon);
                } else if (_profession.equals("Builder")) {
                    holder.profession_img.setImageResource(R.drawable.builder_icon);
                } else if (_profession.equals("Teacher")) {
                    holder.profession_img.setImageResource(R.drawable.teacher_icon);
                } else if (_profession.equals("Lawyer")) {
                    holder.profession_img.setImageResource(R.drawable.lawyer_icon);
                }
                holder.profession.setText(_profession);
                holder.ratingBar.setRating(Float.parseFloat(_rating));
                holder.rating.setText(_rating + "/5");
            } else {
                holder.follow_btn.setLayoutParams(lp_empty);
                holder.professional_layout.setLayoutParams(lp_empty);
            }
            if (_place.isEmpty()) {
                holder.place_layout.setLayoutParams(lp_empty);
            } else {
                holder.place.setText(_place);
            }


            DocumentReference addedDocRef = db.collection("followings").document(user_phone);
            addedDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String res = documentSnapshot.getString(model.getPhone_Number());
                        if (res == null) {
                            res = "false";
                        }
                        if (res.equals("true")) {
                            holder.follow_btn.setBackgroundResource(R.drawable.custom_button_12);
                            holder.follow_btn.setText("Following");
                            holder.follow_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_following_icon_2, 0);
                        } else if (res.equals("false")) {
                            holder.follow_btn.setBackgroundResource(R.drawable.custom_button_11);
                            holder.follow_btn.setText("Follow");
                            holder.follow_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_follow_icon_2, 0);
                        }


                    }
                }
            });
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (model.getPhone_Number().equals(user_phone)) {
                        Intent intent = new Intent(view.getContext(), d_0_0_Profile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    } else {

                        Intent intent = new Intent(view.getContext(), d_0_0_Profile_view.class);
                        intent.putExtra("profile_id_key", model.getPhone_Number());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }

                }
            });

            holder.follow_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    present_followed_by = Integer.parseInt(model.getFollowed_by());

                    DocumentReference addedDocRef = db.collection("followings").document(user_phone);
                    addedDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String res = documentSnapshot.getString(model.getPhone_Number());
                                if (res == null) {
                                    res = "false";
                                }
                                if (res.equals("true")) {
                                    res = "false";
                                    db.collection("users")
                                            .whereEqualTo("Phone_Number", user_phone)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                                                        present_following = Integer.parseInt(document.getString("Following"));

                                                        present_followed_by = present_followed_by - 1;
                                                        present_following = present_following - 1;

                                                        db.collection("followings").document(user_phone).update(model.getPhone_Number(), "false");
                                                        db.collection("users").document(user_phone).update("Following", Integer.toString(present_following));
                                                        db.collection("followers")
                                                                .document(model.getPhone_Number()).update(user_phone, "false")
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        db.collection("users").document(model.getPhone_Number()).update("Followed_by", Integer.toString(present_followed_by));
                                                                    }
                                                                });

                                                    }
                                                }

                                            });
                                } else if (res.equals("false")) {
                                    res = "true";
                                    db.collection("users")
                                            .whereEqualTo("Phone_Number", user_phone)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                                                        present_following = Integer.parseInt(document.getString("Following"));

                                                        present_followed_by = present_followed_by + 1;
                                                        present_following = present_following + 1;
                                                        follown.put(model.getPhone_Number(), "true");
                                                        followr.put(user_phone, "true");

                                                        db.collection("followings").document(user_phone).set(follown, SetOptions.merge());
                                                        db.collection("users").document(user_phone).update("Following", Integer.toString(present_following));
                                                        db.collection("followers")
                                                                .document(model.getPhone_Number())
                                                                .set(followr, SetOptions.merge())
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        db.collection("users").document(model.getPhone_Number()).update("Followed_by", Integer.toString(present_followed_by));
                                                                    }
                                                                });
                                                        followr.clear();
                                                        follown.clear();

                                                    }
                                                }

                                            });
                                }
                            }
                        }
                    });
                }
            });


        } catch (Exception e) {
            Toast.makeText(view.getContext(), e + "", Toast.LENGTH_SHORT).show();
        }

    }


    @NonNull
    @Override
    public ids_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_g_2_id_frame, parent, false);
        return new ids_holder(view);
    }

    class ids_holder extends RecyclerView.ViewHolder {

        LinearLayout layout, professional_layout, place_layout;
        ImageView user_id_img, profession_img;
        TextView name, profession, rating, place;
        Button follow_btn;
        RatingBar ratingBar;

        public ids_holder(View itemview) {
            super(itemview);
            view = itemview;

            b_0_1_Session session = new b_0_1_Session(itemview.getContext());
            HashMap<String, String> userDetails = session.getuserdetailFromSession();
            user_phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

            layout = itemView.findViewById(R.id.user_id_layout_id);
            professional_layout = itemView.findViewById(R.id.user_id_professional_layout_id);
            place_layout = itemView.findViewById(R.id.user_id_place_layout_id);
            user_id_img = itemView.findViewById(R.id.user_id_imgvw_id);
            profession_img = itemView.findViewById(R.id.user_id_profession_imgvw_id);
            name = itemView.findViewById(R.id.user_id_name_txtvw_id);
            profession = itemView.findViewById(R.id.user_id_professiontxtvw_id);
            rating = itemView.findViewById(R.id.user_id_ratingtxtvw_id);
            place = itemView.findViewById(R.id.user_id_placetxtvw_id);

            follow_btn = itemView.findViewById(R.id.user_id_follow_btn_id);
            ratingBar = itemView.findViewById(R.id.user_id_ratingbar_id);

        }
    }
}
