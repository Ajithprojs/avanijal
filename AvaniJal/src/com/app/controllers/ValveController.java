package com.app.controllers;

import java.util.Hashtable;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.avanstart.R;
import com.app.beans.ValveItems;

public class ValveController {
	
	
	static ValveController _instance;

	Hashtable<String, ValveItems> valves;

	private ValveController() {


	}

	public static ValveController getInstance() {

		if(_instance == null)
			_instance = new ValveController();

		return _instance;

	}
	
	public RelativeLayout createValveLayout(ViewGroup container , LinearLayout llayout , Activity activity) {
		
		//llayout.removeAllViews();
		valves = new Hashtable<String, ValveItems>();

			ValveItems fitem = new ValveItems();
			fitem.valveId = 1;
			LayoutInflater linf = activity.getLayoutInflater();
			RelativeLayout relativ = (RelativeLayout)linf.inflate(R.layout.valveconfiguration, container, false);

			return relativ;
		
	}

}
