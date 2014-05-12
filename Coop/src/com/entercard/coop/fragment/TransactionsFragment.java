package com.entercard.coop.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.entercard.coop.R;
import com.entercard.coop.adapters.TransactionsAdapter;
import com.entercard.coop.model.DataModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class TransactionsFragment extends Fragment {
	private ListView listView;

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

		setData();
		return rootView;
	}

	private void setData() {

		ArrayList<DataModel> arrayList = new ArrayList<DataModel>();

		for (int i = 0; i < 20; i++) {
			DataModel dataModel = new DataModel();
			dataModel.setId("00" + i);
			dataModel.setDate(i + "/01/2014");
			dataModel
					.setName("Lorem Ipsum is simply dummy text of the printing");
			dataModel.setPrice(i + "0000");
			arrayList.add(dataModel);
		}

		TransactionsAdapter transactionsAdapter = new TransactionsAdapter(
				getActivity(), 0, arrayList);
		listView.setAdapter(transactionsAdapter);
		transactionsAdapter.notifyDataSetChanged();
	}
}