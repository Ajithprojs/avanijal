package com.app.avanstart;


import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.avanstart.util.AppUtils;
import com.app.beans.ConfigItem;
import com.app.beans.CropItem;
import com.app.controllers.MotorController;

public class ElementsConfigurationActivity extends FragmentActivity {


	ViewPager mViewPager;
	ElementPagerAdapter mfragAdapter;
	CropItem currentCrop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_elementsconfiguration);
		
		Button configBtn = (Button)findViewById(R.id.configbtn);
		
		configBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showConfigScreen();
			}
		});

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		getActionBar().setIcon(R.drawable.avaniheaderlogo);
		getActionBar().setTitle(R.string.cropconfig);

		mfragAdapter = new ElementPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.elementconfigpager);
		mViewPager.setAdapter(mfragAdapter);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.elements_configuration, menu);
		return true;
	}
	
	
	private void showConfigScreen() {
		
		String sms = AppUtils.buildMotorConfigSMS();
		AppUtils.sendSMS("9880652209", sms, this);
		Intent i = new Intent( ElementsConfigurationActivity.this , ConfigurationDetails.class );
		startActivity(i);
		
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class ElementPagerAdapter extends FragmentPagerAdapter {


		String[] elements = null;
		public static final String CROP_SECTION_NUMBER = "crop_number";
		public ElementPagerAdapter(FragmentManager fm) {
			super(fm);
			elements = (String[])getResources().getStringArray(R.array.elements);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new ElementFragment();
			Bundle args = new Bundle();
			args.putInt(ElementPagerAdapter.CROP_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return elements.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			return elements[position].toUpperCase(l);
			//return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class ElementFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */

		//CropItem cropItem;
		//Hashtable<String, MotorItem>  motors;
		//Hashtable<String, FilterItem> filters;
		//public static final String ARG_SECTION_NUMBER = "section_number";
		
		int fragmentIndex = 1;
		int numberofmotors = 1;
		int numberoffilters = 1;

		//ViewPager viewPage;


		public ElementFragment() {

			//motors = null;
			//filters = null;
			
			if(AppUtils.confItems == null) {
				
				AppUtils.confItems = new ConfigItem();
				
			}
		}


		@Override
		public View onCreateView(LayoutInflater inflater, final ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.elementsconfigfragment, container, false);

			Bundle b = this.getArguments();
			fragmentIndex = b.getInt(ElementPagerAdapter.CROP_SECTION_NUMBER);
			final LinearLayout llayout = (LinearLayout)rootView.findViewById(R.id.scrolllinear);
			Spinner itemSpinner = (Spinner)rootView.findViewById(R.id.itemnum);
			TextView totalitemlbl = (TextView)rootView.findViewById(R.id.totalitemlabel);
			
			
			//FilterController.getInstance().createFilterLayout(container, llayout, getActivity(), numberoffilters);
			
			
			switch (fragmentIndex) {
			case 1:

				//// show the motors
				//numberofmotors = 1; /// default value
				//configlayout.setVisibility(View.GONE);
				itemSpinner.setSelection(numberofmotors);
				itemSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int number, long arg3) {
						// TODO Auto-generated method stub
						//llayout.removeAllViews();
						numberofmotors =  number;
						
						//MotorController.getInstance().createMotorLayout(container, getActivity() , llayout, numberofmotors);
						
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

				break;

			case 2:

				//// show the motors
				//configSpinnrer.setVisibility(View.GONE);
				totalitemlbl.setText("Total no of filters :");
				//totalconfiglbl.setText("Total no of config :");
				itemSpinner.setSelection(numberoffilters);
				itemSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int number, long arg3) {
						// TODO Auto-generated method stub
						numberoffilters =  number;
						//
						//FilterController.getInstance().loadMotorData();
						
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

				break;


			case 3 :

				/// shows the valves

				itemSpinner.setVisibility(View.GONE);
				totalitemlbl.setVisibility(View.GONE);
				//ValveController.getInstance().createValveLayout(container, llayout, getActivity());
				
				break;


			case 4 :

				/// shows the sensors

				itemSpinner.setVisibility(View.GONE);
				totalitemlbl.setVisibility(View.GONE);
				//SensorController.getInstance().createSensorLayout(container, llayout, getActivity());
				
				break;	

			default:
				break;
			}

			return rootView;
		}

	}


}
