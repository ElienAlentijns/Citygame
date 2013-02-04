package com.example.loginapp1;

public class Coordinates {
	private int id;
	private double longitude;
	private double latitude;
	
	public Coordinates() {
	}
	
	public Coordinates(double latitude, double longitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	

}
