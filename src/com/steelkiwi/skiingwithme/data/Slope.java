package com.steelkiwi.skiingwithme.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Slope implements Serializable {
	private static final long serialVersionUID = 1;
	
	private int id;
	private String title;
	private String difficulty;
	private int color;
	private String img;
	private double length;
	private ArrayList<SWMPoint> coords;
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public void setImage(String img) {
		this.img = img;
	}
	
	public void setLength(double length) {
		this.length = length;
	}
	
	public void setCoords(ArrayList<SWMPoint> coords) {
		this.coords = coords;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getDifficulty() {
		return this.difficulty;
	}
	
	public int getColor() {
		return this.color;
	}
	
	public String getImage() {
		return this.img;
	}
	
	public double getLength() {
		return this.length;
	}
	
	public ArrayList<SWMPoint> getCoords() {
		return this.coords;
	}
}
