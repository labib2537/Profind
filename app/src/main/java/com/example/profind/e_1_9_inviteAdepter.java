package com.example.profind;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class e_1_9_inviteAdepter extends FirestoreRecyclerAdapter<f_0_0_0_ids_model, e_1_9_inviteAdepter.ids_holder> {
    String _name, _profession, _rating, _profile_img, _place, user_phone, group_id;
    int reacted,not_reacted;
    View view;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docIdRef;


    public e_1_9_inviteAdepter(@NonNull FirestoreRecyclerOptions<f_0_0_0_ids_model> options, String group_id) {
        super(options);
        this.group_id = group_id;
    }

    @Override
    protected void onBindViewHolder(@NonNull ids_holder holder, @SuppressLint("RecyclerView") int position, @NonNull f_0_0_0_ids_model model) {

        try {
            _name = model.getName();
            _profession = model.getProfession();
            _rating = model.getRating();
            _profile_img = model.getProfile_Photo();
            _place = model.getAddress();
            holder.name.setText(_name);
            Picasso.get()
                    .load(_profile_img)
                    .placeholder(R.drawable.profile_img)
                    .into(holder.user_id_img);
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
            if (_place == null) {
                holder.place_img.setImageDrawable(null);
            } else {
                holder.place.setText(_place);
            }

            docIdRef = db.collection("groups").document(group_id);

            docIdRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        e_1_4_0_Group_create_model model_invite = documentSnapshot.toObject(e_1_4_0_Group_create_model.class);

                        for (String invited : model_invite.getInvited()) {
                            if (model.getPhone_Number().equals(invited)) {
                               reacted=1;
                            }else {
                                not_reacted=1;
                            }
                        }
                        if (reacted==1) {
                            holder.invite_btn.setBackgroundResource(R.drawable.custom_button_12);
                            holder.invite_btn.setText("Invited");
                        }else if(not_reacted==1) {
                            holder.invite_btn.setBackgroundResource(R.drawable.custom_button_11);
                            holder.invite_btn.setText("Invite");
                        }
                        reacted=not_reacted=0;
                    }
                }
            });


            holder.invite_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    docIdRef = db.collection("groups").document(group_id);

                    docIdRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                e_1_4_0_Group_create_model model_invite = documentSnapshot.toObject(e_1_4_0_Group_create_model.class);

                                for (String invited : model_invite.getInvited()) {
                                    if (model.getPhone_Number().equals(invited)) {
                                        holder.invite_btn.setBackgroundResource(R.drawable.custom_button_11);
                                        holder.invite_btn.setText("Invite");
                                        db.collection("groups").document(group_id)
                                                .update("invited", FieldValue.arrayRemove(model.getPhone_Number()));
                                    }else {
                                        holder.invite_btn.setBackgroundResource(R.drawable.custom_button_12);
                                        holder.invite_btn.setText("Invited");
                                        db.collection("groups").document(group_id)
                                                .update("invited", FieldValue.arrayUnion(model.getPhone_Number()));
                                    }
                                }

                            }
                        }
                    });

                }
            });
            holder.invite_id_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), model.getPhone_Number()+" profile", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            Toast.makeText(view.getContext(), e + "", Toast.LENGTH_SHORT).show();
        }

    }


    @NonNull
    @Override
    public ids_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_g_4_invite_members, parent, false);
        return new ids_holder(view);
    }


    class ids_holder extends RecyclerView.ViewHolder {
        LinearLayout invite_id_layout;
        ImageView user_id_img, profession_img, place_img;
        TextView name, profession, rating, place;
        Button invite_btn;
        RatingBar ratingBar;

        public ids_holder(View itemview) {
            super(itemview);
            view = itemview;

            b_0_1_Session session = new b_0_1_Session(itemview.getContext());
            HashMap<String, String> userDetails = session.getuserdetailFromSession();
            user_phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

            invite_id_layout = itemView.findViewById(R.id.invite_user_id_layout_id);
            user_id_img = itemView.findViewById(R.id.invite_user_id_imgvw_id);
            profession_img = itemView.findViewById(R.id.invite_user_id_profession_imgvw_id);
            name = itemView.findViewById(R.id.invite_user_id_name_txtvw_id);
            profession = itemView.findViewById(R.id.invite_user_id_professiontxtvw_id);
            rating = itemView.findViewById(R.id.invite_user_id_ratingtxtvw_id);
            place = itemView.findViewById(R.id.invite_user_id_placetxtvw_id);

            invite_btn = itemView.findViewById(R.id.invite_user_id_invite_btn_id);
            ratingBar = itemView.findViewById(R.id.invite_user_id_ratingbar_id);
            place_img = itemView.findViewById(R.id.invite_user_id_placeimg_id);

        }
    }
}
