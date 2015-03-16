package com.example.earthquake;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static EqService eqService;
    private static final int REQ_CODE = 999;

    private static final String urlLastMonth = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson";
    private GeoJson geoJson;
    private List<GeoJson.Seisme> seismes;
    private List<GeoJson.Seisme> seismesDisplay;
    private float minimumMagnitudeForEarthquake;


    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            EqService.LocalBinder mLocalBinder = (EqService.LocalBinder) service;
            eqService = mLocalBinder.getInstance();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            eqService = null;
        }
    };

    private static EqService getEqService() {
        return eqService;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPreference();

        startEqService();
        getLastMonthEqList();
    }

    private void loadPreference() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        minimumMagnitudeForEarthquake = preferences.getFloat("M", (float)-5);
    }

    private void getLastMonthEqList(){
        RequestQueue queue = Volley.newRequestQueue(this);
        final TextView errorDisplay = (TextView) findViewById(R.id.errorDisplay);
        errorDisplay.setText("Please wait.");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLastMonth,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object o) {
                        Gson gson = new Gson();
                        geoJson = gson.fromJson((String)o, GeoJson.class);
                        seismes = geoJson.getFeatures();
                        seismesDisplay = seismes;
                        filterMagnitude();
                        errorDisplay.setText("");
                        Log.d("main", "done");
                        displaySeismes();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("main", "error");
                errorDisplay.setText("An error occured while trying to get the list of earthquakes. Please retry later.");
            }
        });
        queue.add(stringRequest);
    }


    private void startEqService() {
        /*if (eqService == null) {
            Intent seismeServiceIntent = new Intent(MainActivity.this, EqService.class);
            bindService(seismeServiceIntent, mConnection, BIND_AUTO_CREATE);
        }*/
    	Intent seismeServiceIntent = new Intent(MainActivity.this, EqService.class);
        startService(seismeServiceIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //eqService.CheckNotifyEq();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        long currentTimestamp = new Date().getTime();
        float duration;

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, ParamActivity.class);
                startActivityForResult(intent, REQ_CODE);
                return true;
            case R.id.past_hour:
                duration = 1L*60L*60L*1000L;
                filterTime(currentTimestamp, duration);
                return true;
            case R.id.past_day:
                duration = 24L*60L*60L*1000L;
                filterTime(currentTimestamp, duration);
                return true;
            case R.id.past_7_days:
                duration = 7L*24L*60L*60L*1000L;
                filterTime(currentTimestamp, duration);
                return true;
            case R.id.past_30_days:
                duration = 30L*24L*60L*60L*1000L;
                filterTime(currentTimestamp, duration);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQ_CODE) {
            if (data.hasExtra("settings") && data.getBooleanExtra("settings", true)) {
                loadPreference();
                filterMagnitude();
            }
        }
    }

    private void filterMagnitude() {
        seismesDisplay = new ArrayList();

        for (GeoJson.Seisme seisme: seismes) {
            if (seisme.getMag() > minimumMagnitudeForEarthquake) {
                seismesDisplay.add(seisme);
            }
        }
    }
    
    
    private void filterTime(long currentTimestamp, float duration) {
        if (seismes == null) {
            return;
        }

        seismesDisplay = new ArrayList();

        for(GeoJson.Seisme seisme: seismes) {
            if (currentTimestamp - seisme.getTime() <= duration
                    && seisme.getMag() > minimumMagnitudeForEarthquake) {
                seismesDisplay.add(seisme);
            } else {
                break;
            }
        }
        displaySeismes();
        Log.d("filterEarthquakesByTime", Integer.toString(seismesDisplay.size()) + "/"
                + Integer.toString(seismesDisplay.size()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eqService = null;
        getApplicationContext().unbindService(mConnection);
    }

    private void displaySeismes(){
        ListView myListView = (ListView) findViewById(R.id.eqList);
        EqAdapter adapter = new EqAdapter(this, seismesDisplay);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EqActivity.class);
                GeoJson.Seisme seisme = seismesDisplay.get(position);
                intent.putExtra("title", seisme.getTitle());
                intent.putExtra("time", seisme.getStandardTime());
                intent.putExtra("mag", "mag :" + seisme.getMagString());
                intent.putExtra("place", seisme.getProperties().getPlace());
                intent.putExtra("detail", seisme.getProperties().getDetail());
                startActivity(intent);
            }
        });
        myListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

