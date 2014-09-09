package com.no.entercard.coopmedlem.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.no.entercard.coopmedlem.R;

public class EmploymentDetailsAdapter extends ArrayAdapter<String> {

	private Context context;
	private ArrayList<String> dataModelList = new ArrayList<String>();
	private List<Integer> listIndex = new ArrayList<Integer>();
	int index = -1;

	public EmploymentDetailsAdapter(Context context, int resource,
			ArrayList<String> arrayList) {
		super(context, resource, arrayList);
		this.context = context;
		dataModelList.addAll(arrayList);
	}

	public void setPosition(int position) {
		index = position;
		listIndex.add(index);
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.row_employment,parent, false);

			holder = new ViewHolder();

			holder.llMain = (LinearLayout) convertView.findViewById(R.id.llMain);
			holder.lblEmploymentName = (TextView) holder.llMain.findViewById(R.id.lblEmploymentName);
			holder.imgTick = (ImageView) holder.llMain.findViewById(R.id.imgTick);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.lblEmploymentName.setText(getItem(position).toString());
		
		if(index == position) {
			holder.imgTick.setVisibility(View.VISIBLE);
		} else {
			holder.imgTick.setVisibility(View.GONE);
		}
		/*if (position % 2 == 0) {
			convertView.setBackgroundColor(context.getResources().getColor(R.color.list_odd));
		} else {
			convertView.setBackgroundColor(context.getResources().getColor(R.color.list_even));
		}*/
		return convertView;
	}

	class ViewHolder {
		private LinearLayout llMain;
		private TextView lblEmploymentName;
		private ImageView imgTick;
	}
}
