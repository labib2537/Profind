package com.example.profind;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class e_1_8_Group_searchAdapter extends FirestoreRecyclerAdapter<e_1_7_Group_search_model, e_1_8_Group_searchAdapter.ids_holder> {

    String _name, _banner_img, _members,_created_time;
    View view;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public e_1_8_Group_searchAdapter(@NonNull FirestoreRecyclerOptions<e_1_7_Group_search_model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ids_holder holder, @SuppressLint("RecyclerView") int position, @NonNull e_1_7_Group_search_model model) {

        try {
            _name = model.getGroup_name();
            _banner_img= model.getBanner();
            _members= model.getTotal_members();
            _created_time=model.getTime();
            Picasso.get()
                    .load(_banner_img)
                    .placeholder(R.drawable.profile_img)
                    .into(holder.grp_banner);

            holder.name.setText(" "+_name);
            holder.total_members_time.setText("Members : "+_members+" | Since : "+_created_time);
            holder.group_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), e_1_10_Group_profile.class);
                    intent.putExtra("group_id_key", getSnapshots().getSnapshot(position).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                }
            });

        } catch (Exception e) {
            Toast.makeText(view.getContext(), e + "", Toast.LENGTH_SHORT).show();
        }

    }


    @NonNull
    @Override
    public ids_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_g_3_groups_frame, parent, false);
        return new ids_holder(view);
    }

    class ids_holder extends RecyclerView.ViewHolder {

        LinearLayout group_layout;
        ImageView grp_banner;
        TextView name,total_members_time;

        public ids_holder(View itemview) {
            super(itemview);
            view = itemview;

            group_layout = itemView.findViewById(R.id.group_layout_id);
            grp_banner = itemView.findViewById(R.id.group_banner_imgvw_id);
            name = itemView.findViewById(R.id.group_name_txtvw_id);
            total_members_time = itemView.findViewById(R.id.group_member_txtvw_id);
        }
    }
}
