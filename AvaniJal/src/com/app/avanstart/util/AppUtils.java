package com.app.avanstart.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

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

public class AppUtils {

	public static ConfigItem confItems;

	public static CropItem cropItems;

	public static String MOTOR_TYPE = "motor";

	public static String FILTER_TYPE = "filter";

	public static String VALVE_TYPE = "valve";

	public static String SENSOR_TYPE = "sensor";

	public static String VENTURY_TYPE = "ventury";

	public static String phoneNum = "9739287569";

	public static void sendSMS(String phoneNumber, String message , final Activity activity)
	{        
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(activity, 0,
				new Intent(SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(activity, 0,
				new Intent(DELIVERED), 0);

		//---when the SMS has been sent---
		/* activity.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(activity.getBaseContext(), "SMS sent", 
                                Toast.LENGTH_SHORT).show();
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
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(activity.getBaseContext(), "SMS delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(activity.getBaseContext(), "SMS not delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;                        
                }
            }
        }, new IntentFilter(DELIVERED));   */

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
	}
	///for config

	//*AC M1 0 250 420 0 0 800 0 1 1 1234567890 (motor1, 5HP, 250-420V, 3 phase, bore, 800lt/min, starter control, 
	//w.sensors installed, , remote, ph#)

	public static String buildConfigSMS() {

		StringBuffer finalSb = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		/// lets first build for the motors
		
		ConfigItem cItem = AppUtils.confItems;
		ArrayList<Elements> mtItems =  cItem.motorItems;
		if(mtItems != null) {

			for (Elements m : mtItems) {
				MotorItem me = (MotorItem)m;
				sb.append("*AC");
				sb.append(" "+getsmsidfromID(me.itemId)+" ");
				sb.append(" "+me.getHpValeint()+" ");
				sb.append(" "+me.getMinVolts()+" ");
				sb.append(" "+me.getMaxVolts()+" ");
				sb.append(" "+me.getOperationTypeint()+" ");
				sb.append(" "+me.getPumpConnTypenum()+" ");
				sb.append(" "+me.getWaterDeliveryRate()+" ");
				sb.append(" "+me.getDeliveryTypeint()+" ");
				sb.append(" "+me.getCurrentTypeint()+" ");
				sb.append(" "+(me.hasWaterSensor() ? 1:0)+" ");
				sb.append(" "+me.getMotorTypeint()+" ");
				sb.append(" "+me.getPhoneNum()+" ");

			}
		}
		//finalSb.append(sb.toString().length());
		finalSb.append(sb.toString());
		finalSb.append("*en");
		return finalSb.toString();

	}

	public static String buildFilterConfigSms() {

		StringBuffer sb = new StringBuffer();
		/// lets  build for the filters
		sb.append("*AC");
		ConfigItem cItem = AppUtils.confItems;
		Hashtable<String, FilterItem> ftItems = cItem.filterItems;



		if(ftItems != null) {
			int j = 1;
			Enumeration<String> allKeys = ftItems.keys();
			while(allKeys.hasMoreElements()){

				String key = allKeys.nextElement();
				FilterItem fi = ftItems.get(key);
				ArrayList<String> motorIds = fi.getAllAssociatedElementsOfType("motor");
				sb.append(" F"+j+" ");
				sb.append(" "+getsmsidfromID(motorIds.get(0))+" ");
				sb.append(" "+fi.getFrequencyHours()+" ");
				sb.append(" "+fi.getFrequencyminutes()+" ");
				sb.append(" "+fi.getDurationSeconds()+" ");
				j++;

			}
		}
		sb.append("*en");
		return sb.toString();
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
