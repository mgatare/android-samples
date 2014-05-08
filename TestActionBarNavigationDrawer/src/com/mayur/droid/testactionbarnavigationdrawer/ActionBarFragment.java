package com.mayur.droid.testactionbarnavigationdrawer;
//package com.example.testactionbarnavigationdrawer;
//
//import android.annotation.TargetApi;
//import android.app.ActionBar;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//
//public class ActionBarFragment extends Fragment {
//
////	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
////	@Override
////	protected void onCreate(Bundle savedInstanceState) {
////		super.onCreate(savedInstanceState);
////		setContentView(R.layout.activity_actionbarfragment_two);
////
////		final ActionBar actionBar = getActionBar();
////		actionBar.setDisplayHomeAsUpEnabled(true);
////	}
//
//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View rootView = inflater.inflate(R.layout.activity_actionbarfragment_two, container, false);
//
//		final ActionBar actionBar = getActivity().getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		return rootView;
//	}
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			/*Intent upIntent = new Intent(getActivity(), HomeScreenActivity.class);
//			if (NavUtils.shouldUpRecreateTask(getActivity(), upIntent)) {
//				TaskStackBuilder.from(getActivity()).addNextIntent(upIntent).startActivities();
//				finish();
//			} else {
//				NavUtils.navigateUpTo(getActivity(), upIntent);
//			}*/
//			FragmentManager fragmentManager = getFragmentManager();
//			fragmentManager.popBackStackImmediate();
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//}
