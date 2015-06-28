package com.app.beans;



public class SensorItem extends Elements {
	
	private static final long serialVersionUID = 46543467;
	private int sensorType;   /// 0 = water sensor , 1 = soil sensor
	
	public void setValveId( int id ) {
		this.sensorType = id;
	}
	
	public int getValveId() {
		return this.sensorType;
	}

}
