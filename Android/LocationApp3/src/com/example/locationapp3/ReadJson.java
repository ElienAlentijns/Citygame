package com.example.locationapp3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class ReadJson {
	private static final String TAG = MainActivity.class.getSimpleName();

	public String readJsonFromServer(InputStream in) {
			
    	StringBuilder output = new StringBuilder();
    	BufferedReader reader = null;
    	try {
    		reader = new BufferedReader(new InputStreamReader(in));
    		String line;
    		while ((line = reader.readLine()) != null) {
    			output.append(line);
    		}
    	} catch (IOException e) {
    		Log.e(TAG, "" + e.getMessage());
    	} finally {
    		if (reader != null) {
    			try {
    				reader.close();
    			} catch (IOException e) {
    				Log.e(TAG, "" + e.getMessage());
    			}
    		}
    	}
    	return output.toString();    
	}

}
