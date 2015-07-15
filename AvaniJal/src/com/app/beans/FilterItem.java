package com.app.beans;

import java.io.Serializable;

public class FilterItem extends Elements implements Serializable{

	private static final long serialVersionUID = 46543446;
	
	private int frequencyHours;

	private int frequencyMintutes;

	private int durationSeconds; 


	public void setFrequencyHours( int val ) {this.frequencyHours = val ;}

	public void setFrequencyminutes( int val ) {this.frequencyMintutes = val ;}

	public void setDurationSeconds( int val ) {this.durationSeconds = val ;}


	public int getFrequencyHours(  ) {return frequencyHours  ;}

	public int getFrequencyminutes( ) {return frequencyMintutes ;}

	public int getDurationSeconds(  ) {return durationSeconds  ;}


}
