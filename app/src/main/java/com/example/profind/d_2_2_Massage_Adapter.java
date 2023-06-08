package com.example.profind;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Date;


public class d_2_2_Massage_Adapter extends FirestoreRecyclerAdapter<d_2_1_Massage_model, d_2_2_Massage_Adapter.massages_holder> {

    float time_1, time_2, duration;
    String time;
    int t;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public d_2_2_Massage_Adapter(@NonNull FirestoreRecyclerOptions<d_2_1_Massage_model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull d_2_2_Massage_Adapter.massages_holder holder, @SuppressLint("RecyclerView") int position, @NonNull d_2_1_Massage_model model) {

        db.collection("users")
                .document(model.getPhone())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            if(!documentSnapshot.getString("Profile_Photo").equals("")){
                                Picasso.get()
                                        .load(documentSnapshot.getString("Profile_Photo"))
                                        .placeholder(R.drawable.profile_img)
                                        .into(holder.profile_img);
                            }
                            holder.profile_name.setText(documentSnapshot.getString("Name"));
                        }
                    }
                });

        holder.massage_txt.setText(model.getMassage());
        time_1 = Float.parseFloat(model.getTime());
        time_2 = new Date().getTime();
        duration = (time_2 - time_1) / 1000;
        calculate_time(duration);
        holder.time_txt.setText(time);

        holder.massage_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), d_2_0_Massage_person.class);
                intent.putExtra("profile_id_key", model.getPhone());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
        holder.profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), d_2_0_Massage_person.class);
                intent.putExtra("profile_id_key", model.getPhone());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
        holder.profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), d_2_0_Massage_person.class);
                intent.putExtra("profile_id_key", model.getPhone());
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

    @NonNull
    @Override
    public d_2_2_Massage_Adapter.massages_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_g_7_massages, parent, false);
        return new d_2_2_Massage_Adapter.massages_holder(view);
    }

    class massages_holder extends RecyclerView.ViewHolder {

        ImageView profile_img;
        TextView profile_name, massage_txt, time_txt;
        LinearLayout massage_layout;

        public massages_holder(View itemview) {
            super(itemview);
            profile_img = itemview.findViewById(R.id.massage_user_img_id);
            profile_name = itemview.findViewById(R.id.massage_user_name_id);
            massage_txt = itemview.findViewById(R.id.last_massage_text_id);
            time_txt = itemview.findViewById(R.id.last_massage_time_text_id);
            massage_layout = itemview.findViewById(R.id.massage_user_layout_id);
        }
    }
}
