//package com.example.pagertest;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnLongClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class TabView extends LinearLayout {
//
//    private ImageView mImageView;
//    private TextView mTextView;
//
//    public TabView(Context context) {
//        this(context, null);
//    }
//
//    public TabView(Context context, AttributeSet attrs) {
//        this(context, attrs, android.R.attr.actionBarTabStyle);
//    }
//
//    public TabView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//
//        LayoutInflater.from(context).inflate(R.layout.tab_view_merge, this);
//
//        mImageView = (ImageView) findViewById(R.id.image);
//        mTextView = (TextView) findViewById(R.id.text);
//    }
//
//    public void setIcon(int resId) {
//        setIcon(getContext().getResources().getDrawable(resId));
//    }
//
//    public void setIcon(Drawable icon) {
//        if (icon != null) {
//            mImageView.setVisibility(View.VISIBLE);
//            mImageView.setImageDrawable(icon);
//        } else {
//            mImageView.setImageResource(View.GONE);
//        }
//    }
//
//    public void setText(int resId) {
//        setText(getContext().getString(resId));
//    }
//
//    public void setText(CharSequence text) {
//        if (!TextUtils.isEmpty(text)) {
//            if (mTextView != null) {
//                mTextView.setVisibility(View.VISIBLE);
//                mTextView.setText(text);
//            }
//        } else {
//            if (mTextView != null) {
//                mTextView.setVisibility(View.GONE);
//            }
//        }
//        updateHint();
//    }
//
//   @Override
//    public void setContentDescription(CharSequence contentDescription) {
//        super.setContentDescription(contentDescription);
//        updateHint();
//    }
//
//    private void updateHint() {
//        boolean needHint = false;
//        if (mTextView == null || mTextView.getVisibility() == View.GONE) {
//            if (!TextUtils.isEmpty(getContentDescription())) {
//                needHint = true;
//            } else {
//                needHint = false;
//            }
//        }
//
//        if (needHint) {
//            setOnLongClickListener(mOnLongClickListener);
//        } else {
//            setOnLongClickListener(null);
//            setLongClickable(false);
//        }
//    }
//
//    private OnLongClickListener mOnLongClickListener = new OnLongClickListener() {
//       @Override
//        public boolean onLongClick(View v) {
//            final int[] screenPos = new int[2];
//            getLocationOnScreen(screenPos);
//
//            final Context context = getContext();
//            final int width = getWidth();
//            final int height = getHeight();
//            final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
//
//            Toast cheatSheet = Toast.makeText(context, getContentDescription(), Toast.LENGTH_SHORT);
//
//            // Show under the tab
//            cheatSheet.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, (screenPos[0] + width / 2) - screenWidth / 2,
//                    height);
//
//            cheatSheet.show();
//            return true;
//        }
//    };
//}
