//package com.entercard.coopmedlem.view;
//
//import com.entercard.coopmedlem.R;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AbsListView;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.AbsListView.OnScrollListener;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//
//// TODO: Auto-generated Javadoc
///**
// * The Class LoadMoreListView.
// */
//public class LoadMoreListView extends ListView implements OnScrollListener {
//
//	/** The Constant TAG. */
//	private static final String TAG = "LoadMoreListView";
//	
//	/** The on scroll listener. */
//	private OnScrollListener onScrollListener;
//	
//	/** The layout inflater. */
//	private LayoutInflater layoutInflater;
//
//	/** The relative layout footer. */
//	private RelativeLayout relativeLayoutFooter;
//	
//	/** The progress bar load more. */
//	private ProgressBar progressBarLoadMore;
//
//	/** The on load more listener. */
//	private OnLoadMoreListener onLoadMoreListener;
//	
//	/** The is loading more. */
//	private boolean isLoadingMore = false;
//	
//	/** The current scroll state. */
//	private int currentScrollState;
//
//	/**
//	 * Instantiates a new load more list view.
//	 *
//	 * @param context the context
//	 */
//	public LoadMoreListView(Context context) {
//		super(context);
//		init(context);
//	}
//
//	/**
//	 * Instantiates a new load more list view.
//	 *
//	 * @param context the context
//	 * @param attrs the attrs
//	 */
//	public LoadMoreListView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		init(context);
//	}
//
//	/**
//	 * Instantiates a new load more list view.
//	 *
//	 * @param context the context
//	 * @param attrs the attrs
//	 * @param defStyle the def style
//	 */
//	public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		init(context);
//	}
//
//	/**
//	 * Inits the.
//	 *
//	 * @param context the context
//	 */
//	private void init(Context context) {
//
//		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//		relativeLayoutFooter = (RelativeLayout) layoutInflater.inflate(R.layout.footer_load_more_listview, this, false);
//		progressBarLoadMore = (ProgressBar) relativeLayoutFooter.findViewById(R.id.load_more_progressBar);
//
//		addFooterView(relativeLayoutFooter);
//
//		super.setOnScrollListener(this);
//	}
//
//	/* (non-Javadoc)
//	 * @see android.widget.ListView#setAdapter(android.widget.ListAdapter)
//	 */
//	@Override
//	public void setAdapter(ListAdapter adapter) {
//		super.setAdapter(adapter);
//	}
//
//	/**
//	 * Set the listener that will receive notifications every time the list
//	 * scrolls.
//	 * 
//	 * @param l
//	 *            The scroll listener.
//	 */
//	@Override
//	public void setOnScrollListener(AbsListView.OnScrollListener l) {
//		onScrollListener = l;
//	}
//
//	/**
//	 * Register a callback to be invoked when this list reaches the end (last
//	 * item be visible).
//	 *
//	 * @param onLoadMoreListener            The callback to run.
//	 */
//
//	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
//		this.onLoadMoreListener = onLoadMoreListener;
//	}
//
//	/* (non-Javadoc)
//	 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.AbsListView, int, int, int)
//	 */
//	public void onScroll(AbsListView view, int firstVisibleItem,
//			int visibleItemCount, int totalItemCount) {
//
//		if (onScrollListener != null) {
//			onScrollListener.onScroll(view, firstVisibleItem,
//					visibleItemCount, totalItemCount);
//		}
//
//		if (onLoadMoreListener != null) {
//
//			if (visibleItemCount == totalItemCount) {
//				progressBarLoadMore.setVisibility(View.GONE);
//				return;
//			}
//
//			boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
//
//			if (!isLoadingMore && loadMore
//					&& currentScrollState != SCROLL_STATE_IDLE) {
//				progressBarLoadMore.setVisibility(View.VISIBLE);
//				isLoadingMore = true;
//				onLoadMore();
//			}
//
//		}
//
//	}
//
//	/* (non-Javadoc)
//	 * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
//	 */
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//		// bug fix: listview was not clickable after scroll
//		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
//			view.invalidateViews();
//		}
//
//		currentScrollState = scrollState;
//
//		if (onScrollListener != null) {
//			onScrollListener.onScrollStateChanged(view, scrollState);
//		}
//
//	}
//
//	/**
//	 * On load more.
//	 */
//	public void onLoadMore() {
//		Log.d(TAG, "onLoadMore");
//		if (onLoadMoreListener != null) {
//			onLoadMoreListener.onLoadMore();
//		}
//	}
//
//	/**
//	 * Notify the loading more operation has finished.
//	 */
//	public void onLoadMoreComplete() {
//		isLoadingMore = false;
//		progressBarLoadMore.setVisibility(View.GONE);
//	}
//
//	/**
//	 * Interface definition for a callback to be invoked when list reaches the
//	 * last item (the user load more items in the list).
//	 *
//	 * @see OnLoadMoreEvent
//	 */
//	public interface OnLoadMoreListener {
//		
//		/**
//		 * Called when the list reaches the last item (the last item is visible
//		 * to the user).
//		 */
//		public void onLoadMore();
//	}
//
//}