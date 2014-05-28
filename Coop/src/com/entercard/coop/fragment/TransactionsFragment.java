package com.entercard.coop.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.entercard.coop.R;
import com.entercard.coop.ShowDisputeOnMapActivity;
import com.entercard.coop.adapters.TransactionsAdapter;
import com.entercard.coop.model.DataModel;
import com.entercard.coop.utils.StringUtils;

public class TransactionsFragment extends Fragment {
	private ListView listView;
	private ArrayList<DataModel> arrayList;
	public static TransactionsFragment newInstance(int sectionNumber) {
		TransactionsFragment fragment = new TransactionsFragment();
		return fragment;
	}

	public TransactionsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_transactions,
				container, false);

		listView = (ListView) rootView.findViewById(R.id.listView);
		listView.setOnItemClickListener(new ListItemClickListener());
		setData();
		return rootView;
	}

	private void setData() {

		arrayList = new ArrayList<DataModel>();

		for (int i = 0; i < 60; i++) {
			DataModel dataModel = new DataModel();
			dataModel.setId("00" + i);
			
			if (i > 30)
				dataModel.setDate((i-30) + "/01/2014");
			else
				dataModel.setDate(i + "/01/2014");
			
			dataModel.setName("POLARN O PYRET");
			String price = (i+1) + "000";
			dataModel.setPrice(StringUtils.formatCurrency(price));
			dataModel.setShowMap(true);
			
			arrayList.add(dataModel);
		}

		TransactionsAdapter transactionsAdapter = new TransactionsAdapter(getActivity(), 0, arrayList);
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
			if (null != arrayList && arrayList.size() > 0) {
				boolean isMapNeeded = arrayList.get(arg2).getShowMap();
				if (isMapNeeded) {
					Intent intent = new Intent(getActivity(),ShowDisputeOnMapActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				}
			}
		}
	}
}