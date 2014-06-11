package com.entercard.coopmedlem.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.entercard.coopmedlem.ApplicationEx;
import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.adapters.TransactionsAdapter;
import com.entercard.coopmedlem.entities.DataModel;
import com.entercard.coopmedlem.entities.TransactionDataModel;
import com.entercard.coopmedlem.utils.AlertHelper;

public class TransactionsFragment extends Fragment {
	
	private TextView spentTextView;
	private TextView openbuyTextView;
	private ListView listView;
	
	private int position;
	private String openToBuyCash;
	private String spentCash;
	
	private ArrayList<DataModel> arrayList;
	private ArrayList<TransactionDataModel> transactionsArrayList;
	
	public static TransactionsFragment newInstance(int sectionNumber) {
		TransactionsFragment fragment = new TransactionsFragment();
		return fragment;
	}

	public TransactionsFragment() {
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_transactions,
				container, false);

		position = getActivity().getIntent().getExtras().getInt(getResources().getString(R.string.tag_position)); 
		openToBuyCash = getActivity().getIntent().getExtras().getString(getResources().getString(R.string.tag_open_to_buy));
		spentCash = getActivity().getIntent().getExtras().getString(getResources().getString(R.string.tag_spent));
		
		// ActionBar actionBar = getActivity().getActionBar();
		// actionBar.setTitle("Transactions");

		transactionsArrayList = ApplicationEx.applicationEx.getAccountsArrayList().get(position).getTransactionDataArraylist();
		
		spentTextView = (TextView) rootView.findViewById(R.id.spentTextView);
		openbuyTextView = (TextView) rootView.findViewById(R.id.openbuyTextView);
		
		listView = (ListView) rootView.findViewById(R.id.listView);
		listView.setOnItemClickListener(new ListItemClickListener());
		
		if (null != transactionsArrayList && transactionsArrayList.size() > 0) {
			setData();
		} else {
			AlertHelper.Alert(getResources().getString(R.string.no_transactions_found), getActivity());
		}
		
		return rootView;
	}

	private void setData() {

		spentTextView.setText(spentCash != null ? roundAmount(spentCash) : "?");
		openbuyTextView
				.setText(openToBuyCash != null ? roundAmount(openToBuyCash)
						: "?");

		TransactionsAdapter transactionsAdapter = new TransactionsAdapter(
				getActivity(), 0, transactionsArrayList);
		listView.setAdapter(transactionsAdapter);
		transactionsAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	/**
	 * 
	 * @author mgatare
	 *
	 */
	public class ListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Log.i("", "ItemClicked::" + arg2);
//			if (null != arrayList && arrayList.size() > 0) {
//				boolean isMapNeeded = arrayList.get(arg2).getShowMap();
//				if (isMapNeeded) {
//					Intent intent = new Intent(getActivity(),ShowDisputeOnMapActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(intent);
//					getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//				}
//			}
			AlertHelper.Alert("Functionality not implemented yet." , getActivity());
		}
	}
	
	/**
	 * @param amount
	 * @return
	 */
	private String roundAmount(String amount) {

		// DecimalFormat df=new DecimalFormat("0.00");
		// String formate = df.format(amount);
		// double finalValue = (Double)df.parse(formate);
		
		double doubleAmount = Double.parseDouble(amount);
		double finalValue = (double) Math.round(doubleAmount * 100) / 100;
		String finalTxt = String.valueOf(finalValue).replace('.', ',');
		return String.valueOf(finalTxt);
	}
} 