package com.entercard.coop;

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

public class BaseActivity extends ActionBarActivity {

	protected int drawerContentHeight;
	protected LinearLayout userguideLinearLayout;
	protected FrameLayout windowFrameLayout;
	// private AlertDialog errorDialog;
	private Dialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void longToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	public void shortToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("", "BASE ACTIVITY ONRESUME CALLED");
	}

	/**
	 * 
	 * @param title
	 * @param message
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

		progressDialog.setCancelable(true);

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

	// @SuppressWarnings("deprecation")
	// public void displayErrorMessages(String message) {
	// errorDialog = new AlertDialog.Builder(this).create();
	// View dialogView =
	// LayoutInflater.from(this).inflate(R.layout.error_dialog_layout, null);
	//
	// errorDialog.setView(dialogView);
	// TextView messageTextView = (TextView)
	// dialogView.findViewById(R.id.message);
	// messageTextView.setText(message);
	// Button OKButton = (Button) dialogView.findViewById(R.id.ok);
	// OKButton.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// try {
	// if (errorDialog != null && errorDialog.isShowing()) {
	// errorDialog.dismiss();
	// errorDialog.cancel();
	// }
	// errorDialog = null;
	// } catch (Exception e) {
	// // Ignore exception results at times when the event is
	// // received,
	// // after the screen has been closed.
	// }
	// }
	// });
	// messageTextView.setText(message);
	// errorDialog.show();
	// WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	// lp.copyFrom(errorDialog.getWindow().getAttributes());
	// lp.width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
	// lp.height = LayoutParams.WRAP_CONTENT;
	//
	// lp.dimAmount = 0.1f; // change this value for more or less dimming
	//
	// errorDialog.getWindow().setAttributes(lp);
	//
	// // add a blur/dim flags
	// errorDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND
	// | WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	//
	// }
	public void closeKeyBoard() {
		View currentFocus = getCurrentFocus();
		if (currentFocus != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					currentFocus.getApplicationWindowToken(), 0);
		}
	}

	public void makeWindowDim() {
		if (windowFrameLayout != null) {
			windowFrameLayout.getForeground().setAlpha(255 / 2);
		}
	}

	public void makeWindowNormal() {
		if (windowFrameLayout != null) {
			windowFrameLayout.getForeground().setAlpha(0);
		}
	}
}
