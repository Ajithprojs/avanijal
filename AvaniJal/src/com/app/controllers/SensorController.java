package com.app.controllers;

import java.util.Hashtable;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.app.avanstart.R;
import com.app.beans.SensorItem;


public class SensorController {


	Hashtable<String, SensorItem> sensors;

	public SensorController() {


	}

//	public static SensorController getInstance() {
//
//		if(_instance == null)
//			_instance = new SensorController();
//
//		return _instance;
//
//	}


	public RelativeLayout createSensorLayout(ViewGroup container , LinearLayout llayout , Activity activity) {

		//llayout.removeAllViews();
		sensors = new Hashtable<String, SensorItem>();



		//ValveItems fitem = new ValveItems();
		//fitem.valveId = 1;
		LayoutInflater linf = activity.getLayoutInflater();
		RelativeLayout relativ = (RelativeLayout)linf.inflate(R.layout.sensorconfiguration, container, false);

		final TextView moistureMinLbl = (TextView)relativ.findViewById(R.id.moisturemin);

		SeekBar moistureMinBar = (SeekBar)relativ.findViewById(R.id.moistureminseekbar);
		
		final TextView moistureMaxLbl = (TextView)relativ.findViewById(R.id.moisturemax);

		SeekBar moistureMaxBar = (SeekBar)relativ.findViewById(R.id.moisturemaxseekbar);

		moistureMinBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				// TODO Auto-generated method stub
				moistureMinLbl.setText("Min Level : "+progress+"%");
			}
		});
		
		
		moistureMaxBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				// TODO Auto-generated method stub
				moistureMaxLbl.setText("Max Level : "+progress+"%");
			}
		});


		final TextView ecMinLbl = (TextView)relativ.findViewById(R.id.ecminlbl);

		SeekBar ecMinBar = (SeekBar)relativ.findViewById(R.id.ecminseekbar);
		
		final TextView ecMaxLbl = (TextView)relativ.findViewById(R.id.ecmaxlbl);

		SeekBar ecMaxBar = (SeekBar)relativ.findViewById(R.id.ecmaxseekbar);
		

		ecMinBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				// TODO Auto-generated method stub
				float value = (float) ((float)progress / 100.0);
				ecMinLbl.setText("Min Level : "+value);
			}
		});
		
		ecMaxBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				// TODO Auto-generated method stub
				float value = (float) ((float)progress / 100.0);
				ecMaxLbl.setText("Max Level : "+value);
			}
		});


		return relativ;
		//llayout.addView(relativ);
		//valves.put("valve"+(i+1), fitem);

	}



}
