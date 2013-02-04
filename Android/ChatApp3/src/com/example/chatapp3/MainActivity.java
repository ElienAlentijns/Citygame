package com.example.chatapp3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
	private static final String TAG = "MainActivity";
	private TextView chatText;
	private Button sendText;
	private Button btnJager;
	private Button btnProoi;
	private Button btnSpelleider;
	private EditText text;
	private String getMessage;
	private String[] messages;
	private String[] time;
	private String[] players;
	private String[] messageIDS;
	private String[] chatboxes;
	private String message;
    private String timeStamp;
    private String player;
    private String messageID;
    private String chatbox;
    private String role;
    private Runnable Messages;
    private Thread thread;

	@Override
	// initiates the different GUI objects
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		chatText = (TextView) findViewById(R.id.lblChatText);
		sendText = (Button) findViewById(R.id.btnSendText);
		text = (EditText) findViewById(R.id.txtChatText);
		btnJager = (Button) findViewById(R.id.btnJager);
		btnProoi = (Button) findViewById(R.id.btnProoi);
		btnSpelleider = (Button) findViewById(R.id.btnSpelleider);
		
		chatText.setMovementMethod(new ScrollingMovementMethod());
		sendText.setOnClickListener(sendMessageListener);
		btnJager.setOnClickListener(jagerListener);
		btnProoi.setOnClickListener(prooiListener);
		btnSpelleider.setOnClickListener(spelleiderListener);
		
		checkConnection();
	}
	
	private OnClickListener sendMessageListener = new OnClickListener()
    {
        public void onClick(View v) {
        	Messages = new Runnable(){
                public void run() {
                	sendText.setClickable(false);
                	//De functie om berichten te sturen wordt opgeroepen
                	sendMessages();
                }
            };
            
            thread =  new Thread(null, Messages, "MagentoBackground");
            thread.start();
            
            while (thread.isAlive()) {
            	if (!thread.isAlive()) {
            		text.setText("");
            	}
            }
            
            sendText.setClickable(true);
        }
    };
    
    private OnClickListener jagerListener = new OnClickListener()
    {
        public void onClick(View v) {
        	
        	//chatText.setText(chatText.getText() + "\n" + text.getText());
        	
        	btnJager.setClickable(false);
        	role = "Jager";
        	//De berichten tussen de jagers worden opgehaald
        	new LongRunningGetIO().execute();
        	
        	/*for (int i = 0; i < players.length; i++) {
        		chatText.setText(chatText.getText() + "/n" + players[i] + "/n" + messages[i]);
        	}*/
        }
    };
    
    private OnClickListener prooiListener = new OnClickListener()
    {
        public void onClick(View v) {
        	
        	//chatText.setText(chatText.getText() + "\n" + text.getText());
        	role = "Prooi";
        	//De berichten tussen jager en prooi worden opgehaald
        	new LongRunningGetIO().execute();
        	
        	/*for (int i = 0; i < players.length; i++) {
        		chatText.setText(chatText.getText() + "/n" + players[i] + "/n" + messages[i]);
        	}*/
        }
    };
    
    private OnClickListener spelleiderListener = new OnClickListener()
    {
        public void onClick(View v) {
        	
        	//chatText.setText(chatText.getText() + "\n" + text.getText());
        	btnSpelleider.setClickable(false);
        	role = "Spelleider";
        	//De berichten tussen jager en spelleider worden opgehaald
        	new LongRunningGetIO().execute();
        	
        	/*for (int i = 0; i < players.length; i++) {
        		chatText.setText(chatText.getText() + "/n" + players[i] + "/n" + messages[i]);
        	}*/
        }
    };
    
    public void sendMessages() {
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
	        nameValuePairs.add(new BasicNameValuePair("chatbox", "Jager-Spelleider"));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
	        //We voeren het POST request uit 
	        HttpResponse response = httpclient.execute(httppost);
	        Log.e(TAG, "WAAROM?????" + inputStreamToString(response.getEntity().getContent()).toString());

	        
		    /*BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
	        String json = reader.readLine();
	        JSONTokener tokener = new JSONTokener(json);
	        JSONArray finalResult = new JSONArray(tokener);		*/    
		    
	      //We vangen de fouten op 
		} catch (MalformedURLException e) {
    		Log.e(TAG, "MalFormedURL: " + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "IOException: " + e.getMessage());
    	} catch (Exception e) { 
            Log.e(TAG, "Other: " + e.getMessage());
            e.printStackTrace();
    	}
    	
    }

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
				//We gaan een GET request aanmaken
				HttpClient httpclient = new DefaultHttpClient();
			    //De URL om berichten op te vragen 
				HttpGet httpget = new HttpGet("http://webservice.citygamephl.be/CityGameWS/resources/generic/getMessages/1/Jager/3");
			
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
		        //We initialiseren alle arrays die de resultaten zullen bevatten 
		        messages = new String[finalResult.length()];
		        time = new String[finalResult.length()];
		        players = new String[finalResult.length()];
		        messageIDS = new String[finalResult.length()];
		        chatboxes = new String[finalResult.length()];
		        
		        for (int i = 0; i < finalResult.length(); i++) {
		        	
		        	JSONObject hulp = finalResult.getJSONObject(i);
		        	//We vragen de nodige informatie op uit de JSONArray
		        	message = hulp.getString("message");
			        timeStamp = hulp.getString("time");
			        player = hulp.getString("player");
			        messageID = hulp.getString("message_ID");
			        chatbox = hulp.getString("chatbox");
			        //We printen alle informatie af om te kijken of het gelukt is
			        Log.e(TAG, "message: " + message);
			        Log.e(TAG, "time: " + timeStamp);
			        Log.e(TAG, "player: " + player);
			        Log.e(TAG, "lastMessageID: " + messageID);
			        Log.e(TAG, "chatbox: " + chatbox);
			        
			        //We slaan alle gegevens op in arrays
			        messages[i] = message;
			        time[i] = timeStamp;
			        players[i] = player;
			        messageIDS[i] = messageID;
			        chatboxes[i] = chatbox;
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
					chatText.setText("");
					for (int i = 0; i < players.length; i++) {
						if (chatboxes[i].equals("Jager-Jager"))
		        		chatText.setText(chatText.getText() + "\n" + players[i] + "\n" + messages[i]);
		        	}
				}
				else if (role.equals("Prooi")) {
					//De jager krijgt het geprek te zien met de prooi 
					chatText.setText("");
					for (int i = 0; i < players.length; i++) {
						if (chatboxes[i].equals("Prooi-Jager"))
		        		chatText.setText(chatText.getText() + "\n" + players[i] + "\n" + messages[i]);
		        	}
				}
				
				else {
					//De jager krijgt het gesprek te zien met de spelleider 
					chatText.setText("");
					for (int i = 0; i < players.length; i++) {
						if (chatboxes[i].equals("Jager-Spelleider"))
		        		chatText.setText(chatText.getText() + "\n" + players[i] + "\n" + messages[i]);
		        	}
				}
			}
			//Wanneer alles gedaan is kan er terug op de button geklikt worden 
			sendText.setClickable(true);
			btnJager.setClickable(true);
			btnProoi.setClickable(true);
			btnSpelleider.setClickable(true);
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
	
	public boolean checkConnection() {
		boolean connected = false;
        ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        connected = ((networkinfo != null) && (networkinfo.isAvailable()) && (networkinfo
                .isConnected()));
        Log.v("Message ", connected + "");
        if (connected == false) {
            Toast.makeText(MainActivity.this,
                    "Er is geen internetconnectie",
                    Toast.LENGTH_LONG).show();
        } else {
        	connected = true;
        }
        
        return connected;
	}
}
