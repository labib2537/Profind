package com.example.profind;

import static android.icu.text.DisplayContext.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class d_0_0_14_Take_appointment extends AppCompatActivity implements View.OnClickListener {

    LinearLayout time_layout;
    Button back_btn, proceed_btn;
    TextView selected_time, selected_date;
    String phone_of_professional, user_phone, start_time, end_time, first_break, second_break, appointment_date, slctd_time, ampm;
    double m;
    int y, day, new_year, fbreak_duration, sbreak_duration, apnmnt_duration;
    ArrayList<String> dbdays = new ArrayList<String>();
    ArrayList<String> time_slots = new ArrayList<String>();
    CalendarView calendarView;
    GridView gridView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_0_0_14_take_appointment);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        user_phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        phone_of_professional = getIntent().getStringExtra("profile_id_key");
        query = db.collection("appointment_schedule").whereEqualTo("phone", phone_of_professional);

        time_layout = findViewById(R.id.take_appointment_page_time_layout_id);
        selected_date = findViewById(R.id.take_appointment_page_selected_date_id);
        selected_time = findViewById(R.id.take_appointment_page_selected_time_id);
        back_btn = findViewById(R.id.take_appointment_page_back_btn_id);
        proceed_btn = findViewById(R.id.take_appointment_page_proceed_btn_id);
        gridView = (GridView) findViewById(R.id.take_appointment_page_time_grdvw_id);

        calendarView = findViewById(R.id.take_appointment_page_calender_id);
        calendarView.setMinDate(new Date().getTime());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                int k=i1+1;
                appointment_date = i2 + "-" + k + "-" + i;
                selected_date.setText(appointment_date);
                show_date_func(i, i1, i2);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                slctd_time = time_slots.get(i);
                db.collection("appointments")
                        .document(phone_of_professional)
                        .collection("appointments")
                        .whereEqualTo("professionals_phone", phone_of_professional)
                        .whereEqualTo("appointment_time", slctd_time)
                        .whereEqualTo("appointment_date", appointment_date)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(queryDocumentSnapshots.isEmpty()){
                                    selected_time.setText(slctd_time);
                                }else {
                                    selected_time.setText("");
                                    Toast.makeText(getApplicationContext(), "This time is already Booked", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        back_btn.setOnClickListener(this);
        proceed_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.take_appointment_page_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.take_appointment_page_proceed_btn_id) {
            if (selected_date.getText().equals("") || selected_time.getText().equals("")) {
                Toast.makeText(getApplicationContext(), "Select Date and Time", Toast.LENGTH_LONG).show();
            } else {
                Map<String, String> appointment_details = new HashMap<>();
                final String time = new Date().getTime() + "";
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                String strDate = formatter.format(date);
                appointment_details.put("clients_phone", user_phone);
                appointment_details.put("professionals_phone", phone_of_professional);
                appointment_details.put("appointment_time", slctd_time);
                appointment_details.put("appointment_date", appointment_date);
                appointment_details.put("booking_time", time);
                appointment_details.put("booking_date", strDate);

                db.collection("appointments")
                        .document(phone_of_professional)
                        .collection("appointments")
                        .add(appointment_details)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                db.collection("appointments")
                                        .document(user_phone)
                                        .collection("appointments")
                                        .add(appointment_details)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(getApplicationContext(), " Your Appointment Completed", Toast.LENGTH_LONG).show();
                                                notification();
                                            }
                                        });
                            }
                        });
                finish();
            }
        }
    }

    //Notify function
    void notification() {
        Map<String, String> notification_item = new HashMap<>();
        notification_item.put("commenter_liker_appointer_phone", user_phone);
        notification_item.put("poster_phone", phone_of_professional);
        notification_item.put("time", new Date().getTime() + "");
        notification_item.put("type", "appointment");
        notification_item.put("clicked", "no");
        notification_item.put("total_notifications", "1");
        db.collection("notifications").add(notification_item);

    }

    public void show_date_func(int year, int month, int dayofmonth) {
        dbdays.clear();
        new_year = year;
        m = month - 1;
        if (month < 2) {
            m++;
            new_year--;
        }
        y = 5 * (new_year % 4) + 4 * (new_year % 100) + 6 * (new_year % 400);
        day = (dayofmonth + (int) (2.6 * m - 0.2) + y) % 7;
        if (month == 1) {
            day++;
        }

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            dbdays.add(document.getString("sun"));
                            dbdays.add(document.getString("mon"));
                            dbdays.add(document.getString("tue"));
                            dbdays.add(document.getString("wed"));
                            dbdays.add(document.getString("thu"));
                            dbdays.add(document.getString("fri"));
                            dbdays.add(document.getString("sat"));

                            start_time = document.getString("starting_time");
                            end_time = document.getString("ending_time");
                            first_break = document.getString("break_1");
                            second_break = document.getString("break_2");

                            fbreak_duration = Integer.parseInt(document.getString("break_1_duration"));
                            sbreak_duration = Integer.parseInt(document.getString("break_2_duration"));
                            apnmnt_duration = Integer.parseInt(document.getString("appointment_duration"));
                        }
                        if (dbdays.get(day).equals("true")) {
                            Toast.makeText(getApplicationContext(), " Off day", Toast.LENGTH_SHORT).show();
                            selected_date.setText("");
                            selected_time.setText("");
                            LinearLayout.LayoutParams lout_empty = new LinearLayout.LayoutParams(0, 0);
                            time_layout.setLayoutParams(lout_empty);

                        } else {
                            int layout_width = LinearLayout.LayoutParams.MATCH_PARENT;
                            int height = 800;
                            LinearLayout.LayoutParams lout_not_empty = new LinearLayout.LayoutParams(layout_width, height);
                            time_layout.setLayoutParams(lout_not_empty);

                            String[] stm_stry = start_time.split(":");
                            String[] stm2_stry = stm_stry[1].split(" ");
                            String[] etm_stry = end_time.split(":");
                            String[] etm2_stry = etm_stry[1].split(" ");
                            String[] fbtm_stry = first_break.split(":");
                            String[] fbtm2_stry = fbtm_stry[1].split(" ");
                            String[] sbtm_stry = second_break.split(":");
                            String[] sbtm2_stry = sbtm_stry[1].split(" ");
                            int stm_hr, stm_min, etm_hr, etm_min, fbtm_hr, fbtm_min, sbtm_hr, sbtm_min, hr_len = 0, new_shr, new_min, lp;
                            stm_hr = Integer.parseInt(stm_stry[0]);
                            stm_min = Integer.parseInt(stm2_stry[0]);
                            etm_hr = Integer.parseInt(etm_stry[0]);
                            etm_min = Integer.parseInt(etm2_stry[0]);
                            fbtm_hr = Integer.parseInt(fbtm_stry[0]);
                            fbtm_min = Integer.parseInt(fbtm2_stry[0]);
                            sbtm_hr = Integer.parseInt(sbtm_stry[0]);
                            sbtm_min = Integer.parseInt(sbtm2_stry[0]);

                            if (stm_hr < etm_hr) hr_len = etm_hr - stm_hr;
                            if (stm_hr > etm_hr) hr_len = (12 - stm_hr) + etm_hr;
                            lp = (hr_len * 60) / apnmnt_duration;
                            new_shr = stm_hr;
                            new_min = stm_min;
                            ampm=stm2_stry[1];
                            time_slots.clear();
                            time_slots.add(stm_hr + ":" + stm_min + " " + stm2_stry[1]);
                            for (int i = 1; i < lp; i++) {
                                new_min = new_min + apnmnt_duration;
                                if (new_min >= 60) {
                                    new_shr = new_shr + 1;
                                    new_min = new_min - 60;
                                }
                                if (new_shr > 12) {
                                    new_shr = new_shr - 12;
                                    ampm = "pm";
                                }
                                if (new_shr == fbtm_hr && new_min >= fbtm_min && new_min < fbtm_min + fbreak_duration && ampm.equals(fbtm2_stry[1])) {
                                    continue;
                                }
                                if (new_shr == sbtm_hr && new_min >= sbtm_min && new_min < sbtm_min + sbreak_duration && ampm.equals(sbtm2_stry[1])) {
                                    continue;
                                }
                                time_slots.add(new_shr + ":" + new_min + " " + ampm);
                            }
                            d_0_0_15_Time_gridAdapter grid_obj = new d_0_0_15_Time_gridAdapter(time_slots, phone_of_professional, appointment_date, getApplicationContext());
                            gridView.setAdapter(grid_obj);

                        }
                    }
                });
    }

}