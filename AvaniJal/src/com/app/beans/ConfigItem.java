package com.app.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;


public class ConfigItem implements Serializable{

	private static final long serialVersionUID = 46543445; 
	private Hashtable<String, ConfigStatus> elementConfigstatus = new Hashtable<String, ConfigStatus>() ; 
	private ArrayList<Elements> motorItems = new ArrayList<Elements>();
	private ArrayList<Elements> pipelineItems = new ArrayList<Elements>();
	private ArrayList<Elements> filterItems = new ArrayList<Elements>();
	private Hashtable<String, ValveItems> valveItems = new Hashtable<String, ValveItems>();
	private Hashtable<String, SensorItem> sensorItems = new Hashtable<String, SensorItem>();

	public void setElementConfigStatus(Hashtable<String, ConfigStatus> configst) {

		this.elementConfigstatus = configst;
	}

	public void setMotorItems(ArrayList<Elements> obj) {

		this.motorItems = obj;
	}

	public void setPipelineItems(ArrayList<Elements> obj) {

		this.pipelineItems = obj;
	}

	public void setFilterItems(ArrayList<Elements> obj) {

		this.filterItems = obj;
	}

	public void setValveItems(Hashtable<String, ValveItems> obj) {

		this.valveItems = obj;
	}

	public void setSensorItems(Hashtable<String, SensorItem> obj) {

		this.sensorItems = obj;
	}
	
	public Hashtable<String, ConfigStatus> getElementConfigStatus() {

		return elementConfigstatus;
	}

	public ArrayList<Elements>  getMotorItems() {

		return motorItems;
	}

	public ArrayList<Elements>  getPipelineItems() {

		return pipelineItems;
	}

	public ArrayList<Elements> getFilterItems() {

		return filterItems;
	}

	public Hashtable<String, ValveItems> getValveItems() {

		return valveItems ;
	}

	public Hashtable<String, SensorItem> getSensorItems() {

		return sensorItems ;
	}

}
