package com.app.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.sax.StartElementListener;
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

import com.app.avaniadapters.ConfigListViewAdapter;
import com.app.avaniadapters.IrrigationListViewAdapter;
import com.app.avanstart.IrrigationActivity;
import com.app.avanstart.R;
import com.app.avanstart.util.AppUtils;
import com.app.beans.Children;
import com.app.beans.ConfigStatus;
import com.app.beans.CropItem;
import com.app.beans.Elements;
import com.app.beans.IrriCropItems;
import com.app.interfaces.expandedlistinterfaces;

public class IrrigationController implements expandedlistinterfaces {

	static IrrigationController _instance;

	ArrayList<Children> configs;

	Activity cxt;

	AlertDialog alert;

	AlertDialog alert1;

	ConfigListViewAdapter adapter;

	int clickedVal = 0;

	String[] titles;

	private IrrigationController() {


	}
	public static IrrigationController getInstance() {

		if(_instance == null)
			_instance = new IrrigationController();
		return _instance;
	}

	public RelativeLayout getIrrigationLayout(ViewGroup cont ,Activity act) {


		this.cxt = act;
		refreshConfigs();
		clickedVal = 0;
		LayoutInflater oldlinf = (LayoutInflater) act.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		RelativeLayout rel = (RelativeLayout) oldlinf.inflate(R.layout.activity_configurations_list, cont, false);
		ListView list = (ListView)rel.findViewById(R.id.configlist);
		
		adapter = new ConfigListViewAdapter(act,
				createAvaniGroups() , AppUtils.irriItems.getElementConfigstatus(), this);
		list.setAdapter(adapter);
		return rel;
	}

	private ArrayList<Children> createAvaniGroups() {

		ArrayList<Children> groups = new ArrayList<Children>();

		Hashtable<String, CropItem> crops = AppUtils.assoItems.getAllCropItems();
		Enumeration<String> cropenum = crops.keys();
		titles = new String[crops.size()] ;
		Hashtable<String, ConfigStatus> cghash = AppUtils.irriItems.getElementConfigstatus();
		int i = 0;
		while(cropenum.hasMoreElements()) {

			String citemkey = cropenum.nextElement();
			titles[i] = citemkey;
			CropItem citem = crops.get(citemkey);
			ConfigStatus cg = cghash.get(citemkey);
			groups.add(getNewChild(citemkey, cg.getConfigDesc(), citem.getImgId()));
			i++;
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

		Hashtable<String, CropItem> crops = AppUtils.assoItems.getAllCropItems();
		Enumeration<String> cropenum = crops.keys();
		Hashtable<String, ConfigStatus> cghash = AppUtils.irriItems.getElementConfigstatus();
		if(cghash == null){
			cghash = new Hashtable<String, ConfigStatus>();
		}
		ConfigStatus cg = null;

		while(cropenum.hasMoreElements()) {

			String citemkey = cropenum.nextElement();
			if(cghash.containsKey(citemkey))
				cg = cghash.get(citemkey);
			else {
				cg = new ConfigStatus();
				cg.setConfigDesc("Stopped");
				cg.setIsConfigured(false);
				cghash.put(citemkey, cg);
			}
		}
		AppUtils.irriItems.setElementConfigstatus(cghash);
	}

	public void addConfigToElement( String eleName ) {

		Hashtable<String, ConfigStatus> cghash = AppUtils.irriItems.getElementConfigstatus();
		if(cghash == null){
			cghash = new Hashtable<String, ConfigStatus>();
		}
		ConfigStatus cg = cghash.get(eleName);
		cg.setConfigDesc("Active");
		cg.setIsConfigured(true);
		cghash.put(eleName, cg);
		AppUtils.irriItems.setElementConfigstatus(cghash);
		adapter.notifyDataSetChanged();
	}
	public void removeConfigToElement( String eleName ) {

		Hashtable<String, ConfigStatus> cghash = AppUtils.irriItems.getElementConfigstatus();
		if(cghash == null){
			cghash = new Hashtable<String, ConfigStatus>();
		}
		ConfigStatus cg = cghash.get(eleName);
		cg.setConfigDesc("Stopped");
		cg.setIsConfigured(false);
		cghash.put(eleName, cg);
		AppUtils.irriItems.setElementConfigstatus(cghash);
		adapter.notifyDataSetChanged();
	}

	public boolean isElementConfigured( String eleName ) {

		Hashtable<String, ConfigStatus> cghash = AppUtils.irriItems.getElementConfigstatus();
		return cghash.get(eleName).getISConfigured();
	}
	@Override
	public void listclicked(int child) {
		// TODO Auto-generated method stub
		Intent i = new Intent( cxt, IrrigationActivity.class );
		i.putExtra("cropname", titles[child]);
		CropItem irriItem = AppUtils.assoItems.getCropItem(titles[child]);
		i.putExtra("imageid", irriItem.getImgId());
		i.putExtra("acre", irriItem.getAcre());
		ArrayList<String> pipes = irriItem.getAllAssociatedElementsOfType(AppUtils.PIPELINE_TYPE);
		i.putExtra("pipeline", pipes.get(0));
		cxt.startActivityForResult(i, 2004);
	}
}
