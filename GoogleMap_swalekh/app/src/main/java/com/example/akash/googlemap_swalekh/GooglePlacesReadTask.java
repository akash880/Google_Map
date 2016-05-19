package com.example.akash.googlemap_swalekh;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

public class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
    String googlePlacesData = null;
    GoogleMap googleMap;
    Activity a;

    @Override
    protected String doInBackground(Object... inputObj) {
        try {
            Log.d("hey","in the first");
            googleMap = (GoogleMap) inputObj[0];
            String googlePlacesUrl = (String) inputObj[1];
            Http http = new Http();
            a=  (Activity)inputObj[2];
            googlePlacesData = http.read(googlePlacesUrl);
        } catch (Exception e) {
            Log.d("Google Place Read Task", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask(a);
        Object[] toPass = new Object[3];
        toPass[0] = googleMap;
        toPass[1] = result;
        toPass[2] =a;
        placesDisplayTask.execute(toPass);
    }
}