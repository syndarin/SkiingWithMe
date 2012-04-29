package com.steelkiwi.skiingwithme;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;
import com.steelkiwi.skiingwithme.data.Resort;
import com.steelkiwi.skiingwithme.data.SWMPoint;
import com.steelkiwi.skiingwithme.data.Slope;

public class BoundItemizedOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();

	public BoundItemizedOverlay(Drawable defaultMarker) {
		super(defaultMarker);
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

	@Override
	public void draw(Canvas arg0, MapView arg1, boolean arg2) {
		// TODO Auto-generated method stub
		super.draw(arg0, arg1, false);

		Paint mPaint = new Paint();
		
		int strokeColor=Color.argb(128, 192, 0, 0);
		
		mPaint.setDither(true);
		mPaint.setColor(strokeColor);
		mPaint.setAntiAlias(true); 
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(2);

		Path path = new Path();

		Projection projection = arg1.getProjection();

		Point startPoint = new Point();
		projection.toPixels(this.overlays.get(0).getPoint(), startPoint);
		path.moveTo(startPoint.x, startPoint.y);

		for (int i = 1; i < this.overlays.size(); i++) {

			Point currentPoint = new Point();
			projection.toPixels(this.overlays.get(i).getPoint(), currentPoint);
			path.lineTo(currentPoint.x, currentPoint.y);

		}
		
		path.lineTo(startPoint.x, startPoint.y);

		arg0.drawPath(path, mPaint);
	}
	
	public void addOverlay(OverlayItem item) {
		this.overlays.add(item);
		this.populate();
	}

}
