package com.example.locationapp3;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Citygame";
    private static final String TABLE_COORDINATES = "coordinates";
    private static final String KEY_ID = "id";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    
    //Constructor
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    //We maken de nodige tabellen aan 
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COORDINATES_TABLE = "CREATE TABLE " + TABLE_COORDINATES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LATITUDE + " NUMERIC,"
                + KEY_LONGITUDE + " NUMERIC" + ")";
        db.execSQL(CREATE_COORDINATES_TABLE);
    }
 
    //Wanneer de tabel geupgrade wordt
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //De tabel wordt gedropt 
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COORDINATES);
 
        //De tabel wordt opnieuw aangemaakt
        onCreate(db);
    }
    
    //De functie om coordinaten toe te voegen
    public void addCoordinates(Coordinates coor) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	//We verwijderen alles uit de tabel coordinaten
    	db.delete(TABLE_COORDINATES, null, null);
        ContentValues values = new ContentValues();
        //De waardes die meegegeven worden
        values.put(KEY_LATITUDE, coor.getLatitude()); 
        values.put(KEY_LONGITUDE, coor.getLongitude()); 
 
        //De rij wordt ingevoegd
        db.insert(TABLE_COORDINATES, null, values);
        db.close(); // Closing database connection
    }
    
    /*
    public Coordinates getCoordinates(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TABLE_COORDINATES, new String[] { KEY_ID,
                KEY_LATITUDE, KEY_LONGITUDE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
     
        Coordinates coord = new Coordinates(cursor.getDouble(1), cursor.getDouble(2));
        // return contact
        return coord;
    }*/
    
    //Alle coordinaten opvragen
    public List<Coordinates> getAllCoordinates() {
        List<Coordinates> coordinateList = new ArrayList<Coordinates>();
        //Query om alles van de tabel op te halen 
        String selectQuery = "SELECT  * FROM " + TABLE_COORDINATES;
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        //We gaan door heel de tabel en voegen all
        if (cursor.moveToFirst()) {
            do {
            	//We maken een object aan van coordinates
            	Coordinates coord = new Coordinates();
            	//We setten de longitude en latitude
            	coord.setLongitude(Double.parseDouble(cursor.getString(1)));
            	coord.setLatitude(Double.parseDouble(cursor.getString(2)));
                //We voegen de coordinaten toe aan een lijst 
                coordinateList.add(coord);
            } while (cursor.moveToNext());
        }
     
        //Return de coordinaten lijst 
        return coordinateList;
    }
}
