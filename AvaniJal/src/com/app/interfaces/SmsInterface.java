package com.app.interfaces;

public interface SmsInterface {
	
	public void onSmsSent( String smsCode );
	
	public void onSmsDelivered( String smsCode );
	
	public void onSmsRec( String smsCode, String resp );
	
	public void onSmsFailed( String smsCode, String desc );
	
	public void onSmsTimeOut();

}
