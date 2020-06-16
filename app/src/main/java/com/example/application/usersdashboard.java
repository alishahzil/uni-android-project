package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class usersdashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersdashboard);

        Intent myIntent = getIntent();
        String workerid = myIntent.getStringExtra("workerid");
        Toast.makeText(getApplicationContext(),workerid,Toast.LENGTH_LONG).show();


    }
}