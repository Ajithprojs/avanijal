package com.app.beans;

import java.io.Serializable;

public class User implements Serializable {
	
	private static final long serialVersionUID = 36543447;
	
	private String mEmail;
	private String mPassword;
	private String fName;
	private String fMobile;
	private String fId;
	private String fAddress;
	
	public void setEmail( String val ){ this.mEmail = val; }
	public void setPassword( String val ){ this.mPassword = val; }
	public void setName( String val ){ this.fName = val; }
	public void setMobile( String val ){ this.fMobile = val; }
	public void setId( String val ){ this.fId = val; }
	public void setAddress( String val ){ this.fAddress = val; }
	
	public String getEmail() { return this.mEmail; }
	public String getPassword() { return this.mPassword; }
	public String getName() { return this.fName; }
	public String getMobile() { return this.fMobile; }
	public String getId() { return this.fId; }
	public String getAddress() { return this.fAddress; }
	

}
