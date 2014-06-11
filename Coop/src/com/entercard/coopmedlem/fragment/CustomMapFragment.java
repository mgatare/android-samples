package com.entercard.coopmedlem.fragment;
//package com.entercard.coop.fragment;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.UiSettings;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//public class CustomMapFragment extends SupportMapFragment {
//	private static LatLng mPosFija;
//
//	public CustomMapFragment() {
//		super();
//
//	}
//
//	public static CustomMapFragment newInstance(LatLng position) {
//		CustomMapFragment fragment = new CustomMapFragment();
//		mPosFija = position;
//		return fragment;
//	}
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//
//		if (getMap() != null) {
//			initMap();
//			Log.d(getClass().getSimpleName(), "Map ready for use!");
//		}
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		initMap();
//	}
//
//	private void initMap() {
//		Log.v("CustomMapFragment", "initMap");
//		if (getMap() != null) {
//			UiSettings settings = getMap().getUiSettings();
//			settings.setAllGesturesEnabled(true);
//			settings.setMyLocationButtonEnabled(false);
//
//			getMap().clear();
//			getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(mPosFija, 5));
//			getMap().addMarker(
//					new MarkerOptions().position(mPosFija).draggable(false));
//		}
//	}
//}