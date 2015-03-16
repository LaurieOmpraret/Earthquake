package com.example.earthquake;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;


import java.lang.Object;

public class EqService extends Service {

    private GeoJson geoJson;
    private static final String urlLastHour = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";
    private IBinder mBinder = new LocalBinder();

    public EqService() {
    }

    public class LocalBinder extends Binder{
        public EqService getInstance(){
            return EqService.this;
        }
    }

    public void onCreat() {
        super.onCreate();
        CheckNotifyEq();
    }


    public void CheckNotifyEq() {
// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLastHour,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object objet) {
                        getLastEq(objet);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "An error occured while trying to get the list of earthquakes.",
                        Toast.LENGTH_LONG).show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getLastEq(Object objet) {
        Gson gson = new Gson();
        geoJson = gson.fromJson((String) objet, GeoJson.class);
        notifyEq();

    }

    private void notifyEq() {
        int nbSeisme = geoJson.getCount();
        if (nbSeisme > 0) {
            //Toast.makeText(getApplicationContext(), nbSeisme + "New earthquakes in last hour.",
              //      Toast.LENGTH_LONG).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(nbSeisme + "New earthquakes in last hour.");
            AlertDialog dialog = builder.create();

            builder.setNegativeButton("fermer", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id){
                    dialog.cancel();
                }
            });
            dialog.show();
        }


    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return mBinder;
    }
}
