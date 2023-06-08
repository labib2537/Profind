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

import java.util.HashMap;

public class e_1_2_Group_page_fragment_2_mygroups extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    RecyclerView my_grps_recview;
    e_1_8_Group_searchAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public e_1_2_Group_page_fragment_2_mygroups() {

    }

    public static e_1_2_Group_page_fragment_2_mygroups newInstance(String param1, String param2) {
        e_1_2_Group_page_fragment_2_mygroups fragment = new e_1_2_Group_page_fragment_2_mygroups();
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
        View view = inflater.inflate(R.layout.fragment_e_1_2_group_page_2_groups, container, false);

        b_0_1_Session session=new b_0_1_Session(getContext());
        HashMap<String,String> userDetails=session.getuserdetailFromSession();
        String phone =userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        my_grps_recview = view.findViewById(R.id.my_groups_fragment_recyclerview_id);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, false);
        my_grps_recview.setLayoutManager(layoutManager);

        Query query = db.collection("groups").whereArrayContains("members",phone);

        FirestoreRecyclerOptions<e_1_7_Group_search_model> datalist = new
                FirestoreRecyclerOptions.Builder<e_1_7_Group_search_model>()
                .setQuery(query, e_1_7_Group_search_model.class)
                .build();
        adapter = new e_1_8_Group_searchAdapter(datalist);
        my_grps_recview.setNestedScrollingEnabled(false);
        my_grps_recview.setHasFixedSize(true);
        my_grps_recview.setAdapter(adapter);


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