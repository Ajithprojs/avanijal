package com.app.avanstart;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.app.avanicomponents.MultiSelectionSpinner;
import com.app.avanstart.util.AppUtils;
import com.app.beans.AssociationItem;
import com.app.beans.CropItem;
import com.app.beans.Elements;
import com.app.beans.Pipelineitem;
import com.app.beans.ValveItems;
import com.app.interfaces.MultiSelectInterface;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.transition.Visibility;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CropSelectionActivity extends FragmentActivity implements MultiSelectInterface {

	ArrayList<Elements> pipes;
	ArrayList<Elements> valves;
	CropItem crop;
	ArrayList<String> AssopipesIds;
	ArrayList<String> selectedPipeIds;
	ArrayList<String> selectedValveIds;

	private String currentTitle;
	private String cropName;
	private String cropType;
	private int img;

	private int VALVE_SPINNER = 1;
	private int PIPELINE_SPINNER = 78;

	/// the UI components
	private TextView cropNameHolder;
	private LinearLayout imageBg;
	private EditText acreEditText;
	private EditText cropNameEditText;
	
	AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop_selection);

		selectedPipeIds = new ArrayList<String>();
		selectedValveIds = new ArrayList<String>();

		imageBg = (LinearLayout)findViewById(R.id.imgbglayout);
		cropNameHolder = (TextView)findViewById(R.id.cropholder);
		acreEditText = (EditText)findViewById(R.id.acretext);
		cropNameEditText = (EditText)findViewById(R.id.cropname);

		MultiSelectionSpinner pipelineSpinner = (MultiSelectionSpinner)findViewById(R.id.pipelinespinner);
		pipelineSpinner.setTag(PIPELINE_SPINNER);

		Bundle b = getIntent().getExtras();

		if(b.containsKey("currentTitle")  && b.containsKey("cropName")){
			cropName = b.getString("cropName");
			currentTitle = b.getString("currentTitle");
			cropNameHolder.setText(cropName);
		}

		if(b.containsKey("cropType")){
			cropType = b.getString("cropType");
		}

		if(b.containsKey("imgid")) {
			img = b.getInt("imgid");
			imageBg.setBackgroundResource(img);
		}

		if(currentTitle.equalsIgnoreCase("Associated") ) {
			this.crop = AppUtils.assoItems.getCropItem(cropName);
			imageBg.setBackgroundResource(this.crop.getImgId());
			cropNameEditText.setText(this.crop.getCropName());
			acreEditText.setText(""+this.crop.getAcre());
			//selectedValveIds = this.crop.getAllAssociatedElementsOfType(AppUtils.VALVE_TYPE);
			//selectedPipeIds = this.crop.getAllAssociatedElementsOfType(AppUtils.PIPELINE_TYPE);
		} else {
			this.crop = new CropItem();
			this.crop.setImgId(img);
			this.crop.setCropTitle(cropName);
		}

		pipes = AppUtils.confItems.getAllPipelineItems();

		List<String> pipeIds = new ArrayList<String>();
		for (Elements pipe : pipes) {
			pipeIds.add(pipe.getItemid());
		}
		if(pipeIds.size() > 0) {
			pipelineSpinner.setVisibility(View.VISIBLE);
			pipelineSpinner.setItems(pipeIds ,this);
		} else {
			pipelineSpinner.setVisibility(View.GONE);
			TextView pipelinetxt = (TextView)findViewById(R.id.pipelineheading);
			pipelinetxt.setText("Pipeline not configured");
		}

		ArrayList<String> selectedPipes = this.crop.getAllAssociatedElementsOfType(AppUtils.PIPELINE_TYPE);
		pipelineSpinner.setSelection(selectedPipes);


		MultiSelectionSpinner valveSpinner = (MultiSelectionSpinner)findViewById(R.id.valvespinner);
		valveSpinner.setTag(VALVE_SPINNER);
		valves = AppUtils.confItems.getAllValveItems();
		List<String> valveIds = new ArrayList<String>();
		for (Elements valve : valves) {
			if(!valve.isAssociated()) {
			valveIds.add(valve.getItemid());
			
			}
		}
		
		ArrayList<String> selectedValves = this.crop.getAllAssociatedElementsOfType(AppUtils.VALVE_TYPE);
//		Iterator<String> iter1 = selectedValves.iterator();
//		while(iter1.hasNext()){
//			String ele = iter1.next();
//			valveIds.add(ele);
//			
//		}
		for (String str : selectedValves) {
			valveIds.add(str);
			selectedValveIds.add(str);
		}
		if(valveIds.size() > 0) {
			valveSpinner.setVisibility(View.VISIBLE);
			valveSpinner.setItems(valveIds ,this);
			
		}else {
			valveSpinner.setVisibility(View.GONE);
			TextView valvetxt = (TextView)findViewById(R.id.valveheading);
			valvetxt.setText("Valve not configured");
		}
		
		valveSpinner.setSelection(selectedValves);
		
		Button setconf = (Button)findViewById(R.id.configbtn);
		setconf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendConfiguration();
			}
		});
	}

	private void sendConfiguration() {

		if(applyAssociationValidation()) {
			crop.setAcre(AppUtils.getIntFromString(acreEditText.getText().toString()));
			this.cropName = cropNameEditText.getText().toString();
			crop.setCropName(this.cropName);
			sendConfig();
		}

	}

	private Boolean applyAssociationValidation() {
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crop_selection, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub

		pushLanguageScreen();
		return super.onMenuItemSelected(featureId, item);
	}

	private void sendConfig() {
		
		Iterator<String> iter = selectedPipeIds.iterator();
		while (iter.hasNext()) {
			String pipeids = iter.next();
			crop.setAssociatedElement(pipeids);
			
		}
		Iterator<String> iter1 = selectedValveIds.iterator();
		while (iter1.hasNext()) {
			String valvids = iter1.next();
			crop.setAssociatedElement(valvids);
			
		}
		HashMap<String, String> sms = AppUtils.buildCropAssociationSms(cropName , crop);
		Intent i = new Intent(this , SmsActivity.class);
		i.putExtra("phone", AppUtils.phoneNum);
		i.putExtra("MESSAGE", sms);
		i.putExtra("elementid", "");
		startActivityForResult(i, 1000);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 1000:
			/// ok this is from the sms activity

			if(data!= null) {
				Intent i = data;//getIntent();
				HashMap<String, String> smsStr = (HashMap<String, String>) i.getExtras().getSerializable("MESSAGE");

				if(smsStr != null){
					Object[] e = smsStr.keySet().toArray();
					boolean isSuccess = true;
					for( int m = 0 ; m < e.length ; m++ ){
						String key = (String)e[m];
						if(!smsStr.get(key).equals(AppUtils.SMS_CONFIG_SUCCESS)) {
							showDialog("key", smsStr.get(key) );
							isSuccess = false;
							break;
						}
					}

					if(isSuccess){
						// configured successfully
						setConfigured();
					}

				}
			}
			break;
		default:
			break;
		}
	}

	private void setConfigured() {

		ArrayList<Elements> valvelines = AppUtils.confItems.getAllValveItems();
		ArrayList<Elements> pipelines = AppUtils.confItems.getAllPipelineItems();
		crop.setIsConfigured(true);
		ArrayList<String> confvalves = crop.getAllAssociatedElementsOfType(AppUtils.VALVE_TYPE);
		ArrayList<String> confpipes = crop.getAllAssociatedElementsOfType(AppUtils.PIPELINE_TYPE);
		
		Iterator<Elements> iter = valvelines.iterator();
		while(iter.hasNext()){
			Elements ele = iter.next();
			for (String string : confvalves) {
				if(ele.getItemid().equals(string)) {
					//ele.setIsConfigured(true);
					ele.setAssociated(true);
					//AppUtils.confItems.addValveItems(ele);
				}
			}
		}
		//AppUtils.confItems.setValveItems(valvelines);
		Iterator<Elements> iter1 = pipelines.iterator();
		while(iter1.hasNext()){
			Elements ele = iter1.next();
			for (String string : confpipes) {
				if(ele.getItemid().equals(string)) {
					//ele.setIsConfigured(true);
					ele.setAssociated(true);
					//AppUtils.confItems.addPipelineItems(ele);
				}
			}
			
		}
		//AppUtils.confItems.setPipelineItems(pipelines);
		Intent intent=new Intent();  
		intent.putExtra("status","configured");
		AppUtils.assoItems.setCropItem(cropName, crop);
		setResult(2003,intent);  
		finish();

	}

	private void pushLanguageScreen() {

		Intent i = new Intent(this , LanguageSelectionActivity.class);
		startActivityForResult(i, 400);

	}

	@Override
	public void BeforeSelectDialog(MultiSelectionSpinner spinner) {
		// TODO Auto-generated method stub

	}

	@Override
	public void itemSelected(MultiSelectionSpinner spinner, String val) {
		// TODO Auto-generated method stub
		if(spinner.getId() == R.id.pipelinespinner) {
			if(!selectedPipeIds.contains(val)) {
				selectedPipeIds.add(val);
			}
		}else if(spinner.getId() == R.id.valvespinner) {
			if(!selectedValveIds.contains(val)) {
				selectedValveIds.add(val);
			}
		}
	}

	@Override
	public void itemDeselected(MultiSelectionSpinner spinner, String val) {
		// TODO Auto-generated method stub
		String id = val;
		if(spinner.getId() == R.id.pipelinespinner) {

			Iterator<String> iter = selectedPipeIds.iterator();
			while(iter.hasNext()){
				String pitem = (String)iter.next();
				if(pitem.equals(id)) {
					iter.remove();
				}
			}

		}else if(spinner.getId() == R.id.valvespinner) {

			Iterator<String> iter = selectedValveIds.iterator();
			while(iter.hasNext()){
				String vitem = (String)iter.next();
				if(vitem.equals(id)) {
					iter.remove();
				}
			}
		}
	}

	private void showDialog( String motorName , String message ) {

		if(alert == null){

			alert = new AlertDialog.Builder(this).create();
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
}
