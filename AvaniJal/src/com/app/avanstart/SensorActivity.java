package com.app.avanstart;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.avanstart.util.AppUtils;
import com.app.beans.ConfigItem;
import com.app.controllers.SensorController;

public class SensorActivity extends Activity {

	private SensorController sensor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		
		if(AppUtils.confItems == null) {
			AppUtils.confItems = new ConfigItem();
		}
		LinearLayout llayout = (LinearLayout)findViewById(R.id.sensorlayout);
		llayout.addView(sensor().createSensorLayout(null, null, this));
		
	}
	
	private SensorController sensor() {
		if(sensor == null) sensor = new SensorController();
		return sensor;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sensor, menu);
		return true;
	}

}
