package com.app.avaniadapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.avanstart.R;
import com.app.beans.CheckBoxItem;
import com.app.beans.IrriCropItems;
import com.app.interfaces.CheckboxInterface;
import com.app.interfaces.IrrigationInterface;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by vsivaraj on 05-07-2015.
 */
public class IrrigationListViewAdapter extends ArrayAdapter<String> {

	private Hashtable<String, String> fullitems;

	private ArrayList<String> items;
	Context cxt;
	IrrigationInterface delegate;

	ArrayList<String> viewHolder ;
	public int mode;

	public IrrigationListViewAdapter(Context context, int textViewResourceId, 
			Hashtable<String, String> cropsvalvelist, ArrayList<String> item, int mode ,IrrigationInterface _delegate) {
		//super(context, textViewResourceId);
		super(context, textViewResourceId, item);
		this.cxt = context;
		this.delegate = _delegate;
		this.fullitems = cropsvalvelist;//new Hashtable<String, String>();
		this.items = new ArrayList<String>(this.fullitems.keySet());
		this.mode = mode;
	}

	private class ViewHolder {
		TextView valvename;
		CheckBox checkbox;
		EditText timevolumevalue;
		TextView metric;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder ;



		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater)this.cxt.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.irrigationlistlayout, null);

			holder = new ViewHolder();
			holder.valvename = (TextView) convertView.findViewById(R.id.valvename);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkBox1);
			holder.timevolumevalue = (EditText) convertView.findViewById(R.id.timevolumevalue);
			holder.metric = (TextView)convertView.findViewById(R.id.metric);
			holder.checkbox.setTag(position);
			holder.timevolumevalue.setTag(position);
//			InputMethodManager imm = (InputMethodManager) this.cxt.getSystemService(Context.INPUT_METHOD_SERVICE);
//			imm.showSoftInput(holder.timevolumevalue, InputMethodManager.SHOW_IMPLICIT);
			convertView.setTag(holder);
			holder.checkbox.setOnClickListener( new View.OnClickListener() {  
				public void onClick(View v) {  
					CheckBox cb = (CheckBox) v ;  
					int val = (Integer)v.getTag();
					String country = (String) items.get(val);;
					String timeval = holder.timevolumevalue.getText().toString();
					//country.setChecked(cb.isChecked());
					if(timeval.equals("0") || timeval.length() < 1) {
						cb.setChecked(false);
						if(delegate != null) {
							delegate.showSelectError();
						}
					} else
						if(delegate != null) {
							if (cb.isChecked())  
								delegate.onItemSelected(country, timeval); 
							else	
								delegate.onItemDeSelected(country);

						}
				}  
			}); 
		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		String val = items.get(position);
		holder.valvename.setText(val);
		String num = fullitems.get(val);
		if(!num.equals("0") && num.length() > 0 ){
			holder.checkbox.setChecked(true);
		}else {
			holder.checkbox.setChecked(false);
		}
		holder.timevolumevalue.setText(num);
		if(mode == 1){
			/// time based
			holder.metric.setText("Min");
		}else if(mode == 2){
			holder.metric.setText("Lts/Min");
		}

		return convertView;

	}
}
