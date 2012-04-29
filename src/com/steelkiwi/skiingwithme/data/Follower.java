package com.steelkiwi.skiingwithme.data;

import java.io.Serializable;

public class Follower implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String id;
	private String firstName;
	private String lastName;
	private String avatar;
	private double latitude;
	private double longitude;
	
	public Follower(String id, String firstName, String lastName, String avatar, double latitude, double longitude) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.avatar = avatar;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Follower() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}	
}
