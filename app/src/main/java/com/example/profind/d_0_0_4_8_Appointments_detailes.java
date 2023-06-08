package com.example.profind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class d_0_0_4_8_Appointments_detailes extends AppCompatActivity implements View.OnClickListener {

    Button back_btn;
    String appointment_id, professionals_phone, clients_phone,phone;
    LinearLayout client_id, pro_id;
    ImageView client_img, pro_img, profession_img;
    TextView client_name, pro_name, taken_time, apnmnt_time, profession_name, rating_txt, client_phone, pros_phone,time_remain;
    RatingBar ratingBar;
    Intent intent;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_0_0_4_8_appointments_detailes);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        appointment_id = getIntent().getStringExtra("appointment_id");
        professionals_phone=getIntent().getStringExtra("professionals_phone");
        clients_phone=getIntent().getStringExtra("client_phone");

        back_btn = findViewById(R.id.apnmnt_dtls_page_back_btn_id);
        client_img = findViewById(R.id.apnmnt_dtls_page_client_profile_img_id);
        pro_img = findViewById(R.id.apnmnt_dtls_page_pro_profile_img_id);
        profession_img = findViewById(R.id.apnmnt_dtls_page_profession_imgvw_id);

        client_id = findViewById(R.id.apnmnt_dtls_page_client_id_lout_id);
        pro_id = findViewById(R.id.apnmnt_dtls_page_professional_lout_id);

        client_name = findViewById(R.id.apnmnt_dtls_page_client_name_id);
        pro_name = findViewById(R.id.apnmnt_dtls_page_pro_name_id);
        taken_time = findViewById(R.id.apnmnt_dtls_page_appointment_timetaken_id);
        apnmnt_time = findViewById(R.id.apnmnt_dtls_page_willbe_time_id);
        profession_name = findViewById(R.id.apnmnt_dtls_page_profession_name_id);
        rating_txt = findViewById(R.id.apnmnt_dtls_page_pro_ratingtxt_id);
        client_phone = findViewById(R.id.apnmnt_dtls_page_clients_phone_id);
        pros_phone = findViewById(R.id.apnmnt_dtls_page_pros_phone_id);
        time_remain= findViewById(R.id.apnmnt_dtls_page_time_remaining_id);
        ratingBar = findViewById(R.id.apnmnt_dtls_page_pro_ratingbar_id);

        db.collection("appointments")
                .document(professionals_phone)
                .collection("appointments")
                .document(appointment_id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                db.collection("users")
                        .whereEqualTo("Phone_Number", documentSnapshot.getString("clients_phone"))
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    Picasso.get()
                                            .load(document.getString("Profile_Photo"))
                                            .placeholder(R.drawable.profile_img)
                                            .into(client_img);
                                    client_name.setText(document.getString("Name"));

                                }
                            }
                        });
                db.collection("users")
                        .whereEqualTo("Phone_Number", documentSnapshot.getString("professionals_phone"))
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    Picasso.get()
                                            .load(document.getString("Profile_Photo"))
                                            .placeholder(R.drawable.profile_img)
                                            .into(pro_img);
                                    pro_name.setText(document.getString("Name"));
                                    String profession=document.getString("Profession");
                                    profession_name.setText(profession);
                                    if (profession.equals("Builder")) {
                                        profession_img.setImageResource(R.drawable.builder_icon);
                                    } else if (profession.equals("Doctor")) {
                                        profession_img.setImageResource(R.drawable.doctor_icon);
                                    } else if (profession.equals("Lawyer")) {
                                        profession_img.setImageResource(R.drawable.lawyer_icon);
                                    } else if (profession.equals("Teacher")) {
                                        profession_img.setImageResource(R.drawable.teacher_icon);
                                    }
                                    String rating=document.getString("Rating");
                                    rating_txt.setText(rating + "/5");
                                    ratingBar.setRating(Float.parseFloat(rating));

                                }
                            }
                        });
                taken_time.setText(documentSnapshot.getString("booking_date"));
                client_phone.setText("Solaymans Phone : "+documentSnapshot.getString("clients_phone"));
                pros_phone.setText("Nakibs Phone : "+documentSnapshot.getString("professionals_phone"));
                apnmnt_time.setText("Appointment will be : "+documentSnapshot.getString("appointment_date")+" | "+documentSnapshot.getString("appointment_time"));

                try {
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
                    String strDate = formatter.format(date);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy", Locale.ENGLISH);
                    Date firstDate = sdf.parse(strDate);
                    Date secondDate = sdf.parse(documentSnapshot.getString("appointment_date"));

                    long diff = secondDate.getTime() - firstDate.getTime();

                    TimeUnit time = TimeUnit.DAYS;
                    long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                    time_remain.setText(diffrence+" days");

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        back_btn.setOnClickListener(this);
        client_id.setOnClickListener(this);
        pro_id.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.apnmnt_dtls_page_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.apnmnt_dtls_page_client_id_lout_id) {
            if (clients_phone.equals(phone)) {
                Intent intent = new Intent(view.getContext(), d_0_0_Profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            } else {
                intent = new Intent(view.getContext(), d_0_0_Profile_view.class);
                intent.putExtra("profile_id_key", clients_phone);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        } else if (view.getId() == R.id.apnmnt_dtls_page_professional_lout_id) {
            if (professionals_phone.equals(phone)) {
                Intent intent = new Intent(view.getContext(), d_0_0_Profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            } else {
                intent = new Intent(view.getContext(), d_0_0_Profile_view.class);
                intent.putExtra("profile_id_key", professionals_phone);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        }
    }
}