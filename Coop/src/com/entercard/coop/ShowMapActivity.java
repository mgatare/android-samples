package com.entercard.coop;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

public class ShowMapActivity extends FragmentActivity {

	//private Button btnClose;
	private Button btnDispute;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_show_map);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("Map");
		actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		
		if(null == arg0) {
		
		 if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SERVICE_INVALID
	                || GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SERVICE_MISSING) {
	            Log.e("HERE", "Google play not active");

	           // TextViewFragment tvf = new TextViewFragment();
	           // getChildFragmentManager().beginTransaction().replace(R.id.mapview, tvf).commit();
	            Toast.makeText(this, "Play Services not found.", Toast.LENGTH_LONG).show();

	        } else {
				getSupportFragmentManager().beginTransaction()
						.add(R.id.MapContainer, CustomMapFragment.newInstance(new LatLng(19.100050, 72.920343)), "map_screen")
						.commit();
	        }
		}
		
		btnDispute = (Button) findViewById(R.id.btnDispute);
		btnDispute.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(ShowMapActivity.this, "Raise a Dispute", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent upIntent = new Intent(this, HomeScreenActivity.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.from(this).addNextIntent(upIntent).startActivities();
				finish();
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			} else {
				NavUtils.navigateUpTo(this, upIntent);
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
	}
	
	/**
	 * Map Fragment Class
	 * @author mgatare
	 *
	 */
	public static class CustomMapFragment extends SupportMapFragment {
		private static LatLng latLong;

		public CustomMapFragment() {
			super();

		}

		public static CustomMapFragment newInstance(LatLng position) {
			CustomMapFragment fragment = new CustomMapFragment();
			latLong = position;
			return fragment;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			if (getMap() != null) {
				initMap();
				Log.d(getClass().getSimpleName(), "Map ready for use!");
			}
		}

		@Override
		public void onResume() {
			super.onResume();
			initMap();
		}

		private void initMap() {
			Log.v("CustomMapFragment", "initMap");
			if (getMap() != null) {
				
				GoogleMap gMap = getMap();
				
				//Get the current location
				Location location = getCurrentLocation(gMap); 
				
				UiSettings settings = gMap.getUiSettings();
				settings.setAllGesturesEnabled(true);
				settings.setMyLocationButtonEnabled(true);
	            settings.setZoomControlsEnabled(false);
				
	            gMap.clear();
				
				if (null == location) {
					gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 14));
					//gMap.addMarker(new MarkerOptions().position(latLong).draggable(false));
				} else {
					LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
					gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
					//gMap.addMarker(new MarkerOptions().position(currentLatLng).draggable(false));
				}
			}
		}
		/**
		 * 
		 * @param googleMap
		 * @return
		 */
		private Location getCurrentLocation(GoogleMap googleMap) {
	        googleMap = getMap();
	        googleMap.setMyLocationEnabled(true);
	        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

	        Criteria criteria = new Criteria();
	        String provider = locationManager.getBestProvider(criteria, true);
	        Location location = locationManager.getLastKnownLocation(provider);

	        LocationListener locationListener = new LocationListener() {
				@Override
				public void onLocationChanged(Location location) {
					/**Update the Location Object to get new locations**/
				}
			};
			//locationManager.requestLocationUpdates(provider, 50000, 500, locationListener);
			return location;
		}
	}
}