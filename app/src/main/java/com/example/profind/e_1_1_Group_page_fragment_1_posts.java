package com.example.profind;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class e_1_1_Group_page_fragment_1_posts extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    SwipeRefreshLayout swipeRefreshLayout;
    int width_half;
    RecyclerView recyclerView;
    d_0_0_11_postsAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public e_1_1_Group_page_fragment_1_posts(int width_half) {
        this.width_half = width_half;
    }

    public static e_1_1_Group_page_fragment_1_posts newInstance(String param1, String param2, int width_half) {
        e_1_1_Group_page_fragment_1_posts fragment = new e_1_1_Group_page_fragment_1_posts(width_half);
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
        View view= inflater.inflate(R.layout.fragment_e_1_1_group_page_1_posts, container, false);

        swipeRefreshLayout= view.findViewById(R.id.group_fragment_swiperefresh_id);
        recyclerView = view.findViewById(R.id.group_fragment_recyclerview_id);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        Query query = db.collection("posts")
                .whereEqualTo("post_type", "group")
                .orderBy("posted_at", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<d_0_0_10_posts_model> datalist = new
                FirestoreRecyclerOptions.Builder<d_0_0_10_posts_model>()
                .setQuery(query, d_0_0_10_posts_model.class)
                .build();
        adapter = new d_0_0_11_postsAdapter(datalist, width_half);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

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