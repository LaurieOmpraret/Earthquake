package com.example.earthquake;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class EqActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eq);

        Bundle extras = getIntent().getExtras();

        TextView title = (TextView) findViewById(R.id.eqActivityTitle);
        title.setText(extras.getString("title"));

        TextView time = (TextView) findViewById(R.id.eqActivityDate);
        time.setText(extras.getString("time"));

        TextView mag = (TextView) findViewById(R.id.eqActivityMag);
        mag.setText(extras.getString("mag"));

        TextView place = (TextView) findViewById(R.id.eqActivityPalce);
        place.setText(extras.getString("place"));

        TextView detail = (TextView) findViewById(R.id.eqActivityDetail);
        detail.setText(extras.getString("detail"));

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_eq, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_param) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
