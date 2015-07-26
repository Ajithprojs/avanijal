package com.app.avanstart;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.app.beans.Pipelineitem;
import com.app.controllers.ValveController;

public class ValveActivity extends Activity {

	AlertDialog alert;
	private ValveController valve;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_valve);
		if(AppUtils.confItems == null) {
			AppUtils.confItems = new ConfigItem();
		}
		
		Button setconfig = (Button)findViewById(R.id.setvalvconfigbtn);
		setconfig.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(applyValveValidations())
					sendConfig();
			}
		});
		
		LinearLayout llayout = (LinearLayout)findViewById(R.id.valvelayout);
		llayout.addView(valve().createValveLayout(null, null, this));
	}
	
	private ValveController valve() {
		if(valve == null) valve = new ValveController();
		return valve;
	}
	
	private void sendConfig() {

		HashMap<String, String> sms = AppUtils.buildValveConfigSms();
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

		ArrayList<Elements> valvelines = AppUtils.confItems.getValveItems();
		Iterator<Elements> iter = valvelines.iterator();
		while(iter.hasNext()){
			Elements ele = iter.next();
			ele.setIsConfigured(true);
		}
		//showDialog("valve", "Configured Successfully");
		Intent intent=new Intent();  
		intent.putExtra("status","configured");
		String[] values = this.getResources().getStringArray(R.array.elements);
		intent.putExtra("element",values[3]); 
		setResult(2001,intent);  
		finish();

	}


	private Boolean applyValveValidations() {

		Boolean valid = true;

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.valve, menu);
		return true;
	}

}
