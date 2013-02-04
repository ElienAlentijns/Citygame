package com.example.locationapp3;

public class Coordinates {
	private int id;
	private double longitude;
	private double latitude;
	
	//Constructor 
	public Coordinates() {
	}
	
	//Constructor
	public Coordinates(double latitude, double longitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	//Longitude opvragen
	public double getLongitude() {
		return longitude;
	}
	
	//Longitude setten
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	//Latitude opvragen
	public double getLatitude() {
		return latitude;
	}

	//Latitude setten
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	

}
