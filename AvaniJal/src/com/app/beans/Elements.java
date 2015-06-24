package com.app.beans;

import java.util.ArrayList;

public abstract class Elements {

	public String itemId;

	public String type;

	public boolean isEnabled;

	////is configured by sending sms success

	private boolean isConfigured;

	/// other elements associated ids
	private ArrayList<String> ids = new ArrayList<String>();

	public void setAssociatedElement( String val ) { ids.add(val); }

	public void setMotorConfigured( Boolean val ) { this.isConfigured = val; }

	public ArrayList<String> getAllAssociatedElementsOfType( String type ) {

		ArrayList<String> types = new ArrayList<String>();
		for (String string : ids) {

			if(string.contains(type)){
				/// ids match
				types.add(string);
			}

		}
		return types;

	}

	public Boolean getIsConfigured() {return this.isConfigured ;}

}
