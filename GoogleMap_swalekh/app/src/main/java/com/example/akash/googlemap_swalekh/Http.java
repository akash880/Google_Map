package com.example.akash.googlemap_swalekh;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http {

    public String read(String httpUrl) throws IOException {
        String httpData = "";

        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            //Log.d("heyu","in the second"+httpUrl);
            URL url = new URL(httpUrl);
           // Log.d("heyu","inenijejvejvjjj/////"+url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            //Log.d("heyu23", "roar----->"+inputStream+"roar<------");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";

            //Log.d("heyu1","in the second");
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            httpData = stringBuffer.toString();
            Log.d("tag", "  "+ httpData);

            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            inputStream.close();
            httpURLConnection.disconnect();
        }
        return httpData;
    }
}