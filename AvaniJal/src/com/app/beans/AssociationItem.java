package com.app.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class AssociationItem implements Serializable {

	private static final long serialVersionUID = 46543445; 
	private Hashtable<String, ConfigStatus> elementConfigstatus = new Hashtable<String, ConfigStatus>() ; 
	//private ArrayList<CropItem> cropItems = new ArrayList<CropItem>();
	private Hashtable<String,CropItem> cropItems = new Hashtable<String,CropItem>();
	public Hashtable<String, ConfigStatus> getElementConfigstatus() {
		return elementConfigstatus;
	}
	public void setElementConfigstatus(Hashtable<String, ConfigStatus> elementConfigstatus) {
		this.elementConfigstatus = elementConfigstatus;
	}
	//	public ArrayList<CropItem> getCropItems() {
	//		return cropItems;
	//	}
	//	public void setCropItems(ArrayList<CropItem> cropItems) {
	//		this.cropItems = cropItems;
	//	}
	//	public Hashtable<String,CropItem> getCropItems() {
	//		return cropItems;
	//	}
	//	public void setCropItems(Hashtable<String,CropItem> cropItems) {
	//		this.cropItems = cropItems;
	//	}
	
	

	public void setCropItem( String title , CropItem crop ) {

		if( cropItems.containsKey(title) ) {
			cropItems.remove(title);
		}
		cropItems.put(title, crop);
	}

	public CropItem getCropItem( String title ) {
		if(cropItems.containsKey(title)) {
			return cropItems.get(title);
		}
		return null;
	}
	
	public Hashtable<String, CropItem> getAllCropItems() {
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
