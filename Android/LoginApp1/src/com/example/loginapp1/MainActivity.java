

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

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	//Alle variabelen worden aangemaakt
	private static final String TAG = MainActivity.class.getSimpleName();
	private EditText txtUsername;
	private EditText txtPassword;
	private Button btnLogin;
	private String username;
	private String password;
	private TextView text;
	private Runnable Login;
	private Thread thread;
	private String intervalPrey;
	private String gameID;
	private String playerID;
	private String intervalHunter;
	private String role;
	private String loginFailed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		text = (TextView) findViewById(R.id.textView1);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(loginListener);
		
		text.setMovementMethod(new ScrollingMovementMethod());
		
	}
	
	private OnClickListener loginListener = new OnClickListener()
    {
        public void onClick(View v) {
        	//We gaan kijken of de gebruikersnaam en wachtwoord overeen komen in de database
        	username = txtUsername.getText().toString();
        	password = txtPassword.getText().toString();
        	
        	Log.e("TEST", username + " " + password);
        	//We starten de AsyncTask om de login te controleren
        	new LongRunningGetIO().execute();

        }
    };

    
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
		}*/
		
		
		//Creates a connection to the web service
		@Override
		protected String doInBackground(Void... params) {
			try
			{		
				//We gaan een HTTP POST request doen 
				HttpClient httpclient = new DefaultHttpClient();
				//Dit de url om de logingegevens naar de databank te kunnen sturen
				HttpPost httppost = new HttpPost("http://webservice.citygamephl.be/CityGameWS/resources/generic/loginPost");
				//We maken een lijst aan met de parameters die doorgegeven moeten worden aan de POST
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("username", username));
		        nameValuePairs.add(new BasicNameValuePair("password", password));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

		        //We voeren het POST request uit 
		        HttpResponse response = httpclient.execute(httppost);
		        //We vangen de informatie die ons terug gestuurd wordt op 
		        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		        //We zetten de informatie om naar JSON
		        String json = reader.readLine();
		        JSONTokener tokener = new JSONTokener(json);
		        //We steken de informatie in een JSONArray
		        JSONObject finalResult = new JSONObject(tokener);
		        
		        
		        if (finalResult.length() != 0) {
		        	//We vragen de nodige informatie op uit de JSONArray
		        	intervalPrey = finalResult.getString("intervalPrey");
			        intervalHunter = finalResult.getString("intervalHunter");
			        gameID = finalResult.getString("gameID");
			        playerID = finalResult.getString("playerID");
			        role = finalResult.getString("role");
			        //We printen alle informatie af om te kijken of het gelukt is
			        Log.e(TAG, "intervalPrey: " + intervalPrey);
			        Log.e(TAG, "intervalHunter: " + intervalHunter);
			        Log.e(TAG, "gameID: " + gameID);
			        Log.e(TAG, "playerID: " + playerID);
			        Log.e(TAG, "role: " + role);
		        	loginFailed = "Juist";
			        
		        	
	        	}
	        	else {
	        		Log.e(TAG, "FOUT");
	        		loginFailed = "Fout";
	        	}
		     
		        //We vangen de fouten op
			} catch (MalformedURLException e) {
	    		Log.e(TAG, "MalFormedURL: " + e.getMessage());
			} catch (IOException e) {
				Log.e(TAG, "IOException: " + e.getMessage());
	    	} catch (Exception e) { 
	            Log.e(TAG, "Other: " + e.getMessage());
	            e.printStackTrace();
	    	}
			
			return loginFailed;
		}
		
		protected void onPostExecute(String loginFailed) {
			//Wanneer de AsyncTask gedaan heeft met zijn berekeningen gaan we gebruik maken van het resultaat
			if (loginFailed!=null) {
				//Als de logingegevens niet overeenkomen krijg je een foutmelding
				if (loginFailed.equals("Fout")) {
	    			Toast.makeText(getApplicationContext(), "De login is fout", Toast.LENGTH_LONG).show();
	    			txtPassword.setText("");
	    		} 
				else {
					//Als de logingegevens juist zijn wordt er een nieuwe activity gestart
					Intent intent = new Intent(MainActivity.this, HomeActivity.class);
					intent.putExtra("intervalPrey", intervalPrey);
					intent.putExtra("intervalHunter", intervalHunter);
					intent.putExtra("gameID", gameID);
					intent.putExtra("playerID", playerID);
					intent.putExtra("role", role);
	        		startActivity(intent);
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
	}

}
