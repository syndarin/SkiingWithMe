package com.steelkiwi.skiingwithme;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.steelkiwi.skiingwithme.data.Follower;
import com.steelkiwi.skiingwithme.data.Resort;
import com.steelkiwi.skiingwithme.data.SWMPoint;
import com.steelkiwi.skiingwithme.data.Slope;

public class SWMMapActivity extends MapActivity {

	private BroadcastReceiver mapBroadcastReceiver;
	private BroadcastReceiver usersBroadcastReceiver;
	private MapView map;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		this.map = (MapView) findViewById(R.id.mapView);
		this.map.setBuiltInZoomControls(true);

		this.mapBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				updateMap(intent);
			}
		};
		
		this.usersBroadcastReceiver=new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				updateUsers(intent);
			}
		};
	}
	
	private void updateUsers(Intent intent){
		Log.i("user update", "followers updating");
		ArrayList<Follower> followers=(ArrayList<Follower>)intent.getSerializableExtra("followers");
		Drawable userIcon=getResources().getDrawable(R.drawable.skier);
		
		List<Overlay> mapOverlays=this.map.getOverlays();
		for(int i=0; i<mapOverlays.size(); i++){
			if(mapOverlays.get(i) instanceof FollowersItemizedOverlay){
				mapOverlays.remove(i);
			}
		}
		
		
		FollowersItemizedOverlay followersOverlay=new FollowersItemizedOverlay(userIcon, this);
		
		for(Follower fol:followers){
			GeoPoint gPoint=new GeoPoint((int)fol.getLatitude(), (int)fol.getLongitude());
			OverlayItem oItem=new OverlayItem(gPoint, fol.getFirstName()+" "+fol.getLastName(), fol.getAvatar());
			followersOverlay.addOverlay(oItem);
		}
		
		mapOverlays.add(followersOverlay);
		map.invalidate();
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void updateMap(Intent intent) {
		ArrayList<Resort> resorts = (ArrayList<Resort>) intent.getSerializableExtra("resorts");
		int selectedResort = intent.getIntExtra("selected", -1);
		int selectedSlope = intent.getIntExtra("slope", -1);

		List<Overlay> mapOverlays = map.getOverlays();

		// filling all resortes
		Drawable icon = getResources().getDrawable(R.drawable.ic_slope);
		Drawable logo = getResources().getDrawable(R.drawable.logo_small);

		for (Resort resort : resorts) {
			BoundItemizedOverlay boundsOverlay = new BoundItemizedOverlay(icon);

			ArrayList<SWMPoint> bounds = resort.getBounds();
			// resort borders
			for (SWMPoint point : bounds) {
				GeoPoint gPoint = new GeoPoint(point.getLat(), point.getLng());
				OverlayItem oItem = new OverlayItem(gPoint, "", "");
				boundsOverlay.addOverlay(oItem);
			}
			// slopes borders
			for (Slope slope : resort.getSlopes()) {
				SlopesOverlay so = new SlopesOverlay(icon, slope.getColor());
				for (SWMPoint sPoint : slope.getCoords()) {
					GeoPoint slopeGeo = new GeoPoint(sPoint.getLat(), sPoint.getLng());
					OverlayItem slopeItem = new OverlayItem(slopeGeo, "", "");
					so.addOverlay(slopeItem);
				}
				mapOverlays.add(so);
			}
			// resort icons
			IconItemizedOverlay iconOverlay = new IconItemizedOverlay(logo);
			GeoPoint gPoint = this.getCenterResort(resort.getBounds());
			OverlayItem oItem = new OverlayItem(gPoint, "", "");
			iconOverlay.addOverlay(oItem);

			mapOverlays.add(iconOverlay);
			mapOverlays.add(boundsOverlay);
		}

		MapController mc = map.getController();

		// if some resort selected
		if (selectedResort != -1) {
			// slope was selected
			if (selectedSlope != -1) {
				Log.i("map", "res+slope");
				GeoPoint center=this.getCenterResort(resorts.get(selectedResort).getSlopes().get(selectedSlope).getCoords());
				mc.setCenter(center);
				mc.zoomToSpan(center.getLatitudeE6(), center.getLongitudeE6());
			} else {
				Log.i("map", "resort selected, no slope");
				GeoPoint center=this.getCenterResort(resorts.get(selectedResort).getBounds());
				mc.setCenter(center);
				mc.zoomToSpan(center.getLatitudeE6(), center.getLongitudeE6());
			}

		}
		// no resort selected
		else {
			Log.i("map", "no resort selected");
			GeoPoint firstPoint = this.getCenterResort(resorts.get(0).getBounds());
			mc.setCenter(firstPoint);

		}
	}

	private GeoPoint getCenterResort(ArrayList<SWMPoint> bounds) {
		int sumLat = 0, sumLong = 0;
		for (SWMPoint point : bounds) {
			sumLat += point.getLat();
			sumLong += point.getLng();
		}
		sumLat /= bounds.size();
		sumLong /= bounds.size();
		return new GeoPoint(sumLat, sumLong);
	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction(SWMMainActivity.MAP_ACTION);
		registerReceiver(mapBroadcastReceiver, filter);
		
		IntentFilter filter1=new IntentFilter();
		filter1.addAction(SWMMainActivity.USERS_UPDATE);
		registerReceiver(usersBroadcastReceiver, filter1);
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(mapBroadcastReceiver);
	}

}
