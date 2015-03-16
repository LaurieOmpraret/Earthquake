package com.example.earthquake;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;


public class ParamActivity extends ActionBarActivity implements View.OnClickListener {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private RadioButton significantEarthquakes;
    private RadioButton m45Earthquakes;
    private RadioButton m25Earthquakes;
    private RadioButton m10Earthquakes;
    private RadioButton maEarthquakes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        addListenerOnButton();
    }

    public void addListenerOnButton() {
        significantEarthquakes = (RadioButton) findViewById(R.id.significant_earthquake);
        significantEarthquakes.setOnClickListener(this);

        m45Earthquakes = (RadioButton) findViewById(R.id.M45_earthquakes);
        m45Earthquakes.setOnClickListener(this);

        m25Earthquakes = (RadioButton) findViewById(R.id.M25_earthquake);
        m25Earthquakes.setOnClickListener(this);

        m10Earthquakes = (RadioButton) findViewById(R.id.M10_earthquakes);
        m10Earthquakes.setOnClickListener(this);

        maEarthquakes = (RadioButton) findViewById(R.id.MA_earthquakes);
        maEarthquakes.setOnClickListener(this);

        checkGoodButton();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.significant_earthquake:
                editor.putFloat("M", (float) 6.0);
                break;
            case R.id.M45_earthquakes:
                editor.putFloat("M", (float)4.5);
                break;
            case R.id.M25_earthquake:
                editor.putFloat("M", (float)2.5);
                break;
            case R.id.M10_earthquakes:
                editor.putFloat("M", (float)1.0);
                break;
            case R.id.MA_earthquakes:
                editor.putFloat("M", (float)-5);
                break;
        }

        editor.commit();

        Intent data = new Intent();
        data.putExtra("settings", true);
        setResult(RESULT_OK, data);
        finish();
    }

    private void checkGoodButton() {
        float mag = preferences.getFloat("M", (float)-5);
        if (mag == 6.0) {
            significantEarthquakes.setChecked(true);
        } else if (mag == 4.5) {
            m45Earthquakes.setChecked(true);
        } else if (mag == 2.5) {
            m25Earthquakes.setChecked(true);
        } else if (mag == 1.0) {
            m10Earthquakes.setChecked(true);
        } else {
            maEarthquakes.setChecked(true);
        }
    }
}