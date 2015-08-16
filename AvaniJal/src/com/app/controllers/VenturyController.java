package com.app.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.app.avaniadapters.CheckboxListAdapter;
import com.app.avanstart.R;
import com.app.avanstart.util.AppUtils;
import com.app.beans.CheckBoxItem;
import com.app.beans.Elements;
import com.app.interfaces.CheckboxInterface;

public class VenturyController extends ElementsController implements CheckboxInterface{


	/// final motor holder
	LinearLayout venturyLinear;

	Context activity;

	ViewGroup container;

	RelativeLayout lin;

	ArrayList<CheckBoxItem> motorIds = new ArrayList<CheckBoxItem>();

	ArrayList<CheckBoxItem> valveIds = new ArrayList<CheckBoxItem>();

	ArrayList<String> pipelineIds = new ArrayList<String>();

	CheckboxListAdapter valveListadapter;



	public VenturyController() {


	}

	//	public static VenturyController getInstance() {
	//
	//		if(_instance == null)
	//			_instance = new VenturyController();
	//		return _instance;
	//
	//	}

	public void destruct() {
		motorIds = null;
		valveIds = null;
		pipelineIds = null;
		venturyLinear = null;
		_instance = null;
	}

	private void init(Context cxt) {

		/*
		 * send objects to the super class for addition and deletion
		 * 
		 * */

		if(AppUtils.confItems.getVenturyFertigationItems() == null)
			super.elements = new ArrayList<Elements>();
		else
		{
			super.elements = AppUtils.confItems.getVenturyFertigationItems();
		}
		/// adding a empty object so that the dropdowns doesnt show the first object
		motorIds = new ArrayList<CheckBoxItem>();

		valveIds = new ArrayList<CheckBoxItem>();

		pipelineIds = new ArrayList<String>();

		motorIds.clear();
		valveIds.clear();
		ArrayList<Elements> motors = AppUtils.confItems.getAllMotorItems();
		ArrayList<Elements> valves = AppUtils.confItems.getAllValveItems();
		ArrayList<Elements> pipeline = AppUtils.confItems.getAllPipelineItems();

		for (Elements elements : pipeline) {
			if(elements.getIsConfigured())
				pipelineIds.add(elements.getItemid());
		}

		for (Elements elements : motors) {

			if(elements.getIsConfigured()) {


				for (String id : AppUtils.FertigationMotors) {
					if(id.equals(elements.getItemid())){
						CheckBoxItem citem = new CheckBoxItem();
						citem.setType(AppUtils.MOTOR_TYPE);
						citem.setName(elements.getItemid());
						citem.setChecked(false);
						if(containsItem(id))
							citem.setChecked(true);

						motorIds.add(citem);
					}
				}
			}
		}
		for (Elements elements : valves) {
			if(elements.getIsConfigured()) {
				for (String id : AppUtils.VenturyValves) {
					if(id.equals(elements.getItemid())  ){

						for (String elements2 : pipelineIds) {

							String repStr = AppUtils.replaceLetter(id, AppUtils.VALVE_TYPE, AppUtils.PIPELINE_TYPE);
							if(repStr.equals(elements2)){
								CheckBoxItem citem = new CheckBoxItem();
								citem.setType(AppUtils.VALVE_TYPE);
								citem.setName(elements.getItemid());
								citem.setChecked(false);
								if(containsItem(id))
									citem.setChecked(true);
								valveIds.add(citem);
							}
						}
					}
				}
			}
		}
		type = AppUtils.VENTURY_TYPE;
		super.type = type;
		super.activity = cxt;

	}

	public RelativeLayout getVenturyLayout( ViewGroup con ,  Context act , LinearLayout llayout ) {


		this.container = con;
		this.activity = act;
		init(act);


		LayoutInflater oldlinf = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		lin = (RelativeLayout) oldlinf.inflate(R.layout.venturyconfigholder, container, false);
		//ListView ventury = (ListView) lin.findViewById(R.id.valveholder);
		RadioGroup venturygrp = (RadioGroup)lin.findViewById(R.id.venturyferti);
		venturygrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int pos) {
				// TODO Auto-generated method stub
				removeAllElements();
				if(pos == R.id.venturyradio) {
					showVentury();
				}else if( pos == R.id.fertiradio) {
					showFerti();
				}
			}
		});
		//ListView ferti = (ListView) lin.findViewById(R.id.fertiholder);


		//ferti.setAdapter(fertiListadapter);
		reloadUI();
		showVentury();
		return lin;
	}

	private void showVentury() {

		//LayoutInflater oldlinf = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		//RelativeLayout lin = (RelativeLayout) oldlinf.inflate(R.layout.venturyconfigholder, container, false);
		type = AppUtils.VENTURY_TYPE;
		super.type = type;
		ImageView venturyimg = (ImageView) lin.findViewById(R.id.venturyimg);
		ListView ventury = (ListView) lin.findViewById(R.id.valveholder);
		CheckboxListAdapter valveListadapter = new CheckboxListAdapter(this.activity, 0, valveIds, this);
		venturyimg.setImageResource(R.drawable.ventury);
		venturyimg.invalidate();
		ventury.setAdapter(valveListadapter);

	}

	private void showFerti() {
		//LayoutInflater oldlinf = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		//RelativeLayout lin = (RelativeLayout) oldlinf.inflate(R.layout.venturyconfigholder, container, false);
		type = AppUtils.FERTIGATION_TYPE;
		super.type = type;
		ImageView venturyimg = (ImageView) lin.findViewById(R.id.venturyimg);
		ListView ventury = (ListView) lin.findViewById(R.id.valveholder);
		CheckboxListAdapter valveListadapter = new CheckboxListAdapter(this.activity, 0, motorIds, this);
		venturyimg.setImageResource(R.drawable.fertimotor);
		venturyimg.invalidate();
		ventury.setAdapter(valveListadapter);
	}

	@Override
	public void destructController() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reloadUI() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearUI() {
		// TODO Auto-generated method stub

	}

	@Override
	public void buildUI(Elements eitem) {
		// TODO Auto-generated method stub

	}
	public Boolean containsItem( String id ){
		Boolean contains = false;
		for (Elements ele : elements) {

			if(ele.getItemid().equals(id)){
				contains = true;
				break;
			}
		}
		return contains;
	}

	public void deleteItem( String id ) {
		if(elements != null){
			Iterator<Elements> iter = elements.iterator();
			while(iter.hasNext()){
				Elements ele = iter.next();
				if(ele.getItemid().equals(id)){
					iter.remove();
					break;
				}
			}
		}
	}

	@Override
	public void itemSelected(Object c) {
		// TODO Auto-generated method stub
		CheckBoxItem cItem = (CheckBoxItem)c;
		if(!containsItem(cItem.getName())) {
			Elements ele = new Elements();
			ele.setItemid(cItem.getName());
			ele.setType(cItem.getType());
			elements.add(ele);
		}

	}

	@Override
	public void itemDeselected(Object c) {
		// TODO Auto-generated method stub
		CheckBoxItem cItem = (CheckBoxItem)c;
		if(containsItem(cItem.getName())){
			deleteItem(cItem.getName());
		}
	}

}
