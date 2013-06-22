package com.example.locationfinder;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

public class NewLocationActivity extends Activity implements LocationListener {
LocationManager locationManager;
EditText edtLocationName;
AudioManager audioManager;
Location location, myLocation;
Criteria criteria;
SeekBar seekBar;
Button button;
int ProgressVal=200;
ToggleButton toggleButton;
SharedPreferences preferences;
Boolean toggleval = false;
String bestProvider;
public static String mpref = "prefer";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_location);
		button = (Button) findViewById(R.id.button1);
		edtLocationName = (EditText) findViewById(R.id.editText1);
		 preferences = getSharedPreferences(mpref, 0);
		 myLocation = new Location("");
		 seekBar = (SeekBar) findViewById(R.id.seekBar1);
		toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				ProgressVal = seekBar.getProgress() + 200;
			}
		});
		toggleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(preferences.getBoolean("toggle", false) == false)
				{
				toggleval= preferences.edit().putBoolean("toggle", true).commit();
				}
				else 
				toggleval= 	preferences.edit().putBoolean("toggle", false).commit();
				
			}
		});
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		 criteria =  new Criteria();
		 criteria.setAccuracy(Criteria.ACCURACY_FINE);
		 bestProvider = locationManager.getBestProvider(criteria, true);
		 button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				saveLocation();
				
			}

			private void saveLocation() {
				MySQLiteHelper myHelper = new MySQLiteHelper(NewLocationActivity.this);
				myHelper.addLocation(edtLocationName.getText().toString(), myLocation, ProgressVal, toggleval);
				Toast.makeText(getApplicationContext(), "Added Location Successfully!", Toast.LENGTH_LONG).show();
				NewLocationActivity.this.finish();
			}
		});

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.prox, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		myLocation.set(location);
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			locationManager.requestLocationUpdates(bestProvider, 0, 0, this);
			if(preferences.getBoolean("toggle", false) == true)
			{
				toggleButton.setChecked(true);
			}
			
			else 
				toggleButton.setChecked(false);
		}

}
