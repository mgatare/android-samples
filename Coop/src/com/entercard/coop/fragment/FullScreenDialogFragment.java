package com.entercard.coop.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.entercard.coop.R;

public class FullScreenDialogFragment extends DialogFragment implements
		OnClickListener {
	public static FullScreenDialogFragment newInstance() {
		FullScreenDialogFragment f = new FullScreenDialogFragment();
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		//setStyle(DialogFragment.STYLE_NO_FRAME, 0);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black);
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_enter_pin, container,
				false);
		view.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
	}
}