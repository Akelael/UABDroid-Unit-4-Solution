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

import java.util.Calendar;

import org.uab.deic.uabdroid.solutions.unit4.DatabaseAdapter;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class RandomDataService extends Service 
{
	@Override
	public int onStartCommand(Intent _intent, int _flags, int _startId) 
	{
		super.onStartCommand(_intent, _flags, _startId);
		
		new DummyaAsyncTask().execute();
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent _intent) 
	{
		return null;
	}
	
	private class DummyaAsyncTask extends AsyncTask <Void,Integer,Void>
	{
		@Override
		protected Void doInBackground(Void... _params) 
		{
			// First we clean the table
			DatabaseAdapter databaseAdapter = new DatabaseAdapter(getBaseContext());
			databaseAdapter.open();
			databaseAdapter.cleanApplicationsTable();
			databaseAdapter.close();
			
			int i = 0;
			
			while (i++<10)
			{
				// Every 5 seconds, we update the progress
				try 
				{
					Thread.sleep(5000);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				publishProgress(i);
			}
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... _progress) 
		{
			// As seen in the doInBackground method, every 5 seconds this method is called
			// and we add a new register to the database
			DatabaseAdapter databaseAdapter = new DatabaseAdapter(getBaseContext());
			databaseAdapter.open();
			databaseAdapter.insertApp("DummyApp " + _progress[0], "DummyDev " + _progress[0], Calendar.getInstance(), "no url :(");
			databaseAdapter.close();
			super.onProgressUpdate(_progress);
		}

		@Override
		protected void onPostExecute(Void _results) 
		{
			stopSelf();
			super.onPostExecute(_results);
		}
	}
}
