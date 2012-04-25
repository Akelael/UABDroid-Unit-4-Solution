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

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAdapter 
{
	// String for the implicit intent for broadcasting the update database event
	public static final String UPDATE_DATABASE = "org.uab.deic.uabdroid.UPDATE_DATABASE";
	
	private static final String DB_NAME = "exemple.db";
	private static final int DB_VERSION = 1;
	private static final String DB_TABLE_FORM = "form";
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_DEVELOPER = "developer";
	public static final String KEY_DATE = "date";
	public static final String KEY_URL = "url";
	
	private static final String DATABASE_CREATE_FORM =
		"create table " + DB_TABLE_FORM + "("
		+ KEY_ID +" integer primary key, "
		+ KEY_NAME + " text not null, "
		+ KEY_DEVELOPER + " text not null, "
		+ KEY_DATE + " text not null, "
		+ KEY_URL + " text not null);";
	
	private SQLiteDatabase mDatabase;
	private DatabaseHelper mHelper;
	private Context mContext;
	
	private final Intent mUpdateDatabaseIntent = new Intent(UPDATE_DATABASE);
	
	public DatabaseAdapter(Context _context)
	{
		mContext = _context;
		mHelper = new DatabaseHelper(_context);
	}
	
	public void open()
	{
		mDatabase = mHelper.getWritableDatabase();
	}
	
	public void close()
	{
		mDatabase.close();
	}
	
	public boolean isOpen()
	{
		if (mDatabase == null)
		{
			return false;
		}
		return mDatabase.isOpen();
	}
	
	// Whenever that we update the database, we should inform of this fact
	// for the loader sake
	private void sendUpdatedDatabaseEvent()
	{
		mContext.sendBroadcast(mUpdateDatabaseIntent);
	}
	
	public void insertApp(String _name, String _developer, Calendar _calendar, String _url)
	{	
		ContentValues contentValues = new ContentValues();
		
		int day = _calendar.get(Calendar.DAY_OF_MONTH);
		int month = _calendar.get(Calendar.MONTH);
		int year = _calendar.get(Calendar.YEAR);
		String date = day + "/" + month + "/" + year;
		
		contentValues.put(KEY_NAME, _name);
		contentValues.put(KEY_DEVELOPER, _developer);
		contentValues.put(KEY_DATE, date);
		contentValues.put(KEY_URL, _url);
		
		mDatabase.insert(DB_TABLE_FORM, null, contentValues);
		
		sendUpdatedDatabaseEvent();
	}
	
	public Cursor getFormRegister(long _id)
	{
		String where = "_id=" + Long.toString(_id);
		
		return mDatabase.query(DB_TABLE_FORM, null, where, null, null, null, null);
	}
	
	public Cursor getAllFormRegisters()
	{
		String[] selectColumns = {KEY_ID, KEY_NAME, KEY_DEVELOPER, KEY_DATE};
		
		return mDatabase.query(DB_TABLE_FORM, selectColumns, null, null, null, null, null);
	}
	
	// Called to empty the table
	public void cleanApplicationsTable()
	{
		mDatabase.delete(DB_TABLE_FORM, null, null);
		
		sendUpdatedDatabaseEvent();
	}
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		public DatabaseHelper(Context _context)
		{
			super(_context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _database) 
		{
			_database.execSQL(DATABASE_CREATE_FORM);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _database, int _oldVersion, int _newVersion) 
		{
			
		}
	}
}
