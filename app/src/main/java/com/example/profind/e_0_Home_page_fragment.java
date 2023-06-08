package com.example.profind;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class e_0_Home_page_fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    c_0_HomePage homePage;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView home_page_recyclerView;
    ArrayList<d_0_0_10_posts_model> post_list;
    int width_half;
    d_0_0_11_1_home_postsAdapter adapter;

    public e_0_Home_page_fragment(int width_half) {
        this.width_half = width_half;
    }

    public static e_0_Home_page_fragment newInstance(String param1, String param2, int width_half) {
        e_0_Home_page_fragment fragment = new e_0_Home_page_fragment(width_half);
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
        View view = inflater.inflate(R.layout.fragment_e_0_home_page_fragment, container, false);

        homePage = (c_0_HomePage) getActivity();
        home_page_recyclerView = view.findViewById(R.id.home_page_fragment_recyclerview_id);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, false);
        home_page_recyclerView.setLayoutManager(layoutManager);

        post_list = new ArrayList<>();
        adapter = new d_0_0_11_1_home_postsAdapter(post_list, width_half);
        home_page_recyclerView.setNestedScrollingEnabled(false);
        home_page_recyclerView.setHasFixedSize(true);
        home_page_recyclerView.setAdapter(adapter);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .orderBy("posted_at", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list) {
                            d_0_0_10_posts_model obj = documentSnapshot.toObject(d_0_0_10_posts_model.class);
                            obj.setPost_id(documentSnapshot.getId());
                            post_list.add(obj);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        final int[] state = new int[1];
        home_page_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                state[0] = newState;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && (state[0]==0 || state[0]==2)) {
                    hideToolbar();
                } else if(dy<-10){
                    showToolbar();
                }
            }
        });


        swipeRefreshLayout = view.findViewById(R.id.home_page_fragment_swiperefresh_id);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void hideToolbar() {
        homePage.top_toolbar.setVisibility(View.GONE);
        homePage.top_toolbar_background.setVisibility(View.GONE);
    }

    private void showToolbar() {
        homePage.top_toolbar.setVisibility(View.VISIBLE);
        homePage.top_toolbar_background.setVisibility(View.VISIBLE);
    }

}