package com.entercard.coopmedlem.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.entities.TransactionDataModel;
import com.entercard.coopmedlem.utils.StringUtils;

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

	/**
	 * Instantiates a new transactions adapter.
	 *
	 * @param context the context
	 * @param resource the resource
	 * @param transactionsArrayList the transactions array list
	 */
	public TransactionsAdapter(Context context, int resource,
			ArrayList<TransactionDataModel> transactionsArrayList) {
		super(context, resource, transactionsArrayList);
		this.context = context;
		dataModelList.addAll(transactionsArrayList);
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return dataModelList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getItem(int)
	 */
	@Override
	public TransactionDataModel getItem(int position) {
		return dataModelList.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getPosition(java.lang.Object)
	 */
	@Override
	public int getPosition(TransactionDataModel item) {
		return super.getPosition(item);
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.row_transactions,parent, false);
		}

		TextView dateTextView = (TextView) convertView.findViewById(R.id.lblDate);
		TextView priceTextView = (TextView) convertView.findViewById(R.id.lblPrice);
		TextView nameTextView = (TextView) convertView.findViewById(R.id.lblName);
		ImageView imgView = (ImageView) convertView.findViewById(R.id.imgMap);

		dateTextView.setText(getItem(position).getDate());
		priceTextView.setText(StringUtils.formatCurrencyLocally(getItem(position).getBillingAmount()));
		nameTextView.setText(getItem(position).getDescription());

		// nameTextView.setTag(position);
		if (currentExpandedPosition == position) {
			imgView.setVisibility(View.VISIBLE);
			currentExpandedView = imgView;
		} else {
			imgView.setVisibility(View.GONE);
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

					final ImageView imgView = (ImageView) v.findViewById(R.id.imgMap);
					if (imgView.getVisibility() == View.VISIBLE) {
						collapse(imgView);
						currentExpandedPosition = -1;
						currentExpandedView = null;
					} else {
						if (currentExpandedView != null) {
							collapse(currentExpandedView);
							currentExpandedPosition = -1;
							currentExpandedView = null;
							expand(imgView);
						} else {
							expand(imgView);
						}
						currentExpandedView = imgView;
						currentExpandedPosition = pos;
					}
				} else {
					//TODO Nothing for now, don't collapse the other
					Log.d("COOP", "IsDisputable()::::" + getItem(pos).getIsDisputable());
				}
			}
		});

		return convertView;
	}

	/**
	 * Expand.
	 *
	 * @param v the v
	 */
	public static void expand(final View v) {
		v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final int targtetHeight = v.getMeasuredHeight();

		v.getLayoutParams().height = 0;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT: (int) (targtetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp/ms
		a.setDuration((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
		v.startAnimation(a);
	}

	/**
	 * Collapse.
	 *
	 * @param v the v
	 */
	public static void collapse(final View v) {
		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				} else {
					v.getLayoutParams().height = initialHeight- (int) (initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp/ms
		a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
		v.startAnimation(a);

	}
}
