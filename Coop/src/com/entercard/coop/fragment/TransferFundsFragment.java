package com.entercard.coop.fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entercard.coop.R;

public class TransferFundsFragment extends Fragment {

	public TransferFundsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View parentView = inflater.inflate(R.layout.fragment_transfer_funds,
				container, false);

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle("Funds Transfer");
		return parentView;
	}

}
