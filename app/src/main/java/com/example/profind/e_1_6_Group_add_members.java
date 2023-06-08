package com.example.profind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class e_1_6_Group_add_members extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Button back_btn;
    TextInputEditText invite_edttxt;
    RecyclerView invite_recview;
    String group_id, phone, admin_phone;
    e_1_9_inviteAdepter adapter;
    Query query;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_e_1_6_group_add_members);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);


        group_id = getIntent().getStringExtra("group_id_key");

        back_btn = findViewById(R.id.invite_page_back_btn_id);
        invite_edttxt = findViewById(R.id.invite_page_edtxt_id);
        invite_recview = findViewById(R.id.invite_page_recyclerview_id);

        invite_recview.setLayoutManager(new LinearLayoutManagerWrapper(this));

        db.collection("groups").document(group_id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                admin_phone = documentSnapshot.getString("phone");
            }
        });

        Query query = db.collection("users")
                .whereEqualTo("Pro_Account", "Yes")
                .orderBy("Rating", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<f_0_0_0_ids_model> datalist = new
                FirestoreRecyclerOptions.Builder<f_0_0_0_ids_model>()
                .setQuery(query, f_0_0_0_ids_model.class)
                .build();
        adapter = new e_1_9_inviteAdepter(datalist, group_id);
        invite_recview.setNestedScrollingEnabled(false);
        invite_recview.setHasFixedSize(true);
        invite_recview.setAdapter(adapter);


        invite_edttxt.addTextChangedListener(this);
        back_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.invite_page_back_btn_id) {
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        invite_recview.setLayoutManager(new LinearLayoutManager(this));
        if (editable.toString().isEmpty()) {

            query = db.collection("users").whereEqualTo("Pro_Account", "Yes");

        } else {
            query = db.collection("users")
                    .whereEqualTo("Pro_Account", "Yes")
                    .orderBy("Name_lower")
                    .startAt(editable.toString().toLowerCase())
                    .endAt(editable.toString().toLowerCase() + "\uf8ff");

        }
        FirestoreRecyclerOptions<f_0_0_0_ids_model> datalist = new
                FirestoreRecyclerOptions.Builder<f_0_0_0_ids_model>()
                .setQuery(query, f_0_0_0_ids_model.class)
                .build();
        adapter.updateOptions(datalist);
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