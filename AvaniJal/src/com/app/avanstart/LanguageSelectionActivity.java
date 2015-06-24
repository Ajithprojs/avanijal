package com.app.avanstart;

import java.util.Locale;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LanguageSelectionActivity extends Activity {

	ListView lv;
	Locale myLocale;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language_selection);
		
		
		getActionBar().setIcon(R.drawable.avaniheaderlogo);
		getActionBar().setTitle("Select Language");
		
		lv = (ListView)findViewById(R.id.languageList);
		
		/*String[] values = new String[] { "Kannada", 
                "Hindi",
                "English",
                "Malayalam", 
                "Tamil", 
                "Telegu", 
                
               };*/
		
		String[] values = getResources().getStringArray(R.array.languages);
		
		ArrayAdapter<String> simpleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , android.R.id.text1 , values);
		lv.setAdapter(simpleAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id){
				// TODO Auto-generated method stub
				
				// ListView Clicked item index
                int itemPosition     = position + 1;
                
                switch (itemPosition) {
				case 1:
					setLocale("ka");
					break;
				case 2:
					setLocale("hi");
					break;
				case 3:
					setLocale("en");
					break;
				case 4:
					setLocale("ta");
					break;
				case 5:
					setLocale("ma");
					break;
				case 6:
					setLocale("te");
					break;

				default:
					setLocale("en");
					break;
				}
                 navigateToNextScreen();
				
			}
			
			
		});
		
	}
	
	
	public void navigateToNextScreen() {
		
		//Intent i = new Intent( this , LoginActivity.class );
		//startActivity(i);
		finish();
		
	}
	
	public void setLocale(String lang) {

		myLocale = new Locale(lang);
		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.language_selection, menu);
		return true;
	}

}
