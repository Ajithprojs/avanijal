package com.app.beans;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Elements implements Serializable {

	private static final long serialVersionUID = 46543447;
	private String itemId;

	private String type;

	private boolean isEnabled;

	////is configured by sending sms success

	private boolean isConfigured;

	/// other elements associated ids
	private ArrayList<String> ids = new ArrayList<String>();
	
	public void setItemid( String id ) {
		this.itemId = id;
	}
	
	public void setType( String type ) {
		this.type = type;
	}
	public void setIsEnabled( Boolean val ) {
		this.isEnabled = val;
	}

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
	public String getItemid() {return this.itemId ;}
	public String getType() {return this.type ;}
	public Boolean getIsEnabled() {return this.isEnabled ;}
	public Boolean getIsConfigured() {return this.isConfigured ;}

}
