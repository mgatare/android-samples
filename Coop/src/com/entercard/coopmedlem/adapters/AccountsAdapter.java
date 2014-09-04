package com.entercard.coopmedlem.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entercard.coopmedlem.ApplicationEx;
import com.entercard.coopmedlem.R;
import com.entercard.coopmedlem.entities.AccountsModel;

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
			convertView = layoutInflater.inflate(R.layout.row_accounts, parent,false);

			holder = new ViewHolder();

			holder.llMainOne = (LinearLayout) convertView.findViewById(R.id.parentlinearlayout);

			holder.cardNameTextView = (TextView) holder.llMainOne.findViewById(R.id.lblCardName);
			holder.cardNameTextView.setTextColor(context.getResources().getColor(R.color.text_heading));
			
			holder.cardNumberTextView = (TextView) holder.llMainOne.findViewById(R.id.lblCardNumber);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String product = getItem(position).getProduct();
				//+ " "+ getItem(position).getProductName();
		
		holder.cardNameTextView.setText(product);
		holder.cardNumberTextView.setText(ApplicationEx.getInstance()
				.getResources().getString(R.string.card_ending_with)
				+ " "
				+ getItem(position).getCardDataArrayList().get(0).getCardNumberEndingWith());

		if (position % 2 == 0) {
			convertView.setBackgroundResource(R.drawable.list_bg_even);
		} else {
			convertView.setBackgroundResource(R.drawable.list_bg_odd);
		}
		return convertView;
	}

	static class ViewHolder {
		private LinearLayout llMainOne;
		private TextView cardNameTextView;
		private TextView cardNumberTextView;
	}
}
