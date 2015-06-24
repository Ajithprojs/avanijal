package com.app.controllers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.app.avanstart.R;
import com.app.avanstart.util.AppUtils;
import com.app.beans.ConfigItem;
import com.app.beans.FilterItem;
import com.app.beans.MotorItem;


public class FilterController {


	static FilterController _instance;

	Hashtable<String, FilterItem> filters;

	List<String> motornames;
	List<String> motorIds;
	ArrayAdapter<String> dataAdapter;
	RelativeLayout relativ;

	LinearLayout mainLayout;

	Context activity;

	ViewGroup container;

	/// command types
	private int HOUR_COMMAND = 1;

	private int MIN_COMMAND = 2;

	private int SECOND_COMMAND = 3;

	private FilterController() {


	}

	public static FilterController getInstance() {

		if(_instance == null)
			_instance = new FilterController();

		return _instance;

	}


	public LinearLayout createFilterLayout( ViewGroup container , LinearLayout llayout , Context activity , int numberoffilters) {

		this.activity = activity;
		this.container = container;

		if(AppUtils.confItems.filterItems == null)
			filters = new Hashtable<String, FilterItem>();
		else
			filters = AppUtils.confItems.filterItems;

		mainLayout = new LinearLayout(activity);
		LayoutParams LLParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		mainLayout.setLayoutParams(LLParams);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		loadMotorData();

		for (int i = 0; i < numberoffilters; i++) {

			FilterItem fitem = new FilterItem();
			fitem.itemId = "filter"+(i+1);
			if(filters.containsKey("filter"+(i+1))) {
				fitem = (FilterItem)filters.get("filter"+(i+1));
			}

			buildUI(fitem , i);
		}
		AppUtils.confItems.filterItems = filters;
		return mainLayout;
	}

	private void buildUI( FilterItem fItem , int i ) {


		LayoutInflater linf = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		relativ = (RelativeLayout)linf.inflate(R.layout.filterconfiguration, container, false);

		Spinner numberoffiltersSpinner = (Spinner)relativ.findViewById(R.id.motorfilterspinner);
		Spinner freqhoursspinner = (Spinner)relativ.findViewById(R.id.filterfrequencyhours);
		Spinner freqminutesspinner = (Spinner)relativ.findViewById(R.id.filterfrequencyminutes);
		Spinner durationsecondsspinner = (Spinner)relativ.findViewById(R.id.filterdurationseconds);

		numberoffiltersSpinner.setTag("filter"+(i+1));
		freqhoursspinner.setTag("filter"+(i+1));
		freqminutesspinner.setTag("filter"+(i+1));
		durationsecondsspinner.setTag("filter"+(i+1));


		if (motornames.size() > 0) {

			dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, motornames);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			numberoffiltersSpinner.setAdapter(dataAdapter);

		}

		numberoffiltersSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				setMotorId((String)arg0.getTag(), pos);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		freqhoursspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				setCommand((String)arg0.getTag(), pos + 1, HOUR_COMMAND);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		freqminutesspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				setCommand((String)arg0.getTag(), pos + 1, MIN_COMMAND);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		durationsecondsspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				setCommand((String)arg0.getTag(), pos + 1, SECOND_COMMAND);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		filters.put("filter"+(i+1), fItem);
		mainLayout.addView(relativ);
	}

	public void setMotorId( String filterName , int val ) {

		FilterItem mItem = filters.get(filterName);
		mItem.setAssociatedElement(motorIds.get(val));

	}

	void setCommand(String filterName , int val , int cmd){

		FilterItem mItem = filters.get(filterName);
		switch (cmd) {
		case 1:mItem.setFrequencyHours(""+val);break;
		case 2 :mItem.setFrequencyminutes(""+val);break;
		default:mItem.setDurationSeconds(""+val);break;

		}
	}

	public void loadMotorData() {

		motornames = new ArrayList<String>() ;
		motorIds = new ArrayList<String>() ;

		ConfigItem items = AppUtils.confItems;

		ArrayList<MotorItem> motorItems = new ArrayList<MotorItem>();//(ArrayList<MotorItem>)items.motorItems;

		if(motorItems != null){

			for (MotorItem mt : motorItems) {	

				if(mt.getPumpName() != null) {

					if(mt.getPumpName().length() > 1){
						motornames.add(mt.getPumpName());
					}else{
						motornames.add(mt.itemId);
					}

					motorIds.add(mt.itemId);
				}
			}
		}
	}

}
