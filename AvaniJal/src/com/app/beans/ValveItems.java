package com.app.beans;

import java.io.Serializable;
import java.util.Hashtable;

import android.R.integer;

public class ValveItems extends Elements implements Serializable {
	
	private static final long serialVersionUID = 46543437;
	private int valveId;
	private Hashtable<String, integer> valveConfig;
	
	public void setValveId( int id ) {
		this.valveId = id;
	}
	public void setValveConfig(Hashtable<String, integer> conf) {
		this.valveConfig = conf;
	}
	
	public int getValveId() {
		return this.valveId;
	}
	public Hashtable<String, integer> getValveConfig() {
		return this.valveConfig;
	}

}
