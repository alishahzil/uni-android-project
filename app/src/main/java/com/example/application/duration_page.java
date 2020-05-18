package com.example.application;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class duration_page extends AppCompatActivity {

    Handler handler;
    Runnable run;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration_page);

        img=findViewById(R.id.main_icon);
        img.animate().alpha(3000).setDuration(0);

        handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent it =new Intent(duration_page.this,  Choice_uw.class);
                startActivity(it);
                finish();
            }
        } , 4000);
    }
}