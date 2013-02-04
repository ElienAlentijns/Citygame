package com.example.sqliteapp1;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DatabaseHandler db = new DatabaseHandler(this);
		
		/*db.addCoordinates(new Coordinates(50.937185, 5.348667));
		db.addCoordinates(new Coordinates(50.936161, 50.936161));
		db.addCoordinates(new Coordinates(50.934836, 5.354676));
		db.addCoordinates(new Coordinates(50.933429, 5.35605));
		db.addCoordinates(new Coordinates(50.930346, 5.356715));*/
		
		List<Coordinates> coordinates = db.getAllCoordinates();
		
		for (Coordinates cr : coordinates) {
			String log = "(Latitude: " + cr.getLatitude() + ", Longitude: " + cr.getLongitude() + ")";
			Log.e("TEST", log);
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
