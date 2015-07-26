package com.app.avanstart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.app.avanstart.util.AppUtils;
import com.app.beans.ConfigItem;
import com.app.beans.Elements;
import com.app.beans.Pipelineitem;
import com.app.controllers.PipelineController;
import com.app.controllers.VenturyController;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class VenturyFertigationActivity extends Activity {

	VenturyController ventury;
	AlertDialog alert;
	AlertDialog alert1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ventury_fertigation);
		if(AppUtils.confItems == null) {
			AppUtils.confItems = new ConfigItem();
		}


		Button setconfig = (Button)findViewById(R.id.setconfigbtn);
		setconfig.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(applyVenturyValidations())
					sendConfig();
					//showDialogWithOptions("configure" , "the selected valves are for ventury and the selected motors will be used for fertigation",0,"Send SMS");
			}
		});
		LinearLayout llayout = (LinearLayout)findViewById(R.id.finalventurylayout);
		llayout.addView(ventury().getVenturyLayout(null, this, null));
	}
	
	private VenturyController ventury() {
		if(ventury == null) ventury = new VenturyController();
		return ventury;
	}
	
	private void showDialogWithOptions( String title , String msg, int val, String btnTitle ) {
		alert1 = null;
		if(alert1 == null){

			alert1 = new AlertDialog.Builder(this).create();
			alert1.setButton(AlertDialog.BUTTON_POSITIVE, btnTitle,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					switch(which){
					
					case 1 :
						sendConfig();
						break;
					
					default:
						dialog.dismiss();
						break;
					}

				}
			});

			alert1.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();

				}
			});

		}

		alert1.setTitle(title);
		alert1.setMessage(msg);
		alert1.show();
	}
	
	private void sendConfig() {

		HashMap<String, String> sms = AppUtils.buildVenturyConfigSms();
		Intent i = new Intent(this , SmsActivity.class);
		i.putExtra("phone", AppUtils.phoneNum);
		i.putExtra("MESSAGE", sms);
		i.putExtra("elementid", "");
		startActivityForResult(i, 1000);

	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		
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

		ArrayList<Elements> venturt = AppUtils.confItems.getVenturyFertigationItems();
		Iterator<Elements> iter = venturt.iterator();
		while(iter.hasNext()){
			Elements ele = iter.next();
			ele.setIsConfigured(true);
		}
		showDialog("Ventury", "Configured Successfully");
		Intent intent=new Intent();  
		intent.putExtra("status","configured");
		String[] values = this.getResources().getStringArray(R.array.elements);
		intent.putExtra("element",values[4]); 
		setResult(2001,intent);  
		finish();

	}


	private Boolean applyVenturyValidations() {

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
		getMenuInflater().inflate(R.menu.ventury_fertigation, menu);
		return true;
	}

}
