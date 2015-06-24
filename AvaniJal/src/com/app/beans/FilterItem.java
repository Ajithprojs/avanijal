package com.app.beans;

public class FilterItem extends Elements{


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
