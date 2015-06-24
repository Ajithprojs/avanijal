package com.app.avanstart;

import java.util.Enumeration;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.widget.ExpandableListView;

import com.app.avaniadapters.ConfigListViewAdapter;
import com.app.avanstart.util.AppUtils;
import com.app.beans.Children;
import com.app.beans.ConfigGroup;
import com.app.beans.ConfigItem;
import com.app.beans.MotorItem;
import com.app.interfaces.expandedlistinterfaces;

public class ConfigurationDetails extends Activity implements expandedlistinterfaces{

	SparseArray<ConfigGroup> groups = new SparseArray<ConfigGroup>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration_details);
		createData();
		ExpandableListView listView = (ExpandableListView) findViewById(R.id.configlistView);
		//ConfigListViewAdapter adapter = new ConfigListViewAdapter(this,
		//		groups , this);
		//listView.setAdapter(adapter);
	}



	public void createData() {

		ConfigItem item = AppUtils.confItems;

		//// this is for motor items
		if(item.motorItems != null) {


			Enumeration<String> allKeys = null;//item.motorItems.keys();
			int x = 0;
			while(allKeys.hasMoreElements()) {

				String key = allKeys.nextElement();
				MotorItem me = null;//item.motorItems.get(key);
				ConfigGroup group = new ConfigGroup("Motors : " + me.getPumpName());
				group.child.add(getNewChild(new Children(), "Hp Value :"+me.getHPVal(), null, 0));
				group.child.add(getNewChild(new Children(), "operation type :"+me.getOpetationType(), null, 0));
				group.child.add(getNewChild(new Children(), "motor type :"+me.getMotorType(), null, 0));
				group.child.add(getNewChild(new Children(), "max volts :"+me.getMaxVolts(), null, 0));
				group.child.add(getNewChild(new Children(), "min volts :"+me.getMinVolts(), null, 0));
				group.child.add(getNewChild(new Children(), "water delivery rate :"+me.getWaterDeliveryRate(), null, 0));
				group.child.add(getNewChild(new Children(), "delivery Type :"+me.getDeliveryType(), null, 0));
				group.child.add(getNewChild(new Children(), "Current measurement Type :"+me.getCurrentMeasureType(), null, 0));
				
				
				groups.append(x, group);
				x++;

			}

		}

	}
	
	private Children getNewChild(Children ch ,String title , String status , int img){
		
		ch.title = title;
		ch.status = status;
		ch.img = img;
		
		return ch;
		
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configuration_details, menu);
		return true;
	}



	
	@Override
	public void listclicked(int child) {
		// TODO Auto-generated method stub
		
	}

}
