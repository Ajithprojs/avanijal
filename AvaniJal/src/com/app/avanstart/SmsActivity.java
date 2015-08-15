package com.app.avanstart;

import java.util.HashMap;

import com.app.avanicomponents.AvaniTimer;
import com.app.avanstart.util.AppUtils;
import com.app.controllers.SmsController;
import com.app.interfaces.SmsInterface;
import com.app.interfaces.Timerinterface;
import com.app.parsers.SmsParser;
import com.app.parsers.SmsParser.MessageHolder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.widget.TextView;

@TargetApi(19) public class SmsActivity extends FragmentActivity implements SmsInterface, Timerinterface {

	TextView smsStatus;
	Activity activity;

	HashMap<String, String> statusHash;	
	Boolean isLatest = false;
	HashMap<String, String> smsMsgs;
	Object[] keys;
	int i = 0;
	String phoneNum = AppUtils.phoneNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		this.activity = this;
		smsStatus = (TextView)findViewById(R.id.smstatuslbl);
		Bundle b = getIntent().getExtras();
		if(b.containsKey("phone")){
			phoneNum = b.getString("phone");
		}
		phoneNum = AppUtils.getCurrentPhoneNum(this);
		smsMsgs = (HashMap<String, String>) b.getSerializable("MESSAGE");
		statusHash = new HashMap<String, String>();
		// Simple query to show the most recent SMS messages in the inbox
		/// uncomment the following to simulate sms
		if(smsMsgs != null) {
			keys = smsMsgs.keySet().toArray();
		}
		String key = (String)keys[0];
		sendSMSTempOK(key);
		/// comment the following to simulate sms
//		if(smsMsgs != null) {
//			keys = smsMsgs.keySet().toArray();
//			initiateSMS(true);
//		}

	}

	private void initiateSMS( boolean cont ) {

		if(i < keys.length && cont){
			String key = (String)keys[i];
			String sms = smsMsgs.get(key);
			smsStatus.setText("sending sms ("+i+") of "+keys.length +" please wait..");
			startSmsTimeOut(50, key);
			i++;
			SmsController.getSmsInstance().registerSendSMS(sms, key, this, phoneNum, this);
		} else {
			onSmsReceived();
		}
	}

	private void startSmsTimeOut(int seconds, String taskname) {
		AvaniTimer.getInstance().setTimer(this, seconds, taskname);
	}
	
	private void cancelSMSTimer() {
		AvaniTimer.getInstance().cancelTimer();
	}


	public void sendSMSTempOK(String smsCode) {

		SmsManager sms = SmsManager.getDefault();
		//sms.sendTextMessage(AppUtils.phoneNum, null, msg, null, null); 
		statusHash.put(smsCode, AppUtils.SMS_CONFIG_SUCCESS);
		startSmsTimeOut(4, smsCode);
		//onSmsReceived();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sms, menu);
		return true;
	}



	private void onSmsReceived( ) {

		Intent intent=new Intent();  
		intent.putExtra("MESSAGE",statusHash);  
		setResult(1000,intent);  
		SmsController.getSmsInstance().unRegisterReceivers();
		SmsController.getSmsInstance().destruct();
		AvaniTimer.getInstance().destruct();
		finish();//finishing activity  

	}

	@Override
	public void onSmsSent(String smsCode) {
		// TODO Auto-generated method stub
		smsStatus.setText("sms ("+i+") of "+keys.length +" sent..");
		statusHash.put(smsCode, AppUtils.SMS_SENT);

	}



	@Override
	public void onSmsDelivered(String smsCode) {
		// TODO Auto-generated method stub
		smsStatus.setText("sms ("+i+") of "+keys.length +" Delivered..");
		statusHash.put(smsCode, AppUtils.SMS_DELIVERED);
	}



	@Override
	public void onSmsFailed(String smsCode, String desc) {
		// TODO Auto-generated method stub
		smsStatus.setText("sms ("+i+") of "+keys.length +" Failed..");
		statusHash.put(smsCode, desc);
		cancelSMSTimer();
		initiateSMS(false);

	}



	@Override
	public void onSmsTimeOut( String smsCode ) {
		// TODO Auto-generated method stub
		//statusHash.put(smsCode, AppUtils.SMS_SENT);
		smsStatus.setText("sms ("+i+") of "+keys.length +" Timeout..");
		statusHash.put(smsCode, AppUtils.SMS_TIMEOUT);
		cancelSMSTimer();
		initiateSMS(false);
	}



	@Override
	public void onSmsRec(String smsCode,String resp) {
		// TODO Auto-generated method stub
		
		MessageHolder mh = SmsParser.getInstance().getResult(resp);
		if(mh != null) {
			if(mh.isError){
				statusHash.put(smsCode, mh.reason);
				smsStatus.setText("sms ("+i+") of "+keys.length +" Failed..");
			}else {
				statusHash.put(smsCode, AppUtils.SMS_CONFIG_SUCCESS);
				
			}
		}
		cancelSMSTimer();
		//statusHash.put(smsCode, resp);
		initiateSMS(true);
	}

	@Override
	public void onTimerComplete( String taskName ) {
		// TODO Auto-generated method stub
		onSmsReceived();
		//onSmsTimeOut(taskName);
	}

	@Override
	public void onTimerCancelled( String taskName, String reason) {
		// TODO Auto-generated method stub
		
	}

}
