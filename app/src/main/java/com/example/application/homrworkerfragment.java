package com.example.application;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;


public class homrworkerfragment extends Fragment {
    TextView status,phone,bill;
    Button call,location,coming,arrived,jobcompleted;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    Timer myTimer;
    String workerid;
    TimerTask doThis;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_workerhome, container, false);
        Intent intent = ((Activity) getContext()).getIntent();
        workerid = intent.getStringExtra("workerid");
        Log.w("workerid",workerid);
        status= root.findViewById(R.id.workerstatus);
         recyclerView = root.findViewById(R.id.recycle);
         phone=root.findViewById(R.id.phone);
         bill=root.findViewById(R.id.totalbill);
         call=root.findViewById(R.id.call);
         location=root.findViewById(R.id.location);
         coming=root.findViewById(R.id.coming);
         arrived=root.findViewById(R.id.arrived);
         jobcompleted=root.findViewById(R.id.jobcompleted);


        myTimer = new Timer();
        final int delay = 1000;
        final int period = 2000;
        final DocumentReference docRef = db.collection("worker").document(workerid);
        doThis = new TimerTask() {
            public void run() {
                Log.w("run","runing");

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String jobsid = (String) document.get("jobsid");
                                if (!jobsid.isEmpty()) {
                                    status.setText("job avaliable");
                                    getrequest(jobsid);
                                    jobaccepted(jobsid);


                                } else {
                                    status.setText("job not avaliable");
                                    recyclerView.removeAllViewsInLayout();
                                }

                            } else {
                                Log.w("doucment", "No such document");
                            }
                        } else {
                            Log.w("document", "get failed with ", task.getException());
                        }
                    }
                });
            };
        };myTimer.scheduleAtFixedRate(doThis, delay, period);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callphone = phone.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", callphone, null));
                startActivity(intent);

            }
        });
        coming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getjobsid(workerid);
                Toast.makeText(getContext(),"i am coming ",Toast.LENGTH_LONG).show();
                status.setText("Coming");
                arrived.setBackgroundResource(R.drawable.button);
                arrived.setEnabled(true);
                coming.setEnabled(false);
                coming.setBackgroundResource(R.drawable.invisiblebutton);
            }
        });
        arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getjobsid2(workerid);
                status.setText("Arrived");
                Toast.makeText(getContext(),"i am arrived",Toast.LENGTH_LONG).show();
                arrived.setBackgroundResource(R.drawable.invisiblebutton);
                arrived.setEnabled(false);
                jobcompleted.setBackgroundResource(R.drawable.button);
                jobcompleted.setEnabled(true);

            }
        });
        jobcompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getjobid3(workerid);
            }
        });





        return root;
    }
    void getrequest(final String jobid){
        final DocumentReference docRef = db.collection("jobs").document(jobid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String  userid = (String) document.get("userid");
                        getdatauser(userid,jobid);
                    } else {
                        Log.w("doucment", "No such document");
                    }
                } else {
                    Log.w("document", "get failed with ", task.getException());
                }
            }
        });
    }
    void getdatauser(String userid, final String jobsid){

        final DocumentReference docRef = db.collection("users").document(userid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String  name = (String) document.get("username");
                      //  String  location = (String) document.get("location");
                            String[] contacts={name};
                             String[] locations={"G15 islamabad"};
                            String[] job={jobsid};
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                          recyclerView.setAdapter(new requestadopter(contacts,locations,job));

                    } else {
                        Log.w("doucment", "No such document");
                    }
                } else {
                    Log.w("document", "get failed with ", task.getException());
                }
            }
        });
    }
    void jobaccepted(String jobid){
        final DocumentReference docRef = db.collection("jobs").document(jobid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String  accepted = (String) document.get("accepted");
                        String userid=(String)document.get("userid");
                        if(accepted.equals("true")){
                            doThis.cancel();
                            status.setText("you accept the job");
                            userphoneno(userid);
                            call.setBackgroundResource(R.drawable.button);
                            call.setEnabled(true);
                            location.setBackgroundResource(R.drawable.button);
                            location.setEnabled(true);
                            coming.setBackgroundResource(R.drawable.button);
                            coming.setEnabled(true);
                        }

                    } else {
                        Log.w("doucment", "No such document");
                    }
                } else {
                    Log.w("document", "get failed with ", task.getException());
                }
            }
        });

    }
    void userphoneno(String userid){
        final DocumentReference docRef = db.collection("users").document(userid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String  phones = (String) document.get("phone");
                        phone.setText(phones);
                    } else {
                        Log.w("doucment", "No such document");
                    }
                } else {
                    Log.w("document", "get failed with ", task.getException());
                }
            }
        });

    }
    void getjobsid(String workerid){

        final DocumentReference docRef = db.collection("worker").document(workerid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String  jobsids = (String) document.get("jobsid");
                        changecoming(jobsids);

                    } else {
                        Log.w("doucment", "No such document");
                    }
                } else {
                    Log.w("document", "get failed with ", task.getException());
                }
            }
        });

    }
    void changecoming(String jobsids){
         db.collection("jobs")
                        .document(jobsids)
                        .update(
                                "coming", "yes"
                        );
    }
    void getjobsid2(String workerid){
        final DocumentReference docRef = db.collection("worker").document(workerid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String  jobsids = (String) document.get("jobsid");
                        changearrived(jobsids);

                    } else {
                        Log.w("doucment", "No such document");
                    }
                } else {
                    Log.w("document", "get failed with ", task.getException());
                }
            }
        });

    }
    void changearrived(String jobsids){
        db.collection("jobs")
                .document(jobsids)
                .update(
                        "arrived", "yes"
                );

    }
    void getjobid3(String workerid){
        final DocumentReference docRef = db.collection("worker").document(workerid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String  jobsids = (String) document.get("jobsid");
                       getbill(jobsids);

                    } else {
                        Log.w("doucment", "No such document");
                    }
                } else {
                    Log.w("document", "get failed with ", task.getException());
                }
            }
        });


    }
    void getbill(String jobid){
        final DocumentReference docRef = db.collection("jobs").document(jobid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String  totalbill = (String) document.get("totalbill");
                        Log.w("totalbill",totalbill);
                        if(totalbill.equals("0")){
                            status.setText("waiting for user to confirm.... and then press the button");
                        }else{
                            status.setText("job completed");
                            bill.setText("Total bill = "+totalbill);
                        }
                    } else {
                        Log.w("doucment", "No such document");
                    }
                } else {
                    Log.w("document", "get failed with ", task.getException());
                }
            }
        });

    }

}
