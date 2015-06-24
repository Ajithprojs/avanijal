package com.app.avaniadapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.avanstart.R;
import com.app.beans.CropItem;

public class CropsGridViewAdapter extends ArrayAdapter<CropItem> {


	Context context;
	int layoutResourceId;
	ArrayList<CropItem> data = new ArrayList<CropItem>();

	public CropsGridViewAdapter(Context context, 
			int textViewResourceId, ArrayList<CropItem> objects) {
		// TODO Auto-generated constructor stub
		super(context, textViewResourceId, objects);
		this.layoutResourceId = textViewResourceId;
		this.context = context;
		this.data = objects;


	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
		  RecordHolder holder = null;
		  if (row == null) {
		   LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		   row = inflater.inflate(layoutResourceId, parent, false);
		   holder = new RecordHolder();
		   holder.txtTitle = (TextView) row.findViewById(R.id.item_title);
		   holder.imageItem = (ImageButton) row.findViewById(R.id.item_button);
		   row.setTag(holder);
		  } else {
		   holder = (RecordHolder) row.getTag();
		  }
		  CropItem item = data.get(position);
		  holder.txtTitle.setText(item.cropTitle);
		  holder.imageItem.setImageResource(item.imgId);
		  return row;
	}

	static class RecordHolder {
		TextView txtTitle;
		ImageButton imageItem;

	}


}
