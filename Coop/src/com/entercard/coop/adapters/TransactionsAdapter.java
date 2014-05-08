package com.entercard.coop.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entercard.coop.R;

import dcom.entercard.coop.model.DataModel;

public class TransactionsAdapter extends ArrayAdapter<DataModel> {

	private Context context;
	private ArrayList<DataModel> dataModelList = new ArrayList<DataModel>();;

	public TransactionsAdapter(Context context, int resource, ArrayList<DataModel> arrayList) {
		super(context, resource, arrayList);
		this.context = context;
		dataModelList.addAll(arrayList);
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
		holder.priceTextView.setText(getItem(position).getPrice());
		holder.nameTextView.setText(getItem(position).getName());

		return convertView;
	}

	class ViewHolder {
		private LinearLayout llMainOne;
		private TextView dateTextView;
		private TextView priceTextView;
		private TextView nameTextView;
	}
}
