package com.steelkiwi.skiingwithme;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.app.ExpandableListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.steelkiwi.skiingwithme.data.DBAdapter;
import com.steelkiwi.skiingwithme.data.Resort;
import com.steelkiwi.skiingwithme.data.Slope;

public class SWMListActivity extends ExpandableListActivity {
	private DBAdapter dbAdapter;
	
	private BroadcastReceiver broadcasrReceiver;
	

	private ListAdapter listAdapter;
	private ExpandableListView listView;
	private int selectedResort;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        
        listView = (ExpandableListView) findViewById(android.R.id.list);
        listAdapter = new ListAdapter(this);
        listView.setAdapter(listAdapter);
        
        listView.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				selectedResort=groupPosition;
				collapseOtherGroups(groupPosition);
			}
        });
        
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        
        broadcasrReceiver = new BroadcastReceiver() {
    		@Override
    		public void onReceive(Context context, Intent intent) {
    			updateWithNewResorts();
    		}
        };
	}
	
	
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		SWMMainActivity parentActivity=(SWMMainActivity)getParent();
		parentActivity.onSlopeShowRequest(groupPosition, childPosition);
		return true;
	}



	@Override
    public void onResume() {
    	super.onResume();
    	IntentFilter filter = new IntentFilter();
        filter.addAction(SWMMainActivity.UPDATE_ACTION);
        registerReceiver(broadcasrReceiver, filter);
    }
	
	@Override
    public void onPause() {
    	super.onPause();
    	unregisterReceiver(broadcasrReceiver);
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	if (dbAdapter != null) { dbAdapter.close(); }
    }
    
    private void updateWithNewResorts() {
		listAdapter.clearItems();
		
		List<Resort> resortsFromDB;
		try {
			resortsFromDB = dbAdapter.restoreResorts();
			
			if (resortsFromDB == null) {
				listAdapter.notifyDataSetChanged();
				return;
			}
			else {
				for(Resort resort : resortsFromDB) {
					listAdapter.groups.add(resort);
					
					List<Slope> slopes = resort.getSlopes();
					if ((slopes != null) && (slopes.size() > 0)) {
						listAdapter.items.add((ArrayList<Slope>) slopes);
					}
				}
				listAdapter.notifyDataSetChanged();
			}
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    private void collapseOtherGroups(int groupPosition) {
    	for (int i=0; i<listAdapter.groups.size(); i++) {
    		if (i == groupPosition) { continue; }
    		if (listView.isGroupExpanded(i)) {
    			listView.collapseGroup(i);
    		}
    	}
    }
}
