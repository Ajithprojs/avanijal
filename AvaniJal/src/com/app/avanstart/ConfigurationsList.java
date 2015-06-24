package com.app.avanstart;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.app.avaniadapters.ConfigListViewAdapter;
import com.app.beans.Children;
import com.app.beans.ConfigGroup;
import com.app.beans.ConfigStatus;
import com.app.interfaces.expandedlistinterfaces;

public class ConfigurationsList extends Activity implements expandedlistinterfaces {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configurations_list);


	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getActionBar().setIcon(R.drawable.avaniheaderlogo);
		getActionBar().setTitle("AvaniJal");
		initializers();
		/*ListView t= ( ListView )findViewById( R.id.configlist );  // List defined in XML ( See Below )

		Resources res =getResources();
		ElementListAdapter adapter=new ElementListAdapter( this, initializers() ,res );
		t.setAdapter( adapter );*/
		ListView list = (ListView)findViewById(R.id.configlist);
		ConfigListViewAdapter adapter = new ConfigListViewAdapter(this,
				createAvaniGroups() , this);
		list.setAdapter(adapter);
	}

	//	private SparseArray<ConfigGroup> createAvaniGroups() {
	//
	//		SparseArray<ConfigGroup> groups = new SparseArray<ConfigGroup>();
	//
	//		/// lets create group for configuration , association and provisioning
	//
	//		String[] avaniprocess = getResources().getStringArray(R.array.avaniprocess);
	//		
	//		String[] avaniCrops = {"Add Crop"};
	//
	//		for( int i = 0 ; i < avaniprocess.length - 1 ; i++ ) {
	//
	//			switch(i) {
	//
	//			default :
	//
	//				// its elements config
	//				ConfigGroup cg = new ConfigGroup(avaniprocess[i]);
	//				String[] elements = getResources().getStringArray(R.array.elements);
	//				int[] eleimgs = {R.drawable.motors , R.drawable.filters , R.drawable.valves , R.drawable.sensors};
	//				int j = 0;
	//				for (String string : elements) {
	//					cg.child.add(getNewChild(string, "not configured", eleimgs[j]));
	//					j++;
	//				}
	//				groups.append(i, cg);
	//
	//				break;
	//
	//
	//			case 1 :
	//
	//				// its asso config
	//				ConfigGroup cgasso = new ConfigGroup(avaniprocess[i]);
	//				int m = 0;
	//				for (String string : avaniCrops) {
	//					cgasso.child.add(getNewChild(string, "not configured", 0));
	//					m++;
	//				}
	//				groups.append(i, cgasso);
	//
	//				break;
	//
	//			case 2 :
	//
	//				// its asso config
	//				ConfigGroup cgprov = new ConfigGroup(avaniprocess[i]);
	//
	//				groups.append(i, cgprov);
	//
	//				break;
	//
	//			}
	//
	//		}
	//
	//		return groups;
	//	}
	private ArrayList<Children> createAvaniGroups() {

		ArrayList<Children> groups = new ArrayList<Children>();

		/// lets create group for configuration , association and provisioning
		String[] elements = getResources().getStringArray(R.array.elements);
		int[] eleimgs = {R.drawable.motors , R.drawable.filters , R.drawable.valves , R.drawable.sensors};
		int j = 0;
		for (String string : elements) {
			groups.add(getNewChild(string, "not configured", eleimgs[j]));
			j++;
		}

		return groups;
	}


	private Children getNewChild(String title , String status , int img){

		Children ch = new Children();
		ch.title = title;
		ch.status = status;
		ch.img = img;

		return ch;

	}

	public void OnItemClicked( int childPosition ) {


		switch (childPosition + 1) {
		case 1:
			/// show motor config
			navigateToMotorConfig();
			break;

		case 2:

			navigateToFilterConfig();
			break;

		case 3:

			navigateToValveConfig();
			break;

		default:
			navigateToSensorConfig();
			break;

		}

	}

	private void navigateToMotorConfig() {

		Intent i = new Intent( this , MotorActivity.class );
		startActivityForResult(i, 2001);
	}

	private void navigateToFilterConfig() {

		Intent i = new Intent( this , FilterActivity.class );
		startActivityForResult(i, 2002);
	}

	private void navigateToValveConfig() {

		Intent i = new Intent( this , ValveActivity.class );
		startActivityForResult(i, 2003);
	}

	private void navigateToSensorConfig() {

		Intent i = new Intent( this , SensorActivity.class );
		startActivityForResult(i, 2004);
	}

	private void navigateToAddNewCropAssociation() {

		Intent i = new Intent( this , CropSelectionActivity.class );
		startActivityForResult(i, 3001);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configurations_list, menu);
		return true;
	}

	private ArrayList<ConfigStatus> initializers() {

		/// lets first manage the status of elements

		ArrayList<ConfigStatus> init = new ArrayList<ConfigStatus>();

		int[] elementImg = {R.drawable.motors , R.drawable.filters , R.drawable.valves , R.drawable.sensors};
		String[] elements = getResources().getStringArray(R.array.elements);
		int i = 0;
		for (String ele : elements) {
			ConfigStatus cf = new ConfigStatus();
			cf.elementName = ele;
			cf.imgName = elementImg[i];
			init.add(cf);
			i++;
		}

		//AppUtils.confItems = new ConfigItem();
		//AppUtils.confItems.elementConfigstatus = init;
		return init;

	}


	@Override
	public void listclicked(int child) {
		// TODO Auto-generated method stub

		OnItemClicked(child);

	}
}
