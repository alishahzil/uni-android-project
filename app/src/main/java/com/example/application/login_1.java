package com.example.application;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class login_1 extends AppCompatActivity {

    TextView txt_s;
    Button btn_login1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_1);

        txt_s=findViewById(R.id.sign_t);
        btn_login1=findViewById(R.id.login_button);

        txt_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r=new Intent(login_1.this,signup_1.class);
                startActivity(r);
            }
        });
        btn_login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( login_1.this, MapsActivityuser.class);
                startActivity(intent);
            }
        });

    }
}