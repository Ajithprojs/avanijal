package com.app.avanstart;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import com.app.avaniadapters.IrrigationListViewAdapter;
import com.app.avanstart.util.AppUtils;
import com.app.beans.CropItem;
import com.app.beans.Elements;
import com.app.beans.IrriCropItems;
import com.app.interfaces.CheckboxInterface;
import com.app.interfaces.IrrigationInterface;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class IrrigationActivity extends Activity implements IrrigationInterface  {

	private RadioGroup irriType;
	private Hashtable<String, String> valves;
	private ListView valveList;
	int mode;
	int TIME_BASED = 1;
	int VOLUME_BASED = 2;
	int IRRI_START = 1;
	int IRRI_STOP = 2;
	AlertDialog alert;
	private String currentTitle;
	private int imgId;
	private String acreVal;
	private String pipelineVal;
	IrrigationListViewAdapter adapter;
	int runningmode;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_irrigation);

		Bundle b = getIntent().getExtras();
		String title = b.getString("cropname");
		this.currentTitle = title;
		this.imgId = b.getInt("imageid");
		this.acreVal = b.getString("acre");
		this.pipelineVal = b.getString("pipeline");
		IrriCropItems citem = (IrriCropItems)AppUtils.irriItems.getCropItem(title);
		CropItem crop = (CropItem)AppUtils.assoItems.getCropItem(title);
		ArrayList<String> allvalves = crop.getAllAssociatedElementsOfType(AppUtils.VALVE_TYPE);
		if(citem == null) {
			citem = new IrriCropItems();

			for (String string : allvalves) {
				citem.addValve(string, "");
				citem.setMode(TIME_BASED);
			}
			AppUtils.irriItems.setCropItem(title, citem);
		} 
		irriType = (RadioGroup)findViewById(R.id.irrigroup);
		valveList = (ListView)findViewById(R.id.valveslist);
		valveList.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
		valves = citem.getAllValves();

		mode = TIME_BASED;
		adapter = new IrrigationListViewAdapter(this, 0, valves, new ArrayList<String>(this.valves.keySet()), mode, this);
		valveList.setAdapter(adapter);

		TextView cropnameholder = (TextView)findViewById(R.id.cropholder);
		TextView acreholder = (TextView)findViewById(R.id.acretext);
		TextView pipelineholder = (TextView)findViewById(R.id.selectedpipeline);
		LinearLayout imgbg = (LinearLayout)findViewById(R.id.imgbglayout);

		imgbg.setBackgroundResource(this.imgId);
		cropnameholder.setText(this.currentTitle);
		acreholder.setText(this.acreVal);
		pipelineholder.setText("selected pipeline :"+this.pipelineVal);



		irriType.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				setCheckChanged(arg1);
			}
		});

		if(citem.getMode() == TIME_BASED){
			irriType.check(R.id.timerirri);
		}else if(citem.getMode() == VOLUME_BASED){
			irriType.check(R.id.volumeirri);
		}

		Button startBtn = (Button)findViewById(R.id.startirribtn);
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendConfig(IRRI_START);
			}
		});

		Button stopBtn = (Button)findViewById(R.id.stopirribtn);
		stopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendConfig(IRRI_STOP);
			}
		});
	}

	private void setCheckChanged(int val) {
		if(val == R.id.timerirri){
			/// time based selected
			mode = TIME_BASED;

		} else if(val == R.id.volumeirri){
			/// volumes based selected
			mode = VOLUME_BASED;
		}
		adapter.mode = mode;
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.irrigation, menu);
		return true;
	}

	@Override
	public void onItemSelected(String name, String value) {
		// TODO Auto-generated method stub

		if(valves.contains(name)) {
			valves.remove(name);
		}
		valves.put(name, value);

	}

	@Override
	public void onItemDeSelected(String name) {
		// TODO Auto-generated method stub
		if(valves.contains(name)) {
			valves.remove(name);
		}
		valves.put(name, "0");
	}

	private void sendConfig(int mode) {

		IrriCropItems citem = (IrriCropItems)AppUtils.irriItems.getCropItem(this.currentTitle);
		Enumeration<String> valvekeys = valves.keys();
		while(valvekeys.hasMoreElements()){
			String key = valvekeys.nextElement();
			String val = valves.get(key);
			if( val.length() > 0) {
				if(mode == TIME_BASED){
					citem.setMode(TIME_BASED);
				}else {
					citem.setMode(VOLUME_BASED);
				}
				citem.addValve(key, val);
			}
		}
		HashMap<String, String> smss  = null;
		if(mode == IRRI_START){
			runningmode = IRRI_START;
			smss  = AppUtils.buildTimeCropIrrigationSms(this.pipelineVal, citem.getAllValves(), citem.getMode());
		}else if(mode == IRRI_STOP){
			runningmode = IRRI_STOP;
			smss  = AppUtils.buildTimeCropIrrigationSms(this.pipelineVal, citem.getAllValves(), citem.getMode());
		}
		Intent i = new Intent(this , SmsActivity.class);
		i.putExtra("phone", AppUtils.phoneNum);
		i.putExtra("MESSAGE", smss);
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

		IrriCropItems citem = (IrriCropItems)AppUtils.irriItems.getCropItem(this.currentTitle);
		citem.setIsConfigured(true);
		Intent intent=new Intent();  
		if(runningmode == IRRI_START)
			intent.putExtra("status","Active");
		else if(runningmode == IRRI_STOP)
			intent.putExtra("status","Stopped");
		intent.putExtra("element",this.currentTitle);
		AppUtils.irriItems.setCropItem(this.currentTitle, citem);
		setResult(2004,intent);  
		finish();

	}

	@Override
	public void showSelectError() {
		// TODO Auto-generated method stub
		showDialog("Valve Error", "Cannot select a valve unless you enter a value");
	}

	private void showDialog( String motorName , String message ) {


		alert = new AlertDialog.Builder(this).create();
		alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.setTitle(motorName);
		alert.setMessage(message);
		alert.show();

	}

}
