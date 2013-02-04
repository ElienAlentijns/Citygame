package com.example.getlocationapp;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LocationManagerHelper implements LocationListener {
    private static double latitude;
    private static double longitude;

    @Override
    public void onLocationChanged(Location loc) {
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
        Log.i("MyLocation",Double.toString(latitude)+" "+Double.toString(longitude));
    }

    @Override
    public void onProviderDisabled(String provider) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    public static double getLatitude() {
        return latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

}