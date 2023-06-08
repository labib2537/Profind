package com.example.profind;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class f_2_1_Builder_page_fragment_1 extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    RecyclerView doctor_page_recyclerView;
    int width_half;
    d_0_0_11_postsAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference=db.collection("posts");

    public f_2_1_Builder_page_fragment_1(int width_half) {
        this.width_half = width_half;
    }


    public static f_2_1_Builder_page_fragment_1 newInstance(String param1, String param2,int width_half) {
        f_2_1_Builder_page_fragment_1 fragment = new f_2_1_Builder_page_fragment_1(width_half);
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

        View view = inflater.inflate(R.layout.fragment_f_2_1_builder_page_1, container, false);

        doctor_page_recyclerView = view.findViewById(R.id.builder_page_1_fragment_recyclerview_id);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, false);
        doctor_page_recyclerView.setLayoutManager(layoutManager);

        Query query=collectionReference.whereEqualTo("profession", "Builder").orderBy("posted_at", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<d_0_0_10_posts_model> datalist=new
                FirestoreRecyclerOptions.Builder<d_0_0_10_posts_model>()
                .setQuery(query, d_0_0_10_posts_model.class)
                .build();
        adapter = new d_0_0_11_postsAdapter(datalist,width_half);
        doctor_page_recyclerView.setNestedScrollingEnabled(false);
        doctor_page_recyclerView.setHasFixedSize(true);
        doctor_page_recyclerView.setAdapter(adapter);

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