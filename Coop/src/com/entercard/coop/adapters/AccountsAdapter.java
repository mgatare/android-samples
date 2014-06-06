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
import com.entercard.coop.entities.AccountsModel;

public class AccountsAdapter extends ArrayAdapter<AccountsModel> {

	private Context context;
	private ArrayList<AccountsModel> dataModelList = new ArrayList<AccountsModel>();;

	public AccountsAdapter(Context context, int resource, ArrayList<AccountsModel> arrayList) {
		super(context, resource, arrayList);
		this.context = context;
		dataModelList.addAll(arrayList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.rc_row_two_textview_vertical, parent,false);

			holder = new ViewHolder();

			holder.llMainOne = (LinearLayout) convertView.findViewById(R.id.parentlinearlayout);

			holder.cardNameTextView = (TextView) holder.llMainOne.findViewById(R.id.titleTextView);
			holder.cardNumberTextView = (TextView) holder.llMainOne.findViewById(R.id.detailTextView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.cardNameTextView.setText(getItem(position).getProduct());
		holder.cardNumberTextView.setText("Card number ending with:"+getItem(position).getCardDataArrayList().get(0).getCardNumberEndingWith());

		return convertView;
	}

	class ViewHolder {
		private LinearLayout llMainOne;
		private TextView cardNameTextView;
		private TextView cardNumberTextView;
	}
}
