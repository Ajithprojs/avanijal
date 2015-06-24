package com.app.avanstart;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.avanstart.util.AppUtils;
import com.app.beans.ConfigItem;
import com.app.controllers.ValveController;

public class ValveActivity extends Activity {

	RelativeLayout valveLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_valve);
		if(AppUtils.confItems == null) {
			AppUtils.confItems = new ConfigItem();
		}
		
		LinearLayout llayout = (LinearLayout)findViewById(R.id.valvelayout);
		valveLayout = ValveController.getInstance().createValveLayout(null, null, this);
		llayout.addView(valveLayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.valve, menu);
		return true;
	}

}
