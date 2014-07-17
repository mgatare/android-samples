package com.entercard.coopmedlem.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entercard.coopmedlem.BaseActivity;
import com.entercard.coopmedlem.EnterPINCodeActivity;
import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.R.style;

// TODO: Auto-generated Javadoc
/**
 * The Class TermsAndConditionsDialogFragment.
 */
public class AcceptTermsAndConditionDialogFragment extends DialogFragment {

	/** The parent activity. */
	//private DisputeTransactionActivity parentActivity;
	
	/** The result code. */
	private int RESULT_CODE_DISPUTE = 101;
	private int RESULT_CODE_CLI = 102;
	private static int type;
    /**
     * New instance.
     *
     * @param title the title
     * @return the terms and conditions dialog fragment
     */
    public static AcceptTermsAndConditionDialogFragment newInstance(int title) {
        AcceptTermsAndConditionDialogFragment frag = new AcceptTermsAndConditionDialogFragment();
        Bundle args = new Bundle();
        args.putInt("type", title);
        frag.setArguments(args);
        return frag;
    }
    
    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    /* (non-Javadoc)
     * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
     */
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
    	type = getArguments().getInt("type");
    	
		setStyle(0, style.Theme_Coopmedlem);
		//parentActivity = (DisputeTransactionActivity) getActivity();
		
		LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		View innerView = layoutInflater.inflate(R.layout.rc_row_single_textview, null);
		TextView lblSingleTitle = (TextView) innerView.findViewById(R.id.lblSingleTitle);
		
		if(type == 0) 
			lblSingleTitle.setText(R.string.accept_terms_conditions_dispute);
		else
			lblSingleTitle.setText(R.string.accept_terms_condition_cli);
		
		return new AlertDialog.Builder(getActivity())
				.setTitle(getResources().getString(R.string.accept_terms_and_condition))
				.setPositiveButton(getResources().getString(R.string.i_accept),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								
								Intent intent = new Intent(getActivity(), EnterPINCodeActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								if(type == 0) {
									intent.putExtra(getResources().getString(R.string.pref_verify_pin), BaseActivity.DISPUTE);
									getActivity().startActivityForResult(intent, RESULT_CODE_DISPUTE);
								} else {
									intent.putExtra(getResources().getString(R.string.pref_verify_pin), BaseActivity.CLI);
									getActivity().startActivityForResult(intent, RESULT_CODE_CLI);
								}
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
}