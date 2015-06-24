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

	RelativeLayout sensorLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		
		if(AppUtils.confItems == null) {
			AppUtils.confItems = new ConfigItem();
		}
		
		LinearLayout llayout = (LinearLayout)findViewById(R.id.sensorlayout);
		sensorLayout = SensorController.getInstance().createSensorLayout(null, null, this);
		llayout.addView(sensorLayout);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sensor, menu);
		return true;
	}

}
