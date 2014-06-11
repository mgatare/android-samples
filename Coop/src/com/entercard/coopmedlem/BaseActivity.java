package com.entercard.coopmedlem;

import android.app.Dialog;
import android.content.Context;
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
	// private AlertDialog errorDialog;
	/** The progress dialog. */
	private Dialog progressDialog;

	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * Long toast.
	 *
	 * @param message the message
	 */
	public void longToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * Short toast.
	 *
	 * @param message the message
	 */
	public void shortToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Show developer log.
	 *
	 * @param tag the tag
	 * @param msg the msg
	 */
	void showDeveloperLog(String tag, String msg) {
		Log.d(tag, msg);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Log.i("", "BASE ACTIVITY ONRESUME CALLED");
	}

	/**
	 * Show progress dialog.
	 */
	public void showProgressDialog() {

		progressDialog = new Dialog(BaseActivity.this);
		View dialogView = LayoutInflater.from(this).inflate(
				R.layout.progress_dialog, null);
		progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		progressDialog.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

		progressDialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		progressDialog.setContentView(dialogView);

		progressDialog.setCancelable(false);

		ImageView progressSpinner = (ImageView) dialogView
				.findViewById(R.id.progress);

		RotateAnimation anim = new RotateAnimation(0f, 350f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(500);
		anim.setInterpolator(new LinearInterpolator());
		progressSpinner.startAnimation(anim);

		progressDialog.show();
		progressDialog.getWindow().setLayout(100, 100);
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
}
