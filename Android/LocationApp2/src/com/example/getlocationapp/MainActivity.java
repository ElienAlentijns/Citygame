package com.example.getlocationapp;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView txtLatitude;
	private TextView txtLongitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		txtLatitude = (TextView) findViewById(R.id.latitude);
		txtLongitude = (TextView) findViewById(R.id.longitude);
		
		LocationManager locationManager =
		        (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationManagerHelper lmh = new LocationManagerHelper();

		String mlocProvider;
		Criteria hdCrit = new Criteria();

		hdCrit.setAccuracy(Criteria.ACCURACY_COARSE);

		mlocProvider = locationManager.getBestProvider(hdCrit, true);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1000, lmh);
		Location currentLocation = locationManager.getLastKnownLocation(mlocProvider);
		locationManager.removeUpdates(lmh);

		double currentLatitude = currentLocation.getLatitude();
		double currentLongitude = currentLocation.getLongitude();
		
		txtLatitude.setText("Latitude: " + currentLatitude);
		txtLongitude.setText("Longitude: " + currentLongitude);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
