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

public class d_0_0_4_7_Appointments_Adapter extends FirestoreRecyclerAdapter<d_0_0_4_6_Appointments_model, d_0_0_4_7_Appointments_Adapter.appointments_holder> {

    String asapro;
    View view;
    Intent intent;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public d_0_0_4_7_Appointments_Adapter(@NonNull FirestoreRecyclerOptions<d_0_0_4_6_Appointments_model> options, String asapro) {
        super(options);
        this.asapro = asapro;
    }

    @Override
    protected void onBindViewHolder(@NonNull d_0_0_4_7_Appointments_Adapter.appointments_holder holder, @SuppressLint("RecyclerView") int position, @NonNull d_0_0_4_6_Appointments_model model) {
        try {
            int layout_width = LinearLayout.LayoutParams.MATCH_PARENT;
            int layout_height = LinearLayout.LayoutParams.WRAP_CONTENT;
            LinearLayout.LayoutParams lout_empty = new LinearLayout.LayoutParams(0, 0);
            LinearLayout.LayoutParams lout_not_empty = new LinearLayout.LayoutParams(layout_width, layout_height);

            if (asapro.equals("true")) {
                holder.profession_layout.setLayoutParams(lout_empty);
                db.collection("users")
                        .document(model.getClients_phone())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Picasso.get()
                                            .load(documentSnapshot.getString("Profile_Photo"))
                                            .placeholder(R.drawable.profile_img)
                                            .into(holder.profile_img);
                                    holder.name.setText(documentSnapshot.getString("Name"));
                                }
                            }
                        });
            } else {
                holder.profession_layout.setLayoutParams(lout_not_empty);
                db.collection("users")
                        .document(model.getProfessionals_phone())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Picasso.get()
                                            .load(documentSnapshot.getString("Profile_Photo"))
                                            .placeholder(R.drawable.profile_img)
                                            .into(holder.profile_img);
                                    holder.name.setText(documentSnapshot.getString("Name"));
                                    holder.profession_name.setText(documentSnapshot.getString("Profession"));
                                    if (documentSnapshot.getString("Profession").equals("Doctor")) {
                                        holder.profession_logo.setImageResource(R.drawable.doctor_icon);
                                    } else if (documentSnapshot.getString("Profession").equals("Builder")) {
                                        holder.profession_logo.setImageResource(R.drawable.builder_icon);
                                    } else if (documentSnapshot.getString("Profession").equals("Teacher")) {
                                        holder.profession_logo.setImageResource(R.drawable.teacher_icon);
                                    } else if (documentSnapshot.getString("Profession").equals("Lawyer")) {
                                        holder.profession_logo.setImageResource(R.drawable.lawyer_icon);
                                    }
                                }
                            }
                        });
            }
            holder.Time.setText("Date : "+model.getAppointment_date()+"  |  Time : "+model.getAppointment_time());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(view.getContext(), d_0_0_4_8_Appointments_detailes.class);
                    intent.putExtra("appointment_id", getSnapshots().getSnapshot(position).getId());
                    intent.putExtra("client_phone", model.getClients_phone());
                    intent.putExtra("professionals_phone", model.getProfessionals_phone());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                }
            });

        } catch (Exception e) {
            Toast.makeText(view.getContext(), e + "", Toast.LENGTH_SHORT).show();
        }
    }

    public d_0_0_4_7_Appointments_Adapter.appointments_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_g_9_appointments, parent, false);
        return new d_0_0_4_7_Appointments_Adapter.appointments_holder(view);
    }

    class appointments_holder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout, profession_layout;
        ImageView profile_img, profession_logo;
        TextView name, profession_name, Time;

        public appointments_holder(View itemview) {
            super(itemview);
            view = itemview;

            linearLayout = itemview.findViewById(R.id.appointment_layout_id);
            profession_layout = itemview.findViewById(R.id.appointment_layout_profession_id);
            profile_img = itemview.findViewById(R.id.appointment_imgvw_id);
            profession_logo = itemview.findViewById(R.id.appointment_profession_imgvw_id);
            name = itemview.findViewById(R.id.appointment_name_txtvw_id);
            profession_name = itemview.findViewById(R.id.appointment_professiontxtvw_id);
            Time = itemview.findViewById(R.id.appointment_ratingtxtvw_id);

        }
    }
}
