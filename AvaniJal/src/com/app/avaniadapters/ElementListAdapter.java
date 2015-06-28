package com.app.avaniadapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.avanstart.R;
import com.app.beans.ConfigStatus;

public class ElementListAdapter extends BaseAdapter implements android.widget.AdapterView.OnItemClickListener{


	/*********** Declare Used Variables *********/
	private Activity activity;
	private static LayoutInflater inflater=null;
	public Resources res;
	int i=0;
	
	ArrayList<ConfigStatus> data;

	/*************  CustomAdapter Constructor *****************/
	public ElementListAdapter(Activity a , ArrayList<ConfigStatus> d ,Resources resLocal) {

		/********** Take passed values **********/
		activity = a;
		res = resLocal;
		data = d;

		/***********  Layout inflator to call external xml layout () ***********/
		inflater = ( LayoutInflater )activity.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		

	}

	/******** What is the size of Passed Arraylist Size ************/
	public int getCount() {

		if(data.size()<=0)
			return 1;
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	/****** Depends upon data size called for each row , Create each ListView row *****/
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;

		if(convertView==null){

			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			vi = inflater.inflate(R.layout.list_elementsconfig, null);

			/****** View Holder Object to contain tabitem.xml file elements ******/

			holder = new ViewHolder();
			holder.elementName = (TextView) vi.findViewById(R.id.configlistfirstLine);
			holder.configText=(TextView)vi.findViewById(R.id.configlistsecondLine);
			holder.image=(ImageView)vi.findViewById(R.id.configlisticon);

			/************  Set holder with LayoutInflater ************/
			vi.setTag( holder );
		}
		else 
			holder=(ViewHolder)vi.getTag();

		if(data.size()<=0)
		{
			holder.elementName.setText("No Data");

		}
		else
		{
			
			/************  Set Model values in Holder elements ***********/
			ConfigStatus cs = (ConfigStatus)data.get(position);
			holder.elementName.setText(cs.getElementName() );
			holder.configText.setText(cs.getConfigDesc());
			//holder.image.setImageResource(
				//	res.getIdentifier(
					//		"com.androidexample.customlistview:drawable/"+name.get(position)
						//	,null,null));
			holder.image.setImageResource(cs.getImgName());

			/******** Set Item Click Listner for LayoutInflater for each row *******/

			vi.setOnClickListener(new OnItemClickListener( position ));
		}
		return vi;
	}

	public void onClick(View v) {
	}

	/********* Called when Item click in ListView ************/
	private class OnItemClickListener  implements OnClickListener{           
		private int mPosition;

		OnItemClickListener(int position){
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {


			//ConfigurationsList sct = (ConfigurationsList)activity;

			/****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

			//sct.OnItemClicked(mPosition);
		}               
	}   
	
	/********* Create a holder Class to contain inflated xml file elements *********/
	public class ViewHolder{

		public TextView elementName;
		public TextView configText;
		public ImageView image;

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
