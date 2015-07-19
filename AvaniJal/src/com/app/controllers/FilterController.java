package com.app.controllers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.avanstart.R;
import com.app.avanstart.util.AppUtils;
import com.app.beans.ConfigItem;
import com.app.beans.Elements;
import com.app.beans.FilterItem;
import com.app.beans.MotorItem;
import com.app.beans.Pipelineitem;


public class FilterController extends ElementsController {


	static FilterController _instance;

	ArrayList<String> pipelineIds = new ArrayList<String>();
	RelativeLayout relativ;

	Context activity;

	ViewGroup container;

	int MAX_FILTERS;

	int filterVal;

	String type;

	/// final motor holder
	LinearLayout filterLinear;

	RelativeLayout lin;

	Boolean disableDropDown = false;

	Button addFilterBtn;


	/// command types
	private int HOUR_COMMAND = 1;

	private int MIN_COMMAND = 2;

	private int SECOND_COMMAND = 3;

	boolean disablaDropDown = false;

	private FilterController() {


	}

	public static FilterController getInstance() {

		if(_instance == null)
			_instance = new FilterController();

		return _instance;

	}

	private void init(Context cxt) {

		/*
		 * send objects to the super class for addition and deletion
		 * 
		 * */

		if(AppUtils.confItems.getFilterItems() == null)
			this.elements = new ArrayList<Elements>();
		else
		{
			this.elements = AppUtils.confItems.getFilterItems();

		}
		pipelineIds.clear();
		//pipelineIds.add(" ");
		ArrayList<Elements> pipeline = AppUtils.confItems.getPipelineItems();

		for (Elements elements : pipeline) {
			if(elements.getIsConfigured())
				pipelineIds.add(elements.getItemid());
		}
		MAX_FILTERS = pipelineIds.size();
		filterVal = 0;
		type = AppUtils.FILTER_TYPE;
		super.max = MAX_FILTERS;
		super.type = type;
		super.activity = cxt;

	}


	public RelativeLayout createFilterLayout( ViewGroup container , LinearLayout llayout , Context activity , int numberoffilters) {

		this.activity = activity;
		this.container = container;
		init(activity);

		LayoutInflater oldlinf = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		lin = (RelativeLayout) oldlinf.inflate(R.layout.filterconfigholder, container, false);
		filterLinear = (LinearLayout)lin.findViewById(R.id.filterlayout);

		addFilterBtn = (Button)lin.findViewById(R.id.addfilterbtn);
		addFilterBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addFilter();
			}
		});
		reloadUI();
		return lin;
	}

	public void addFilter() {

		if(MAX_FILTERS == filterVal){
			super.showDialog("Filters", "We can have only " +MAX_FILTERS+" filters");
		}else {

			FilterItem pitem = new FilterItem();
			disableDropDown = false;
			filterVal++;
			super.addElement(""+filterVal, pitem);
			//AppUtils.confItems.setPipelineItems(filters);
			AppUtils.confItems.setFilterItems(this.elements);
		}

	}

	public void deleteFilter(String id ) {

		setButtonsSync();
		filterVal--;
		super.deleteElement(id);

	}

	public void setButtonsSync() {

		addFilterBtn.setText("Add Filter ("+(MAX_FILTERS - filterVal)+")");

	}
	private int getFilterInt( String filterId ){

		int indx = type.length();
		int num = Integer.parseInt(filterId.substring(indx, indx+1));
		return num;
	}

	public void setRemovePipelineIDfromArray( String id ){

		if(pipelineIds != null){
			Iterator<String> iter = pipelineIds.iterator();
			while(iter.hasNext()){
				String str = iter.next();
				if(str.equals(id)){
					iter.remove();
					break;
				}
			}
		}
	}
	public void setAddPipelineToArray( String id ) {
		pipelineIds.add(id);
	}

	private void addSelectedPipe( FilterItem filter , String id ) {

		ArrayList<String> pipes = filter.getAllAssociatedElementsOfType(type);
		if(!pipes.contains(id)){
			pipes.add(id);
			setRemovePipelineIDfromArray(id);
		} else {
			pipes.remove(id);
			setAddPipelineToArray(id);
		}
	}

	@Override
	public void buildUI(Elements eitem) {
		// TODO Auto-generated method stub
		FilterItem fitem = (FilterItem)eitem;
		LayoutInflater linf = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		relativ = (RelativeLayout)linf.inflate(R.layout.filterconfiguration, container, false);

		Spinner numberoffiltersSpinner = (Spinner)relativ.findViewById(R.id.motorfilterspinner);
		//Spinner freqhoursspinner = (Spinner)relativ.findViewById(R.id.filterfrequencyhours);
		final EditText freqminutesspinner = (EditText)relativ.findViewById(R.id.filterfrequencyminutes);
		Spinner durationsecondsspinner = (Spinner)relativ.findViewById(R.id.filterdurationseconds);
		final Button deleteBtn = (Button)relativ.findViewById(R.id.delebtn);
		final TextView filterid  = (TextView)relativ.findViewById(R.id.filternumtxt);

		numberoffiltersSpinner.setTag(fitem.getItemid());
		//freqhoursspinner.setTag(fitem.getItemid());
		freqminutesspinner.setTag(fitem.getItemid());
		durationsecondsspinner.setTag(fitem.getItemid());
		relativ.setTag(fitem.getItemid());
		deleteBtn.setTag(fitem.getItemid());
		filterid.setText("Filter "+fitem.getItemid());

		//freqminutesspinner.setSelection(fitem.getFrequencyminutes());
		freqminutesspinner.setText(""+fitem.getFrequencyminutes());
		durationsecondsspinner.setSelection(fitem.getDurationSeconds());
		numberoffiltersSpinner.setSelection(1+getAssociatedId(eitem.getItemid()));

		setButtonsSync();

		deleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//deleteElement((String)deleteBtn.getTag() , pipeLinear);
				if(!disableDropDown)
					deleteFilter((String)deleteBtn.getTag());
			}
		});

		if (pipelineIds.size() > 0) {

			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, pipelineIds);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			numberoffiltersSpinner.setAdapter(dataAdapter);

		}

		numberoffiltersSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				if(!disableDropDown)
					setMotorId((String)arg0.getTag(), pos);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		//		freqhoursspinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		//			@Override
		//			public void onItemSelected(AdapterView<?> arg0, View arg1,
		//					int pos, long arg3) {
		//				// TODO Auto-generated method stub
		//				setCommand((String)arg0.getTag(), pos + 1, HOUR_COMMAND);
		//			}
		//
		//			@Override
		//			public void onNothingSelected(AdapterView<?> arg0) {
		//				// TODO Auto-generated method stub
		//
		//			}
		//		});

		//		freqminutesspinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		//			@Override
		//			public void onItemSelected(AdapterView<?> arg0, View arg1,
		//					int pos, long arg3) {
		//				// TODO Auto-generated method stub
		//				if(!disableDropDown)
		//				setCommand((String)arg0.getTag(), pos + 1, MIN_COMMAND);
		//			}
		//
		//			@Override
		//			public void onNothingSelected(AdapterView<?> arg0) {
		//				// TODO Auto-generated method stub
		//
		//			}
		//		});

		freqminutesspinner.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length() > 1){
					FilterItem mItem = getPipelineObjectforTag((String)freqminutesspinner.getTag());
					if(mItem != null)
						//mItem.setMaxVolts(Editmaxvoltage.getText().toString());
						mItem.setFrequencyminutes(Integer.parseInt(freqminutesspinner.getText().toString()));
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		durationsecondsspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				if(!disableDropDown)
					setCommand((String)arg0.getTag(), pos + 1, SECOND_COMMAND);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		disablaDropDown = false;
		filterLinear.addView(relativ);
	}

	public void setMotorId( String filterName , int val ) {

		FilterItem mItem = getPipelineObjectforTag(filterName);
		//mItem.setAssociatedElement(motorIds.get(val));
		mItem.setAssociatedElement(pipelineIds.get(val));

	}

	public int getAssociatedId( String filterName   ) {
		FilterItem mItem = getPipelineObjectforTag(filterName);
		ArrayList<String> items = mItem.getAllAssociatedElementsOfType(AppUtils.PIPELINE_TYPE);
		if(items.size() > 0){
			String assoId = items.get(0);
			Iterator<String> ids = pipelineIds.iterator();
			int j = 0;
			while(ids.hasNext()) {

				String id = ids.next();
				if(id.equals(assoId)){
					return j;

				}
				j++;
			}
		}

		return 0;
	}

	private FilterItem getPipelineObjectforTag( String filterName ) {

		FilterItem mt = null;
		for (Elements mitem : elements) {
			if(mitem.getItemid().equalsIgnoreCase(filterName))
				mt = (FilterItem)mitem;
		}
		return mt;

	}

	void setCommand(String filterName , int val , int cmd){

		FilterItem mItem = getPipelineObjectforTag(filterName);
		switch (cmd) {
		case 1:mItem.setFrequencyHours(val);break;
		case 2 :mItem.setFrequencyminutes(val);break;
		default:mItem.setDurationSeconds(val);break;

		}
	}

	@Override
	public void reloadUI() {
		// TODO Auto-generated method stub
		clearUI();
		ArrayList<Elements> tempFilters = (ArrayList<Elements>)elements.clone();
		for (Elements mt : tempFilters) {

			disableDropDown = true;
			if(mt.getIsConfigured()) {
				addElement(""+getFilterInt(mt.getItemid()), mt);
				filterVal++;
			}
		}
		setButtonsSync();
	}

	@Override
	public void clearUI() {
		// TODO Auto-generated method stub
		if(filterLinear != null) {
			filterLinear.removeAllViews();
		}
	}

	@Override
	public void destructController() {
		// TODO Auto-generated method stub


		pipelineIds = null;

		/// holds all the motor elements
		elements = null;

		filterLinear = null;

		lin = null;

		_instance = null;
	}




}
