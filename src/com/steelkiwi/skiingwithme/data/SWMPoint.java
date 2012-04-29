package com.steelkiwi.skiingwithme.data;

import java.io.Serializable;

public class SWMPoint implements Serializable {
	private static final long serialVersionUID = 1;
	
	private int lat;
	private int lng;
	
	

	public SWMPoint() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SWMPoint(int lat, int lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}
	
	public void setLng(int lng) {
		this.lng = lng;
	}
	
	public int getLat() {
		return lat;
	}
	
	public int getLng() {
		return lng;
	}
}
