package com.entercard.coopmedlem.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.entities.TransactionDataModel;
import com.entercard.coopmedlem.utils.StringUtils;

public class TransactionsAdapter extends ArrayAdapter<TransactionDataModel> {

	private Context context;
	private ArrayList<TransactionDataModel> dataModelList = new ArrayList<TransactionDataModel>();;

	public TransactionsAdapter(Context context, int resource, ArrayList<TransactionDataModel> transactionsArrayList) {
		super(context, resource, transactionsArrayList);
		this.context = context;
		dataModelList.addAll(transactionsArrayList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.row_transactions, parent,false);

			holder = new ViewHolder();

			holder.llMainOne = (LinearLayout) convertView.findViewById(R.id.llMainRow);

			holder.dateTextView = (TextView) holder.llMainOne.findViewById(R.id.dateTextView);
			holder.priceTextView = (TextView) holder.llMainOne.findViewById(R.id.priceTextView);
			holder.nameTextView = (TextView) holder.llMainOne.findViewById(R.id.nameTextView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.dateTextView.setText(getItem(position).getDate());
		holder.priceTextView.setText(StringUtils.formatCurrency(getItem(position).getBillingAmount()));
		holder.nameTextView.setText(getItem(position).getDescription());

		if (position % 2 == 0){
			convertView.setBackgroundColor(context.getResources().getColor(R.color.list_odd));
		} else {
			convertView.setBackgroundColor(context.getResources().getColor(R.color.list_even));
		}
		
		return convertView;
	}

	class ViewHolder {
		private LinearLayout llMainOne;
		private TextView dateTextView;
		private TextView priceTextView;
		private TextView nameTextView;
	}
}
