package com.app.avanstart.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.provider.Telephony;
import android.telephony.SmsManager;

import com.app.beans.ConfigItem;
import com.app.beans.CropItem;
import com.app.beans.Elements;
import com.app.beans.FilterItem;
import com.app.beans.MotorItem;
import com.app.beans.Pipelineitem;
import com.app.beans.ValveItems;

public class AppUtils {

	public static ConfigItem confItems;

	public static CropItem cropItems;

	public static String MOTOR_TYPE = "M";
	
	public static String PIPELINE_TYPE = "P";

	public static String FILTER_TYPE = "F";

	public static String VALVE_TYPE = "V";

	public static String SENSOR_TYPE = "sensor";

	public static String VENTURY_TYPE = "ventury";
	
	//// sms statuses
	
	public static String SMS_SENT = "sent";
	
	public static String SMS_RECEIVED = "received";
	
	public static String SMS_DELIVERED = "delivered";
	
	public static String SMS_FAILED = "failed";
	
	public static String SMS_CONFIG_SUCCESS = "success";
	
	public static String SMS_TIMEOUT = "timeout";

	public static String phoneNum = "9739287569";
	//public static String phoneNum = "9986003200";
	
	public static String CONFIG_FILE_NAME = "config";
	
	public static String USER_FILE_NAME = "user";
	
	public static int MAX_MOTOR_PER_SMS = 2;
	
	public static String seperator = " ";

	
	///for config

	//*AC M1 0 250 420 0 0 800 0 1 1 1234567890 (motor1, 5HP, 250-420V, 3 phase, bore, 800lt/min, starter control, 
	//w.sensors installed, , remote, ph#)

	public static HashMap<String, String> buildMotorConfigSMS() {

		/// lets first build for the motors
		HashMap<String, String> smss = new HashMap<String, String>();
		
		ConfigItem cItem = AppUtils.confItems;
		ArrayList<Elements> mtItems =  cItem.getMotorItems();
		if(mtItems != null) {
			
			int j = 1;
			int i = mtItems.size();
			int r = 0;
			int x =0 ;
			int n = i;
			if( i > MAX_MOTOR_PER_SMS) {
				j = i % MAX_MOTOR_PER_SMS;
				n = MAX_MOTOR_PER_SMS;
			}
				
			for( x = 1 ; x <= j ; x++ ){
				
				StringBuffer finalSb = new StringBuffer();
				for(int y = 0 ; y < n; y++){
					Elements m = mtItems.get(r);
					StringBuffer sb = new StringBuffer();
					MotorItem me = (MotorItem)m;
//					if(y!= 0){
//						sb.append(" ");
//					}
					sb.append("*AC"+ seperator);
					sb.append(""+me.getItemid() + seperator);
					sb.append(""+me.getHpValeint()+ seperator);
					sb.append(""+me.getMinVolts()+ seperator);
					sb.append(""+me.getMaxVolts()+ seperator);
					sb.append(""+me.getOperationTypeint()+ seperator);
					sb.append(""+me.getPumpConnTypenum()+ seperator);
					sb.append(""+me.getWaterDeliveryRate()+ seperator);
					//sb.append(" "+me.getDeliveryTypeint()+ seperator);
					sb.append(""+me.getCurrentTypeint()+ seperator);
					sb.append(""+(me.hasWaterSensor() ? 1:0)+ seperator);
					//sb.append(" "+me.getMotorTypeint()+ seperator);
					if(me.getPhoneNum().length()>=10)
					sb.append(""+me.getPhoneNum()+ seperator);
					finalSb.append(sb.toString());
					r++;
				}
				
				finalSb.append("*en");
				smss.put("motor batch :"+x, finalSb.toString());
			}
			
		}
		return smss;

	}
	
	public static HashMap<String, String> buildPipeLineConfigSms() {
		
		HashMap<String, String> smss = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		/// lets  build for the filters
		ConfigItem cItem = AppUtils.confItems;
		ArrayList<Elements> pipelines = cItem.getPipelineItems();
		Iterator<Elements> iter = pipelines.iterator();
		while(iter.hasNext()) {
			Pipelineitem ele = (Pipelineitem)iter.next();
			sb.append("*AC"+ seperator);
			sb.append(ele.getItemid()+ seperator);
			
			ArrayList<String> assoids = ele.getAllAssociatedElementsOfType(MOTOR_TYPE);
			Iterator<String> assoiter = assoids.iterator();
			while(assoiter.hasNext()){
				String str = assoiter.next();
				sb.append(str+ seperator);
			}
			
		}
		
		sb.append("*en"+ seperator);
		smss.put("pipeline", sb.toString());
		return smss;
	}

	public static HashMap<String, String> buildFilterConfigSms() {

		HashMap<String, String> smss = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		/// lets  build for the filters
		
		ConfigItem cItem = AppUtils.confItems;
		ArrayList<Elements> ftItems = cItem.getFilterItems();
		
		Iterator<Elements> iter = ftItems.iterator();
		while(iter.hasNext()) {
			sb.append("*AC"+ seperator);
			FilterItem ele = (FilterItem)iter.next();
			sb.append(ele.getItemid()+ seperator);
			Iterator<String> asso = ele.getAllAssociatedElementsOfType(PIPELINE_TYPE).iterator();
			while(asso.hasNext()){
				sb.append(asso.next()+ seperator);
			}
			
			//sb.append(" "+ele.getFrequencyHours()+" ");
			sb.append(ele.getFrequencyminutes()+ seperator);
			sb.append(ele.getDurationSeconds()+ seperator);
			
		}
		sb.append("*en"+ seperator);
		smss.put("filter", sb.toString());
		return smss;
	}
	
	public static HashMap<String, String> buildValveConfigSms() {

		HashMap<String, String> smss = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		/// lets  build for the filters
		
		ConfigItem cItem = AppUtils.confItems;
		ArrayList<Elements> ftItems = cItem.getValveItems();
		
		Iterator<Elements> iter = ftItems.iterator();
		while(iter.hasNext()) {
			
			ValveItems ele = (ValveItems)iter.next();
			sb.append("*AC"+ seperator);
			sb.append(ele.getItemid()+ seperator);
			
		}
		sb.append("*en");
		smss.put("valve", sb.toString());
		return smss;
	}
	

	public static String getsmsidfromID(String id) {

		String val = "";

		if(id.contains("motor")) {

			int num = Integer.parseInt(id.substring(5, 6));
			val = "M"+num;
		}

		return val;

	}


	public static String getCRC() {

		String crcStr = "";

		return crcStr;

	}
	
	
	public static int getIntFromString( String val ) {
		
		if(val.length() > 0){
			
			return Integer.parseInt(val);
		}
		return 0;
	}

	/**
	 * Check if the device runs Android 4.3 (JB MR2) or higher.
	 */
	public static boolean hasJellyBeanMR2() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2;
	}

	/**
	 * Check if the device runs Android 4.4 (KitKat) or higher.
	 */
	public static boolean hasKitKat() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;
	}


}
