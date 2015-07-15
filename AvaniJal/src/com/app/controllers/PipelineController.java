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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.avanicomponents.MultiSelectionSpinner;
import com.app.avanstart.R;
import com.app.avanstart.util.AppUtils;
import com.app.beans.Elements;
import com.app.beans.Pipelineitem;
import com.app.interfaces.MultiSelectInterface;

public class PipelineController extends ElementsController implements MultiSelectInterface {


	static PipelineController _instance;

	int MAX_PIPELINE = 0;

	int pipeVal = 0;

	/// holds all the motor elements
	ArrayList<Elements> pipelines;

	/// final motor holder
	LinearLayout pipeLinear;

	Context activity;

	ViewGroup container;

	ArrayAdapter<String> dataAdapter;

	Boolean disableDropDown = false;

	Button addPipelineBtn;

	ArrayList<String> motorIds = new ArrayList<String>();

	
	MultiSelectionSpinner typeSpinner;
	
	boolean isSpinnerOpen = false;


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
		/// adding a empty object so that the dropdowns doesnt show the first object

		pipeVal = 0;
		if(typeSpinner != null)
		typeSpinner.clearAdapter();
		motorIds.clear();
		motorIds.add(" ");
		ArrayList<Elements> motors = AppUtils.confItems.getMotorItems();
		MAX_PIPELINE = motors.size();
		for (Elements elements : motors) {
			motorIds.add(elements.getItemid());
		}
		type = AppUtils.PIPELINE_TYPE;
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
		RelativeLayout lin = (RelativeLayout) oldlinf.inflate(R.layout.pipelineconfigholder, container, false);
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
			if(mt.getIsConfigured()){
			addElement(""+getPipeInt(mt.getItemid()), mt);
			pipeVal++;
			}
		}
		setButtonsSync();
		return lin;
	}

	public void addPipeline() {

		if(MAX_PIPELINE == pipeVal){
			super.showDialog("Pipeline", "We can have only " +MAX_PIPELINE+" pipelines");
		}else {

			Pipelineitem pitem = new Pipelineitem();
			disableDropDown = false;
			pipeVal++;
			super.addElement(""+pipeVal, pitem);
			AppUtils.confItems.setPipelineItems(pipelines);
		}

	}

	public void deletePipeline(String id, ViewGroup vg ) {

		
		pipeVal--;
		setButtonsSync();
		super.deleteElement(id, vg);

	}

	public void setButtonsSync() {

		addPipelineBtn.setText("Add Pipeline ("+(MAX_PIPELINE - pipeVal)+")");

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
		
		
			pipe.setAssociatedElement(id);
			//setRemoveMotorIDfromArray(id);
	}
	
	private void removeSelectedMotor(Pipelineitem pipe , String id) {
		
		if(pipe.hasAssociatedElement(id)){
			pipe.removeAssociatedElement(id);
			//setAddMotorToArray(id);
		}
	}

	@Override
	public void buildUI(Elements eitem) {
		// TODO Auto-generated method stub
		final Pipelineitem pitem = (Pipelineitem)eitem;
		LayoutInflater linf = (LayoutInflater) this.activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		RelativeLayout relativ = (RelativeLayout)linf.inflate(R.layout.pipelineconfi, container, false);

		final TextView pipelineid  = (TextView)relativ.findViewById(R.id.pipelinenumtxt);
		final Button deleteBtn = (Button)relativ.findViewById(R.id.delebtn);
		typeSpinner = (MultiSelectionSpinner)relativ.findViewById(R.id.pipelinespinner);

		pipelineid.setTag(pitem.getItemid());
		deleteBtn.setTag(pitem.getItemid());
		typeSpinner.setTag(pitem.getItemid());
		relativ.setTag(pitem.getItemid());

		pipelineid.setText("Pipeline "+pitem.getItemid());

		typeSpinner.setItems(motorIds , this);
		setAssociatedMotor(pitem);
		setButtonsSync();
		deleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//deleteElement((String)deleteBtn.getTag() , pipeLinear);
				deletePipeline((String)deleteBtn.getTag(), pipeLinear);
			}
		});
		

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

	public void onClick(View v){  
		String s = typeSpinner.getSelectedItemsAsString();  
		Toast.makeText(this.activity, s , Toast.LENGTH_LONG).show();  
	}

	@Override
	public void BeforeSelectDialog(MultiSelectionSpinner spinner) {
		// TODO Auto-generated method stub
		Pipelineitem pitem = getPipelineObjectforTag((String)spinner.getTag());
		//syncSpinnerForPiple(pitem);
		isSpinnerOpen = true;
		//setAssociatedMotor(pitem);
		
	}
	
	public void setAssociatedMotor( Pipelineitem pitem ) {
		ArrayList<String> arrIds = pitem.getAllAssociatedElementsOfType(AppUtils.MOTOR_TYPE);
		typeSpinner.setSelection(arrIds);
	}

	@Override
	public void itemSelected(MultiSelectionSpinner spinner,String val) {
		// TODO Auto-generated method stub
		if(isSpinnerOpen)
		addSelectedMotor(getPipelineObjectforTag((String)spinner.getTag()), val);
		
	}

	@Override
	public void itemDeselected(MultiSelectionSpinner spinner, String val) {
		// TODO Auto-generated method stub
		if(isSpinnerOpen)
			removeSelectedMotor(getPipelineObjectforTag((String)spinner.getTag()), val);
	}  

}
