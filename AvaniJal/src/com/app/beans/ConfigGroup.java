package com.app.beans;

import java.util.ArrayList;
import java.util.List;

public class ConfigGroup {
	
	
	public String string;
	public final List<Children> child = new ArrayList<Children>();

	  public ConfigGroup(String string) {
	    this.string = string;
	  }
	  
	  

}
