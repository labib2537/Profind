package com.example.profind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class d_0_0_9_post_location_page extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout address_txtlayout;
    TextInputEditText adress_edttxt;
    Button set_btn,post_location_back_btn;
    String texts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Profind);
        setContentView(R.layout.activity_d_0_0_9_post_location_page);
        getSupportActionBar().hide();

        address_txtlayout = findViewById(R.id.post_address_txtlayout_id);
        adress_edttxt = findViewById(R.id.post_address_edtxt_id);
        set_btn = findViewById(R.id.post_set_btn_id);
        post_location_back_btn= findViewById(R.id.post_location_page_back_btn_id);
        post_location_back_btn.setOnClickListener(this);
        set_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.post_location_page_back_btn_id){
           finish();
        }
        else if(view.getId()==R.id.post_address_edtxt_id){
            /*
            List<Place.Field> fieldList= Arrays.asList(Place.Field.ADDRESS
            ,Place.Field.LAT_LNG,Place.Field.NAME);
            Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
            ,fieldList).build(this);
            startActivityForResult(intent,100);
            */

        }else if(view.getId()==R.id.post_set_btn_id){
            texts=adress_edttxt.getText().toString().trim();
            if(texts==null){
                texts="";
            }
            Intent intent=new Intent();
            intent.putExtra("place_key",texts);
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}