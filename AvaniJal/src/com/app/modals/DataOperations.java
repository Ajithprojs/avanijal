package com.app.modals;

import java.util.Hashtable;

import android.os.Environment;

import com.app.beans.CropItem;

public class DataOperations {


	public static Hashtable<String, Hashtable<String, CropItem>> crops;

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	public void writeData( Hashtable<String, String[]> values , String type ) {



	}


}
