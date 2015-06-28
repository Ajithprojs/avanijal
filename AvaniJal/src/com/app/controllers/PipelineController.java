package com.app.controllers;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.avanicomponents.MultiSelectionSpinner;
import com.app.avanstart.R;
import com.app.avanstart.util.AppUtils;
import com.app.beans.Elements;
import com.app.beans.MotorItem;
import com.app.beans.Pipelineitem;

public class PipelineController extends ElementsController {


	static PipelineController _instance;

	int MAX_PIPELINE = 0;


	/// holds all the motor elements
	ArrayList<Elements> pipelines;

	/// relative layout for each motor
	RelativeLayout relativ;

	/// final motor holder
	LinearLayout pipeLinear;

	RelativeLayout lin;


	Context activity;

	ViewGroup container;

	ArrayAdapter<String> dataAdapter;

	Boolean disableDropDown = false;

	Button addPipelineBtn;

	ArrayList<String> motorIds = new ArrayList<String>();

	ArrayList<String> selectedMotorIds = new ArrayList<String>();



	private PipelineController() {


	}

	public static PipelineController getInstance() {

		if(_instance == null)
			_instance = new PipelineController();
		return _instance;

	}

	private void init(Context cxt) {

		/*
		 * send objects to the super class for addition and deletion
		 * 
		 * */

		if(AppUtils.confItems.getPipelineItems() == null)
			pipelines = new ArrayList<Elements>();
		else
		{
			pipelines = AppUtils.confItems.getPipelineItems();

		}
		ArrayList<Elements> motors = AppUtils.confItems.getMotorItems();
		MAX_PIPELINE = motors.size();
		for (Elements elements : motors) {
			motorIds.add(elements.getItemid());
		}
		type = "pipeline";
		super.elements = pipelines;
		super.max = MAX_PIPELINE;
		super.type = type;
		super.activity = cxt;

	}

	public RelativeLayout getPipelineLayout( ViewGroup con ,  Context act , LinearLayout llayout ) {


		this.container = con;
		this.activity = act;
		init(act);


		LayoutInflater oldlinf = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		lin = (RelativeLayout) oldlinf.inflate(R.layout.pipelineconfigholder, container, false);
		pipeLinear = (LinearLayout)lin.findViewById(R.id.pipelinelayout);

		addPipelineBtn = (Button)lin.findViewById(R.id.addpipelinebtn);


		addPipelineBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addPipeline();
			}
		});
		ArrayList<Elements> tempPipes = (ArrayList<Elements>)pipelines.clone();
		for (Elements mt : tempPipes) {

			disableDropDown = true;
			addElement(""+getPipeInt(mt.getItemid()), mt);
			//buildUI((Pipelineitem)mt);
		}

		return lin;
	}

	public void addPipeline() {

		if(MAX_PIPELINE == localVal){
			super.showDialog("Pipeline", "We can have only" +MAX_PIPELINE+"pipelines");
		}else {
			Pipelineitem pitem = new Pipelineitem();

			disableDropDown = false;
			super.addElement(""+localVal, pitem);
			AppUtils.confItems.setPipelineItems(pipelines);
		}

	}

	public void deletePipeline(String id, ViewGroup vg, int mType ) {

		setButtonsSync();
		super.deleteElement(id, vg);

	}

	public void setButtonsSync() {

		addPipelineBtn.setText("Add Pipeline ("+elementsnum.size()+")");

	}
	private int getPipeInt( String pipeId ){

		int indx = type.length();
		int num = Integer.parseInt(pipeId.substring(indx, indx+1));
		return num;
	}

	public void syncSpinnerForPiple( Pipelineitem pipe ) {


		ArrayList<String> selectedItems = pipe.getAllAssociatedElementsOfType(type);
		for (String itemids : selectedItems) {
			if(!motorIds.contains(itemids)) {
				motorIds.add(itemids);
			}

		}

	}

	public void setRemoveMotorIDfromArray( String id ){

		if(motorIds != null){
			Iterator<String> iter = motorIds.iterator();
			while(iter.hasNext()){
				String str = iter.next();
				if(str.equals(id)){
					iter.remove();
					break;
				}
			}
		}
	}
	public void setAddMotorToArray( String id ) {
		motorIds.add(id);
	}

	private void addSelectedMotor( Pipelineitem pipe , String id ) {

		ArrayList<String> motors = pipe.getAllAssociatedElementsOfType(type);
		if(!motors.contains(id)){
			motors.add(id);
			setRemoveMotorIDfromArray(id);
		} else {
			motors.remove(id);
			setAddMotorToArray(id);
		}
	}

	@Override
	public void buildUI(Elements eitem) {
		// TODO Auto-generated method stub
		final Pipelineitem pitem = (Pipelineitem)eitem;
		LayoutInflater linf = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		relativ = (RelativeLayout)linf.inflate(R.layout.pipelineconfi, container, false);

		final TextView pipelineid  = (TextView)relativ.findViewById(R.id.pipelinenumtxt);
		final Button deleteBtn = (Button)relativ.findViewById(R.id.delebtn);
		final MultiSelectionSpinner typeSpinner = (MultiSelectionSpinner)relativ.findViewById(R.id.pipelinespinner);

		pipelineid.setTag(pitem.getItemid());
		deleteBtn.setTag(pitem.getItemid());
		typeSpinner.setTag(pitem.getItemid());
		relativ.setTag(pitem.getItemid());

		typeSpinner.setItems(motorIds);

		deleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				deleteElement((String)deleteBtn.getTag() , pipeLinear);
			}
		});

		typeSpinner.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
//				if(!disableDropDown) {
//					syncSpinnerForPiple(getPipelineObjectforTag((String)v.getTag()));
//					ArrayList<String> arrIds = pitem.getAllAssociatedElementsOfType(type);
//					typeSpinner.setSelection(arrIds);
//				}
				return false;
			}
		});

		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> v, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				addSelectedMotor(getPipelineObjectforTag((String)v.getTag()), (String)v.getTag());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		disableDropDown = false;
		pipeLinear.addView(relativ);

	}

	private Pipelineitem getPipelineObjectforTag( String pipeName ) {

		Pipelineitem mt = null;
		for (Elements mitem : pipelines) {
			if(mitem.getItemid().equalsIgnoreCase(pipeName))
				mt = (Pipelineitem)mitem;
		}
		return mt;

	}

}
