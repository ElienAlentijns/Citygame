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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import android.app.Activity;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MapFragment extends Fragment {
	private GoogleMap googleMap;
	private static final String TAG = MainActivity.class.getSimpleName();
	private String getLongitude;
	private String getLatitude;
	private Runnable Coordinates;
	private Thread thread;
	private DatabaseHandler db;
	private String player;
	private String APP_KEY = "e3eb335ae1c7047b1e16365536cf3f652797c9dd";
	private String players[];
	private String latitudes[];
	private String longitudes[];

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_map, container, false);
		//Apphance.startNewSession(this, APP_KEY, Apphance.Mode.QA);
        //new LongRunningGetIO().execute();
        //getMap();
		return view;
	}
	
	/*public void getMap() {
		// Getting reference to the SupportMapFragment of activity_main.xml
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
 
        // Getting GoogleMap object from the fragment
        googleMap = fm.getMap();
 
        // Enabling MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);
 
        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
 
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
 
        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
 
        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);
 
        if(location!=null){
            onLocationChanged(location);
        }
 
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
        
        LatLng latLng = new LatLng(50.929188, 5.357417);
        Marker test = googleMap.addMarker(new MarkerOptions()
        			.position(latLng)
        			.title("Prooi")
        			.snippet("Je zal me toch nooit vinden!")
        			.icon(BitmapDescriptorFactory.fromResource(R.drawable.prooi)));
        
        latLng = new LatLng(50.927103, 5.355062);
        test = googleMap.addMarker(new MarkerOptions()
        	.position(latLng)
        	.title("Opdracht")
        	.snippet("Hier moet je zijn"));
       
	}
	
	@Override
    public void onLocationChanged(Location location) {
 
        TextView tvLocation = (TextView) findViewById(R.id.tv_location);
 
        // Getting latitude of the current location
        double latitude = location.getLatitude();
 
        // Getting longitude of the current location
        double longitude = location.getLongitude();
 
        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
        
        db = new DatabaseHandler(this);
		
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
        		.strokeColor(Color.RED));
 
        // Showing the current location in Google Map
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        
        // Zoom in the Google Map
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
 
        // Setting latitude and longitude in the TextView tv_location
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
		}
		
		
		//Creates a connection to the web service
		@Override
		protected String doInBackground(Void... params) {
			try
			{			
				HttpClient httpclient = new DefaultHttpClient();
			    HttpGet httppost = new HttpGet("http://webservice.citygamephl.be/CityGameWS/resources/generic/getLocations/1/2/Jager");
				// Add your data
		        /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		        nameValuePairs.add(new BasicNameValuePair("gameID", "1"));
		        nameValuePairs.add(new BasicNameValuePair("playerID", "2"));
		        nameValuePairs.add(new BasicNameValuePair("type", "Jager"));
		        
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        //Log.e("TEST", "LAQLJZEOMQJIMGOEIJGMOIQJGEOJGQOJGOQEGJMEOQGJEOQIGJEQMOGJQMOEGJOQEGJQEOGJIQEJGMQEIOGJQEMIOGJQEIGJ");
		        //Log.e(TAG, inputStreamToString(response.getEntity().getContent()).toString());
		        
		        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		        String json = reader.readLine();
		        JSONTokener tokener = new JSONTokener(json);
		        JSONArray finalResult = new JSONArray(tokener);
		        players = new String[finalResult.length()];
		        latitudes = new String[finalResult.length()];
		        longitudes = new String[finalResult.length()];
		        
		        for (int i = 0; i < finalResult.length(); i++) {
		        	JSONObject hulp = finalResult.getJSONObject(i);
		        	
		        	player = hulp.getString("player");
			        getLatitude = hulp.getString("latitude");
			        getLongitude = hulp.getString("longitude");
			        Log.e(TAG, "getLatitude: " + getLatitude);
			        Log.e(TAG, "getLongitude: " + getLongitude);
			        Log.e(TAG, "player: " + player);
			        
			        players[i] = player;
			        latitudes[i] = getLatitude;
			        longitudes[i] = getLongitude;
		        }
		    
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
				for (int i = 0; i < players.length; i++)
                {   
                	LatLng latLng = new LatLng(Double.parseDouble(latitudes[i]), Double.parseDouble(longitudes[i]));
                    Marker test = googleMap.addMarker(new MarkerOptions()
                    	.position(latLng)
                    	.title(players[i])
                    	.icon(BitmapDescriptorFactory.fromResource(R.drawable.jager))
                    	.snippet("Hier ben ik!"));
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
	}*/
}
