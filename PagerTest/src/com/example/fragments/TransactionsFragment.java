package com.example.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.adapters.TransactionsAdapter;
import com.example.pagertest.DataModel;
import com.example.pagertest.R;

public class TransactionsFragment extends Fragment {
	private ListView listView;
	private ArrayList<DataModel> arrayList;
	public static TransactionsFragment newInstance() {
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

		for (int i = 0; i < 40; i++) {
			DataModel dataModel = new DataModel();
			dataModel.setId("00" + i);
			dataModel.setDate(i + "/01/2014");
			dataModel.setName("POLARN O PYRET");
			dataModel.setPrice((i+1) + "000.00");
			//if(i==2) 
				dataModel.setShowMap(true);	
			//else
			//	dataModel.setShowMap(false);
			arrayList.add(dataModel);
		}

		TransactionsAdapter transactionsAdapter = new TransactionsAdapter(getActivity(), 0, arrayList);
		listView.setAdapter(transactionsAdapter);
		transactionsAdapter.notifyDataSetChanged();
	}
	
	public class ListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Log.i("", "ItemClicked::" + arg2);
			if (null != arrayList && arrayList.size() > 0) {
				boolean isMapNeeded = arrayList.get(arg2).getShowMap();
//				if (isMapNeeded) {
//					Intent intent = new Intent(getActivity(),ShowMapActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(intent);
//					getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//				}
			}
		}
	}
}