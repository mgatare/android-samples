package com.entercard.coopmedlem.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.entercard.coopmedlem.utils.StringUtils;

public class CardsFragment extends Fragment {

	private HomeScreenActivity parentActivity;
	
	private CardsAdapter cardsAdapter;
	private ListView allCardsListView;
	private Button btnIncreaseCreditLimit;
	private TextView lblCreditlimit;
	private TextView lblCardServiceNumber;
	private TextView lblBlockCardNumber;
	
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
		
		lblCardServiceNumber = (TextView) parentView.findViewById(R.id.lblCardServiceNumber);
		lblBlockCardNumber = (TextView) parentView.findViewById(R.id.lblBlockCardNumber);
		
		position = parentActivity.getAccountPosition();
		cardsArrayList = ApplicationEx.getInstance().getAccountsArrayList().get(position).getCardDataArrayList();
		creditLimitTxt= ApplicationEx.getInstance().getAccountsArrayList().get(position).getCreditLimit();
		//08-19 15:33:52.382: I/(22978): localeTxt>>nb_NO
		if(StringUtils.getCurrentLocale().equalsIgnoreCase("nb_NO"))
			lblCreditlimit.setText(StringUtils.roundAndFormatCurrencyNorway(creditLimitTxt));
		 else
			lblCreditlimit.setText(StringUtils.roundAndFormatCurrency(creditLimitTxt));
			
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
				makeCall(lblCardServiceNumber.getText().toString());
				break;
				
			case R.id.linearBlockCard:
				makeCall(lblBlockCardNumber.getText().toString());
				break;
			/**
			 * Intent LaunchIntent =
			 * getPackageManager().getLaunchIntentForPackage
			 * ("com.package.address"); startActivity(LaunchIntent);
			 **/
			case R.id.btnIncreaseCreditLimit:
				Intent intent = new Intent(getActivity(), CreditLineIncreaseActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			default:
				break;
			}
		}

		private void makeCall(String string) {
			/*Check if Phone calling is supported or not then initiate the phone call*/
			PackageManager packageManager = parentActivity.getPackageManager();
			boolean canCall = packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
			if (canCall) {
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + StringUtils.trimStringOnly(string)));
				startActivity(intent);
			} else {
				parentActivity.longToast("This device does not support telephonic facility.");
			}
		}
	};
}
