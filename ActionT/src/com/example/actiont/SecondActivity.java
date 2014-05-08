package com.example.actiont;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;

import com.example.actiont.model.DataModel;

public class SecondActivity extends FragmentActivity {

	private ListView listView;
	private ArrayList<DataModel> arrayList;
	private TransactionsAdapter transactionsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_map_listvew);
		listView = (ListView) findViewById(R.id.listView);
		
		setData();

//		String[] values = new String[] { "Test Data One", "Test Data Two",
//				"Test Data Three", "Test Data Four", "Test Data Five",
//				"Test Data Six", "Test Data Seven", "Test Data Eight" };
//
//		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
//		listView.setAdapter(dataAdapter);
//		dataAdapter.notifyDataSetChanged();
	}

	private void setData() {
		
		arrayList = new ArrayList<>();
		
		for (int i = 0; i < 20; i++) {
			DataModel dataModel = new DataModel();
			dataModel.setId("00" + i);
			dataModel.setDate(i + "/01/2014");
			dataModel.setName("Lorem Ipsum is simply dummy text of the printing");
			dataModel.setPrice(i + "0000");
			arrayList.add(dataModel);
		}
		
		transactionsAdapter = new TransactionsAdapter(SecondActivity.this, 0, arrayList);
		listView.setAdapter(transactionsAdapter);
		transactionsAdapter.notifyDataSetChanged();
	}
}
