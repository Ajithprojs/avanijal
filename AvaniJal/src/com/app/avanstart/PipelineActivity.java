package com.app.avanstart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.app.avanstart.util.AppUtils;
import com.app.beans.ConfigItem;
import com.app.beans.Elements;
import com.app.beans.MotorItem;
import com.app.beans.Pipelineitem;
import com.app.controllers.PipelineController;
import com.app.parsers.SmsParser;
import com.app.parsers.SmsParser.MessageHolder;

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

public class PipelineActivity extends Activity {

	RelativeLayout pipelineLayout;
	AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pipeline);
		if(AppUtils.confItems == null) {
			AppUtils.confItems = new ConfigItem();
		}


		Button setconfig = (Button)findViewById(R.id.setconfigbtn);
		setconfig.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(applyPiplelineValidations())
					sendConfig();
			}
		});
		LinearLayout llayout = (LinearLayout)findViewById(R.id.finalpipelinelayout);
		pipelineLayout = PipelineController.getInstance().getPipelineLayout(null, this, null);
		llayout.addView(pipelineLayout);
	}

	private void sendConfig() {

		HashMap<String, String> sms = AppUtils.buildPipeLineConfigSms();
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

		ArrayList<Elements> pipelines = AppUtils.confItems.getPipelineItems();
		Iterator<Elements> iter = pipelines.iterator();
		while(iter.hasNext()){
			Elements ele = iter.next();
			ele.setIsConfigured(true);
		}
		showDialog("Pipeline", "Configured Successfully");
		Intent intent=new Intent();  
		intent.putExtra("status","configured");
		String[] values = this.getResources().getStringArray(R.array.elements);
		intent.putExtra("element",values[1]); 
		setResult(2001,intent);  
		finish();

	}


	private Boolean applyPiplelineValidations() {

		Boolean valid = true;
		ArrayList<Elements> pipes = AppUtils.confItems.getPipelineItems();
		for (Elements m : pipes) {
			Pipelineitem pt = (Pipelineitem)m;
			ArrayList<String> ids = pt.getAllAssociatedElementsOfType(AppUtils.MOTOR_TYPE);

			if(ids.size() == 0){
				valid = false;
				showDialog(pt.getItemid(), "Please associate motor to the pipeline");
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pipeline, menu);
		return true;
	}

}
