package com.example.uverifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class FirstSignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_sign);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext() , CapchaActivity.class);
                intent.putExtra("reason" , "ekyc");
                intent.putExtra("msg" , "Getting EKYC Offline ,STEP: 1/2");
                startActivity(intent);
                finish();
            }
        }, 3000);


    }
}