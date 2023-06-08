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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link f_2_2_Builder_page_fragment_2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class f_2_2_Builder_page_fragment_2 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    RecyclerView builder_page_ids_recyclerView;
    f_0_0_0_idsAdepter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public f_2_2_Builder_page_fragment_2() {

    }

    public static f_2_2_Builder_page_fragment_2 newInstance(String param1, String param2) {
        f_2_2_Builder_page_fragment_2 fragment = new f_2_2_Builder_page_fragment_2();
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
        View view = inflater.inflate(R.layout.fragment_f_2_2_builder_page_2, container, false);

        builder_page_ids_recyclerView = view.findViewById(R.id.builder_page_2_fragment_recyclerview_id);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, false);
        builder_page_ids_recyclerView.setLayoutManager(layoutManager);

        Query query = db.collection("users")
                .whereEqualTo("Profession", "Builder")
                .orderBy("Rating", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<f_0_0_0_ids_model> datalist = new
                FirestoreRecyclerOptions.Builder<f_0_0_0_ids_model>()
                .setQuery(query, f_0_0_0_ids_model.class)
                .build();
        adapter = new f_0_0_0_idsAdepter(datalist);
        builder_page_ids_recyclerView.setNestedScrollingEnabled(false);
        builder_page_ids_recyclerView.setHasFixedSize(true);
        builder_page_ids_recyclerView.setAdapter(adapter);

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