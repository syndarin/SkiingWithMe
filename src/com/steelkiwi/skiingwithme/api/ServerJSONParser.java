package com.steelkiwi.skiingwithme.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.util.Log;

import com.steelkiwi.skiingwithme.AC;
import com.steelkiwi.skiingwithme.data.Follower;
import com.steelkiwi.skiingwithme.data.Resort;
import com.steelkiwi.skiingwithme.data.SWMPoint;
import com.steelkiwi.skiingwithme.data.Slope;
import com.steelkiwi.skiingwithme.entities.AuthResponseData;

public class ServerJSONParser {
	
	public Serializable parseSWMauthResponse(JSONObject rootJSON) throws JSONException{
		
		AuthResponseData authData=new AuthResponseData();
		
		String id=rootJSON.getString(AC.ServerKeys.AUTH_ID);
		authData.setId(id);
		String picture=rootJSON.getString(AC.ServerKeys.AUTH_PICTURE);
		authData.setPicture(picture);
		String latitude=rootJSON.getString(AC.ServerKeys.AUTH_LATITUDE);
		authData.setLatitude(Double.parseDouble(latitude));
		String longitude=rootJSON.getString(AC.ServerKeys.AUTH_LONGITUDE);
		authData.setLongitude(Double.parseDouble(longitude));
		String firstName=rootJSON.getString(AC.ServerKeys.AUTH_FIRST_NAME);
		authData.setFirstName(firstName);
		String lastName=rootJSON.getString(AC.ServerKeys.AUTH_LAST_NAME);
		authData.setLastName(lastName);
		
		return authData;
	}
	
	public ArrayList<Resort> parseResorts(JSONArray rootJSON) throws JSONException{
		
		ArrayList<Resort>  resorts=new ArrayList<Resort>();
		
		for(int i=0; i<rootJSON.length(); i++){
			
			Resort resort=new Resort();
			
			JSONObject resortJSON=rootJSON.getJSONObject(i);
			
			String idString=resortJSON.getString(AC.ServerKeys.RESORT_ID);
			resort.setId(Integer.parseInt(idString));
			String title=resortJSON.getString(AC.ServerKeys.RESORT_TITLE);
			resort.setTitle(title);
			String country=resortJSON.getString(AC.ServerKeys.RESORT_COUNTRY);
			resort.setCountry(country);
			
			String mapPoints=resortJSON.getString(AC.ServerKeys.RESORT_MAP);
			ArrayList<SWMPoint> resortMap=this.parsePoints(mapPoints);
			resort.setBounds(resortMap);
			
			JSONArray slopesJSONArray=resortJSON.getJSONArray(AC.ServerKeys.RESORT_SLOPES);
			ArrayList<Slope> slopesArray=new ArrayList<Slope>();
			
			for(int j=0; j<slopesJSONArray.length(); j++){
				
				Slope slope=new Slope();
				
				JSONObject slopeJSON=slopesJSONArray.getJSONObject(j);
				
				String slopeTitle=slopeJSON.getString(AC.ServerKeys.SLOPE_TITLE);
				slope.setTitle(slopeTitle);
				
				String colorString=slopeJSON.getString(AC.ServerKeys.SLOPE_COLOR);
				int color=Color.parseColor(colorString);
				slope.setColor(color); 
				Log.i("Parser", "Color "+color);//******************************************
				
				String image=slopeJSON.getString(AC.ServerKeys.SLOPE_IMAGE);
				slope.setImage(image);		
				Log.i("Parser", "Img "+image);//******************************************
				
				String difficulty=slopeJSON.getString(AC.ServerKeys.SLOPE_DIFFICULTY);
				slope.setDifficulty(difficulty);
				Log.i("Parser", "Diff "+difficulty);//******************************************
				
				String length=slopeJSON.getString(AC.ServerKeys.SLOPE_LENGTH);
				slope.setLength(Double.parseDouble(length));
				Log.i("Parser", "Length "+length);//******************************************
				
				String id=slopeJSON.getString(AC.ServerKeys.SLOPE_ID);
				slope.setId(Integer.parseInt(id));
				Log.i("Parser", "id "+id);//******************************************
				
				String mapString=slopeJSON.getString(AC.ServerKeys.SLOPE_COORDINATES);
				ArrayList<SWMPoint> bounds=this.parsePoints(mapString);
				slope.setCoords(bounds);	
				
				slopesArray.add(slope);
			}	
			
			resort.setSlopes(slopesArray);
			
			resorts.add(resort);
		}		
		return resorts;
		
	}
	
	private ArrayList<SWMPoint> parsePoints(String s) throws JSONException{
		ArrayList<SWMPoint> result=new ArrayList<SWMPoint>();
		JSONArray pointsArray=new JSONArray(s);
		for(int i=0; i<pointsArray.length(); i++){
			JSONObject point=pointsArray.getJSONObject(i);
			
			String latitudeString=point.getString(AC.ServerKeys.COORD_LAT);
			Double latitude=Double.parseDouble(latitudeString);
			latitude*=1E6;
			
			String longitudeString=point.getString(AC.ServerKeys.COORD_LONG);
			Double longitude=Double.parseDouble(longitudeString);
			longitude*=1E6;
			
			result.add(new SWMPoint(latitude.intValue(), longitude.intValue()));
		}
		return result;
	}
	
	public ArrayList<Follower> parseFollowers(String s) throws JSONException{
		JSONArray followersJSON=new JSONArray(s);
		ArrayList<Follower> result=new ArrayList<Follower>();
		for(int i=0; i<followersJSON.length(); i++){
			JSONObject singleJSON=followersJSON.getJSONObject(i);
			
			String firstName=singleJSON.getString(AC.ServerKeys.FOL_FIRST_NAME);
			
			String lastName=singleJSON.getString(AC.ServerKeys.FOL_LAST_NAME);
			
			String avatar=singleJSON.getString(AC.ServerKeys.FOL_AVATAR);
			
			String idString=singleJSON.getString(AC.ServerKeys.FOL_ID);
			
			String latitudeString=singleJSON.getString(AC.ServerKeys.FOL_LAT);
			Double latitude=Double.parseDouble(latitudeString);
			latitude*=1E6;
			
			String longitudeString=singleJSON.getString(AC.ServerKeys.FOL_LONG);
			Double longitude=Double.parseDouble(longitudeString);
			longitude*=1E6;
			
			result.add(new Follower(idString, firstName, lastName, avatar, latitude, longitude));
		}
		return result;
	}

}
