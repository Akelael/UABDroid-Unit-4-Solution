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


package org.uab.deic.uabdroid.solutions.unit4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

// As there is no Loader that enables us to load data asyncrhonously
// from a database, we mus create a new one
public class DatabaseCursorLoader extends AsyncTaskLoader<Cursor> 
{
	private DatabaseAdapter mDatabaseAdapter;
	private DatabaseReceiver mDatabaseReceiver;
	
	// When constructed, we open the database
	public DatabaseCursorLoader(Context _context) 
	{
		super(_context);
		mDatabaseAdapter = new DatabaseAdapter(getContext());
		mDatabaseAdapter.open();
	}

	// When the LoaderManager inits a load, we must call the
	// forceLoad() method from AsyncTaskLoader. Doing that, we 
	// are launching a new thread, that will execute the 
	// loadInBackground method
	@Override
	protected void onStartLoading() 
	{
		// The DatabaseReceiver is used to capture the update database event.
		// BroadcastReceivers are a part of CursorLoader, but we will see them at
		// unit 5
		if (mDatabaseReceiver == null)
		{
			mDatabaseReceiver = new DatabaseReceiver();
		}
		
		forceLoad();
		super.onStartLoading();
	}

	// This method will be executed in a new thread
	// Here we perform the data loading operations
	@Override
	public Cursor loadInBackground() 
	{	
		Cursor cursor = mDatabaseAdapter.getAllFormRegisters();
		
		return cursor;
	}
	
	// If the loader if reseted, we must do some cleaning :)
	@Override 
	protected void onReset() 
	{
		super.onReset();
         
		// Stop the loading, if it's being perfomed
		onStopLoading();

		// Close the database
		mDatabaseAdapter.close();
		mDatabaseAdapter = null;

		// Unregister the receiver
		if (mDatabaseReceiver != null) 
		{
			getContext().unregisterReceiver(mDatabaseReceiver);
			mDatabaseReceiver = null;
		}
     }
	
	private class DatabaseReceiver extends BroadcastReceiver
	{
		public DatabaseReceiver()
		{
			// We need to register the receiver to filter one or more intents
			IntentFilter filter = new IntentFilter(DatabaseAdapter.UPDATE_DATABASE);
            getContext().registerReceiver(this, filter);
		}
		
		@Override
		public void onReceive(Context _context, Intent _intent) 
		{
			if (_intent.getAction().compareTo(DatabaseAdapter.UPDATE_DATABASE) == 0)
			{
				// We call onContentChanged(), a method from the Loader, to indicate
				// that a change in the database has taken place, so the Loader has
				// to restart the loading process
				onContentChanged();
			}
		}		
	}
}
