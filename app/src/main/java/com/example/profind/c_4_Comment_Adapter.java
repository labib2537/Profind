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
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class c_4_Comment_Adapter extends RecyclerView.Adapter<c_4_Comment_Adapter.myviewholder> {

    String phone,time,user_phone;
    float time_1,time_2,duration;
    int t;
    Intent intent;
    ArrayList<c_3_Comment_model> comment_list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public c_4_Comment_Adapter(ArrayList<c_3_Comment_model> comment_list) {
        this.comment_list = comment_list;
    }

    @Override
    public void onBindViewHolder(@NonNull c_4_Comment_Adapter.myviewholder holder, @SuppressLint("RecyclerView") int position) {
        phone = comment_list.get(position).getUser_id();
        db.collection("users")
                .whereEqualTo("Phone_Number", phone)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            holder.name_txt.setText(document.getString("Name"));
                            Picasso.get()
                                    .load(document.getString("Profile_Photo"))
                                    .placeholder(R.drawable.background_img)
                                    .into(holder.profile_img);
                        }
                    }
                });
        holder.comment_txt.setText(comment_list.get(position).getComment_text());
        time_1 = Float.parseFloat(comment_list.get(position).getTime());
        time_2 = new Date().getTime();
        duration = (time_2 - time_1) / 1000;
        calculate_time(duration);
        holder.time_txt.setText(time);

        holder.profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comment_list.get(position).getUser_id().equals(user_phone)) {
                    intent = new Intent(view.getContext(), d_0_0_Profile.class);
                } else {

                    intent = new Intent(view.getContext(), d_0_0_Profile_view.class);
                    intent.putExtra("profile_id_key", comment_list.get(position).getUser_id());
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);

            }
        });
        holder.name_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comment_list.get(position).getUser_id().equals(user_phone)) {
                    intent = new Intent(view.getContext(), d_0_0_Profile.class);
                } else {

                    intent = new Intent(view.getContext(), d_0_0_Profile_view.class);
                    intent.putExtra("profile_id_key", comment_list.get(position).getUser_id());
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
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

    @Override
    public int getItemCount() {
        return comment_list.size();
    }

    @NonNull
    @Override
    public c_4_Comment_Adapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_g_5_comments, parent, false);
        return new c_4_Comment_Adapter.myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        ImageView profile_img;
        TextView name_txt, comment_txt, time_txt;

        public myviewholder(@NonNull View itemview) {
            super(itemview);

            b_0_1_Session session = new b_0_1_Session(itemview.getContext());
            HashMap<String, String> userDetails = session.getuserdetailFromSession();
            user_phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

            profile_img = itemView.findViewById(R.id.comment_user_img_id);
            name_txt = itemView.findViewById(R.id.comment_user_name_id);
            comment_txt = itemView.findViewById(R.id.comment_text_id);
            time_txt = itemView.findViewById(R.id.comment_time_text_id);

        }
    }
}