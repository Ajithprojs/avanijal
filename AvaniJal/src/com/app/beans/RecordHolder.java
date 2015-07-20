package com.app.beans;

import android.widget.ImageButton;
import android.widget.TextView;

public class RecordHolder {
	
	private TextView txtTitle;
	private ImageButton imageItem;
	private int tagVal;
	private int croptype;
	public TextView getTxtTitle() {
		return txtTitle;
	}
	public void setTxtTitle(TextView txtTitle) {
		this.txtTitle = txtTitle;
	}
	public ImageButton getImageItem() {
		return imageItem;
	}
	public void setImageItem(ImageButton imageItem) {
		this.imageItem = imageItem;
	}
	public int getTagVal() {
		return tagVal;
	}
	public void setTagVal(int tagVal) {
		this.tagVal = tagVal;
	}
	public int getCroptype() {
		return croptype;
	}
	public void setCroptype(int croptype) {
		this.croptype = croptype;
	}

}
