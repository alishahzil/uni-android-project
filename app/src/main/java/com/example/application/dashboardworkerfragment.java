package com.example.application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class dashboardworkerfragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workerdashboard,container,false);

    }
}
/*public class Worker_map extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient client;
    Location locations;
    LocationRequest request;
    Button btn_out,btn_set;
    FirebaseAuth fireAuth;
    FirebaseUser currUser;
    Boolean currentLogStatus = false;
    DatabaseReference AssignedCus, AssignedCusPickRef;
    String workerID, customerID="";
    Marker PickupMarker;
    ValueEventListener AssCusPickRefListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_map);

        fireAuth = FirebaseAuth.getInstance();
        currUser = fireAuth.getCurrentUser();
        workerID = fireAuth.getCurrentUser().getUid();
        btn_out = findViewById(R.id.button7);
        btn_set= findViewById(R.id.button6);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent set = new Intent(Worker_map.this,settings.class);
                set.putExtra("type","Workers");
                startActivity(set);
            }
        });

        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentLogStatus = true;
                Disconnect();
                fireAuth.signOut();
                Logout_Driver();

            }
        });

        GetCustomerRequest();

    }

    private void GetCustomerRequest() {
        AssignedCus = FirebaseDatabase.getInstance().getReference().child("Users").child("Workers")
                .child(workerID).child("Customer's WorkerID");
        AssignedCus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    customerID = dataSnapshot.getValue().toString();

                    GetAssignedCusLoc();
                } else {
                    customerID = "";
                    if (PickupMarker != null) {

                        PickupMarker.remove();
                    }
                    if(AssCusPickRefListener != null){
                        AssignedCusPickRef.removeEventListener(AssCusPickRefListener);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GetAssignedCusLoc() {
        AssignedCusPickRef = FirebaseDatabase.getInstance().getReference()
                .child("Customer Request").child(customerID).child("l");
        AssCusPickRefListener = AssignedCusPickRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    List<Object> CustomerLocMap = (List<Object>) dataSnapshot.getValue();

                    double LocationLat = 0;
                    double LocationLng = 0;

                    if(CustomerLocMap.get(0) != null){
                        LocationLat = Double.parseDouble(CustomerLocMap.get(0).toString());
                    }
                    if(CustomerLocMap.get(1) != null){
                        LocationLng = Double.parseDouble(CustomerLocMap.get(1).toString());
                    }
                    LatLng WorkerLng = new LatLng(LocationLat,LocationLng);
                    PickupMarker = mMap.addMarker(new MarkerOptions().position(WorkerLng).title("Customer Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.here)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        buildAPIclient();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        buildAPIclient();
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest();
        request.setInterval(1000);
        request.setFastestInterval(1000);
        request.setPriority(request.PRIORITY_HIGH_ACCURACY);
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(getApplicationContext() != null){

            locations = location;
            LatLng latitude = new LatLng(location.getAltitude(),location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latitude));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference DriverAval = FirebaseDatabase.getInstance().getReference().child("Workers Avalable");
            GeoFire geofireAval = new GeoFire(DriverAval);
            DatabaseReference WorkerIngRef = FirebaseDatabase.getInstance().getReference().child("worker Working");
            GeoFire geofireIng = new GeoFire(WorkerIngRef);

            switch(customerID){
                case"":
                    geofireIng.removeLocation(userID);
                    geofireAval.setLocation(userID,new GeoLocation(location.getLatitude(),location.getLongitude()));
                    break;
                default:
                    geofireAval.removeLocation(userID);
                    geofireIng.setLocation(userID,new GeoLocation(location.getLatitude(),location.getLongitude()));
                    break;
            }
        }
    }
    protected synchronized void buildAPIclient(){
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!currentLogStatus){
            Disconnect();
        }
    }



    private void Disconnect() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference DriverAval = FirebaseDatabase.getInstance().getReference().child("Drivers Avalable");
        GeoFire geofire = new GeoFire(DriverAval);
        geofire.removeLocation(userID);
    }

    private void Logout_Driver() {
        Intent welcome = new Intent(Worker_map.this,Choice_uw.class);
        welcome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(welcome);
        finish();
    }

}*/
