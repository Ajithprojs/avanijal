package com.app.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.app.avaniadapters.IrrigationListViewAdapter;
import com.app.avanstart.R;
import com.app.avanstart.util.AppUtils;
import com.app.beans.Children;
import com.app.beans.Elements;

public class IrrigationController {
	
	static IrrigationController _instance;

	ArrayList<Children> configs;
	
	List<String> valves;
	
	Activity cxt;

	private IrrigationController() {


	}
	public static IrrigationController getInstance() {

		if(_instance == null)
			_instance = new IrrigationController();
		return _instance;
	}
	
	public LinearLayout getIrrigationLayout(ViewGroup cont ,Activity act) {

		
		//valves = AppUtils.confItems.getValveItems();
		
		//if(valves == null)
		//	valves = new ArrayList<Elements>();
		valves = Arrays.asList(act.getResources().getStringArray(R.array.irragation_valve_test)) ;
		ArrayList<String> valveArray = new ArrayList<String>();
		for(int i = 0; i < valves.size(); i++) {
			//Elements ele = (Elements)valves.get(i);
			valveArray.add(valves.get(i));
		}
		this.cxt = act;
		LayoutInflater oldlinf = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout rel = (LinearLayout) oldlinf.inflate(R.layout.activity_irrigation_list, cont, false);
		ListView irrigationList = (ListView) rel.findViewById(R.id.irrigation_listview);
		irrigationList.setAdapter(new IrrigationListViewAdapter(act, valveArray));
		addButtonLisener(rel);
		return rel;
	}

	void addButtonLisener(LinearLayout rel) {
		boolean radio = false;
		Button start_setconfigbtn = (Button) rel.findViewById(R.id.start_setconfigbtn);
		start_setconfigbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		Button stop_setconfigbtn = (Button) rel.findViewById(R.id.stop_setconfigbtn);
		stop_setconfigbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(cxt);
				alert.setTitle("Alert Dialog");
				alert.setMessage("Are you sure you want to stop this irrigation ?");

				alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				alert.setIcon(android.R.drawable.ic_dialog_alert).show();
			}
		});
	}
}
