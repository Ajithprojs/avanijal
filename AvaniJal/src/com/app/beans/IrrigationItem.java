package com.app.beans;

import java.io.Serializable;
import java.util.Hashtable;

public class IrrigationItem extends Elements implements Serializable {
	
	private static final long serialVersionUID = 46113445; 
	private Hashtable<String, ConfigStatus> elementConfigstatus = new Hashtable<String, ConfigStatus>() ; 
	//private ArrayList<CropItem> cropItems = new ArrayList<CropItem>();
	private Hashtable<String,IrriCropItems> cropItems = new Hashtable<String,IrriCropItems>();
	public Hashtable<String, ConfigStatus> getElementConfigstatus() {
		return elementConfigstatus;
	}
	public void setElementConfigstatus(Hashtable<String, ConfigStatus> elementConfigstatus) {
		this.elementConfigstatus = elementConfigstatus;
	}
	
	public void setCropItem( String title , IrriCropItems crop ) {

		if( cropItems.containsKey(title) ) {
			cropItems.remove(title);
		}
		cropItems.put(title, crop);
	}

	public IrriCropItems getCropItem( String title ) {
		if(cropItems.containsKey(title)) {
			return cropItems.get(title);
		}
		return null;
	}
	
	public Hashtable<String, IrriCropItems> getAllCropItems() {
		return cropItems;
	}

	public Boolean containsCropItemForTitle( String title ) {

		if(cropItems.contains(title)) return true;
		return false;
	}
	
	public int getCropItemsCount() {
		return cropItems.size();
	}

}
