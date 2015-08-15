/*
on * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.avanstart;


import java.util.ArrayList;
import java.util.Hashtable;

import com.app.avanstart.util.AppUtils;
import com.app.beans.AssociationItem;
import com.app.beans.ConfigItem;
import com.app.beans.ConfigStatus;
import com.app.beans.IrrigationItem;
import com.app.controllers.AssociationController;
import com.app.controllers.ConfigListController;
import com.app.controllers.HistoryController;
import com.app.controllers.IrrigationController;
import com.app.controllers.ProvisionController;
import com.app.controllers.SettingsController;
import com.app.modals.DataOperations;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */
public class DashBoardActivity extends FragmentActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	public static String[] mDrawerTitles;
	public Context cxt;
	FragmentManager fmg;
	//Fragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		mTitle = mDrawerTitle = getTitle();
		mDrawerTitles = getNavigationTitles();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		this.cxt = this;

		// set a custom shadow that overlays the main content when the drawer opens
		//mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mDrawerTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description for accessibility */
				R.string.drawer_close  /* "close drawer" description for accessibility */
				) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
		if(AppUtils.assoItems != null) {
			if(AppUtils.assoItems.getCropItemsCount() > 0) {
				int pos = 0;
				for( int i = 0 ; i < mDrawerTitles.length ; i++ ) {
					if(mDrawerTitles[i] == AppUtils.menu_irri){
						pos = i;
						break;
					}
				}
				selectItem(pos);
			}
		}
		fmg = getSupportFragmentManager();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	private String[] getNavigationTitles() {
		ArrayList<String> titleHolder = new ArrayList<String>();
		titleHolder.add(AppUtils.menu_config);
		if(isElementConfigured("Motor") && isElementConfigured("Pipeline") && isElementConfigured("Valve")) {
			titleHolder.add(AppUtils.menu_prov);
			titleHolder.add(AppUtils.menu_asso);
		}
		if(AppUtils.assoItems != null) {
			if(AppUtils.assoItems.getCropItemsCount() > 0) {
				titleHolder.add(AppUtils.menu_irri);
			}
		}
		titleHolder.add(AppUtils.menu_history);
		titleHolder.add(AppUtils.menu_settings);
		String[] strArray = new String[titleHolder.size()];
		strArray = titleHolder.toArray(strArray);
		return strArray;
	}

	public boolean isElementConfigured( String eleName ) {

		Hashtable<String, ConfigStatus> cghash = AppUtils.confItems.getElementConfigStatus();
		Boolean val;
		if(cghash == null) {
			val =  false;
		}
		if(cghash.containsKey(eleName))
		{
			ConfigStatus cs = cghash.get(eleName);
			val =  cs.getISConfigured();
		}
		else{ 
			val = false;
		}
		return val;
	}

	public boolean isElementAssociated( String eleName ) {

		Hashtable<String, ConfigStatus> cghash = AppUtils.assoItems.getElementConfigstatus();
		Boolean val;
		if(cghash == null) {
			val =  false;
		}
		if(cghash.containsKey(eleName))
		{
			ConfigStatus cs = cghash.get(eleName);
			val =  cs.getISConfigured();
		}
		else{ 
			val = false;
		}
		return val;
	}

	public void RefreshTitles() {
		mDrawerTitles = getNavigationTitles();
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mDrawerTitles));
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new PlanetFragment();
		Bundle args = new Bundle();
		args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}



	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public static void destructControllers() {
		ConfigListController.getInstance().destructControllers();
		//AssociationController.getInstance().destructController();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 2001:
			/// ok this is from the config screen
			if(data!=null){

				Intent i = data;
				Bundle b = i.getExtras();
				if(b!=null){
					if(b.containsKey("status")){
						String status = b.getString("status");
						String elementName = b.getString("element");
						if(status.equals("configured")){
							ConfigListController.getInstance().addConfigToElement(elementName);
							DataOperations.saveDataToFile(AppUtils.confItems, AppUtils.CONFIG_FILE_NAME, cxt);
							//refreshFragment(0);

						}
					}
				}
			}
			ConfigListController.getInstance().destructControllers();
			break;

		case 2003:
			/// ok this is from the config screen
			if(data!=null){

				Intent i = data;
				Bundle b = i.getExtras();
				if(b!=null){
					if(b.containsKey("status")){
						String status = b.getString("status");
						String elementName = b.getString("element");
						if(status.equals("configured")){
							DataOperations.saveDataToFile(AppUtils.assoItems, AppUtils.ASSO_FILE_NAME, cxt);
							refreshFragment(2);

						}
					}
				}
			}
			//ConfigListController.getInstance().destructControllers();
			break;

		case 2004:
			/// ok this is from the irrigation screen
			if(data!=null){

				Intent i = data;
				Bundle b = i.getExtras();
				if(b!=null){
					if(b.containsKey("status")){
						String status = b.getString("status");
						String elementName = b.getString("element");
						if(status.equals("Active")){
							IrrigationController.getInstance().addConfigToElement(elementName);
							DataOperations.saveDataToFile(AppUtils.irriItems, AppUtils.IRRI_FILE_NAME, cxt);
							//refreshFragment();
						} else {
							IrrigationController.getInstance().removeConfigToElement(elementName);
						}
					}
				}
			}
			//ConfigListController.getInstance().destructControllers();
			break;

		default:
			break;
		}
		RefreshTitles();
	}

	private void refreshFragment(int val) {

		selectItem(val);
	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */

	public static class PlanetFragment extends Fragment {
		public static final String ARG_PLANET_NUMBER = "planet_number";

		public PlanetFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			//View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
			int i = getArguments().getInt(ARG_PLANET_NUMBER);
			View rootView = getCurrentView(i, container);//ConfigListController.getInstance().getConfigLayout(container, getActivity());
			return rootView;
		}

		private View getCurrentView( int pos, ViewGroup container ){

			//			AppUtils.confItems = (ConfigItem)DataOperations.getDataFromFile(AppUtils.CONFIG_FILE_NAME, getActivity());
			//			AppUtils.assoItems = (AssociationItem)DataOperations.getDataFromFile(AppUtils.ASSO_FILE_NAME, getActivity());
			//			AppUtils.irriItems = (IrrigationItem)DataOperations.getDataFromFile(AppUtils.IRRI_FILE_NAME, getActivity());
			if(AppUtils.confItems == null) {
				AppUtils.confItems = new ConfigItem();
			}
			if(AppUtils.assoItems == null) {
				AppUtils.assoItems = new AssociationItem();
			}
			if(AppUtils.irriItems == null) {
				AppUtils.irriItems = new IrrigationItem();
			}
			//destructControllers();


			if(mDrawerTitles[pos].equals(AppUtils.menu_config))
				return ConfigListController.getInstance().getConfigLayout(container, getActivity());
			else if(mDrawerTitles[pos].equals(AppUtils.menu_prov)) {
				return ProvisionController.getInstance().getProvisioningLayout(container, getActivity());
			}
			else if(mDrawerTitles[pos].equals(AppUtils.menu_asso)) {
				return AssociationController.getInstance().getAssociationLayout(container, getActivity(),getActivity().getSupportFragmentManager());
			}
			else if(mDrawerTitles[pos].equals(AppUtils.menu_irri)) {
				return IrrigationController.getInstance().getIrrigationLayout(container, getActivity());
			}
			else if(mDrawerTitles[pos].equals(AppUtils.menu_history)) {
				return HistoryController.getInstance().getHistoryLayout(container, getActivity());
			}else{
				return SettingsController.getInstance().getSettingsLayout(container, getActivity());
			}



			//			switch(pos){
			//
			//			default:
			//				return ConfigListController.getInstance().getConfigLayout(container, getActivity());
			//			case 1:
			//				return ProvisionController.getInstance().getProvisioningLayout(container, getActivity());
			//			case 2:
			//				//return new AssociationController().getAssociationLayout(container, getActivity(),getActivity().getSupportFragmentManager());
			//				return AssociationController.getInstance().getAssociationLayout(container, getActivity(),getActivity().getSupportFragmentManager());
			//			case 3:
			//				return ProvisionController.getInstance().getProvisioningLayout(container, getActivity());
			//			case 4:
			//				return IrrigationController.getInstance().getIrrigationLayout(container, getActivity());
			//			case 5:
			//				return HistoryController.getInstance().getHistoryLayout(container, getActivity());
			//			case 6:
			//				return SettingsController.getInstance().getSettingsLayout(container, getActivity());
			//
			//			}
		}
	}
}

