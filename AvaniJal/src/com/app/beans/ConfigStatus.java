package com.app.beans;

import java.io.Serializable;

public class ConfigStatus implements Serializable  {
	
	private static final long serialVersionUID = 46543497;
	
	private Boolean isConfigured = false;
	
	private String elementName;
	
	private int imgName;
	
	private String configDescription = "Not Configured yet";
	
	public void setIsConfigured( Boolean config ){ this.isConfigured = config; }
	
	public void setElementName( String val ){ this.elementName = val; }
	
	public void setImageName( int val ){ this.imgName = val; }
	
	public void setConfigDesc( String val ){ this.configDescription = val; }
	
	public Boolean getISConfigured(){ return this.isConfigured; }
	
	public String getElementName(){ return this.elementName; }
	
	public int getImgName(){ return this.imgName; }
	
	public String getConfigDesc(){ return this.configDescription; }
	

}
