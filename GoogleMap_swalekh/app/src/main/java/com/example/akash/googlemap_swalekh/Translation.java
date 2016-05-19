package com.example.akash.googlemap_swalekh;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by akash on 13/5/16.
 */

public class Translation extends AsyncTask<String , String, String> {
    String error;
    String fileName="";
    Activity a;
    String s4="";
    ArrayList<String> output;
    public Translation(Activity activity){
        this.a=activity;

    }
    @Override

    protected String doInBackground(String... params) {
        String transliteration = "http://api.reverieinc.com/parabola/transliterateSimpleJSON";
        HttpURLConnection urlConnection = null;
        int statusCode=0;
        s4=""+ params[0];
        String statusMsg="";

        try {
            URL url = new URL(transliteration);

//                    url = new URL("https://api.reverieinc.com/parabola/getBaseTranslations");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            //if (statusCode == 200 && statusMsg.equalsIgnoreCase("OK")) {
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(10 * 1000);          // 3 s.
            urlConnection.setReadTimeout(10*1000);
            urlConnection.setChunkedStreamingMode(0);


            JSONObject jsonParam = getJsonObjTrans();


            OutputStream os = urlConnection.getOutputStream();
            os.write(jsonParam.toString().getBytes("UTF-8"));
            os.close();
            statusCode = urlConnection.getResponseCode();
            statusMsg = urlConnection.getResponseMessage();

        }catch (SocketTimeoutException e)
        {
            error="SocketTimeout";
            e.printStackTrace();
            return fileName;
        }
        catch (ConnectTimeoutException e)
        {
            error="ConnectionTimeout";
            e.printStackTrace();
            return fileName;

        }
        catch (IOException e) {
            Log.e("REVERIE ", "22222http Post request error. " + e.getMessage());
            error=e.toString();
            return fileName;

        } catch (IllegalStateException e) {
            error=e.toString();
            Log.e("REVERIE ", "33333http Post request error. " + e.getMessage());
            //e.printStackTrace();
            return fileName;

        }

        try {
            if (statusCode == 200 && statusMsg.equalsIgnoreCase("OK"))
            {

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                String response = builder.toString();
//                    Log.e("http RESPONSE: ", "" + response);

//                    Log.e("statusCode&msg ", "code: " + statusCode + "msg: " + statusMsg);

                JSONObject json;
                try {
                    json = new JSONObject(response);
                    Log.e("rt","12"+response);
                    JSONArray jsonArray = json.optJSONArray("outArray");
                    int length = jsonArray.length();
                    output = new ArrayList();

                    for(int j=0; j < length; j++) {
                        JSONObject jsonloc2 = jsonArray.getJSONObject(j);
                         fileName  = jsonloc2.optString("transResponse").toString();
                        Log.e("filename output ",""+fileName);
                        output.add(fileName);
                        Log.d("pehla",""+output);

                    }
                    return fileName;
//
                } catch (JSONException e) {
                    error=e.toString();
                    e.printStackTrace();
                    return fileName;

                }

            }

        } catch (IOException e) {
            error=e.toString();

//                    Log.w("REVERIE " + this.getClass().getSimpleName(), "http Post request error. " + e.getMessage());
            e.printStackTrace();

        } finally {
            urlConnection.disconnect();
        }

        return fileName;

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        receiver revInst = (receiver)a.getApplication();
        revInst.setYo1(output);
        Intent intent = new Intent();
        intent.setAction("com.map.action");
        intent.putExtra("result",true);
        a.sendBroadcast(intent);

        Log.e("ee",""+result+error);

    }

    private JSONObject getJsonObjTrans() {
        ArrayList <String> input= new ArrayList();
        input.add(s4);
        JSONObject jObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();

        for (int i = 0; i < input.size(); i++) {
            jsonArr.put(input.get(i));
        }

        try {



            jObj.put("inArray", jsonArr);
            jObj.put("REV-APP-ID", "com.sdktest");
            jObj.put("REV-API-KEY", "R1y1zhmyFH5llkbA93V0loTx2twrPrHyd3Sb");
            jObj.put("domain", 9);
            jObj.put("language", "hindi");
            jObj.put("originLanguage", "hindi");
            jObj.put("webSdk", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Log.e("JSON Send", "" + jObj);
        return jObj;
    }


}
