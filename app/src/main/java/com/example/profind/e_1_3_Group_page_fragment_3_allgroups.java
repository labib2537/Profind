package com.example.profind;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class e_1_3_Group_page_fragment_3_allgroups extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    RecyclerView all_grps_recview;
    e_1_8_Group_searchAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public e_1_3_Group_page_fragment_3_allgroups() {

    }
    public static e_1_3_Group_page_fragment_3_allgroups newInstance(String param1, String param2) {
        e_1_3_Group_page_fragment_3_allgroups fragment = new e_1_3_Group_page_fragment_3_allgroups();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_e_1_3_group_page_3_find, container, false);

        all_grps_recview = view.findViewById(R.id.all_groups_fragment_recyclerview_id);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, false);
        all_grps_recview.setLayoutManager(layoutManager);

        Query query = db.collection("groups");

        FirestoreRecyclerOptions<e_1_7_Group_search_model> datalist = new
                FirestoreRecyclerOptions.Builder<e_1_7_Group_search_model>()
                .setQuery(query, e_1_7_Group_search_model.class)
                .build();
        adapter = new e_1_8_Group_searchAdapter(datalist);
        all_grps_recview.setNestedScrollingEnabled(false);
        all_grps_recview.setHasFixedSize(true);
        all_grps_recview.setAdapter(adapter);

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}