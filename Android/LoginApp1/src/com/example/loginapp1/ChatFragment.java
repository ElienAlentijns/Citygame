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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChatFragment extends Fragment {
	private static final String TAG = "MainActivity";
	private TextView chatText;
	private Button sendText;
	private Button btnJager;
	private Button btnProoi;
	private Button btnSpelleider;
	private EditText text;

	private String lastMessageID = "0";
	private String message;
    private String timeStamp;
    private String player;
    private String chatbox;
    private String role = "Jager";
    private String currentChatbox;
    private Runnable Messages;
    private Thread thread;
    private HttpGet httpget;
    private String messageHunter = "";
    private String messagePrey = "";
    private String messageLeader = "";
    private String result;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_chat, container, false);
		
		chatText = (TextView) view.findViewById(R.id.lblChatText);
		sendText = (Button) view.findViewById(R.id.btnSendText);
		text = (EditText) view.findViewById(R.id.txtChatText);
		btnJager = (Button) view.findViewById(R.id.btnJager);
		btnProoi = (Button) view.findViewById(R.id.btnProoi);
		btnSpelleider = (Button) view.findViewById(R.id.btnSpelleider);
		
		chatText.setMovementMethod(new ScrollingMovementMethod());
		sendText.setOnClickListener(sendMessageListener);
		btnJager.setOnClickListener(jagerListener);
		btnProoi.setOnClickListener(prooiListener);
		btnSpelleider.setOnClickListener(spelleiderListener);
		
		//checkConnection();
		
		return view;
	}
	
	private OnClickListener sendMessageListener = new OnClickListener()
    {
        public void onClick(View v) {
	        sendText.setClickable(false);
	        //De functie om berichten te sturen wordt opgeroepen
	        new sendMessages().execute();
        }
    };
    
    private OnClickListener jagerListener = new OnClickListener()
    {
        public void onClick(View v) {
        	btnJager.setClickable(false);
        	role = "Jager";
        	currentChatbox = "Jager-Jager";
        	//De berichten tussen de jagers worden opgehaald
        	new getMessages().execute();
        }
    };
    
    private OnClickListener prooiListener = new OnClickListener()
    {
        public void onClick(View v) {
        	role = "Prooi";
        	currentChatbox = "Prooi-Jager";
        	//De berichten tussen jager en prooi worden opgehaald
        	new getMessages().execute();

        }
    };
    
    private OnClickListener spelleiderListener = new OnClickListener()
    {
        public void onClick(View v) {
        	btnSpelleider.setClickable(false);
        	role = "Spelleider";
        	currentChatbox = "Jager-Spelleider";
        	//De berichten tussen jager en spelleider worden opgehaald
        	new getMessages().execute();
        }
    };

	private class getMessages extends AsyncTask <Void, Void, String> {
		private static final String TAG = "MainActivity";

		@Override
		protected String doInBackground(Void... params) {
			try
			{
				//We gaan een GET request aanmaken
				HttpClient httpclient = new DefaultHttpClient();
			    //De URL om berichten op te vragen 
				
				httpget = new HttpGet("http://webservice.citygamephl.be/CityGameWS/resources/generic/getMessages/1/Jager/" + lastMessageID);
				
			
			    //We voeren het GET request uit 
		        HttpResponse response = httpclient.execute(httpget);
		        //Log.e(TAG, inputStreamToString(response.getEntity().getContent()).toString());
			    
			    //We vangen de informatie die ons terug gestuurd wordt op 
		        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		        //We zetten de informatie om naar JSON
		        String json = reader.readLine();
		        JSONTokener tokener = new JSONTokener(json);
		        //We steken de informatie in een JSONArray
		        JSONArray finalResult = new JSONArray(tokener);
		        
		        for (int i = 0; i < finalResult.length(); i++) {
		        	
		        	JSONObject hulp = finalResult.getJSONObject(i);
		        	//We vragen de nodige informatie op uit de JSONArray
		        	message = hulp.getString("message");
			        timeStamp = hulp.getString("time");
			        player = hulp.getString("player");
			        lastMessageID = hulp.getString("message_ID");
			        chatbox = hulp.getString("chatbox");
			        //We printen alle informatie af om te kijken of het gelukt is
			        Log.e(TAG, "message: " + message);
			        Log.e(TAG, "time: " + timeStamp);
			        Log.e(TAG, "player: " + player);
			        Log.e(TAG, "lastMessageID: " + lastMessageID);
			        Log.e(TAG, "chatbox: " + chatbox);
			        
			        if (chatbox.equals("Jager-Jager")) {
						messageHunter = messageHunter + "\n" + player + "\n" + message;
					} 
			        else if (chatbox.equals("Prooi-Jager")) {
			        	messagePrey = messagePrey + "\n" + player + "\n" + message;
			        }
			        else {
			        	messageLeader = messageLeader + "\n" + player + "\n" + message;
			        }
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
			return message;
			
		}
		
		protected void onPostExecute(String results) {
			//Wanneer de AsyncTask gedaan heeft met zijn berekeningen gaan we gebruik maken van het resultaat
			if (results!=null) {
				//De jager krijgt het gesprek met een andere jager te zien 
				if (role.equals("Jager")) {
					chatText.setText(messageHunter);
				}
				else if (role.equals("Prooi")) {
					//De jager krijgt het geprek te zien met de prooi 
					chatText.setText(messagePrey);
				}
				else {
					//De jager krijgt het gesprek te zien met de spelleider 
					chatText.setText(messageLeader);
				}
			}
			//Wanneer alles gedaan is kan er terug op de button geklikt worden 
			sendText.setClickable(true);
			btnJager.setClickable(true);
			btnProoi.setClickable(true);
			btnSpelleider.setClickable(true);
		}
	}
	
	private class sendMessages extends AsyncTask <Void, Void, String> {
		private static final String TAG = "MainActivity";

		@Override
		protected String doInBackground(Void... params) {
			try
			{
	    		//We gaan een HTTP POST request doen 
				HttpClient httpclient = new DefaultHttpClient();
			    //Dit de url om berichten naar de databank te kunnen sturen
				HttpPost httppost = new HttpPost("http://webservice.citygamephl.be/CityGameWS/resources/generic/sendMessage");
				//We maken een lijst aan met de parameters die doorgegeven moeten worden aan de POST
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		        nameValuePairs.add(new BasicNameValuePair("gameID", "1"));
		        nameValuePairs.add(new BasicNameValuePair("playerID", "3"));
		        nameValuePairs.add(new BasicNameValuePair("message", text.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("chatbox", currentChatbox));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
		        //We voeren het POST request uit 
		        HttpResponse response = httpclient.execute(httppost);
		        Log.e(TAG, "WAAROM?????" + inputStreamToString(response.getEntity().getContent()).toString()); 
		        result = "yes";
		      //We vangen de fouten op 
			} catch (MalformedURLException e) {
	    		Log.e(TAG, "MalFormedURL: " + e.getMessage());
			} catch (IOException e) {
				Log.e(TAG, "IOException: " + e.getMessage());
	    	} catch (Exception e) { 
	            Log.e(TAG, "Other: " + e.getMessage());
	            e.printStackTrace();
	    	}
	    	
			return result;
		}
		
		protected void onPostExecute(String result) {
			//Wanneer de AsyncTask gedaan heeft met zijn berekeningen gaan we gebruik maken van het resultaat
			if (result.equals("yes")) {
				text.setText("");
				sendText.setClickable(true);
				new getMessages().execute();
			}
		}
	}
	
	//We zetten de http response om naar een string 
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
	
	/*public boolean checkConnection() {
		boolean connected = false;
        ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        connected = ((networkinfo != null) && (networkinfo.isAvailable()) && (networkinfo.isConnected()));
        Log.v("Message ", connected + "");
        if (connected == false) {
            Toast.makeText(MainActivity.this, "Er is geen internetconnectie", Toast.LENGTH_LONG).show();
        } else {
        	connected = true;
        }
        
        return connected;
	}*/

}
