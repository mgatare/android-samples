package com.entercard.coopmedlem;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.entercard.coopmedlem.entities.SingletonWebservicesDataModel;

// TODO: Auto-generated Javadoc
/**
 * The Class BaseActivity.
 */
public class BaseActivity extends ActionBarActivity {

	/** The drawer content height. */
	protected int drawerContentHeight;

	/** The userguide linear layout. */
	protected LinearLayout userguideLinearLayout;

	/** The window frame layout. */
	protected FrameLayout windowFrameLayout;

	/** The progress dialog. */
	private Dialog progressDialog;

	/** The receiver. */
	private BroadcastReceiver receiver;

	/** The account position. */
	private static int accountPosition;

	/** The account trnsactions. */
	private static int transactionsCount;

	/** The open to buy. */
	private static String openToBuy;

	/** The spent. */
	private static String spent;
	
	/** The is first visit. */
	private static boolean isFirstVisit;

	/** The singleton user data model array list. */
	private static ArrayList<SingletonWebservicesDataModel> singletonUserDataModelArrayList;
	
	/** The Constant NO_STATE. */
	public static final  int NO_STATE = 110;
	
	/** The Constant DISPUTE. */
	public static final int DISPUTE = 111; 
	
	/** The Constant TRANSFER_FUNDS. */
	public static final int TRANSFER_FUNDS = 112; 
	
	/** The Constant CLI. */
	public static final int CLI = 113;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		
		//For some devices this may be needed
		//getWindow().setFlags(LayoutParams.FLAG_SECURE, LayoutParams.FLAG_SECURE);
		regUnlockReceiver();
	}

	/**
	 * Long toast.
	 * 
	 * @param message
	 *            the message
	 */
	public void longToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * Short toast.
	 * 
	 * @param message
	 *            the message
	 */
	public void shortToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Show developer log.
	 * 
	 * @param msg
	 *            the msg
	 */
	public void showDeveloperLog(String msg) {
		Log.d("Coop", msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Log.v("", ">>>BASE ACTIVITY ONRESUME CALLED>>>>");
		//onPhoneCallReceived();
	}
	@Override
	protected void onPause() {
		Log.v("", "::::::::::BASE ACTIVITY onPause CALLED:::::::::::");
		super.onPause();
	}

	/**
	 * Show progress dialog.
	 */
	public void showProgressDialog() {
		
		progressDialog = new Dialog(BaseActivity.this);
		View dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
		progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		progressDialog.setContentView(dialogView);

		progressDialog.setCancelable(false);

		ImageView progressSpinner = (ImageView) dialogView.findViewById(R.id.progSpent);

		RotateAnimation anim = new RotateAnimation(0f, 350f,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF,
				0.5f);
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(500);
		anim.setInterpolator(new LinearInterpolator());
		progressSpinner.startAnimation(anim);

		progressDialog.show();
		progressDialog.getWindow().setLayout(120, 120);
	}

	/**
	 * Hide progress dialog.
	 */
	public void hideProgressDialog() {
		try {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog.cancel();
			}
			progressDialog = null;
		} catch (Exception e) {
			Log.e("COOP", "" + e.getMessage());
		}
	}

	/**
	 * Close key board.
	 */
	public void closeKeyBoard() {
		View currentFocus = getCurrentFocus();
		if (currentFocus != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					currentFocus.getApplicationWindowToken(), 0);
		}
	}

	/**
	 * Show key board.
	 *
	 * @param view the view
	 */
	public void showKeyBoard(View view) {
		view.requestFocus();
		InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		keyboard.showSoftInput(view, 0);
	}

	/**
	 * Make window dim.
	 */
	public void makeWindowDim() {
		if (windowFrameLayout != null) {
			windowFrameLayout.getForeground().setAlpha(255 / 2);
		}
	}

	/**
	 * Make window normal.
	 */
	public void makeWindowNormal() {
		if (windowFrameLayout != null) {
			windowFrameLayout.getForeground().setAlpha(0);
		}
	}

	/**
	 * Gets the account position.
	 * 
	 * @return the account position
	 */
	public int getAccountPosition() {
		return accountPosition;
	}

	/**
	 * Sets the account position.
	 * 
	 * @param accountPosition
	 *            the new account position
	 */
	public void setAccountPosition(int accountPosition) {
		BaseActivity.accountPosition = accountPosition;
	}

	/**
	 * Gets the transactions count.
	 * 
	 * @return the transactions count
	 */
	public int getTransactionsCount() {
		return transactionsCount;
	}

	/**
	 * Sets the transactions count.
	 * 
	 * @param transactionsCount
	 *            the new transactions count
	 */
	public void setTransactionsCount(int transactionsCount) {
		BaseActivity.transactionsCount = transactionsCount;
	}

	/**
	 * Gets the open to buy.
	 * 
	 * @return the open to buy
	 */
	public String getOpenToBuy() {
		return openToBuy;
	}

	/**
	 * Sets the open to buy.
	 * 
	 * @param openToBuy
	 *            the new open to buy
	 */
	public void setOpenToBuy(String openToBuy) {
		BaseActivity.openToBuy = openToBuy;
	}

	/**
	 * Gets the spent.
	 * 
	 * @return the spent
	 */
	public String getSpent() {
		return spent;
	}

	/**
	 * Sets the spent.
	 * 
	 * @param spent
	 *            the new spent
	 */
	public void setSpent(String spent) {
		BaseActivity.spent = spent;
	}

	/**
	 * Checks if is first visit.
	 *
	 * @return true, if is first visit
	 */
	public static boolean isFirstVisit() {
		return isFirstVisit;
	}

	/**
	 * Sets the first visit.
	 *
	 * @param isFirstVisit the new first visit
	 */
	public static void setFirstVisit(boolean isFirstVisit) {
		BaseActivity.isFirstVisit = isFirstVisit;
	}
	
	/**
	 * Reg unlock receiver.
	 */
	private void regUnlockReceiver() {
		IntentFilter filter = new IntentFilter(Intent.ACTION_USER_PRESENT);
		receiver = new OnScreenUnlockReceiver();
		registerReceiver(receiver, filter);
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// * android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os
	// * .Bundle)
	// */
	// @Override
	// public void onSaveInstanceState(Bundle outState) {
	// Log.e("COOP", ">>BASE In Method: onSaveInstanceState()");
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	// */
	// @Override
	// public void onRestoreInstanceState(Bundle inState) {
	// Log.e("COOP", ">>BASE In Method: onRestoreInstanceState()");
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("COOP", ">>BASE In Method: onDestroy()");
		if (null != receiver) {
			unregisterReceiver(receiver);
			receiver = null;
		}
	}

	
	/**
	 * Gets the singleton user data model array list.
	 *
	 * @return the singleton user data model array list
	 */
	public static ArrayList<SingletonWebservicesDataModel> getSingletonUserDataModelArrayList() {
		return singletonUserDataModelArrayList;
	}

	/**
	 * Sets the singleton user data model list.
	 *
	 * @param singletonUserDataModel the new singleton user data model list
	 */
	public static void setSingletonUserDataModelList(ArrayList<SingletonWebservicesDataModel> singletonUserDataModel) {
		BaseActivity.singletonUserDataModelArrayList = singletonUserDataModel;
	}
}
