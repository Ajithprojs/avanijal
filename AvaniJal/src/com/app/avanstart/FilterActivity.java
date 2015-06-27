package com.app.avanstart;

import java.util.ArrayList;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Element;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.app.avanstart.util.AppUtils;
import com.app.beans.ConfigItem;
import com.app.beans.Elements;
import com.app.beans.MotorItem;
import com.app.controllers.FilterController;



public class FilterActivity extends Activity {

	LinearLayout filterLayout;
	ArrayAdapter<String> dataAdapter;
	boolean changeFilterNum = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		if(AppUtils.confItems == null) {
			AppUtils.confItems = new ConfigItem();
		}

		Spinner filternum = (Spinner)findViewById(R.id.filternum);
		ArrayList<Elements> mItems = (ArrayList<Elements>)AppUtils.confItems.motorItems;
		if (mItems != null) {
			
			String[] i = new String[mItems.size() + 1];
			i[0] = "0";
			for(int x = 1 ; x <= mItems.size() ; x++){
				i[x] = ""+x;
			}
			
			changeFilterNum = false;
			dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, i);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			filternum.setAdapter(dataAdapter);

		}
		
		filternum.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int number, long arg3) {
				// TODO Auto-generated method stub
				if(changeFilterNum)
				ValueChanged(number);
				changeFilterNum = true;

			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		Button setconfigbtn  = (Button)findViewById(R.id.setfilterconfigbtn);

		setconfigbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendConfig();

			}
		});


		int nofilters = 0;
		LinearLayout llayout = (LinearLayout)findViewById(R.id.filterlayout);
		filterLayout = FilterController.getInstance().createFilterLayout(null, null, this, nofilters);
		llayout.addView(filterLayout);
	}

	private void sendConfig() {

		String sms = AppUtils.buildFilterConfigSms();
		Intent i = new Intent(this , SmsActivity.class);
		i.putExtra("phone", AppUtils.phoneNum);
		i.putExtra("msg", sms);
		startActivity(i);

	}

	private void ValueChanged(int pos) {

		LinearLayout llayout = (LinearLayout)findViewById(R.id.filterlayout);
		llayout.removeAllViews();
		filterLayout = FilterController.getInstance().createFilterLayout(null, null , this, pos);
		llayout.addView(filterLayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter, menu);
		return true;
	}

}
