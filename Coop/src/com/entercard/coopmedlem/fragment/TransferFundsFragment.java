package com.entercard.coopmedlem.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.utils.AlertHelper;

public class TransferFundsFragment extends Fragment {

	private Button btnFundsTransfer;
	
	public static TransferFundsFragment newInstance() {
		TransferFundsFragment fragment = new TransferFundsFragment();
		return fragment;
	}

	public TransferFundsFragment() {
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View parentView = inflater.inflate(R.layout.fragment_transfer_funds,
				container, false);
		btnFundsTransfer = (Button) parentView.findViewById(R.id.btnIncreaseCreditLimit);
		btnFundsTransfer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertHelper.Alert("Functionality not implemented yet." , getActivity());
			}
		});
		
		return parentView;
	}
}
