package com.app.avanicomponents;

import java.util.ArrayList;
import java.util.Hashtable;

import com.app.avanstart.R;
import com.app.beans.CropItem;
import com.app.beans.RecordHolder;
import com.app.interfaces.FragmentInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CropSectionFragment extends Fragment{

	String[] cropTitlesIds;
	int[] imgIds;
	//ArrayList<CropItem> cropArray1 ;
	Hashtable<String, RecordHolder> cropConfig;
	ArrayList<CropItem> cropHolder;
	public static final String ARG_SECTION_NUMBER = "section_number";
	int index = 0;
	FragmentInterface delegate;

	public CropSectionFragment( ) {

		cropTitlesIds = null;
		imgIds = null;
		cropHolder = null;
		cropConfig = null;
		
	}
	
	public void setDelegate(FragmentInterface _del) {
		this.delegate = _del;
	}
	
	public void destruct() {
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
		cropHolder = new ArrayList<CropItem>();
		cropConfig = new Hashtable<String, RecordHolder>();
		for( int i = 0 ; i < cropTitlesIds.length ; i++ ) {

			CropItem ci = new CropItem();
			ci.cropTitle = cropTitlesIds[i];
			ci.imgId = imgIds[i];
			cropHolder.add( ci);

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
			holder.setTxtTitle((TextView) row.findViewById(R.id.item_title));
			holder.setImageItem((ImageButton) row.findViewById(R.id.item_button));

			holder.setTagVal(i);
			holder.setCroptype(index);
			
			holder.getImageItem().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View obj) {
					// TODO Auto-generated method stub
					//showCropSelection( (String)obj.getTag() );
					if(delegate != null) {
						RecordHolder holder = (RecordHolder)cropConfig.get((String)obj.getTag());
						delegate.onItemClicked(holder);
					}
				}
			});

			row.setTag(holder);
			if(i % 2 == 0){
				linear2.addView(row);
			}else {
				linear3.addView(row);
			}

			CropItem item = cropHolder.get(i);
			holder.getTxtTitle().setText(item.cropTitle);
			holder.getImageItem().setImageResource(item.imgId);
			holder.getImageItem().setTag(item.cropTitle);
			cropConfig.put(item.cropTitle, holder);

		}
		mainLayout.addView(sv);
		return rootView;
	}

//	private void showCropSelection( String holderTitle ) {
//
//		RecordHolder holder = (RecordHolder)cropConfig.get(holderTitle);
//		Intent i = new Intent(getActivity() , ElementsConfigurationActivity.class);
//		i.putExtra("cropName", holder.txtTitle.getText());
//		i.putExtra("cropType", holder.croptype);
//		startActivityForResult(i, 1001);
//		//startActivity(i);
//
//	}
}

