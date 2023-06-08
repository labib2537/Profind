package com.example.profind;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;


public class e_2_1_Notification_adapter extends FirestoreRecyclerAdapter<e_2_0_Notification_model, e_2_1_Notification_adapter.notifications_holder> {

    int t;
    String time,txt;
    Context context;
    float time_1,time_2,duration;
    View view;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public e_2_1_Notification_adapter(@NonNull FirestoreRecyclerOptions<e_2_0_Notification_model> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull e_2_1_Notification_adapter.notifications_holder holder,
                                    @SuppressLint("RecyclerView") int position,
                                    @NonNull e_2_0_Notification_model model) {
        if(model.getType().equals("appointment")){
            txt="has taken your appointment.";
        }else if(model.getType().equals("comment")) {
            txt = "and "+model.getTotal_notifications()+" others has commented on your post.";
        }
        else if(model.getType().equals("react")) {
            txt = "and "+model.getTotal_notifications()+" others has reacted on your post.";
        }
        db.collection("users")
                .whereEqualTo("Phone_Number", model.commenter_liker_appointer_phone)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if(!document.getString("Profile_Photo").equals("")){
                                Picasso.get()
                                        .load(document.getString("Profile_Photo"))
                                        .placeholder(R.drawable.profile_img)
                                        .into(holder.profile_img);
                            }
                            holder.name_txtvw.setText(Html.fromHtml("<b>" + document.getString("Name") + "</b> <font color=\"#808080\">"+txt+"</font>"));
                        }
                    }
                });
        time_1 = Float.parseFloat(model.getTime());
        time_2 = new Date().getTime();
        duration = (time_2 - time_1) / 1000;
        calculate_time(duration);
        holder.time_txtvw.setText(time);
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
    public e_2_1_Notification_adapter.notifications_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_g_10_notifications, parent, false);
        return new e_2_1_Notification_adapter.notifications_holder(view);
    }

    class notifications_holder extends RecyclerView.ViewHolder {

        ImageView profile_img;
        TextView name_txtvw,time_txtvw;

        public notifications_holder(View itemview) {
            super(itemview);
            view = itemview;

            profile_img=itemview.findViewById(R.id.notification_user_img_id);
            name_txtvw=itemview.findViewById(R.id.notification_user_name_id);
            time_txtvw=itemview.findViewById(R.id.notification_time_text_id);

        }
    }
}
