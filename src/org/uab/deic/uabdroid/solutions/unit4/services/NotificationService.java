/*
   Copyright 2012 Ruben Serrano

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */


package org.uab.deic.uabdroid.solutions.unit4.services;

import org.uab.deic.uabdroid.solutions.unit4.AppListActivity;
import org.uab.deic.uabdroid.solutions.unit4.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;


public class NotificationService extends Service 
{
	@Override
	public int onStartCommand(Intent _intent, int _flags, int _startId) 
	{
		super.onStartCommand(_intent, _flags, _startId);
		
		// Get the NotificationManager instance for using it later 
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		// The icon that should be displayed at the notification bar
		int icon = R.drawable.ic_launcher;
		// The text for the notification
		String text = "Notificacio";
		// The date when the notification occurred
		long when = System.currentTimeMillis();
		
		// With the above parameters, create a Notification
		Notification notification  = new Notification(icon, text, when);
		
		// Now, we are going to add some details for the extended notification bar
		// Notice that we need the application's context instead of the Service's one
		Context context = getApplicationContext();
		// Title for the expanded status
		String expandedTitle = "Notificacio extesa";
		// Text for the expanded status
		String expandedText = "Aixo es una notificacio"; 
		// Pending Intent to launch an Activity when the notification is clicked
		Intent intent = new Intent(this, AppListActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		
		// Set the above parameters
		notification.setLatestEventInfo(context, expandedTitle, expandedText, pendingIntent);
		
		// And finally fire the notification
		notificationManager.notify(1,notification);
		
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent _intent) 
	{
		return null;
	}
}
