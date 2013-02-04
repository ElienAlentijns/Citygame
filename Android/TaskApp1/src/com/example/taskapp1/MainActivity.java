package com.example.taskapp1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity  {
	private String description;
	private String location;
	private String latitude;
	private String longitude;
	private TextView lblTask;
	
	//LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING STRING ADAPTER WHICH WILL HANDLE DATA OF LISTVIEW
    ArrayAdapter<String> adapter;

    //RECORDING HOW MUCH TIMES BUTTON WAS CLICKED
    int clickCounter=0;

	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        new LongRunningGetIO().execute();
        adapter=new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1,
            listItems);
        setListAdapter(adapter);
        
    }
	
	private class LongRunningGetIO extends AsyncTask <Void, Void, String> {
		private static final String TAG = "MainActivity";

		/*protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n>0) {
				byte[] b = new byte[4096];
				n =  in.read(b);
				if (n>0) out.append(new String(b, 0, n));
			}
			return out.toString();
		}*/
		
		
		//Creates a connection to the web service
		@Override
		protected String doInBackground(Void... params) {
			try
			{			
				//We gaan een HTTP GET request doen 
				HttpClient httpclient = new DefaultHttpClient();
				//Dit de url om opdrachten van de databank op te halen 
				HttpGet httpget = new HttpGet("http://webservice.citygamephl.be/CityGameWS/resources/generic/getLatestTask/1/Prooi");
				
				//We voeren het GET request uit 
		        HttpResponse response = httpclient.execute(httpget);
		        //We vangen de informatie die ons terug gestuurd wordt op 
		        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		      //We zetten de informatie om naar JSON
		        String json = reader.readLine();
		        JSONTokener tokener = new JSONTokener(json);
		        //We steken de informatie in een JSONObject
		        JSONObject finalResult = new JSONObject(tokener);
		        
		        //We vragen de nodige informatie op uit de JSONArray
		        description = finalResult.getString("description");
			    location = finalResult.getString("location");
			    latitude = finalResult.getString("latitude");
			    longitude = finalResult.getString("longitude");
			    //We printen alle informatie af om te kijken of het gelukt is
			    Log.e(TAG, "description: " + description);
			    Log.e(TAG, "location: " + location);
			    Log.e(TAG, "latitude: " + latitude);
			    Log.e(TAG, "longitude: " + longitude);
	        	
		     
			} catch (MalformedURLException e) {
	    		Log.e(TAG, "MalFormedURL: " + e.getMessage());
			} catch (IOException e) {
				Log.e(TAG, "IOException: " + e.getMessage());
	    	} catch (Exception e) { 
	            Log.e(TAG, "Other: " + e.getMessage());
	            e.printStackTrace();
	    	}
			
			return description;
		}
		
		protected void onPostExecute(String description) {
			//Wanneer de AsyncTask gedaan heeft met zijn berekeningen gaan we gebruik maken van het resultaat
			if (description!=null) {
				listItems.add(description);
		        adapter.notifyDataSetChanged();
			}
			
		}
		
		private StringBuilder inputStreamToString(InputStream is) {
		    String line = "";
		    StringBuilder total = new StringBuilder();
		    
		    // Wrap a BufferedReader around the InputStream
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		    // Read response until the end
		    try {
				while ((line = rd.readLine()) != null) { 
				    total.append(line); 
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    // Return full string
		    return total;
		}
	}

}
