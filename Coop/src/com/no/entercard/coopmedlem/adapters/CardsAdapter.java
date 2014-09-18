package com.no.entercard.coopmedlem.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.no.entercard.coopmedlem.R;
import com.no.entercard.coopmedlem.entities.CardDataModel;


public class CardsAdapter extends ArrayAdapter<CardDataModel> {

	private Context context;
	private ArrayList<CardDataModel> dataModelList = new ArrayList<CardDataModel>();;

	public CardsAdapter(Context context, int resource, ArrayList<CardDataModel> arrayList) {
		super(context, resource, arrayList);
		this.context = context;
		dataModelList.addAll(arrayList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.row_cards_fragment, parent,false);

			holder = new ViewHolder();

			holder.llMainOne = (LinearLayout) convertView.findViewById(R.id.parentlinearlayout);

			holder.cardNametextView = (TextView) holder.llMainOne.findViewById(R.id.titleTextView);
			holder.cardNumberTextView = (TextView) holder.llMainOne.findViewById(R.id.lblDetail);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.cardNametextView.setText(getItem(position).getCardholderName());
		holder.cardNametextView.setTextColor(context.getResources().getColor(R.color.text_body));
		holder.cardNumberTextView.setText(context.getResources().getString(R.string.card_ending_with)+" "+getItem(position).getCardNumberEndingWith());
		if (position % 2 == 0) {
			convertView.setBackgroundResource(R.drawable.list_bg_odd);
		} else {
			convertView.setBackgroundResource(R.drawable.list_bg_even);
		}
		return convertView;
	}

	class ViewHolder {
		private LinearLayout llMainOne;
		private TextView cardNametextView;
		private TextView cardNumberTextView;
	}
}
