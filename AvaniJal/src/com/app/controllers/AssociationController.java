package com.app.controllers;

import java.util.ArrayList;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.avaniadapters.CropsGridViewAdapter;
import com.app.avanicomponents.CropSectionFragment;
import com.app.avanstart.CropSelectionActivity;
import com.app.avanstart.ElementsConfigurationActivity;
import com.app.avanstart.R;
import com.app.avanstart.util.AppUtils;
import com.app.beans.AssociationItem;
import com.app.beans.Children;
import com.app.beans.CropItem;
import com.app.beans.RecordHolder;
import com.app.interfaces.FragmentInterface;

public class AssociationController {

	static AssociationController _instance;

	ArrayList<Children> configs;

	Activity cxt;

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	CropsGridViewAdapter customGridAdapter;
	static Hashtable< String,  Object> cropConfig;
	FragmentManager fmg;

	private AssociationController() {


	}
	public static AssociationController getInstance() {

		if(_instance == null)
			_instance = new AssociationController();
		return _instance;
	}

	public void destructController() {
		configs = null;
		fmg = null;
		mSectionsPagerAdapter = null;
		cropConfig = null;

		_instance = null;
	}

	public RelativeLayout getAssociationLayout(ViewGroup cont ,Activity act, FragmentManager _fmg) {

		this.cxt = act;
		this.fmg = _fmg;
		if(AppUtils.assoItems == null) AppUtils.assoItems = new AssociationItem();
		LayoutInflater oldlinf = (LayoutInflater) act.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		RelativeLayout rel = (RelativeLayout) oldlinf.inflate(R.layout.activity_crop_list, cont, false);
		cropConfig = new Hashtable<String, Object>();
		if(mViewPager != null) {
			mViewPager.removeAllViews();
			mViewPager = null;
		}
		if(mSectionsPagerAdapter != null) {
			mSectionsPagerAdapter = null;
		}
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				_fmg);
		mViewPager = (ViewPager) rel.findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		return rel;
	}

	public class SectionsPagerAdapter extends FragmentStatePagerAdapter implements FragmentInterface {

		ArrayList<String> cropTypes = new ArrayList<String>();
		String currentTitle;
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			int count = AppUtils.assoItems.getCropItemsCount();
			if(count > 0) {
				cropTypes.add("Associated");
			}
			String[] str = cxt.getResources().getStringArray(R.array.croptype);
			for (String string : str) {
				cropTypes.add(string);
			}
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new CropSectionFragment();
			((CropSectionFragment)fragment).setDelegate(this);
			Bundle args = new Bundle();
			args.putInt(CropSectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return cropTypes.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			currentTitle = cropTypes.get(position);
			return currentTitle;//cropTypes.get(position);
		}
		@Override
		public void onItemClicked(Object obj) {
			// TODO Auto-generated method stub
			if(obj!=null){
				RecordHolder rec = (RecordHolder)obj;
				Intent i = new Intent(cxt , CropSelectionActivity.class);
				i.putExtra("currentTitle", cropTypes.get(rec.getCroptype()));
				i.putExtra("cropName", rec.getTxtTitle().getText());
				i.putExtra("cropType", cropTypes.get(rec.getCroptype()));
				i.putExtra("imgid", rec.getImgId());
				cxt.startActivityForResult(i, 2003);
			}
		}
	}



	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	//public static class CropSectionFragment extends Fragment {}

}
