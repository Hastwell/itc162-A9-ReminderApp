package com.murach.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class ReminderService extends Service {

    public static final int FIRST_NOTIFICATION_DELAY = 5 * 1000;
	public static final int NOTIFICATION_INTERVAL_MINUTES = 1 * 1000 * 60;
    public static final int NOTIFICATION_ID = 498000;
    public static final String MESSAGE_TO_DISPLAY = "Look into the distance! It's good for your eyes!";
	
    private Timer reminderTimer;
	private boolean isRunning = false;
    
	@Override
	public void onCreate()
	{
		this.reminderTimer = new Timer();
	}
	
	@Override
	public IBinder onBind(Intent intent)
	{
      // Not a bound service; don't return a binding
      return null;
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if(!this.isRunning)
		{
			this.isRunning = true;
			this.reminderTimer.scheduleAtFixedRate(new TimerTask() { @Override public void run() { ReminderService.this.tickTimer(); }	}, FIRST_NOTIFICATION_DELAY, NOTIFICATION_INTERVAL_MINUTES);
            Log.d(ReminderService.class.getName(), "Service Started; was not already running");
        }
        else
        {
            Log.w(ReminderService.class.getName(), "Service was started while already running; not spawning another instance");
        }

		return Service.START_STICKY;
	}
	
	@Override
	public void onDestroy()
	{
		this.reminderTimer.cancel();
		this.isRunning = false;
        Log.d(ReminderService.class.getName(), "Service Stopped");
	}
	
	private void tickTimer()
	{
        Notification notif = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(MESSAGE_TO_DISPLAY)
                .setContentTitle("Hey you!")
                .setContentText(MESSAGE_TO_DISPLAY)
                .setAutoCancel(true)
                .build();

		// TODO: send a notification
        NotificationManager nManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, notif);
        Log.i(ReminderService.class.getName(), MESSAGE_TO_DISPLAY);
	}
}