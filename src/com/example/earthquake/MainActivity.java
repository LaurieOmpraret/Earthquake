package com.example.earthquake;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	static final String TAG="earthquake";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
		{ super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_main);
		
		Button btn1 = (Button)findViewById(R.id.button1); 
		btn1.setOnClickListener(new OnClickListener() {
	
			@Override //on passe des infos
			public void onClick(View v) {
				Intent monIntent = new Intent(MainActivity.this,MainActivity.class); 
				monIntent.putExtra("Value1", "Salut ! "); 
				monIntent.putExtra("Value2", "C'est l'activité 2 !"); 
				startActivity(monIntent);
			} 	
		});
		/*Button btn2 = (Button)findViewById(R.id.button2); 
		btn2.setOnClickListener(new OnClickListener() {
			@Override //on passe pas d'infos
			public void onClick(View v) {
				Intent monIntent = new Intent(MainActivity.this,Volley.class); 
				startActivity(monIntent);
			}
		});*/
		Button btn3 = (Button)findViewById(R.id.button3); 
		btn3.setOnClickListener(new OnClickListener() {
			@Override // on load le json en cliquant
			public void onClick(View v) {
				loadJson();
			}
		});
		
		Button button4 = (Button) findViewById(R.id.button4); 
		button4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				List<NameValuePair> params = new ArrayList<NameValuePair>(); 
				new WebServiceRequestor("http://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php",params).execute(); 
				}
		}); 
	
		}

	public void loadJson() {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); 
			StrictMode.setThreadPolicy(policy);
			}
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php"); try {
		HttpResponse response = httpclient.execute(httpget); if(response != null) {
		String line = "";
		HttpEntity httpEntity = response.getEntity(); line = EntityUtils.toString(httpEntity);
		Log.i(TAG, "lines " + line);
		JSONObject theObject = new JSONObject(line);
		Toast.makeText(this,line.substring(0,500), Toast.LENGTH_LONG).show();
		
		} else {
		Toast.makeText(this, "Unable to complete your request", Toast.LENGTH_LONG).show();
		}
		} catch (ClientProtocolException e) {
		Toast.makeText(this, "Caught ClientProtocolException", Toast.LENGTH_SHORT).show();
		Log.e("test",e.toString()); } catch (IOException e) {
		Toast.makeText(this, "Caught IOException", Toast.LENGTH_SHORT).show();
		Log.e("test",e.toString()); } catch (Exception e) {
		Toast.makeText(this, "Caught Exception", Toast.LENGTH_SHORT).show();
		Log.e("test",e.toString()); }
		
	}

    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
        Log.d("TAG",  "onStart");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
        Log.d("TAG",  "onRestart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        Log.d("TAG",  "onResume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
        Log.d("TAG",  "onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
        Log.d("TAG",  "onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
        Log.d("TAG",  "onDestroy");
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


private class WebServiceRequestor extends AsyncTask<String, Void, String> {
	private ProgressDialog pDialog; 
	String URL;
	List<NameValuePair> parameters;
	public WebServiceRequestor(String url, List<NameValuePair> params) {
		this.URL = url;
		this.parameters = params; 
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient(); 
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			HttpPost httpPost = new HttpPost(URL);
			if (parameters != null) {
				httpPost.setEntity(new UrlEncodedFormEntity(parameters)); 
				}
			httpResponse = httpClient.execute(httpPost);
			httpEntity = httpResponse.getEntity(); 
			return EntityUtils.toString(httpEntity);
		} catch (Exception e) {
		}
		return ""; 
	}
	@Override
	protected void onPostExecute(String result) {
		pDialog.dismiss();
		TextView txt = (TextView) findViewById(R.id.output);
		//txt.setText("Response is: "+ result.substring(0,500)); 
		// txt.setText(result);
		try {
			JSONObject theObject = new JSONObject(result); 
			txt.setText("Response is: "+theObject.getString("status")+"\n"+theObject.getString("count")+"/"+theObject.getString("count_total")); 
		} catch (Exception e){
			txt.setText("Error during process"); // txt.setText(result); 
			}
			super.onPostExecute(result); 
		}
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(MainActivity.this); 
			pDialog.setMessage("Processing Request..."); 
			pDialog.setIndeterminate(false); 
			pDialog.setCancelable(false);
			pDialog.show();
			super.onPreExecute(); 
		}
		@Override
		protected void onProgressUpdate(Void... values) { }
		}

}



