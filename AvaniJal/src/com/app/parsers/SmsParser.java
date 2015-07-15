package com.app.parsers;

import com.app.beans.Elements;


public class SmsParser {

	static SmsParser parserInstance;
	private String smsok = "ok";
	private String smsFailReason = "SMS Failed, Check your inputs";
	private int errMsgStrtIndx = 3;
	private int spaceIndx = 1;
	private int fieldIndx = 2;
	private int err1Indx = 3;
	private int err2Indx = 3;
	private int sysErrIndx = 3;

	private SmsParser() {

	}
	public static SmsParser getInstance() {

		if(parserInstance == null){

			parserInstance = new SmsParser();
		}
		return parserInstance;

	}

	public MessageHolder getResult( String msg ) {

		MessageHolder result = new MessageHolder();

		if(msg.toLowerCase().contains(smsok)){
			/// success
			result.isError = false;
			result.reason = "success";

		}else if( msg.toLowerCase().contains("er") ){

			/// error condition
			result.isError = true;
			result.reason = smsFailReason;

			int initIndx = errMsgStrtIndx + spaceIndx;

			/// get the errorfield
			if( msg.substring(initIndx, fieldIndx) != null ){
				result.failedField = Integer.parseInt(msg.substring(initIndx, fieldIndx));
				initIndx+=spaceIndx;
			}

			/// get the error 1 field
			if( msg.substring(initIndx, err1Indx) != null ){
				result.err1 = Integer.parseInt(msg.substring(initIndx, err1Indx));
				initIndx+=spaceIndx;
			}

			//// get the error 2 field
			if( msg.substring(initIndx, err2Indx) != null ){
				result.err1 = Integer.parseInt(msg.substring(initIndx, err2Indx));
				initIndx+=spaceIndx;
			}

			//// get the system err field
			if( msg.substring(initIndx, sysErrIndx) != null ){
				result.err1 = Integer.parseInt(msg.substring(initIndx, sysErrIndx));
				initIndx+=spaceIndx;
			}
		} else {
			result.isError = false;
			result.reason = "invalid";
		}

		return result;

	}

	public class MessageHolder {

		public Boolean isError;

		public String reason;

		public int failedField;

		public int err1;

		public int err2;

		public int syserr;
	}

}
