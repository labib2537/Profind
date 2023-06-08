package com.example.profind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class d_1_Search extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Button back_btn;
    TextInputEditText search_edttxt;
    RecyclerView search_recview;
    f_0_0_0_idsAdepter adapter;
    Query query;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_1_search);
        getSupportActionBar().hide();

        back_btn = findViewById(R.id.search_page_back_btn_id);
        search_edttxt = findViewById(R.id.search_page_edtxt_id);
        search_recview = findViewById(R.id.search_page_recyclerview_id);
        search_recview.setLayoutManager(new LinearLayoutManagerWrapper(this));

        Query query = db.collection("users")
                .orderBy("Rating", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<f_0_0_0_ids_model> datalist = new
                FirestoreRecyclerOptions.Builder<f_0_0_0_ids_model>()
                .setQuery(query, f_0_0_0_ids_model.class)
                .build();

        adapter = new f_0_0_0_idsAdepter(datalist);
        search_recview.setNestedScrollingEnabled(false);
        search_recview.setHasFixedSize(true);
        search_recview.setAdapter(adapter);
        search_edttxt.addTextChangedListener(this);
        back_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search_page_back_btn_id) {
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
        search_recview.setLayoutManager(new LinearLayoutManager(this));
        if (editable.toString().isEmpty()) {

            query = db.collection("users");

        } else {
            query = db.collection("users")
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