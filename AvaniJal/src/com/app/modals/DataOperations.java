package com.app.modals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Hashtable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.app.beans.CropItem;


public class DataOperations  {

	/// temp ways of storing data
	
	//private String configFileName = "/configData";
	
	public static void saveDataToFile( Object data , String configFileName, Context cxt ) {
		
		try {
			//ObjectMapper mapper = new ObjectMapper();
			//mapper.writeValue(new File("c:\\"+configFileName+".json"), data);
			FileOutputStream fos = cxt.openFileOutput(configFileName, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(data);
			os.close();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static Object getDataFromFile( String fileName , Context cxt ) {
		
		ObjectInputStream is;
		Object simpleClass = null;
		try {
			FileInputStream fis = cxt.openFileInput(fileName);
			is = new ObjectInputStream(fis);
			simpleClass = (Object) is.readObject();
			is.close();
			fis.close();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return simpleClass;
		
	}
	
	public static void deleteFiles( String fileName , Context cxt ) {
		
		try {
			//ObjectMapper mapper = new ObjectMapper();
			//mapper.writeValue(new File("c:\\"+configFileName+".json"), data);
			FileOutputStream fos = cxt.openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject("");
			os.close();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean deleteDir(File dir) {
	    if (dir != null && dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }

	    return dir.delete();
	}

}
