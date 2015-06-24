package com.app.controllers;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.app.avaniadapters.ConfigListViewAdapter;
import com.app.avanstart.R;
import com.app.beans.Children;
import com.app.interfaces.expandedlistinterfaces;

public class SettingsController implements expandedlistinterfaces{
	
	static SettingsController _instance;

	ArrayList<Children> configs;
	
	Activity cxt;

	private SettingsController() {


	}
	public static SettingsController getInstance() {

		if(_instance == null)
			_instance = new SettingsController();
		return _instance;
	}
	
	public RelativeLayout getSettingsLayout(ViewGroup cont ,Activity act) {
		
		this.cxt = act;
		LayoutInflater oldlinf = (LayoutInflater) act.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		RelativeLayout rel = (RelativeLayout) oldlinf.inflate(R.layout.activity_settings_list, cont, false);
		ListView list = (ListView)rel.findViewById(R.id.settinglist);
		ConfigListViewAdapter adapter = new ConfigListViewAdapter(act,
				createAvaniGroups() , this);
		list.setAdapter(adapter);
		return rel;
	}
	
	private ArrayList<Children> createAvaniGroups() {

		ArrayList<Children> groups = new ArrayList<Children>();

		/// lets create group for configuration , association and provisioning
		String[] elements = this.cxt.getResources().getStringArray(R.array.settingitems);
		int[] eleimgs = {0};
		int j = 0;
		for (String string : elements) {
			groups.add(getNewChild(string, "", eleimgs[0]));
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
	@Override
	public void listclicked(int child) {
		// TODO Auto-generated method stub
		
	}
	

}
