package com.steelkiwi.skiingwithme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.steelkiwi.skiingwithme.api.RequestRunnable;
import com.steelkiwi.skiingwithme.data.DBAdapter;
import com.steelkiwi.skiingwithme.data.Follower;
import com.steelkiwi.skiingwithme.data.Resort;
import com.steelkiwi.skiingwithme.entities.AuthResponseData;

public class SWMMainActivity extends TabActivity implements OnTabChangeListener {

	private final String TAG = this.getClass().getSimpleName();

	public static String UPDATE_ACTION = "com.steelkiwi.skiingwithme.UPDATE_DATA";
	public static String MAP_ACTION = "com.steelkiwi.skiingwithme.LOAD_MAP";
	public static String USERS_UPDATE="com.steelkiwi.skiingwithme.USERS_UPDATE";

	private static float locationUpdateDistance = 0.1f; // m
	private static int locationUpdateTime = 2000; // ms

	private LocationManager locationManager;
	private GpsLocationListener locationListener;
	private String provider;
	private Criteria criteria;

	private DBAdapter dbAdapter;
	private Handler handler = new Handler();
	private ProgressDialog progressDlg;

	private TabHost tabHost;
	private int selectedResort = -1;
	private ArrayList<Resort> resorts;
	private AuthResponseData userData;
	private Thread followersThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);

		// create tabs

		tabHost = (TabHost) findViewById(android.R.id.tabhost);

		Intent intent;
		intent = new Intent().setClass(this, SWMMapActivity.class);
		TabHost.TabSpec specMap = tabHost.newTabSpec("MAP");
		String inflater = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getSystemService(inflater);
		View specMapView = li.inflate(R.layout.tabheader, null);
		TextView tabMapText = (TextView) specMapView.findViewById(R.id.tabLbl);
		tabMapText.setText(R.string.tabMap);
		specMap.setIndicator(specMapView);
		specMap.setContent(intent);
		tabHost.addTab(specMap);

		intent = new Intent().setClass(this, SWMListActivity.class);
		TabHost.TabSpec specList = tabHost.newTabSpec("LIST");
		View specListView = li.inflate(R.layout.tabheader, null);
		TextView tabListText = (TextView) specListView.findViewById(R.id.tabLbl);
		tabListText.setText(R.string.tabList);
		specList.setIndicator(specListView);
		specList.setContent(intent);
		tabHost.addTab(specList);

		tabHost.setCurrentTab(1);
		tabHost.setOnTabChangedListener(this);

		// create db adapter class for db routine

		dbAdapter = new DBAdapter(this);
		dbAdapter.open();

		// progress bar is displayed while data processing

		progressDlg = new ProgressDialog(this);
		progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDlg.setMessage("Loading data..");
		//
		// //debug
		// //i created two resort objects to test how they are stored and retrieved to/from db
		// //in reality you need to query a server for a list of all resorts
		// //and display them in the list and map activity
		// //do everything in background
		// //and when you get the data from server, parse json, store data in db
		// //+ notify map and list activity to update their ui
		// //notify by sending broadcast as i already had done in the code below (handler.post(notifyUpdateResorts);)
		//

		// location mamager for getting current user location

		userData=(AuthResponseData)getIntent().getSerializableExtra("userdata");
		
		String serviceString = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(serviceString);
		locationListener = new GpsLocationListener();
		
		// find the best location provider
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);
	
		RequestRunnable resortsRequest = new RequestRunnable(Request.RESORTS_REQUEST, null, threadHandler);
		Thread t = new Thread(resortsRequest);
		t.start();
		handler.post(doShowProgressDlg);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (locationManager != null) {
			provider = locationManager.getBestProvider(criteria, true);
			locationManager.requestLocationUpdates(provider, locationUpdateTime, locationUpdateDistance, locationListener);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (locationManager != null) {
			locationManager.removeUpdates(locationListener);
		}
		if(this.followersThread!=null){
			this.followersThread.interrupt();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dbAdapter != null) {
			dbAdapter.close();
		}
	}

	private class GpsLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				Log.i(TAG, "location changed");
				// todo:
				double latitude=location.getLatitude();
				
				double longitude=location.getLongitude();
				
				Log.i(TAG, "Sended "+Double.valueOf(latitude).toString()+" "+Double.valueOf(longitude).toString());
				
				HashMap<String, String> params=new HashMap<String, String>();
				params.put(AC.HttpParamName.LATITUDE, Double.valueOf(latitude).toString());
				params.put(AC.HttpParamName.LONGITUDE, Double.valueOf(longitude).toString());
				params.put(AC.HttpParamName.FB_ID, userData.getId());
				
				RequestRunnable sendLocation=new RequestRunnable(Request.LOCATION_REQUEST, params, threadHandler);
				Thread t=new Thread(sendLocation);
				t.start();
				// you can now send current location to server and get response what resort you're in now
				// and display info in the header bar (opposite to small logo), smth like "Im in <resort_name>"
				// do everything in background thread
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	// notify map and list activity to update their ui when data is changed

	private Runnable notifyUpdateResorts = new Runnable() {
		public void run() {
			Intent intent = new Intent(UPDATE_ACTION);
			sendBroadcast(intent);
			handler.post(doHideProgressDlg);
		}
	};

	private Runnable doShowProgressDlg = new Runnable() {
		public void run() {
			if (progressDlg == null) {
				progressDlg = new ProgressDialog(SWMMainActivity.this);
				progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDlg.setMessage("Loading data..");
			}
			progressDlg.show();
		}
	};

	private Runnable doHideProgressDlg = new Runnable() {
		public void run() {
			if (progressDlg != null && progressDlg.isShowing()) {
				progressDlg.dismiss();
			}
		}
	};

	private Handler threadHandler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.arg1) {
			case AC.TI.RESORTS_THREAD:
				if (msg.what == AC.TR.THREAD_SUCCESS) {
					onGetResortsSuccess((ArrayList<Resort>) msg.obj);
				} else {
					onGetResortsFailed();
				}
				break;
			case AC.TI.LOCATION_THREAD:
				Log.i(TAG, "location completed");
				break;
				
			case AC.TI.FOLLOWERS_THREAD:
				
				if(msg.what==AC.TR.THREAD_SUCCESS){
					onFollowersGetSuccess((ArrayList<Follower>)msg.obj);
					Log.i(TAG, "followers completed");
				}else{
					Log.i(TAG, "followers failed");
				}
				
			default:
				break;
			}
		}

	};
	
	private void onFollowersGetSuccess(ArrayList<Follower> followers){
		Log.i(TAG, " sending followers broadcast");
		Intent intent=new Intent(USERS_UPDATE);
		intent.putExtra("followers", followers);
		sendBroadcast(intent);
	}

	private void onGetResortsSuccess(ArrayList<Resort> resorts) {
		Log.i(TAG, "get resorts success");
		this.resorts = resorts;
		try {
			dbAdapter.storeResorts(resorts);
			System.out.println("stored");
			handler.post(notifyUpdateResorts);
			handler.post(doHideProgressDlg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void onGetResortsFailed() {
		Log.i(TAG, "get resorts failed!");
	}

	// request to show slope
	public void onSlopeShowRequest(int resortId, int slopeId) {
		//Toast.makeText(this, "!!!", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(SWMMainActivity.MAP_ACTION);
		intent.putExtra("resorts", this.resorts);
		intent.putExtra("selected", resortId);
		intent.putExtra("slope", slopeId);
		sendBroadcast(intent);
	}

	// select resort listener
	public void onResortExpanded(int resortId) {
		this.selectedResort = resortId;
	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		int i = getTabHost().getCurrentTab();

		// if request to map
		if (i == 0) {
			// if some resort was selected
			Intent intent = new Intent(SWMMainActivity.MAP_ACTION);
			intent.putExtra("resorts", this.resorts);
			intent.putExtra("selected", selectedResort);
			sendBroadcast(intent);
			
			this.followersThread=new Thread(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(true&&!Thread.interrupted()){
						HashMap<String, String> params=new HashMap<String, String>();
						params.put(AC.HttpParamName.FB_ID, userData.getId());
						RequestRunnable followersRunnable=new RequestRunnable(Request.FOLLOWERS_REQUEST, params, threadHandler);
						Thread followersThread=new Thread(followersRunnable);
						followersThread.start();
						
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}			
				}
				
			};
			this.followersThread.start();
		}

	}

}
