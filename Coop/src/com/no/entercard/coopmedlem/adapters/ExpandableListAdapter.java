package com.no.entercard.coopmedlem.adapters;

import java.util.ArrayList;
import java.util.Currency;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
	private Handler handler;
	private boolean isVenueAddressValid = false;
	
	public ExpandableListAdapter(Context context, ArrayList<TransactionDataModel> listDataHeader) {
		this.context = context;
		this.listTrsansactionHeader.addAll(listDataHeader);
		
		imageLoader = new ImageLoader(context);
		handler = new Handler();
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
		
		String city = null;
		String country = null;
		String description = null;
		
		if(null!=dataModel.getCity())
			city = StringUtils.trimStringOnly(dataModel.getCity());
		if(null!=dataModel.getCountry())
			country = StringUtils.trimStringOnly(dataModel.getCountry());
		if(null!=dataModel.getDescription())
			description = StringUtils.trimStringOnly(dataModel.getDescription());
		
//		getAddressFromLocation(context, description, imgMarker, city, country, imgMap);
		
		//
		if (null != country || null != city || null != description)
			imgMarker.setVisibility(View.VISIBLE);
		else
			imgMarker.setVisibility(View.GONE);
		
		isVenueAddressValid = Utils.getGeoCodedForVenueAddress(description, context);
		
		if(isVenueAddressValid) {
			
			imgMarker.setVisibility(View.VISIBLE);
			
			//Make HTTP call
			String strURL = Utils.getMapThumbnailURL(city, country, description);
			Log.i("COOP","URL>>>>>"+strURL);
			if(!TextUtils.isEmpty(strURL))
				imageLoader.DisplayImage(strURL, imgMap);
			
		} else {
			imgMarker.setVisibility(View.GONE);
			
			//Make HTTP call
			String strURL = Utils.getMapThumbnailURL(city, country, null);
			Log.i("COOP","URL>>>>>"+strURL);
			if(!TextUtils.isEmpty(strURL))
				imageLoader.DisplayImage(strURL, imgMap);
		}
		
		btnDisputeOnMap.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, DisputeTransactionActivity.class);
				intent.putExtra("amount", dataModel.getBillingAmount());
				intent.putExtra("desc", dataModel.getDescription());
				intent.putExtra("date", dataModel.getDate());
				intent.putExtra("id", dataModel.getId());
				//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
		
		if(isExpanded) {
			
			if(StringUtils.getCurrentLocale().equalsIgnoreCase("nb_NO") && !dataModel.getCurrency().equalsIgnoreCase("NOK")) {
				
				lblPrice.setText(getSymbolForCurrenctCode(dataModel.getCurrency(), dataModel.getAmount()));
				
			} else if(StringUtils.getCurrentLocale().equalsIgnoreCase("nb_SV") && !dataModel.getCurrency().equalsIgnoreCase("SEK")) {
				
				lblPrice.setText(getSymbolForCurrenctCode(dataModel.getCurrency(), dataModel.getAmount()));
				
			} else if(StringUtils.getCurrentLocale().equalsIgnoreCase("nb_DN") && !dataModel.getCurrency().equalsIgnoreCase("DKK")) {
				
				lblPrice.setText(getSymbolForCurrenctCode(dataModel.getCurrency(), dataModel.getAmount()));
				
			} else {
				lblPrice.setText(StringUtils.roundAndFormatCurrency(dataModel.getBillingAmount()));
			}
		} else {
			
			if(StringUtils.getCurrentLocale().equalsIgnoreCase("nb_NO"))
				lblPrice.setText(StringUtils.roundAndFormatCurrencyNorwayWithEndingZeros(dataModel.getBillingAmount()));			
			else
				lblPrice.setText(StringUtils.roundAndFormatCurrency(dataModel.getBillingAmount()));
		}

		
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

	private String getSymbolForCurrenctCode(String code, String price) {
		Currency currency = Currency.getInstance(code);
		String symbol = currency.getSymbol().replace(code, "");
		return symbol+price;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	/**
	 * 
	 * @param context
	 * @param place
	 * @param imgMarker
	 * @param city
	 * @param country
	 * @param imgMap
	 */
//	public void getAddressFromLocation(final Context context,final String place, final ImageView imgMarker, final String city,
//			final String country, final ImageView imgMap) {
//		
//		Thread thread = new Thread() {
//			@Override
//			public void run() {
//				//Initialize the GeopCoder to verify the Venue for the transation
//				Locale locale = context.getResources().getConfiguration().locale;
//				Geocoder geocoder = new Geocoder(context, locale);
//				
//				String urlTxt = null;
//				String resultTxt = null;
//				double latitude = 0.0;
//				double longitude = 0.0;
//
//				try {
//					List<Address> list = geocoder.getFromLocationName(place, 1);
//					if (list != null && list.size() > 0) {
//
//						Address location = list.get(0);
//						latitude = location.getLatitude();
//						longitude = location.getLongitude();
//
//						resultTxt = location.getCountryName();
//						Log.e("TAG", "------result--" + resultTxt +"-----"+place);
//					}
//				} catch (IOException e) {
//					Log.e("TAG","-------Impossible to connect to Geocoder--", e);
//					handler.post(new Runnable() {
//						@Override
//						public void run() {
//							imgMarker.setVisibility(View.GONE);						
//						}
//					});
//					
//				} finally {
//					if (resultTxt != null && (latitude != 0.0 || longitude != 0.0)) {
//
//						// The Venue description is VALID so show marker on Map
//						Log.e("TAG", "---------------milaaaaaaaaaaaaaaaaa--");
//						handler.post(new Runnable() {
//							@Override
//							public void run() {
//								imgMarker.setVisibility(View.VISIBLE);					
//							}
//						});
//						
//						// Make HTTP call
//						urlTxt = Utils.getMapThumbnailURL(city, country, place);
//						Log.i("COOP", "URL>>>>>" + urlTxt);
//
//					} else {
//
//						// Venue description is INVALID so No Marker shown
//						Log.e("TAG", "---------------nahiiiiiiiiiiiiiiiiii--");
//						
//						handler.post(new Runnable() {
//							@Override
//							public void run() {
//								imgMarker.setVisibility(View.GONE);				
//							}
//						});
//						
//						// Make HTTP call
//						urlTxt = Utils.getMapThumbnailURL(city, country, null);
//						Log.i("COOP", "URL>>>>>" + urlTxt);
//					}
//					
//					//Update the Image view with the MAP
//					final String mapImageURLTxt = urlTxt;
//					handler.post(new Runnable() {
//						@Override
//						public void run() {
//							imageLoader.DisplayImage(mapImageURLTxt, imgMap);							
//						}
//					});
//				}
//			}
//		};
//		thread.start();
//	}
}
