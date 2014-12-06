package com.example.photosharing;
/*
 * Fragment code for Manage Page
 *  This file is related to fragment_manage.xml
 * 
 */
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ManageFragment extends Fragment {
	String msgID;
	TextView DeviceIDValue,PointsValue,UploadsValue,EmptyFirstName,EmptyLastName;
	Button FillDetails,GetPoints,SubmitDetails;
	EditText FirstNameValue,LastNameValue;
		@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_manage, container, false);
        
        TelephonyManager mngr = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE); 
		msgID = mngr.getDeviceId();
		// Above lines to get IMEI Number of phone/ device ID of emulator
		DeviceIDValue=(TextView)rootView.findViewById(R.id.DeviceIDValue);
		PointsValue=(TextView)rootView.findViewById(R.id.PointsValue);
		UploadsValue=(TextView)rootView.findViewById(R.id.UploadsValue);
		EmptyFirstName=(TextView)rootView.findViewById(R.id.EmptyFirstName);
		EmptyLastName=(TextView)rootView.findViewById(R.id.EmptyLastName);
		FillDetails=(Button)rootView.findViewById(R.id.FillDetails);
		GetPoints=(Button)rootView.findViewById(R.id.GetPoints);
		SubmitDetails=(Button)rootView.findViewById(R.id.SubmitDetails);
		FirstNameValue=(EditText)rootView.findViewById(R.id.FirstNameValue);
		LastNameValue=(EditText)rootView.findViewById(R.id.LastNameValue);
		DeviceIDValue.setText(msgID);
		getnSetData();
		FillDetails.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EmptyFirstName.setVisibility(View.GONE);
				EmptyLastName.setVisibility(View.GONE);
				//Clearing the Empty value fields and setting the edit texts
				FirstNameValue.setVisibility(View.VISIBLE);
				LastNameValue.setVisibility(View.VISIBLE);
				FillDetails.setVisibility(View.GONE);
				SubmitDetails.setVisibility(View.VISIBLE);
				getnSetData();
			}
		});	
		SubmitDetails.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EmptyFirstName.setVisibility(View.VISIBLE);
				EmptyLastName.setVisibility(View.VISIBLE);
				FirstNameValue.setVisibility(View.GONE);
				LastNameValue.setVisibility(View.GONE);
				//After submission, clear the editTexts and bring back the text views
				FillDetails.setVisibility(View.VISIBLE);
				SubmitDetails.setVisibility(View.GONE);
				if(!FirstNameValue.getText().toString().matches("")||!LastNameValue.getText().toString().matches(""))
            	{
            		
            		new setData().execute(msgID.toString(),FirstNameValue.getText().toString(),LastNameValue.getText().toString());
            		Toast.makeText(getActivity().getApplicationContext(), "Data Uploaded.", 
               			   Toast.LENGTH_LONG).show();
            	
            	}
            	else
            	{
            		Toast.makeText(getActivity().getApplicationContext(), "Please enter a Valid String", 
              			   Toast.LENGTH_LONG).show();
            	}
				getnSetData();
			}
		});
		GetPoints.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		return rootView;
    }
	void getnSetData()
	{
		/*List<String> abc=(List<String>) new getData().execute(msgID.toString());
		DeviceIDValue.setText(abc.get(0).toString());
		PointsValue.setText(abc.get(1).toString());
		UploadsValue.setText(abc.get(2).toString());
		EmptyFirstName.setText(abc.get(4).toString());
		EmptyLastName.setText(abc.get(5).toString());*/
		Toast.makeText(getActivity().getApplicationContext(), new getData().execute(msgID.toString()).toString(),Toast.LENGTH_LONG).show();
	}
/*
 *  setData is the class responsible for sending data over to the Database
 * 	It makes use of asynchronous task
 *  Currently have used HTTP services to send the data, will be changing over to DTN
 *  We make use of JSON Parser and write bytes according to JSON syntax
 *  
 */

class setData extends AsyncTask<String, Integer, Long>{

	final String MY_TAG = "setData";
	
	protected Long doInBackground(String... strings ){
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		@SuppressWarnings("unused")
		DataInputStream inputStream = null;
		String urlServer = getString(R.string.Server_link)+"setData.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary =  "*****"; // Demarkation

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1*1024*1024;
		String msgID= strings[0];
		String ph_ID = strings[1];
		String title = strings[2];
		try
		{
			
			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();
			
			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setChunkedStreamingMode(1024 * 1024);

			// Enable POST method
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			
			outputStream = new DataOutputStream( connection.getOutputStream() );
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			// Adding username
			outputStream.writeBytes("Content-Disposition: form-data; name=\"username\"; "+ lineEnd + lineEnd + msgID );
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			// Adding First name 
			outputStream.writeBytes("Content-Disposition: form-data; name=\"fname\"; "+ lineEnd + lineEnd + ph_ID);
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			// Adding Last name			
			outputStream.writeBytes("Content-Disposition: form-data; name=\"lname\"; "+ lineEnd + lineEnd + title);
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			outputStream.flush();
			outputStream.close();
			
			

			// Responses from the server (code and message)
			int respCode = connection.getResponseCode();
			String respMsg = connection.getResponseMessage();
			Log.d(MY_TAG, "Data Uploaded "  + respCode + "\nMessage:" +respMsg);
		} catch (Exception ex)
		{
			Log.e(MY_TAG,"Data not uploaded :"+ex.getMessage());
		} 

		return null;
	}
}
/*
 *  getData is the class responsible for getting data from the Database
 * 	It makes use of asynchronous task
 *  Currently have used HTTP services to send the data, will be changing over to DTN
 *  We make use of JSON Object as php file returns a JSON coded stream/file 
 *  
 */

class getData extends AsyncTask<String, Void, String>{

	final String MY_TAG = "getData";
	
	protected String doInBackground(String... strings ){
		HttpClient httpclient = new DefaultHttpClient();
		List<String> snd = new ArrayList<String>();
		String all="abc";
		//ignore all
        HttpPost httppost = new HttpPost(getString(R.string.Server_link)+"/getData.php");
        // We are using the POST method for getting data
        
        try {
        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", strings[0]));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        	HttpResponse response = httpclient.execute(httppost);
        	
        	String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
        	JSONObject object = new JSONObject(jsonResult);
        	
        	String username = object.getString("username");
        	String points = object.getString("points");
        	String no_uploads = object.getString("no_uploads");
        	String time_started = object.getString("time_started");
        	String fname = object.getString("fname");
        	String lname = object.getString("lname");
        	snd.add(username);
        	snd.add(points);
        	snd.add(no_uploads);
        	snd.add(time_started);
        	snd.add(fname);
        	snd.add(lname);
        	//Retrieve everything and then add it to the string
        	all=(username+" "+points+" "+no_uploads+" "+time_started+" "+fname+" "+lname+" ");
        	}
        catch (JSONException e) {
        		e.printStackTrace();
        	}
        catch (ClientProtocolException e) {
        		e.printStackTrace();
        	}
        catch (IOException e) {
        		e.printStackTrace();
        	}
		return all;
        
		}
	}
/*
 * Function to take value from Input Stream, basically the php file and convert the 
 * values to string
 */
private StringBuilder inputStreamToString(InputStream is) {
    String rLine = "";
    StringBuilder answer = new StringBuilder();
    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
      
    try {
     while ((rLine = rd.readLine()) != null) {
      answer.append(rLine);
       }
    }
      
    catch (IOException e) {
        e.printStackTrace();
     }
    return answer;
   }
}

