package com.example.profind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

public class d_0_0_4_4_asaclient extends AppCompatActivity implements View.OnClickListener {

    Button back_btn;
    d_0_0_4_7_Appointments_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_0_0_4_4_asaclient);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        String phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        back_btn = findViewById(R.id.asaclient_page__back_btn_id);
        RecyclerView recyclerView = findViewById(R.id.asaclient_page_recyclerview_id);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManagerWrapper(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("appointments")
                .document(phone)
                .collection("appointments")
                .whereEqualTo("clients_phone", phone)
                .orderBy("booking_time", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<d_0_0_4_6_Appointments_model> datalist = new
                FirestoreRecyclerOptions.Builder<d_0_0_4_6_Appointments_model>()
                .setQuery(query, d_0_0_4_6_Appointments_model.class)
                .build();
        adapter = new d_0_0_4_7_Appointments_Adapter(datalist,"false");
        recyclerView.setAdapter(adapter);

        back_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.asaclient_page__back_btn_id) {
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
        adapter.stopListening();
    }

}