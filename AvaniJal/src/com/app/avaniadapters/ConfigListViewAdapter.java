package com.app.avaniadapters;

import java.util.ArrayList;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.avanstart.R;
import com.app.avanstart.util.AppUtils;
import com.app.beans.Children;
import com.app.beans.ConfigGroup;
import com.app.beans.ConfigStatus;
import com.app.interfaces.expandedlistinterfaces;

public class ConfigListViewAdapter extends BaseAdapter{

	public LayoutInflater inflater;
	public expandedlistinterfaces delegate;
	private  ArrayList<Children> groups;
	Hashtable<String, ConfigStatus> cghash ;

	public ConfigListViewAdapter(Context act , ArrayList<Children> groups , expandedlistinterfaces del) {

		this.groups = groups;
		this.delegate = del;
		cghash = AppUtils.confItems.elementConfigstatus;
		inflater = (LayoutInflater) act.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.groups.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Children children = (Children) groups.get(pos);
		final int position = pos;
		TextView text = null;
		TextView statustext = null;
		ImageView elementImage = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_configlistitems, null);
		}
		text = (TextView) convertView.findViewById(R.id.configitemlbl);
		statustext = (TextView) convertView.findViewById(R.id.configitemstatus);
		elementImage = (ImageView) convertView.findViewById(R.id.groupimageHolder);

		text.setText(children.title);
		ConfigStatus cg = cghash.get(children.title);
		if(children.status != null){
			statustext.setVisibility(View.VISIBLE);
			statustext.setText(cg.configDescription);
		}
		if(children.img != 0){
			elementImage.setVisibility(View.VISIBLE);
			elementImage.setImageDrawable(elementImage.getResources().getDrawable(children.img));
		}

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* Toast.makeText(activity, children.title,
	            Toast.LENGTH_SHORT).show();*/
				delegate.listclicked(position);
			}
		});
		return convertView;
	}

}
