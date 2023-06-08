package com.example.profind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

public class d_2_Massage extends AppCompatActivity implements View.OnClickListener {
    String user_phone;
    Button back_btn;
    RecyclerView recyclerView;
    d_2_2_Massage_Adapter adapter;
    Query query;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_2_massage);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        user_phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        back_btn = findViewById(R.id.massages_page_back_btn_id);
        recyclerView = findViewById(R.id.massages_recyclerview_id);
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(this));
        query = db.collection("last_massage")
                .document(user_phone)
                .collection("show_massages")
                .orderBy("time", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<d_2_1_Massage_model> datalist = new
                FirestoreRecyclerOptions.Builder<d_2_1_Massage_model>()
                .setQuery(query, d_2_1_Massage_model.class)
                .build();
        adapter = new d_2_2_Massage_Adapter(datalist);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        finish();
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