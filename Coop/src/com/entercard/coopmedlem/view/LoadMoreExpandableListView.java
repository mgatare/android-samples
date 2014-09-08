package com.entercard.coopmedlem.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.entercard.coopmedlem.R;

// TODO: Auto-generated Javadoc
/**
 * The Class LoadMoreExpandableListView.
 */
public class LoadMoreExpandableListView extends ExpandableListView implements
		OnScrollListener {

	/** The Constant TAG. */
	private static final String TAG = "LoadMoreListView";

	/**
	 * Listener that will receive notifications every time the list scrolls.
	 */
	private OnScrollListener mOnScrollListener;
	
	/** The m inflater. */
	private LayoutInflater mInflater;

	// footer view
	/** The m footer view. */
	private RelativeLayout mFooterView;
	// private TextView mLabLoadMore;
	/** The m progress bar load more. */
	private ProgressBar mProgressBarLoadMore;

	// Listener to process load more items when user reaches the end of the list
	/** The m on load more listener. */
	private OnLoadMoreListener mOnLoadMoreListener;
	// To know if the list is loading more items
	/** The m is loading more. */
	private boolean mIsLoadingMore = false;
	
	/** The m current scroll state. */
	private int mCurrentScrollState;

	/**
	 * Instantiates a new load more expandable list view.
	 *
	 * @param context the context
	 */
	public LoadMoreExpandableListView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * Instantiates a new load more expandable list view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public LoadMoreExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * Instantiates a new load more expandable list view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public LoadMoreExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * Inits the.
	 *
	 * @param context the context
	 */
	private void init(Context context) {

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// footer
		mFooterView = (RelativeLayout) mInflater.inflate(R.layout.footer_load_more_listview, this, false);
		/*
		 * mLabLoadMore = (TextView) mFooterView
		 * .findViewById(R.id.load_more_lab_view);
		 */
		mProgressBarLoadMore = (ProgressBar) mFooterView.findViewById(R.id.load_more_progressBar);

		addFooterView(mFooterView);

		super.setOnScrollListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListView#setAdapter(android.widget.ExpandableListAdapter)
	 */
	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		super.setAdapter(adapter);
	}

	/**
	 * Set the listener that will receive notifications every time the list
	 * scrolls.
	 * 
	 * @param l
	 *            The scroll listener.
	 */
	@Override
	public void setOnScrollListener(AbsListView.OnScrollListener l) {
		mOnScrollListener = l;
	}

	/**
	 * Register a callback to be invoked when this list reaches the end (last
	 * item be visible).
	 *
	 * @param onLoadMoreListener            The callback to run.
	 */

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		mOnLoadMoreListener = onLoadMoreListener;
	}

	/* (non-Javadoc)
	 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.AbsListView, int, int, int)
	 */
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}

		if (mOnLoadMoreListener != null) {

			if (visibleItemCount == totalItemCount) {
				mProgressBarLoadMore.setVisibility(View.GONE);
				return;
			}

			boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

			if (!mIsLoadingMore && loadMore && mCurrentScrollState != SCROLL_STATE_IDLE) {
				mProgressBarLoadMore.setVisibility(View.VISIBLE);
				mIsLoadingMore = true;
				onLoadMore();
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
	 */
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mCurrentScrollState = scrollState;

		// bug fix: listview was not clickable after scroll
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			view.invalidateViews();
		}

		mCurrentScrollState = scrollState;

		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}

	}

	/**
	 * On load more.
	 */
	public void onLoadMore() {
		Log.d(TAG, "onLoadMore");
		if (mOnLoadMoreListener != null) {
			mOnLoadMoreListener.onLoadMore();
		}
	}

	/**
	 * Notify the loading more operation has finished.
	 */
	public void onLoadMoreComplete() {
		mIsLoadingMore = false;
		mProgressBarLoadMore.setVisibility(View.GONE);
		//removeFooterView(mFooterView);
	}

	/**
	 * Interface definition for a callback to be invoked when list reaches the
	 * last item (the user load more items in the list).
	 *
	 * @see OnLoadMoreEvent
	 */
	public interface OnLoadMoreListener {
		
		/**
		 * Called when the list reaches the last item (the last item is visible
		 * to the user).
		 */
		public void onLoadMore();
	}
}