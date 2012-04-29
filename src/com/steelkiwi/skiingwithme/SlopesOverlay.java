package com.steelkiwi.skiingwithme;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class SlopesOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	private int color;

	public SlopesOverlay(Drawable defaultMarker, int color) {
		super(defaultMarker);
		// TODO Auto-generated constructor stub
		this.color = color;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		super.draw(canvas, mapView, false);

		Paint mPaint = new Paint();

		mPaint.setDither(true);
		mPaint.setColor(this.color);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(2);

		Path path = new Path();

		Projection projection = mapView.getProjection();

		Point startPoint = new Point();
		projection.toPixels(this.overlays.get(0).getPoint(), startPoint);
		path.moveTo(startPoint.x, startPoint.y);

		for (int i = 1; i < this.overlays.size(); i++) {

			Point currentPoint = new Point();
			projection.toPixels(this.overlays.get(i).getPoint(), currentPoint);
			path.lineTo(currentPoint.x, currentPoint.y);

		}

		path.lineTo(startPoint.x, startPoint.y);

		canvas.drawPath(path, mPaint);
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
	
	public void addOverlay(OverlayItem item) {
		this.overlays.add(item);
		this.populate();
	}

}
