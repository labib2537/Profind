package com.example.profind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class d_0_0_4_2_available_time extends AppCompatActivity {

    LinearLayout time_layout;
    String phone,date, start_time, end_time, first_break, second_break,ampm="am";
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
        setContentView(R.layout.activity_d_0_0_4_2_available_time);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        query = db.collection("appointment_schedule").whereEqualTo("phone", phone);

        time_layout= findViewById(R.id.available_time_page_time_layout_id);
        calendarView = findViewById(R.id.available_time_page_calender_id);
        gridView = findViewById(R.id.available_time_page_time_grdvw_id);
        calendarView.setMinDate(new Date().getTime());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayofmonth) {

                date= dayofmonth + "-" + month + "-" + year;
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
                                    LinearLayout.LayoutParams lout_empty = new LinearLayout.LayoutParams(0, 0);
                                    time_layout.setLayoutParams(lout_empty);

                                } else {
                                    int layout_width = LinearLayout.LayoutParams.MATCH_PARENT;
                                    int height = LinearLayout.LayoutParams.MATCH_PARENT;;
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
                                    int stm_hr, stm_min, etm_hr, etm_min, fbtm_hr, fbtm_min, sbtm_hr, sbtm_min, hr_len = 0,new_shr,new_min,lp;
                                    stm_hr = Integer.parseInt(stm_stry[0]);
                                    stm_min = Integer.parseInt(stm2_stry[0]);
                                    etm_hr = Integer.parseInt(etm_stry[0]);
                                    etm_min = Integer.parseInt(etm2_stry[0]);
                                    fbtm_hr = Integer.parseInt(fbtm_stry[0]);
                                    fbtm_min = Integer.parseInt(fbtm2_stry[0]);
                                    sbtm_hr = Integer.parseInt(sbtm_stry[0]);
                                    sbtm_min = Integer.parseInt(sbtm2_stry[0]);

                                    if (stm_hr < etm_hr) hr_len = etm_hr - stm_hr;
                                    if (stm_hr > etm_hr) hr_len = (12-stm_hr)+ etm_hr;
                                    lp = (hr_len*60)/ apnmnt_duration;
                                    new_shr=stm_hr;
                                    new_min=stm_min;
                                    time_slots.clear();
                                    time_slots.add(stm_hr+":"+stm_min+" "+stm2_stry[1]);
                                    for(int i=1;i<lp;i++){
                                        new_min=new_min+apnmnt_duration;
                                        if(new_min>=60){
                                            new_shr=new_shr+1;
                                            new_min=new_min-60;
                                        }
                                        if(new_shr>12){
                                            new_shr=new_shr-12;
                                            ampm="pm";
                                        }
                                        if(new_shr==fbtm_hr && new_min>=fbtm_min && new_min<fbtm_min+fbreak_duration && ampm.equals(fbtm2_stry[1])){
                                            continue;
                                        }
                                        if(new_shr==sbtm_hr && new_min>=sbtm_min && new_min<sbtm_min+sbreak_duration && ampm.equals(sbtm2_stry[1])){
                                            continue;
                                        }
                                        time_slots.add(new_shr+":"+new_min+" "+ampm);
                                    }
                                    d_0_0_15_Time_gridAdapter grid_obj = new d_0_0_15_Time_gridAdapter(time_slots,phone,date, getApplicationContext());
                                    gridView.setAdapter(grid_obj);
                                }
                            }
                        });
            }
        });
    }
}