package com.example.getphotowebserverapp1;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	//We maken de variabelen aan die we nodig hebben 
	private static final String TAG = MainActivity.class.getSimpleName();
	private Runnable Pictures;
	private Thread thread;
	private Button btnGetPicture;
	private ImageView image;
	private String ba1;
	private ReadJson json = new ReadJson();
	private String description;
	private String photo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//We nemen een foto in een bitmap
		Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.photo);
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 100, bao);
		//We zetten de foto op naar bytes
		byte [] ba = bao.toByteArray();
		//We coderen de bytes naar een Base64 string 
		ba1 = Base64.encodeToString(ba,Base64.DEFAULT);
		Log.i(TAG, "STRING FOTO:" + ba1);
		
        btnGetPicture = (Button) findViewById(R.id.btnGetPicture);
        btnGetPicture.setOnClickListener(getPictureListener);
        image = (ImageView)this.findViewById(R.id.imageView);

        new setPhoto().execute();
        
	}
	
	private OnClickListener getPictureListener = new OnClickListener()
    {
        public void onClick(View v) {
        	new getPhoto().execute();
        }
    };
	
	private class getPhoto extends AsyncTask <Void, Void, String> {
		private static final String TAG = "MainActivity";		
		
		//Creates a connection to the web service
		@Override
		protected String doInBackground(Void... params) {
			try
			{
				//We gaan een GET request aanmaken
				HttpClient httpclient = new DefaultHttpClient();
			    //De URL om berichten op te vragen 
				HttpGet httpget = new HttpGet("http://webservice.citygamephl.be/CityGameWS/resources/generic/getPhoto");
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
		        	description = hulp.getString("description");
			        photo = hulp.getString("photo");
			        //We printen alle informatie af om te kijken of het gelukt is
			        Log.e(TAG, "description: " + description);
			        Log.e(TAG, "photo: " + photo);
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
			if (description!=null) {
				//De foto die we opgevraagd hebben wordt getoond in een ImageView
				byte[] imageAsBytes = Base64.decode(photo, Base64.DEFAULT);
	    	    image.setImageBitmap(
	    	            BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
	    	    );
			}
			
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
	
	private class setPhoto extends AsyncTask <Void, Void, String> {
		private static final String TAG = "MainActivity";

		//Creates a connection to the web service
		@Override
		protected String doInBackground(Void... params) {
			try
			{	
				//We gaan een HTTP POST request doen 
				HttpClient httpclient = new DefaultHttpClient();
				//Dit de url om foto's naar de databank te kunnen sturen
				HttpPost httppost = new HttpPost("http://webservice.citygamephl.be/CityGameWS/resources/generic/sendPhoto");
				//We maken een lijst aan met de parameters die doorgegeven moeten worden aan de POST
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		        nameValuePairs.add(new BasicNameValuePair("playerID", "4"));
		        nameValuePairs.add(new BasicNameValuePair("taskID", "3"));
		        nameValuePairs.add(new BasicNameValuePair("photo", ba1));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

		        //We voeren het POST request uit 
		        HttpResponse response = httpclient.execute(httppost);
		        Log.e(TAG, inputStreamToString(response.getEntity().getContent()).toString());

			} catch (MalformedURLException e) {
	    		Log.e(TAG, "MalFormedURL: " + e.getMessage());
			} catch (IOException e) {
				Log.e(TAG, "IOException: " + e.getMessage());
	    	} catch (Exception e) { 
	            Log.e(TAG, "Other: " + e.getMessage());
	            e.printStackTrace();
	    	}
			
			return "yes";
		}
		
		protected void onPostExecute(String result) {
			//Wanneer de AsyncTask gedaan heeft met zijn berekeningen gaan we gebruik maken van het resultaat
			if (result.equals("yes")) {
				Toast.makeText(getApplicationContext(), "Foto is verzonden", Toast.LENGTH_LONG).show();
			}
			
		}
	}
}
