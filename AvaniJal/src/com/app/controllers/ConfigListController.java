package com.app.controllers;

import java.util.ArrayList;
import java.util.Hashtable;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.avaniadapters.ConfigListViewAdapter;
import com.app.avanstart.FilterActivity;
import com.app.avanstart.MotorActivity;
import com.app.avanstart.PipelineActivity;
import com.app.avanstart.R;
import com.app.avanstart.SensorActivity;
import com.app.avanstart.ValveActivity;
import com.app.avanstart.VenturyFertigationActivity;
import com.app.avanstart.util.AppUtils;
import com.app.beans.Children;
import com.app.beans.ConfigItem;
import com.app.beans.ConfigStatus;
import com.app.interfaces.expandedlistinterfaces;
import com.app.parsers.SmsParser;
import com.app.parsers.SmsParser.MessageHolder;

public class ConfigListController implements expandedlistinterfaces {

	static ConfigListController _instance;

	ArrayList<Children> configs;

	Activity cxt;

	AlertDialog alert;
	
	AlertDialog alert1;

	ConfigListViewAdapter adapter;
	
	int clickedVal = 0;


	private ConfigListController() {


	}
	public static ConfigListController getInstance() {

		if(_instance == null) {
			_instance = new ConfigListController();

		}
		return _instance;
	}

	public RelativeLayout getConfigLayout(ViewGroup cont ,Activity act) {

		this.cxt = act;
		refreshConfigs();
		clickedVal = 0;
		LayoutInflater oldlinf = (LayoutInflater) act.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		RelativeLayout rel = (RelativeLayout) oldlinf.inflate(R.layout.activity_configurations_list, cont, false);
		ListView list = (ListView)rel.findViewById(R.id.configlist);
		adapter = new ConfigListViewAdapter(act,
				createAvaniGroups() , AppUtils.confItems.getElementConfigStatus(), this);
		list.setAdapter(adapter);
		return rel;
	}
	
	public void destructControllers() {
		//MotorController.getInstance().destructController();
		//PipelineController.getInstance().destructController();
		//FilterController.getInstance().destructController();
	}
	private ArrayList<Children> createAvaniGroups() {

		ArrayList<Children> groups = new ArrayList<Children>();

		/// lets create group for configuration , association and provisioning
		String[] elements = this.cxt.getResources().getStringArray(R.array.elements);
		int[] eleimgs = {R.drawable.motors, R.drawable.pipelines , R.drawable.filters , R.drawable.valves , R.drawable.fertigation, R.drawable.sensors};
		int j = 0;
		Hashtable<String, ConfigStatus> cghash = AppUtils.confItems.getElementConfigStatus();
		for (String string : elements) {
			ConfigStatus cg = cghash.get(string);
			groups.add(getNewChild(string, cg.getConfigDesc(), eleimgs[j]));
			j++;
		}

		return groups;
	}
	private Children getNewChild(String title , String status , int img){

		Children ch = new Children();
		ch.title = title;
		ch.status = status;
		ch.img = img;
		return ch;

	}

	public void refreshConfigs() {

		String[] elements = this.cxt.getResources().getStringArray(R.array.elements);
		Hashtable<String, ConfigStatus> cghash = AppUtils.confItems.getElementConfigStatus();
		if(cghash == null){
			cghash = new Hashtable<String, ConfigStatus>();
		}
		ConfigStatus cg = null;
		for (String string : elements) {
			if(cghash.containsKey(string))
				cg = cghash.get(string);
			else {
				cg = new ConfigStatus();
				cg.setConfigDesc("Not Configured");
				cg.setIsConfigured(false);
				cghash.put(string, cg);
			}
		}
		AppUtils.confItems.setElementConfigStatus(cghash);
	}

	public void addConfigToElement( String eleName ) {

		Hashtable<String, ConfigStatus> cghash = AppUtils.confItems.getElementConfigStatus();
		if(cghash == null){
			cghash = new Hashtable<String, ConfigStatus>();
		}
		ConfigStatus cg = cghash.get(eleName);
		cg.setConfigDesc("Configured");
		cg.setIsConfigured(true);
		cghash.put(eleName, cg);
		AppUtils.confItems.setElementConfigStatus(cghash);
		adapter.notifyDataSetChanged();
	}
	public void removeConfigToElement( String eleName ) {

		Hashtable<String, ConfigStatus> cghash = AppUtils.confItems.getElementConfigStatus();
		if(cghash == null){
			cghash = new Hashtable<String, ConfigStatus>();
		}
		ConfigStatus cg = cghash.get(eleName);
		cg.setConfigDesc("Not Configured");
		cg.setIsConfigured(false);
		cghash.put(eleName, cg);
		AppUtils.confItems.setElementConfigStatus(cghash);
		adapter.notifyDataSetChanged();
	}
	

	public boolean isElementConfigured( String eleName ) {

		Hashtable<String, ConfigStatus> cghash = AppUtils.confItems.getElementConfigStatus();
		return cghash.get(eleName).getISConfigured();
	}



	public void OnItemClicked( int childPosition ) {


		switch (childPosition + 1) {
		case 1:
			/// show motor config
			if(isElementConfigured("Motor")){
				clickedVal = 1; 
				showDialogWithOptions( "Configured" ,"Motors are already configured, do you want to edit?", 1, "Edit" );
			}else{
				navigateToMotorConfig();
			}
			//navigateToMotorConfig();
			break;

		case 2:
			/// show pipeline config
			if(isElementConfigured("Motor")){

				if(isElementConfigured("Pipeline")){
					clickedVal = 2; 
					showDialogWithOptions( "Configured" ,"Pipelines are already configured, do you want to edit?", 2, "Edit" );
				}else{
					navigateToPipelineConfig();
				}
				//navigateToPipelineConfig();

			}else {
				showDialog("Error", "Pipeline cannot be configured unless Motor is configured");
			}
			break;

		case 3:

			if(isElementConfigured("Pipeline")){
				if(isElementConfigured("Filter")){
					clickedVal = 3; 
					showDialogWithOptions( "Configured" ,"Filters are already configured, do you want to edit?", 3, "Edit" );
				}else{
					navigateToFilterConfig();
				}
				//navigateToFilterConfig();
			}else {
				showDialog("Error", "Filters cannot be configured unless Pipeline is configured");
			}

			break;

		case 4:

			navigateToValveConfig();
			break;
			
		case 5 :
			if(isElementConfigured("Valve") && isElementConfigured("Motor") && isElementConfigured("Pipeline")){
				if(isElementConfigured("Ventury")){
					clickedVal = 5; 
					showDialogWithOptions( "Configured" ,"Ventury and Fertigation are already configured, do you want to edit?", 3, "Edit" );
				}else{
					navigateToVenturyAndFertigation();
				}
			}else {
				showDialog("Error", "Ventury cannot be configured unless Valve and Motors are configured");
			}
			break;

		default:
			navigateToSensorConfig();
			break;

		}

	}

	private void navigateToMotorConfig() {

		if(AppUtils.confItems == null) {
			AppUtils.confItems = new ConfigItem();
		}
		Intent i = new Intent( cxt , MotorActivity.class );
		this.cxt.startActivityForResult(i, 2001);
	}
	private void navigateToPipelineConfig() {

		Intent i = new Intent( cxt , PipelineActivity.class );
		cxt.startActivityForResult(i, 2001);
	}

	private void navigateToFilterConfig() {

		Intent i = new Intent( cxt , FilterActivity.class );
		cxt.startActivityForResult(i, 2001);
	}

	private void navigateToValveConfig() {

		Intent i = new Intent( cxt , ValveActivity.class );
		cxt.startActivityForResult(i, 2001);
	}
	
	private void navigateToVenturyAndFertigation() {
		Intent i = new Intent( cxt , VenturyFertigationActivity.class );
		cxt.startActivityForResult(i, 2001);
	}

	private void navigateToSensorConfig() {

		Intent i = new Intent( cxt , SensorActivity.class );
		cxt.startActivityForResult(i, 2001);
	}
	@Override
	public void listclicked(int child) {
		// TODO Auto-generated method stub
		OnItemClicked(child);

	}

	private void showDialog( String motorName , String message ) {
		alert = null;
		if(alert == null){

			alert = new AlertDialog.Builder(cxt).create();
			alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

		}

		alert.setTitle(motorName);
		alert.setMessage(message);
		alert.show();

	}

	private void showDialogWithOptions( String title , String msg, int val, String btnTitle ) {
		alert1 = null;
		if(alert1 == null){

			alert1 = new AlertDialog.Builder(cxt).create();
			alert1.setButton(AlertDialog.BUTTON_POSITIVE, btnTitle,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					switch(clickedVal){

					case 1 :
						navigateToMotorConfig();
						break;
					case 2:
						navigateToPipelineConfig();
						break;
					case 3:
						navigateToFilterConfig();
						break;
					case 5 :
						navigateToVenturyAndFertigation();
						break;

					default:
						dialog.dismiss();
						break;
					}

				}
			});

			alert1.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();

				}
			});

		}

		alert1.setTitle(title);
		alert1.setMessage(msg);
		alert1.show();
	}

}
