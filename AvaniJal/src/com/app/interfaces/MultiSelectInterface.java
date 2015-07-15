package com.app.interfaces;

import com.app.avanicomponents.MultiSelectionSpinner;

public interface MultiSelectInterface {
	
	public void BeforeSelectDialog( MultiSelectionSpinner spinner );
	
	public void itemSelected(MultiSelectionSpinner spinner,String val);
	
	public void itemDeselected(MultiSelectionSpinner spinner,String val);

}
