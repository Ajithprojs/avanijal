package com.app.avanicomponents;

import com.app.interfaces.Timerinterface;

import android.os.Handler;
import android.os.SystemClock;

public class AvaniTimer {

	private static AvaniTimer _instance;
	Timerinterface delegate;

	private long startTime = 0L;
	private Handler customHandler = new Handler();
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	long endTime = 60;

	private AvaniTimer() {}

	public static AvaniTimer getInstance() {
		if(_instance == null)
			_instance = new AvaniTimer();
		return _instance;
	}

	public void setTimer( Timerinterface _delegate, long seconds ) {
		this.delegate = _delegate;
		this.endTime = seconds;
		startTime = SystemClock.uptimeMillis();
		customHandler.postDelayed(updateTimerThread, 0);

	}

	private Runnable updateTimerThread = new Runnable() {

		public void run() {
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			updatedTime = timeSwapBuff + timeInMilliseconds;
			int secs = (int) (updatedTime / 1000);
			secs = secs % 60;
			if(secs != endTime){
				customHandler.postDelayed(this, 0);
			}
			else{
				customHandler.removeCallbacks(this);
				delegate.onTimerComplete();
			}
		}

	};


}
