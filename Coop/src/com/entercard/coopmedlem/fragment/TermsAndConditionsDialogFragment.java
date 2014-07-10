package com.entercard.coopmedlem.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.entercard.coopmedlem.BaseActivity;
import com.entercard.coopmedlem.DisputeTransactionActivity;
import com.entercard.coopmedlem.EnterPINCodeActivity;
import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.R.style;

/**
 * 
 * @author mgatare
 *
 */
public class TermsAndConditionsDialogFragment extends DialogFragment {

	private DisputeTransactionActivity parentActivity;
	private int RESULT_CODE = 101;
	
    public static TermsAndConditionsDialogFragment newInstance(int title) {
        TermsAndConditionsDialogFragment frag = new TermsAndConditionsDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
		setStyle(0, style.Theme_Coopmedlem);
		parentActivity = (DisputeTransactionActivity) getActivity();
		
		LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		View innerView = layoutInflater.inflate(R.layout.rc_row_single_textview, null);
		TextView lblSingleTitle = (TextView) innerView.findViewById(R.id.lblSingleTitle);
		
		lblSingleTitle.setText(R.string.accept_terms_conditions_text);
		
		return new AlertDialog.Builder(getActivity())
				.setTitle(getResources().getString(R.string.accept_terms_and_condition))
				.setPositiveButton(getResources().getString(R.string.i_accept),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/***/
								
								Log.d("COOP", ">>>DISPUTE AMOUNT>>>"+BaseActivity.getSingletonUserDataModelArrayList().get(0).getDisputebillingAmount());
								
								Intent intent = new Intent(parentActivity, EnterPINCodeActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra(getResources().getString(R.string.pref_verify_pin), BaseActivity.DISPUTE);
								parentActivity.startActivityForResult(intent, RESULT_CODE);
							}
						})
				.setNegativeButton(getResources().getString(android.R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dismiss();
							}
						})
						.setView(innerView)
						.create();
	}
    
//    @Override
//    public void onResume() {
//    	super.onResume();
//    	Log.i("", "onResume() calledddddddddddddddddddddddddddddddddddd");
//    	actCodeEditText.requestFocus();
//		actCodeEditText.setRawInputType(Configuration.KEYBOARD_12KEY);
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				// Get focus and show the Keyboard
//				InputMethodManager inputMgr = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//				inputMgr.toggleSoftInput(0, 0);
//			}
//		}, 200);
//    }
}