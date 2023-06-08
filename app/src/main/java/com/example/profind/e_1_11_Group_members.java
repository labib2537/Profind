package com.example.profind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class e_1_11_Group_members extends AppCompatActivity implements View.OnClickListener {
    Button back_btn;
    RecyclerView recyclerView;
    String group_id;
    f_0_0_0_idsAdepter adapter;
    Query query;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docIdRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_e_1_11_group_members);
        getSupportActionBar().hide();

        b_0_1_Session session=new b_0_1_Session(this);
        HashMap<String,String> userDetails=session.getuserdetailFromSession();
        String phone =userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        back_btn= findViewById(R.id.group_members_page_back_btn_id);
        recyclerView= findViewById(R.id.group_members_page_recyclerview_id);

        group_id = getIntent().getStringExtra("group_id_key");

        back_btn.setOnClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManagerWrapper(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        query=db.collection("users").whereArrayContains("groups",group_id);
        FirestoreRecyclerOptions<f_0_0_0_ids_model> datalist = new
                FirestoreRecyclerOptions.Builder<f_0_0_0_ids_model>()
                .setQuery(query, f_0_0_0_ids_model.class)
                .build();
        adapter = new f_0_0_0_idsAdepter(datalist);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.group_members_page_back_btn_id) {
            finish();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}