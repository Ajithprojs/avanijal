package com.app.avanstart;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class SplashActivity extends Activity {

	private Timer tm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);


	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		/*Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(3000);

					runOnUiThread(new Runnable() {
						public void run() {
							pushNextScreen();
						}
					});
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		th.run();*/
		
		
		tm = new Timer();
		tm.schedule(new ExecuteNext(), 3000);
		
	}


	private void pushNextScreen() {


		Intent i = new Intent(this , LoginActivity.class);
		startActivity(i);
		finish();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	
	class ExecuteNext extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			pushNextScreen();
		}
		
		
		
	}

	

}

