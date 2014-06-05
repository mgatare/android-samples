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
import com.entercard.coop.model.DataModel;

public class CardsAdapter extends ArrayAdapter<DataModel> {

	private Context context;
	private ArrayList<DataModel> dataModelList = new ArrayList<DataModel>();;

	public CardsAdapter(Context context, int resource, ArrayList<DataModel> arrayList) {
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

			holder.llMainOne = (LinearLayout) convertView.findViewById(R.id.llMain);

			holder.cardNametextView = (TextView) holder.llMainOne.findViewById(R.id.cardNametextView);
			holder.cardNumberTextView = (TextView) holder.llMainOne.findViewById(R.id.cardNumberTextView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.cardNametextView.setText(getItem(position).getName());
		holder.cardNumberTextView.setText("Card number: "+getItem(position).getId());

		return convertView;
	}

	class ViewHolder {
		private LinearLayout llMainOne;
		private TextView cardNametextView;
		private TextView cardNumberTextView;
	}
}
