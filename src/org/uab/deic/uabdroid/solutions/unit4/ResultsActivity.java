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

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class ResultsActivity extends Activity 
{
	@Override
	public void onCreate(Bundle _savedInstanceState) 
	{
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.results_layout);
		
		Intent intent = getIntent();		
		long id = intent.getLongExtra(DatabaseAdapter.KEY_ID, 0);

		DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
		databaseAdapter.open();
		Cursor cursor = databaseAdapter.getFormRegister(id);
		cursor.moveToFirst();
		
		TextView text = (TextView)findViewById(R.id.textview_result_name);
		text.setText(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME)));
		text = (TextView)findViewById(R.id.textview_result_developer);
		text.setText(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_DEVELOPER)));
		text = (TextView)findViewById(R.id.textview_result_date);
		text.setText(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_DATE)));
		text = (TextView)findViewById(R.id.textview_result_url);
		text.setText(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_URL)));
		
		cursor.close();
		databaseAdapter.close();
	}
}
