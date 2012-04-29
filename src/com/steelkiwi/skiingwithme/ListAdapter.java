package com.steelkiwi.skiingwithme;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.steelkiwi.skiingwithme.data.Resort;
import com.steelkiwi.skiingwithme.data.Slope;

public class ListAdapter extends BaseExpandableListAdapter {
	private Context ctx;
	public ArrayList<Resort> groups;
    public ArrayList<ArrayList<Slope>> items;
    
    public ListAdapter(Context context) {
    	super();
    	ctx = context;
    	
    	groups = new ArrayList<Resort>();
    	items = new ArrayList<ArrayList<Slope>>();
    }
	
	@Override
	public Object getChild(int arg0, int arg1) {
		return items.get(arg0).get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return arg1;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3, ViewGroup arg4) {
		LinearLayout itemView;
		
		if (items == null) { return null; }
		if (items.get(arg0) == null) { return null; }
		
		Slope item = items.get(arg0).get(arg1);
		
		if (arg3 == null) {
			itemView = new LinearLayout(ctx);
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li = (LayoutInflater)ctx.getSystemService(inflater);
			li.inflate(R.layout.list_item, itemView);
		}
		else {
			itemView = (LinearLayout)arg3;
		}
		
		ImageView slopeIcon = (ImageView)itemView.findViewById(R.id.slopeIcon);
		//todo: download from server, item.getImage() returns path to download from
		
		TextView name = (TextView)itemView.findViewById(R.id.titleTxt);
		name.setText(item.getTitle());
		
		TextView length = (TextView)itemView.findViewById(R.id.lenTxt);
		length.setText("Length: " + item.getLength());
		
		TextView dif = (TextView)itemView.findViewById(R.id.difTxt);
		dif.setText("Difficulty: " + item.getDifficulty());
		
		return itemView;
	}

	@Override
	public int getChildrenCount(int arg0) {
		return items.get(arg0).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		LinearLayout groupView;
		
		if (groups == null) { return null; }
		if (groups.size() == 0) { return null; }
		
		Resort group = groups.get(groupPosition);
		
		if (convertView == null) {
			groupView = new LinearLayout(ctx);
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li = (LayoutInflater)ctx.getSystemService(inflater);
			li.inflate(R.layout.list_parent_item, groupView);
		}
		else {
			groupView = (LinearLayout)convertView;
		}
		
		TextView name = (TextView)groupView.findViewById(R.id.nameText);
		name.setText(group.getTitle());
		
		TextView addr = (TextView)groupView.findViewById(R.id.addrText);
		addr.setText(group.getCountry());
		
		return groupView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		 return true;
	}
    
	public void clearItems() {
		if (items != null) {
			for (int i=0; i<items.size(); i++) {
				if (items.get(i) != null) { items.get(i).clear(); }
			}
			items.clear();
		}	
		if (groups != null) { groups.clear(); }
	}
}