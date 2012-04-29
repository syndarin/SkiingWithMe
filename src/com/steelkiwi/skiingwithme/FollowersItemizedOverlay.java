package com.steelkiwi.skiingwithme;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class FollowersItemizedOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	private Context context;

	public FollowersItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
		this.context=context;
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
	protected boolean onTap(int arg0) {
		// TODO Auto-generated method stub
		UserDialog dialog=new UserDialog(this.context, this.overlays.get(arg0));
		dialog.show();
		return true;
	}

	public void addOverlay(OverlayItem item) {
		this.overlays.add(item);
		this.populate();
	}
	
	private class UserDialog extends Dialog{
		
		private OverlayItem item;
		
		public UserDialog(Context context, OverlayItem item) {
			super(context);
			// TODO Auto-generated constructor stub
			this.item=item;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.user_dialog);
			ImageView userpic=(ImageView)findViewById(R.id.userIcon);
			TextView userName=(TextView)findViewById(R.id.textUserName);
			LinearLayout root=(LinearLayout)findViewById(R.id.dialogRoot);
			root.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
			userName.setText(this.item.getTitle());
			try {
				Log.i("dialog", this.item.getSnippet());
				URL picUrl=new URL(this.item.getSnippet());
				InputStream is=picUrl.openConnection().getInputStream();
				Bitmap bm=BitmapFactory.decodeStream(is);
				if(bm!=null){
					userpic.setImageBitmap(bm);
				}else{
					userpic.setImageResource(R.drawable.skier);
				}
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		
	}

}
