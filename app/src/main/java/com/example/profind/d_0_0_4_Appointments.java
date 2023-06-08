package com.example.profind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class d_0_0_4_Appointments extends AppCompatActivity implements View.OnClickListener {
    LinearLayout make_schedule, available_time, asapro_appointments, asaclient_appointments, previous_appointments;
    Button back_btn;
    Intent intent;
    String phone;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_0_0_4_appointments);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        back_btn = findViewById(R.id.appointments_page_back_btn_id);
        make_schedule = findViewById(R.id.appointment_page_make_schedule_layout_id);
        available_time = findViewById(R.id.appointment_page_available_time_layout_id);
        asapro_appointments = findViewById(R.id.appointment_page_as_a_pro_layout_id);
        asaclient_appointments = findViewById(R.id.appointment_page_as_a_client_layout_id);
        previous_appointments = findViewById(R.id.appointment_page_prev_appointment_layout_id);

        back_btn.setOnClickListener(this);
        make_schedule.setOnClickListener(this);
        available_time.setOnClickListener(this);
        asapro_appointments.setOnClickListener(this);
        asaclient_appointments.setOnClickListener(this);
        previous_appointments.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.appointments_page_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.appointment_page_make_schedule_layout_id) {
            intent = new Intent(getApplicationContext(), d_0_0_4_1_make_schedule.class);
            startActivity(intent);
        } else if (view.getId() == R.id.appointment_page_available_time_layout_id) {
            db.collection("appointment_schedule")
                    .whereEqualTo("phone", phone)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "You have not made any schedule yet !", Toast.LENGTH_SHORT).show();
                            } else {
                                intent = new Intent(getApplicationContext(), d_0_0_4_2_available_time.class);
                                startActivity(intent);
                            }
                        }

                    });

        } else if (view.getId() == R.id.appointment_page_as_a_pro_layout_id) {
            intent = new Intent(getApplicationContext(), d_0_0_4_3_asapro.class);
            startActivity(intent);
        } else if (view.getId() == R.id.appointment_page_as_a_client_layout_id) {
            intent = new Intent(getApplicationContext(), d_0_0_4_4_asaclient.class);
            startActivity(intent);
        } else if (view.getId() == R.id.appointment_page_prev_appointment_layout_id) {
            intent = new Intent(getApplicationContext(), d_0_0_4_5_previous_appointment.class);
            startActivity(intent);
        }
    }
}