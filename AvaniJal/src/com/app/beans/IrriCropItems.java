package com.app.beans;

import java.io.Serializable;
import java.util.Hashtable;

public class IrriCropItems extends Elements implements Serializable {

	private static final long serialVersionUID = 92542137;
	private Hashtable<String, String> timeBasedValves;
	private int mode;

	public void addValve(String valveId, String time) {
		if(timeBasedValves == null) timeBasedValves = new Hashtable<String, String>();
		timeBasedValves.put(valveId, time);

	}

	public String getValve( String valveId ) {
		if( timeBasedValves == null ) timeBasedValves = new Hashtable<String, String>();
		return timeBasedValves.get(valveId);
	}

	public Hashtable<String, String> getAllValves() {
		if( timeBasedValves == null ) timeBasedValves = new Hashtable<String, String>();
		return timeBasedValves;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}


}
