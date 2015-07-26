package com.app.controllers;



import com.app.avanicomponents.AvaniTimer;
import com.app.avanstart.util.AppUtils;
import com.app.interfaces.SmsInterface;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony.Sms.Inbox;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.telephony.SmsManager;

@TargetApi(19) public class SmsController implements LoaderCallbacks<Cursor> {

	private static SmsController _smsInstance;
	FragmentActivity activity;
	String SENT = "SMS_SENT";
	String DELIVERED = "SMS_DELIVERED";
	String RECEIVED = "SMS_RECEIVED";
	SmsInterface _delegate;
	String msgId;
	Boolean isLatest = false;
	long currentTime;
	String phoneNum;
	//int x = 0 ;
	

	private SmsController() {
		// TODO Auto-generated constructor stub
	}

	public static SmsController getSmsInstance() {

		if(_smsInstance == null)
			_smsInstance = new SmsController();
		return _smsInstance;

	}
	
	private void init() {
		isLatest = false;
		currentTime = System.currentTimeMillis();
	}
	
	public void destruct() {
		_smsInstance = null;
	}

	public void registerSendSMS(String sms, String msgId, SmsInterface _delegate, String phoneNum, FragmentActivity _cxt) {
		this.activity = _cxt;
		_cxt.getSupportLoaderManager().initLoader(SmsQuery.TOKEN, null, this);
		//x = smsStrs.length;
		init();
		this.msgId = msgId;
		this._delegate = _delegate;
		this.phoneNum = phoneNum;
		sendSMS(phoneNum, sms);

	}
	
	

	private void sendSMS(String phoneNumber, String message)
	{        

		PendingIntent sentPI = PendingIntent.getBroadcast(activity, 0,
				new Intent(SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(activity, 0,
				new Intent(DELIVERED), 0);
		
		unRegisterReceivers();

		//---when the SMS has been sent---
		activity.registerReceiver(msendReceiver, new IntentFilter(SENT));

		//---when the SMS has been delivered---
		activity.registerReceiver(mDeliverReceiver, new IntentFilter(DELIVERED));   


		SmsManager sms = SmsManager.getDefault();
		isLatest = true;
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
	}
	
	BroadcastReceiver msendReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			switch (getResultCode())
			{
			case Activity.RESULT_OK:
				_delegate.onSmsSent(msgId);
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				_delegate.onSmsFailed(msgId, "Generic Failure");
				break;
			case SmsManager.RESULT_ERROR_NO_SERVICE:
				_delegate.onSmsFailed(msgId, "No Sms Service available, check if you have network");
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				_delegate.onSmsFailed(msgId, "Null PDU");
				break;
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				_delegate.onSmsFailed(msgId, "Radio Off");
				break;
			}
		}
	};
	
	BroadcastReceiver mDeliverReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent intent) {
			switch (getResultCode())
			{
			case Activity.RESULT_OK:
				_delegate.onSmsDelivered(msgId);
				isLatest = true;
				break;
			case Activity.RESULT_CANCELED:
				_delegate.onSmsFailed(msgId, "Sms cancelled");
				break;                        
			}

		}
	};

	public void messageSuccess( String msg ) {
		AvaniTimer.getInstance().cancelTimer();
		_delegate.onSmsRec(msgId, msg);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		// TODO Auto-generated method stub
		if (i == SmsQuery.TOKEN) {
			// This will fetch all SMS messages in the inbox, ordered by date desc
			return new CursorLoader(this.activity, SmsQuery.CONTENT_URI, SmsQuery.PROJECTION, null, null,
					SmsQuery.SORT_ORDER);
		}
		return null;

	}
	
	public void unRegisterReceivers() {
		try {
			activity.unregisterReceiver(msendReceiver);
			activity.unregisterReceiver(mDeliverReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		// TODO Auto-generated method stub
		if (cursorLoader.getId() == SmsQuery.TOKEN && cursor != null) {
			cursor.moveToFirst();
			String address = cursor.getString(SmsQuery.ADDRESS);
			String body = cursor.getString(SmsQuery.BODY);
			long smsTime = Long.parseLong(cursor.getString(SmsQuery.DATE));

			if(address.contains(phoneNum)  && isLatest && smsTime > currentTime && (body.toLowerCase().contains("ok") || body.toLowerCase().contains("er") )) {

				String latestSms = "Address=&gt; "+address+"n SMS =&gt; "+body;
				//onSmsReceived(latestSms);
				isLatest = false;
				messageSuccess(latestSms);
			}

		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * A basic SmsQuery on android.provider.Telephony.Sms.Inbox
	 */
	private interface SmsQuery {
		int TOKEN = 1;

		static final Uri CONTENT_URI = Inbox.CONTENT_URI;

		static final String[] PROJECTION = {
			Inbox._ID,
			Inbox.ADDRESS,
			Inbox.BODY,
			Inbox.DATE
		};

		static final String SORT_ORDER = Inbox.DEFAULT_SORT_ORDER;

		int ID = 0;
		int ADDRESS = 1;
		int BODY = 2;
		int DATE = 3;
	}
}
