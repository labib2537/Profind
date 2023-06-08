package com.example.profind;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class d_2_0_Massage_person extends AppCompatActivity implements View.OnClickListener {
    Button back_btn;
    ImageView profile_img;
    TextView profile_name;
    RecyclerView recyclerView;
    TextInputEditText massage_txt;
    ImageButton massage_sent_btn;
    String user_phone,phone, massage_str,sender_room,receiver_room;
    d_2_3_Massages_person_Adapter adapter;
    ArrayList<d_2_1_Massage_model> massages;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_2_0_massage_person);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        user_phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        phone = getIntent().getStringExtra("profile_id_key");

        back_btn = findViewById(R.id.massage_page_back_btn_id);
        profile_img = findViewById(R.id.massage_page_user_img_id);
        profile_name = findViewById(R.id.massage_page_user_name_id);
        recyclerView = findViewById(R.id.massage_recyclerview_id);
        massage_txt = findViewById(R.id.massage_edtxt_id);
        massage_sent_btn = findViewById(R.id.send_massage_btn_id);

        DocumentReference addedDocRef = db.collection("users").document(phone);
        addedDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if(!documentSnapshot.getString("Profile_Photo").isEmpty()){
                        Picasso.get()
                                .load(documentSnapshot.getString("Profile_Photo"))
                                .placeholder(R.drawable.profile_img)
                                .into(profile_img);
                    }
                    profile_name.setText(documentSnapshot.getString("Name"));
                }
            }
        });

        sender_room=user_phone+phone;
        receiver_room=phone+user_phone;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView .setLayoutManager(layoutManager);
        massages=new ArrayList<>();
        adapter=new d_2_3_Massages_person_Adapter(this,massages);
        recyclerView.setHasFixedSize(true);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("massages")
                .document(sender_room)
                .collection("chats")
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<DocumentSnapshot> list=value.getDocuments();
                        massages.clear();
                        for(DocumentSnapshot documentSnapshot:list){
                            d_2_1_Massage_model obj=documentSnapshot.toObject(d_2_1_Massage_model.class);
                            obj.setMassage_id(documentSnapshot.getId());
                            massages.add(obj);
                        }
                        recyclerView .setAdapter(adapter);
                    }
                });


        profile_name.setOnClickListener(this);
        profile_img.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        massage_sent_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.massage_page_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.massage_page_user_img_id) {
            Intent intent = new Intent(view.getContext(), d_0_0_Profile_view.class);
            intent.putExtra("profile_id_key", phone);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        } else if (view.getId() == R.id.massage_page_user_name_id) {
            Intent intent = new Intent(view.getContext(), d_0_0_Profile_view.class);
            intent.putExtra("profile_id_key", phone);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        } else if (view.getId() == R.id.send_massage_btn_id) {
            massage_str = massage_txt.getText().toString().trim();
            if(massage_str.equals("")){
                Toast.makeText(getApplicationContext(), "Write something !", Toast.LENGTH_LONG).show();
            }else {
                Date date = new Date();
                d_2_1_Massage_model massage = new d_2_1_Massage_model(date.getTime() + "", user_phone, massage_str);
                d_2_1_Massage_model last_massage = new d_2_1_Massage_model(date.getTime() + "", phone, massage_str);
                db.collection("massages")
                        .document(sender_room)
                        .collection("chats")
                        .add(massage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        db.collection("massages")
                                .document(receiver_room)
                                .collection("chats")
                                .add(massage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                db.collection("last_massage").document(user_phone).collection("show_massages")
                                        .document(sender_room).set(last_massage);
                                db.collection("last_massage").document(phone).collection("show_massages")
                                        .document(receiver_room).set(last_massage);
                            }
                        });
                    }
                });
                massage_txt.setText("");
            }
        }
    }
}