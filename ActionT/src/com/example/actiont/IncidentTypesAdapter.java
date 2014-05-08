//package com.example.actiont;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.ptpprotocol.app.ui.R;
//import com.ptpprotocol.app.ui.RecordVideoActivity;
//import com.ptpprotocol.models.IncidentModel;
//
///**
// * The Class AllTripsAdapter.
// */
//public class IncidentTypesAdapter extends ArrayAdapter<IncidentModel> {
//	private List<IncidentModel> list = new ArrayList<IncidentModel>();
//
//	private Activity context;
//	private List<Integer> listIncidentIndex = new ArrayList<Integer>();
//	int index = -1;
//
//	public IncidentTypesAdapter(Activity context, int item,
//			List<IncidentModel> data) {
//		super(context, item, data);
//		this.context = context;
//		list.addAll(data);
//	}
//
//	public void setPosition(int position) {
//		index = position;
//		listIncidentIndex.add(index);
//	}
//
//	@SuppressLint({ "ResourceAsColor", "NewApi" })
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// final String rowData = list.get(position);
//		final ViewHolder holder;
//
//		if (convertView == null) {
//			LayoutInflater llinflator = (LayoutInflater) context
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = llinflator.inflate(R.layout.row_addincident, parent,
//					false);
//
//			convertView.invalidate();
//			convertView.refreshDrawableState();
//
//			holder = new ViewHolder();
//			holder.llMainOne = (RelativeLayout) convertView
//					.findViewById(R.id.llMainOne);
//			holder.lblIncidentName = (TextView) holder.llMainOne
//					.findViewById(R.id.lblIncidentName);
//			holder.imgChecked = (ImageView) holder.llMainOne
//					.findViewById(R.id.imgChecked);
//
//			convertView.setTag(holder);
//
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//
//		if (RecordVideoActivity.listIncidentType.contains(getItem(position)
//				.getName())) {
//			holder.imgChecked.setVisibility(View.VISIBLE);
//		} else {
//			holder.imgChecked.setVisibility(View.GONE);
//
//		}
//		holder.lblIncidentName.setText(getItem(position).getName());
//
//		return convertView;
//	}
//}
//
///**
// * The Class ViewHolder.
// */
//class ViewHolder {
//	RelativeLayout llMainOne;
//	TextView lblIncidentName;
//	ImageView imgChecked;
//}
