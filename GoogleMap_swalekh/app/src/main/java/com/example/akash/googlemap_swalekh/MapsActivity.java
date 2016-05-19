package com.example.akash.googlemap_swalekh;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements LocationListener {

    private static final String GOOGLE_API_KEY = "AIzaSyAjaZ2SCK_ftpVPsGSCKHd1h0jeafJceL8";
    GoogleMap googleMap;
    ArrayAdapter<String> listAdapter;
    EditText placeText;
    double latitude = 0;
    double longitude = 0;
    private int PROXIMITY_RADIUS = 50000;
    ListView listView;

receiver revInst;
    private BroadcastReceiver displayPlaceBroadcastReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

            revInst=(receiver)getApplication();
        registerDisplayBroadcast();

        placeText = (EditText) findViewById(R.id.placeText);
        Button btnFind = (Button) findViewById(R.id.btnFind);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        googleMap = fragment.getMap();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // mapFragment.getMapAsync(this);
        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

      if(location == null)
      {
          location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
      }

//        if (location != null) {
            onLocationChanged(location);
//        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

        btnFind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                String type = placeText.getText().toString();
                Log.d("o", "" + type);

                StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
                googlePlacesUrl.append("location=" + latitude + "," + longitude);
                Log.d("o", "" + type + latitude + longitude);
                googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlacesUrl.append("&query=" + type);

                googlePlacesUrl.append("&key=" + "AIzaSyAjaZ2SCK_ftpVPsGSCKHd1h0jeafJceL8");
                googlePlacesUrl.append("&sensor=true");
                GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
                Object[] toPass = new Object[3];
                toPass[0] = googleMap;
                toPass[1] = googlePlacesUrl.toString();
                toPass[2] = MapsActivity.this;
                Log.d("o", "" + googlePlacesUrl.toString());
                googlePlacesReadTask.execute(toPass);

            }
        });

    }
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }


    public void registerDisplayBroadcast() {
        displayPlaceBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                    listView=(ListView) findViewById(R.id.mobile_list);
                if(intent != null && intent.hasExtra("result")) {
                    boolean value = intent.getExtras().getBoolean("result");
                    if(value){
                        final ArrayList<String> list = new ArrayList<String>();
                                list.addAll(revInst.getYo());
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MapsActivity.this,android.R.layout.simple_list_item_1,list);
                        listView.setAdapter(arrayAdapter);
                        //get value from application
                    }
                    else{

                    }
                }

            }
        };

        IntentFilter intentFilter = new IntentFilter("com.map.action");
        MapsActivity.this.registerReceiver(displayPlaceBroadcastReceiver, intentFilter);
    }




    @Override
    public void onLocationChanged(Location location) {
        if(location!= null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }





    public void  a1(String[] va){
        final ListView listview = (ListView) findViewById(R.id.mobile_list);

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < va.length; ++i) {
            list.add(va[i]);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if(displayPlaceBroadcastReceiver != null) {
            MapsActivity.this.unregisterReceiver(displayPlaceBroadcastReceiver);
        }
    }

}
