package com.example.profind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class d_0_0_15_Time_gridAdapter extends BaseAdapter {
    ArrayList<String> time;
    Context context;
    String phone, day;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public d_0_0_15_Time_gridAdapter(ArrayList<String> time, String phone, String day, Context context) {
        this.time = time;
        this.phone = phone;
        this.day = day;
        this.context = context;
    }

    @Override
    public int getCount() {
        return time.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = layoutInflater.inflate(R.layout.sample_g_8_times, null);

        LinearLayout time_layout = view1.findViewById(R.id.times_layout_id);
        TextView times_text = view1.findViewById(R.id.time_textvw_id);
        times_text.setText(time.get(i));

        db.collection("appointments")
                .document(phone)
                .collection("appointments")
                .whereEqualTo("professionals_phone", phone)
                .whereEqualTo("appointment_time", time.get(i))
                .whereEqualTo("appointment_date", day)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            time_layout.setBackgroundResource(R.drawable.custom_button_7);
                        }
                    }
                });

        return view1;
    }
}
