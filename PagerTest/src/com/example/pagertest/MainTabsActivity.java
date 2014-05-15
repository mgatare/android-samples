//package com.example.pagertest;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//
//import com.viewpagerindicator.IconPagerAdapter;
//import com.viewpagerindicator.TabPageIndicator;
//
//public class MainTabsActivity extends FragmentActivity {
//    private static final String[] CONTENT = new String[] { "Transaction", "Cards", "Transfer Funds", };
//    private static final int[] ICONS = new int[] {
//            R.drawable.ic_launcher,
//            R.drawable.ic_launcher,
//            R.drawable.ic_launcher,
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.simple_tabs);
//
//        FragmentPagerAdapter adapter = new CustomTabsAdapter(getSupportFragmentManager());
//
//        ViewPager pager = (ViewPager)findViewById(R.id.pager);
//        pager.setAdapter(adapter);
//
//        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
//        indicator.setViewPager(pager);
//    }
///**
// * 
// * @author mgatare
// *
// */
//    private class CustomTabsAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
//        public CustomTabsAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return CONTENT[position % CONTENT.length];
//        }
//
//        @Override public int getIconResId(int index) {
//          return ICONS[index];
//        }
//
//      @Override
//        public int getCount() {
//          return CONTENT.length;
//        }
//    }
//}
