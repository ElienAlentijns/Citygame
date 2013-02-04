package com.example.loginapp1;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InfoFragment extends Fragment {
	private String role;
	private String description = "";
	private TextView getDescription;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_info, container, false);
		role = getArguments().getString("role");
		getDescription = (TextView) view.findViewById(R.id.getDescription);
		if (description.equals("")) {
			new LongRunningGetIO().execute();
		} 
		else {
			getDescription.setText(description);
		}
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Save UI state changes to the savedInstanceState.
	  // This bundle will be passed to onCreate if the process is
	  // killed and restarted.
	  savedInstanceState.putString("description", description);
	  // etc.
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Restore UI state from the savedInstanceState.
	  // This bundle has also been passed to onCreate.
	  description = savedInstanceState.getString("description");
	}


private class LongRunningGetIO extends AsyncTask <Void, Void, String> {
	private static final String TAG = "MainActivity";
	
	//Creates a connection to the web service
	@Override
	protected String doInBackground(Void... params) {
		try
		{
			//We gaan een GET request aanmaken
			HttpClient httpclient = new DefaultHttpClient();
		    //De URL om berichten op te vragen 
			HttpGet httpget = new HttpGet("http://webservice.citygamephl.be/CityGameWS/resources/generic/getRoleDescription/" + role);
		
		    //We voeren het GET request uit 
	        HttpResponse response = httpclient.execute(httpget);
	        //Log.e(TAG, inputStreamToString(response.getEntity().getContent()).toString());
		    
		    //We vangen de informatie die ons terug gestuurd wordt op 
	        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
	        //We zetten de informatie om naar JSON
	        String json = reader.readLine();
	        JSONTokener tokener = new JSONTokener(json);
	        //We steken de informatie in een JSONArray
	        JSONArray finalResult = new JSONArray(tokener);
	        
	        for (int i = 0; i < finalResult.length(); i++) {
	        	
	        	JSONObject hulp = finalResult.getJSONObject(i);
	        	//We vragen de nodige informatie op uit de JSONArray
		        description = hulp.getString("roleDescription");
		        //We printen alle informatie af om te kijken of het gelukt is
		        Log.e(TAG, "description: " + description);
		     
	        }
	     
	        //We vangen de fouten op
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
		getDescription.setText(description);		
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

