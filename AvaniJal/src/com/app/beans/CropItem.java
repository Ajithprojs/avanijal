package com.app.beans;

import java.io.Serializable;

public class CropItem extends Elements implements Serializable {
	
	private static final long serialVersionUID = 46542137;
	private int imgId;
	private String cropTitle;
	private int acre;
	private int kunta;
	private String cropName;

	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	public String getCropTitle() {
		return cropTitle;
	}
	public void setCropTitle(String cropTitle) {
		this.cropTitle = cropTitle;
	}
	public int getAcre() {
		return acre;
	}
	public void setAcre(int acre) {
		this.acre = acre;
	}
	public int getKunta() {
		return kunta;
	}
	public void setKunta(int kunta) {
		this.kunta = kunta;
	}
	public String getCropName() {
		return cropName;
	}
	public void setCropName(String cropName) {
		this.cropName = cropName;
	}

}
