package com.entercard.coop.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.entercard.coop.R;
import com.entercard.coop.adapters.CardsAdapter;
import com.entercard.coop.entities.DataModel;

public class CardsFragment extends Fragment {

	private CardsAdapter cardsAdapter;
	private ListView allCardsListView;
	private String [] cardHolderName = {"Mayur G", "Mayuresh G", "Mayur G"};
	private Button btnIncreaseCreditLimit;
	
	public static CardsFragment newInstance() {
		CardsFragment fragment = new CardsFragment();
		return fragment;
	}

	public CardsFragment() {
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View parentView = inflater.inflate(R.layout.fragment_cards, container, false);
		allCardsListView = (ListView) parentView.findViewById(R.id.allCardsListView);
		setData();
		
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle("Credit Line Increase");
		
		btnIncreaseCreditLimit = (Button) parentView.findViewById(R.id.btnIncreaseCreditLimit);
		btnIncreaseCreditLimit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Clicked Credit line increase", Toast.LENGTH_SHORT).show();
			}
		});
		
		return parentView;
	}

	private void setData() {

		ArrayList<DataModel> arrayList = new ArrayList<DataModel>();

		for (int i = 0; i < 3; i++) {
			
			DataModel dataModel = new DataModel();
			dataModel.setId("XXXX XXXX XXXX 120"+ i+1);
			dataModel.setName(""+cardHolderName[i]);
			arrayList.add(dataModel);
		}

		cardsAdapter = new CardsAdapter(getActivity(), 0, arrayList);
		allCardsListView.setAdapter(cardsAdapter);
		cardsAdapter.notifyDataSetChanged();
	}
}
