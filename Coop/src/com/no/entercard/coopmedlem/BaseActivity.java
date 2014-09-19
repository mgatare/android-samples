package com.no.entercard.coopmedlem;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
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
import android.widget.Toast;

import com.no.entercard.coopmedlem.entities.SingletonWebservicesDataModel;
import com.no.entercard.coopmedlem.utils.CompatibilityUtils;
// TODO: Auto-generated Javadoc
/**
 * The Class BaseActivity.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
public abstract class BaseActivity extends ActionBarActivity {

	/** The Constant TAG. */
	protected static final String TAG = "COOP BASEACTIVITY";
	
	/** The is app to background. */
	public static boolean isAppToBackground = false;
	
	/** The is window focused. */
	public static boolean isWindowFocused = false;
	
	/** The is menu opened. */
	public static boolean isMenuOpened = false;
	
	/** The is back pressed. */
	public static boolean isBackPressed = false;
	
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
	public static final  int TYPE_NONE = 110;
	
	/** The Constant DISPUTE. */
	public static final int TYPE_DISPUTE = 111; 
	
	/** The Constant TRANSFER_FUNDS. */
	public static final int TYPE_TRANSFER_FUNDS = 112; 
	
	/** The Constant CLI. */
	public static final int TYPE_CLI = 113;
	
	private boolean isAppBackground = false;
	
	public static final long DISCONNECT_TIMEOUT = 60000;// 1 min = 1 * 60 * 1000 ms
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		
		//For some devices this may be needed
		//getWindow().setFlags(LayoutParams.FLAG_SECURE, LayoutParams.FLAG_SECURE);
		regUnlockReceiver();
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.icon_coop);
		
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
	

    private static Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
        	//Log.e("", "................disconnectedddddddddddddddddddddddddddddd........."+isAppBackground);
        	isAppBackground = true;
        	
        }
    };

    public void resetDisconnectTimer(){
    	//Log.d("", "......RESET.....");
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction(){
    	//Log.e("", "..onUserInteraction called...");
    	isAppBackground = false;
        resetDisconnectTimer();
    }
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		resetDisconnectTimer();
		//Log.v("", ">>>BASE ACTIVITY ONRESUME CALLED>>>>"+isAppBackground);
		
		if(isAppBackground) {
			if(CompatibilityUtils.getSdkVersion() < 11) {
				Intent intent = new Intent(getApplicationContext(), EnterPINCodeActivity.class);
				intent.putExtra(getResources().getString(R.string.pref_verify_pin), BaseActivity.TYPE_NONE);
				ComponentName cn = intent.getComponent();
				Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
				startActivity(mainIntent);
			} else {
				Intent intent = new Intent(this, EnterPINCodeActivity.class);
				intent.putExtra(getResources().getString(R.string.pref_verify_pin), BaseActivity.TYPE_NONE);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		//Log.d("", "::::::BASE ACTIVITY onPause CALLED:::::::");
		//isAppBackground = true;
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		//Log.d("COOP", ">>BASE In Method: onDestroy()");
		if (null != receiver) {
			unregisterReceiver(receiver);
			receiver = null;
		}
		stopDisconnectTimer();
	}
    @Override
    public void onStop() {
        super.onStop();
        //Log.d("COOP", ">>BASE In Method: onStop()");
        //stopDisconnectTimer();
    }
	
	/**
	 * Show progress dialog.
	 */
	public void showProgressDialog() {
		
		progressDialog = new Dialog(this, R.style.DialogSlideAnim);
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
		//progressDialog.getWindow().setLayout(120, 120);
		
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
			//Log.e("COOP", "" + e.getMessage());
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
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  // refresh your views here
	  super.onConfigurationChanged(newConfig);
	}
	
//	public void updateLocalTimeoutStamp () {
//		PreferenceHelper preferenceHelper = new PreferenceHelper(this);
//		preferenceHelper.addString(getResources().getString(R.string.pref_device_session), DateUtils.getCurrentTimeStamp());
//		Log.i("", ">>>>>LOCAL DATE TIME SESSION UPPPDATEEEEED>>>>>>>>>>>");
//	}
}
