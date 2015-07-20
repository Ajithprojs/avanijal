package com.app.avaniadapters;

import java.util.ArrayList;

import com.app.avanstart.R;
import com.app.beans.CheckBoxItem;
import com.app.interfaces.CheckboxInterface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class CheckboxListAdapter extends ArrayAdapter<CheckBoxItem> {

	private ArrayList<CheckBoxItem> valueList;
	Context cxt;
	CheckboxInterface delegate;

	public CheckboxListAdapter(Context context, int textViewResourceId, 
			ArrayList<CheckBoxItem> countryList, CheckboxInterface _delegate) {
		super(context, textViewResourceId, countryList);
		this.cxt = context;
		this.delegate = _delegate;
		this.valueList = new ArrayList<CheckBoxItem>();
		this.valueList.addAll(countryList);
	}

	private class ViewHolder {
		TextView tv;
		CheckBox name;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater)this.cxt.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.checkboxlistlayout, null);

			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.stringvalue);
			holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
			holder.name.setTag(position);
			convertView.setTag(holder);

			holder.name.setOnClickListener( new View.OnClickListener() {  
				public void onClick(View v) {  
					CheckBox cb = (CheckBox) v ;  
					int val = (Integer)v.getTag();
					CheckBoxItem country = (CheckBoxItem) valueList.get(val);;  
					country.setChecked(cb.isChecked());
					if(delegate != null) {
						if (cb.isChecked())  
							delegate.itemSelected(country); 
						else	
							delegate.itemDeselected(country);

					}
				}  
			});  
		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		CheckBoxItem val = valueList.get(position);
		holder.tv.setText(val.getName());
		holder.name.setChecked(val.isChecked());

		return convertView;

	}

}
