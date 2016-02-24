package com.baoluo.community.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.baoluo.community.ui.R;

public class SlideCutView extends LinearLayout{
	private static final String TAG = "SlideCutView";
	
	private Context mContext;
    private LinearLayout mViewContent;
    private Scroller mScroller;
    private OnSlideListener mOnSlideListener;
    private boolean mIsMoveClick = false;
    private int mHolderWidth = 70;

    private int mLastX = 0;
    private int mLastY = 0;
    private static final int TAN = 2;

    public SlideCutView(Context context) {
        super(context);
        initView();
    }

    public SlideCutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mContext = getContext();
        mScroller = new Scroller(mContext);
        setOrientation(LinearLayout.HORIZONTAL);
        View.inflate(mContext, R.layout.merge_slide_cut_view, this);
        mViewContent = (LinearLayout) findViewById(R.id.ll_content);
        mHolderWidth = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHolderWidth, getResources()
                        .getDisplayMetrics()));
    }

    public void setButtonText(CharSequence text) {
        ((TextView)findViewById(R.id.tv_slide)).setText(text);
    }

    public void setContentView(View view) {
        mViewContent.addView(view);
    }

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        mOnSlideListener = onSlideListener;
    }

    public void shrink() {
        if (getScrollX() != 0) {
            this.smoothScrollTo(0, 0);
        }
        mIsMoveClick = false;
    }

    public void onRequireTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = getScrollX();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            if (mOnSlideListener != null) {
                mOnSlideListener.onSlide(this,
                        OnSlideListener.SLIDE_STATUS_START_SCROLL);
            }
            break;
        }
        case MotionEvent.ACTION_MOVE: {
            int deltaX = x - mLastX;
            int deltaY = y - mLastY;
            if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
                break;
            }
            int newScrollX = scrollX - deltaX;
            if (deltaX != 0) {
            	/*if (newScrollX < 0 && newScrollX < -mHolderWidth) {
                    newScrollX = -mHolderWidth;
                    L.i(TAG, "action move  111111111111");
                } else*/ 
            	if (newScrollX > mHolderWidth) {
                    newScrollX = mHolderWidth;
                }
            	if(deltaX < 0){
            		this.scrollTo(newScrollX, 0);
            	}
            }
            break;
        }
        case MotionEvent.ACTION_UP: {
            int newScrollX = 0;
            if (scrollX - mHolderWidth * 0.75 > 0) {
                newScrollX = mHolderWidth;
                mIsMoveClick = !mIsMoveClick;
            }/*else if (-scrollX - mHolderWidth * 0.75 > 0) {
                newScrollX = -mHolderWidth;
                mIsMoveClick = !mIsMoveClick;
                L.i(TAG, "action up  222222222222");
            }*/else{
            	mIsMoveClick = false;
            }
            this.smoothScrollTo(newScrollX, 0);
            if (mOnSlideListener != null) {
                mOnSlideListener.onSlide(this,
                        newScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF
                                : OnSlideListener.SLIDE_STATUS_ON);
            }
            break;
        }
        default:
            break;
        }

        mLastX = x;
        mLastY = y;
    }

    private void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
    public void open() {
		if(getScrollX() < mHolderWidth){
			int newScrollX = mHolderWidth;
			this.smoothScrollTo(newScrollX, 0);
			if (mOnSlideListener != null) {
				mOnSlideListener.onSlide(this,
						newScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF
								: OnSlideListener.SLIDE_STATUS_ON);
			}
		}else{
			mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, Math.abs(mHolderWidth) * 3);
			invalidate();
		}
	}
	
	public boolean close() {
		if(getScrollX() != 0){
			mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, Math.abs(mHolderWidth) * 3);
			invalidate();
			return false;
		}
		return true;
	}
	
	public void reset() {
		mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 0);
		invalidate();
	}

	public boolean ismIsMoveClick() {
		return mIsMoveClick;
	}
	
	public interface OnSlideListener {
        public static final int SLIDE_STATUS_OFF = 0;
        public static final int SLIDE_STATUS_START_SCROLL = 1;
        public static final int SLIDE_STATUS_ON = 2;

        /**
         * @param view current SlideCutView
         * @param status SLIDE_STATUS_ON or SLIDE_STATUS_OFF
         */
        public void onSlide(View view, int status);
    }

}
