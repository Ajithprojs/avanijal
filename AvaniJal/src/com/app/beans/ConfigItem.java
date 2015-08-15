package com.app.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;


public class ConfigItem implements Serializable{

	private static final long serialVersionUID = 46543905; 
	private Hashtable<String, ConfigStatus> elementConfigstatus = new Hashtable<String, ConfigStatus>() ; 
	private ArrayList<Elements> motorItems = new ArrayList<Elements>();
	private ArrayList<Elements> pipelineItems = new ArrayList<Elements>();
	private ArrayList<Elements> filterItems = new ArrayList<Elements>();
	private ArrayList<Elements> valveItems = new ArrayList<Elements>();
	private ArrayList<Elements> venturyFertigationItems = new ArrayList<Elements>();
	private Hashtable<String, SensorItem> sensorItems = new Hashtable<String, SensorItem>();

	public void setElementConfigStatus(Hashtable<String, ConfigStatus> configst) {

		this.elementConfigstatus = configst;
	}
	
	public void addMotorItems(Elements obj) {
		if(motorItems.contains(obj)) {
			motorItems.remove(obj);
		}
		motorItems.add(obj);
	}
	public ArrayList<Elements> getAllMotorItems() {
		return motorItems;
	}

	public void addPipelineItems(Elements obj) {
		if(pipelineItems.contains(obj)) {
			pipelineItems.remove(obj);
		}
		pipelineItems.add(obj);
	}
	
//	public void setPipelineItems(ArrayList<Elements> obj) {
//
//		this.pipelineItems = obj;
//	}

	public void setFilterItems(ArrayList<Elements> obj) {

		this.filterItems = obj;
	}

	public void setValveItems(ArrayList<Elements> obj) {

		this.valveItems = obj;
	}
	
	public void setVenturyItems(ArrayList<Elements> obj) {

		this.venturyFertigationItems = obj;
	}
	
	public void setSensorItems(Hashtable<String, SensorItem> obj) {

		this.sensorItems = obj;
	}
	
	public Hashtable<String, ConfigStatus> getElementConfigStatus() {

		return elementConfigstatus;
	}

	public ArrayList<Elements>  getAllPipelineItems() {

		return pipelineItems;
	}

	public ArrayList<Elements> getFilterItems() {

		return filterItems;
	}

	public ArrayList<Elements> getValveItems() {

		return valveItems ;
	}
	
	public ArrayList<Elements> getVenturyFertigationItems() {

		return venturyFertigationItems ;
	}
	

	public Hashtable<String, SensorItem> getSensorItems() {

		return sensorItems ;
	}

}
