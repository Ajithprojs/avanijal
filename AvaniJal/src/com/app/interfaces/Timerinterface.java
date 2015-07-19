package com.app.interfaces;

public interface Timerinterface {
	
	public void onTimerComplete(String taskName);
	
	public void onTimerCancelled(String taskName, String reason );
	

}
