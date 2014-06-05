//package com.entercard.coop.fragment;
//
//import android.app.Activity;
//import android.content.SharedPreferences;
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.v4.app.ActionBarDrawerToggle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.ActionBarActivity;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import com.entercard.coop.HomeScreenActivity;
//import com.entercard.coop.R;
//
//public class NavigationDrawerFragment extends Fragment {
//
//	private static final String STATE_SELECTED_POSITION = "state_selected_position";
//	private static final String PREF_USER_LEARNED_DRAWER = "pref_user_learned_drawer";
//
//	private NavigationDrawerCallbacks navigationDrawerCallbacks;
//
//	private ActionBarDrawerToggle actionBarDrawerToggle;
//
//	private DrawerLayout drawerLayout;
//	private ListView drawerItemsListView;
//	private View fragmentContainerView;
//	private View profileView;
//
//	private int currentSelectedPosition = 0;
//	private boolean fromSavedInstanceState;
//	private boolean userLearnedDrawer;
//	private HomeScreenActivity parentActivity;
//
//	public NavigationDrawerFragment() {
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		parentActivity = (HomeScreenActivity) getActivity();
//		
//		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
//		userLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
//
//		// Select either the default item (0) or the last selected item.
//		//selectItem(currentSelectedPosition);
//	}
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		setHasOptionsMenu(true);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//		View rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
//		
//		profileView = (View) rootView.findViewById(R.id.profileView);
//		profileView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				getActivity().finish();
//			}
//		});
//		//TextView accountNameTextView = (TextView)profileView.findViewById(R.id.accountNameTextView);
//		//accountNameTextView.setText(HomeScreenActivity.accountName !=null ?HomeScreenActivity.accountName :"Account Number");
//		
//		drawerItemsListView = (ListView) rootView.findViewById(R.id.listTabOptions);
//		drawerItemsListView.setSelector(R.drawable.list_selector);
//		drawerItemsListView.setOnItemClickListener(new ListItemClickListener());
//		drawerItemsListView.setAdapter(new ArrayAdapter<String>(getActionBar()
//				.getThemedContext(), R.layout.row_textview,
//					R.id.textView1, new String[] {
//						getString(R.string.option_transactions),
//						getString(R.string.option_transfer_funds),
//						getString(R.string.option_credit_line_increase), }));
//		//drawerItemsListView.setItemChecked(currentSelectedPosition, true);
//		return rootView;
//	}
//
//	public boolean isDrawerOpen() {
//		return drawerLayout != null && drawerLayout.isDrawerOpen(fragmentContainerView);
//	}
//
//	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
//		fragmentContainerView = getActivity().findViewById(fragmentId);
//		this.drawerLayout = drawerLayout;
//
//		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
//
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		actionBar.setHomeButtonEnabled(true);
//
//		actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(),
//				drawerLayout, R.drawable.ic_navigation_drawer,
//				R.string.navigation_drawer_open,
//				R.string.navigation_drawer_close) {
//			@Override
//			public void onDrawerClosed(View drawerView) {
//				super.onDrawerClosed(drawerView);
//				if (!isAdded()) {
//					return;
//				}
//				getActivity().supportInvalidateOptionsMenu(); 
//			}
//
//			@Override
//			public void onDrawerOpened(View drawerView) {
//				super.onDrawerOpened(drawerView);
//				if (!isAdded()) {
//					return;
//				}
//
//				if (!userLearnedDrawer) {
//					userLearnedDrawer = true;
//					SharedPreferences sp = PreferenceManager
//							.getDefaultSharedPreferences(getActivity());
//					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
//							.commit();
//				}
//				getActivity().supportInvalidateOptionsMenu();
//			}
//		};
//		if (!userLearnedDrawer && !fromSavedInstanceState) {
//			drawerLayout.openDrawer(fragmentContainerView);
//		}
//
//		// Defer code dependent on restoration of previous instance state.
//		drawerLayout.post(new Runnable() {
//			@Override
//			public void run() {
//				actionBarDrawerToggle.syncState();
//			}
//		});
//
//		drawerLayout.setDrawerListener(actionBarDrawerToggle);
//	}
//
//	private void selectItem(int position) {
//		currentSelectedPosition = position;
//		if (drawerItemsListView != null) {
//			drawerItemsListView.setItemChecked(position, true);
//		}
//		if (drawerLayout != null) {
//			drawerLayout.closeDrawer(fragmentContainerView);
//		}
//		if (navigationDrawerCallbacks != null) {
//			navigationDrawerCallbacks.onNavigationDrawerItemSelected(position);
//		}
//	}
//
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		try {
//			navigationDrawerCallbacks = (NavigationDrawerCallbacks) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(
//					"Activity must implement NavigationDrawerCallbacks.");
//		}
//	}
//
//	@Override
//	public void onDetach() {
//		super.onDetach();
//		navigationDrawerCallbacks = null;
//	}
//
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		outState.putInt(STATE_SELECTED_POSITION, currentSelectedPosition);
//	}
//
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//		actionBarDrawerToggle.onConfigurationChanged(newConfig);
//	}
//
//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		if (drawerLayout != null && isDrawerOpen()) {
//			showGlobalContextActionBar();
//		}
//		super.onCreateOptionsMenu(menu, inflater);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
//			return true;
//		}
//
//		return super.onOptionsItemSelected(item);
//	}
//
//	private void showGlobalContextActionBar() {
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayShowTitleEnabled(true);
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//	}
//
//	private ActionBar getActionBar() {
//		return ((ActionBarActivity) getActivity()).getSupportActionBar();
//	}
//
//	public static interface NavigationDrawerCallbacks {
//		void onNavigationDrawerItemSelected(int position);
//	}
//
//	private class ListItemClickListener implements AdapterView.OnItemClickListener {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//			
//			selectItem(position);
//			FragmentManager fragmentManager = parentActivity.getSupportFragmentManager();
//			
//			switch (position) {
//			case 0:
//				fragmentManager.popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//				break;
//			case 1:
//				if(null == fragmentManager.findFragmentByTag("transfer_fragment"))
//					fragmentManager.beginTransaction().replace(R.id.navgationContainer, new TransferFundsFragment(), "transfer_fragment")
//							.addToBackStack("transfer_fragment")
//							.commit();
//				 else 
//					fragmentManager.beginTransaction()
//							.replace(R.id.navgationContainer, fragmentManager.findFragmentByTag("transfer_fragment"), "transfer_fragment")
//							.commit();
//				break;
//			case 2:
//				if (null == fragmentManager.findFragmentByTag("card_fragment")) 
//					fragmentManager.beginTransaction()
//							.replace(R.id.navgationContainer, new CardsFragment(),"card_fragment")
//							.addToBackStack("card_fragment")
//							.commit();
//				 else 
//					fragmentManager.beginTransaction()
//							.replace(R.id.navgationContainer,  fragmentManager.findFragmentByTag("card_fragment"),"card_fragment")
//							.commit();
//				break;
//			default:
//				break;
//			}
//		}
//	}
//}
