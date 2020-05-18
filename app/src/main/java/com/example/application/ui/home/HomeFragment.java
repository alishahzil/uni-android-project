package com.example.application.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.application.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        View electrition = root.findViewById(R.id.electrition_img);
        View work = root.findViewById(R.id.works_img);
        View paint = root.findViewById(R.id.paint_img);
        View carpanter = root.findViewById(R.id.carpenter_img);
        View plumber = root.findViewById(R.id.plumber_img);
        View services = root.findViewById(R.id.service_img);

        electrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent u=new Intent(Choice_uw.this,login_1.class);
                //  startActivity(u);
                Toast.makeText(getContext(),"Electriction",Toast.LENGTH_LONG).show();
            }
        });
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent u=new Intent(Choice_uw.this,login_1.class);
                //  startActivity(u);
                Toast.makeText(getContext(),"work",Toast.LENGTH_LONG).show();
            }
        });
        plumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent u=new Intent(Choice_uw.this,login_1.class);
                //  startActivity(u);
                Toast.makeText(getContext(),"plumber",Toast.LENGTH_LONG).show();
            }
        });
        paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent u=new Intent(Choice_uw.this,login_1.class);
                //  startActivity(u);
                Toast.makeText(getContext(),"paint",Toast.LENGTH_LONG).show();
            }
        });
        carpanter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent u=new Intent(Choice_uw.this,login_1.class);
                //  startActivity(u);
                Toast.makeText(getContext(),"carpenter",Toast.LENGTH_LONG).show();
            }
        });
        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent u=new Intent(Choice_uw.this,login_1.class);
                //  startActivity(u);
                Toast.makeText(getContext(),"services",Toast.LENGTH_LONG).show();
            }
        });


        return root;
    }




}
