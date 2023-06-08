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

public class f_1_2_Teacher_page_fragment_2 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    RecyclerView teacher_page_ids_recyclerView;
    f_0_0_0_idsAdepter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public f_1_2_Teacher_page_fragment_2() {

    }

    public static f_1_2_Teacher_page_fragment_2 newInstance(String param1, String param2) {
        f_1_2_Teacher_page_fragment_2 fragment = new f_1_2_Teacher_page_fragment_2();
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
        View view = inflater.inflate(R.layout.fragment_f_1_2_teacher_page_2, container, false);
        teacher_page_ids_recyclerView = view.findViewById(R.id.teacher_page_2_fragment_recyclerview_id);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, false);
        teacher_page_ids_recyclerView.setLayoutManager(layoutManager);

        Query query = db.collection("users")
                .whereEqualTo("Profession", "Teacher")
                .orderBy("Rating", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<f_0_0_0_ids_model> datalist = new
                FirestoreRecyclerOptions.Builder<f_0_0_0_ids_model>()
                .setQuery(query, f_0_0_0_ids_model.class)
                .build();
        adapter = new f_0_0_0_idsAdepter(datalist);
        teacher_page_ids_recyclerView.setNestedScrollingEnabled(false);
        teacher_page_ids_recyclerView.setHasFixedSize(true);
        teacher_page_ids_recyclerView.setAdapter(adapter);

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