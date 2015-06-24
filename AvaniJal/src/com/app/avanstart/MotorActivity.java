package com.app.avanstart;

import java.util.ArrayList;
import java.util.Enumeration;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.app.avanstart.util.AppUtils;
import com.app.beans.ConfigItem;
import com.app.beans.ConfigStatus;
import com.app.beans.Elements;
import com.app.beans.MotorItem;
import com.app.controllers.MotorController;
import com.app.parsers.SmsParser;
import com.app.parsers.SmsParser.MessageHolder;

public class MotorActivity extends Activity {

	RelativeLayout motorLayout;
	AlertDialog alert;
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
		LinearLayout llayout = (LinearLayout)findViewById(R.id.finalmotorlayout);
		motorLayout = MotorController.getInstance().getMotorLayout(null, this, null);
		llayout.addView(motorLayout);
	}

	private void sendConfig() {

		String sms = AppUtils.buildConfigSMS();
		Intent i = new Intent(this , SmsActivity.class);
		i.putExtra("phone", AppUtils.phoneNum);
		i.putExtra("msg", sms);
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
			Intent i = data;//getIntent();
			String smsStr = i.getExtras().getString("MESSAGE");
			if(smsStr != null){
				MessageHolder mh = SmsParser.getInstance().getResult(smsStr);
				if(mh != null) {
					if(mh.isError){
						showDialog("motor", "error in configuration");
					}else {
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

		ArrayList<MotorItem> motors = AppUtils.confItems.motorItems;
		Iterator<MotorItem> iter = motors.iterator();
		
		showDialog("Motors", "Configured Successfully");
		Intent intent=new Intent();  
        intent.putExtra("status","configured");  
        setResult(2001,intent);  
		finish();

	}


	private Boolean applyMotorValidations() {

		Boolean valid = true;
		//ArrayList<Elements> moto = (ArrayList<Elements>)AppUtils.confItems.motorItems;
		ArrayList<MotorItem> motors = AppUtils.confItems.motorItems;
		//ArrayList<MotorItem> motors = new ArrayList<MotorItem>();//(ArrayList<MotorItem>)moto;


		for (MotorItem mt : motors) {

			String pumpName = mt.getPumpName();
			if(pumpName == "")
				pumpName = mt.itemId;

			/// validation for min volts
			if(mt.getMinVolts() == ""){
				showDialog(pumpName, "Enter minimum voltage for the motor");
				valid = false;
				break;
			}

			/// validation for max volts
			if(mt.getMaxVolts() == ""){
				showDialog(pumpName, "Enter maximum voltage for the motor");
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


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//syncMotorWithIds();
		/// sync activities


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
