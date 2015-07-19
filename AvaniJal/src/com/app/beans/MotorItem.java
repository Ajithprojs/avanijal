package com.app.beans;


import java.io.Serializable;

import android.util.Log;

public class MotorItem extends Elements implements Serializable{
	
	private static final long serialVersionUID = 46543448;

	/// user name for the pump
	private String pumpName = "";

	/// if its a 5 or 7.5 or 10 HP motor
	private int hpVal = 0;

	/// if its a 2 phase or 3 phase operation
	private int operationType = 1; /// [0 = 2phase 1 = 3phase]

	/// if its irrigation or fertigation
	private int agriType = 0; ///[0 = irrigation , 1 = fertigation]

	//// if its remote or local
	private int motorType = 0; ///[0 = remote , 1 = local]

	/// if its a remote name then the phone number
	private String phoneNum = "";

	/// max and min volts for the motor
	private String maxVolts = "";

	private String minVolts = "";

	//// where is the pump connected
	private int pumpConn = 0; /// 0 = Bore , 1 = open well , 2 = pond , 3 = river

	/// water delivery per minute in liters
	private String waterDeliveryRate = "";

	///delivery type
	private int deliveryType = 0 ; // 0 = single , 1 = combined

	//// current measurement
	private int currentMeasurement = 0;  // 0 = starter current , 1 = CT

	//// has water level sensors
	private Boolean HasWaterSensors = false;
	
	public void setPumpName(String name){ this.pumpName = name;}

	public String getPumpName(){ return this.pumpName; }

	public void setHpVal(int val){ this.hpVal = val; }

	public void setOperationType( int val ){ this.operationType = val; }

	public void setAgriType( int val ){ this.agriType = val; }

	public void setMotorType( int val ){ this.motorType = val; }

	public void setDeliveryType( int val ){ this.deliveryType = val; }

	public void setWaterDeliveryRate( String val ) { 
		int intval = Integer.parseInt(val);
		if( intval >= 100 && intval <= 2000){
			this.waterDeliveryRate = val;
		}else{
			this.waterDeliveryRate = "";
			Log.e("Element Error", "Invalid water delivery");
		}
		//this.waterDeliveryRate = val; 
		}

	public void setCurrentDelivery( int val ) { this.currentMeasurement = val; }

	public void setWaterSensor( Boolean val ) { this.HasWaterSensors = val; }
	

	public void setMinVolts(String val){ 
		int intval = Integer.parseInt(val);
		if( intval >= 200 && intval <= 380){
			this.minVolts = val;
		}else{
			this.minVolts = "";
			Log.e("Element Error", "Invalid min volts");
		}
	}

	public void setMaxVolts(String val){ 
		int intval = Integer.parseInt(val);
		if(intval >= 400  && intval <= 420){
			this.maxVolts = val;
		}else{
			Log.e("Element Error", "Invalid min volts");
		}
	}

	public void setPumpConn( int val ){ this.pumpConn = val; }

	public void setPhoneNum( String val ){ 
		if(val.length() == 10) {
			this.phoneNum = val; 
		} else {
			Log.e("Element Error", "Invalid phone number");
		}

	}

	public String getMinVolts() { return this.minVolts; }

	public String getMaxVolts() { return this.maxVolts; }

	public String getPhoneNum() {return this.phoneNum;}

	public String getWaterDeliveryRate() { return this.waterDeliveryRate; }

	public Boolean hasWaterSensor() { return this.HasWaterSensors; }
	

	public int getCurrentTypeint() { return this.currentMeasurement; }
	public String getCurrentMeasuerment() {

		switch(this.currentMeasurement){
		case 1:
			return "CT Current";
		default:
			return "Starter Current";
		}
	}

	public int getHpValeint() { return this.hpVal; }
	public String getHPVal() {

		switch(hpVal + 1) {

		case 1 :
			return ">5";

		case 2 :
			return "5";
			
		case 3 :
			return "7.5";
		
		case 4 :
			return "10";

		default :
			return "10+";
		}
	}

	public int getOperationTypeint() { return this.operationType; }
	public String getOpetationType( ) {

		switch(operationType) {

		case 1 :
			return "2 phase";

		default :
			return "3 phase";
		}
	}

	public int getAgriTypeint() { return this.agriType; }
	public String getAgriType( ) {

		switch(agriType) {

		case 0 :
			return "Irrigation";

		default :
			return "Fertigation";
		}
	}

	public int getMotorTypeint() { return this.motorType; }
	public String getMotorType( ) {

		switch(motorType) {

		case 0 :
			return "Local";

		default :
			return "Remote";
		}
	}

	public int getDeliveryTypeint() { return this.deliveryType; }
	public String getDeliveryType( ) {

		switch(deliveryType) {

		case 0 :
			return "Single";

		default :
			return "Combined";
		}
	}

	public int getCurrentMeasureTypeint(){ return this.currentMeasurement; }
	public String getCurrentMeasureType( ) {

		switch(currentMeasurement) {

		case 0 :
			return "Starter";

		default :
			return "CT";
		}
	}

	public int getPumpConnTypenum(){ return this.pumpConn; }
	public String getPumpConnType( ) {

		switch(pumpConn) {

		case 0 :
			return "Bore";

		case 1 :
			return "Open Well";

		case 2 :
			return "Pond";

		default :
			return "River";
		}
	}
	
	public Boolean ValidateItem() {
		
		return true;
	}


}
