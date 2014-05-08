package com.mayur.droid.testactionbarnavigationdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

/**
 * The Class ExtendedSupportMapFragment.
 */
public class ExtendedSupportMapFragment extends SupportMapFragment {

	/** The lat long. */
	private LatLng latLong;

	/**
	 * Instantiates a new extended support map fragment.
	 */
	public ExtendedSupportMapFragment() {
		super();
	}

	/**
	 * New instance.
	 * 
	 * @param position
	 *            the position
	 * @return the extended support map fragment
	 */
	public static ExtendedSupportMapFragment newInstance(LatLng position) {
		ExtendedSupportMapFragment frag = new ExtendedSupportMapFragment();
		frag.latLong = position;
		// setMapTransparent((ViewGroup) frag.getView());
		return frag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.android.gms.maps.SupportMapFragment#onCreate(android.os.Bundle
	 * )
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.android.gms.maps.SupportMapFragment#onCreateView(android.view
	 * .LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		View v = super.onCreateView(arg0, arg1, arg2);
		if (getMap() == null) {
			initMap();
		}
		setMapTransparent(arg1);
		return v;
	}

	/**
	 * Inits the map.
	 */
	private void initMap() {
		UiSettings settings = getMap().getUiSettings();
		settings.setAllGesturesEnabled(true);
		settings.setMyLocationButtonEnabled(true);
	}

	/**
	 * Sets the map transparent.
	 * 
	 * @param group
	 *            the new map transparent
	 */
	private static void setMapTransparent(ViewGroup group) {
		int childCount = group.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = group.getChildAt(i);
			if (child instanceof ViewGroup) {
				setMapTransparent((ViewGroup) child);
			} else if (child instanceof SurfaceView) {
				child.setBackgroundColor(0x00000000);
			}
		}
	}
}