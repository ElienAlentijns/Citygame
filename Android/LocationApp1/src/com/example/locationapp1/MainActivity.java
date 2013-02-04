package com.example.locationapp1;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MyLocationOverlay;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import android.app.Activity;
import android.graphics.drawable.Drawable;

public class MainActivity extends Activity {
	MyLocationOverlay myLocationOverlay = null;
	MyItemizedOverlay myItemizedOverlay = null;
	MapController myMapController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final MapView mapView = (MapView) findViewById(R.id.mapview);
        //Inzoomen en uitzoomen met knoppen
        //mapView.setUseDataConnection(false);
        mapView.setBuiltInZoomControls(true);
        //Inzoomen en uitzoomen met multitouch
        mapView.setMultiTouchControls(true);
        
        //Duidt de locatie van de gebruiker aan
        myLocationOverlay = new MyLocationOverlay(this, mapView);
        mapView.getOverlays().add(myLocationOverlay);
        
        myMapController = mapView.getController();
        //Zet het zoomlevel bij het opstarten van de applicatie
        myMapController.setZoom(12);
        
        //Het gebied waar de gebruiker zich bevindt wordt in kaart gebracht
        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
            	mapView.getController().animateTo(myLocationOverlay.getMyLocation());
            } 
        });
        
        //Sterren worden getekend om op de map te plaatsen
        Drawable marker=getResources().getDrawable(android.R.drawable.star_big_on);
        int markerWidth = marker.getIntrinsicWidth();
        int markerHeight = marker.getIntrinsicHeight();
        marker.setBounds(0, markerHeight, markerWidth, 0);
         
        ResourceProxy resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
         
        myItemizedOverlay = new MyItemizedOverlay(marker, resourceProxy);
        mapView.getOverlays().add(myItemizedOverlay);
        
        //Er worden punten aangeduid op de map met deze coördinaten
        GeoPoint myPoint1 = new GeoPoint(50.929188, 5.357417);
        myItemizedOverlay.addItem(myPoint1, "myPoint1", "myPoint1");
        GeoPoint myPoint2 = new GeoPoint(50.937342, 5.348669);
        myItemizedOverlay.addItem(myPoint2, "myPoint2", "myPoint2");
       
    }
    
    @Override
    protected void onResume() {
	     super.onResume();
	     myLocationOverlay.enableMyLocation();
	     myLocationOverlay.enableFollowLocation();
	     myLocationOverlay.enableCompass();
    } 
    
    @Override
    protected void onPause() {
	     super.onPause();
	     myLocationOverlay.disableMyLocation();
	     myLocationOverlay.disableFollowLocation();
	     myLocationOverlay.disableCompass();
    }
   
}
