package com.example.locationapp3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.apphance.android.Apphance;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import com.apphance.android.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements LocationListener {
	//We maken alle nodige veriabelen aan 
	private GoogleMap googleMap;
	private static final String TAG = MainActivity.class.getSimpleName();
	private String getLongitude;
	private String getLatitude;
	private Thread thread;
	private DatabaseHandler db;
	private String player;
	private String APP_KEY = "e3eb335ae1c7047b1e16365536cf3f652797c9dd";
	private String playerPrey;
	private String latitudePrey;
	private String longitudePrey;
	private String playersHunter[];
	private String latitudesHunter[];
	private String longitudesHunter[];
	private Runnable Preys;
	private Runnable Coordinates;
	private double latitude;
	private double longitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        //We gaan de coordinaten opvragen 
		new getHunter().execute();
        new getPrey().execute();
		
        //We gaan de map ophalen 
        getMap();
	}
	
	public void getMap() {
		
		//Referentie naar de layout 
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //We maken een googleMap object aan om toegang te krijgen tot de map
        googleMap = fm.getMap();
        //We zorgen ervoor dat de huidige locatie opgevraagd kan worden
        googleMap.setMyLocationEnabled(true);
        //We maken een locationmanager object aan 
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        //We halen de naam op van de provider
        String provider = locationManager.getBestProvider(criteria, true);
        //We halen de huidige locatie op 
        Location location = locationManager.getLastKnownLocation(provider);
        //Als deze locatie verschillend is van null gaan we in de functie onLocationChanged 
        if(location!=null){
            onLocationChanged(location);
        }
 
        //We updaten de locatie
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
	}
	
	@Override
    public void onLocationChanged(Location location) {
 
        TextView tvLocation = (TextView) findViewById(R.id.tv_location);
        //We vragen de latitude op van de huidige positie
        latitude = location.getLatitude();
        //We vragen de longitude op van de huidige positie
        longitude = location.getLongitude();
        //We sturen de longitude en latitude naar de databank
        new sendCoordinates().execute();
        //We maken hiervoor een LatLng object aan
        LatLng latLng = new LatLng(latitude, longitude);
        //We maken een object van de databank klasse aan
        db = new DatabaseHandler(this);
        //We voegen de longitude en latitude toe aan de database
		db.addCoordinates(new Coordinates(latitude, longitude));
        
       /* List<Coordinates> coordinates = db.getAllCoordinates();
		for (Coordinates cr : coordinates) {
			String log = "(Latitude: " + cr.getLatitude() + ", Longitude: " + cr.getLongitude() + ")";
			Log.e("TEST", log);
			
			latLng = new LatLng(cr.getLatitude(), cr.getLongitude());
	        Marker test = googleMap.addMarker(new MarkerOptions()
	        	.position(latLng)
	        	.title("Lalala")
	        	.snippet("Deze weg heb ik gevolgd"));
		}
		

		Polygon polygon = googleMap.addPolygon(new PolygonOptions()
        		.add(new LatLng(latitude, longitude))
        		.strokeColor(Color.RED));*/
 
        //Showing the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        
        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
 
        //We tonen de latitude en longitude
        tvLocation.setText("Latitude:" +  latitude  + ", Longitude:"+ longitude );
        
    }
 
    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

	private class getHunter extends AsyncTask <Void, Void, String> {
		private static final String TAG = "MainActivity";
		
		@Override
		protected String doInBackground(Void... params) {
			try
			{		
				//We gaan een GET request aanmaken
				HttpClient httpclient = new DefaultHttpClient();
				//De URL om de coordinaten op te vragen 
				HttpGet httppost = new HttpGet("http://webservice.citygamephl.be/CityGameWS/resources/generic/getLocations/1/2/Jager");

				//We voeren het GET request uit 
		        HttpResponse response = httpclient.execute(httppost);
		        
		        //We vangen de informatie die ons terug gestuurd wordt op 
		        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		        //We zetten de informatie om naar JSON
		        String json = reader.readLine();
		        JSONTokener tokener = new JSONTokener(json);
		        //We steken de informatie in een JSONArray
		        JSONArray finalResult = new JSONArray(tokener);
		        //We initialiseren alle arrays die de resultaten zullen bevatten 
		        playersHunter = new String[finalResult.length()];
		        latitudesHunter = new String[finalResult.length()];
		        longitudesHunter = new String[finalResult.length()];
		        
		        for (int i = 0; i < finalResult.length(); i++) {
		        	JSONObject hulp = finalResult.getJSONObject(i);
		        	//We vragen de nodige informatie op uit de JSONArray
		        	player = hulp.getString("player");
			        getLatitude = hulp.getString("latitude");
			        getLongitude = hulp.getString("longitude");
			        //We printen alle informatie af om te kijken of het gelukt is
			        Log.e(TAG, "getLatitude: " + getLatitude);
			        Log.e(TAG, "getLongitude: " + getLongitude);
			        Log.e(TAG, "player: " + player);
			        
			        //We slaan alle gegevens op in arrays
			       	playersHunter[i] = player;
			       	latitudesHunter[i] = getLatitude;
			       	longitudesHunter[i] = getLongitude;
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
			
			return getLongitude;
			
		}
		
		protected void onPostExecute(String getLongitude) {
			// final to-do statements like emptying the textbox
			if (getLongitude!=null) {
				for (int i = 0; i < playersHunter.length; i++)
                {   
						LatLng latLng = new LatLng(Double.parseDouble(latitudesHunter[i]), Double.parseDouble(longitudesHunter[i]));
	                    Marker test = googleMap.addMarker(new MarkerOptions()
	                    	.position(latLng)
	                    	.title(playersHunter[i])
	                    	.icon(BitmapDescriptorFactory.fromResource(R.drawable.jager))
	                    	.snippet("Hier ben ik!"));
                } 
			}
		}
	}
	
	private class getPrey extends AsyncTask <Void, Void, String> {
		private static final String TAG = "MainActivity";
		
		@Override
		protected String doInBackground(Void... params) {
			try
			{		
				//We gaan een GET request aanmaken
				HttpClient httpclient = new DefaultHttpClient();
				//De URL om de coordinaten op te vragen 
				HttpGet httppost = new HttpGet("http://webservice.citygamephl.be/CityGameWS/resources/generic/getLocations/1/2/Prooi");

				//We voeren het GET request uit 
		        HttpResponse response = httpclient.execute(httppost);
		        
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
		        	player = hulp.getString("player");
			        getLatitude = hulp.getString("latitude");
			        getLongitude = hulp.getString("longitude");
			        //We printen alle informatie af om te kijken of het gelukt is
			        Log.e(TAG, "getLatitude: " + getLatitude);
			        Log.e(TAG, "getLongitude: " + getLongitude);
			        Log.e(TAG, "player: " + player);
			        
			        //We slaan alle gegevens op in arrays
			        playerPrey = player;
			        latitudePrey = getLatitude;
			        longitudePrey = getLongitude;
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
			
			return getLongitude;
			
		}
		
		protected void onPostExecute(String getLongitude) {
			// final to-do statements like emptying the textbox
			if (getLongitude!=null) {
				 LatLng latLng = new LatLng(Double.parseDouble(latitudePrey), Double.parseDouble(longitudePrey));
			        Marker marker = googleMap.addMarker(new MarkerOptions()
			        	.position(latLng)
			        	.title(playerPrey)
			        	.icon(BitmapDescriptorFactory.fromResource(R.drawable.prooi))
			        	.snippet("Hier ben ik!"));	
			}
		}
	}
	
	private class sendCoordinates extends AsyncTask <Void, Void, String> {
		private static final String TAG = "MainActivity";
		
		@Override
		protected String doInBackground(Void... params) {
			try
			{
	    		//We gaan een HTTP POST request doen 
				HttpClient httpclient = new DefaultHttpClient();
			    //Dit de url om coordinaten naar de databank te kunnen sturen
				HttpPost httppost = new HttpPost("http://webservice.citygamephl.be/CityGameWS/resources/generic/setPlayerCoordinate");
				//We maken een lijst aan met de parameters die doorgegeven moeten worden aan de POST
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		        nameValuePairs.add(new BasicNameValuePair("playerID", "3"));
		        nameValuePairs.add(new BasicNameValuePair("gameID", "1"));
		        nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
		        nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
		        //We voeren het POST request uit 
		        HttpResponse response = httpclient.execute(httppost);
		        Log.e(TAG, inputStreamToString(response.getEntity().getContent()).toString());

		        
			    /*BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		        String json = reader.readLine();
		        JSONTokener tokener = new JSONTokener(json);
		        JSONArray finalResult = new JSONArray(tokener);		*/    
			    
		      //We vangen de fouten op 
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
			// final to-do statements like emptying the textbox
			if (result.equals("yes")) {
				 Toast.makeText(getApplicationContext(), "Coordinaten zijn verstuurd", Toast.LENGTH_LONG).show();
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
}
