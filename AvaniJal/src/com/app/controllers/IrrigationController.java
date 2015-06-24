package com.app.controllers;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.avanstart.R;
import com.app.beans.Children;

public class IrrigationController {
	
	static IrrigationController _instance;

	ArrayList<Children> configs;
	
	Activity cxt;

	private IrrigationController() {


	}
	public static IrrigationController getInstance() {

		if(_instance == null)
			_instance = new IrrigationController();
		return _instance;
	}
	
	public RelativeLayout getIrrigationLayout(ViewGroup cont ,Activity act) {
		
		this.cxt = act;
		LayoutInflater oldlinf = (LayoutInflater) act.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		RelativeLayout rel = (RelativeLayout) oldlinf.inflate(R.layout.activity_irrigation_list, cont, false);
		return rel;
	}

}
