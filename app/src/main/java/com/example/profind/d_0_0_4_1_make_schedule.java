package com.example.profind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class d_0_0_4_1_make_schedule extends AppCompatActivity implements View.OnClickListener {
    Button back_btn, save_btn, sat_btn, sun_btn, mon_btn, tue_btn, wed_btn, thus_btn, fri_btn;
    TextView starting_time, end_time, first_break, second_break;
    NumberPicker fbreak_duration, sbreak_duration, apnmt_duration;
    String strt, end, brk1, brk2, phone;
    int t1Hour, t1Minute, brkdr1, brkdr2, duration_apntmnt, dbbrkdr1, dbbrkdr2, dbapntmntdr;
    Boolean clcd1 = false, clcd2 = false, clcd3 = false, clcd4 = false, clcd5 = false, clcd6 = false, clcd7 = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_0_0_4_1_make_schedule);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        back_btn = findViewById(R.id.make_schedule_page_back_btn_id);
        save_btn = findViewById(R.id.make_schedule_page_save_changes_btn_id);
        sat_btn = findViewById(R.id.make_schedule_page_satday_id);
        sun_btn = findViewById(R.id.make_schedule_page_sunday_id);
        mon_btn = findViewById(R.id.make_schedule_page_monday_id);
        tue_btn = findViewById(R.id.make_schedule_page_tueday_id);
        wed_btn = findViewById(R.id.make_schedule_page_wedday_id);
        thus_btn = findViewById(R.id.make_schedule_page_thuday_id);
        fri_btn = findViewById(R.id.make_schedule_page_friday_id);

        starting_time = findViewById(R.id.make_schedule_page_starting_time_id);
        end_time = findViewById(R.id.make_schedule_page_end_time_id);
        first_break = findViewById(R.id.make_schedule_page_1st_break_time_id);
        fbreak_duration = findViewById(R.id.make_schedule_page_1stbreak_duration_id);
        second_break = findViewById(R.id.make_schedule_page_2nd_break_time_id);
        sbreak_duration = findViewById(R.id.make_schedule_page_2ndbreak_duration_id);
        apnmt_duration = findViewById(R.id.make_schedule_page_appointment_duration_id);

        fbreak_duration.setMinValue(0);
        fbreak_duration.setMaxValue(120);
        sbreak_duration.setMinValue(0);
        sbreak_duration.setMaxValue(120);
        apnmt_duration.setMinValue(0);
        apnmt_duration.setMaxValue(120);

        try {
            db.collection("appointment_schedule")
                    .whereEqualTo("phone", phone)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                starting_time.setText(document.getString("starting_time"));
                                end_time.setText(document.getString("ending_time"));
                                first_break.setText(document.getString("break_1"));
                                second_break.setText(document.getString("break_2"));
                                dbbrkdr1 = Integer.parseInt(document.getString("break_1_duration"));
                                dbbrkdr2 = Integer.parseInt(document.getString("break_2_duration"));
                                dbapntmntdr = Integer.parseInt(document.getString("appointment_duration"));
                                fbreak_duration.setValue(dbbrkdr1);
                                sbreak_duration.setValue(dbbrkdr2);
                                apnmt_duration.setValue(dbapntmntdr);

                                if (document.getString("sat").equals("true")) {
                                    sat_btn.setBackgroundResource(R.drawable.custom_button_7);
                                    clcd1 = true;
                                }
                                if (document.getString("sun").equals("true")) {
                                    sun_btn.setBackgroundResource(R.drawable.custom_button_7);
                                    clcd2 = true;
                                }
                                if (document.getString("mon").equals("true")) {
                                    mon_btn.setBackgroundResource(R.drawable.custom_button_7);
                                    clcd3 = true;
                                }
                                if (document.getString("tue").equals("true")) {
                                    tue_btn.setBackgroundResource(R.drawable.custom_button_7);
                                    clcd4 = true;
                                }
                                if (document.getString("wed").equals("true")) {
                                    wed_btn.setBackgroundResource(R.drawable.custom_button_7);
                                    clcd5 = true;
                                }
                                if (document.getString("thu").equals("true")) {
                                    thus_btn.setBackgroundResource(R.drawable.custom_button_7);
                                    clcd6 = true;
                                }
                                if (document.getString("fri").equals("true")) {
                                    fri_btn.setBackgroundResource(R.drawable.custom_button_7);
                                    clcd7 = true;
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, e + "", Toast.LENGTH_SHORT).show();
        }

        fbreak_duration.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                brkdr1 = i1;
            }
        });
        sbreak_duration.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                brkdr2 = i1;
            }
        });
        apnmt_duration.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                duration_apntmnt = i1;
            }
        });


        back_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        sat_btn.setOnClickListener(this);
        sun_btn.setOnClickListener(this);
        mon_btn.setOnClickListener(this);
        tue_btn.setOnClickListener(this);
        wed_btn.setOnClickListener(this);
        thus_btn.setOnClickListener(this);
        fri_btn.setOnClickListener(this);

        starting_time.setOnClickListener(this);
        end_time.setOnClickListener(this);
        first_break.setOnClickListener(this);
        fbreak_duration.setOnClickListener(this);
        second_break.setOnClickListener(this);
        sbreak_duration.setOnClickListener(this);
        apnmt_duration.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.make_schedule_page_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.make_schedule_page_starting_time_id) {
            time_picker(starting_time);
        } else if (view.getId() == R.id.make_schedule_page_end_time_id) {
            time_picker(end_time);
        } else if (view.getId() == R.id.make_schedule_page_1st_break_time_id) {
            time_picker(first_break);
        } else if (view.getId() == R.id.make_schedule_page_2nd_break_time_id) {
            time_picker(second_break);
        } else if (view.getId() == R.id.make_schedule_page_satday_id) {
            if (clcd1) {
                sat_btn.setBackgroundResource(R.drawable.custom_button_4);
                clcd1 = false;
            } else {
                sat_btn.setBackgroundResource(R.drawable.custom_button_7);
                clcd1 = true;
            }
        } else if (view.getId() == R.id.make_schedule_page_sunday_id) {
            if (clcd2) {
                sun_btn.setBackgroundResource(R.drawable.custom_button_4);
                clcd2 = false;
            } else {
                sun_btn.setBackgroundResource(R.drawable.custom_button_7);
                clcd2 = true;
            }
        } else if (view.getId() == R.id.make_schedule_page_monday_id) {
            if (clcd3) {
                mon_btn.setBackgroundResource(R.drawable.custom_button_4);
                clcd3 = false;
            } else {
                mon_btn.setBackgroundResource(R.drawable.custom_button_7);
                clcd3 = true;
            }
        } else if (view.getId() == R.id.make_schedule_page_tueday_id) {
            if (clcd4) {
                tue_btn.setBackgroundResource(R.drawable.custom_button_4);
                clcd4 = false;
            } else {
                tue_btn.setBackgroundResource(R.drawable.custom_button_7);
                clcd4 = true;
            }
        } else if (view.getId() == R.id.make_schedule_page_wedday_id) {
            if (clcd5) {
                wed_btn.setBackgroundResource(R.drawable.custom_button_4);
                clcd5 = false;
            } else {
                wed_btn.setBackgroundResource(R.drawable.custom_button_7);
                clcd5 = true;
            }
        } else if (view.getId() == R.id.make_schedule_page_thuday_id) {
            if (clcd6) {
                thus_btn.setBackgroundResource(R.drawable.custom_button_4);
                clcd6 = false;
            } else {
                thus_btn.setBackgroundResource(R.drawable.custom_button_7);
                clcd6 = true;
            }
        } else if (view.getId() == R.id.make_schedule_page_friday_id) {
            if (clcd7) {
                fri_btn.setBackgroundResource(R.drawable.custom_button_4);
                clcd7 = false;
            } else {
                fri_btn.setBackgroundResource(R.drawable.custom_button_7);
                clcd7 = true;
            }
        } else if (view.getId() == R.id.make_schedule_page_save_changes_btn_id) {
            Map<String, String> appointment_schedule = new HashMap<>();

            strt = starting_time.getText().toString();
            end = end_time.getText().toString();
            brk1 = first_break.getText().toString();
            brk2 = second_break.getText().toString();

            if (strt.equals("") || end.equals("") || duration_apntmnt < 10) {
                if (duration_apntmnt < 10) {
                    Toast.makeText(getApplicationContext(), "Appointment duration cant be less then 10 min", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "'*' Fields can't be empty", Toast.LENGTH_LONG).show();
                }
            } else {
                appointment_schedule.put("phone", phone);
                if (brk1.equals("")) {
                    appointment_schedule.put("break_1", "00:00 pm");
                    appointment_schedule.put("break_1_duration", "0");
                } else {
                    appointment_schedule.put("break_1", brk1);
                    appointment_schedule.put("break_1_duration", fbreak_duration.getValue() + "");
                }
                appointment_schedule.put("starting_time", strt);
                appointment_schedule.put("ending_time", end);
                if (brk2.equals("")) {
                    appointment_schedule.put("break_2", "00:00 pm");
                    appointment_schedule.put("break_2_duration", "0");
                } else {
                    appointment_schedule.put("break_2", brk2);
                    appointment_schedule.put("break_2_duration", sbreak_duration.getValue() + "");
                }
                appointment_schedule.put("appointment_duration",apnmt_duration.getValue() + "");
                appointment_schedule.put("sat", clcd1 + "");
                appointment_schedule.put("sun", clcd2 + "");
                appointment_schedule.put("mon", clcd3 + "");
                appointment_schedule.put("tue", clcd4 + "");
                appointment_schedule.put("wed", clcd5 + "");
                appointment_schedule.put("thu", clcd6 + "");
                appointment_schedule.put("fri", clcd7 + "");

                db.collection("appointment_schedule")
                        .document(phone)
                        .set(appointment_schedule)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Saved Successfully !", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }

    }

    public void time_picker(TextView tm) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                d_0_0_4_1_make_schedule.this,
                android.R.style.Theme_Holo_Dialog_MinWidth,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        t1Hour = i;
                        t1Minute = i1;
                        String time = t1Hour + ":" + t1Minute;
                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try {
                            Date date = f24Hours.parse(time);
                            SimpleDateFormat f12Hours = new SimpleDateFormat(
                                    "hh:mm aa"
                            );
                            tm.setText(f12Hours.format(date));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 12, 0, false);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.updateTime(t1Hour, t1Minute);
        timePickerDialog.show();
    }

}