package com.app.beans;

import java.util.ArrayList;
import java.util.Hashtable;

public class ConfigItem {
	
	public Hashtable<String, ConfigStatus> elementConfigstatus ; 
	//public Hashtable<String, MotorItem>  motorItems;
	public ArrayList<Elements> motorItems;
	public Hashtable<String, FilterItem> filterItems;
	public Hashtable<String, ValveItems> valveItems;
	public Hashtable<String, SensorItem> sensorItems;

}
