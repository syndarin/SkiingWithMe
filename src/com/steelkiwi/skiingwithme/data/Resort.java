package com.steelkiwi.skiingwithme.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Resort implements Serializable {
	private static final long serialVersionUID = 1;
	
	private int id;
	private String title;
	private String country;
	private String city;
	private ArrayList<Slope> slopes;
	private ArrayList<SWMPoint> bounds;
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public void setSlopes(ArrayList<Slope> slopes) {
		this.slopes = slopes;
	}
	
	public void setBounds(ArrayList<SWMPoint> bounds) {
		this.bounds = bounds;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public ArrayList<Slope> getSlopes() {
		return this.slopes;
	}
	
	public ArrayList<SWMPoint> getBounds() {
		return this.bounds;
	}
}
