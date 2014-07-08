package com.entercard.coopmedlem.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	private int tranxCount;
	private String openToBuyCashTxt;
	private String spentCashTxt;
	
	private LoadMoreListView transactionListView;
	private ArrayList<TransactionDataModel> transactionsArrayList;
	private TransactionsAdapter transactionsAdapter;
	private HomeScreenActivity parentActivity;
	private GetMoreTransactionsService getMoreTransactionsService;
	private int pageNumber = 0; //TEST
	
	public static TransactionsFragment newInstance() {
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

		View rootView = inflater.inflate(R.layout.fragment_transactions,container, false);
		init(rootView);

		transactionsArrayList = ApplicationEx.getInstance()
				.getAccountsArrayList().get(position)
				.getTransactionDataArraylist();

		if (null != transactionsArrayList && !transactionsArrayList.isEmpty()) {
			setData();
		} else {
			AlertHelper.Alert(getResources().getString(R.string.no_transactions_found),
					getActivity());
		}
		return rootView;
	}

	private void init(View rootView) {

		parentActivity = (HomeScreenActivity) getActivity();
		// Get Initial values for the account
		position = parentActivity.getAccountPosition();
		openToBuyCashTxt = parentActivity.getOpenToBuy();
		spentCashTxt = parentActivity.getSpent();
		tranxCount = parentActivity.getTransactionsCount();

		spentTextView = (TextView) rootView.findViewById(R.id.lblSpent);
		openbuyTextView = (TextView) rootView.findViewById(R.id.lblOpenToBuy);

		transactionListView = (LoadMoreListView) rootView
				.findViewById(R.id.listTransaction);
	}

	private void setData() {

		spentTextView.setText(spentCashTxt != null ? StringUtils.formatCurrencyLocally(spentCashTxt) : "?");
		openbuyTextView.setText(openToBuyCashTxt != null ? StringUtils.formatCurrencyLocally(openToBuyCashTxt): "?");
		/***/
		transactionsAdapter = new TransactionsAdapter(getActivity(), 0, transactionsArrayList);
		transactionListView.setAdapter(transactionsAdapter);
		transactionsAdapter.notifyDataSetChanged();
		
		transactionListView.setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				
				/*int pageNumber = transactionsArrayList.get(position).getPage();
				int perNumber = transactionsArrayList.get(position).getPerPage();
				int total = transactionsArrayList.get(position).getTotal();*/
				
				//Log.e("COOP", "<PAGE>" + pageNumber + "<PERPAGE>" + perNumber + "<TOTAL>" + total);
				Log.e("COOP", "<SIZE>"+transactionsArrayList.size());
				
				if (transactionsArrayList.size() < tranxCount) {

					String uuidTxt = ApplicationEx.getInstance().getUUID();
					String accountIDTxt = ApplicationEx.getInstance().getAccountsArrayList().get(position).getAccountNumber();
					String sessionIDTxt = ApplicationEx.getInstance().getCookie();
					
					getMoreTransactionsService = new GetMoreTransactionsService(uuidTxt, sessionIDTxt, accountIDTxt, pageNumber);
					getMoreTransactionsService.setTransactionListener(TransactionsFragment.this);
					ApplicationEx.operationsQueue.execute(getMoreTransactionsService);
					++pageNumber;
				} else {
					transactionListView.onLoadMoreComplete();
				}
			}
		});
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
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
				//TODO need to add the transaction to ArrayList so need not again make WS calls for next 50 tranx
				transactionsArrayList.addAll(accountArrayList);
				transactionsAdapter.notifyDataSetChanged();
				transactionListView.onLoadMoreComplete();
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
				if (!TextUtils.isEmpty(error)) {
					AlertHelper.Alert(error, getActivity());
					transactionListView.onLoadMoreComplete();
				} else {
					AlertHelper.Alert(ApplicationEx.getInstance().getString(R.string.exception_general), getActivity());
					transactionListView.onLoadMoreComplete();
				}
			}
		});
	}
}