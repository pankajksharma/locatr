package com.example.locationfinder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;


public class MyLocationListener extends Service {
	LocationManager locationManager;
	LocationListener locationListener;
	
	public void onCreate(){
		//Toast.makeText(getApplicationContext(), "asdasd", Toast.LENGTH_LONG).show();
		checkPlace();
		
	}
	
	public void onResume() {
//		manager.requestLocationUpdates(provider, minTime, minDistance, intent)
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
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
					MySQLiteHelper myHelper = new MySQLiteHelper(MyLocationListener.this);
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
							AudioManager audio_manger = (AudioManager) MyLocationListener.this.getSystemService(MyLocationListener.this.AUDIO_SERVICE);
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
	
	
//	Location location, dest;
//	LocationManager locationManager;
//	GpsStatus.Listener gpsStatus;
//	Geocoder geocoder;
//	GpsSatellite gpsSatellite;
//	Criteria criteria;
//	LocationListener locationListener;
//	String provider = LocationManager.GPS_PROVIDER;
//	
//		
//	locationListener = new LocationListener() {
//		
//		@Override
//		public void onStatusChanged(String provider,  int arg1, Bundle arg2) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void onProviderEnabled(String provider) {
//			// TODO Auto-generated method stub
//			Toast.makeText(getApplication(), "enabled gps", Toast.LENGTH_LONG).show();
//		}
//		
//		@Override
//		public void onProviderDisabled(String arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void onLocationChanged(Location location) {
//			// TODO Auto-generated method stub
//	float a = location.getSpeed();
//	int b = (int) location.getAltitude();
//	double c = location.getLatitude();
//	double d = location.getLongitude();
//	long e = location.getTime();
//	float f = location.getBearing();
//	 x = location.distanceTo(dest);
//	
//	
//	geocoder = new Geocoder(getApplicationContext());
//	
//	
//	textView.setText("the current speed is" + a +" m/sec "+ " Elevation is"+b + "meters" +"   latitude is "+c+"  longitude is "+d+" time is "+e +"bearing is" + f);
//	Toast.makeText(getApplicationContext(), "distance to" + x , Toast.LENGTH_LONG).show();
//	
//	if(location != null){
//		if (location.distanceTo(dest) < 2000){
//			startActivity(intent);
//		}
//	}
//	
//	
//	criteria = new Criteria();
//	criteria.setAltitudeRequired(true);
//	
//	 xy = locationManager.getBestProvider(criteria, false);
//	Toast.makeText(this, xy, Toast.LENGTH_LONG).show();
//	
//		}
	
