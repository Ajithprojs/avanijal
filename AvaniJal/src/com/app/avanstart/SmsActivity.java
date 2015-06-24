package com.app.avanstart;




import com.app.avanstart.util.AppUtils;

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
import android.support.v4.widget.SimpleCursorAdapter;
import android.telephony.SmsManager;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(19) public class SmsActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

	TextView smsStatus;
	Activity activity;
	String SENT = "SMS_SENT";
	String DELIVERED = "SMS_DELIVERED";
	String RECEIVED = "SMS_RECEIVED";
	String latestSms = "";
	Boolean isLatest = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		this.activity = this;
		smsStatus = (TextView)findViewById(R.id.smstatuslbl);
		Bundle b = getIntent().getExtras();
		String phone = b.getString("phone");
		String msg = b.getString("msg");
		// Simple query to show the most recent SMS messages in the inbox
		getSupportLoaderManager().initLoader(SmsQuery.TOKEN, null, this);
		//sendSMS(phone, msg);
		sendSMSTempOK();

	}
	
	public void sendSMSTempOK() {
		onSmsReceived("OK");
	}
	public void sendSMS(String phoneNumber, String message)
	{        
		
		PendingIntent sentPI = PendingIntent.getBroadcast(activity, 0,
				new Intent(SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(activity, 0,
				new Intent(DELIVERED), 0);

		//---when the SMS has been sent---
		activity.registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				case Activity.RESULT_OK:
					Toast.makeText(activity.getBaseContext(), "SMS sent", 
							Toast.LENGTH_SHORT).show();
					smsStatus.setText("SMS sent");
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(activity.getBaseContext(), "Generic failure", 
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(activity.getBaseContext(), "No service", 
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(activity.getBaseContext(), "Null PDU", 
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(activity.getBaseContext(), "Radio off", 
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		//---when the SMS has been delivered---
		activity.registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent intent) {
				switch (getResultCode())
				{
				case Activity.RESULT_OK:
					Toast.makeText(activity.getBaseContext(), "SMS delivered", 
							Toast.LENGTH_SHORT).show();
					smsStatus.setText("SMS delivered .. waiting for response please wait");
					isLatest = true;
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(activity.getBaseContext(), "SMS not delivered", 
							Toast.LENGTH_SHORT).show();
					break;                        
				}

			}
		}, new IntentFilter(DELIVERED));   


		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sms, menu);
		return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		// TODO Auto-generated method stub
		if (i == SmsQuery.TOKEN) {
			// This will fetch all SMS messages in the inbox, ordered by date desc
			return new CursorLoader(this, SmsQuery.CONTENT_URI, SmsQuery.PROJECTION, null, null,
					SmsQuery.SORT_ORDER);
		}
		return null;

	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		// TODO Auto-generated method stub
		if (cursorLoader.getId() == SmsQuery.TOKEN && cursor != null) {
			cursor.moveToFirst();
			String address = cursor.getString(SmsQuery.ADDRESS);
			String body = cursor.getString(SmsQuery.BODY);

			if(address.contains(AppUtils.phoneNum)  && isLatest == true) {

				latestSms = "Address=&gt; "+address+"n SMS =&gt; "+body;
				onSmsReceived(latestSms);
			}
			
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}
	
	private void onSmsReceived( String msg ) {
		
		Intent intent=new Intent();  
        intent.putExtra("MESSAGE",msg);  
        setResult(1000,intent);  
        finish();//finishing activity  

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
		};

		static final String SORT_ORDER = Inbox.DEFAULT_SORT_ORDER;

		int ID = 0;
		int ADDRESS = 1;
		int BODY = 2;
	}

}
