package com.steelkiwi.skiingwithme.api;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.steelkiwi.skiingwithme.Request;
import com.steelkiwi.skiingwithme.data.Follower;

public class RequestSender {

	private final String TAG = this.getClass().getSimpleName();

	private ServerJSONParser parser = new ServerJSONParser();

	public Serializable getUserParams(Map<String, String> params) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(Request.AUTH_REQUEST.getUrl());

		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			postParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		try {

			postRequest.setEntity(new UrlEncodedFormEntity(postParams));
			HttpResponse httpResonse = httpClient.execute(postRequest);

			HttpEntity entity = httpResonse.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF8");
			Log.i(TAG, responseString);

			return this.parser.parseSWMauthResponse(new JSONObject(responseString));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void sendLocation(Map<String, String> params) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpPut request = new HttpPut(Request.LOCATION_REQUEST.getUrl());
		Log.i(TAG, "making location request");

		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			postParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		try {

			request.setEntity(new UrlEncodedFormEntity(postParams));
			HttpResponse httpResonse = httpClient.execute(request);

			HttpEntity entity = httpResonse.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF8");
			Log.i(TAG, responseString);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public Serializable getAllResorts() {

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet postRequest = new HttpGet(Request.RESORTS_REQUEST.getUrl());

		try {
			HttpResponse response = httpClient.execute(postRequest);
			HttpEntity responseEntity = response.getEntity();
			String responseString = EntityUtils.toString(responseEntity, "UTF8");
			Log.i(TAG, responseString);
			return this.parser.parseResorts(new JSONArray(responseString));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<Follower> getFollowers(String id) {
		HttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet("http://skiingwith.me/api/resort/" + id + "/users/");

		try {
			HttpResponse response = client.execute(getRequest);
			HttpEntity httpEntity = response.getEntity();
			String responseString = EntityUtils.toString(httpEntity, "UTF8");
			Log.i(TAG, responseString);
			return this.parser.parseFollowers(responseString);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
