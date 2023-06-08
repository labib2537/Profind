package com.example.profind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class d_0_0_13_img_vdo_showAdapter extends RecyclerView.Adapter<d_0_0_13_img_vdo_showAdapter.myviewholder> {

    String[] img_vdo, indicator;
    Context context;

    public d_0_0_13_img_vdo_showAdapter(Context context,String[] img_vdo, String[] indicator) {
        this.context = context;
        this.img_vdo = img_vdo;
        this.indicator = indicator;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.sample_g_1_img_vdo_frame,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position) {
        int layout_width = LinearLayout.LayoutParams.MATCH_PARENT;
        LinearLayout.LayoutParams lout_empty = new LinearLayout.LayoutParams(0, 0);
        LinearLayout.LayoutParams lout_not_empty = new LinearLayout.LayoutParams(layout_width, 800);
        lout_not_empty.setMargins(0, 0, 0, 10);


        if (indicator[position].equals("0")) {
            Picasso.get()
                    .load(img_vdo[position])
                    .placeholder(R.drawable.profile_img)
                    .into(holder.img);
            holder.img.setLayoutParams(lout_not_empty);
            holder.vdo.setLayoutParams(lout_empty);
        } else if (indicator[position].equals("1")) {
            Uri vdo_uri = Uri.parse(img_vdo[position]);
            holder.vdo.setVideoURI(vdo_uri);
            holder.vdo.setLayoutParams(lout_not_empty);
            holder.img.setLayoutParams(lout_empty);
            video_controler(holder.vdo);
        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (img_vdo[position] != null) {
                    Intent intent = new Intent(view.getContext(), d_0_0_3_Full_image_view.class);
                    intent.putExtra("fullimgkey", img_vdo[position]);
                    view.getContext().startActivity(intent);
                } else {
                    Toast.makeText(view.getContext(), "No Profile Image exist !", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return img_vdo.length;
    }

    public void video_controler(VideoView v) {
        MediaController mediaController = new MediaController(v.getContext());
        mediaController.setAnchorView(v);
        v.setMediaController(mediaController);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        ImageView img;
        VideoView vdo;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_vdo_img_id);
            vdo = itemView.findViewById(R.id.img_vdo_vdo_id);
        }
    }
}
