package com.example.profind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class e_1_5_Group_search extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    Button back_btn;
    TextInputEditText search_edttxt;
    RecyclerView search_recview;
    e_1_8_Group_searchAdapter adapter;
    Query query;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_e_1_5_group_search);
        getSupportActionBar().hide();


        back_btn = findViewById(R.id.search_grp_page_back_btn_id);
        search_edttxt = findViewById(R.id.search_grp_page_edtxt_id);
        search_recview = findViewById(R.id.search_grp_page_recyclerview_id);

        search_recview.setLayoutManager(new LinearLayoutManagerWrapper(this));

        Query query = db.collection("groups");

        FirestoreRecyclerOptions<e_1_7_Group_search_model> datalist = new
                FirestoreRecyclerOptions.Builder<e_1_7_Group_search_model>()
                .setQuery(query, e_1_7_Group_search_model.class)
                .build();
        adapter = new e_1_8_Group_searchAdapter(datalist);
        search_recview.setNestedScrollingEnabled(false);
        search_recview.setHasFixedSize(true);
        search_recview.setAdapter(adapter);

        search_edttxt.addTextChangedListener(this);
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search_grp_page_back_btn_id) {
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

            query = db.collection("groups");

        } else {
            query = db.collection("groups")
                    .orderBy("group_name_lower")
                    .startAt(editable.toString().toLowerCase())
                    .endAt(editable.toString().toLowerCase()+"\uf8ff");

        }
        FirestoreRecyclerOptions<e_1_7_Group_search_model> datalist = new
                FirestoreRecyclerOptions.Builder<e_1_7_Group_search_model>()
                .setQuery(query, e_1_7_Group_search_model.class)
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