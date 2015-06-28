package com.app.controllers;

import java.util.ArrayList;
import java.util.Hashtable;


import android.app.Activity;
import android.content.Context;
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

	ConfigListViewAdapter adapter;


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
		LayoutInflater oldlinf = (LayoutInflater) act.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		RelativeLayout rel = (RelativeLayout) oldlinf.inflate(R.layout.activity_configurations_list, cont, false);
		ListView list = (ListView)rel.findViewById(R.id.configlist);
		adapter = new ConfigListViewAdapter(act,
				createAvaniGroups() , this);
		list.setAdapter(adapter);
		return rel;
	}
	private ArrayList<Children> createAvaniGroups() {

		ArrayList<Children> groups = new ArrayList<Children>();

		/// lets create group for configuration , association and provisioning
		String[] elements = this.cxt.getResources().getStringArray(R.array.elements);
		int[] eleimgs = {R.drawable.motors, R.drawable.motors , R.drawable.filters , R.drawable.valves , R.drawable.sensors, R.drawable.sensors};
		int j = 0;
		Hashtable<String, ConfigStatus> cghash = AppUtils.confItems.elementConfigstatus;
		for (String string : elements) {
			ConfigStatus cg = cghash.get(string);
			groups.add(getNewChild(string, cg.configDescription, eleimgs[j]));
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
		Hashtable<String, ConfigStatus> cghash = AppUtils.confItems.elementConfigstatus;
		if(cghash == null){
			cghash = new Hashtable<String, ConfigStatus>();
		}
		ConfigStatus cg = null;
		for (String string : elements) {
			if(cghash.containsKey(string))
				cg = cghash.get(string);
			else {
				cg = new ConfigStatus();
				cg.configDescription = "Not Configured";
				cghash.put(string, cg);
			}
		}
		AppUtils.confItems.elementConfigstatus = cghash;
	}

	public void addConfigToElement( String eleName ) {

		Hashtable<String, ConfigStatus> cghash = AppUtils.confItems.elementConfigstatus;
		if(cghash == null){
			cghash = new Hashtable<String, ConfigStatus>();
		}
		ConfigStatus cg = cghash.get(eleName);
		cg.configDescription = "Configured";
		cghash.put(eleName, cg);
		AppUtils.confItems.elementConfigstatus = cghash;
		adapter.notifyDataSetChanged();
	}
	public void removeConfigToElement( String eleName ) {

		Hashtable<String, ConfigStatus> cghash = AppUtils.confItems.elementConfigstatus;
		if(cghash == null){
			cghash = new Hashtable<String, ConfigStatus>();
		}
		ConfigStatus cg = cghash.get(eleName);
		cg.configDescription = "Not Configured";
		cghash.put(eleName, cg);
		AppUtils.confItems.elementConfigstatus = cghash;
		adapter.notifyDataSetChanged();
	}



	public void OnItemClicked( int childPosition ) {


		switch (childPosition + 1) {
		case 1:
			/// show motor config
			navigateToMotorConfig();
			break;

		case 2:
			/// show pipeline config
			navigateToPipelineConfig();
			break;

		case 3:

			navigateToFilterConfig();
			break;

		case 4:

			navigateToValveConfig();
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
		cxt.startActivityForResult(i, 2002);
	}

	private void navigateToFilterConfig() {

		Intent i = new Intent( cxt , FilterActivity.class );
		cxt.startActivityForResult(i, 2003);
	}

	private void navigateToValveConfig() {

		Intent i = new Intent( cxt , ValveActivity.class );
		cxt.startActivityForResult(i, 2004);
	}

	private void navigateToSensorConfig() {

		Intent i = new Intent( cxt , SensorActivity.class );
		cxt.startActivityForResult(i, 2005);
	}
	@Override
	public void listclicked(int child) {
		// TODO Auto-generated method stub
		OnItemClicked(child);

	}

}
