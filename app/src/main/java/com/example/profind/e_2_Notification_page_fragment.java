package com.example.profind;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;


public class e_2_Notification_page_fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    int width_half;
    Context context;
    String phone;
    RecyclerView notification_recyclerView;
    e_2_1_Notification_adapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public e_2_Notification_page_fragment(int width_half, Context context) {
        this.width_half = width_half;
        this.context=context;
    }

    public static e_2_Notification_page_fragment newInstance(String param1, String param2, int width_half, Context context) {
        e_2_Notification_page_fragment fragment = new e_2_Notification_page_fragment(width_half,context);
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
        View view = inflater.inflate(R.layout.fragment_e_2_notification_page_fragment, container, false);

        b_0_1_Session session = new b_0_1_Session(context);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        notification_recyclerView = view.findViewById(R.id.notification_page_fragment_recyclerview_id);

        notification_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = db.collection("notifications")
                .whereEqualTo("poster_phone",phone)
                .orderBy("time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<e_2_0_Notification_model> datalist = new
                FirestoreRecyclerOptions.Builder<e_2_0_Notification_model>()
                .setQuery(query, e_2_0_Notification_model.class)
                .build();
        adapter = new e_2_1_Notification_adapter(datalist,context);
        notification_recyclerView.setNestedScrollingEnabled(false);
        notification_recyclerView.setHasFixedSize(true);
        notification_recyclerView.setAdapter(adapter);

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