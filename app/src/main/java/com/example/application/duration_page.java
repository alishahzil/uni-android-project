package com.example.application;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class duration_page extends AppCompatActivity {

    Handler handler;
    Runnable run;
    ImageView img;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration_page);

        img=findViewById(R.id.main_icon);
        img.animate().alpha(3000).setDuration(0);
        mAuth = FirebaseAuth.getInstance();

        handler= new Handler();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser !=null){
                    Intent it =new Intent(duration_page.this,  MainActivity.class);
                    startActivity(it);
                    finish();

                }else{
                    Intent it =new Intent(duration_page.this,  Choice_uw.class);
                    startActivity(it);
                    finish();
                }
            }
        } , 2000);
    }
}