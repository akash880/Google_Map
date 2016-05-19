package com.example.akash.googlemap_swalekh;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

    JSONObject googlePlacesJson;
    Translation t;
    GoogleMap googleMap;
    Activity activity;
    Activity a1;
    String translate;
    receiver revInst;
    public PlacesDisplayTask(Activity a) {
        activity = a;
    }

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

        List<HashMap<String, String>> googlePlacesList = null;
        Places placeJsonParser = new Places();

        try {
            googleMap = (GoogleMap) inputObj[0];
            googlePlacesJson = new JSONObject((String) inputObj[1]);
            a1=(Activity) inputObj[2];
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);;
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return googlePlacesList;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
        googleMap.clear();
        String s;

        revInst =(receiver)a1.getApplication();
        MapsActivity m = new MapsActivity();
        String[] values = new String[list.size()];
        String s1;
        for (int i = 0; i < list.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = list.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
           values[i] = ""+ placeName+vicinity+latLng+"\n";

            s1 =""+placeName+vicinity;
            translate= translate+s1+"\n";


            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);

            googleMap.addMarker(markerOptions);

            //revInst.setYo1(s1);

        }
        t=new Translation(a1);
        t.execute(translate);
      /*  revInst.setYo1(t.output);
        Intent intent = new Intent();
        intent.setAction("com.map.action");
        intent.putExtra("result",true);
        activity.sendBroadcast(intent);*/

    }

}