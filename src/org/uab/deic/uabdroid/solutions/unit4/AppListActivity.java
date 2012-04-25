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

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

// This activity now obtains the data for filling the form from
// a loader instead of directly from the cursor
public class AppListActivity extends FragmentActivity
{
	private static final String[] COLUMNS_FROM = {DatabaseAdapter.KEY_NAME,DatabaseAdapter.KEY_DEVELOPER, DatabaseAdapter.KEY_DATE};
	private static final int[] VIEWS_TO = {R.id.item_textview_result_name, R.id.item_textview_result_developer, R.id.item_textview_result_date};
	
	private SimpleCursorAdapter mCursorAdapter; 
			
	@Override
	public void onCreate(Bundle _savedInstanceState) 
	{
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.listactivity);
		
		// The adapter is created without a cursor (third parameter is null)
		mCursorAdapter = new SimpleCursorAdapter(getBaseContext(), 
													R.layout.results_list_item, 
													null, 
													COLUMNS_FROM, 
													VIEWS_TO);

		// Set the listView adapter to the one we have just created
		ListView listView = (ListView)findViewById(R.id.list_view);
		listView.setAdapter(mCursorAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> _parentView, View _view, int _position, long _id) 
			{
				Intent intent = new Intent(getBaseContext(), ResultsActivity.class);
				intent.putExtra(DatabaseAdapter.KEY_ID, _id);
				startActivity(intent);
			}
		});
		
		// We call the LoaderManager with a new instance of an object that implements the LoaderCallbacks
		// interface, which is defined below
		getSupportLoaderManager().initLoader(0, null, new DatabaseCursorLoaderCallback());	
	}

	// A class that implements the interface LoaderCallbacks to call the Loader and
	// obtain the results
	private class DatabaseCursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>
	{
		// In the constructor, obtain a new Loader
		@Override
		public Loader<Cursor> onCreateLoader(int _id, Bundle _args) 
		{
			return new DatabaseCursorLoader(getBaseContext());
		}

		// When the load process has finished, we clear the previous curso, if any, and
		// fill the adapter with the resultant cursor
		@Override
		public void onLoadFinished(Loader<Cursor> _loader, Cursor _cursor) 
		{
			Cursor oldCursor = null;
			if ((oldCursor = mCursorAdapter.getCursor())!=null)
			{
				oldCursor.close();
			}
			mCursorAdapter.changeCursor(_cursor);
		}

		// If the Loader is reseted, we empty the adapter
		@Override
		public void onLoaderReset(Loader<Cursor> arg0) 
		{
			mCursorAdapter.changeCursor(null);
		}
	}
}
