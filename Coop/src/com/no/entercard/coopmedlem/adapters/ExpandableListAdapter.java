package com.no.entercard.coopmedlem.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.no.entercard.coopmedlem.DisputeTransactionActivity;
import com.no.entercard.coopmedlem.R;
import com.no.entercard.coopmedlem.entities.TransactionDataModel;
import com.no.entercard.coopmedlem.utils.DateUtils;
import com.no.entercard.coopmedlem.utils.ImageLoader;
import com.no.entercard.coopmedlem.utils.StringUtils;
import com.no.entercard.coopmedlem.utils.Utils;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<TransactionDataModel> listTrsansactionHeader = new ArrayList<TransactionDataModel>();
	private ImageLoader imageLoader;
	
	public ExpandableListAdapter(Context context, ArrayList<TransactionDataModel> listDataHeader) {
		this.context = context;
		this.listTrsansactionHeader.addAll(listDataHeader);
		
		imageLoader = new ImageLoader(context);
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return listTrsansactionHeader.get(groupPosition);
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		//return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
		return 1;
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		final TransactionDataModel dataModel = (TransactionDataModel) getChild(groupPosition, childPosition);
		
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.row_transaction_child, null);
		}
		ImageView imgMap = (ImageView) convertView.findViewById(R.id.imgMap);
		ImageView imgMarker = (ImageView) convertView.findViewById(R.id.imgMarker);
		Button btnDisputeOnMap  = (Button) convertView.findViewById(R.id.btnDisputeOnMap);
		
		String city = "";
		String country = "";
		
		if(null!=dataModel.getCity())
			city = StringUtils.trimStringAndDigits(dataModel.getCity());
		if(null!=dataModel.getCountry())
			country = StringUtils.trimStringAndDigits(dataModel.getCountry());
		
		if(null!=country || null!=city)
			imgMarker.setVisibility(View.VISIBLE);
		else
			imgMarker.setVisibility(View.GONE);
		//Make HTTP call
		String strURL = Utils.getMapThumbnailFromCityOrCountry(city, country);
		Log.i("COOP","URL>>>>>"+strURL);
		
		if(!TextUtils.isEmpty(strURL))
			imageLoader.DisplayImage(strURL, imgMap);
		
		btnDisputeOnMap.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, DisputeTransactionActivity.class);
				intent.putExtra("amount", dataModel.getBillingAmount());
				intent.putExtra("desc", dataModel.getDescription());
				intent.putExtra("date", dataModel.getDate());
				intent.putExtra("id", dataModel.getId());
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return listTrsansactionHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return listTrsansactionHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.row_transactions, null);
		}
		
		final TransactionDataModel dataModel = (TransactionDataModel) getGroup(groupPosition);
		
		TextView lblDate = (TextView) convertView.findViewById(R.id.lblDate);
		TextView lblPrice = (TextView) convertView.findViewById(R.id.lblPrice);
		TextView lblName = (TextView) convertView.findViewById(R.id.lblName);
		TextView lblNameBox = (TextView) convertView.findViewById(R.id.lblNameBox);
		
		
		if(isExpanded) {
			if(StringUtils.getCurrentLocale().equalsIgnoreCase("nb_NO"))
				lblDate.setText(DateUtils.formatDateNorwayWithLeadingZeros(dataModel.getDate()));
			else
				lblDate.setText(DateUtils.getFormatedTransactionDate(dataModel.getDate(), true));
		} else {
			lblDate.setText(DateUtils.getFormatedTransactionDate(dataModel.getDate(), false));
		}
		
		if(StringUtils.getCurrentLocale().equalsIgnoreCase("nb_NO"))
			lblPrice.setText(StringUtils.roundAndFormatCurrencyNorwayWithEndingZeros(dataModel.getBillingAmount()));			
		else
			lblPrice.setText(StringUtils.roundAndFormatCurrency(dataModel.getBillingAmount()));
		
		if (((TransactionDataModel) getGroup(groupPosition)).getType().equalsIgnoreCase("Credit")) {
			lblName.setVisibility(View.GONE);
			lblNameBox.setVisibility(View.VISIBLE);
			lblNameBox.setText(dataModel.getDescription());
		} else {
			lblName.setVisibility(View.VISIBLE);
			lblNameBox.setVisibility(View.GONE);
			lblName.setText(dataModel.getDescription());
		}
		
		if (groupPosition % 2 == 0) {
			convertView.setBackgroundResource(R.drawable.list_bg_odd);
		} else {
			convertView.setBackgroundResource(R.drawable.list_bg_even);
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
