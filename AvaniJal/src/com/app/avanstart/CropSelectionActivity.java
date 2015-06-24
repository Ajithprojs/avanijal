package com.app.avanstart;

import java.util.Hashtable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.avaniadapters.CropsGridViewAdapter;
import com.app.beans.CropItem;
import com.app.modals.DataOperations;

public class CropSelectionActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	//static GridView gridView;
	CropsGridViewAdapter customGridAdapter;

	static Hashtable< String,  Object> cropConfig;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop_selection);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		cropConfig = new Hashtable<String, Object>();
		if(DataOperations.crops == null) {
			
			DataOperations.crops = new Hashtable<String, Hashtable<String,CropItem>>();
			
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getActionBar().setIcon(R.drawable.avaniheaderlogo);
		getActionBar().setTitle(R.string.cropconfig);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crop_selection, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub

		pushLanguageScreen();
		return super.onMenuItemSelected(featureId, item);
	}


	@Override
	protected void onActivityResult(int operation, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(operation, arg1, arg2);

		if(operation == 400) {
			this.onStart();
		} else if(operation == 1001) {
			
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		String[] cropTypes;
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			cropTypes = getResources().getStringArray(R.array.croptype);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new CropSectionFragment();
			Bundle args = new Bundle();
			args.putInt(CropSectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return cropTypes.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			
			return cropTypes[position];
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class CropSectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */

		String[] cropTitlesIds;
		int[] imgIds;
		//ArrayList<CropItem> cropArray1 ;
		Hashtable<String, CropItem> cropHolder;
		public static final String ARG_SECTION_NUMBER = "section_number";
		int index = 0;

		public CropSectionFragment() {

			cropTitlesIds = null;
			imgIds = null;
			cropHolder = null;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(
					R.layout.fragment_crop_selection_dummy, container, false);
			Bundle b = this.getArguments();
			index = b.getInt(CropSectionFragment.ARG_SECTION_NUMBER);
			//cropArray1 = new ArrayList<CropItem>();
			switch(index) {

			case 1 :
				/// show patram details 
				cropTitlesIds = (String[])getResources().getStringArray(R.array.patramcrops);
				imgIds = (int[])getResources().getIntArray(R.array.patramcropimages);
				break;

			case 2 :
				/// show pushpam details 
				cropTitlesIds = (String[])getResources().getStringArray(R.array.pushpamcrops);
				imgIds = (int[])getResources().getIntArray(R.array.pushpamcropimages);
				break;

			case 3 :
				/// show palam details 
				cropTitlesIds = (String[])getResources().getStringArray(R.array.palamcrops);
				imgIds = (int[])getResources().getIntArray(R.array.palamcropimages);
				break;

			default :
				/// show dhanyam details 
				cropTitlesIds = (String[])getResources().getStringArray(R.array.dhanyamcrops);
				imgIds = (int[])getResources().getIntArray(R.array.dhanyamcropimages);
				break;


			}
			cropHolder = new Hashtable<String, CropItem>();
			for( int i = 0 ; i < cropTitlesIds.length ; i++ ) {

				CropItem ci = new CropItem();
				ci.cropTitle = cropTitlesIds[i];
				ci.imgId = imgIds[i];
				cropHolder.put(ci.cropTitle, ci);

			}

			LayoutInflater linf = getActivity().getLayoutInflater();
			ScrollView sv = (ScrollView)linf.inflate(R.layout.fragment_cropconfig_scrollview, container , false);

			LinearLayout linear2 = (LinearLayout) sv.findViewById(R.id.linear2);
			LinearLayout linear3 = (LinearLayout) sv.findViewById(R.id.linear3);

			RecordHolder holder = null;
			View row = null;
			LinearLayout mainLayout = (LinearLayout)rootView.findViewById(R.id.mainLayout);
			mainLayout.removeAllViews();
			for(int i = 0 ; i < cropTitlesIds.length ; i++) {


				//if (row == null) {
				LayoutInflater inflat = getActivity().getLayoutInflater();
				row = inflat.inflate(R.layout.configurationitemslist, container, false);
				holder = new RecordHolder();
				holder.txtTitle = (TextView) row.findViewById(R.id.item_title);
				holder.imageItem = (ImageButton) row.findViewById(R.id.item_button);
				
				holder.tagVal = i;
				holder.croptype = index;
				holder.imageItem.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View obj) {
						// TODO Auto-generated method stub
						showCropSelection( (String)obj.getTag() );
					}
				});


				row.setTag(holder);

				

				if(i % 2 == 0){
					linear2.addView(row);
				}else {
					linear3.addView(row);
				}

				CropItem item = cropHolder.get(cropTitlesIds[i]);
				holder.txtTitle.setText(item.cropTitle);
				holder.imageItem.setImageResource(item.imgId);
				holder.imageItem.setTag(item.cropTitle);
				cropConfig.put(item.cropTitle, holder);
				DataOperations.crops.put(getResources().getStringArray(R.array.croptype)[index], cropHolder);

			}

			mainLayout.addView(sv);

			return rootView;
		}


		private void showCropSelection( String holderTitle ) {

			RecordHolder holder = (RecordHolder)cropConfig.get(holderTitle);
			Intent i = new Intent(getActivity() , ElementsConfigurationActivity.class);
			i.putExtra("cropName", holder.txtTitle.getText());
			i.putExtra("cropType", holder.croptype);
			startActivityForResult(i, 1001);
			//startActivity(i);

		}
		
		
	}
	


	private void pushLanguageScreen() {

		Intent i = new Intent(this , LanguageSelectionActivity.class);
		startActivityForResult(i, 400);

	}
	static class RecordHolder  {

		TextView txtTitle;
		ImageButton imageItem;
		int tagVal;
		int croptype;
		

	}
	
	
}
