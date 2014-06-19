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
import android.widget.TextView;

import com.entercard.coopmedlem.ApplicationEx;
import com.entercard.coopmedlem.HomeScreenActivity;
import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.adapters.TransactionsAdapter;
import com.entercard.coopmedlem.entities.TransactionDataModel;
import com.entercard.coopmedlem.services.GetMoreTransactionsService;
import com.entercard.coopmedlem.services.GetMoreTransactionsService.GetMoreTransaction;
import com.entercard.coopmedlem.utils.AlertHelper;
import com.entercard.coopmedlem.utils.StringUtils;
import com.entercard.coopmedlem.view.LoadMoreListView;
import com.entercard.coopmedlem.view.LoadMoreListView.OnLoadMoreListener;

public class TransactionsFragment extends Fragment implements GetMoreTransaction {
	
	private TextView spentTextView;
	private TextView openbuyTextView;
	
	private int position;
	private String openToBuyCashTxt;
	private String spentCashTxt;
	private String uuidTxt;
	private String accountIDTxt;
	private String sessionIDTxt;
	
	private LoadMoreListView listViewTransactions;
	private ArrayList<TransactionDataModel> transactionsArrayList;
	private TransactionsAdapter transactionsAdapter;
	private HomeScreenActivity parentActivity;
	private GetMoreTransactionsService getMoreTransactionsService;
	private boolean isFirstCall;
	
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

		parentActivity = (HomeScreenActivity) getActivity();
		parentActivity.showDeveloperLog("openToBuyCash>>"+openToBuyCashTxt);
		
		position = parentActivity.getAccountPosition();
		openToBuyCashTxt = parentActivity.getOpenToBuy();
		spentCashTxt = parentActivity.getSpent();
		 
		transactionsArrayList = ApplicationEx.applicationEx.getAccountsArrayList().get(position).getTransactionDataArraylist();
		
		uuidTxt = ApplicationEx.applicationEx.getUUID();
		accountIDTxt = ApplicationEx.applicationEx.getAccountsArrayList().get(position).getAccountNumber();
		sessionIDTxt = ApplicationEx.applicationEx.getCookie();
		
		spentTextView = (TextView) rootView.findViewById(R.id.lblSpent);
		openbuyTextView = (TextView) rootView.findViewById(R.id.lblOpenToBuy);
		
		listViewTransactions = (LoadMoreListView) rootView.findViewById(R.id.listTransaction);
		listViewTransactions.setOnItemClickListener(new ListItemClickListener());
		
		if (null != transactionsArrayList && !transactionsArrayList.isEmpty()) {
			setData();
		} else {
			AlertHelper.Alert(getResources().getString(R.string.no_transactions_found), getActivity());
		}
		
		return rootView;
	}

	private void setData() {

		spentTextView.setText(spentCashTxt != null ? StringUtils.formatCurrencyLocally(spentCashTxt) : "?");
		openbuyTextView.setText(openToBuyCashTxt != null ? StringUtils.formatCurrencyLocally(openToBuyCashTxt): "?");

		transactionsAdapter = new TransactionsAdapter(getActivity(), 0, transactionsArrayList);
		listViewTransactions.setAdapter(transactionsAdapter);
		transactionsAdapter.notifyDataSetChanged();
		
		listViewTransactions.setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				
				int pageNumber = transactionsArrayList.get(position).getPage();
				int perNumber = transactionsArrayList.get(position).getPerPage();
				int total = transactionsArrayList.get(position).getTotal();
				
				Log.i("COOP", "PAGE::" + pageNumber + "::PERPAGE::" + perNumber + "::TOTAL::" + total);
				
				if (total > transactionsArrayList.size() && total == -1) {
					
					getMoreTransactionsService = new GetMoreTransactionsService(uuidTxt, sessionIDTxt, accountIDTxt, pageNumber);
					getMoreTransactionsService.setTransactionListener(TransactionsFragment.this);
					ApplicationEx.operationsQueue.execute(getMoreTransactionsService);
					
				} else {
					listViewTransactions.onLoadMoreComplete();
				}
			}
		});
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.entercard.coopmedlem.services.GetMoreTransactionsService.
	 * GetMoreTransaction#onGetMoreTransactionFinished(java.util.ArrayList)
	 */
	@Override
	public void onGetMoreTransactionFinished(final ArrayList<TransactionDataModel> accountArrayList) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				transactionsArrayList.addAll(accountArrayList);
				transactionsAdapter.notifyDataSetChanged();
				listViewTransactions.onLoadMoreComplete();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.entercard.coopmedlem.services.GetMoreTransactionsService.
	 * GetMoreTransaction#onGetMoreTransactionFailed(java.lang.String)
	 */
	@Override
	public void onGetMoreTransactionFailed(final String error) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertHelper.Alert(error, getActivity());
				listViewTransactions.onLoadMoreComplete();
			}
		});
	}
}