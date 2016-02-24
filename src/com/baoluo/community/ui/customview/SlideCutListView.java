package com.baoluo.community.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * listView 左滑动
 * @author tao.lai
 *
 */
public class SlideCutListView extends ListView{
	
	private static final String TAG = "SlideCutListView";
    
	private SlideCutView mFocusedItemView;

    private int position;
    
    public SlideCutListView(Context context) {
        super(context);
    }

    public SlideCutListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideCutListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void shrinkListItem(int position) {
        View item = getChildAt(position);
        if (item != null) {
            try {
                ((SlideCutView) item).shrink();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(MotionEvent event) {
    	int x = (int) event.getX();
    	int y = (int) event.getY();
    	position = pointToPosition(x, y);
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            if (position != INVALID_POSITION) {
                mFocusedItemView = slideCutListener.getSlideCutView(position);
            }
        }
        default:
            break;
        }
        if (mFocusedItemView != null) {
        	if(position == INVALID_POSITION){
        		mFocusedItemView.shrink();
        		return super.onTouchEvent(event);
        	}
            mFocusedItemView.onRequireTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }
    public int getPosition() {
		return position;
	}
    
    private SlideCutViewListener slideCutListener;
    public void setSlideCutViewListener(SlideCutViewListener slideCutListener){
    	this.slideCutListener = slideCutListener;
    }
    public interface SlideCutViewListener{
    	 SlideCutView getSlideCutView(int position);
    }
}
