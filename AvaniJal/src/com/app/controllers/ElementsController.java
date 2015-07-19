package com.app.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.avanstart.R;
import com.app.beans.Elements;

public abstract class ElementsController {

	static ElementsController _instance;
	int localVal = 0;
	int strtIndx = 0;
	ViewGroup container;
	LinearLayout elementLinear;
	AlertDialog alert;

	/*
	 * Variables that has to be fed from the inheriting class
	 * 
	 * */

	public ArrayList<Elements> elements;
	Context activity;
	int max = 6;
	public String type;
	ArrayList<String> elementsnum = new ArrayList<String>();


	public ElementsController() {
		//uncomment for element UI Testing
		//		elementsnum.add("1");
		//		elementsnum.add("2");
		//		elementsnum.add("3");
		//		elementsnum.add("4");
		//		elementsnum.add("5");
		//		elementsnum.add("6");
		//		type = "element";

	}

	//	public static ElementsController getInstance() {
	//
	//		if(_instance == null)
	//			_instance = new ElementsController();
	//		return _instance;
	//
	//	}
	//uncomment for element UI Testing
	//	public RelativeLayout getElementLayout( ViewGroup con ,  Context act  ) {
	//
	//		this.container = con;
	//		this.activity = act;
	//
	//		LayoutInflater oldlinf = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	//		RelativeLayout lin = (RelativeLayout) oldlinf.inflate(R.layout.elementconfigholder, container, false);
	//		elementLinear = (LinearLayout)lin.findViewById(R.id.motorlayout);
	//		Button addLocalMotorBtn = (Button)lin.findViewById(R.id.addelementbtn);
	//		addLocalMotorBtn.setOnClickListener(new OnClickListener() {
	//
	//			@Override
	//			public void onClick(View arg0) {
	//				// TODO Auto-generated method stub
	//				if(max == localVal){
	//					showDialog("elements", "We can have only " +localVal);
	//				}else {
	//
	//					addElement(elementsnum.get(0) , new Elements());
	//					localVal++;
	//				}
	//			}
	//		});
	//		return lin;
	//
	//	}

	public void addElement( String id , Elements eitem ) {

		setRemoveElementFromArray(id);
		//setAddElementToArray(id);
		eitem.setItemid(type+id);
		strtIndx = type.length();
		if(elements == null){
			elements = new ArrayList<Elements>();
		}
		localVal++;
		//add only if not existing
		if(!hasElement(eitem)){
			deleteElement(eitem.getItemid());
			elements.add(eitem);
		}
		//uncomment for element UI Testing
		buildUI(eitem);
	}

	public void deleteElement(String id ) {

//		if(vg != null){
//			int childcount = vg.getChildCount();
//			for (int i=0; i < childcount; i++){
//				View view = vg.getChildAt(i);
//				String oneView = (String)view.getTag();
//				if(oneView.equals(id)){
//					vg.removeView(view);
//					break;
//				}
//			}
//		}

		Iterator<Elements> iter = elements.iterator();

		while (iter.hasNext()) {
			Elements ele = iter.next();

			if (ele.getItemid().equals(id)){
				iter.remove();
				setAddElementToArray(id);
				//setRemoveElementFromArray(id);
				localVal--;
				reloadUI();
				break;
			}
		}
		

	}

	public boolean hasElement( Elements elem ) {

		Boolean has = false;
		for (Elements ele : elements) {
			if(ele.getItemid().equals(elem)) {
				has = true;
				break;
			}
		}
		return has;
	}
	
	public abstract void destructController(); {
		
//		ElementsController _instance = null;
//		container = null;
//		elementLinear= null;
//		alert = null;
//
//		elements = null;
//		activity = null;
//		ArrayList<String> elementsnum = null;
		
	}

	public abstract void reloadUI();
	public abstract void clearUI();
	public abstract void buildUI( Elements eitem);

	//uncomment for element UI Testing

	//	private void buildUI( final Elements eitem ) {
	//
	//		LayoutInflater linf = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	//
	//		RelativeLayout relativ = (RelativeLayout)linf.inflate(R.layout.elementconfiguration, container, false);
	//
	//		TextView tv = (TextView)relativ.findViewById(R.id.motornumtxt);
	//
	//		final Button deleteBtn = (Button)relativ.findViewById(R.id.delebtn);
	//
	//		relativ.setTag(eitem.itemId);
	//
	//		deleteBtn.setTag(eitem.itemId);
	//
	//		tv.setText(eitem.itemId);
	//
	//		deleteBtn.setOnClickListener(new OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				// TODO Auto-generated method stub
	//				deleteElement((String)deleteBtn.getTag() , elementLinear);
	//			}
	//		});
	//
	//		elementLinear.addView(relativ);
	//	}

	public void setRemoveElementFromArray( String id  ) {


		Iterator<String> iter = elementsnum.iterator();

		while (iter.hasNext()) {
			String str = iter.next();

			if (str.equals(id))
				iter.remove();
		}
	}

	public void setAddElementToArray( String id ){

		if(elementsnum != null) {
			Boolean hasElement = false;
			Iterator<String> iter = elementsnum.iterator();

			while (iter.hasNext()) {
				String str = iter.next();

				if (str.equals(id)) {
					hasElement = true;
					break;
				}
			}
			if(!hasElement){
				elementsnum.add(""+getElementInt(id));
			}
		}
	}

	private int getElementInt( String elementId ){

		int num = Integer.parseInt(elementId.substring(strtIndx, strtIndx + 1));
		return num;
	}

	public void showDialog( String titleName , String message ) {

		if(alert == null){

			alert = new AlertDialog.Builder(activity).create();
			alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

		}

		alert.setTitle(titleName);
		alert.setMessage(message);
		alert.show();

	}

}
