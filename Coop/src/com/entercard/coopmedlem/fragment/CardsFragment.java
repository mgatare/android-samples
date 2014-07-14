package com.entercard.coopmedlem.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.entercard.coopmedlem.ApplicationEx;
import com.entercard.coopmedlem.CreditLineIncreaseActivity;
import com.entercard.coopmedlem.HomeScreenActivity;
import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.adapters.CardsAdapter;
import com.entercard.coopmedlem.entities.CardDataModel;

public class CardsFragment extends Fragment {

	private HomeScreenActivity parentActivity;
	
	private CardsAdapter cardsAdapter;
	private ListView allCardsListView;
	private Button btnIncreaseCreditLimit;
	private TextView lblCreditlimit;
	
	private ArrayList<CardDataModel> cardsArrayList;
	private int position;
	private String creditLimitTxt;
	private LinearLayout linearCardService;
	private LinearLayout linearBlockCard;
	
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
		
		setData(parentView);
		
		return parentView;
	}

	private void setData(View parentView) {
		
		parentActivity = (HomeScreenActivity) getActivity();
		allCardsListView = (ListView) parentView.findViewById(R.id.allCardsListView);
		lblCreditlimit = (TextView) parentView.findViewById(R.id.lblCreditlimit);
		linearCardService = (LinearLayout) parentView.findViewById(R.id.linearCardService);
		linearBlockCard = (LinearLayout) parentView.findViewById(R.id.linearBlockCard);
		btnIncreaseCreditLimit = (Button) parentView.findViewById(R.id.btnIncreaseCreditLimit);
		
		position = parentActivity.getAccountPosition();
		cardsArrayList = ApplicationEx.getInstance().getAccountsArrayList().get(position).getCardDataArrayList();
		creditLimitTxt= ApplicationEx.getInstance().getAccountsArrayList().get(position).getCreditLimit();
		
		lblCreditlimit.setText(creditLimitTxt);
		
		cardsAdapter = new CardsAdapter(parentActivity, 0, cardsArrayList);
		allCardsListView.setAdapter(cardsAdapter);
		cardsAdapter.notifyDataSetChanged();
		
		btnIncreaseCreditLimit.setOnClickListener(viewOnCLickListener);
		linearCardService.setOnClickListener(viewOnCLickListener);
		linearBlockCard.setOnClickListener(viewOnCLickListener);
	}
	
	/**
	 * OnCLick Listener for the Numbers of Service Center
	 */
	OnClickListener viewOnCLickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.linearCardService:

				break;
			case R.id.linearBlockCard:

				break;
			case R.id.btnIncreaseCreditLimit:
				Intent intent = new Intent(getActivity(), CreditLineIncreaseActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				break;
			default:
				break;
			}

		}
	};
}
