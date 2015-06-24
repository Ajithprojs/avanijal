package com.app.controllers;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.avanstart.R;
import com.app.beans.Children;

public class AssociationController {
	
	static AssociationController _instance;

	ArrayList<Children> configs;
	
	Activity cxt;

	private AssociationController() {


	}
	public static AssociationController getInstance() {

		if(_instance == null)
			_instance = new AssociationController();
		return _instance;
	}
	
	public RelativeLayout getAssociationLayout(ViewGroup cont ,Activity act) {
		
		this.cxt = act;
		LayoutInflater oldlinf = (LayoutInflater) act.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		RelativeLayout rel = (RelativeLayout) oldlinf.inflate(R.layout.activity_association_list, cont, false);
		return rel;
	}

}
