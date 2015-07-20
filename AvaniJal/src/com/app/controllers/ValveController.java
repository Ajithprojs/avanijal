package com.app.controllers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.avanicomponents.MultiSelectionSpinner;
import com.app.avanstart.R;
import com.app.avanstart.util.AppUtils;
import com.app.beans.Elements;
import com.app.beans.ValveItems;
import com.app.interfaces.MultiSelectInterface;

public class ValveController implements MultiSelectInterface {
	
	
	static ValveController _instance;

	ArrayList<Elements> valves;
	
	private String WIRED_VALVE = "wired";
	
	private String WIRELESS_VALVE = "wireless";
	
	private String PLC_VALVE = "plc";
	
	private String FERT_VALVE = "fertigation";

	private ValveController() {


	}

	public static ValveController getInstance() {

		if(_instance == null)
			_instance = new ValveController();

		return _instance;

	}
	
	public RelativeLayout createValveLayout(ViewGroup container , LinearLayout llayout , Activity activity) {
		
		//llayout.removeAllViews();
		valves = AppUtils.confItems.getValveItems();
		if(valves == null) {
			valves = new ArrayList<Elements>();
		}

			ValveItems fitem = new ValveItems();
			fitem.setValveId(1) ;
			LayoutInflater linf = activity.getLayoutInflater();
			RelativeLayout relativ = (RelativeLayout)linf.inflate(R.layout.valveconfiguration, container, false);
			
			MultiSelectionSpinner spinner1 = (MultiSelectionSpinner)relativ.findViewById(R.id.wirednumberirrivalve);
			spinner1.setTag(WIRED_VALVE);
			spinner1.setItems(activity.getResources().getStringArray(R.array.irrigationnvalvenumber) ,this);
			ArrayList<String> selectedIds = new ArrayList<String>();
			for (Elements ele : valves) {
				selectedIds.add(ele.getItemid());
			}
			spinner1.setSelection(selectedIds);
//			MultiSelectionSpinner spinner2 = (MultiSelectionSpinner)relativ.findViewById(R.id.plcnumberirrivalve);
//			spinner1.setTag(PLC_VALVE);
//			spinner2.setItems(activity.getResources().getStringArray(R.array.irrigationnvalvenumber) , this);
//			
//			MultiSelectionSpinner spinner3 = (MultiSelectionSpinner)relativ.findViewById(R.id.wirelessnumberirrivalve);
//			spinner1.setTag(WIRELESS_VALVE);
//			spinner3.setItems(activity.getResources().getStringArray(R.array.irrigationnvalvenumber), this);
//			
//			MultiSelectionSpinner spinner4 = (MultiSelectionSpinner)relativ.findViewById(R.id.numberfertivalve);
//			spinner1.setTag(FERT_VALVE);
//			spinner4.setItems(activity.getResources().getStringArray(R.array.fertigationvalvenumber), this);
			
			AppUtils.confItems.setValveItems(valves);
			
			return relativ;
		
	}

	@Override
	public void BeforeSelectDialog(MultiSelectionSpinner spinner) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemSelected(MultiSelectionSpinner spinner, String val) {
		// TODO Auto-generated method stub
		String id = val;
		ValveItems valve = new ValveItems();
		valve.setItemid(id);
		valve.setType((String)spinner.getTag());
		if(!valves.contains(valve)) {
			
			valves.add(valve);
		}else 
			valve = null;
		
		
	}

	@Override
	public void itemDeselected(MultiSelectionSpinner spinner, String val) {
		// TODO Auto-generated method stub
		String id = val;
		Iterator<Elements> iter = valves.iterator();
		while(iter.hasNext()){
			ValveItems vitem = (ValveItems)iter.next();
			if(vitem.getItemid().equals(id)) {
				iter.remove();
			}
		}
		
		
	}

}
