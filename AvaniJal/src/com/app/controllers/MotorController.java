package com.app.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import android.R.string;
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



public class MotorController extends ElementsController {


	static MotorController _instance;

	int MAX_LOCAL = 4;

	int MAX_REMOTE = 2;

	/// holds all the motor elements
	ArrayList<Elements> motors;

	/// relative layout for each motor
	RelativeLayout relativ;

	/// final motor holder
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

	String type;

	Button addLocalMotorBtn ;

	Button addRemoteMotorBtn ;


	private MotorController() {


	}

	public static MotorController getInstance() {

		if(_instance == null)
			_instance = new MotorController();
		return _instance;

	}

	private void init(Context cxt) {

		/*
		 * send objects to the super class for addition and deletion
		 * 
		 * */

		if(AppUtils.confItems.getMotorItems() == null)
			motors = new ArrayList<Elements>();
		else
		{
			motors = AppUtils.confItems.getMotorItems();
		}
		type = "motor";
		super.elements = motors;
		super.max = 6;
		super.type = "motor";
		super.activity = cxt;

		localmotonum = new ArrayList<String>(Arrays.asList((String[])this.activity.getResources().getStringArray(R.array.localmotornumber)));
		remotemotonum = new ArrayList<String>(Arrays.asList((String[])this.activity.getResources().getStringArray(R.array.remotemotornumber)));


	}

	public RelativeLayout getMotorLayout( ViewGroup con ,  Context act , LinearLayout llayout ) {


		this.container = con;
		this.activity = act;
		init(act);


		LayoutInflater oldlinf = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		lin = (RelativeLayout) oldlinf.inflate(R.layout.motorconfigholder, container, false);
		motorLinear = (LinearLayout)lin.findViewById(R.id.motorlayout);

		addLocalMotorBtn = (Button)lin.findViewById(R.id.addlocalmotorbtn);

		addRemoteMotorBtn = (Button)lin.findViewById(R.id.addremotemotorbtn);

		addLocalMotorBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addLocalMotor();
			}
		});

		addRemoteMotorBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addRemoteMotor();
			}
		});
		ArrayList<Elements> tempMotors = (ArrayList<Elements>)motors.clone();
		for (Elements mt : tempMotors) {

			disableDropDown = true;
			MotorItem m  = (MotorItem)mt;
			addElement(""+getMotorInt(m.getItemid()), m);
		}

		return lin;
	}

	public void addLocalMotor() {

		if(MAX_LOCAL == localVal){
			super.showDialog("Motors", "We can have only" +MAX_LOCAL+"local motors");
		}else {
			localVal++;
			MotorItem mitem = new MotorItem();
			mitem.setMotorType(0);
			disableDropDown = false;
			addElement(localmotonum.get(0), mitem);
		}
	}

	public void addRemoteMotor() {

		if(MAX_REMOTE == remoteVal){
			super.showDialog("Motors", "We can have only" +MAX_REMOTE+"remote motors");
		}else {
			remoteVal++;
			MotorItem mitem = new MotorItem();
			mitem.setMotorType(1);
			disableDropDown = false;
			addElement(remotemotonum.get(0), mitem);
		}
	}

	public void addElement( String id , MotorItem eitem ) {

		setRemoveMotorToArray(id, eitem.getMotorTypeint());
		setButtonsSync();
		super.addElement(id, eitem);
		AppUtils.confItems.setMotorItems(motors);
	}

	public void deleteElement(String id, ViewGroup vg, int mType ) {

		setAddMotorToArray(id, mType);
		setButtonsSync();
		super.deleteElement(id, vg);

	}

	public void setAddMotorToArray( String id , int mType ){

		ArrayList<String> motonum = null;
		if(mType == 0){
			motonum = localmotonum;
			localVal--;
		}else
		{
			motonum = remotemotonum;
			remoteVal--;
		}
		if(motonum != null){
			Iterator<String> iter = motonum.iterator();
			Boolean hasElement = false;
			while(iter.hasNext()){
				String str = iter.next();
				if(str.equals(id)){
					hasElement = true;
					break;
				}
			}
			if(!hasElement){
				motonum.add(""+getMotorInt(id));
			}
		}
	}

	public void setRemoveMotorToArray( String id, int mType ){

		ArrayList<String> motonum = null;
		if(mType == 0){
			motonum = localmotonum;
		}else
		{
			motonum = remotemotonum;
		}
		if(motonum != null){
			Iterator<String> iter = motonum.iterator();
			while(iter.hasNext()){
				String str = iter.next();
				if(str.equals(id)){
					iter.remove();
					break;
				}
			}
		}
	}

	private int getMotorInt( String motorId ){

		int num = Integer.parseInt(motorId.substring(5, 6));
		return num;
	}

	public void setMotorId( String pumpName  , String val ) {

		MotorItem mItem = getMotorObjectforTag(pumpName);
		mItem.setItemid("motor"+val) ;

	}

	public void setButtonsSync() {

		addLocalMotorBtn.setText("Add Local Button ("+localmotonum.size()+")");
		addRemoteMotorBtn.setText("Add Remote Button("+remotemotonum.size()+")");

	}


	//// ui stuff from here

	@Override
	public void buildUI(Elements eitem) {

		final MotorItem mitem = (MotorItem)eitem;
		LayoutInflater linf = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

		relativ = (RelativeLayout)linf.inflate(R.layout.motorconfiguration, container, false);

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

		operations.setTag(mitem.getItemid());
		hpvalue.setTag(mitem.getItemid());
		deliverytype.setTag(mitem.getItemid());
		currenttype.setTag(mitem.getItemid());
		EditmotorName.setTag(mitem.getItemid());
		Editminvoltage.setTag(mitem.getItemid());
		Editmaxvoltage.setTag(mitem.getItemid());
		Editlitres.setTag(mitem.getItemid());
		typeSpinner.setTag(mitem.getItemid());
		deleteBtn.setTag(mitem.getItemid());
		relativ.setTag(mitem.getItemid());
		motornum.setTag(mitem.getItemid());
		motornum.setText(mitem.getItemid());


		disableDropDown = true;
		deleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteElement((String)deleteBtn.getTag() , motorLinear , mitem.getMotorTypeint());
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

		EditmotorName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length() == 3){
					MotorItem mItem = getMotorObjectforTag((String)EditmotorName.getTag());
					if(mItem != null)
						mItem.setPumpName(EditmotorName.getText().toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		Editlitres.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length() == 3){
					MotorItem mItem = getMotorObjectforTag((String)Editlitres.getTag());
					if(mItem != null)
						mItem.setWaterDeliveryRate(Editlitres.getText().toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		Editminvoltage.addTextChangedListener( new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length() == 3){
					MotorItem mItem = getMotorObjectforTag((String)Editminvoltage.getTag());
					if(mItem != null)
						mItem.setMinVolts(Editminvoltage.getText().toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		Editmaxvoltage.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length() == 3){
					MotorItem mItem = getMotorObjectforTag((String)Editmaxvoltage.getTag());
					if(mItem != null)
						mItem.setMaxVolts(Editmaxvoltage.getText().toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		motornum.requestFocus();
		motorLinear.addView(relativ);

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
		for (Elements mitem : motors) {
			if(mitem.getItemid().equalsIgnoreCase(motorName))
				mt = (MotorItem)mitem;
		}
		return mt;

	}


	private String getMotorTypeHeading(int val) {

		if(val == 0){
			return "Local Motor";
		}else {
			return "Remote Motor";
		}
	}

}



