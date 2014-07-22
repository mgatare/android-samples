package com.entercard.coopmedlem.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entercard.coopmedlem.DisputeTransactionActivity;
import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.entities.TransactionDataModel;
import com.entercard.coopmedlem.utils.DateUtils;
import com.entercard.coopmedlem.utils.ImageLoader;
import com.entercard.coopmedlem.utils.StringUtils;
import com.entercard.coopmedlem.utils.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class TransactionsAdapter.
 */
public class TransactionsAdapter extends ArrayAdapter<TransactionDataModel> {

	/** The context. */
	private Context context;
	
	/** The data model list. */
	private ArrayList<TransactionDataModel> dataModelList = new ArrayList<TransactionDataModel>();

	/** The current expanded view. */
	private View currentExpandedView = null;
	
	/** The current expanded position. */
	private int currentExpandedPosition = -1;
	
	/** The image loader. */
	private ImageLoader imageLoader;

	/**
	 * Instantiates a new transactions adapter.
	 *
	 * @param context the context
	 * @param resource the resource
	 * @param transactionsArrayList the transactions array list
	 */
	public TransactionsAdapter(Context context, int resource, ArrayList<TransactionDataModel> transactionsArrayList) {
		super(context, resource, transactionsArrayList);
		this.context = context;
		
		imageLoader = new ImageLoader(context);
		dataModelList.addAll(transactionsArrayList);
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.row_transactions,parent, false);
		}

		TextView lblDate = (TextView) convertView.findViewById(R.id.lblDate);
		TextView lblPrice = (TextView) convertView.findViewById(R.id.lblPrice);
		TextView lblName = (TextView) convertView.findViewById(R.id.lblName);
		TextView lblNameBox = (TextView) convertView.findViewById(R.id.lblNameBox);
		Button btnDisputeOnMap = (Button) convertView.findViewById(R.id.btnDisputeOnMap);
		//ImageView imgMarker = (ImageView) convertView.findViewById(R.id.imgMarker);
		
		RelativeLayout relMapLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayoutMap);

		String date = DateUtils.getTransactionDate(getItem(position).getDate());
		lblDate.setText(date.substring(0, 6));
		
		lblPrice.setText(StringUtils.formatCurrencyLocally(getItem(position).getBillingAmount()));
		if(getItem(position).getType().equalsIgnoreCase("Credit")){
			lblName.setVisibility(View.GONE);
			lblNameBox.setVisibility(View.VISIBLE);
			lblNameBox.setText(getItem(position).getDescription());
		} else {
			lblName.setVisibility(View.VISIBLE);
			lblNameBox.setVisibility(View.GONE);
			lblName.setText(getItem(position).getDescription());
		}	
			
		// IS THIS USEFUL??
		if (currentExpandedPosition == position) {
			relMapLayout.setVisibility(View.VISIBLE);
			currentExpandedView = relMapLayout;
			lblDate.setText(date);
		} else {
			lblDate.setText(date.substring(0, 6));
			relMapLayout.setVisibility(View.GONE);
		}

		if (position % 2 == 0) {
			convertView.setBackgroundColor(context.getResources().getColor(R.color.list_odd));
		} else {
			convertView.setBackgroundColor(context.getResources().getColor(R.color.list_even));
		}

		convertView.setTag(position);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int pos = (Integer) v.getTag();
				
				if (getItem(pos).getIsDisputable()) {
					
					RelativeLayout relMapLayout = (RelativeLayout) v.findViewById(R.id.relativeLayoutMap);
					TextView lblDate = (TextView) v.findViewById(R.id.lblDate);
					ImageView imgView = (ImageView) v.findViewById(R.id.imgMap);
					ImageView imgMarker = (ImageView) v.findViewById(R.id.imgMarker);
					Button btnDisputeOnMap = (Button) v.findViewById(R.id.btnDisputeOnMap);
					
					String date = DateUtils.getTransactionDate(getItem(pos).getDate());
					String city = StringUtils.trimStringAndReplaceNumbers(getItem(pos).getCity());
					String country = StringUtils.trimStringAndReplaceNumbers(getItem(pos).getCountry());
					
					String strURL = Utils.getMapThumbnailFromCityOrCountry(city, country);
					Log.i("COOP","SWED URL>>>>>"+strURL);
					
					if(!TextUtils.isEmpty(strURL))
						imageLoader.DisplayImage(strURL, imgView);
					
					if (relMapLayout.getVisibility() == View.VISIBLE) {
						
						collapse(relMapLayout);
						currentExpandedPosition = -1;
						currentExpandedView = null;
						imgMarker.setVisibility(View.VISIBLE);
						btnDisputeOnMap.setVisibility(View.VISIBLE);
						lblDate.setText(date.substring(0, 6));
						
					} else {
						if (currentExpandedView != null) {
							collapse(currentExpandedView);
							currentExpandedPosition = -1;
							currentExpandedView = null;
							expand(relMapLayout);
							imgMarker.setVisibility(View.VISIBLE);
							btnDisputeOnMap.setVisibility(View.VISIBLE);
							lblDate.setText(date);
						} else {
							expand(relMapLayout);
							imgMarker.setVisibility(View.VISIBLE);
							btnDisputeOnMap.setVisibility(View.VISIBLE);
							lblDate.setText(date);
						}
						currentExpandedView = relMapLayout;
						currentExpandedPosition = pos;
					}
				} else {
					//TODO Nothing for now, don't collapse the other
					Log.d("COOP", "IsDisputable()::::" + getItem(pos).getIsDisputable());
				}
			}
		});
		
		btnDisputeOnMap.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Log.e("COOP", ":::::AMOUNT:::::"+getItem(position).getBillingAmount());
				//Log.e("COOP", ":::::DESCRIPTION:::::"+getItem(position).getDescription());
				//Log.e("COOP", ":::::DATE:::::"+getItem(position).getDate());
				Intent intent = new Intent(context, DisputeTransactionActivity.class);
				intent.putExtra("amount", getItem(position).getBillingAmount());
				intent.putExtra("desc", getItem(position).getDescription());
				intent.putExtra("date", getItem(position).getDate());
				intent.putExtra("id", getItem(position).getId());
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}

	/**
	 * Expand.
	 *
	 * @param view the v
	 */
	public static void expand(final View view) {
		view.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final int targtetHeight = view.getMeasuredHeight();

		view.getLayoutParams().height = 0;
		view.setVisibility(View.VISIBLE);
		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				view.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
						: (int) (targtetHeight * interpolatedTime);
				view.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		animation.setDuration((int) (targtetHeight / view.getContext().getResources()
				.getDisplayMetrics().density));
		view.startAnimation(animation);
	}

	/**
	 * Collapse.
	 *
	 * @param view the v
	 */
	public static void collapse(final View view) {
		final int initialHeight = view.getMeasuredHeight();

		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if (interpolatedTime == 1) {
					view.setVisibility(View.GONE);
				} else {
					view.getLayoutParams().height = initialHeight
							- (int) (initialHeight * interpolatedTime);
					view.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		animation.setDuration((int) (initialHeight / view.getContext().getResources()
				.getDisplayMetrics().density));
		view.startAnimation(animation);

	}
}
