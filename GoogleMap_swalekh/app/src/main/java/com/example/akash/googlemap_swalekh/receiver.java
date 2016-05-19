package com.example.akash.googlemap_swalekh;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by akash on 6/5/16.
 */
public class receiver extends Application {

     ArrayList<String> list = new ArrayList<String>();
     ArrayList<String> yo = new ArrayList<String>();
     ArrayList<String> yo1 = new ArrayList<String>();
    public void setYo1(ArrayList<String> q) {
        if(q!=null) {
            list.clear();
            list.addAll(q);
        }

    }


    public ArrayList<String> getYo() {
        return list;
    }




    }

