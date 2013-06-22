package com.example.locationfinder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper{
	
	private static final String DB_NAME = "locations";
	private static final String TABLE_NAME = "saved_locations";
	private static final String COL_ID = "id";
	private static final String COL_LAT = "latitude";
	private static final String COL_LONG = "logitude";
	private static final String COL_NAME = "name";
	private static final String COL_RADIUS = "radius";
	private static final String COL_RING_VAL = "ringer_value";
	
	
	
	public MySQLiteHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}
	
	public void addLocation(String place_name, Location location, int radius, Boolean ring){
		
		String location_name = (place_name == null)? "Unknown" : place_name;
		Log.d("", location_name);
		int ringer_val = (ring) ? 1 : 0;
		SQLiteDatabase db = this.getWritableDatabase();
//		Toast.makeText(context, db+"", Toast.LENGTH_LONG).show();
		db.execSQL("INSERT INTO "+TABLE_NAME+"( "+COL_NAME+", "+COL_LAT+", "+COL_LONG+", "+COL_RADIUS+", "+COL_RING_VAL+") VALUES ('"+location_name+"', "+location.getLatitude()+", "+location.getLongitude()+", "+radius+", "+ringer_val+")");
	}

	
	public Cursor getAllPlaces(){
		SQLiteDatabase db = this.getReadableDatabase();
		String[] cols = {COL_NAME, COL_LAT, COL_LONG, COL_RADIUS, COL_RING_VAL};
		return db.query(TABLE_NAME, cols, null, null, null, null, null);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("oncreate", "oncreate");
		db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_NAME+" TEXT, "+COL_LAT+" FLOAT, "+COL_LONG+" FLOAT, "+COL_RADIUS+" INTEGER, "+COL_RING_VAL+" INTEGER)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
}