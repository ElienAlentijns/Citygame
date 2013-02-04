package com.example.sqliteapp1;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "Citygame";
 
    // Contacts table name
    private static final String TABLE_COORDINATES = "coordinates";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COORDINATES_TABLE = "CREATE TABLE " + TABLE_COORDINATES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LONGITUDE + " NUMERIC,"
                + KEY_LATITUDE + " NUMERIC" + ")";
        db.execSQL(CREATE_COORDINATES_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COORDINATES);
 
        // Create tables again
        onCreate(db);
    }
    
    public void addCoordinates(Coordinates coor) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, coor.getLatitude()); // Contact Name
        values.put(KEY_LONGITUDE, coor.getLongitude()); // Contact Phone
 
        // Inserting Row
        db.insert(TABLE_COORDINATES, null, values);
        db.close(); // Closing database connection
    }
    
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
    }
    
    public List<Coordinates> getAllCoordinates() {
        List<Coordinates> contactList = new ArrayList<Coordinates>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COORDINATES;
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Coordinates coord = new Coordinates();
            	coord.setLongitude(Double.parseDouble(cursor.getString(1)));
            	coord.setLatitude(Double.parseDouble(cursor.getString(2)));
                // Adding contact to list
                contactList.add(coord);
            } while (cursor.moveToNext());
        }
     
        // return contact list
        return contactList;
    }
}
