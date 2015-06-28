package com.app.beans;

import java.io.Serializable;

public class FilterItem extends Elements implements Serializable{

	private static final long serialVersionUID = 46543446;
	
	private String frequencyHours;

	private String frequencyMintutes;

	private String durationSeconds; 


	public void setFrequencyHours( String val ) {this.frequencyHours = val ;}

	public void setFrequencyminutes( String val ) {this.frequencyMintutes = val ;}

	public void setDurationSeconds( String val ) {this.durationSeconds = val ;}


	public String getFrequencyHours(  ) {return frequencyHours  ;}

	public String getFrequencyminutes( ) {return frequencyMintutes ;}

	public String getDurationSeconds(  ) {return durationSeconds  ;}


}
