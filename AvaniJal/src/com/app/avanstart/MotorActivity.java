package com.app.avanstart;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.avanstart.util.AppUtils;
import com.app.beans.ConfigItem;
import com.app.beans.Elements;
import com.app.beans.MotorItem;
import com.app.controllers.MotorController;
import com.app.controllers.SmsController;
import com.app.parsers.SmsParser;
import com.app.parsers.SmsParser.MessageHolder;

public class MotorActivity extends Activity {

	AlertDialog alert;
	LinearLayout llayout;
	MotorController motor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_motor);

		if(AppUtils.confItems == null) {
			AppUtils.confItems = new ConfigItem();
		}


		Button setconfig = (Button)findViewById(R.id.setconfigbtn);
		setconfig.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(applyMotorValidations())
					sendConfig();
			}
		});
		llayout = (LinearLayout)findViewById(R.id.finalmotorlayout);
		llayout.addView(motor().getMotorLayout(null, this, null));
	}
	
	public MotorController motor() {
		if(motor == null) motor = new MotorController();
		return motor;
		
	}

	private void sendConfig() {
		
		HashMap<String, String> smss = AppUtils.buildMotorConfigSMS(motor.elements);
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
			if(data != null){
				Intent i = data;//getIntent();
				HashMap<String, String> smsStr = (HashMap<String, String>) i.getExtras().getSerializable("MESSAGE");

				if(smsStr != null){
					SmsController.getSmsInstance().unRegisterReceivers();
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

		ArrayList<Elements> motorItems =  motor.elements;
//		for (Elements elements : motorItems) {
//			AppUtils.confItems.addMotorItems(elements);
//		}
//		ArrayList<Elements> motors = AppUtils.confItems.getAllMotorItems();
		Iterator<Elements> iter = motorItems.iterator();
		while(iter.hasNext()){
			Elements ele = iter.next();
			ele.setIsConfigured(true);
			AppUtils.confItems.addMotorItems(ele);
		}
		//showDialog("Motors", "Configured Successfully");
		Intent intent=new Intent();  
		intent.putExtra("status","configured"); 
		String[] values = this.getResources().getStringArray(R.array.elements);
		intent.putExtra("element",values[0]); 
		setResult(2001,intent);  
		finish();

	}


	private Boolean applyMotorValidations() {

		Boolean valid = true;
		ArrayList<Elements> motors = motor.elements;//AppUtils.confItems.getMotorItems();

		for (Elements m : motors) {

			MotorItem mt = (MotorItem)m;

			String pumpName = mt.getPumpName();
			if(pumpName == "")
				pumpName = mt.getItemid();

			/// validation for min volts
			if(mt.getMinVolts() == "" ){
				showDialog(pumpName, "The Minimum voltage of the motor must be between 200 to 350");
				valid = false;
				break;
			}

			/// validation for max volts
			if(mt.getMaxVolts() == ""){
				showDialog(pumpName, "The Maximum voltage of the motor must be between 400 to 420");
				valid = false;
				break;
			}

			///validation for litres
			if(mt.getWaterDeliveryRate() == ""){
				showDialog(pumpName, "Enter water delivery rate for the motor");
				valid = false;
				break;
			}

			///validation for remote number

			if(mt.getMotorTypeint() == 1 && mt.getPhoneNum() == ""){
				showDialog(pumpName, "Enter remote phone number for the motor");
				valid = false;
				break;
			}
		}
		return valid;
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


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		//outState.putInt(20, motorLayout.getValue());
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.motor, menu);
		return true;
	}

}
