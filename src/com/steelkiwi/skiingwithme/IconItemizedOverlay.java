package com.steelkiwi.skiingwithme;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.steelkiwi.skiingwithme.data.Resort;

public class IconItemizedOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();

	public IconItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return this.overlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return overlays.size();
	}
	
	
	
//	@Override
//	public boolean onTap(GeoPoint p, MapView mapView) {
//		// TODO Auto-generated method stub
//		MapController mc=mapView.getController();
//		mc.setCenter(p);		
//		return true;
//	}

	public void addOverlay(OverlayItem item) {
		this.overlays.add(item);
		this.populate();
	}

}
