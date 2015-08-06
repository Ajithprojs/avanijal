package com.app.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public  class Elements implements Serializable {

	private static final long serialVersionUID = 46543447;
	private String itemId;

	private String type;

	private boolean isEnabled;

	////is configured by sending sms success

	private boolean isConfigured;
	
	/// is associated by sending sms success
	
	private boolean isAssociated;
	
	/// is used for irrigation
	
	private boolean forIrrigation;
	
	/// is used for fertigation
	
	private boolean forFertigation;

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
	
	public Boolean hasAssociatedElement( String val ) {
		Boolean hasVal = false;
		Iterator<String> vals = ids.iterator();
		while(vals.hasNext()) {
			String v = vals.next();
			if(v.equals(val)){
				hasVal = true;
				break;
			}
		}
		return hasVal;
	}

	public void setAssociatedElement( String val ) { 
		//ids.add(val); 
		
		if(!hasAssociatedElement(val)){
			ids.add(val);
		}

	}

	public void removeAssociatedElement(String val) {

		Iterator<String> vals = ids.iterator();
		while(vals.hasNext()) {
			String v = vals.next();
			if(v.equals(val)){
				vals.remove();
			}
		}

	}

	public void setIsConfigured( Boolean val ) { this.isConfigured = val; }

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

	public boolean isAssociated() {
		return isAssociated;
	}

	public void setAssociated(boolean isAssociated) {
		this.isAssociated = isAssociated;
	}

	public boolean isForIrrigation() {
		return forIrrigation;
	}

	public void setForIrrigation(boolean forIrrigation) {
		this.forIrrigation = forIrrigation;
	}

	public boolean isForFertigation() {
		return forFertigation;
	}

	public void setForFertigation(boolean forFertigation) {
		this.forFertigation = forFertigation;
	}

}
