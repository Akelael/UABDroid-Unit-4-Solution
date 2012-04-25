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

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

public class DownloadService extends Service 
{
	@Override
	public int onStartCommand(Intent _intent, int _flags, int _startId) 
	{
		super.onStartCommand(_intent, _flags, _startId);
		
		// We call the AsyncTask instead of trying to download the file
		// directly here
		new CelestialAsyncTask().execute();
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}
	
	private class CelestialAsyncTask extends AsyncTask <Void,Void,Void>
	{
		// All the work is going to be done by this method, because it is
		// executed in a different thread than the Main Thread
		@Override
		protected Void doInBackground(Void... _params) 
		{
			try 
			{
				URL url = new URL("http://www.google.es");
				InputStream inputStream = url.openConnection().getInputStream();
				
				FileOutputStream fileOutputStream = openFileOutput("index.html",Context.MODE_PRIVATE);
				
				byte[] buffer = new byte[1024];
				
				while ((inputStream.read(buffer) > -1))
				{
					fileOutputStream.write(buffer);
				}
				fileOutputStream.close();
				inputStream.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			} 
			return null;
		}

		@Override
		protected void onPostExecute(Void _results) 
		{
			Toast toast = Toast.makeText(getBaseContext(), "Descarrega completada!", Toast.LENGTH_LONG);
			toast.show();
			// When the file is downloaded, stop the Service
			stopSelf();
			super.onPostExecute(_results);
		}
	}
}
