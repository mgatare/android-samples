package com.no.entercard.coopmedlem.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.no.entercard.coopmedlem.ApplicationEx;
import com.no.entercard.coopmedlem.BaseActivity;
import com.no.entercard.coopmedlem.HomeScreenActivity;
import com.no.entercard.coopmedlem.R;
import com.no.entercard.coopmedlem.adapters.ExpandableListAdapter;
import com.no.entercard.coopmedlem.entities.TransactionDataModel;
import com.no.entercard.coopmedlem.services.GetMoreTransactionsService;
import com.no.entercard.coopmedlem.services.GetMoreTransactionsService.GetMoreTransaction;
import com.no.entercard.coopmedlem.utils.AlertHelper;
import com.no.entercard.coopmedlem.utils.StringUtils;
import com.no.entercard.coopmedlem.view.LoadMoreExpandableListView;
import com.no.entercard.coopmedlem.view.LoadMoreExpandableListView.OnLoadMoreListener;

public class TransactionsFragment extends Fragment implements GetMoreTransaction {
	
	private TextView spentTextView;
	private TextView openbuyTextView;
	
	private int position;
	private int tranxCount;
	private String openToBuyCashTxt;
	private String spentCashTxt;
	private ProgressBar progSpentCash;
	
	private LoadMoreExpandableListView transactionListView;
	private ArrayList<TransactionDataModel> transactionsArrayList;
	private HomeScreenActivity parentActivity;
	private GetMoreTransactionsService getMoreTransactionsService;
	private int pageNumber = 0;
	private Handler handler;
	private ProgressBarAnimation barAnimation;
	private LinearLayout headerLayout;
	private ExpandableListAdapter transactionExplistAdapter;
	
	public static TransactionsFragment newInstance() {
		TransactionsFragment fragment = new TransactionsFragment();
		return fragment;
	}

	public TransactionsFragment() {
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_transactions,container, false);
		init(rootView);

		transactionsArrayList = ApplicationEx.getInstance().getAccountsArrayList().get(position)
				.getTransactionDataArraylist();

		if (null != transactionsArrayList && !transactionsArrayList.isEmpty()) {
			setData();
		} else {
			AlertHelper.Alert(getResources().getString(R.string.no_transactions_found),getActivity());
		}
		// Set the Progress bar and other animations
		setProgressBarValue();
		
		return rootView;
	}

	/**
	 * @param rootView
	 */
	private void init(View rootView) {

		parentActivity = (HomeScreenActivity) getActivity();
		handler = new Handler();
		
		LayoutInflater layoutInflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		headerLayout =  (LinearLayout) layoutInflater.inflate(R.layout.header_transaction_fragment, null ,false);
		
		// Get Initial values for the account
		position = parentActivity.getAccountPosition();
		openToBuyCashTxt = parentActivity.getOpenToBuy();
		spentCashTxt = parentActivity.getSpent();
		tranxCount = parentActivity.getTransactionsCount();

		spentTextView = (TextView) headerLayout.findViewById(R.id.lblSpent);
		openbuyTextView = (TextView) headerLayout.findViewById(R.id.lblOpenToBuy);
		progSpentCash = (ProgressBar) headerLayout.findViewById(R.id.progSpentCash);

		transactionListView = (LoadMoreExpandableListView) rootView.findViewById(R.id.listTransaction);
		transactionListView.addHeaderView(headerLayout);
		
	}

	private void setData() {
		
		transactionExplistAdapter= new ExpandableListAdapter(getActivity(), transactionsArrayList);
		transactionListView.setAdapter(transactionExplistAdapter);
		//transactionExplistAdapter.notifyDataSetChanged();
		
		transactionListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View view,
					int groupPosition, long id) {
				//Log.d("", ">>>>>>"+transactionsArrayList.get(groupPosition).getIsDisputable());
				if(transactionsArrayList.get(groupPosition).getIsDisputable()) {
					return false;
				} else {
					return true;//consume click
				}
			}
		});
		
		// Listview Group expanded listener for showing only one group expanded
		transactionListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				int len = transactionExplistAdapter.getGroupCount();
				for (int i = 0; i < len; i++) {
					if (i != groupPosition) {
						transactionListView.collapseGroup(i);
					}
				}
			}
		});
		
		transactionListView.setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				//Log.i("", "transactionsArrayList.size()--"+transactionsArrayList.size());
				//Log.i("", "tranxCount---"+tranxCount);
				if (transactionsArrayList.size() <= tranxCount) {

					String uuidTxt = ApplicationEx.getInstance().getUUID();
					String accountIDTxt = ApplicationEx.getInstance().getAccountsArrayList().get(position).getAccountNumber();
					String cookieTxt = ApplicationEx.getInstance().getCookie();
					
					getMoreTransactionsService = new GetMoreTransactionsService(uuidTxt, cookieTxt, accountIDTxt, pageNumber);
					getMoreTransactionsService.setTransactionListener(TransactionsFragment.this);
					ApplicationEx.operationsQueue.execute(getMoreTransactionsService);
					++pageNumber;
					
				} else {
					transactionListView.onLoadMoreComplete();
				}
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.entercard.coopmedlem.services.GetMoreTransactionsService.
	 * GetMoreTransaction#onGetMoreTransactionFinished(java.util.ArrayList)
	 */
	@Override
	public void onGetMoreTransactionFinished(final ArrayList<TransactionDataModel> accountArrayList) {
		parentActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				int position = transactionsArrayList.size()-5;
				
				transactionListView.onLoadMoreComplete();
				transactionsArrayList.addAll(accountArrayList);
				
				// hack since adapter.notifydatasetchanged() is not calling ExpandableListView base adapter
				transactionExplistAdapter= new ExpandableListAdapter(getActivity(), transactionsArrayList);
				transactionListView.setAdapter(transactionExplistAdapter);
				transactionListView.setSelection(position);
				
				transactionExplistAdapter.notifyDataSetChanged();
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
		parentActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!TextUtils.isEmpty(error)) {
					AlertHelper.Alert(error, getActivity());
					transactionListView.onLoadMoreComplete();
				} else {
					AlertHelper.Alert(getResources().getString(R.string.encap_something_went_wrong),
							ApplicationEx.getInstance().getString(R.string.encap_something_went_wrong), getActivity());
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
	
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	private void setProgressBarValue() {
		
		double otb = Double.parseDouble(openToBuyCashTxt);
		double spent = Double.parseDouble(spentCashTxt);
		
		int otbValue = (int)Math.round(otb);
		int spentValue = (int)Math.round(spent);
		
		int TOTAL = otbValue + spentValue;
		int percentageDiff = (int)(spentValue * 100 / TOTAL);

		if (otbValue == spentValue) {
			barAnimation = new ProgressBarAnimation(progSpentCash, 0, 100);
		} else {
			barAnimation = new ProgressBarAnimation(progSpentCash, 0, percentageDiff);
		}
		
		//Log.d("COOP", "BaseActivity.isFirstVisit()--->>>"+BaseActivity.isFirstVisit());
		
		if(BaseActivity.isFirstVisit()) {
			
			/* FOR PROGRESS BAR ANIMATION*/
			barAnimation.setDuration(1000);
			progSpentCash.startAnimation(barAnimation);
			
			/* RUNNABLES FOR SpentCredit TEXTVIEWS ANIMATIONS*/
			SpentCreditRunnable runnableLblSpent = new SpentCreditRunnable(handler, spentTextView, spentValue);
			if (runnableLblSpent != null) {
				handler.removeCallbacks(runnableLblSpent);
			}
			handler.post(runnableLblSpent);
			
			/* RUNNABLES FOR OpenToBuyCredit TEXTVIEWS ANIMATIONS*/
			OpenToBuyCreditRunnable newTextTwoUpdateRunnable = new OpenToBuyCreditRunnable(handler, openbuyTextView, otbValue);
			if (newTextTwoUpdateRunnable != null) {
				handler.removeCallbacks(newTextTwoUpdateRunnable);
			}
			handler.post(newTextTwoUpdateRunnable);
			
			//Toggle States to NOT allow animation again
			BaseActivity.setFirstVisit(false);
			
		} else {
			
			if(StringUtils.getCurrentLocale().equalsIgnoreCase("nb_NO"))
				spentTextView.setText(spentCashTxt != null ? StringUtils.roundAndFormatCurrencyNorwayWithEndingZeros(spentCashTxt) : "?");			
			else
				spentTextView.setText(spentCashTxt != null ? StringUtils.roundAndFormatCurrency(spentCashTxt) : "?");
			
			if(StringUtils.getCurrentLocale().equalsIgnoreCase("nb_NO"))
				openbuyTextView.setText(openToBuyCashTxt != null ? StringUtils.roundAndFormatCurrencyNorwayWithEndingZeros(openToBuyCashTxt): "?");
			else
				openbuyTextView.setText(openToBuyCashTxt != null ? StringUtils.roundAndFormatCurrency(openToBuyCashTxt): "?");
			
			if (otbValue == spentValue) {
				progSpentCash.setProgress(100);
			} else {
				progSpentCash.setProgress(percentageDiff);
			}
		}
	}
	/**
	 * 
	 * @author mgatare
	 *
	 */
	private class SpentCreditRunnable implements Runnable {
		private int LIMIT;
		private int count;
		private Handler handler;
		private TextView textView;

		public SpentCreditRunnable(Handler handler, TextView textView, int limit) {
			this.handler = handler;
			this.textView = textView;
			this.LIMIT = limit;
		}

		public void run() {
			if (textView != null) {
				textView.setText(StringUtils.roundAndFormatCurrency(""+count));
				if(LIMIT <= 10000)
					count = count + 423;
				else if(LIMIT <= 1000)
					count = count + 12;
				else if(LIMIT <= 5000)
					count = count + 123;
				else if(LIMIT <= 50000)
					count = count + 2234;
				else if(LIMIT <= 100000)
					count = count + 22341;
				else if(LIMIT <= 500000)
					count = count + 22312;
				else if(LIMIT <= 1000000)
					count = count + 323321;
				else 
					count = count + 423321;

				if (handler != null && count <= LIMIT) {
					handler.postDelayed(this, 105);//will queue the runnable
				} else {
					if(StringUtils.getCurrentLocale().equalsIgnoreCase("nb_NO")) 
						textView.setText(StringUtils.roundAndFormatCurrencyNorwayWithEndingZeros(spentCashTxt));
					else
						textView.setText(StringUtils.roundAndFormatCurrency(spentCashTxt));
				}
			}
		}
	};
	/**
	 * 
	 * @author mgatare
	 *
	 */
	private class OpenToBuyCreditRunnable implements Runnable {
		private int LIMIT = 0;
		private int count = 0;
		private Handler handler;
		private TextView textView;

		public OpenToBuyCreditRunnable(Handler handler, TextView textView, int limit) {
			this.handler = handler;
			this.textView = textView;
			this.LIMIT = limit;
		}

		public void run() {
			if (textView != null) {
				
				textView.setText(StringUtils.roundAndFormatCurrency(""+ count));
				if(LIMIT <= 10000)
					count = count + 423;
				else if(LIMIT <= 1000)
					count = count + 12;
				else if(LIMIT <= 5000)
					count = count + 98;
				else if(LIMIT <= 50000)
					count = count + 2234;
				else if(LIMIT <= 100000)
					count = count + 22341;
				else if(LIMIT <= 500000)
					count = count + 22312;
				else if(LIMIT <= 1000000)
					count = count + 323321;
				else 
					count = count + 423321;
				
				if (handler != null && count <= LIMIT) {
					handler.postDelayed(this, 105);
				} else {
					if(StringUtils.getCurrentLocale().equalsIgnoreCase("nb_NO")) 
						textView.setText(StringUtils.roundAndFormatCurrencyNorwayWithEndingZeros(openToBuyCashTxt));
					else
						textView.setText(StringUtils.roundAndFormatCurrency(openToBuyCashTxt));
				}
			}
		}
	};
	/**
	 * 
	 * @author mgatare
	 *
	 */
	public class ProgressBarAnimation extends Animation {
		private ProgressBar progressBar;
		private float FROM;
		private float TO;

		public ProgressBarAnimation(ProgressBar progressBar, float from,
				float to) {
			super();
			this.progressBar = progressBar;
			this.FROM = from;
			this.TO = to;
		}

		@Override
		protected void applyTransformation(float interpolatedTime,Transformation transformation) {
			super.applyTransformation(interpolatedTime, transformation);
			float progressValue = FROM + (TO - FROM) * interpolatedTime;
			progressBar.setProgress((int) progressValue);
		}
	}
}