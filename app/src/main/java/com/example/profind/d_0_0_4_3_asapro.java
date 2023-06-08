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

public class d_0_0_4_3_asapro extends AppCompatActivity implements View.OnClickListener {

    String phone;
    Button back_btn;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    d_0_0_4_7_Appointments_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_0_0_4_3_asapro);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        back_btn = findViewById(R.id.asapro_page__back_btn_id);
        recyclerView = findViewById(R.id.asapro_page_recyclerview_id);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManagerWrapper(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        Query query = db.collection("appointments")
                .document(phone)
                .collection("appointments")
                .whereEqualTo("professionals_phone", phone)
                .orderBy("booking_time", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<d_0_0_4_6_Appointments_model> datalist = new
                FirestoreRecyclerOptions.Builder<d_0_0_4_6_Appointments_model>()
                .setQuery(query, d_0_0_4_6_Appointments_model.class)
                .build();
        adapter = new d_0_0_4_7_Appointments_Adapter(datalist,"true");
        recyclerView.setAdapter(adapter);

        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.asapro_page__back_btn_id) {
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