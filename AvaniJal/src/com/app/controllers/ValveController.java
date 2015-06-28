package com.app.controllers;

import java.util.Hashtable;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.avanicomponents.MultiSelectionSpinner;
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
			fitem.setValveId(1) ;
			LayoutInflater linf = activity.getLayoutInflater();
			RelativeLayout relativ = (RelativeLayout)linf.inflate(R.layout.valveconfiguration, container, false);
			
			MultiSelectionSpinner spinner1 = (MultiSelectionSpinner)relativ.findViewById(R.id.wirednumberirrivalve);
			spinner1.setItems(activity.getResources().getStringArray(R.array.irrigationnvalvenumber));
			
			MultiSelectionSpinner spinner2 = (MultiSelectionSpinner)relativ.findViewById(R.id.plcnumberirrivalve);
			spinner2.setItems(activity.getResources().getStringArray(R.array.irrigationnvalvenumber));
			
			MultiSelectionSpinner spinner3 = (MultiSelectionSpinner)relativ.findViewById(R.id.wirelessnumberirrivalve);
			spinner3.setItems(activity.getResources().getStringArray(R.array.irrigationnvalvenumber));
			
			MultiSelectionSpinner spinner4 = (MultiSelectionSpinner)relativ.findViewById(R.id.numberfertivalve);
			spinner4.setItems(activity.getResources().getStringArray(R.array.fertigationvalvenumber));

			return relativ;
		
	}

}
