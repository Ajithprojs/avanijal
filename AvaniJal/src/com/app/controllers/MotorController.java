package com.app.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.app.avanstart.R;
import com.app.avanstart.util.AppUtils;
import com.app.beans.Elements;
import com.app.beans.MotorItem;



public class MotorController {


	static MotorController _instance;

	int MAX_LOCAL = 4;

	int MAX_REMOTE = 2;

	ArrayList<MotorItem> motors;

	RelativeLayout relativ;

	LinearLayout motorLinear;

	RelativeLayout lin;

	int selectedMotorType;

	Context activity;

	ViewGroup container;

	ArrayAdapter<String> dataAdapter;

	ArrayList<String> localmotonum;

	ArrayList<String> remotemotonum;

	Boolean disableDropDown = false;

	int localVal,remoteVal;

	AlertDialog alert;

	String type;

	int strtIndx;


	private MotorController() {


	}

	public static MotorController getInstance() {

		if(_instance == null)
			_instance = new MotorController();
		return _instance;

	}

	public RelativeLayout getMotorLayout( ViewGroup con ,  Context act , LinearLayout llayout ) {


		this.container = con;
		this.activity = act;
		type = "motor";

		localmotonum = new ArrayList<String>(Arrays.asList((String[])this.activity.getResources().getStringArray(R.array.localmotornumber)));

		remotemotonum = new ArrayList<String>(Arrays.asList((String[])this.activity.getResources().getStringArray(R.array.remotemotornumber)));


		LayoutInflater oldlinf = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		lin = (RelativeLayout) oldlinf.inflate(R.layout.motorconfigholder, container, false);
		motorLinear = (LinearLayout)lin.findViewById(R.id.motorlayout);

		Button addLocalMotorBtn = (Button)lin.findViewById(R.id.addlocalmotorbtn);

		Button addRemoteMotorBtn = (Button)lin.findViewById(R.id.addremotemotorbtn);

		addLocalMotorBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(MAX_LOCAL == localVal){
					showDialog("Motors", "We can have only" +MAX_LOCAL+"local motors");
				}else {
					localVal++;
					MotorItem mitem = new MotorItem();
					mitem.setMotorType(0);
					disableDropDown = false;
					addElement(localmotonum.get(0), mitem);
				}
			}
		});

		addRemoteMotorBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(MAX_REMOTE == remoteVal){
					showDialog("Motors", "We can have only" +MAX_REMOTE+"remote motors");
				}else {
					remoteVal++;
					MotorItem mitem = new MotorItem();
					mitem.setMotorType(1);
					disableDropDown = false;
					addElement(remotemotonum.get(0), mitem);
				}
			}
		});

		if(AppUtils.confItems.motorItems == null)
			motors = new ArrayList<MotorItem>();
		else
		{
			//motors = AppUtils.confItems.motorItems;
		}

		for (MotorItem mt : motors) {

			disableDropDown = true;
			buildUI(mt);
		}

		return lin;
	}

	//	public void addMotor( int val ) {
	//
	//		final MotorItem mitem = new MotorItem();
	//		mitem.setMotorType(val);
	//		setMotorArray(val);
	//		mitem.itemId = "motor"+motonum.get(0);
	//		motors.add(mitem);
	//		//AppUtils.confItems.motorItems = motors;
	//		disableDropDown = false;
	//		buildUI(mitem);
	//
	//	}


	private void buildUI( final MotorItem mitem ) {


		LayoutInflater linf = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

		relativ = (RelativeLayout)linf.inflate(R.layout.motorconfiguration, container, false);

		final Spinner idSpinner = (Spinner)relativ.findViewById(R.id.motoridval);
		idSpinner.setTag(mitem.itemId);
		final TextView motornum = (TextView)relativ.findViewById(R.id.motornumtxt);
		final RadioGroup operations = (RadioGroup)relativ.findViewById(R.id.operationgroup);
		final RadioGroup hpvalue = (RadioGroup)relativ.findViewById(R.id.hptypegroup);
		final RadioGroup deliverytype = (RadioGroup)relativ.findViewById(R.id.deliverytypegroup);
		final RadioGroup currenttype = (RadioGroup)relativ.findViewById(R.id.curmeasuregroup);

		final EditText EditmotorName = (EditText)relativ.findViewById(R.id.motorname);
		final EditText Editminvoltage = (EditText)relativ.findViewById(R.id.minvoltage);
		final EditText Editmaxvoltage = (EditText)relativ.findViewById(R.id.maxvoltage);
		final EditText Editlitres = (EditText)relativ.findViewById(R.id.litreminute);
		final EditText phoneNum = (EditText)relativ.findViewById(R.id.phonenumfield);
		final Button deleteBtn = (Button)relativ.findViewById(R.id.delebtn);
		final Spinner typeSpinner = (Spinner)relativ.findViewById(R.id.typespinner);


		if(mitem.getHpValeint() == 1)
		{
			hpvalue.check(R.id.secondhp);
		}else if(mitem.getHpValeint() == 2){
			hpvalue.check(R.id.thirdhp);
		}else {
			hpvalue.check(R.id.firsthp);
		}


		if(mitem.getOperationTypeint() == 1)
		{
			operations.check(R.id.threephase);
		} else {
			operations.check(R.id.twophase);
		}


		if(mitem.getDeliveryTypeint() == 1)
		{
			deliverytype.check(R.id.combinedbutton);
		}else{
			deliverytype.check(R.id.singlebutton);
		}


		if(mitem.getCurrentTypeint() == 1)
		{
			currenttype.check(R.id.ctbutton);
		}else{
			currenttype.check(R.id.startercurrentbutton);
		}

		if(mitem.getMotorTypeint() == 1)
		{
			//motortype.check(R.id.remotebutton);
			phoneNum.setVisibility(View.VISIBLE);
		}else {
			//motortype.check(R.id.localbutton);
			phoneNum.setVisibility(View.GONE);
		}

		EditmotorName.setText(mitem.getPumpName());
		Editminvoltage.setText(mitem.getMinVolts());
		Editmaxvoltage.setText(mitem.getMaxVolts());
		Editlitres.setText(mitem.getWaterDeliveryRate());

		typeSpinner.setSelection(mitem.getAgriTypeint());
		motornum.setText(getMotorTypeHeading(mitem.getMotorTypeint()));

		ArrayList<String> motonum = null;
		if(mitem.equals("0")){
			/// its a local motor
			motonum = localmotonum;
		}else {
			motonum = remotemotonum;
		}

		operations.setTag(mitem.itemId);
		hpvalue.setTag(mitem.itemId);
		deliverytype.setTag(mitem.itemId);
		currenttype.setTag(mitem.itemId);
		EditmotorName.setTag(mitem.itemId);
		Editminvoltage.setTag(mitem.itemId);
		Editmaxvoltage.setTag(mitem.itemId);
		Editlitres.setTag(mitem.itemId);
		typeSpinner.setTag(mitem.itemId);
		deleteBtn.setTag(mitem.itemId);
		relativ.setTag(mitem.itemId);
		idSpinner.setTag(mitem.itemId);
		motornum.setTag(mitem.itemId);
		motornum.setText(mitem.itemId);


		//		idSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		//			@Override
		//			public void onItemSelected(AdapterView<?> view, View v,
		//					int pos, long arg3) {
		//				// TODO Auto-generated method stub
		//
		//				if(!disableDropDown) {
		//					String val = motonum.get(pos);
		//					motornum.setText(getMotorTypeHeading(mitem.getMotorTypeint())+val);
		//					setMotorId((String)view.getTag(), val);
		//					operations.setTag(mitem.itemId);
		//					hpvalue.setTag(mitem.itemId);
		//					deliverytype.setTag(mitem.itemId);
		//					currenttype.setTag(mitem.itemId);
		//					EditmotorName.setTag(mitem.itemId);
		//					Editminvoltage.setTag(mitem.itemId);
		//					Editmaxvoltage.setTag(mitem.itemId);
		//					Editlitres.setTag(mitem.itemId);
		//					typeSpinner.setTag(mitem.itemId);
		//					deleteBtn.setTag(mitem.itemId);
		//					relativ.setTag(mitem.itemId);
		//					idSpinner.setTag(mitem.itemId);
		//					motornum.setTag(mitem.itemId);
		//					motornum.setText(mitem.itemId);
		//				}
		//				disableDropDown = false;
		//
		//
		//			}
		//
		//			@Override
		//			public void onNothingSelected(AdapterView<?> arg0) {
		//				// TODO Auto-generated method stub
		//
		//			}
		//		});

		if (motonum.size() > 0) {

			dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, motonum);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			idSpinner.setAdapter(dataAdapter);

		}

		disableDropDown = true;
		//idSpinner.setSelection(getIndex(idSpinner, ""+getMotorInt(mitem.itemId)), false);

		deleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//				int childcount = motorLinear.getChildCount();
				//				for (int i=0; i < childcount; i++){
				//					View view = motorLinear.getChildAt(i);
				//					if(view.getTag().equals(deleteBtn.getTag())){
				//						view.setVisibility(View.GONE);
				//						String tagObj = (String) deleteBtn.getTag();
				//						if(motors != null )
				//							motors.remove(tagObj);
				//						break;
				//					}
				//				}

				deleteElement((String)deleteBtn.getTag() , motorLinear);
			}
		});



		operations.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup rg2, int val) {
				// TODO Auto-generated method stub
				setMotorOperationTypeChanged((String)rg2.getTag(), val);
			}
		});

		hpvalue.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup rg3, int val) {
				// TODO Auto-generated method stub
				setMotorHPValueChanged((String)rg3.getTag(), val);
			}
		});

		deliverytype.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup rg2, int val) {
				// TODO Auto-generated method stub
				//setMotorOperationTypeChanged((String)rg2.getTag(), val);
				setDeliveryType((String)rg2.getTag(), val);
			}
		});

		currenttype.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup rg3, int val) {
				// TODO Auto-generated method stub
				setCurrentType((String)rg3.getTag(), val);
			}
		});

		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				setAgriType((String)arg0.getTag(), arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		EditmotorName.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					MotorItem mItem = getMotorObjectforTag((String)EditmotorName.getTag());
					if(mItem != null)
						mItem.setPumpName(EditmotorName.getText().toString());
				}
			}
		});

		Editlitres.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					MotorItem mItem = getMotorObjectforTag((String)Editlitres.getTag());
					if(mItem != null)
						mItem.setWaterDeliveryRate(Editlitres.getText().toString());
				}
			}
		});

		Editminvoltage.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					MotorItem mItem = getMotorObjectforTag((String)Editminvoltage.getTag());
					if(mItem != null)
						mItem.setMinVolts(Editminvoltage.getText().toString());
				}
			}
		});

		Editmaxvoltage.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					MotorItem mItem = getMotorObjectforTag((String)Editmaxvoltage.getTag());
					if(mItem != null)
						mItem.setMaxVolts(Editmaxvoltage.getText().toString());
				}
			}
		});

		motornum.requestFocus();
		motorLinear.addView(relativ);

	}


	private int getMotorInt( String motorId ){

		int num = Integer.parseInt(motorId.substring(5, 6));
		return num;
	}

	public void setMotorId( String pumpName  , String val ) {

		MotorItem mItem = getMotorObjectforTag(pumpName);
		mItem.itemId = "motor"+val;

	}


	public void setMotorHPValueChanged( String pumpNme , int val )  {

		MotorItem mItem = getMotorObjectforTag(pumpNme);
		if(mItem != null)
			if(val == R.id.firsthp){
				mItem.setHpVal(0);
			}else if(val == R.id.secondhp){
				mItem.setHpVal(1);
			}else {
				mItem.setHpVal(2);
			}

	}

	public void setMotorOperationTypeChanged( String pumpNme , int val )  {

		MotorItem mItem = getMotorObjectforTag(pumpNme);
		if(mItem != null)
			if(val == R.id.twophase){
				mItem.setOperationType(0);
			}else
			{
				mItem.setOperationType(1);
			}


	}

	public void setDeliveryType( String pumpName , int val ) {

		MotorItem mItem = getMotorObjectforTag(pumpName);
		if(mItem != null)
			if(val == R.id.singlebutton)
				mItem.setDeliveryType(0);
			else
				mItem.setDeliveryType(1);
	}

	public void setCurrentType( String pumpName , int val ) {

		MotorItem mItem = getMotorObjectforTag(pumpName);
		if(mItem != null)
			if(val == R.id.startercurrentbutton)
				mItem.setCurrentDelivery(0) ;
			else
				mItem.setCurrentDelivery(1);
	}

	public void setAgriType( String pumpName , int val ) {
		MotorItem mItem = getMotorObjectforTag(pumpName);
		if(mItem != null)
			mItem.setAgriType(val);
	}

	private MotorItem getMotorObjectforTag( String motorName ) {

		MotorItem mt = null;
		for (MotorItem mitem : motors) {
			if(mitem.itemId.equalsIgnoreCase(motorName))
				mt = mitem;
		}
		return mt;

	}

	//	private int getIndex(Spinner spinner,String string){
	//
	//		//Pseudo code because I dont remember the API
	//
	//		int index = 0;
	//
	//		for (int i = 0; i < motonum.size(); i++){
	//
	//			if (spinner.getItemAtPosition(i).equals(string)){
	//				index = i;
	//			}
	//
	//		}
	//
	//		return index;
	//
	//	}

	private void showDialog( String motorName , String message ) {

		if(alert == null){

			alert = new AlertDialog.Builder(activity).create();
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

	private String getMotorTypeHeading(int val) {

		if(val == 0){
			return "Local Motor";
		}else {
			return "Remote Motor";
		}
	}

	public void addElement( String id , MotorItem eitem ) {

		setRemoveElementFromArray(id, eitem.getMotorTypeint());
		eitem.itemId = type+id;
		strtIndx = type.length();
		if(motors == null){
			motors = new ArrayList<MotorItem>();
		}
		motors.add(eitem);
		buildUI(eitem);
		AppUtils.confItems.motorItems = motors;
	}

	public void deleteElement(String id, ViewGroup vg ) {

		int childcount = vg.getChildCount();
		for (int i=0; i < childcount; i++){
			View view = vg.getChildAt(i);
			String oneView = (String)view.getTag();
			if(oneView.equals(id)){
				vg.removeView(view);
				break;
			}
		}

		Iterator<MotorItem> iter = motors.iterator();

		while (iter.hasNext()) {
			MotorItem ele = iter.next();

			if (ele.itemId.equals(id)){
				iter.remove();
				setAddElementToArray(id , ele.getMotorTypeint());
				localVal--;
				break;
			}
		}

	}

	public void setRemoveElementFromArray( String id, int mtype  ) {

		ArrayList<String> motonum = null;
		if(mtype == 0){
			/// its a local motor
			motonum = localmotonum;
		}else {
			motonum = remotemotonum;
		}

		Iterator<String> iter = motonum.iterator();

		while (iter.hasNext()) {
			String str = iter.next();

			if (str.equals(id))
				iter.remove();
		}
	}

	public void setAddElementToArray( String id, int mtype ){

		ArrayList<String> motonum = null;
		if(mtype == 0){
			/// its a local motor
			motonum = localmotonum;
		}else {
			motonum = remotemotonum;
		}
		if(motonum != null) {
			Boolean hasElement = false;
			Iterator<String> iter = motonum.iterator();

			while (iter.hasNext()) {
				String str = iter.next();

				if (str.equals(id)) {
					hasElement = true;
					break;
				}
			}
			if(!hasElement){
				motonum.add(""+getElementInt(id));
			}
		}
	}

	private int getElementInt( String elementId ){

		int num = Integer.parseInt(elementId.substring(strtIndx, strtIndx + 1));
		return num;
	}

	private boolean removeMotorObjForTag( String motorName ) {

		boolean removed = false;
		MotorItem mt = null;
		for (MotorItem mitem : motors) {
			if(mitem.itemId.equalsIgnoreCase(motorName)) {
				motors.remove(mt);
				removed = true;
				break;
			}
		}
		return removed;
	}


}



