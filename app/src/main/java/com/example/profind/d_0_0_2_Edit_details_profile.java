package com.example.profind;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class d_0_0_2_Edit_details_profile extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout name_edttxt, address_txtlayout;
    TextInputEditText adress_edttxt;
    Button save_btn, back_btn;
    String phone;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_0_0_2_edit_details_profile);
        getSupportActionBar().hide();

        b_0_1_Session session = new b_0_1_Session(this);
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        back_btn = findViewById(R.id.edt_details_page_back_btn_id);
        name_edttxt = findViewById(R.id.edit_details_username_edtxt_id);
        address_txtlayout = findViewById(R.id.edit_details_address_txtlayout_id);
        adress_edttxt = findViewById(R.id.edit_details_address_edtxt_id);
        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("Phone_Number", phone)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String nam = document.getString("Name");
                            String adrs = document.getString("Address");

                            name_edttxt.getEditText().setText(nam);
                            address_txtlayout.getEditText().setText(adrs);
                        }
                    }
                });

        //Places.initialize(getApplicationContext(),"AIzaSyDNZtpkGH8elmneD-2w8yRO8S9F8gNLkas");
        save_btn = findViewById(R.id.edit_details_save_btn_id);
        //adress_edttxt.setFocusable(false);
        //adress_edttxt.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edt_details_page_back_btn_id) {
            finish();
        }
        else if (view.getId() == R.id.edit_details_address_edtxt_id) {
            /*
            List<Place.Field> fieldList= Arrays.asList(Place.Field.ADDRESS
            ,Place.Field.LAT_LNG,Place.Field.NAME);
            Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
            ,fieldList).build(this);
            startActivityForResult(intent,100);
            */

        } else if (view.getId() == R.id.edit_details_save_btn_id) {
            if (!validate_name()) {
                return;
            }

            String given_name = name_edttxt.getEditText().getText().toString().trim();
            String given_address = address_txtlayout.getEditText().getText().toString().trim();
            db.collection("users").document(phone).update("Name", given_name);
            db.collection("users").document(phone).update("Name_lower", given_name.toLowerCase());
            db.collection("users").document(phone).update("Address", given_address);
            Toast.makeText(getApplicationContext(), "Successfully edited", Toast.LENGTH_SHORT).show();
            b_0_1_Session session = new b_0_1_Session(this);
            session.createLoginSession(given_name, phone);
        }

    }

    private boolean validate_name() {
        String val = name_edttxt.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            name_edttxt.setError("Field can not be empty");
            return false;
        } else if (val.length() < 4) {
            name_edttxt.setError("Must be more then 3 character");
            return false;
        } else if (!(Character.isUpperCase(val.charAt(0)))) {
            name_edttxt.setError("First latter must be upper case");
            return false;
        } else {
            name_edttxt.setError(null);
            return true;
        }
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK){
            Place place=Autocomplete.getPlaceFromIntent(data);
            address_txtlayout.getEditText().setText(place.getAddress());
        }else if(resultCode== AutocompleteActivity.RESULT_ERROR){
            Status status=Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(),Toast.LENGTH_SHORT).show();
        }
    }

     */
}