package com.example.locationfinder;


import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	ListView saved_places;
	Button new_place_btn;
	ToggleButton toggle_btn;
	SharedPreferences serviceState;
	
	LocationManager locationManager;
	LocationListener locationListener;
	
	
	protected void onResume() {
		super.onResume();
		setPlaces();
		checkPlace();
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		saved_places = (ListView) findViewById(R.id.saved_places_list);
		new_place_btn = (Button) findViewById(R.id.new_place_btn);
		toggle_btn = (ToggleButton) findViewById(R.id.btn);
		
		serviceState = getSharedPreferences("serviceState", MODE_PRIVATE);
		
		
		if(serviceState.contains("state_of_service") && serviceState.getBoolean("state_of_service", false))
			toggle_btn.setChecked(true);
		else
			toggle_btn.setChecked(false);
		
		new_place_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent newLocationActivity = new Intent(getApplicationContext(), NewLocationActivity.class);
				startActivity(newLocationActivity);
			}
		});
		
		toggle_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				Toast.makeText(getApplicationContext(), "snm", Toast.LENGTH_LONG).show();
				if(isChecked)
					startService();
				else
					stopService();
			}
		});
		
	//	setPlaces();
		
	}
	


	private void setPlaces() {
		ArrayList<String> all_places = new ArrayList<String>();
		MySQLiteHelper myHelper = new MySQLiteHelper(this);
		Cursor places = myHelper.getAllPlaces();
		if(places.getCount() != 0){
			places.moveToFirst();
			while(!places.isAfterLast()){
				all_places.add(places.getString(0));
				places.moveToNext();
			}
			
			saved_places.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, all_places));
		}
	}	
	
	private void startService(){
		Intent locationService = new Intent(this, MyLocationListener.class);
		startService(locationService);
		
		SharedPreferences.Editor editor = serviceState.edit();
		editor.putBoolean("state_of_service", true);
		editor.commit();
		
	}
	
	private void stopService() {
		Intent locationService = new Intent(this, MyLocationListener.class);
		stopService(locationService);
		
		SharedPreferences.Editor editor = serviceState.edit();
		editor.putBoolean("state_of_service", false);
		editor.commit();
	}
	
	private void checkPlace() {
		// TODO Auto-generated method stub
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);	
		locationListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider,  int arg1, Bundle arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "current location is :_" + location, Toast.LENGTH_LONG).show();
				if(location != null){
					MySQLiteHelper myHelper = new MySQLiteHelper(MainActivity.this);
					Cursor places_details = myHelper.getAllPlaces();
					places_details.moveToFirst();
					while(!places_details.isAfterLast()){
						Location place_location = new Location(location);
						place_location.setLatitude(places_details.getDouble(1));
						place_location.setLongitude(places_details.getDouble(2));
						int radius = places_details.getInt(3);
						int ringing = places_details.getInt(4);
						if(location.distanceTo(place_location) < radius)
						{
							AudioManager audio_manger = (AudioManager) MainActivity.this.getSystemService(MainActivity.this.AUDIO_SERVICE);
							if(ringing == 1)
								audio_manger.setRingerMode(AudioManager.RINGER_MODE_SILENT);
							else
								audio_manger.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
						}
						places_details.moveToNext();
					}
				}
			}

		
	};
	
	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	}


}