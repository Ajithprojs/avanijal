package com.app.avanstart;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class CropSelectionActivity extends FragmentActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop_selection);

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
	}


	


	private void pushLanguageScreen() {

		Intent i = new Intent(this , LanguageSelectionActivity.class);
		startActivityForResult(i, 400);

	}
	
}
